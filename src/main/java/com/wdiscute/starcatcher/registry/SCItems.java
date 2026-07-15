package com.wdiscute.starcatcher.registry;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.compat.CreateCompat;
import com.wdiscute.starcatcher.guide.FishingGuideItem;
import com.wdiscute.starcatcher.messageinabottle.letter.BottledLetterItem;
import com.wdiscute.starcatcher.messageinabottle.letter.LetterItem;
import com.wdiscute.starcatcher.messageinabottle.message.MessageInABottleItem;
import com.wdiscute.starcatcher.messageinabottle.message.MessageItem;
import com.wdiscute.starcatcher.registry.items.*;
import com.wdiscute.starcatcher.messageinabottle.*;
import com.wdiscute.utils.item.BasicItem;
import com.wdiscute.utils.item.FireResistantBasicItem;
import com.wdiscute.utils.item.SingleStackBasicItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public interface SCItems
{

    static void registerExtraItems()
    {
        if (ModList.get().isLoaded("create") || DatagenModLoader.isRunningDataGen())
            CreateCompat.register();
    }

    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items NON_BUCKETABLE_FISH_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items BUCKETABLE_FISHES_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items NON_FISH_FISH_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items TEMPLATES_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items RODS_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items HOOKS_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items BOBBERS_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);

    DeferredItem<Item> MISSINGNO = ITEMS.register("missingno", BasicItem::new);
    DeferredItem<Item> UNKNOWN_FISH = ITEMS.register("unknown_fish", BasicItem::new);

    DeferredItem<Item> GUIDE = ITEMS.register("starcatcher_guide", FishingGuideItem::new);

    DeferredItem<Item> FISH_RADAR = ITEMS.register("fish_radar", SingleStackBasicItem::new);
    DeferredItem<Item> STARCATCHER_TWINE = ITEMS.register("starcatcher_twine", SingleStackBasicItem::new);

    DeferredItem<Item> SETTINGS = ITEMS.register("settings", () -> new Item(new Item
            .Properties().component(Tooltips.TOOLTIP_ALWAYS, List.of("wadwad"))));
    DeferredItem<Item> TREASURE = ITEMS.register("treasure", BasicItem::new);

    //hooks
    DeferredItem<Item> HOOK = HOOKS_REGISTRY.register("hook", SingleStackBasicItem::new);
    DeferredItem<Item> AMETHYST_HOOK = HOOKS_REGISTRY.register("amethyst_hook", SingleStackBasicItem::new);
    DeferredItem<Item> SHINY_HOOK = HOOKS_REGISTRY.register("shiny_hook", SingleStackBasicItem::new);
    DeferredItem<Item> GOLD_HOOK = HOOKS_REGISTRY.register("gold_hook", SingleStackBasicItem::new);
    DeferredItem<Item> MOSSY_HOOK = HOOKS_REGISTRY.register("mossy_hook", SingleStackBasicItem::new);
    DeferredItem<Item> STONE_HOOK = HOOKS_REGISTRY.register("stone_hook", SingleStackBasicItem::new);
    DeferredItem<Item> SPLIT_HOOK = HOOKS_REGISTRY.register("split_hook", SingleStackBasicItem::new);
    DeferredItem<Item> HEAVY_HOOK = HOOKS_REGISTRY.register("heavy_hook", SingleStackBasicItem::new);
    DeferredItem<Item> VANILLA_HOOK = HOOKS_REGISTRY.register("vanilla_hook", SingleStackBasicItem::new);
    DeferredItem<Item> COPPER_HOOK = HOOKS_REGISTRY.register("copper_hook", SingleStackBasicItem::new);
    DeferredItem<Item> EXPOSED_COPPER_HOOK = HOOKS_REGISTRY.register("exposed_copper_hook", SingleStackBasicItem::new);
    DeferredItem<Item> WEATHERED_COPPER_HOOK = HOOKS_REGISTRY.register("weathered_copper_hook", SingleStackBasicItem::new);
    DeferredItem<Item> OXIDISED_COPPER_HOOK = HOOKS_REGISTRY.register("oxidised_copper_hook", SingleStackBasicItem::new);
    DeferredItem<Item> ECHOING_HOOK = HOOKS_REGISTRY.register("echoing_hook", SingleStackBasicItem::new);
    DeferredItem<Item> FROZEN_HOOK = HOOKS_REGISTRY.register("frozen_hook", SingleStackBasicItem::new);

    //bobbers
    DeferredItem<Item> BOBBER = BOBBERS_REGISTRY.register("bobber", SingleStackBasicItem::new);
    DeferredItem<Item> STEADY_BOBBER = BOBBERS_REGISTRY.register("steady_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> CLEAR_BOBBER = BOBBERS_REGISTRY.register("clear_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> DRIPSTONE_BOBBER = BOBBERS_REGISTRY.register("dripstone_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> VANILLA_BOBBER = BOBBERS_REGISTRY.register("vanilla_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> LEAF_BOBBER = BOBBERS_REGISTRY.register("leaf_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> SLIMEY_BOBBER = BOBBERS_REGISTRY.register("slimey_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> GLOWING_BOBBER = BOBBERS_REGISTRY.register("glowing_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> GOLDEN_BOBBER = BOBBERS_REGISTRY.register("golden_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> CLOUD_BOBBER = BOBBERS_REGISTRY.register("cloud_bobber", SingleStackBasicItem::new);

    //baits
    DeferredItem<Item> WORM = ITEMS.register("worm", BasicItem::new);
    DeferredItem<Item> ALMIGHTY_WORM = ITEMS.register("almighty_worm", BasicItem::new);
    DeferredItem<Item> SEEKING_WORM = ITEMS.register("seeking_worm", BasicItem::new);

    DeferredItem<Item> GUNPOWDER_BAIT = ITEMS.register("gunpowder_bait", BasicItem::new);
    DeferredItem<Item> CHERRY_BAIT = ITEMS.register("cherry_bait", BasicItem::new);
    DeferredItem<Item> LUSH_BAIT = ITEMS.register("lush_bait", BasicItem::new);
    DeferredItem<Item> SCULK_BAIT = ITEMS.register("sculk_bait", BasicItem::new);
    DeferredItem<Item> DRIPSTONE_BAIT = ITEMS.register("dripstone_bait", BasicItem::new);
    DeferredItem<Item> MURKWATER_BAIT = ITEMS.register("murkwater_bait", BasicItem::new);
    DeferredItem<Item> LEGENDARY_BAIT = ITEMS.register("legendary_bait", BasicItem::new);
    DeferredItem<Item> METEOROLOGICAL_BAIT = ITEMS.register("meteorological_bait", BasicItem::new);


    //tackle templates
    DeferredItem<Item> PEARL_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("pearl_smithing_template", BasicItem::new);
    DeferredItem<Item> KIMBE_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("kimbe_smithing_template", BasicItem::new);
    DeferredItem<Item> COLORFUL_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("colorful_smithing_template", BasicItem::new);
    DeferredItem<Item> CLEAR_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("clear_smithing_template", BasicItem::new);
    DeferredItem<Item> FROG_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("frog_smithing_template", BasicItem::new);
    DeferredItem<Item> KING_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("king_smithing_template", BasicItem::new);
    DeferredItem<Item> VALLEY_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("valley_smithing_template", BasicItem::new);
    DeferredItem<Item> SURVIVOR_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("survivor_smithing_template", BasicItem::new);

    //skin templates
    DeferredItem<Item> NATURALIST_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("naturalist_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> ICEBORN_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("iceborn_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> MAGMAFORGED_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("magmaforged_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> SLIMED_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("slimed_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> SHARKTOOTH_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("sharktooth_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("azure_crystal_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> BAMBOO_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("bamboo_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> OBSIDIAN_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("obsidian_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> BONER_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("boner_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> SKY_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("sky_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("lush_glowberry_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> HUMBLE_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("humble_skin_smithing_template", BasicItem::new);


    //rods
    DeferredItem<Item> ROD = RODS_REGISTRY.register("starcatcher_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> NATURALIST_ROD = RODS_REGISTRY.register("naturalist_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> ICEBORN_ROD = RODS_REGISTRY.register("iceborn_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> MAGMAFORGED_ROD = RODS_REGISTRY.register("magmaforged_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> SLIMED_ROD = RODS_REGISTRY.register("slimed_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> SHARKTOOTH_ROD = RODS_REGISTRY.register("sharktooth_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> AZURE_CRYSTAL_ROD = RODS_REGISTRY.register("azure_crystal_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> BAMBOO_ROD = RODS_REGISTRY.register("bamboo_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> OBSIDIAN_ROD = RODS_REGISTRY.register("obsidian_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> BONER_ROD = RODS_REGISTRY.register("boner_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> SKY_ROD = RODS_REGISTRY.register("sky_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> LUSH_GLOWBERRY_ROD = RODS_REGISTRY.register("lush_glowberry_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> HUMBLE_ROD = RODS_REGISTRY.register("humble_rod", StarcatcherFishingRodItem::new);

    //secrets
    DeferredItem<Item> LETTER = ITEMS.register("letter", LetterItem::new);
    DeferredItem<Item> BOTTLED_LETTER = ITEMS.register("bottled_letter", BottledLetterItem::new);

    DeferredItem<Item> MESSAGE_IN_A_BOTTLE = ITEMS.register("message_in_a_bottle", MessageInABottleItem::new);
    DeferredItem<Item> MESSAGE = ITEMS.register("message", MessageItem::new);

    DeferredItem<Item> BROKEN_BOTTLE = ITEMS.register("broken_bottle", BrokenBottleItem::new);


    DeferredItem<Item> FISH_BONES = ITEMS.register("fish_bones", BasicItem::new);
    DeferredItem<Item> PEARL = ITEMS.register("pearl", BasicItem::new);

    //
    //  ,---. ,--.         ,--.
    // /  .-' `--'  ,---.  |  ,---.   ,---.   ,---.
    // |  `-, ,--. (  .-'  |  .-.  | | .-. : (  .-'
    // |  .-' |  | .-'  `) |  | |  | \   --. .-'  `)
    // `--'   `--' `----'  `--' `--'  `----' `----'
    //

    //lake
    DeferredItem<Item> BOOT = ITEMS.register("boot", BasicItem::new);

    DeferredItem<Item> OBIDONTIEE = registerBucketFish("obidontiee");
    DeferredItem<Item> DRIFTFIN = registerBucketFish("driftfin");
    DeferredItem<Item> RAINFIN = registerNonBucketFish("rainfin");
    DeferredItem<Item> ROCKGILL = registerNonBucketFish("rockgill");
    DeferredItem<Item> PEAKDWELLER = registerNonBucketFish("peakdweller");

    DeferredItem<Item> SILVERVEIL_PERCH = registerBucketFish("silverveil_perch");
    DeferredItem<Item> ELDERSCALE = registerBucketFish("elderscale");
    DeferredItem<Item> SUNNY_STURGEON = registerBucketFish("sunny_sturgeon");

    DeferredItem<Item> TWILIGHT_KOI = registerBucketFish("twilight_koi");
    DeferredItem<Item> RIPPLE_CATFISH = registerNonBucketFish("ripple_catfish");
    DeferredItem<Item> SUN_SEEKING_CARP = registerBucketFish("sun_seeking_carp");

    DeferredItem<Item> THUNDER_BASS = registerBucketFish("thunder_bass");
    DeferredItem<Item> LIGHTNING_BASS = registerBucketFish("lightning_bass");

    //cold lake
    DeferredItem<Item> FROSTJAW_TROUT = registerNonBucketFish("frostjaw_trout");

    DeferredItem<Item> CRYSTALBACK_TROUT = registerBucketFish("crystalback_trout");

    DeferredItem<Item> WINTERY_PIKE = registerBucketFish("wintery_pike");

    DeferredItem<Item> BLUE_ICE_PIKE = registerNonBucketFish("blue_ice_pike");

    DeferredItem<Item> AURORA = registerNonBucketFish("aurora");

    //warm lake (desert/badlands etc)
    DeferredItem<Item> SANDTAIL = registerNonBucketFish("sandtail");
    DeferredItem<Item> SCORCHFISH = registerNonBucketFish("scorchfish");

    DeferredItem<Item> MIRAGE_CARP = registerBucketFish("mirage_carp");

    DeferredItem<Item> AGAVE_BREAM = registerBucketFish("agave_bream");

    DeferredItem<Item> CACTIFISH = registerBucketFish("cactifish");

    DeferredItem<Item> OASIS_STURGEON = registerNonBucketFish("oasis_sturgeon");

    //swamp
    DeferredItem<Item> MOSSY_BOOT = ITEMS.register("mossy_boot", MossyBootItem::new);

    DeferredItem<Item> SLUDGE_CATFISH = registerBucketFish("sludge_catfish");

    DeferredItem<Item> LILY_SNAPPER = registerBucketFish("lily_snapper");

    DeferredItem<Item> SAGE_CATFISH = registerNonBucketFish("sage_catfish");

    //darkoak_forest
    DeferredItem<Item> PALE_CARP = registerNonBucketFish("pale_carp");
    DeferredItem<Item> PALE_PINFISH = registerBucketFish("pale_pinfish");
    DeferredItem<Item> PINFISH = registerBucketFish("pinfish");


    //cherry grove
    DeferredItem<Item> BLOSSOMFISH = registerNonBucketFish("blossomfish");
    DeferredItem<Item> PETALDRIFT_CARP = registerBucketFish("petaldrift_carp");
    DeferredItem<Item> PINK_KOI = registerBucketFish("pink_koi");
    DeferredItem<Item> MORGANITE = registerBucketFish("morganite");
    DeferredItem<Item> ROSE_SIAMESE_FISH = registerNonBucketFish("rose_siamese_fish");
    DeferredItem<Item> VESANI = registerNonBucketFish("vesani");

    //flower forest
    DeferredItem<Item> PETAL_BASS = registerNonBucketFish("petal_bass");

    //sunflower field
    DeferredItem<Item> SUNFLOWER_CARP = registerNonBucketFish("sunflower_carp");

    //clouds
    DeferredItem<Item> CLOUDFIN = registerNonBucketFish("cloudfin");

    //icy mountain
    DeferredItem<Item> CRYSTALBACK_STURGEON = registerNonBucketFish("crystalback_sturgeon");
    DeferredItem<Item> ICETOOTH_STURGEON = registerBucketFish("icetooth_sturgeon");
    DeferredItem<Item> BOREAL = registerBucketFish("boreal");
    DeferredItem<Item> CRYSTALBACK_BOREAL = registerBucketFish("crystalback_boreal");

    //rivers
    DeferredItem<Item> SILVERFIN_PIKE = registerBucketFish("silverfin_pike");
    DeferredItem<Item> CARPENJOE = registerBucketFish("carpenjoe");
    DeferredItem<Item> WILLOW_BREAM = registerBucketFish("willow_bream");
    DeferredItem<Item> DRIFTING_BREAM = registerBucketFish("drifting_bream");
    DeferredItem<Item> DOWNFALL_BREAM = registerBucketFish("downfall_bream");
    DeferredItem<Item> HOLLOWBELLY_DARTER = registerBucketFish("hollowbelly_darter");
    DeferredItem<Item> MISTBACK_CHUB = registerBucketFish("mistback_chub");
    DeferredItem<Item> BLUEGIGI = registerNonBucketFish("bluegigi");
    DeferredItem<Item> DRIED_SEAWEED = ITEMS.register("dried_seaweed", BasicItem::new);

    //icy river
    DeferredItem<Item> FROSTGILL_CHUB = registerBucketFish("frostgill_chub");
    DeferredItem<Item> CRYSTALBACK_MINNOW = registerBucketFish("crystalback_minnow");
    DeferredItem<Item> AZURE_CRYSTALBACK_MINNOW = registerNonBucketFish("azure_crystalback_minnow");
    DeferredItem<Item> BLUE_CRYSTAL_FIN = registerBucketFish("blue_crystal_fin");

    //ocean
    DeferredItem<Item> BLUE_HERRING = registerBucketFish("blue_herring");
    DeferredItem<Item> IRONJAW_HERRING = registerBucketFish("ironjaw_herring");
    DeferredItem<Item> DEEPJAW_HERRING = registerBucketFish("deepjaw_herring");
    DeferredItem<Item> DUSKTAIL_SNAPPER = registerBucketFish("dusktail_snapper");
    DeferredItem<Item> JOEL = registerNonBucketFish("joel");
    DeferredItem<Item> REDSCALED_TUNA = registerBucketFish("redscaled_tuna");
    DeferredItem<Item> BIGEYE_TUNA = registerBucketFish("bigeye_tuna");
    DeferredItem<Item> SEA_BASS = registerNonBucketFish("sea_bass");
    //DeferredItem<Item> SHARK = registerBucketFish("shark");

    //mushroom islands
    DeferredItem<Item> SHROOMFISH = registerNonBucketFish("shroomfish");
    DeferredItem<Item> SPOREFISH = registerNonBucketFish("sporefish");

    //underground
    DeferredItem<Item> GOLD_FAN = registerNonBucketFish("gold_fan");
    DeferredItem<Item> GEODE_EEL = registerNonFishFish("geode_eel");

    //caves
    DeferredItem<Item> WHITEVEIL = registerBucketFish("whiteveil");
    DeferredItem<Item> BLACK_EEL = registerNonFishFish("black_eel");
    DeferredItem<Item> AMETHYSTBACK = registerNonBucketFish("amethystback");
    DeferredItem<Item> STONEFISH = registerNonBucketFish("stonefish");

    //dripstone caves
    DeferredItem<Item> FOSSILIZED_ANGELFISH = registerNonBucketFish("fossilized_angelfish");
    DeferredItem<Item> DRIPFIN = registerNonBucketFish("dripfin");
    DeferredItem<Item> YELLOWSTONE_FISH = registerBucketFish("yellowstone_fish");

    //lush caves
    DeferredItem<Item> LUSH_PIKE = registerBucketFish("lush_pike");
    DeferredItem<Item> VIVID_MOSS = registerBucketFish("vivid_moss");
    DeferredItem<Item> THE_QUARRISH = registerBucketFish("the_quarrish");

    //bamboo
    DeferredItem<Item> LIVID_BAMBOO = registerNonBucketFish("livid_bamboo");

    //deepslate
    DeferredItem<Item> GHOSTLY_PIKE = registerBucketFish("ghostly_pike");
    DeferredItem<Item> AQUAMARINE_PIKE = registerNonBucketFish("aquamarine_pike");
    DeferredItem<Item> GARNET_MACKEREL = registerNonBucketFish("garnet_mackerel");
    DeferredItem<Item> BRIGHT_AMETHYST_SNAPPER = registerNonBucketFish("bright_amethyst_snapper");
    DeferredItem<Item> DARK_AMETHYST_SNAPPER = registerNonBucketFish("dark_amethyst_snapper");
    DeferredItem<Item> DEEPSLATEFISH = registerNonBucketFish("deepslatefish");

    //deep dark
    DeferredItem<Item> SCULKFISH = registerBucketFish("sculkfish");
    DeferredItem<Item> WARD = registerNonBucketFish("ward");
    DeferredItem<Item> GLOWING_DARK = registerNonBucketFish("glowing_dark");
    //todo
    //DeferredItem<Item> XXXXX = registerNonBucketFish("xxxxx");

    //overworld surface lava
    DeferredItem<Item> SUNEATER = registerLavaBucketFish("suneater");
    DeferredItem<Item> PYROTROUT = registerLavaBucketFish("pyrotrout");
    DeferredItem<Item> OBSIDIAN_EEL = registerNonFishFish("obsidian_eel", true);

    //overworld underground lava
    DeferredItem<Item> MOLTEN_SHRIMP = registerNonFishFish("molten_shrimp", true);
    DeferredItem<Item> OBSIDIAN_CRAB = registerNonFishFish("obsidian_crab", true);

    //overworld deepslate lava
    DeferredItem<Item> SCORCHED_BLOODSUCKER = registerNonFishFish("scorched_bloodsucker", true);
    DeferredItem<Item> MOLTEN_DEEPSLATE_CRAB = registerNonFishFish("molten_deepslate_crab", true);

    //nether
    DeferredItem<Item> EMBERGILL = registerLavaBucketFish("embergill");
    DeferredItem<Item> SCALDING_PIKE = registerNonBucketFish("scalding_pike", true);
    DeferredItem<Item> CINDER_SQUID = registerNonBucketFish("cinder_squid", true);
    DeferredItem<Item> LAVA_CRAB = registerNonFishFish("lava_crab", true);
    DeferredItem<Item> MAGMA_FISH = registerLavaBucketFish("magma_fish");
    DeferredItem<Item> GLOWSTONE_SEEKER = registerNonBucketFish("glowstone_seeker", true);
    DeferredItem<Item> GLOWSTONE_PUFFERFISH = registerNonBucketFish("glowstone_pufferfish", true);
    DeferredItem<Item> WILLISH = registerLavaBucketFish("willish");

    DeferredItem<Item> CERBERAY = registerLavaBucketFish("cerberay");

    DeferredItem<Item> LAVA_CRAB_CLAW = ITEMS.register("lava_crab_claw", FireResistantBasicItem::new);

    //the end
    DeferredItem<Item> CHARFISH = registerBucketFish("charfish");
    DeferredItem<Item> CHORUS_CRAB = registerNonFishFish("chorus_crab");
    DeferredItem<Item> END_GLOW = registerNonBucketFish("end_glow");

    //end void
    DeferredItem<Item> VOIDBITER = registerBucketFish("voidbiter");
    DeferredItem<Item> PURPLE_CARP = registerNonBucketFish("purple_carp");
    DeferredItem<Item> VOIDFIN = registerNonBucketFish("voidfin");
    DeferredItem<Item> SPACEJELLY = registerNonBucketFish("spacejelly");
    DeferredItem<Item> CHORUS_MINNOW = registerNonBucketFish("chorus_minnow");
    DeferredItem<Item> NEBULA_SQUID = registerNonBucketFish("nebula_squid");

    //bucket
    DeferredItem<Item> STARCAUGHT_BUCKET = ITEMS.register("starcaught_bucket", () -> new StarcaughtBucket(Fluids.WATER));
    DeferredItem<Item> STARCAUGHT_LAVA_BUCKET = ITEMS.register("starcaught_lava_bucket", () -> new StarcaughtBucket(Fluids.LAVA));

    DeferredItem<Item> STARCAUGHT_FISH = ITEMS.register("starcaught_fish", () -> new Item(new Item.Properties().food(SCFoodProperties.BASIC_RAW_FISH)));
    DeferredItem<Item> COOKED_STARCAUGHT_FISH = ITEMS.register("cooked_starcaught_fish", () -> new Item(new Item.Properties().food(SCFoodProperties.BASIC_COOKED_FISH)));

    static DeferredItem<Item> registerNonFishFish(String name)
    {
        return registerNonFishFish(name, false);
    }

    static DeferredItem<Item> registerNonFishFish(String name, boolean fireResistant)
    {
        if (fireResistant)
            return NON_FISH_FISH_REGISTRY.register(name, FireResistantBasicItem::new);
        else
            return NON_FISH_FISH_REGISTRY.register(name, BasicItem::new);
    }

    static DeferredItem<Item> registerBucketFish(String name)
    {
        return BUCKETABLE_FISHES_REGISTRY.register(name, () -> new FishItem(new Item.Properties().food(SCFoodProperties.BASIC_RAW_FISH)));
    }

    static DeferredItem<Item> registerLavaBucketFish(String name)
    {
        return BUCKETABLE_FISHES_REGISTRY.register(name, () -> new FishItem(new Item.Properties().food(SCFoodProperties.BASIC_RAW_FISH).fireResistant()));
    }

    static DeferredItem<Item> registerNonBucketFish(String name)
    {
        return registerNonBucketFish(name, false);
    }

    static DeferredItem<Item> registerNonBucketFish(String name, boolean fireResistant)
    {
        if (fireResistant)
            return NON_BUCKETABLE_FISH_REGISTRY.register(name, () -> new FishItem(new Item.Properties().food(SCFoodProperties.BASIC_RAW_FISH).fireResistant()));
        else
            return NON_BUCKETABLE_FISH_REGISTRY.register(name, () -> new FishItem(new Item.Properties().food(SCFoodProperties.BASIC_RAW_FISH)));
    }

    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
        NON_BUCKETABLE_FISH_REGISTRY.register(modEventBus);
        BUCKETABLE_FISHES_REGISTRY.register(modEventBus);
        NON_FISH_FISH_REGISTRY.register(modEventBus);
        TEMPLATES_REGISTRY.register(modEventBus);
        BOBBERS_REGISTRY.register(modEventBus);
        HOOKS_REGISTRY.register(modEventBus);
        RODS_REGISTRY.register(modEventBus);
    }
}
