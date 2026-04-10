package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideItem;
import com.wdiscute.starcatcher.registry.items.*;
import com.wdiscute.starcatcher.registry.items.helper.BasicItem;
import com.wdiscute.starcatcher.registry.items.helper.FireResistantBasicItem;
import com.wdiscute.starcatcher.registry.items.helper.SingleStackBasicItem;
import com.wdiscute.starcatcher.registry.items.TackleSkinItem;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.starcatcher.registry.items.rod.StarcatcherFishingRodItem;
import com.wdiscute.starcatcher.secretnotes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface SCItems
{

    static void registerExtra()
    {
        //this works!
        if (ModList.get().isLoaded("create"))
        {
            //DeferredItem<Item> FISH = ITEMS_REGISTRY.register("fish", FishItem::new);
        }
    }

    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items BUCKETABLE_FISHES_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items TEMPLATES_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items RODS_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items HOOKS_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items BOBBERS_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);


    DeferredItem<Item> MISSINGNO = ITEMS.register("missingno", BasicItem::new);
    DeferredItem<Item> UNKNOWN_FISH = ITEMS.register("unknown_fish", BasicItem::new);

    DeferredItem<Item> GUIDE = ITEMS.register("starcatcher_guide", FishingGuideItem::new);

    DeferredItem<Item> FISH_RADAR = ITEMS.register("fish_radar", SingleStackBasicItem::new);
    DeferredItem<Item> STARCATCHER_TWINE = ITEMS.register("starcatcher_twine", SingleStackBasicItem::new);

    DeferredItem<Item> SETTINGS = ITEMS.register("settings", BasicItem::new);

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

    //bobbers
    DeferredItem<Item> BOBBER = BOBBERS_REGISTRY.register("bobber", SingleStackBasicItem::new);
    DeferredItem<Item> STEADY_BOBBER = BOBBERS_REGISTRY.register("steady_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> CLEAR_BOBBER = BOBBERS_REGISTRY.register("clear_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> AQUA_BOBBER = BOBBERS_REGISTRY.register("aqua_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> VANILLA_BOBBER = BOBBERS_REGISTRY.register("vanilla_bobber", SingleStackBasicItem::new);
    DeferredItem<Item> LEAF_BOBBER = BOBBERS_REGISTRY.register("leaf_bobber", SingleStackBasicItem::new);

    //baits
    DeferredItem<Item> WORM = ITEMS.register("worm", BasicItem::new);
    DeferredItem<Item> ALMIGHTY_WORM = ITEMS.register("almighty_worm", BasicItem::new);
    DeferredItem<Item> SEEKING_WORM = ITEMS.register("seeking_worm", BasicItem::new);
    DeferredItem<Item> DEV_WORM = ITEMS.register("dev_worm", BasicItem::new);

    DeferredItem<Item> GUNPOWDER_BAIT = ITEMS.register("gunpowder_bait", BasicItem::new);
    DeferredItem<Item> CHERRY_BAIT = ITEMS.register("cherry_bait", BasicItem::new);
    DeferredItem<Item> LUSH_BAIT = ITEMS.register("lush_bait", BasicItem::new);
    DeferredItem<Item> SCULK_BAIT = ITEMS.register("sculk_bait", BasicItem::new);
    DeferredItem<Item> DRIPSTONE_BAIT = ITEMS.register("dripstone_bait", BasicItem::new);
    DeferredItem<Item> MURKWATER_BAIT = ITEMS.register("murkwater_bait", BasicItem::new);
    DeferredItem<Item> LEGENDARY_BAIT = ITEMS.register("legendary_bait", BasicItem::new);
    DeferredItem<Item> METEOROLOGICAL_BAIT = ITEMS.register("meteorological_bait", BasicItem::new);


    //tackle templates
    DeferredItem<Item> PEARL_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("pearl_smithing_template", () -> new TackleSkinItem(SCTackleSkins.PEARL_TACKLE_SKIN));
    DeferredItem<Item> KIMBE_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("kimbe_smithing_template", () -> new TackleSkinItem(SCTackleSkins.KIMBE_TACKLE_SKIN));
    DeferredItem<Item> COLORFUL_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("colorful_smithing_template", () -> new TackleSkinItem(SCTackleSkins.COLORFUL_TACKLE_SKIN));
    DeferredItem<Item> CLEAR_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("clear_smithing_template", () -> new TackleSkinItem(SCTackleSkins.CLEAR_TACKLE_SKIN));
    DeferredItem<Item> FROG_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("frog_smithing_template", () -> new TackleSkinItem(SCTackleSkins.FROG_TACKLE_SKIN));
    DeferredItem<Item> KING_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("king_smithing_template", () -> new TackleSkinItem(SCTackleSkins.KING_TACKLE_SKIN));

    //skin templates
    DeferredItem<Item> NATURALIST_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("naturalist_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> ICEBORN_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("iceborn_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> MAGMAFORGED_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("magmaforged_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> SLIMED_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("slimed_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> SHARKTOOTH_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("sharktooth_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("azure_crystal_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> BAMBOO_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("bamboo_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> OBSIDIAN_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("obsidian_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> ALPHA_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("alpha_skin_smithing_template", BasicItem::new);
    DeferredItem<Item> GOOD_OLD_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.register("good_old_skin_smithing_template", BasicItem::new);
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
    DeferredItem<Item> GOOD_OLD_ROD = RODS_REGISTRY.register("good_old_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> BAMBOO_ROD = RODS_REGISTRY.register("bamboo_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> OBSIDIAN_ROD = RODS_REGISTRY.register("obsidian_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> ALPHA_ROD = RODS_REGISTRY.register("alpha_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> BONER_ROD = RODS_REGISTRY.register("boner_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> SKY_ROD = RODS_REGISTRY.register("sky_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> LUSH_GLOWBERRY_ROD = RODS_REGISTRY.register("lush_glowberry_rod", StarcatcherFishingRodItem::new);
    DeferredItem<Item> HUMBLE_ROD = RODS_REGISTRY.register("humble_rod", StarcatcherFishingRodItem::new);

    //secrets
    DeferredItem<Item> LETTER = ITEMS.register("letter", LetterItem::new);
    DeferredItem<Item> BOTTLED_LETTER = ITEMS.register("bottled_letter", BottledLetterItem::new);

    DeferredItem<Item> MESSAGE_IN_A_BOTTLE = ITEMS.register("message_in_a_bottle", MessageInABottleItem::new);
    DeferredItem<Item> MESSAGE = ITEMS.register("message", LetterItem::new);

    DeferredItem<Item> BROKEN_BOTTLE = ITEMS.register("broken_bottle", BrokenBottleItem::new);

    //built-in secret notes
    DeferredItem<Item> SECRET_NOTE = ITEMS.register("secret_note", SecretNote::new);

    DeferredItem<Item> DRIFTING_WATERLOGGED_BOTTLE = ITEMS.register("drifting_waterlogged_bottle", () -> new NoteContainer(SecretNote.Note.AMETHYST_HOOK));
    DeferredItem<Item> SCALDING_BOTTLE = ITEMS.register("scalding_bottle", () -> new NoteContainer(new Item.Properties().stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_1));
    DeferredItem<Item> BURNING_BOTTLE = ITEMS.register("burning_bottle", () -> new NoteContainer(new Item.Properties().stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_2));
    DeferredItem<Item> HOPEFUL_BOTTLE = ITEMS.register("hopeful_bottle", () -> new NoteContainer(SecretNote.Note.HOPEFUL_NOTE));
    DeferredItem<Item> HOPELESS_BOTTLE = ITEMS.register("hopeless_bottle", () -> new NoteContainer(SecretNote.Note.HOPELESS_NOTE));
    DeferredItem<Item> TRUE_BLUE_BOTTLE = ITEMS.register("true_blue_bottle", () -> new NoteContainer(SecretNote.Note.TRUE_BLUE));
    DeferredItem<Item> WITHERED_BOTTLE = ITEMS.register("withered_bottle", () -> new NoteContainer(SecretNote.Note.WITHER));

    //treasure
    DeferredItem<Item> WATERLOGGED_SATCHEL = ITEMS.register("waterlogged_satchel", WaterloggedSatchel::new);

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
    DeferredItem<Item> OBIDONTIEE = BUCKETABLE_FISHES_REGISTRY.register("obidontiee", FishItem::new);
    DeferredItem<Item> SILVERVEIL_PERCH = BUCKETABLE_FISHES_REGISTRY.register("silverveil_perch", FishItem::new);
    DeferredItem<Item> ELDERSCALE = BUCKETABLE_FISHES_REGISTRY.register("elderscale", FishItem::new);
    DeferredItem<Item> DRIFTFIN = BUCKETABLE_FISHES_REGISTRY.register("driftfin", FishItem::new);
    DeferredItem<Item> TWILIGHT_KOI = BUCKETABLE_FISHES_REGISTRY.register("twilight_koi", FishItem::new);
    DeferredItem<Item> THUNDER_BASS = BUCKETABLE_FISHES_REGISTRY.register("thunder_bass", FishItem::new);
    DeferredItem<Item> LIGHTNING_BASS = BUCKETABLE_FISHES_REGISTRY.register("lightning_bass", FishItem::new);
    DeferredItem<Item> BOOT = ITEMS.register("boot", BasicItem::new);

    //swamp
    DeferredItem<Item> SLUDGE_CATFISH = BUCKETABLE_FISHES_REGISTRY.register("sludge_catfish", FishItem::new);
    DeferredItem<Item> LILY_SNAPPER = BUCKETABLE_FISHES_REGISTRY.register("lily_snapper", FishItem::new);
    DeferredItem<Item> SAGE_CATFISH = BUCKETABLE_FISHES_REGISTRY.register("sage_catfish", FishItem::new);
    DeferredItem<Item> MOSSY_BOOT = ITEMS.register("mossy_boot", BasicItem::new);

    //darkoak_forest
    DeferredItem<Item> PALE_CARP = BUCKETABLE_FISHES_REGISTRY.register("pale_carp", FishItem::new);
    DeferredItem<Item> PALE_PINFISH = BUCKETABLE_FISHES_REGISTRY.register("pale_pinfish", FishItem::new);
    DeferredItem<Item> PINFISH = BUCKETABLE_FISHES_REGISTRY.register("pinfish", FishItem::new);

    //icy lake
    DeferredItem<Item> FROSTJAW_TROUT = BUCKETABLE_FISHES_REGISTRY.register("frostjaw_trout", FishItem::new);
    DeferredItem<Item> CRYSTALBACK_TROUT = BUCKETABLE_FISHES_REGISTRY.register("crystalback_trout", FishItem::new);
    DeferredItem<Item> AURORA = BUCKETABLE_FISHES_REGISTRY.register("aurora", FishItem::new);
    DeferredItem<Item> WINTERY_PIKE = BUCKETABLE_FISHES_REGISTRY.register("wintery_pike", FishItem::new);

    //warm lake (desert/savanna etc)
    DeferredItem<Item> SANDTAIL = BUCKETABLE_FISHES_REGISTRY.register("sandtail", FishItem::new);
    DeferredItem<Item> MIRAGE_CARP = BUCKETABLE_FISHES_REGISTRY.register("mirage_carp", FishItem::new);
    DeferredItem<Item> SCORCHFISH = BUCKETABLE_FISHES_REGISTRY.register("scorchfish", FishItem::new);
    DeferredItem<Item> CACTIFISH = BUCKETABLE_FISHES_REGISTRY.register("cactifish", FishItem::new);
    DeferredItem<Item> AGAVE_BREAM = BUCKETABLE_FISHES_REGISTRY.register("agave_bream", FishItem::new);

    //mountain
    DeferredItem<Item> SUNNY_STURGEON = BUCKETABLE_FISHES_REGISTRY.register("sunny_sturgeon", FishItem::new);
    DeferredItem<Item> ROCKGILL = BUCKETABLE_FISHES_REGISTRY.register("rockgill", FishItem::new);
    DeferredItem<Item> PEAKDWELLER = BUCKETABLE_FISHES_REGISTRY.register("peakdweller", FishItem::new);
    DeferredItem<Item> SUN_SEEKING_CARP = BUCKETABLE_FISHES_REGISTRY.register("sun_seeking_carp", FishItem::new);

    //cherry grove
    DeferredItem<Item> BLOSSOMFISH = BUCKETABLE_FISHES_REGISTRY.register("blossomfish", FishItem::new);
    DeferredItem<Item> PETALDRIFT_CARP = BUCKETABLE_FISHES_REGISTRY.register("petaldrift_carp", FishItem::new);
    DeferredItem<Item> PINK_KOI = BUCKETABLE_FISHES_REGISTRY.register("pink_koi", FishItem::new);
    DeferredItem<Item> MORGANITE = BUCKETABLE_FISHES_REGISTRY.register("morganite", FishItem::new);
    DeferredItem<Item> ROSE_SIAMESE_FISH = BUCKETABLE_FISHES_REGISTRY.register("rose_siamese_fish", FishItem::new);
    DeferredItem<Item> VESANI = BUCKETABLE_FISHES_REGISTRY.register("vesani", FishItem::new);

    //icy mountain
    DeferredItem<Item> CRYSTALBACK_STURGEON = BUCKETABLE_FISHES_REGISTRY.register("crystalback_sturgeon", FishItem::new);
    DeferredItem<Item> ICETOOTH_STURGEON = BUCKETABLE_FISHES_REGISTRY.register("icetooth_sturgeon", FishItem::new);
    DeferredItem<Item> BOREAL = BUCKETABLE_FISHES_REGISTRY.register("boreal", FishItem::new);
    DeferredItem<Item> CRYSTALBACK_BOREAL = BUCKETABLE_FISHES_REGISTRY.register("crystalback_boreal", FishItem::new);

    //rivers
    DeferredItem<Item> SILVERFIN_PIKE = BUCKETABLE_FISHES_REGISTRY.register("silverfin_pike", FishItem::new);
    DeferredItem<Item> CARPENJOE = BUCKETABLE_FISHES_REGISTRY.register("carpenjoe", FishItem::new);
    DeferredItem<Item> WILLOW_BREAM = BUCKETABLE_FISHES_REGISTRY.register("willow_bream", FishItem::new);
    DeferredItem<Item> DRIFTING_BREAM = BUCKETABLE_FISHES_REGISTRY.register("drifting_bream", FishItem::new);
    DeferredItem<Item> DOWNFALL_BREAM = BUCKETABLE_FISHES_REGISTRY.register("downfall_bream", FishItem::new);
    DeferredItem<Item> HOLLOWBELLY_DARTER = BUCKETABLE_FISHES_REGISTRY.register("hollowbelly_darter", FishItem::new);
    DeferredItem<Item> MISTBACK_CHUB = BUCKETABLE_FISHES_REGISTRY.register("mistback_chub", FishItem::new);
    DeferredItem<Item> BLUEGIGI = BUCKETABLE_FISHES_REGISTRY.register("bluegigi", FishItem::new);
    DeferredItem<Item> DRIED_SEAWEED = ITEMS.register("dried_seaweed", BasicItem::new);

    //icy river
    DeferredItem<Item> FROSTGILL_CHUB = BUCKETABLE_FISHES_REGISTRY.register("frostgill_chub", FishItem::new);
    DeferredItem<Item> CRYSTALBACK_MINNOW = BUCKETABLE_FISHES_REGISTRY.register("crystalback_minnow", FishItem::new);
    DeferredItem<Item> AZURE_CRYSTALBACK_MINNOW = BUCKETABLE_FISHES_REGISTRY.register("azure_crystalback_minnow", FishItem::new);
    DeferredItem<Item> BLUE_CRYSTAL_FIN = BUCKETABLE_FISHES_REGISTRY.register("blue_crystal_fin", FishItem::new);

    //saltwater
    DeferredItem<Item> BLUE_HERRING = BUCKETABLE_FISHES_REGISTRY.register("blue_herring", FishItem::new);
    DeferredItem<Item> IRONJAW_HERRING = BUCKETABLE_FISHES_REGISTRY.register("ironjaw_herring", FishItem::new);
    DeferredItem<Item> DEEPJAW_HERRING = BUCKETABLE_FISHES_REGISTRY.register("deepjaw_herring", FishItem::new);
    DeferredItem<Item> DUSKTAIL_SNAPPER = BUCKETABLE_FISHES_REGISTRY.register("dusktail_snapper", FishItem::new);
    DeferredItem<Item> JOEL = BUCKETABLE_FISHES_REGISTRY.register("joel", FishItem::new);
    DeferredItem<Item> REDSCALED_TUNA = BUCKETABLE_FISHES_REGISTRY.register("redscaled_tuna", FishItem::new);
    DeferredItem<Item> BIGEYE_TUNA = BUCKETABLE_FISHES_REGISTRY.register("bigeye_tuna", FishItem::new);
    DeferredItem<Item> SEA_BASS = BUCKETABLE_FISHES_REGISTRY.register("sea_bass", FishItem::new);

    //mushroom islands
    DeferredItem<Item> SHROOMFISH = BUCKETABLE_FISHES_REGISTRY.register("shroomfish", FishItem::new);
    DeferredItem<Item> SPOREFISH = BUCKETABLE_FISHES_REGISTRY.register("sporefish", FishItem::new);

    //underground
    DeferredItem<Item> GOLD_FAN = BUCKETABLE_FISHES_REGISTRY.register("gold_fan", FishItem::new);
    DeferredItem<Item> GEODE_EEL = ITEMS.register("geode_eel", BasicItem::new);

    //caves
    DeferredItem<Item> WHITEVEIL = BUCKETABLE_FISHES_REGISTRY.register("whiteveil", FishItem::new);
    DeferredItem<Item> BLACK_EEL = ITEMS.register("black_eel", BasicItem::new);
    DeferredItem<Item> AMETHYSTBACK = BUCKETABLE_FISHES_REGISTRY.register("amethystback", FishItem::new);
    DeferredItem<Item> STONEFISH = BUCKETABLE_FISHES_REGISTRY.register("stonefish", FishItem::new);

    //dripstone caves
    DeferredItem<Item> FOSSILIZED_ANGELFISH = BUCKETABLE_FISHES_REGISTRY.register("fossilized_angelfish", FishItem::new);
    DeferredItem<Item> DRIPFIN = BUCKETABLE_FISHES_REGISTRY.register("dripfin", FishItem::new);
    DeferredItem<Item> YELLOWSTONE_FISH = BUCKETABLE_FISHES_REGISTRY.register("yellowstone_fish", FishItem::new);

    //lush caves
    DeferredItem<Item> LUSH_PIKE = BUCKETABLE_FISHES_REGISTRY.register("lush_pike", FishItem::new);
    DeferredItem<Item> VIVID_MOSS = BUCKETABLE_FISHES_REGISTRY.register("vivid_moss", FishItem::new);
    DeferredItem<Item> THE_QUARRISH = BUCKETABLE_FISHES_REGISTRY.register("the_quarrish", FishItem::new);

    //deepslate
    DeferredItem<Item> GHOSTLY_PIKE = BUCKETABLE_FISHES_REGISTRY.register("ghostly_pike", FishItem::new);
    DeferredItem<Item> AQUAMARINE_PIKE = BUCKETABLE_FISHES_REGISTRY.register("aquamarine_pike", FishItem::new);
    DeferredItem<Item> GARNET_MACKEREL = BUCKETABLE_FISHES_REGISTRY.register("garnet_mackerel", FishItem::new);
    DeferredItem<Item> BRIGHT_AMETHYST_SNAPPER = BUCKETABLE_FISHES_REGISTRY.register("bright_amethyst_snapper", FishItem::new);
    DeferredItem<Item> DARK_AMETHYST_SNAPPER = BUCKETABLE_FISHES_REGISTRY.register("dark_amethyst_snapper", FishItem::new);
    DeferredItem<Item> DEEPSLATEFISH = BUCKETABLE_FISHES_REGISTRY.register("deepslatefish", FishItem::new);

    //deep dark
    DeferredItem<Item> SCULKFISH = BUCKETABLE_FISHES_REGISTRY.register("sculkfish", FishItem::new);
    DeferredItem<Item> WARD = BUCKETABLE_FISHES_REGISTRY.register("ward", FishItem::new);
    DeferredItem<Item> GLOWING_DARK = BUCKETABLE_FISHES_REGISTRY.register("glowing_dark", FishItem::new);

    //overworld surface lava
    DeferredItem<Item> SUNEATER = BUCKETABLE_FISHES_REGISTRY.register("suneater", FireResistantBasicItem::new);
    DeferredItem<Item> PYROTROUT = BUCKETABLE_FISHES_REGISTRY.register("pyrotrout", FireResistantBasicItem::new);
    DeferredItem<Item> OBSIDIAN_EEL = ITEMS.register("obsidian_eel", FireResistantBasicItem::new);

    //overworld underground lava
    DeferredItem<Item> MOLTEN_SHRIMP = ITEMS.register("molten_shrimp", FireResistantBasicItem::new);
    DeferredItem<Item> OBSIDIAN_CRAB = ITEMS.register("obsidian_crab", FireResistantBasicItem::new);

    //overworld deepslate lava
    DeferredItem<Item> SCORCHED_BLOODSUCKER = ITEMS.register("scorched_bloodsucker", FireResistantBasicItem::new);
    DeferredItem<Item> MOLTEN_DEEPSLATE_CRAB = ITEMS.register("molten_deepslate_crab", FireResistantBasicItem::new);

    //nether
    DeferredItem<Item> EMBERGILL = BUCKETABLE_FISHES_REGISTRY.register("embergill", FireResistantBasicItem::new);
    DeferredItem<Item> SCALDING_PIKE = BUCKETABLE_FISHES_REGISTRY.register("scalding_pike", FireResistantBasicItem::new);
    DeferredItem<Item> CINDER_SQUID = ITEMS.register("cinder_squid", FireResistantBasicItem::new);
    DeferredItem<Item> LAVA_CRAB = ITEMS.register("lava_crab", FireResistantBasicItem::new);
    DeferredItem<Item> MAGMA_FISH = BUCKETABLE_FISHES_REGISTRY.register("magma_fish", FireResistantBasicItem::new);
    DeferredItem<Item> GLOWSTONE_SEEKER = BUCKETABLE_FISHES_REGISTRY.register("glowstone_seeker", FireResistantBasicItem::new);
    DeferredItem<Item> GLOWSTONE_PUFFERFISH = BUCKETABLE_FISHES_REGISTRY.register("glowstone_pufferfish", FireResistantBasicItem::new);
    DeferredItem<Item> WILLISH = BUCKETABLE_FISHES_REGISTRY.register("willish", FireResistantBasicItem::new);

    DeferredItem<Item> CERBERAY = BUCKETABLE_FISHES_REGISTRY.register("cerberay", FireResistantBasicItem::new);

    DeferredItem<Item> LAVA_CRAB_CLAW = ITEMS.register("lava_crab_claw", FireResistantBasicItem::new);

    //the end
    DeferredItem<Item> CHARFISH = BUCKETABLE_FISHES_REGISTRY.register("charfish", FishItem::new);
    DeferredItem<Item> CHORUS_CRAB = ITEMS.register("chorus_crab", FishItem::new);
    DeferredItem<Item> END_GLOW = BUCKETABLE_FISHES_REGISTRY.register("end_glow", FishItem::new);
    DeferredItem<Item> VOIDBITER = BUCKETABLE_FISHES_REGISTRY.register("voidbiter", FishItem::new);

    //bucket
    DeferredItem<Item> STARCAUGHT_BUCKET = ITEMS.register("starcaught_bucket", () -> new StarcaughtBucket(Fluids.WATER));

    DeferredItem<Item> COOKED_STARCAUGHT_FISH = ITEMS.register("cooked_starcaught_fish", () -> new Item(new Item.Properties().food(SCFoodProperties.BASIC_COOKED_FISH)));

    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
        BUCKETABLE_FISHES_REGISTRY.register(modEventBus);
        TEMPLATES_REGISTRY.register(modEventBus);
        BOBBERS_REGISTRY.register(modEventBus);
        HOOKS_REGISTRY.register(modEventBus);
        RODS_REGISTRY.register(modEventBus);
    }
}
