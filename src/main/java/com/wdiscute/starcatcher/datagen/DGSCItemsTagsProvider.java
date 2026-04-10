package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.fishing.DGStarcatcherFishes;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.wdiscute.starcatcher.registry.SCItems.*;
import static com.wdiscute.starcatcher.blocks.SCBlocks.*;

public class DGSCItemsTagsProvider extends ItemTagsProvider
{

    public DGSCItemsTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                 CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, blockTags, Starcatcher.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        //fishes, cat_food, foods/raw_fish
        for (var item : BUCKETABLE_FISHES_REGISTRY.getEntries())
        {
            tag(ItemTags.FISHES).add(item.get());
            tag(ItemTags.CAT_FOOD).add(item.get());
            tag(Tags.Items.FOODS_RAW_FISH).add(item.get());
            tag(SCTags.BUCKETABLE_FISHES).add(item.get());
            tag(SCTags.STARCAUGHT_FISHES).add(item.get());
        }

        //todo figure out what to do with crabs/eels tags?

        //rarity tags
        FishingPropertiesRegistry.PROPERTIES.forEach(p ->
        {
            //return if not a fish or alwaysSpawnEntity
            FishProperties fp = p.getSecond();
            if (!fp.catchInfo().fishEntryType().equals(FishProperties.CatchInfo.FishEntryType.FISH)) return;
            if (fp.catchInfo().alwaysSpawnEntity()) return;

            switch (p.getSecond().rarity())
            {
                case TRASH -> tag(SCTags.TRASH).addOptional(fp.catchInfo().fish().getKey().location());
                case COMMON -> tag(SCTags.COMMON_FISHES).addOptional(fp.catchInfo().fish().getKey().location());
                case UNCOMMON -> tag(SCTags.UNCOMMON_FISHES).addOptional(fp.catchInfo().fish().getKey().location());
                case RARE -> tag(SCTags.RARE_FISHES).addOptional(fp.catchInfo().fish().getKey().location());
                case EPIC -> tag(SCTags.EPIC_FISHES).addOptional(fp.catchInfo().fish().getKey().location());
                case LEGENDARY -> tag(SCTags.LEGENDARY_FISHES).addOptional(fp.catchInfo().fish().getKey().location());
            }
        });

        for (FishProperties fp : DGStarcatcherFishes.STARCATCHER_FISHES)
        {
            switch (fp.rarity())
            {
                case COMMON -> tag(SCTags.COMMON_FISHES).add(fp.catchInfo().fish().value());
                case UNCOMMON -> tag(SCTags.UNCOMMON_FISHES).add(fp.catchInfo().fish().value());
                case RARE -> tag(SCTags.RARE_FISHES).add(fp.catchInfo().fish().value());
                case EPIC -> tag(SCTags.EPIC_FISHES).add(fp.catchInfo().fish().value());
                case LEGENDARY -> tag(SCTags.LEGENDARY_FISHES).add(fp.catchInfo().fish().value());
            }
        }

        //worms
        tag(SCTags.WORMS)
                .add(WORM.get())
                .add(ALMIGHTY_WORM.get())
                .add(SEEKING_WORM.get());

        //baits tag
        tag(SCTags.BAITS)
                .add(WORM.get())
                .add(ALMIGHTY_WORM.get())
                .add(SEEKING_WORM.get())
                .add(DEV_WORM.get())
                .add(GUNPOWDER_BAIT.get())
                .add(CHERRY_BAIT.get())
                .add(LUSH_BAIT.get())
                .add(SCULK_BAIT.get())
                .add(DRIPSTONE_BAIT.get())
                .add(MURKWATER_BAIT.get())
                .add(LEGENDARY_BAIT.get())
                .add(METEOROLOGICAL_BAIT.get())
                .add(Items.WITHER_SKELETON_SKULL)
                .add(Items.BUCKET)

                .addOptional(rl("fishofthieves", "earthworms"))
                .addOptional(rl("fishofthieves", "grubs"))
                .addOptional(rl("fishofthieves", "leeches"))

