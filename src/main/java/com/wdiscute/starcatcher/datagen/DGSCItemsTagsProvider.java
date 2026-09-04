package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.compat.CreateCompat;
import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.Utils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.concurrent.CompletableFuture;

import static com.wdiscute.starcatcher.registry.SCItems.*;
import static com.wdiscute.starcatcher.registry.SCBlocks.*;

public class DGSCItemsTagsProvider extends ItemTagsProvider
{

    public DGSCItemsTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, lookupProvider, Starcatcher.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        //fishes, cat_food, foods/raw_fish
        for (var item : BUCKETABLE_FISHES_REGISTRY.getEntries())
            tag(SCTags.BUCKETABLE_FISHES).addOptional(rk(BuiltInRegistries.ITEM.getKey(item.value())));

        //starcatcher fishes to minecraft and common tags
        tag(ItemTags.CAT_FOOD).addTag(SCTags.STARCAUGHT_FISHABLE_FISH);
        tag(ItemTags.FISHES).addTag(SCTags.STARCAUGHT_FISHABLE_FISH);
        tag(Tags.Items.FOODS_RAW_FISH).addTag(SCTags.STARCAUGHT_FISHABLE_FISH);

        //add every starcatcher FP of STARCAUGHT_FISHABLES to STARCAUGHT_FISHABLES item tag
        for (FishProperties fp : FishRegistration.STARCATCHER_FISHABLE)
        {
            Identifier key = BuiltInRegistries.ITEM.getKey(fp.catchInfo().fish().toItem());
            tag(SCTags.STARCAUGHT_FISHABLE).addOptional(rk(key));
        }

        //starcaught fishable fish
        tag(SCTags.STARCAUGHT_FISHABLE_FISH)
                .addTag(SCTags.STARCAUGHT_FISHABLE)
                .remove(SCTags.CRABS)
                .remove(SCTags.EELS)
                .remove(SCTags.SHRIMPS)
        ;

