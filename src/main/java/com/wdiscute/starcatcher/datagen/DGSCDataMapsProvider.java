package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.aquarium.Decoration;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fish.Treasure;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.*;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.*;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.interaction.SCAquariumInteractions;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.utils.Utils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.List;
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
        var decoration = this.builder(SCDataMaps.AQUARIUM_DECORATION);
        var ground = this.builder(SCDataMaps.AQUARIUM_GROUND);
        //var currencies = this.builder(SBDataMaps.SELLING_BIN_CURRENCIES);
        //var sellable = this.builder(SBDataMaps.SELLING_BIN_VALUE);
        var compostable = this.builder(NeoForgeDataMaps.COMPOSTABLES);
        var modifiers = this.builder(SCDataMaps.ITEM_MODIFIERS);
        var modifiers_effects = this.builder(SCDataMaps.EFFECT_MODIFIERS);
        var modifiers_enchants = this.builder(SCDataMaps.ENCHANTMENT_MODIFIERS);
        var tackleSkin = this.builder(SCDataMaps.TACKLE_SKIN);
        var messages = this.builder(SCDataMaps.MESSAGE_BACKGROUND);

        //messages background
        messages.add(BuiltinDimensionTypes.OVERWORLD, Message.BACKGROUND_OVERWORLD, false);
        messages.add(BuiltinDimensionTypes.NETHER, Message.BACKGROUND_NETHER, false);
        messages.add(BuiltinDimensionTypes.END, Message.BACKGROUND_END, false);


        //
        //                                           ,--.
        // ,--,--.  ,---.  ,--.,--.  ,--,--. ,--.--. `--' ,--.,--. ,--,--,--.
        //' ,-.  | | .-. | |  ||  | ' ,-.  | |  .--' ,--. |  ||  | |        |
        //\ '-'  | ' '-' | '  ''  ' \ '-'  | |  |    |  | '  ''  ' |  |  |  |
        // `--`--'  `-|  |  `----'   `--`--' `--'    `--'  `----'  `--`--`--'
        //,--.        `--',--.                                     ,--.   ,--.
        //`--' ,--,--,  ,-'  '-.  ,---.  ,--.--.  ,--,--.  ,---. ,-'  '-. `--'  ,---.  ,--,--,   ,---.
        //,--. |      \ '-.  .-' | .-. : |  .--' ' ,-.  | | .--' '-.  .-' ,--. | .-. | |      \ (  .-'
        //|  | |  ||  |   |  |   \   --. |  |    \ '-'  | \ `--.   |  |   |  | ' '-' ' |  ||  | .-'  `)
        //`--' `--''--'   `--'    `----' `--'     `--`--'  `---'   `--'   `--'  `---'  `--''--' `----'
        //

        //simple
        decoration.add(Items.SMALL_AMETHYST_BUD.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.MEDIUM_AMETHYST_BUD.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.LARGE_AMETHYST_BUD.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.AMETHYST_CLUSTER.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.TORCH.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);

        //non-fan coral
        decoration.add(Items.BRAIN_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.BUBBLE_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.FIRE_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.HORN_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.TUBE_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);

        //dead non-fan coral
        decoration.add(Items.DEAD_BRAIN_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.DEAD_BUBBLE_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.DEAD_FIRE_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.DEAD_HORN_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);
        decoration.add(Items.DEAD_TUBE_CORAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SIMPLE.get()), false);



        //corals
        decoration.add(Items.BRAIN_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);
        decoration.add(Items.BUBBLE_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);
        decoration.add(Items.FIRE_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);
        decoration.add(Items.HORN_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);
        decoration.add(Items.TUBE_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);

        //dead corals
        decoration.add(Items.DEAD_BRAIN_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);
        decoration.add(Items.DEAD_BUBBLE_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);
        decoration.add(Items.DEAD_FIRE_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);
        decoration.add(Items.DEAD_HORN_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);
        decoration.add(Items.DEAD_TUBE_CORAL_FAN.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.CORAL_FAN.get()), false);

        //uniques
        decoration.add(Items.KELP.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.KELP.get(), Vec3.ZERO, new Vec3(0.3d, 0, 0.3d)), false);
        decoration.add(Items.SEAGRASS.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SEAGRASS.get(), Vec3.ZERO, new Vec3(0.3d, 0, 0.3d)), false);
        decoration.add(Items.SEA_PICKLE.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.SEA_PICKLE.get(), Vec3.ZERO, new Vec3(0.3d, 0, 0.3d)), false);
        decoration.add(Items.BONE_MEAL.builtInRegistryHolder(), new Decoration(SCAquariumInteractions.BONE_MEAL.get()), false);

        //ground
        ground.add(SCTags.AQUARIUM_GROUND, Starcatcher.MISSINGNO, false);




        //compostable
        compostable.add(SCTags.WORMS, new Compostable(0.65F, false), false);
        compostable.add(SCTags.BUCKETABLE_FISHES, new Compostable(0.9F, false), false);

        //selling sellable datagen
        //shouldn't be run as the JSONs are manually moved to a
        //built-in datapack instead of hard coded into the mod's resources
        //if (false)
        //{
        //    //selling sellable currencies
        //    currencies.add(Items.EMERALD.builtInRegistryHolder(), 100, false);
        //    currencies.add(Items.EMERALD_BLOCK.builtInRegistryHolder(), 900, false);