                .addOptional(rl("tfc", "food/bluegill"))
                .addOptional(rl("tfc", "food/cod"))
                .addOptional(rl("tfc", "food/salmon"))
                .addOptional(rl("tfc", "food/tropical_fish"))
        ;

        //templates tag
        TEMPLATES_REGISTRY.getEntries().forEach(o -> tag(SCTags.TEMPLATES).add(o.get()));

        //Equipment tag
        RODS_REGISTRY.getEntries().forEach(o -> tag(SCTags.EQUIPMENTS).add(o.get()));
        //ModItems.HATS_REGISTRY.getEntries().stream().forEach(o -> tag(StarcatcherTags.EQUIPMENTS).add(o.get()));

        //gadgets
        tag(SCTags.GADGETS).add(FISH_RADAR.get());

        //hooks tag
        HOOKS_REGISTRY.getEntries().forEach(o -> tag(SCTags.HOOKS).add(o.get()));
        tag(SCTags.HOOKS).addOptional(rl("tide", "void_fishing_hook"));

        //bobbers tag
        BOBBERS_REGISTRY.getEntries().forEach(o -> tag(SCTags.BOBBERS).add(o.get()));

        //rods and tools/fishing_rod
        RODS_REGISTRY.getEntries().forEach(o -> tag(SCTags.RODS).add(o.get()));
        RODS_REGISTRY.getEntries().forEach(o -> tag(Tags.Items.TOOLS_FISHING_ROD).add(o.get()));

        tag(SCTags.AQUARIUM_INTERACTIONS)
                .add(Items.DIAMOND_PICKAXE)
                .add(Items.DIAMOND_SHOVEL)
                .add(Items.STONE)
                .add(Items.GRAVEL)
                .add(Items.SAND)
                .add(Items.RED_SAND)
                .add(Items.KELP)
                .add(Items.SEAGRASS)
                .add(Items.BUCKET)
                .add(AURORA.get())
                .add(CONCH.asItem())
                .add(CLAM.asItem())
        ;

        //hats
        HATS.getEntries().forEach(o -> tag(SCTags.HATS).add(((DeferredBlock<?>) o).asItem()));

        //equippable hats
        tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .addTag(SCTags.HATS);

        tag(SCTags.PLACEABLE_IN_DISPLAY)
                .addTag(SCTags.BUCKETABLE_FISHES)
                .add(GUIDE.get())
        ;

        tag(SCTags.PLACEABLE_IN_TACKLE_BOX)
                .addTag(SCTags.BAITS)
                .addTag(SCTags.HOOKS)
                .addTag(SCTags.BOBBERS)
                .addTag(ItemTags.FISHES)
                .addTag(SCTags.COMMON_FISHES)
                .addTag(SCTags.UNCOMMON_FISHES)
                .addTag(SCTags.RARE_FISHES)
                .addTag(SCTags.EPIC_FISHES)
                .addTag(SCTags.LEGENDARY_FISHES)
        ;

        //tackle boxes
        tag(SCTags.TACKLE_BOXES)
                .add(TACKLE_BOX.asItem())
                .add(TACKLE_BOX_BLACK.asItem())
                .add(TACKLE_BOX_BLUE.asItem())
                .add(TACKLE_BOX_LIGHT_BLUE.asItem())
                .add(TACKLE_BOX_ORANGE.asItem())
                .add(TACKLE_BOX_YELLOW.asItem())
                .add(TACKLE_BOX_RED.asItem())
                .add(TACKLE_BOX_BROWN.asItem())
                .add(TACKLE_BOX_CYAN.asItem())
                .add(TACKLE_BOX_GREEN.asItem())
                .add(TACKLE_BOX_LIME.asItem())
                .add(TACKLE_BOX_GRAY.asItem())
                .add(TACKLE_BOX_LIGHT_GRAY.asItem())
                .add(TACKLE_BOX_PINK.asItem())
                .add(TACKLE_BOX_MAGENTA.asItem())
                .add(TACKLE_BOX_PURPLE.asItem())
                .add(TACKLE_BOX_WHITE.asItem())
        ;

    }


    public static ResourceLocation rl(String ns, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(ns, path);
    }
}
