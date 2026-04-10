package com.wdiscute.starcatcher.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.io.network.FishingStartedPayload;
import com.wdiscute.starcatcher.registry.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.minigamemodifiers.AbstractMinigameModifier;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.command.EnumArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SCCommands
{
    private static final DynamicCommandExceptionType ERROR_ROD = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.rod_not_found", o)
    );

    private static final DynamicCommandExceptionType NOTHING_THERE = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.nothing_there")
    );

    private static final DynamicCommandExceptionType ERROR_EMPTY = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.item_empty", o)
    );

    private static final DynamicCommandExceptionType ERROR_FISH_ENTRY_INVALID = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.fish_entry_not_found", o)
    );

    private static final DynamicCommandExceptionType ERROR_MODIFIER_INVALID = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.starcatcher.modifier_not_found", o)
    );


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context)
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
                                        .then(Commands.argument("percentile", IntegerArgumentType.integer(0, 100))
                                                .then(Commands.argument("rarity", EnumArgument.enumArgument(FishProperties.Rarity.class))
                                                        .executes(c ->
                                                                setDataOnStack(
                                                                        c.getSource().getPlayerOrException(),
                                                                        IntegerArgumentType.getInteger(c, "size_in_cm"),
                                                                        IntegerArgumentType.getInteger(c, "weight_in_grams"),
                                                                        IntegerArgumentType.getInteger(c, "percentile"),
                                                                        FishProperties.Rarity.valueOf(c.getArgument("rarity", FishProperties.Rarity.class).toString())
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )


                //starcatcher add_modifier starcatcher:freeze_on_miss
                .then(Commands.literal("add_minigame_modifier")
                        .then(Commands.argument("modifier", ResourceArgument.resource(context, Starcatcher.MINIGAME_MODIFIERS))
                                .executes(c ->
                                        addMinigameModifier(
                                                c.getSource().getPlayerOrException(),
                                                ResourceArgument.getResource(c, "modifier", Starcatcher.MINIGAME_MODIFIERS).unwrap().left().get()
                                        )
                                ))
                )

                //starcatcher add_modifier starcatcher:ignore_daytime_and_weather_restrictions
                .then(Commands.literal("add_catch_modifier")
                        .then(Commands.argument("modifier", ResourceArgument.resource(context, Starcatcher.CATCH_MODIFIERS))
                                .executes(c ->
                                        addCatchModifier(
                                                c.getSource().getPlayerOrException(),
                                                ResourceArgument.getResource(c, "modifier", Starcatcher.CATCH_MODIFIERS).unwrap().left().get()
                                        )
                                ))
                )

                //starcatcher add_tackle_skin starcatcher:ignore_daytime_and_weather_restrictions
                .then(Commands.literal("add_tackle_skin")
                        .then(Commands.argument("modifier", ResourceArgument.resource(context, Starcatcher.TACKLE_SKIN))
                                .executes(c ->
                                        addTackleSkin(
                                                c.getSource().getPlayerOrException(),
                                                ResourceArgument.getResource(c, "modifier", Starcatcher.TACKLE_SKIN).unwrap().left().get()
                                        )
                                ))
                )

                //starcatcher remove_catch_modifier
                .then(Commands.literal("remove_minigame_modifier")
                        .executes(c ->
                                removeMinigameModifier(
                                        c.getSource().getPlayerOrException()
                                )
                        )
                )

                //starcatcher remove_minigame_modifier
                .then(Commands.literal("remove_catch_modifier")
                        .executes(c ->
                                removeCatchModifier(
                                        c.getSource().getPlayerOrException()
                                )
                        )
                )

                //starcatcher award_fish ...
                .then(Commands.literal("award_fish")
                        .executes(c -> awardAllFish(c.getSource().getPlayerOrException()))
                        // -> /starcatcher award_fish all
                        .then(Commands.literal("all")
                                // -> /starcatcher award_fish all
                                .executes(c -> awardAllFish(c.getSource().getPlayerOrException()))
                                // -> /starcatcher award_fish all 0 0 0
                                .then(Commands.argument("ticks", IntegerArgumentType.integer())
                                        .then(Commands.argument("size", IntegerArgumentType.integer())
                                                .then(Commands.argument("weight", IntegerArgumentType.integer())
                                                        .then(Commands.argument("percentile", BoolArgumentType.bool())
                                                                .then(Commands.argument("golden", BoolArgumentType.bool())
                                                                        .executes(c -> awardAllFish(
                                                                                        c.getSource().getPlayerOrException(),
                                                                                        IntegerArgumentType.getInteger(c, "ticks"),
                                                                                        IntegerArgumentType.getInteger(c, "size"),
                                                                                        IntegerArgumentType.getInteger(c, "weight"),
                                                                                        FloatArgumentType.getFloat(c, "percentile"),
                                                                                        BoolArgumentType.getBool(c, "golden")
                                                                                )
                                                                        )
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
                                        0, 0, 0, 0, false
                                ))
                                // -> /starcatcher award_fish 123, 123, 132, 0, false
                                .then(Commands.argument("ticks", IntegerArgumentType.integer())
                                        .then(Commands.argument("size", IntegerArgumentType.integer())
                                                .then(Commands.argument("weight", IntegerArgumentType.integer())
                                                        .then(Commands.argument("percentile", FloatArgumentType.floatArg())
                                                                .then(Commands.argument("golden", BoolArgumentType.bool())
                                                                        .executes(c -> awardFish(
                                                                                        c.getSource().getPlayerOrException(),
                                                                                        ResourceArgument.getResource(c, "fish", Starcatcher.FISH_REGISTRY_KEY).key(),
                                                                                        IntegerArgumentType.getInteger(c, "ticks"),
                                                                                        IntegerArgumentType.getInteger(c, "size"),
                                                                                        IntegerArgumentType.getInteger(c, "weight"),
                                                                                        FloatArgumentType.getFloat(c, "percentile"),
                                                                                        BoolArgumentType.getBool(c, "golden")
                                                                                )
                                                                        )
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

    private static int awardAllFish(ServerPlayer player, int ticks, int size, int weight, float percentile, boolean golden)
    {
        for (FishProperties fp : player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            FishCaughtCounter.awardFishCaughtCounter(fp, player, ticks, size, weight, percentile, false, false, golden);

        return 0;
    }

    private static int awardAllFish(ServerPlayer player)
    {
        for (FishProperties fp : player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            FishCaughtCounter.awardFishCaughtCounter(fp, player, 0, 0, 0, 0, false, false, false);

        return 0;
    }

    private static int awardFish(ServerPlayer player, ResourceKey<FishProperties> fish, int ticks, int size, int weight, float percentile, boolean golden) throws CommandSyntaxException
    {
        Optional<FishProperties> optional = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getOptional(fish);
        if (optional.isPresent())
            FishCaughtCounter.awardFishCaughtCounter(optional.get(), player, ticks, size, weight, percentile, false, false, golden);
        else
            throw ERROR_FISH_ENTRY_INVALID.create(fish);
        return 0;
    }

    private static int removeMinigameModifier(ServerPlayer player) throws CommandSyntaxException
    {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) throw ERROR_EMPTY.create(null);

        if (SCDataComponents.has(stack, SCDataComponents.MINIGAME_MODIFIERS))
        {
            SCDataComponents.remove(stack, SCDataComponents.MINIGAME_MODIFIERS);
        }
        return 1;
    }

    private static int removeCatchModifier(ServerPlayer player) throws CommandSyntaxException
    {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) throw ERROR_EMPTY.create(null);

        if (SCDataComponents.has(stack, SCDataComponents.CATCH_MODIFIERS))
        {
            SCDataComponents.remove(stack, SCDataComponents.CATCH_MODIFIERS);
        }
        return 1;
    }

    private static int addMinigameModifier(ServerPlayer player, ResourceKey<Supplier<AbstractMinigameModifier>> modifier) throws CommandSyntaxException
    {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) throw ERROR_EMPTY.create(null);

        if (SCDataComponents.has(stack, SCDataComponents.MINIGAME_MODIFIERS))
        {
            List<ResourceLocation> mods = new ArrayList<>(SCDataComponents.get(stack, SCDataComponents.MINIGAME_MODIFIERS));
            mods.add(modifier.location());
            SCDataComponents.set(stack, SCDataComponents.MINIGAME_MODIFIERS, mods);
        }
        else
        {
            SCDataComponents.set(stack, SCDataComponents.MINIGAME_MODIFIERS, List.of(modifier.location()));
        }

        return 1;
    }

    private static int addCatchModifier(ServerPlayer player, ResourceKey<Supplier<AbstractCatchModifier>> modifier) throws CommandSyntaxException
    {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) throw ERROR_EMPTY.create(null);

        if (SCDataComponents.has(stack, SCDataComponents.CATCH_MODIFIERS))
        {
            List<ResourceLocation> mods = new ArrayList<>(SCDataComponents.get(stack, SCDataComponents.CATCH_MODIFIERS));
            mods.add(modifier.location());
            SCDataComponents.set(stack, SCDataComponents.CATCH_MODIFIERS, mods);
        }
        else
        {
            SCDataComponents.set(stack, SCDataComponents.CATCH_MODIFIERS, List.of(modifier.location()));
        }

        return 1;
    }

    private static int addTackleSkin(ServerPlayer player, ResourceKey<Supplier<AbstractTackleSkin>> tackleSkin) throws CommandSyntaxException
    {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(SCTags.RODS)) throw ERROR_ROD.create(null);

        SCDataComponents.set(stack, SCDataComponents.TACKLE_SKIN, tackleSkin.location());

        return 1;
    }

    private static int startMinigame(ServerPlayer player) throws CommandSyntaxException
    {
        if (!player.getMainHandItem().is(SCTags.RODS)) throw ERROR_ROD.create(null);

        List<FishProperties> available = new ArrayList<>();
        for (FishProperties fp : player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
        {
            if (fp.calculateChance(player, player.level(), player.getMainHandItem(), AbstractFishRestriction.Context.COMMAND) > 0)
                available.add(fp.loadTreasure(player));
        }

        if (!available.isEmpty())
        {
            FishProperties fpToFish = available.get(U.r.nextInt(available.size()));
            PacketDistributor.sendToPlayer(player, new FishingStartedPayload(fpToFish, player.getMainHandItem()));
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
            PacketDistributor.sendToPlayer(player, new FishingStartedPayload(optional.get().loadTreasure(player), player.getMainHandItem()));
            return 1;
        }
        else
        {
            throw ERROR_FISH_ENTRY_INVALID.create(fish.location());
        }
    }

    private static int setDataOnStack(ServerPlayer player, int size, int weight, int percentile, FishProperties.Rarity rarity) throws CommandSyntaxException
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

        SCDataComponents.set(stack, SCDataComponents.CAUGHT_FISH_INFO, new CaughtFishInfo(size, weight, percentile, rarity, rarity.equals(FishProperties.Rarity.GOLDEN)));

        return 1;
    }
}
