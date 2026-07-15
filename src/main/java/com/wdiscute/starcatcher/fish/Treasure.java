package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.utils.MaybeStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Treasure
        (
                List<WeightedLootTable> lootTables,
                List<WeightedStack> stacks,
                List<Ingredient> blacklist
        )
{
    public static final Treasure EMPTY = new Treasure(List.of(), List.of(), List.of());
    public static final Treasure VANILLA_FISHING_LOOT_TABLE = Treasure.lootTable(List.of(BuiltInLootTables.FISHING_TREASURE.location()), Items.FISHING_ROD);
    public static final Treasure EXAMPLE_TREASURE =
            new Treasure(
                    List.of(
                            new WeightedLootTable(BuiltInLootTables.FISHING.location(), 20),
                            new WeightedLootTable(BuiltInLootTables.ANCIENT_CITY.location(), 1)
                    ),
                    List.of(
                            new WeightedStack(new MaybeStack(Items.DIAMOND), 2),
                            new WeightedStack(new MaybeStack(Items.GOLD_BLOCK), 15),
                            new WeightedStack(new MaybeStack(Items.DEEPSLATE_EMERALD_ORE), 7)
                    ),
                    List.of(Ingredient.of(Items.FISHING_ROD)
                    ));


    public static final Treasure AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE = Treasure.specificItem(SCItems.AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final Treasure KIMBE_SMITHING_TEMPLATE = Treasure.specificItem(SCItems.KIMBE_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final Treasure COLORFUL_SMITHING_TEMPLATE = Treasure.specificItem(SCItems.COLORFUL_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final Treasure CLEAR_SMITHING_TEMPLATE = Treasure.specificItem(SCItems.CLEAR_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final Treasure KING_SMITHING_TEMPLATE = Treasure.specificItem(SCItems.KING_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final Treasure ICEBORN_SKIN_SMITHING_TEMPLATE = Treasure.specificItem(SCItems.ICEBORN_SKIN_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final Treasure SKY_SKIN_SMITHING_TEMPLATE = Treasure.specificItem(SCItems.SKY_SKIN_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final Treasure SHARK_SKIN_SMITHING_TEMPLATE = Treasure.specificItem(SCItems.SHARKTOOTH_SKIN_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final Treasure LUSH_SKIN_SMITHING_TEMPLATE = Treasure.specificItem(SCItems.LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE.value().getDefaultInstance());

    public static final Codec<Treasure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedLootTable.CODEC.listOf().optionalFieldOf("loot_tables", List.of()).forGetter(Treasure::lootTables),
            WeightedStack.CODEC.listOf().optionalFieldOf("stacks", List.of()).forGetter(Treasure::stacks),
            Ingredient.CODEC.listOf().optionalFieldOf("blacklist", List.of()).forGetter(Treasure::blacklist)
    ).apply(instance, Treasure::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Treasure> STREAM_CODEC = StreamCodec.composite(
            WeightedLootTable.STREAM_CODEC.apply(ByteBufCodecs.list()), Treasure::lootTables,
            WeightedStack.STREAM_CODEC.apply(ByteBufCodecs.list()), Treasure::stacks,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), Treasure::blacklist,
            Treasure::new
    );

    public static Treasure lootTable(List<ResourceLocation> lootTables, Item... blacklist)
    {
        return new Treasure(lootTables.stream().map(o -> new WeightedLootTable(o, 1))
                .toList(), List.of(), Arrays.stream(blacklist).map(Ingredient::of).toList());
    }

    public static Treasure specificItem(ItemStack itemStack)
    {
        return new Treasure(List.of(), List.of(new WeightedStack(new MaybeStack(itemStack), 1)), List.of());
    }

    public ItemStack unpack(ServerPlayer player, List<AbstractCatchModifier> modifiers)
    {
        List<ResourceLocation> weightedLootTables = new ArrayList<>();

        RandomSource r = player.getRandom();

        for (WeightedLootTable chancedTable : this.lootTables)
        {
            for (int i = 0; i < chancedTable.weight(); i++)
            {
                weightedLootTables.add(chancedTable.resourceLocation());
            }
        }

        List<ItemStack> weightedStacks = new ArrayList<>();

        modifiers.forEach(o -> weightedStacks.addAll(o.addToTreasureWeightedList()));

        for (WeightedStack stack : this.stacks)
        {
            for (int i = 0; i < stack.weight(); i++)
            {
                MaybeStack maybeStack = stack.stack();
                if (!maybeStack.isEmpty())
                    weightedStacks.add(maybeStack.toStack());
            }
        }

        //if both loot tables and stacks are empty, return empty
        if (weightedLootTables.isEmpty() && weightedStacks.isEmpty())
            return ItemStack.EMPTY;

        //if loot tables empty, return random stack from list
        if (weightedLootTables.isEmpty())
            return weightedStacks.get(r.nextInt(weightedStacks.size()));

        //if stacks empty unpack and return random loot table
        if (weightedStacks.isEmpty())
            return unpackLootTable(player, weightedLootTables.get(r.nextInt(weightedLootTables.size())), blacklist);

        //pick a random index from the combined virtual list
        int random = r.nextInt(weightedLootTables.size() + weightedStacks.size());

        if (random < weightedLootTables.size())
            return unpackLootTable(player, weightedLootTables.get(random), blacklist);
        else
            return weightedStacks.get(random - weightedLootTables.size());

    }

    private ItemStack unpackLootTable(ServerPlayer player, ResourceLocation rl, List<Ingredient> blacklist)
    {
        LootParams lootparams = new LootParams.Builder((ServerLevel) player.level())
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.TOOL, player.getMainHandItem().is(Tags.Items.RODS) ? player.getMainHandItem() : player.getOffhandItem())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withLuck(player.getLuck())
                .create(LootContextParamSets.FISHING);

        LootTable table = player.level().getServer().reloadableRegistries().getLootTable(
                ResourceKey.create(Registries.LOOT_TABLE, rl)
        );

        ObjectArrayList<ItemStack> randomItems = table.getRandomItems(lootparams);

        if (randomItems.isEmpty()) return ItemStack.EMPTY;

        List<ItemStack> validItems = randomItems.stream()
                .filter(stack -> blacklist.stream().noneMatch(ingredient -> ingredient.test(stack)))
                .toList();

        if (validItems.isEmpty()) return ItemStack.EMPTY;

        return validItems.get(player.getRandom().nextInt(validItems.size()));
    }

}
