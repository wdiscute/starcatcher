package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.io.SCDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.nikdo53.neobackports.io.components.DataComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.wdiscute.starcatcher.blocks.SCBlocks.*;

public class DGSCBlockLootTableProvider extends BlockLootSubProvider
{
    protected DGSCBlockLootTableProvider()
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate()
    {
        HATS.getEntries().forEach(o -> dropSelf(o.get()));
        TACKLE_BOXES.getEntries().forEach(o ->
                add(o.get(), LootTable.lootTable().withPool(this.applyExplosionCondition(
                                        o.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                                .add(LootItem.lootTableItem(o.get())
                                                        .apply(CopyNameFunction
                                                                .copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)
                                                        ).apply(CopyNbtFunction
                                                                .copyData(ContextNbtProvider.BLOCK_ENTITY)
                                                                .copy("Items", DataComponents.CONTAINER.getRegisteredName())
                                                                .copy("Fishes", SCDataComponents.TACKLE_BOX_FISHES.getRegisteredName())
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