        //cycle every FP
        for (FishProperties fp : FishRegistration.ALL_FISHABLE)
        {
            Identifier key = fp.catchInfo().fish().identifier();

            //add all non-trophy non-message non-extra to rarity tags
            if (fp.hasGuideEntry() && fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH) && !fp.catchInfo().alwaysSpawnEntity())
            {
                //fishable for tackle box
                tag(SCTags.FISHABLE).addOptional(rk(key));

                switch (fp.rarity())
                {
                    case COMMON -> tag(SCTags.COMMON_FISHES).addOptional(rk(key));
                    case UNCOMMON -> tag(SCTags.UNCOMMON_FISHES).addOptional(rk(key));
                    case RARE -> tag(SCTags.RARE_FISHES).addOptional(rk(key));
                    case EPIC -> tag(SCTags.EPIC_FISHES).addOptional(rk(key));
                    case LEGENDARY -> tag(SCTags.LEGENDARY_FISHES).addOptional(rk(key));
                }

                if (key.getNamespace().equals("starcatcher"))
                    switch (fp.rarity())
                    {
                        case COMMON -> tag(SCTags.COMMON_STARCAUGHT_FISHES).addOptional(rk(key));
                        case UNCOMMON -> tag(SCTags.UNCOMMON_STARCAUGHT_FISHES).addOptional(rk(key));
                        case RARE -> tag(SCTags.RARE_STARCAUGHT_FISHES).addOptional(rk(key));
                        case EPIC -> tag(SCTags.EPIC_STARCAUGHT_FISHES).addOptional(rk(key));
                        case LEGENDARY -> tag(SCTags.LEGENDARY_STARCAUGHT_FISHES).addOptional(rk(key));
                    }
            }
        }

        //crabs
        tag(SCTags.CRABS)
                .add(rk(CHORUS_CRAB.get()))
                .add(rk(LAVA_CRAB.get()))
                .add(rk(MOLTEN_DEEPSLATE_CRAB.get()))
                .add(rk(OBSIDIAN_CRAB.get()));

        //eels
        tag(SCTags.EELS)
                .add(rk(BLACK_EEL.get()))
                .add(rk(GEODE_EEL.get()))
                .add(rk(OBSIDIAN_EEL.get()))
                .addOptional(rk(BuiltInRegistries.ITEM.getKey(CreateCompat.EEL_DYNAMO.value())))
        ;

        //shrimps
        tag(SCTags.SHRIMPS)
                .add(rk(MOLTEN_SHRIMP.get()))
                .add(rk(SCORCHED_BLOODSUCKER.get()))
        ;

        //worms
        tag(SCTags.WORMS)
                .add(rk(WORM.get()))
                .add(rk(ALMIGHTY_WORM.get()))
                .add(rk(SEEKING_WORM.get()));

        //baits tag
        tag(SCTags.BAITS)
                .add(rk(WORM.get()))
                .add(rk(ALMIGHTY_WORM.get()))
                .add(rk(SEEKING_WORM.get()))
                .add(rk(GUNPOWDER_BAIT.get()))
                .add(rk(CHERRY_BAIT.get()))
                .add(rk(LUSH_BAIT.get()))
                .add(rk(SCULK_BAIT.get()))
                .add(rk(DRIPSTONE_BAIT.get()))
                .add(rk(MURKWATER_BAIT.get()))
                .add(rk(LEGENDARY_BAIT.get()))
                .add(rk(METEOROLOGICAL_BAIT.get()))
                .add(rk(Items.WITHER_SKELETON_SKULL))
                .addTag(Tags.Items.BUCKETS_EMPTY)

                .addOptional(rk(rl("fishofthieves", "earthworms")))
                .addOptional(rk(rl("fishofthieves", "grubs")))
                .addOptional(rk(rl("fishofthieves", "leeches")))

                .addOptional(rk(rl("tfc", "food/bluegill")))
                .addOptional(rk(rl("tfc", "food/cod")))
                .addOptional(rk(rl("tfc", "food/salmon")))
                .addOptional(rk(rl("tfc", "food/tropical_fish")))
        ;

        //templates tag
        TEMPLATES_REGISTRY.getEntries().forEach(o -> tag(SCTags.TEMPLATES).add(rk(o.get())));

        //tackle skins
        tag(SCTags.TACKLE_SKINS)
                .add(rk(PEARL_SMITHING_TEMPLATE.get()))
                .add(rk(KING_SMITHING_TEMPLATE.get()))
                .add(rk(COLORFUL_SMITHING_TEMPLATE.get()))
                .add(rk(CLEAR_SMITHING_TEMPLATE.get()))
                .add(rk(FROG_SMITHING_TEMPLATE.get()))
                .add(rk(PEARL_SMITHING_TEMPLATE.get()))
        ;

        //Equipment tag
        RODS_REGISTRY.getEntries().forEach(o -> tag(SCTags.EQUIPMENTS).add(rk(o.get())));

        //gadgets
        tag(SCTags.GADGETS).add(rk(FISH_RADAR.get()));

        //hooks tag
        HOOKS_REGISTRY.getEntries().forEach(o -> tag(SCTags.HOOKS).add(rk(o.get())));
        tag(SCTags.HOOKS).addOptional(rk(rl("tide", "void_fishing_hook")));

        //bobbers tag
        BOBBERS_REGISTRY.getEntries().forEach(o -> tag(SCTags.BOBBERS).add(rk(o.get())));

        //rods and tools/fishing_rod
        RODS_REGISTRY.getEntries().forEach(o -> tag(SCTags.RODS).add(rk(o.get())));
        RODS_REGISTRY.getEntries().forEach(o -> tag(Tags.Items.TOOLS_FISHING_ROD).add(rk(o.get())));

        tag(SCTags.AQUARIUM_INTERACTIONS)
                .add(rk(Items.DIAMOND_PICKAXE))
                .add(rk(Items.DIAMOND_SHOVEL))
                .add(rk(Items.STONE))
                .add(rk(Items.GRAVEL))
                .add(rk(Items.SAND))
                .add(rk(Items.RED_SAND))
                .add(rk(Items.KELP))
                .add(rk(Items.SEAGRASS))
                .addTag(Tags.Items.BUCKETS_EMPTY)
                .add(rk(AURORA.get()))
                .add(rk(CONCH.asItem()))
                .add(rk(CLAM.asItem()))
        ;

        //hats
        HATS.getEntries().forEach(o -> tag(SCTags.HATS).add(rk(((DeferredBlock<?>) o).asItem())));

        //equippable hats
        tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .addTag(SCTags.HATS);

        //enchantable rods
        tag(ItemTags.FISHING_ENCHANTABLE)
                .addTag(SCTags.RODS);

        tag(SCTags.PLACEABLE_IN_DISPLAY)
                .addTag(SCTags.BUCKETABLE_FISHES)
                .add(rk(GUIDE.get()))
        ;

        tag(SCTags.PLACEABLE_IN_TACKLE_BOX)
                .addTag(SCTags.BAITS)
                .addTag(SCTags.HOOKS)
                .addTag(SCTags.BOBBERS)
                .addTag(SCTags.FISHABLE)
                .addTag(SCTags.HATS)
                .addTag(ItemTags.FISHES)
                .add(rk(GUIDE.get()))
                .add(rk(FISH_RADAR.get()))
        ;

        //tackle boxes
        tag(SCTags.TACKLE_BOXES)
                .add(rk(TACKLE_BOX.asItem()))
                .add(rk(TACKLE_BOX_BLACK.asItem()))
                .add(rk(TACKLE_BOX_BLUE.asItem()))
                .add(rk(TACKLE_BOX_LIGHT_BLUE.asItem()))
                .add(rk(TACKLE_BOX_ORANGE.asItem()))
                .add(rk(TACKLE_BOX_YELLOW.asItem()))
                .add(rk(TACKLE_BOX_RED.asItem()))
                .add(rk(TACKLE_BOX_BROWN.asItem()))
                .add(rk(TACKLE_BOX_CYAN.asItem()))
                .add(rk(TACKLE_BOX_GREEN.asItem()))
                .add(rk(TACKLE_BOX_LIME.asItem()))
                .add(rk(TACKLE_BOX_GRAY.asItem()))
                .add(rk(TACKLE_BOX_LIGHT_GRAY.asItem()))
                .add(rk(TACKLE_BOX_PINK.asItem()))
                .add(rk(TACKLE_BOX_MAGENTA.asItem()))
                .add(rk(TACKLE_BOX_PURPLE.asItem()))
                .add(rk(TACKLE_BOX_WHITE.asItem()))
        ;

        tag(ItemTags.BOOKSHELF_BOOKS)
                .add(rk(GUIDE.get()));

        tag(SCTags.HAS_RADAR_LAYER)
                .add(rk(FISH_RADAR.get()));

        tag(SCTags.HAS_TRACKER_LAYER)
                .add(rk(GUIDE.get()))
                .addTag(SCTags.RODS);

        tag(SCTags.HAS_FARMLAND_INTERACTION)
                .add(rk(Items.BONE_MEAL));

        this.tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(SCTags.RODS);
    }

    private static ResourceKey<Item> rk(Identifier id)
    {
        return ResourceKey.create(Registries.ITEM, id);
    }

    private static ResourceKey<Item> rk(Item item)
    {
        return ResourceKey.create(Registries.ITEM, BuiltInRegistries.ITEM.getKey(item));
    }


    public static Identifier rl(String ns, String path)
    {
        return Utils.rl(ns, path);
    }
}
