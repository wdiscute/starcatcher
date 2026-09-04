package com.wdiscute.starcatcher.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.List;

public class DGSCLootModifiers extends GlobalLootModifierProvider
{
    public DGSCLootModifiers(PackOutput output)
    {
        super(output, Starcatcher.MOD_ID);
    }

    @Override
    protected void start()
    {
        //thank you kaupen my goat
        this.add("fishing_hat_from_shipwrecks",
                new AddItemModifier(new LootItemCondition[]{
                        new LootTableIdCondition.Builder(BuiltInLootTables.SHIPWRECK_MAP).build(),
                        LootItemRandomChanceCondition.randomChance(0.5f).build()
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
            generatedLoot.add(items.get(lootContext.getRandom().nextInt(items.size())).getDefaultInstance());
            return generatedLoot;
        }

        @Override
        public Codec<? extends IGlobalLootModifier> codec()
        {
            return CODEC.codec();
        }
    }

}
