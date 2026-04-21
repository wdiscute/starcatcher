package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.DGStarcatcherFishes;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.concurrent.CompletableFuture;

import static com.wdiscute.starcatcher.registry.SCItems.*;
import static com.wdiscute.starcatcher.blocks.SCBlocks.*;

public class DGSCItemsTagsProvider extends KeyTagProvider<Item>
{

    public DGSCItemsTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(output, Registries.ITEM, lookupProvider, Starcatcher.MOD_ID);
    }

    public ResourceKey<Item> rk(String ns, String rl)
    {
        return ResourceKey.create(Registries.ITEM, U.rl(ns, rl));
    }

    public ResourceKey<Item> rk(Identifier rl)
    {
        return ResourceKey.create(Registries.ITEM, rl);
    }

    public ResourceKey<Item> rk(Item item)
    {
        return ResourceKey.create(Registries.ITEM, BuiltInRegistries.ITEM.getKey(item));
    }

    public ResourceKey<Item> rk(DeferredItem<Item> item)
    {
        return ResourceKey.create(Registries.ITEM, BuiltInRegistries.ITEM.getKey(item.get()));
    }

    public ResourceKey<Item> rk(DeferredBlock<Block> block)
    {
        return ResourceKey.create(Registries.ITEM, BuiltInRegistries.ITEM.getKey(block.asItem()));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        //fishes, cat_food, foods/raw_fish
        for (DeferredHolder<Item, ? extends Item> item : BUCKETABLE_FISHES_REGISTRY.getEntries())
        {
            tag(ItemTags.FISHES).add(rk(item.getId()));
            tag(ItemTags.CAT_FOOD).add(rk(item.getId()));
            tag(Tags.Items.FOODS_RAW_FISH).add(rk(item.getId()));
            tag(SCTags.BUCKETABLE_FISHES).add(rk(item.getId()));
            tag(SCTags.STARCAUGHT_FISHES).add(rk(item.getId()));
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
                case TRASH -> tag(SCTags.TRASH).addOptional(rk(fp.catchInfo().fish().value()));
                case COMMON -> tag(SCTags.COMMON_FISHES).addOptional(rk(fp.catchInfo().fish().value()));
                case UNCOMMON -> tag(SCTags.UNCOMMON_FISHES).addOptional(rk(fp.catchInfo().fish().value()));
                case RARE -> tag(SCTags.RARE_FISHES).addOptional(rk(fp.catchInfo().fish().value()));
                case EPIC -> tag(SCTags.EPIC_FISHES).addOptional(rk(fp.catchInfo().fish().value()));
                case LEGENDARY -> tag(SCTags.LEGENDARY_FISHES).addOptional(rk(fp.catchInfo().fish().value()));
            }
        });

        for (FishProperties fp : DGStarcatcherFishes.STARCATCHER_FISHES)
        {
            switch (fp.rarity())
            {
                case COMMON -> tag(SCTags.COMMON_FISHES).add(rk(fp.catchInfo().fish().value()));
                case UNCOMMON -> tag(SCTags.UNCOMMON_FISHES).add(rk(fp.catchInfo().fish().value()));
                case RARE -> tag(SCTags.RARE_FISHES).add(rk(fp.catchInfo().fish().value()));
                case EPIC -> tag(SCTags.EPIC_FISHES).add(rk(fp.catchInfo().fish().value()));
                case LEGENDARY -> tag(SCTags.LEGENDARY_FISHES).add(rk(fp.catchInfo().fish().value()));
            }
        }

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
                .add(rk(DEV_WORM.get()))
                .add(rk(GUNPOWDER_BAIT.get()))
                .add(rk(CHERRY_BAIT.get()))
                .add(rk(LUSH_BAIT.get()))
                .add(rk(SCULK_BAIT.get()))
                .add(rk(DRIPSTONE_BAIT.get()))
                .add(rk(MURKWATER_BAIT.get()))
                .add(rk(LEGENDARY_BAIT.get()))
                .add(rk(METEOROLOGICAL_BAIT.get()))
                .add(rk(Items.WITHER_SKELETON_SKULL))
                .add(rk(Items.BUCKET))

                .addOptional(rk("fishofthieves", "earthworms"))
                .addOptional(rk("fishofthieves", "grubs"))
                .addOptional(rk("fishofthieves", "leeches"))

                .addOptional(rk("tfc", "food/bluegill"))
                .addOptional(rk("tfc", "food/cod"))
                .addOptional(rk("tfc", "food/salmon"))
                .addOptional(rk("tfc", "food/tropical_fish"))
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
        //ModItems.HATS_REGISTRY.getEntries().stream().forEach(o -> tag(StarcatcherTags.EQUIPMENTS).add(o.get()));

        //gadgets
        tag(SCTags.GADGETS).add(rk(FISH_RADAR.get()));

        //hooks tag
        HOOKS_REGISTRY.getEntries().forEach(o -> tag(SCTags.HOOKS).add(rk(o.get())));
        tag(SCTags.HOOKS).addOptional(rk("tide", "void_fishing_hook"));

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
                .add(rk(Items.BUCKET))
                .add(rk(AURORA.get()))
                .add(rk(CONCH.asItem()))
                .add(rk(CLAM.asItem()))
        ;

        //hats
        HATS.getEntries().forEach(o -> tag(SCTags.HATS).add(rk(o.getId())));

        //equippable hats
        tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .addTag(SCTags.HATS);

        tag(SCTags.PLACEABLE_IN_DISPLAY)
                .addTag(SCTags.BUCKETABLE_FISHES)
                .add(rk(GUIDE.get()))
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

        tag(SCTags.PLACEABLE_IN_TACKLE_BOX_FISH_SLOT)
                .addTag(ItemTags.FISHES)
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

    }


    public static Identifier rl(String ns, String path)
    {
        return Identifier.fromNamespaceAndPath(ns, path);
    }
}