//
        //    //selling sellable fishes
        //    Map<ResourceLocation, Float> qualities = Map.of(
        //            ResourceLocation.fromNamespaceAndPath("quality_food", "diamond"), 2f,
        //            ResourceLocation.fromNamespaceAndPath("quality_food", "gold"), 1.5f,
        //            ResourceLocation.fromNamespaceAndPath("quality_food", "iron"), 1.25f
        //    );
//
        //    sellable.add(SCTags.COMMON_FISHES, new SBDataMaps.ItemValue(25, List.of(
        //            new FishProcessor(2, 10f),
        //            new QualityFoodsProcessor(qualities)
        //    )), false);
//
        //    sellable.add(SCTags.UNCOMMON_FISHES, new SBDataMaps.ItemValue(50, List.of(
        //            new FishProcessor(2, 10f),
        //            new QualityFoodsProcessor(qualities)
        //    )), false);
//
        //    sellable.add(SCTags.RARE_FISHES, new SBDataMaps.ItemValue(100, List.of(
        //            new FishProcessor(2, 10f),
        //            new QualityFoodsProcessor(qualities)
        //    )), false);
//
        //    sellable.add(SCTags.EPIC_FISHES, new SBDataMaps.ItemValue(150, List.of(
        //            new FishProcessor(2, 10f),
        //            new QualityFoodsProcessor(qualities)
        //    )), false);
//
        //    sellable.add(SCTags.LEGENDARY_FISHES, new SBDataMaps.ItemValue(200, List.of(
        //            new FishProcessor(2, 10f),
        //            new QualityFoodsProcessor(qualities)
        //    )), false);
