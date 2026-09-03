package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.wdiscute.starcatcher.registry.SCItems.*;
import static com.wdiscute.starcatcher.registry.SCBlocks.*;

public class DGSCModelProvider extends ModelProvider
{
    public DGSCModelProvider(PackOutput output)
    {
        super(output, Starcatcher.MOD_ID);
    }

    private ItemModelGenerators itemModels;
    private BlockModelGenerators blockModels;

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks()
    {
        List<Holder<Block>> list = new ArrayList<>();
        list.addAll(HATS.getEntries().stream().toList());
        list.addAll(TACKLE_BOXES.getEntries().stream().toList());

        list.add(TROPHY_COPPER);
        list.add(TROPHY_IRON);
        list.add(TROPHY_GOLD);
        list.add(TROPHY_EMERALD);
        list.add(TROPHY_DIAMOND);

        return list.stream();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems()
    {
        List<Holder<Item>> list = new ArrayList<>();

        list.addAll(BUCKETABLE_FISHES_REGISTRY.getEntries().stream().toList());
        list.addAll(TEMPLATES_REGISTRY.getEntries().stream().toList());
        list.addAll(HOOKS_REGISTRY.getEntries().stream().toList());
        list.addAll(BOBBERS_REGISTRY.getEntries().stream().toList());
        list.addAll(HATS.getEntries().stream().map(o -> o.get().asItem().builtInRegistryHolder()).toList());
        list.addAll(TACKLE_BOXES.getEntries().stream().map(o -> o.get().asItem().builtInRegistryHolder()).toList());

        list.add(TROPHY_COPPER.asItem().builtInRegistryHolder());
        list.add(TROPHY_IRON.asItem().builtInRegistryHolder());
        list.add(TROPHY_GOLD.asItem().builtInRegistryHolder());
        list.add(TROPHY_EMERALD.asItem().builtInRegistryHolder());
        list.add(TROPHY_DIAMOND.asItem().builtInRegistryHolder());

        return list.stream();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
    {
        this.itemModels = itemModels;
        this.blockModels = blockModels;

        //bucket fishes
        for (DeferredHolder<Item, ? extends Item> item : BUCKETABLE_FISHES_REGISTRY.getEntries())
            simpleItem((DeferredItem<? extends Item>) item);

        //non bucket fishes
        for (DeferredHolder<Item, ? extends Item> item : NON_BUCKETABLE_FISH_REGISTRY.getEntries())
            simpleItem((DeferredItem<? extends Item>) item);

        //non fish fishes
        for (DeferredHolder<Item, ? extends Item> item : NON_FISH_FISH_REGISTRY.getEntries())
            simpleItem((DeferredItem<? extends Item>) item);

        //trash
        simpleItem(BOOT);
        simpleItem(DRIED_SEAWEED);
        simpleItem(LAVA_CRAB_CLAW);
        simpleItem(MOSSY_BOOT);

        //items
        simpleItem(MISSINGNO);
        simpleItem(UNKNOWN_FISH);
        simpleItem(GUIDE);
        simpleItem(FISH_RADAR);
        simpleItem(STARCATCHER_TWINE);
        simpleItem(FISH_BONES);
        simpleItem(PEARL);
        simpleItem(STARCAUGHT_BUCKET);
        simpleItem(STARCAUGHT_LAVA_BUCKET);
        simpleItem(STARCAUGHT_FISH);
        simpleItem(COOKED_STARCAUGHT_FISH);
        simpleItem(SETTINGS);
        simpleItem(TREASURE);

        //notes & messages
        simpleItem(LETTER);
        simpleItem(BOTTLED_LETTER);

        simpleItem(MESSAGE_IN_A_BOTTLE);
        //simpleItem(MESSAGE);

        simpleItem(BROKEN_BOTTLE);

        //hooks
        simpleItem(HOOK);
        simpleItem(AMETHYST_HOOK);
        simpleItem(SHINY_HOOK);
        simpleItem(GOLD_HOOK);
        simpleItem(MOSSY_HOOK);
        simpleItem(STONE_HOOK);
        simpleItem(SPLIT_HOOK);
        simpleItem(HEAVY_HOOK);
        simpleItem(VANILLA_HOOK);
        simpleItem(COPPER_HOOK);
        simpleItem(EXPOSED_COPPER_HOOK);
        simpleItem(WEATHERED_COPPER_HOOK);
        simpleItem(OXIDISED_COPPER_HOOK);
        simpleItem(FROZEN_HOOK);
        simpleItem(RUSTY_HOOK);
        simpleItem(ECHOING_HOOK);

        //bobbers
        simpleItem(BOBBER);
        simpleItem(STEADY_BOBBER);
        simpleItem(CLEAR_BOBBER);
        simpleItem(DRIPSTONE_BOBBER);
        simpleItem(VANILLA_BOBBER);
        simpleItem(LEAF_BOBBER);
        simpleItem(SLIMEY_BOBBER);
        simpleItem(GLOWING_BOBBER);
        simpleItem(GOLDEN_BOBBER);
        simpleItem(CLOUD_BOBBER);

        //baits
        simpleItem(WORM);
        simpleItem(ALMIGHTY_WORM);
        simpleItem(SEEKING_WORM);
        simpleItem(GUNPOWDER_BAIT);
        simpleItem(CHERRY_BAIT);
        simpleItem(LUSH_BAIT);
        simpleItem(SCULK_BAIT);
        simpleItem(DRIPSTONE_BAIT);
        simpleItem(MURKWATER_BAIT);
        simpleItem(LEGENDARY_BAIT);
        simpleItem(METEOROLOGICAL_BAIT);

        //templates
        TEMPLATES_REGISTRY.getEntries().forEach(o -> simpleItem(((DeferredItem) o)));

        //rods
        //custom model

        simpleItem(DeferredItem.createItem(Starcatcher.rl("clam")));
        simpleItem(DeferredItem.createItem(Starcatcher.rl("conch")));

        //trophies block item
        simpleBlockItem(TROPHY_COPPER);
        simpleBlockItem(TROPHY_IRON);
        simpleBlockItem(TROPHY_GOLD);
        simpleBlockItem(TROPHY_EMERALD);
        simpleBlockItem(TROPHY_DIAMOND);
        simpleBlockItem(TROPHY_OF_THE_OLDER_ANGLER);

        //hats model, just parents to block
        simpleBlockItem(FISHERMAN_HAT_WHITE);
        simpleBlockItem(FISHERMAN_HAT_LIME);
        simpleBlockItem(FISHERMAN_HAT_ORANGE);
        simpleBlockItem(FISHERMAN_HAT_RED);
        simpleBlockItem(FISHERMAN_HAT_GRAY);
        simpleBlockItem(FISHERMAN_HAT_LIGHT_GRAY);
        simpleBlockItem(FISHERMAN_HAT_BLACK);
        simpleBlockItem(FISHERMAN_HAT_BROWN);
        simpleBlockItem(FISHERMAN_HAT_YELLOW);
        simpleBlockItem(FISHERMAN_HAT_PINK);
        simpleBlockItem(FISHERMAN_HAT_MAGENTA);
        simpleBlockItem(FISHERMAN_HAT_PURPLE);
        simpleBlockItem(FISHERMAN_HAT_BLUE);
        simpleBlockItem(FISHERMAN_HAT_LIGHT_BLUE);
        simpleBlockItem(FISHERMAN_HAT_CYAN);
        simpleBlockItem(FISHERMAN_HAT_GREEN);

        //tacklebox
        simpleBlockItem(TACKLE_BOX);
        simpleBlockItem(TACKLE_BOX_WHITE);
        simpleBlockItem(TACKLE_BOX_LIME);
        simpleBlockItem(TACKLE_BOX_ORANGE);
        simpleBlockItem(TACKLE_BOX_RED);
        simpleBlockItem(TACKLE_BOX_GRAY);
        simpleBlockItem(TACKLE_BOX_LIGHT_GRAY);
        simpleBlockItem(TACKLE_BOX_BLACK);
        simpleBlockItem(TACKLE_BOX_BROWN);
        simpleBlockItem(TACKLE_BOX_YELLOW);
        simpleBlockItem(TACKLE_BOX_PINK);
        simpleBlockItem(TACKLE_BOX_MAGENTA);
        simpleBlockItem(TACKLE_BOX_PURPLE);
        simpleBlockItem(TACKLE_BOX_BLUE);
        simpleBlockItem(TACKLE_BOX_LIGHT_BLUE);
        simpleBlockItem(TACKLE_BOX_CYAN);
        simpleBlockItem(TACKLE_BOX_GREEN);

    }

    private void simpleItem(DeferredItem<? extends Item> item)
    {
        itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
    }

    private void simpleBlockItem(Holder<Block> block)
    {
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block.value(),
                        BlockModelGenerators.plainVariant(Starcatcher.rl("block/" + block.getKey().identifier().getPath())))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }
}
