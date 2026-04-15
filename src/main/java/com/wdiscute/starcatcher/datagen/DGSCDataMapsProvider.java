package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlock;
import com.wdiscute.starcatcher.registry.catchmodifiers.SCCatchModifiers;
import com.wdiscute.starcatcher.registry.minigamemodifiers.SCMinigameModifiers;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.starcatcher.sellingbin.FishProcessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.nikdo53.neobackports.datagen.DataMapProvider;
import net.nikdo53.neobackports.datamaps.NeoForgeDataMaps;
import net.nikdo53.neobackports.datamaps.builtin.Compostable;

import javax.annotation.processing.AbstractProcessor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DGSCDataMapsProvider extends DataMapProvider
{
    protected DGSCDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider)
    {
        var aquarium = this.builder(SCDataMaps.AQUARIUM_INTERACTION);
        var currencies = this.builder(SBDataMaps.SELLING_BIN_CURRENCIES);
        var sellable = this.builder(SBDataMaps.SELLING_BIN_VALUE);
        var compostable = this.builder(NeoForgeDataMaps.COMPOSTABLES);
        var catchModifiers = this.builder(SCDataMaps.CATCH_MODIFIERS);
        var minigameModifiers = this.builder(SCDataMaps.MINIGAME_MODIFIERS);
        var tackleSkin = this.builder(SCDataMaps.TACKLE_SKIN);

        //ground
        aquarium.add(Items.GRAVEL.builtInRegistryHolder(), AquariumBlock.Interaction.PLACE_GRAVEL, false);
        aquarium.add(Items.SAND.builtInRegistryHolder(), AquariumBlock.Interaction.PLACE_SAND, false);
        aquarium.add(Items.RED_SAND.builtInRegistryHolder(), AquariumBlock.Interaction.PLACE_RED_SAND, false);
        aquarium.add(Items.STONE.builtInRegistryHolder(), AquariumBlock.Interaction.PLACE_STONE, false);

        //decorations
        aquarium.add(ItemTags.SHOVELS, AquariumBlock.Interaction.BUILD_CASTLE, false);
        aquarium.add(ItemTags.PICKAXES, AquariumBlock.Interaction.BUILD_CAVE, false);
        aquarium.add(Items.KELP.builtInRegistryHolder(), AquariumBlock.Interaction.PLACE_KELP, false);
        aquarium.add(Items.SEAGRASS.builtInRegistryHolder(), AquariumBlock.Interaction.PLACE_SEAGRASS, false);
        aquarium.add(SCBlocks.CONCH.get().asItem().builtInRegistryHolder(), AquariumBlock.Interaction.PLACE_CONCH, false);
        aquarium.add(SCBlocks.CLAM.get().asItem().builtInRegistryHolder(), AquariumBlock.Interaction.PLACE_CLAM, false);

        aquarium.add(SCItems.STARCAUGHT_BUCKET, AquariumBlock.Interaction.PLACE_FISH, false);
        aquarium.add(SCTags.BUCKETABLE_FISHES, AquariumBlock.Interaction.PLACE_FISH_CREATIVE, false);
        aquarium.add(Items.BUCKET.builtInRegistryHolder(), AquariumBlock.Interaction.RETRIEVE_FISH, false);

        //compostable
        compostable.add(SCTags.WORMS, new Compostable(0.65F, false), false);
        compostable.add(SCTags.BUCKETABLE_FISHES, new Compostable(0.9F, false), false);

        //selling sellable datagen
        //shouldn't be run as the JSONs are manually moved to a
        //built-in datapack instead of hard coded into the mod's resources
        if (false)
        {
            //selling sellable currencies
            currencies.add(Items.EMERALD.builtInRegistryHolder(), 100, false);
            currencies.add(Items.EMERALD_BLOCK.builtInRegistryHolder(), 900, false);

            //selling sellable fishes
            Map<ResourceLocation, Float> qualities = Map.of(
                    new ResourceLocation("quality_food", "diamond"), 2f,
                    new ResourceLocation("quality_food", "gold"), 1.5f,
                    new ResourceLocation("quality_food", "iron"), 1.25f
            );

            sellable.add(SCTags.COMMON_FISHES, new SBDataMaps.ItemValue(25, List.of(
                    new FishProcessor(2, 10f),
                    new QualityFoodsProcessor(qualities)
            )), false);

            sellable.add(SCTags.UNCOMMON_FISHES, new SBDataMaps.ItemValue(50, List.of(
                    new FishProcessor(2, 10f),
                    new QualityFoodsProcessor(qualities)
            )), false);

            sellable.add(SCTags.RARE_FISHES, new SBDataMaps.ItemValue(100, List.of(
                    new FishProcessor(2, 10f),
                    new QualityFoodsProcessor(qualities)
            )), false);

            sellable.add(SCTags.EPIC_FISHES, new SBDataMaps.ItemValue(150, List.of(
                    new FishProcessor(2, 10f),
                    new QualityFoodsProcessor(qualities)
            )), false);

            sellable.add(SCTags.LEGENDARY_FISHES, new SBDataMaps.ItemValue(200, List.of(
                    new FishProcessor(2, 10f),
                    new QualityFoodsProcessor(qualities)
            )), false);

            sellable.add(SCItems.PEARL.get().asItem().builtInRegistryHolder(), AbstractProcessor.createEmpty(50), false);
        }


        //minigame modifiers
        minigameModifiers.add(SCItems.FROZEN_HOOK, List.of(SCMinigameModifiers.PREVENT_FROZEN.getId()), false);
        minigameModifiers.add(SCItems.SHINY_HOOK, List.of(SCMinigameModifiers.SPAWN_TREASURE_ON_THREE_HITS.getId()), false);
        minigameModifiers.add(SCItems.MOSSY_HOOK, List.of(SCMinigameModifiers.HARDER_WITH_TREASURE_ON_PERFECT.getId()), false);
        minigameModifiers.add(SCItems.STONE_HOOK, List.of(SCMinigameModifiers.STOP_DECAY_ON_HIT.getId()), false);
        minigameModifiers.add(SCItems.HEAVY_HOOK, List.of(SCMinigameModifiers.SLOWER_MOVING_SWEET_SPOTS.getId()), false);
        minigameModifiers.add(SCItems.STEADY_BOBBER, List.of(SCMinigameModifiers.BIGGER_GREEN_SWEET_SPOTS.getId()), false);
        minigameModifiers.add(SCItems.CLEAR_BOBBER, List.of(SCMinigameModifiers.SLOWER_VANISHING.getId()), false);
        minigameModifiers.add(SCItems.AQUA_BOBBER, List.of(SCMinigameModifiers.ADD_AQUA_SWEET_SPOT.getId()), false);
        minigameModifiers.add(SCItems.COPPER_HOOK, List.of(SCMinigameModifiers.FASTER_POINTER_SPEED.getId()), false);
        minigameModifiers.add(SCItems.EXPOSED_COPPER_HOOK, List.of(SCMinigameModifiers.SLIGHTLY_FASTER_POINTER_SPEED.getId()), false);
        minigameModifiers.add(SCItems.WEATHERED_COPPER_HOOK, List.of(SCMinigameModifiers.SLIGHTLY_SLOWER_POINTER_SPEED.getId()), false);
        minigameModifiers.add(SCItems.OXIDISED_COPPER_HOOK, List.of(SCMinigameModifiers.SLOWER_POINTER_SPEED.getId()), false);
        minigameModifiers.add(SCItems.LEAF_BOBBER, List.of(SCMinigameModifiers.ADD_LEAVES.getId()), false);
        minigameModifiers.add(SCItems.SLIMEY_BOBBER, List.of(SCMinigameModifiers.BOUNCE_BACK.getId()), false);
        minigameModifiers.add(SCItems.DEV_WORM, List.of(SCMinigameModifiers.NEVER_LOSE.getId()), false);

        //catch modifiers
        catchModifiers.add(SCItems.SHINY_HOOK, List.of(SCCatchModifiers.HIDE_CATCH.getFirst()), false);
        catchModifiers.add(SCItems.ECHOING_HOOK, List.of(SCCatchModifiers.GUARANTEE_NEW_FISH_ALWAYS.getFirst()), false);
        catchModifiers.add(SCItems.AMETHYST_HOOK, List.of(SCCatchModifiers.SURVIVES_LAVA.getFirst()), false);
        catchModifiers.add(SCItems.GOLD_HOOK, List.of(SCCatchModifiers.ADD_5_GOLDEN_CHANCE.getFirst()), false);
        catchModifiers.add(SCItems.STONE_HOOK, List.of(SCCatchModifiers.INCREASE_LURE_TIME.getFirst()), false);
        catchModifiers.add(SCItems.SPLIT_HOOK, List.of(SCCatchModifiers.EXTRA_ITEM.getFirst()), false);
        catchModifiers.add(SCItems.VANILLA_BOBBER, List.of(SCCatchModifiers.VANILLA_LOOT.getFirst()), false);
        catchModifiers.add(SCItems.VANILLA_HOOK, List.of(SCCatchModifiers.SKIP_MINIGAME_IF_VANILLA_LOOT.getFirst()), false);
        catchModifiers.add(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.builtInRegistryHolder(), List.of(SCCatchModifiers.SURVIVES_LAVA.getFirst()), false);

        catchModifiers.add(SCItems.WORM, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst()), false);
        catchModifiers.add(SCItems.ALMIGHTY_WORM, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst(), SCCatchModifiers.FISH_ENTITY.getFirst()), false);
        catchModifiers.add(SCItems.SEEKING_WORM, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst(), SCCatchModifiers.GUARANTEE_NEW_FISH_HALF.getFirst()), false);

        catchModifiers.add(SCItems.GUNPOWDER_BAIT, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst()), false);
        catchModifiers.add(SCItems.CHERRY_BAIT, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst()), false);
        catchModifiers.add(SCItems.LUSH_BAIT, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst()), false);
        catchModifiers.add(SCItems.SCULK_BAIT, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst()), false);
        catchModifiers.add(SCItems.DRIPSTONE_BAIT, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst()), false);
        catchModifiers.add(SCItems.MURKWATER_BAIT, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst()), false);
        catchModifiers.add(SCItems.LEGENDARY_BAIT, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst()), false);
        catchModifiers.add(SCItems.METEOROLOGICAL_BAIT, List.of(SCCatchModifiers.DECREASES_LURE_TIME.getFirst(), SCCatchModifiers.IGNORE_DAYTIME_AND_WEATHER_RESTRICTIONS.getFirst()), false);

        catchModifiers.add(SCItems.DEV_WORM, List.of(
                SCCatchModifiers.BIG_DECREASES_LURE_TIME.getFirst(),
                SCCatchModifiers.BIG_DECREASES_LURE_TIME.getFirst(),
                SCCatchModifiers.BIG_DECREASES_LURE_TIME.getFirst(),
                SCCatchModifiers.BIG_DECREASES_LURE_TIME.getFirst(),
                SCCatchModifiers.BIG_DECREASES_LURE_TIME.getFirst()), false);

        //tackle skins
        tackleSkin.add(SCItems.PEARL_SMITHING_TEMPLATE, SCTackleSkins.PEARL_TACKLE_SKIN, false);
        tackleSkin.add(SCItems.KING_SMITHING_TEMPLATE, SCTackleSkins.KING_TACKLE_SKIN, false);
        tackleSkin.add(SCItems.COLORFUL_SMITHING_TEMPLATE, SCTackleSkins.COLORFUL_TACKLE_SKIN, false);
        tackleSkin.add(SCItems.CLEAR_SMITHING_TEMPLATE, SCTackleSkins.CLEAR_TACKLE_SKIN, false);
        tackleSkin.add(SCItems.FROG_SMITHING_TEMPLATE, SCTackleSkins.FROG_TACKLE_SKIN, false);
        tackleSkin.add(SCItems.KIMBE_SMITHING_TEMPLATE, SCTackleSkins.KIMBE_TACKLE_SKIN, false);


    }


}
