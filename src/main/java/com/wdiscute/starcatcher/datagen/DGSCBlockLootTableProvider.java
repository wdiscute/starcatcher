package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.wdiscute.starcatcher.registry.SCBlocks.*;

public class DGSCBlockLootTableProvider extends BlockLootSubProvider
{
    protected DGSCBlockLootTableProvider(HolderLookup.Provider registries)
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate()
    {
        HATS.getEntries().forEach(o -> dropSelf(o.get()));
        TACKLE_BOXES.getEntries().forEach(o ->
                add(o.get(), LootTable.lootTable().withPool(this.applyExplosionCondition(
                                        o.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                                .add(LootItem.lootTableItem(o.get())
                                                        .apply(CopyComponentsFunction
                                                                .copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                                                .include(DataComponents.CUSTOM_NAME)
                                                                .include(DataComponents.CONTAINER)
                                                                .include(SCDataComponents.TACKLE_BOX_FISHES.get())
                                                        ))))
                ));




        dropSelf(AQUARIUM.get());

        dropSelf(DISPLAY.get());

        dropSelf(TROPHY_COPPER.get());
        dropSelf(TROPHY_IRON.get());
        dropSelf(TROPHY_GOLD.get());
        dropSelf(TROPHY_EMERALD.get());
        dropSelf(TROPHY_DIAMOND.get());

        dropSelf(CLAM.get());
        dropSelf(CONCH.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        List<Block> list = new ArrayList<>();
        list.addAll(HATS.getEntries().stream().map(Holder::value).toList());
        list.addAll(TACKLE_BOXES.getEntries().stream().map(Holder::value).toList());

        list.add(TROPHY_COPPER.get());
        list.add(TROPHY_IRON.get());
        list.add(TROPHY_GOLD.get());
        list.add(TROPHY_EMERALD.get());
        list.add(TROPHY_DIAMOND.get());

        list.add(AQUARIUM.get());
        list.add(DISPLAY.get());

        list.add(CLAM.get());
        list.add(CONCH.get());
        return list::iterator;
    }
}
