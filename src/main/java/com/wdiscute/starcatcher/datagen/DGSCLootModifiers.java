package com.wdiscute.starcatcher.datagen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DGSCLootModifiers extends GlobalLootModifierProvider
{
    public DGSCLootModifiers(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, Starcatcher.MOD_ID);
    }

    @Override
    protected void start()
    {
        //thank you kaupen my goat 🐐
        this.add("fishing_hat_from_shipwrecks",
                new AddItemModifier(new LootItemCondition[]{
                        new LootTableIdCondition.Builder(BuiltInLootTables.SHIPWRECK_MAP.location()).build(),
                        LootItemRandomChanceCondition.randomChance(0.1f).build()
                }, SCBlocks.HATS.getEntries().stream().map(o -> o.get().asItem()).toList()
                ));
    }

    public static class AddItemModifier extends LootModifier
    {
        public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
                LootModifier.codecStart(inst).and(
                        BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("items").forGetter(e -> e.items)).apply(inst, AddItemModifier::new));
        private final List<Item> items;

        public AddItemModifier(LootItemCondition[] conditionsIn, List<Item> items)
        {
            super(conditionsIn);
            this.items = items;
        }

        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext)
        {
            for (LootItemCondition condition : this.conditions)
            {
                if (!condition.test(lootContext))
                {
                    return generatedLoot;
                }
            }
            generatedLoot.add(items.get(lootContext.getRandom().nextInt(items.size())).getDefaultInstance());
            return generatedLoot;
        }

        @Override
        public MapCodec<? extends IGlobalLootModifier> codec()
        {
            return CODEC;
        }
    }

}