//
        //    sellable.add(SCItems.PEARL.get().asItem().builtInRegistryHolder(), AbstractProcessor.createEmpty(50), false);
        //}


        //
        //                      ,--. ,--.  ,---. ,--.
        //,--,--,--.  ,---.   ,-|  | `--' /  .-' `--'  ,---.  ,--.--.  ,---.
        //|        | | .-. | ' .-. | ,--. |  `-, ,--. | .-. : |  .--' (  .-'
        //|  |  |  | ' '-' ' \ `-' | |  | |  .-' |  | \   --. |  |    .-'  `)
        //`--`--`--'  `---'   `---'  `--' `--'   `--'  `----' `--'    `----'
        //

        modifiers.add(SCItems.SLIMEY_BOBBER, List.of(
                new BounceBackModifier("")
        ), false);

        modifiers.add(SCItems.CLEAR_BOBBER, List.of(
                new AdjustVanishingRate(0.3f, "")
        ), false);

        modifiers.add(SCItems.STEADY_BOBBER, List.of(
                new SteadyBobberModifier("")
        ), false);

        modifiers.add(SCItems.HEAVY_HOOK, List.of(
                new AdjustMovingModifier(0.75f, "")
        ), false);

        modifiers.add(SCItems.COPPER_HOOK, List.of(
                new AdjustBaseHandleSpeedModifier(1.5f, "")
        ), false);

        modifiers.add(SCItems.EXPOSED_COPPER_HOOK, List.of(
                new AdjustBaseHandleSpeedModifier(1.25f, "")
        ), false);

        modifiers.add(SCItems.WEATHERED_COPPER_HOOK, List.of(
                new AdjustBaseHandleSpeedModifier(0.75f, "")
        ), false);

        modifiers.add(SCItems.OXIDISED_COPPER_HOOK, List.of(
                new AdjustBaseHandleSpeedModifier(0.5f, "")
        ), false);

        modifiers.add(SCItems.LEAF_BOBBER, List.of(
                new AddLeavesSweetspotsModifier(0.025f, "")
        ), false);

        modifiers.add(SCItems.DRIPSTONE_BOBBER, List.of(
                new AddBasicSweetSpotModifier(Difficulty.SweetSpot.DRIPSTONE, "tooltip.modifier.starcatcher.dripstone_bobber")
        ), false);

        modifiers.add(SCItems.GLOWING_BOBBER, List.of(
                new AddBasicSweetSpotModifier(Difficulty.SweetSpot.GLOWING, "tooltip.modifier.starcatcher.glowing_bobber")
        ), false);

        modifiers.add(SCItems.MOSSY_HOOK, List.of(
                new AwardTreasureOnPerfectCatch(0.2f, "")
        ), false);

        modifiers.add(SCItems.FROZEN_HOOK, List.of(
                new PreventFrozenModifier("")
        ), false);

        modifiers.add(SCItems.RUSTY_HOOK, List.of(
                new AdjustPenaltyModifier(3, ""),
                new AdjustHPModifier(0.75f, "")
        ), false);

        modifiers.add(SCItems.SHINY_HOOK, List.of(
                new HideCatchModifier(1, ""),
                new SpawnTreasureOnHitX(3, "")
        ), false);

        modifiers.add(SCItems.ECHOING_HOOK, List.of(
                new NewCatchIncreaseModifier(15, ""),
                new DeepDarkModifier("")
        ), false);

        modifiers.add(SCItems.AMETHYST_HOOK, List.of(
                new SurvivesLavaModifier("")
        ), false);

        modifiers.add(SCItems.GOLD_HOOK, List.of(
                new ExtraGoldenChanceModifier(0.05f, false, "")
        ), false);

        modifiers.add(SCItems.GOLDEN_BOBBER, List.of(
                new ExtraGoldenChanceModifier(0.05f, false, "")
        ), false);

        modifiers.add(SCItems.CLOUD_BOBBER, List.of(
                new NoGravityModifier("")
        ), false);

        modifiers.add(SCItems.STONE_HOOK, List.of(
                new AdjustLureTimeModifier(1.2f, 1.6f, 1f, ""),
                new StoneHookModifier("")
        ), false);

        modifiers.add(SCItems.SPLIT_HOOK, List.of(
                new ExtraBaseCatchModifier(1, true, "")
        ), false);

        modifiers.add(SCItems.VANILLA_BOBBER, List.of(
                new VanillaFishingLootModifier(1, ""),
                new TriggersSkipMinigameModifier("hide")
        ), false);

        modifiers.add(SCItems.VANILLA_HOOK, List.of(
                new SkipMinigameIfTriggerFoundModifier("tooltip.modifier.starcatcher.skip_minigame_if_vanilla_bobber")
        ), false);

        //templates
        modifiers.add(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.builtInRegistryHolder(), List.of(
                new SurvivesLavaModifier(""),
                new ExtraGoldenChanceModifier(0.1f, true, ""),
                new QualityFoodRollModifier(0, 5, false, false, 100, ""),
                new AdjustHPModifier(0.75f, "")
        ), false);

        //worms
        modifiers.add(SCItems.WORM, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, "")
        ), false);

        modifiers.add(SCItems.ALMIGHTY_WORM, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, ""),
                new ForceFishEntityModifier(1, "")
        ), false);

        modifiers.add(SCItems.SEEKING_WORM, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, ""),
                new NewCatchIncreaseModifier(5, "")
        ), false);

        //baits
        modifiers.add(SCItems.GUNPOWDER_BAIT, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, ""),
                new EmptyModifier("tooltip.modifier.starcatcher.gunpowder_bait")
        ), false);

        modifiers.add(SCItems.CHERRY_BAIT, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, ""),
                new EmptyModifier("tooltip.modifier.starcatcher.cherry_bait")
        ), false);

        modifiers.add(SCItems.LUSH_BAIT, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, ""),
                new EmptyModifier("tooltip.modifier.starcatcher.lush_bait")
        ), false);

        modifiers.add(SCItems.SCULK_BAIT, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, ""),
                new EmptyModifier("tooltip.modifier.starcatcher.sculk_bait")
        ), false);

        modifiers.add(SCItems.DRIPSTONE_BAIT, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, ""),
                new EmptyModifier("tooltip.modifier.starcatcher.dripstone_bait")
        ), false);

        modifiers.add(SCItems.MURKWATER_BAIT, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, ""),
                new EmptyModifier("tooltip.modifier.starcatcher.murkwater_bait")
        ), false);

        modifiers.add(SCItems.LEGENDARY_BAIT, List.of(
                new AdjustLureTimeModifier(0.5f, 0.6f, 1.5f, ""),
                new EmptyModifier("tooltip.modifier.starcatcher.legendary_bait")
        ), false);

        modifiers.add(SCItems.METEOROLOGICAL_BAIT, List.of(
                new AdjustLureTimeModifier(0.7f, 0.8f, 1.3f, ""),
                new IgnoreDaytimeRestrictionsModifier(1f, ""),
                new IgnoreWeatherRestrictionsModifier(1f, "")
        ), false);


        //angler's hat (artifacts / reliquified artifacts compat)
        //modifiers.add(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("artifacts", "anglers_hat")),
        //        List.of(
        //                new AnglersHatModifier("")
        //        ), false);


        //hats
        modifiers.add(SCBlocks.FISHERMAN_HAT_CYAN.asItem().builtInRegistryHolder(),
                List.of(
                        new AdjustDecayRateModifier(0.7f, ""),
                        new AdjustPenaltyModifier(0.7f, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_PURPLE.asItem().builtInRegistryHolder(),
                List.of(
                        new AdjustBaseHandleSpeedModifier(0.75f, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_MAGENTA.asItem().builtInRegistryHolder(),
                List.of(
                        new AdjustBaseHandleSpeedModifier(1.25f, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_PINK.asItem().builtInRegistryHolder(),
                List.of(
                        new AdjustBaseHandleSpeedModifier(5f, ""),
                        new BurnOnMissModifier(20, 4, 30, ""),
                        new AllowedRaritiesModifier(List.of(Rarity.TRASH), "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_BLACK.asItem().builtInRegistryHolder(),
                List.of(
                        new ExtraBaseCatchModifier(5, false, ""),
                        new CancelGoldenModifier(""),
                        new HideCatchModifier(1, ""),
                        new AdjustPenaltyModifier(5f, ""),
                        new AdjustDecayRateModifier(2f, ""),
                        new AdjustVanishingRate(1.5f, ""),
                        new AdjustMovingModifier(1.5f, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_GRAY.asItem().builtInRegistryHolder(),
                List.of(
                        new ExtraBaseCatchModifier(3, true, ""),
                        new CancelGoldenModifier(""),
                        new HideCatchModifier(1, ""),
                        new AdjustPenaltyModifier(5, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_LIGHT_GRAY.asItem().builtInRegistryHolder(),
                List.of(
                        new ExtraBaseCatchModifier(1, false, ""),
                        new CancelGoldenModifier(""),
                        new HideCatchModifier(1, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_GREEN.asItem().builtInRegistryHolder(),
                List.of(
                        new RestrictedRaritiesModifier(List.of(Rarity.EPIC, Rarity.LEGENDARY), ""),
                        new AdjustHPModifier(0.5f, ""),
                        new ExtraExpBasedOnPerformanceModifier(""),
                        new AdjustDecayRateModifier(1.5f, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_LIME.asItem().builtInRegistryHolder(),
                List.of(
                        new ForceSelectFishFromEntryModifier(
                                1,
                                Utils.rl("creeper"),
                                "tooltip.modifier.starcatcher.add_creeper"
                        ),

                        new AdjustLureTimeModifier(0.5f, 0.5f, 1, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_WHITE.asItem().builtInRegistryHolder(),
                List.of(
                        new RollPercentileModifier(2, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_BROWN.asItem().builtInRegistryHolder(),
                List.of(
                        new BoostThrowSpeedModifier(2.5f, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_YELLOW.asItem().builtInRegistryHolder(),
                List.of(
                        new ExtraGoldenChanceModifier(0.05f, true, ""),
                        new AdjustLureTimeModifier(1.2f, 1.2f, 1.4f, ""),
                        new AdjustPenaltyModifier(1.5f, "")
                ), false);


        modifiers.add(SCBlocks.FISHERMAN_HAT_RED.asItem().builtInRegistryHolder(),
                List.of(
                        new IgnoreDaytimeRestrictionsModifier(0.5f, ""),
                        new FlipEveryHitModifier("")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_BLUE.asItem().builtInRegistryHolder(),
                List.of(
                        new IgnoreWeatherRestrictionsModifier(0.5f, ""),
                        new FlipEveryHitModifier(""),
                        new AdjustBaseHandleSpeedModifier(0.75f, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_LIGHT_BLUE.asItem().builtInRegistryHolder(),
                List.of(
                        new AdjustLureTimeModifier(1.4f, 1.4f, 1.4f, ""),
                        new AddBasicSweetSpotModifier(Difficulty.SweetSpot.DRIPSTONE, "tooltip.modifier.starcatcher.dripstone_bobber"),
                        new FreezeOnMissModifier(40, 10, "")
                ), false);

        modifiers.add(SCBlocks.FISHERMAN_HAT_ORANGE.asItem().builtInRegistryHolder(),
                List.of(
                        new AddBasicSweetSpotModifier(Difficulty.SweetSpot.GLOWING, "tooltip.modifier.starcatcher.glowing_bobber")
                ), false);

        //
        //         ,---.  ,---.                  ,--.
        // ,---.  /  .-' /  .-'  ,---.   ,---. ,-'  '-.  ,---.
        //| .-. : |  `-, |  `-, | .-. : | .--' '-.  .-' (  .-'
        //\   --. |  .-' |  .-' \   --. \ `--.   |  |   .-'  `)
        // `----' `--'   `--'    `----'  `---'   `--'   `----'
        //

        //modifiers_effects.add(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(MobEffects.LUCK.value()),
        //        List.of(
        //                new AdjustBaseHandleSpeedModifier(1.25f, "")
        //        ), false);


        //
        //                        ,--.                          ,--.
        // ,---.  ,--,--,   ,---. |  ,---.   ,--,--. ,--,--,  ,-'  '-.  ,---.
        //| .-. : |      \ | .--' |  .-.  | ' ,-.  | |      \ '-.  .-' (  .-'
        //\   --. |  ||  | \ `--. |  | |  | \ '-'  | |  ||  |   |  |   .-'  `)
        // `----' `--''--'  `---' `--' `--'  `--`--' `--''--'   `--'   `----'
        //

        modifiers_enchants.add(Enchantments.LUCK_OF_THE_SEA,
                List.of(
                        new SpawnTreasureModifier(0.02f, "hide")
                ), false);

        modifiers_enchants.add(Enchantments.LURE,
                List.of(
                        new AdjustLureTimeModifier(0.95f, 0.95f, 1f, "hide")
                ), false);


        //
        //  ,--.                   ,--.     ,--.                     ,--.     ,--.
        //,-'  '-.  ,--,--.  ,---. |  |,-.  |  |  ,---.       ,---.  |  |,-.  `--' ,--,--,   ,---.
        //'-.  .-' ' ,-.  | | .--' |     /  |  | | .-. :     (  .-'  |     /  ,--. |      \ (  .-'
        //  |  |   \ '-'  | \ `--. |  \  \  |  | \   --.     .-'  `) |  \  \  |  | |  ||  | .-'  `)
        //  `--'    `--`--'  `---' `--'`--' `--'  `----'     `----'  `--'`--' `--' `--''--' `----'
        //


        tackleSkin.add(SCItems.PEARL_SMITHING_TEMPLATE, SCTackleSkins.PEARL_TACKLE_SKIN.get(), false);
        tackleSkin.add(SCItems.KING_SMITHING_TEMPLATE, SCTackleSkins.KING_TACKLE_SKIN.get(), false);
        tackleSkin.add(SCItems.COLORFUL_SMITHING_TEMPLATE, SCTackleSkins.COLORFUL_TACKLE_SKIN.get(), false);
        tackleSkin.add(SCItems.CLEAR_SMITHING_TEMPLATE, SCTackleSkins.CLEAR_TACKLE_SKIN.get(), false);
        tackleSkin.add(SCItems.FROG_SMITHING_TEMPLATE, SCTackleSkins.FROG_TACKLE_SKIN.get(), false);
        tackleSkin.add(SCItems.KIMBE_SMITHING_TEMPLATE, SCTackleSkins.KIMBE_TACKLE_SKIN.get(), false);
        tackleSkin.add(SCItems.VALLEY_SMITHING_TEMPLATE, SCTackleSkins.VALLEY_TACKLE_SKIN.get(), false);
        tackleSkin.add(SCItems.SURVIVOR_SMITHING_TEMPLATE, SCTackleSkins.SURVIVOR_TACKLE_SKIN.get(), false);


        //
        //  ,--.
        //,-'  '-. ,--.--.  ,---.   ,--,--.  ,---.  ,--.,--. ,--.--.  ,---.   ,---.
        //'-.  .-' |  .--' | .-. : ' ,-.  | (  .-'  |  ||  | |  .--' | .-. : (  .-'
        //  |  |   |  |    \   --. \ '-'  | .-'  `) '  ''  ' |  |    \   --. .-'  `)
        //  `--'   `--'     `----'  `--`--' `----'   `----'  `--'     `----' `----'
        //

        var treasures = this.builder(SCDataMaps.TREASURE);

        //add rod treasure to every fish
        treasures.add(SCTags.COMMON_ENTRIES_FP, Treasure.VANILLA_FISHING_LOOT_TABLE, false);
        treasures.add(SCTags.UNCOMMON_ENTRIES_FP, Treasure.VANILLA_FISHING_LOOT_TABLE, false);
        treasures.add(SCTags.RARE_ENTRIES_FP, Treasure.VANILLA_FISHING_LOOT_TABLE, false);
        treasures.add(SCTags.EPIC_ENTRIES_FP, Treasure.VANILLA_FISHING_LOOT_TABLE, false);
        treasures.add(SCTags.LEGENDARY_ENTRIES_FP, Treasure.VANILLA_FISHING_LOOT_TABLE, false);


        treasures.add(Starcatcher.rl("azure_crystalback_minnow"), Treasure.AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE, false);
        treasures.add(Starcatcher.rl("willish"), Treasure.KIMBE_SMITHING_TEMPLATE, false);
        treasures.add(Starcatcher.rl("vesani"), Treasure.COLORFUL_SMITHING_TEMPLATE, false);
        treasures.add(Starcatcher.rl("boreal"), Treasure.CLEAR_SMITHING_TEMPLATE, false);
        treasures.add(Starcatcher.rl("cerberay"), Treasure.KING_SMITHING_TEMPLATE, false);
        treasures.add(Starcatcher.rl("aurora"), Treasure.ICEBORN_SKIN_SMITHING_TEMPLATE, false);
        treasures.add(Starcatcher.rl("cloudfin"), Treasure.SKY_SKIN_SMITHING_TEMPLATE, false);
        treasures.add(Starcatcher.rl("joel"), Treasure.SHARK_SKIN_SMITHING_TEMPLATE, false);
        treasures.add(Starcatcher.rl("lush_pike"), Treasure.LUSH_SKIN_SMITHING_TEMPLATE, false);


    }

    public static TagKey<FishProperties> fp(String s)
    {
        return TagKey.create(Starcatcher.FISH_REGISTRY_KEY, Starcatcher.rl(s));
    }
}
