package com.wdiscute.starcatcher.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.fish.FishApi;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.data.network.CBFishingStartedPayload;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.command.EnumArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SCCommands
{
    DynamicCommandExceptionType ERROR_ROD = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.rod_not_found", o)
    );

    DynamicCommandExceptionType NOTHING_THERE = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.nothing_there")
    );

    DynamicCommandExceptionType ERROR_EMPTY = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.item_empty", o)
    );

    DynamicCommandExceptionType ERROR_FISH_ENTRY_INVALID = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.fish_entry_not_found", o)
    );

    DynamicCommandExceptionType ERROR_MODIFIER_INVALID = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.modifier_not_found", o)
    );


    static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context)
    {
        dispatcher.register(Commands.literal("starcatcher")
                .requires(sourceStack -> sourceStack.hasPermission(2))

                //starcatcher simulate_fish starcatcher:aurora
                .then(Commands.literal("simulate_fish")
                        .executes(c ->
                                startMinigame(
                                        c.getSource().getPlayerOrException()
                                )
                        )
                )


                //starcatcher simulate_fish starcatcher:aurora
                .then(Commands.literal("simulate_fish")
                        .then(Commands.argument("fish_entry", ResourceArgument.resource(context, Starcatcher.FISH_REGISTRY_KEY))
                                .executes(c ->
                                        startMinigameFromFP(
                                                c.getSource().getPlayerOrException(),
                                                ResourceArgument.getResource(c, "fish_entry", Starcatcher.FISH_REGISTRY_KEY).unwrap().left().get()
                                        )
                                )
                        )
                )


                //starcatcher set_data 100 100 50 epic true
                .then(Commands.literal("set_data")
                        .then(Commands.argument("size_in_cm", IntegerArgumentType.integer())
                                .then(Commands.argument("weight_in_grams", IntegerArgumentType.integer())
                                        .then(Commands.argument("size", FloatArgumentType.floatArg())
                                                .then(Commands.argument("weight", FloatArgumentType.floatArg())
                                                        .then(Commands.argument("percentile", IntegerArgumentType.integer(0, 100))
                                                                .then(Commands.argument("rarity", EnumArgument.enumArgument(Rarity.class))
                                                                        .executes(c ->
                                                                                setDataOnStack(
                                                                                        c.getSource().getPlayerOrException(),
                                                                                        FloatArgumentType.getFloat(c, "size"),
                                                                                        FloatArgumentType.getFloat(c, "weight"),
                                                                                        IntegerArgumentType.getInteger(c, "percentile"),
                                                                                        Rarity.valueOf(c.getArgument("rarity", Rarity.class).toString())
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )

                //starcatcher add_tackle_skin starcatcher:ignore_daytime_and_weather_restrictions
                .then(Commands.literal("set_tackle_skin")
                        .then(Commands.argument("tackle_skin", ResourceArgument.resource(context, Starcatcher.TACKLE_SKIN))
                                .executes(c ->
                                        setTackleSkin(
                                                c.getSource().getPlayerOrException(),
                                                ResourceArgument.getResource(c, "tackle_skin", Starcatcher.TACKLE_SKIN).unwrap().left().get()
                                        )
                                )
                        )
                )

                //starcatcher award_fish ...
                .then(Commands.literal("award_fish")
                        .executes(c -> awardAllFish(c.getSource().getPlayerOrException()))
                        // -> /starcatcher award_fish all
                        .then(Commands.literal("_all")
                                // -> /starcatcher award_fish all
                                .executes(c -> awardAllFish(c.getSource().getPlayerOrException()))
                                // -> /starcatcher award_fish all 0 0 0
                                .then(Commands.argument("ticks", IntegerArgumentType.integer())
                                        .then(Commands.argument("percentile", FloatArgumentType.floatArg())
                                                .then(Commands.argument("golden", BoolArgumentType.bool())
                                                        .executes(c -> awardAllFish(
                                                                        c.getSource().getPlayerOrException(),
                                                                        IntegerArgumentType.getInteger(c, "ticks"),
                                                                        FloatArgumentType.getFloat(c, "percentile"),
                                                                        BoolArgumentType.getBool(c, "golden")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )

                        // -> /starcatcher award_fish random
                        .then(Commands.literal("_random")
                                // -> /starcatcher award_fish all
                                .executes(c -> awardRandomFish(c.getSource().getPlayerOrException()))

                                .then(Commands.argument("ticks", IntegerArgumentType.integer())
                                        .then(Commands.argument("percentile", FloatArgumentType.floatArg())
                                                .then(Commands.argument("golden", BoolArgumentType.bool())
                                                                .executes(c -> awardRandomFish(
                                                                                c.getSource().getPlayerOrException(),
                                                                                IntegerArgumentType.getInteger(c, "ticks"),
                                                                                FloatArgumentType.getFloat(c, "percentile"),
                                                                                BoolArgumentType.getBool(c, "golden")
                                                                        )
                                                                )
                                                )
                                        )
                                )
                        )

                        // -> starcatcher award_fish starcatcher:aurora
                        .then(Commands.argument("fish", ResourceArgument.resource(context, Starcatcher.FISH_REGISTRY_KEY))
                                .executes(c -> awardFish(
                                        c.getSource().getPlayerOrException(),
                                        ResourceArgument.getResource(c, "fish", Starcatcher.FISH_REGISTRY_KEY).unwrap().left().get(),
                                        0, 0, false
                                ))
                                // -> /starcatcher award_fish 123, 123, 132, 0, false
                                .then(Commands.argument("ticks", IntegerArgumentType.integer())
                                        .then(Commands.argument("percentile", FloatArgumentType.floatArg())
                                                .then(Commands.argument("golden", BoolArgumentType.bool())
                                                        .executes(c -> awardFish(
                                                                        c.getSource().getPlayerOrException(),
                                                                        ResourceArgument.getResource(c, "fish", Starcatcher.FISH_REGISTRY_KEY).key(),
                                                                        IntegerArgumentType.getInteger(c, "ticks"),
                                                                        FloatArgumentType.getFloat(c, "percentile"),
                                                                        BoolArgumentType.getBool(c, "golden")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )

                //starcatcher revoke_fish ...
                .then(Commands.literal("revoke_fish")
                        // -> /starcatcher revoke_fish
                        .executes(c -> revokeAllFish(c.getSource().getPlayerOrException()))
                        // -> /starcatcher revoke_fish all
                        .then(Commands.literal("all").executes(c -> revokeAllFish(c.getSource().getPlayerOrException())))
                        // -> starcatcher revoke_fish starcatcher:aurora
                        .then(Commands.argument("fish", ResourceArgument.resource(context, Starcatcher.FISH_REGISTRY_KEY))
                                .executes(c -> revokeFish(
                                        c.getSource().getPlayerOrException(),
                                        ResourceArgument.getResource(c, "fish", Starcatcher.FISH_REGISTRY_KEY).key()
                                ))
                        )

                )
        );
    }

    private static int revokeAllFish(ServerPlayer player)
    {
        FishingGuideAttachment.getFishesCaught(player).clear();
        FishingGuideAttachment.sync(player);
        return 0;
    }

    private static int revokeFish(ServerPlayer player, ResourceKey<FishProperties> fish)
    {
        FishingGuideAttachment.getFishesCaught(player).remove(fish.location());
        FishingGuideAttachment.sync(player);
        return 0;
    }

    private static int awardAllFish(ServerPlayer player, int ticks, float percentile, boolean golden)
    {
        for (FishProperties fp : player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            FishCaughtCounter.awardFishCaughtCounter(fp, null, player, ticks, percentile,
                    false, false, golden, false);

        return 0;
    }

    private static int awardAllFish(ServerPlayer player)
    {
        for (FishProperties fp : player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            FishCaughtCounter.awardFishCaughtCounter(fp, null, player, 0, 0,
                    false, false, false, false);

        return 0;
    }

    private static int awardRandomFish(ServerPlayer player)
    {
        awardRandomFish(player, 0, 0, false);
        return 0;
    }

    private static int awardRandomFish(ServerPlayer player, int ticks, float percentile, boolean golden)
    {
        List<FishProperties> list = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).stream().toList();

        if (list.isEmpty()) return 0;

        FishProperties fp = list.get(Utils.r.nextInt(list.size()));

        FishCaughtCounter.awardFishCaughtCounter(fp, null, player,
                ticks, percentile, false, false, golden, true);
        return 0;
    }

    private static int awardFish(ServerPlayer player, ResourceKey<FishProperties> fish, int ticks, float percentile, boolean golden) throws CommandSyntaxException
    {
        Optional<FishProperties> optional = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getOptional(fish);
        if (optional.isPresent())
            FishCaughtCounter.awardFishCaughtCounter(optional.get(), null, player, ticks, percentile, false, false, golden, true);
        else
            throw ERROR_FISH_ENTRY_INVALID.create(fish);
        return 0;
    }

    private static int setTackleSkin(ServerPlayer player, ResourceKey<AbstractTackleSkin> tackleSkin) throws CommandSyntaxException
    {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(SCTags.RODS)) throw ERROR_ROD.create(null);

        SCDataComponents.set(stack, SCDataComponents.TACKLE_SKIN, Starcatcher.TACKLE_SKIN_REGISTRY.get(tackleSkin));

        return 1;
    }

    private static int startMinigame(ServerPlayer player) throws CommandSyntaxException
    {
        if (!player.getMainHandItem().is(SCTags.RODS)) throw ERROR_ROD.create(null);

        List<FishProperties> available = new ArrayList<>();
        for (FishProperties fp : player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
        {
            if (fp.calculateChance(player, player.level(), player.getMainHandItem(), AbstractFishRestriction.Context.COMMAND) > 0)
                available.add(fp);
        }

        if (!available.isEmpty())
        {
            FishProperties fpToFish = available.get(Utils.r.nextInt(available.size()));
            PacketDistributor.sendToPlayer(player, new CBFishingStartedPayload(fpToFish, MaybeStack.EMPTY, new MaybeStack(player.getMainHandItem())));
        }
        else
        {
            throw NOTHING_THERE.create(available);
        }
        return 1;
    }

    private static int startMinigameFromFP(ServerPlayer player, ResourceKey<FishProperties> fish) throws CommandSyntaxException
    {
        if (!player.getMainHandItem().is(SCTags.RODS)) throw ERROR_ROD.create(null);

        Optional<FishProperties> optional = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getOptional(fish);

        if (optional.isPresent())
        {
            var treasure = new MaybeStack(FishApi.getTreasure(player, optional.get(), List.of()));
            if (SCConfig.HIDE_TREASURES.get()) treasure = new MaybeStack(SCItems.UNKNOWN_FISH);
            PacketDistributor.sendToPlayer(player, new CBFishingStartedPayload(optional.get(), treasure, new MaybeStack(player.getMainHandItem())));
            return 1;
        }
        else
        {
            throw ERROR_FISH_ENTRY_INVALID.create(fish.location());
        }
    }

    private static int setDataOnStack(ServerPlayer player, float size, float weight, int percentile, Rarity rarity) throws CommandSyntaxException
    {
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack stack;

        if (mainHand.isEmpty() && offHand.isEmpty())
            throw ERROR_EMPTY.create(null);

        if (mainHand.isEmpty())
            stack = offHand;
        else
            stack = mainHand;

        SCDataComponents.set(stack, SCDataComponents.CAUGHT_FISH_INFO, new CaughtFishInfo(size, weight, percentile, rarity));

        return 1;
    }
}
