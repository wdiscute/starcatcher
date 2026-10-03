package com.wdiscute.starcatcher.datagen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;

public class DGSCLootModifiers extends GlobalLootModifierProvider
{

    public DGSCLootModifiers(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, Starcatcher.MOD_ID);
    }

    @Override
    protected void start()
    {
        this.add(
                "hat_from_shipwreck_map",
                new AddHatModifier(Optional.empty(), 0)
        );
    }

    public static class AddHatModifier extends LootModifier
    {
        public static final MapCodec<AddHatModifier> CODEC =
                RecordCodecBuilder.mapCodec(instance ->
                        LootModifier.codecStart(instance)
                                .apply(instance, AddHatModifier::new)
                );

        public AddHatModifier(Optional<Holder<LootItemCondition>> condition, int priority)
        {
            super(condition, priority);
        }

        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext)
        {
            List<Item> hats = SCBlocks.HATS.getEntries()
                    .stream()
                    .map(entry -> entry.get().asItem())
                    .toList();

            if (!hats.isEmpty())
            {
                Item hat = hats.get(
                        lootContext.getRandom().nextInt(hats.size())
                );

                generatedLoot.add(hat.getDefaultInstance());
            }

            return generatedLoot;
        }

        @Override
        public MapCodec<? extends IGlobalLootModifier> codec()
        {
            return CODEC;
        }
    }
}