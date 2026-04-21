package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideItem;
import com.wdiscute.starcatcher.registry.items.*;
import com.wdiscute.starcatcher.registry.items.helper.FireResistantBasicItem;
import com.wdiscute.starcatcher.registry.items.helper.SingleStackBasicItem;
import com.wdiscute.starcatcher.registry.items.rod.StarcatcherFishingRodItem;
import com.wdiscute.starcatcher.secretnotes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

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


    DeferredItem<net.minecraft.world.item.Item> MISSINGNO = ITEMS.registerItem("missingno", Item::new);
    DeferredItem<net.minecraft.world.item.Item> UNKNOWN_FISH = ITEMS.registerItem("unknown_fish", Item::new);

    DeferredItem<net.minecraft.world.item.Item> GUIDE = ITEMS.registerItem("starcatcher_guide", FishingGuideItem::new);

    DeferredItem<net.minecraft.world.item.Item> FISH_RADAR = ITEMS.registerItem("fish_radar", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> STARCATCHER_TWINE = ITEMS.registerItem("starcatcher_twine", SingleStackBasicItem::new);

    DeferredItem<net.minecraft.world.item.Item> SETTINGS = ITEMS.registerItem("settings", Item::new);

    //hooks
    DeferredItem<net.minecraft.world.item.Item> HOOK = HOOKS_REGISTRY.registerItem("hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> AMETHYST_HOOK = HOOKS_REGISTRY.registerItem("amethyst_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> SHINY_HOOK = HOOKS_REGISTRY.registerItem("shiny_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> GOLD_HOOK = HOOKS_REGISTRY.registerItem("gold_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> MOSSY_HOOK = HOOKS_REGISTRY.registerItem("mossy_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> STONE_HOOK = HOOKS_REGISTRY.registerItem("stone_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> SPLIT_HOOK = HOOKS_REGISTRY.registerItem("split_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> HEAVY_HOOK = HOOKS_REGISTRY.registerItem("heavy_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> VANILLA_HOOK = HOOKS_REGISTRY.registerItem("vanilla_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> COPPER_HOOK = HOOKS_REGISTRY.registerItem("copper_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> EXPOSED_COPPER_HOOK = HOOKS_REGISTRY.registerItem("exposed_copper_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> WEATHERED_COPPER_HOOK = HOOKS_REGISTRY.registerItem("weathered_copper_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> OXIDISED_COPPER_HOOK = HOOKS_REGISTRY.registerItem("oxidised_copper_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> ECHOING_HOOK = HOOKS_REGISTRY.registerItem("echoing_hook", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> FROZEN_HOOK = HOOKS_REGISTRY.registerItem("frozen_hook", SingleStackBasicItem::new);

    //bobbers
    DeferredItem<net.minecraft.world.item.Item> BOBBER = BOBBERS_REGISTRY.registerItem("bobber", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> STEADY_BOBBER = BOBBERS_REGISTRY.registerItem("steady_bobber", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> CLEAR_BOBBER = BOBBERS_REGISTRY.registerItem("clear_bobber", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> AQUA_BOBBER = BOBBERS_REGISTRY.registerItem("aqua_bobber", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> VANILLA_BOBBER = BOBBERS_REGISTRY.registerItem("vanilla_bobber", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> LEAF_BOBBER = BOBBERS_REGISTRY.registerItem("leaf_bobber", SingleStackBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> SLIMEY_BOBBER = BOBBERS_REGISTRY.registerItem("slimey_bobber", SingleStackBasicItem::new);

    //baits
    DeferredItem<net.minecraft.world.item.Item> WORM = ITEMS.registerItem("worm", Item::new);
    DeferredItem<net.minecraft.world.item.Item> ALMIGHTY_WORM = ITEMS.registerItem("almighty_worm", Item::new);
    DeferredItem<net.minecraft.world.item.Item> SEEKING_WORM = ITEMS.registerItem("seeking_worm", Item::new);
    DeferredItem<net.minecraft.world.item.Item> DEV_WORM = ITEMS.registerItem("dev_worm", Item::new);

    DeferredItem<net.minecraft.world.item.Item> GUNPOWDER_BAIT = ITEMS.registerItem("gunpowder_bait", Item::new);
    DeferredItem<net.minecraft.world.item.Item> CHERRY_BAIT = ITEMS.registerItem("cherry_bait", Item::new);
    DeferredItem<net.minecraft.world.item.Item> LUSH_BAIT = ITEMS.registerItem("lush_bait", Item::new);
    DeferredItem<net.minecraft.world.item.Item> SCULK_BAIT = ITEMS.registerItem("sculk_bait", Item::new);
    DeferredItem<net.minecraft.world.item.Item> DRIPSTONE_BAIT = ITEMS.registerItem("dripstone_bait", Item::new);
    DeferredItem<net.minecraft.world.item.Item> MURKWATER_BAIT = ITEMS.registerItem("murkwater_bait", Item::new);
    DeferredItem<net.minecraft.world.item.Item> LEGENDARY_BAIT = ITEMS.registerItem("legendary_bait", Item::new);
    DeferredItem<net.minecraft.world.item.Item> METEOROLOGICAL_BAIT = ITEMS.registerItem("meteorological_bait", Item::new);


    //tackle templates
    DeferredItem<net.minecraft.world.item.Item> PEARL_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("pearl_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> KIMBE_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("kimbe_smithing_template",  Item::new);
    DeferredItem<net.minecraft.world.item.Item> COLORFUL_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("colorful_smithing_template",  Item::new);
    DeferredItem<net.minecraft.world.item.Item> CLEAR_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("clear_smithing_template",  Item::new);
    DeferredItem<net.minecraft.world.item.Item> FROG_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("frog_smithing_template",  Item::new);
    DeferredItem<net.minecraft.world.item.Item> KING_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("king_smithing_template",  Item::new);

    //skin templates
    DeferredItem<net.minecraft.world.item.Item> NATURALIST_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("naturalist_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> ICEBORN_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("iceborn_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> MAGMAFORGED_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("magmaforged_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> SLIMED_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("slimed_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> SHARKTOOTH_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("sharktooth_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("azure_crystal_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> BAMBOO_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("bamboo_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> OBSIDIAN_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("obsidian_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> ALPHA_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("alpha_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> GOOD_OLD_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("good_old_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> BONER_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("boner_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> SKY_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("sky_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("lush_glowberry_skin_smithing_template", Item::new);
    DeferredItem<net.minecraft.world.item.Item> HUMBLE_SKIN_SMITHING_TEMPLATE = TEMPLATES_REGISTRY.registerItem("humble_skin_smithing_template", Item::new);


    //rods
    DeferredItem<net.minecraft.world.item.Item> ROD = RODS_REGISTRY.registerItem("starcatcher_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> NATURALIST_ROD = RODS_REGISTRY.registerItem("naturalist_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> ICEBORN_ROD = RODS_REGISTRY.registerItem("iceborn_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> MAGMAFORGED_ROD = RODS_REGISTRY.registerItem("magmaforged_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> SLIMED_ROD = RODS_REGISTRY.registerItem("slimed_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> SHARKTOOTH_ROD = RODS_REGISTRY.registerItem("sharktooth_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> AZURE_CRYSTAL_ROD = RODS_REGISTRY.registerItem("azure_crystal_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> GOOD_OLD_ROD = RODS_REGISTRY.registerItem("good_old_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> BAMBOO_ROD = RODS_REGISTRY.registerItem("bamboo_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> OBSIDIAN_ROD = RODS_REGISTRY.registerItem("obsidian_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> ALPHA_ROD = RODS_REGISTRY.registerItem("alpha_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> BONER_ROD = RODS_REGISTRY.registerItem("boner_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> SKY_ROD = RODS_REGISTRY.registerItem("sky_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> LUSH_GLOWBERRY_ROD = RODS_REGISTRY.registerItem("lush_glowberry_rod", StarcatcherFishingRodItem::new);
    DeferredItem<net.minecraft.world.item.Item> HUMBLE_ROD = RODS_REGISTRY.registerItem("humble_rod", StarcatcherFishingRodItem::new);

    //secrets
    DeferredItem<net.minecraft.world.item.Item> LETTER = ITEMS.registerItem("letter", LetterItem::new);
    DeferredItem<net.minecraft.world.item.Item> BOTTLED_LETTER = ITEMS.registerItem("bottled_letter", BottledLetterItem::new);

    DeferredItem<net.minecraft.world.item.Item> MESSAGE_IN_A_BOTTLE = ITEMS.registerItem("message_in_a_bottle", MessageInABottleItem::new);
    DeferredItem<net.minecraft.world.item.Item> MESSAGE = ITEMS.registerItem("message", LetterItem::new);

    DeferredItem<net.minecraft.world.item.Item> BROKEN_BOTTLE = ITEMS.registerItem("broken_bottle", BrokenBottleItem::new);

    //built-in secret notes
    DeferredItem<net.minecraft.world.item.Item> SECRET_NOTE = ITEMS.registerItem("secret_note", SecretNote::new);

    DeferredItem<net.minecraft.world.item.Item> DRIFTING_WATERLOGGED_BOTTLE = ITEMS.registerItem("drifting_waterlogged_bottle", (p) -> new NoteContainer(p, SecretNote.Note.AMETHYST_HOOK));
    DeferredItem<net.minecraft.world.item.Item> SCALDING_BOTTLE = ITEMS.registerItem("scalding_bottle", (p) -> new NoteContainer(p.stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_1));
    DeferredItem<net.minecraft.world.item.Item> BURNING_BOTTLE = ITEMS.registerItem("burning_bottle", (p) -> new NoteContainer(p.stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_2));
    DeferredItem<net.minecraft.world.item.Item> HOPEFUL_BOTTLE = ITEMS.registerItem("hopeful_bottle", (p) -> new NoteContainer(p, SecretNote.Note.HOPEFUL_NOTE));
    DeferredItem<net.minecraft.world.item.Item> HOPELESS_BOTTLE = ITEMS.registerItem("hopeless_bottle", (p) -> new NoteContainer(p, SecretNote.Note.HOPELESS_NOTE));
    DeferredItem<net.minecraft.world.item.Item> TRUE_BLUE_BOTTLE = ITEMS.registerItem("true_blue_bottle", (p) -> new NoteContainer(p, SecretNote.Note.TRUE_BLUE));
    DeferredItem<net.minecraft.world.item.Item> WITHERED_BOTTLE = ITEMS.registerItem("withered_bottle", (p) -> new NoteContainer(p, SecretNote.Note.WITHER));

    //treasure
    DeferredItem<net.minecraft.world.item.Item> WATERLOGGED_SATCHEL = ITEMS.registerItem("waterlogged_satchel", WaterloggedSatchel::new);

    DeferredItem<net.minecraft.world.item.Item> FISH_BONES = ITEMS.registerItem("fish_bones", Item::new);
    DeferredItem<net.minecraft.world.item.Item> PEARL = ITEMS.registerItem("pearl", Item::new);

    //
    //  ,---. ,--.         ,--.
    // /  .-' `--'  ,---.  |  ,---.   ,---.   ,---.
    // |  `-, ,--. (  .-'  |  .-.  | | .-. : (  .-'
    // |  .-' |  | .-'  `) |  | |  | \   --. .-'  `)
    // `--'   `--' `----'  `--' `--'  `----' `----'
    //

    //lake
    DeferredItem<net.minecraft.world.item.Item> OBIDONTIEE = BUCKETABLE_FISHES_REGISTRY.registerItem("obidontiee", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> SILVERVEIL_PERCH = BUCKETABLE_FISHES_REGISTRY.registerItem("silverveil_perch", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> ELDERSCALE = BUCKETABLE_FISHES_REGISTRY.registerItem("elderscale", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> DRIFTFIN = BUCKETABLE_FISHES_REGISTRY.registerItem("driftfin", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> TWILIGHT_KOI = BUCKETABLE_FISHES_REGISTRY.registerItem("twilight_koi", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> THUNDER_BASS = BUCKETABLE_FISHES_REGISTRY.registerItem("thunder_bass", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> LIGHTNING_BASS = BUCKETABLE_FISHES_REGISTRY.registerItem("lightning_bass", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> BOOT = ITEMS.registerItem("boot", Item::new);

    //swamp
    DeferredItem<net.minecraft.world.item.Item> SLUDGE_CATFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("sludge_catfish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> LILY_SNAPPER = BUCKETABLE_FISHES_REGISTRY.registerItem("lily_snapper", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> SAGE_CATFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("sage_catfish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> MOSSY_BOOT = ITEMS.registerItem("mossy_boot", Item::new);

    //darkoak_forest
    DeferredItem<net.minecraft.world.item.Item> PALE_CARP = BUCKETABLE_FISHES_REGISTRY.registerItem("pale_carp", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> PALE_PINFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("pale_pinfish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> PINFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("pinfish", FishItem::new);

    //icy lake
    DeferredItem<net.minecraft.world.item.Item> FROSTJAW_TROUT = BUCKETABLE_FISHES_REGISTRY.registerItem("frostjaw_trout", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> CRYSTALBACK_TROUT = BUCKETABLE_FISHES_REGISTRY.registerItem("crystalback_trout", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> AURORA = BUCKETABLE_FISHES_REGISTRY.registerItem("aurora", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> WINTERY_PIKE = BUCKETABLE_FISHES_REGISTRY.registerItem("wintery_pike", FishItem::new);

    //warm lake (desert/savanna etc)
    DeferredItem<net.minecraft.world.item.Item> SANDTAIL = BUCKETABLE_FISHES_REGISTRY.registerItem("sandtail", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> MIRAGE_CARP = BUCKETABLE_FISHES_REGISTRY.registerItem("mirage_carp", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> SCORCHFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("scorchfish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> CACTIFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("cactifish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> AGAVE_BREAM = BUCKETABLE_FISHES_REGISTRY.registerItem("agave_bream", FishItem::new);

    //mountain
    DeferredItem<net.minecraft.world.item.Item> SUNNY_STURGEON = BUCKETABLE_FISHES_REGISTRY.registerItem("sunny_sturgeon", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> ROCKGILL = BUCKETABLE_FISHES_REGISTRY.registerItem("rockgill", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> PEAKDWELLER = BUCKETABLE_FISHES_REGISTRY.registerItem("peakdweller", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> SUN_SEEKING_CARP = BUCKETABLE_FISHES_REGISTRY.registerItem("sun_seeking_carp", FishItem::new);

    //cherry grove
    DeferredItem<net.minecraft.world.item.Item> BLOSSOMFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("blossomfish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> PETALDRIFT_CARP = BUCKETABLE_FISHES_REGISTRY.registerItem("petaldrift_carp", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> PINK_KOI = BUCKETABLE_FISHES_REGISTRY.registerItem("pink_koi", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> MORGANITE = BUCKETABLE_FISHES_REGISTRY.registerItem("morganite", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> ROSE_SIAMESE_FISH = BUCKETABLE_FISHES_REGISTRY.registerItem("rose_siamese_fish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> VESANI = BUCKETABLE_FISHES_REGISTRY.registerItem("vesani", FishItem::new);

    //icy mountain
    DeferredItem<net.minecraft.world.item.Item> CRYSTALBACK_STURGEON = BUCKETABLE_FISHES_REGISTRY.registerItem("crystalback_sturgeon", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> ICETOOTH_STURGEON = BUCKETABLE_FISHES_REGISTRY.registerItem("icetooth_sturgeon", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> BOREAL = BUCKETABLE_FISHES_REGISTRY.registerItem("boreal", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> CRYSTALBACK_BOREAL = BUCKETABLE_FISHES_REGISTRY.registerItem("crystalback_boreal", FishItem::new);

    //rivers
    DeferredItem<net.minecraft.world.item.Item> SILVERFIN_PIKE = BUCKETABLE_FISHES_REGISTRY.registerItem("silverfin_pike", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> CARPENJOE = BUCKETABLE_FISHES_REGISTRY.registerItem("carpenjoe", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> WILLOW_BREAM = BUCKETABLE_FISHES_REGISTRY.registerItem("willow_bream", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> DRIFTING_BREAM = BUCKETABLE_FISHES_REGISTRY.registerItem("drifting_bream", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> DOWNFALL_BREAM = BUCKETABLE_FISHES_REGISTRY.registerItem("downfall_bream", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> HOLLOWBELLY_DARTER = BUCKETABLE_FISHES_REGISTRY.registerItem("hollowbelly_darter", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> MISTBACK_CHUB = BUCKETABLE_FISHES_REGISTRY.registerItem("mistback_chub", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> BLUEGIGI = BUCKETABLE_FISHES_REGISTRY.registerItem("bluegigi", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> DRIED_SEAWEED = ITEMS.registerItem("dried_seaweed", Item::new);

    //icy river
    DeferredItem<net.minecraft.world.item.Item> FROSTGILL_CHUB = BUCKETABLE_FISHES_REGISTRY.registerItem("frostgill_chub", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> CRYSTALBACK_MINNOW = BUCKETABLE_FISHES_REGISTRY.registerItem("crystalback_minnow", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> AZURE_CRYSTALBACK_MINNOW = BUCKETABLE_FISHES_REGISTRY.registerItem("azure_crystalback_minnow", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> BLUE_CRYSTAL_FIN = BUCKETABLE_FISHES_REGISTRY.registerItem("blue_crystal_fin", FishItem::new);

    //saltwater
    DeferredItem<net.minecraft.world.item.Item> BLUE_HERRING = BUCKETABLE_FISHES_REGISTRY.registerItem("blue_herring", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> IRONJAW_HERRING = BUCKETABLE_FISHES_REGISTRY.registerItem("ironjaw_herring", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> DEEPJAW_HERRING = BUCKETABLE_FISHES_REGISTRY.registerItem("deepjaw_herring", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> DUSKTAIL_SNAPPER = BUCKETABLE_FISHES_REGISTRY.registerItem("dusktail_snapper", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> JOEL = BUCKETABLE_FISHES_REGISTRY.registerItem("joel", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> REDSCALED_TUNA = BUCKETABLE_FISHES_REGISTRY.registerItem("redscaled_tuna", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> BIGEYE_TUNA = BUCKETABLE_FISHES_REGISTRY.registerItem("bigeye_tuna", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> SEA_BASS = BUCKETABLE_FISHES_REGISTRY.registerItem("sea_bass", FishItem::new);

    //mushroom islands
    DeferredItem<net.minecraft.world.item.Item> SHROOMFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("shroomfish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> SPOREFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("sporefish", FishItem::new);

    //underground
    DeferredItem<net.minecraft.world.item.Item> GOLD_FAN = BUCKETABLE_FISHES_REGISTRY.registerItem("gold_fan", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> GEODE_EEL = ITEMS.registerItem("geode_eel", Item::new);

    //caves
    DeferredItem<net.minecraft.world.item.Item> WHITEVEIL = BUCKETABLE_FISHES_REGISTRY.registerItem("whiteveil", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> BLACK_EEL = ITEMS.registerItem("black_eel", Item::new);
    DeferredItem<net.minecraft.world.item.Item> AMETHYSTBACK = BUCKETABLE_FISHES_REGISTRY.registerItem("amethystback", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> STONEFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("stonefish", FishItem::new);

    //dripstone caves
    DeferredItem<net.minecraft.world.item.Item> FOSSILIZED_ANGELFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("fossilized_angelfish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> DRIPFIN = BUCKETABLE_FISHES_REGISTRY.registerItem("dripfin", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> YELLOWSTONE_FISH = BUCKETABLE_FISHES_REGISTRY.registerItem("yellowstone_fish", FishItem::new);

    //lush caves
    DeferredItem<net.minecraft.world.item.Item> LUSH_PIKE = BUCKETABLE_FISHES_REGISTRY.registerItem("lush_pike", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> VIVID_MOSS = BUCKETABLE_FISHES_REGISTRY.registerItem("vivid_moss", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> THE_QUARRISH = BUCKETABLE_FISHES_REGISTRY.registerItem("the_quarrish", FishItem::new);

    //deepslate
    DeferredItem<net.minecraft.world.item.Item> GHOSTLY_PIKE = BUCKETABLE_FISHES_REGISTRY.registerItem("ghostly_pike", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> AQUAMARINE_PIKE = BUCKETABLE_FISHES_REGISTRY.registerItem("aquamarine_pike", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> GARNET_MACKEREL = BUCKETABLE_FISHES_REGISTRY.registerItem("garnet_mackerel", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> BRIGHT_AMETHYST_SNAPPER = BUCKETABLE_FISHES_REGISTRY.registerItem("bright_amethyst_snapper", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> DARK_AMETHYST_SNAPPER = BUCKETABLE_FISHES_REGISTRY.registerItem("dark_amethyst_snapper", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> DEEPSLATEFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("deepslatefish", FishItem::new);

    //deep dark
    DeferredItem<net.minecraft.world.item.Item> SCULKFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("sculkfish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> WARD = BUCKETABLE_FISHES_REGISTRY.registerItem("ward", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> GLOWING_DARK = BUCKETABLE_FISHES_REGISTRY.registerItem("glowing_dark", FishItem::new);

    //overworld surface lava
    DeferredItem<net.minecraft.world.item.Item> SUNEATER = BUCKETABLE_FISHES_REGISTRY.registerItem("suneater", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> PYROTROUT = BUCKETABLE_FISHES_REGISTRY.registerItem("pyrotrout", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> OBSIDIAN_EEL = ITEMS.registerItem("obsidian_eel", FireResistantBasicItem::new);

    //overworld underground lava
    DeferredItem<net.minecraft.world.item.Item> MOLTEN_SHRIMP = ITEMS.registerItem("molten_shrimp", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> OBSIDIAN_CRAB = ITEMS.registerItem("obsidian_crab", FireResistantBasicItem::new);

    //overworld deepslate lava
    DeferredItem<net.minecraft.world.item.Item> SCORCHED_BLOODSUCKER = ITEMS.registerItem("scorched_bloodsucker", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> MOLTEN_DEEPSLATE_CRAB = ITEMS.registerItem("molten_deepslate_crab", FireResistantBasicItem::new);

    //nether
    DeferredItem<net.minecraft.world.item.Item> EMBERGILL = BUCKETABLE_FISHES_REGISTRY.registerItem("embergill", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> SCALDING_PIKE = BUCKETABLE_FISHES_REGISTRY.registerItem("scalding_pike", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> CINDER_SQUID = ITEMS.registerItem("cinder_squid", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> LAVA_CRAB = ITEMS.registerItem("lava_crab", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> MAGMA_FISH = BUCKETABLE_FISHES_REGISTRY.registerItem("magma_fish", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> GLOWSTONE_SEEKER = BUCKETABLE_FISHES_REGISTRY.registerItem("glowstone_seeker", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> GLOWSTONE_PUFFERFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("glowstone_pufferfish", FireResistantBasicItem::new);
    DeferredItem<net.minecraft.world.item.Item> WILLISH = BUCKETABLE_FISHES_REGISTRY.registerItem("willish", FireResistantBasicItem::new);

    DeferredItem<net.minecraft.world.item.Item> CERBERAY = BUCKETABLE_FISHES_REGISTRY.registerItem("cerberay", FireResistantBasicItem::new);

    DeferredItem<net.minecraft.world.item.Item> LAVA_CRAB_CLAW = ITEMS.registerItem("lava_crab_claw", FireResistantBasicItem::new);

    //the end
    DeferredItem<net.minecraft.world.item.Item> CHARFISH = BUCKETABLE_FISHES_REGISTRY.registerItem("charfish", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> CHORUS_CRAB = ITEMS.registerItem("chorus_crab", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> END_GLOW = BUCKETABLE_FISHES_REGISTRY.registerItem("end_glow", FishItem::new);
    DeferredItem<net.minecraft.world.item.Item> VOIDBITER = BUCKETABLE_FISHES_REGISTRY.registerItem("voidbiter", FishItem::new);

    //bucket
    DeferredItem<net.minecraft.world.item.Item> STARCAUGHT_BUCKET = ITEMS.registerItem("starcaught_bucket", (p) -> new StarcaughtBucket(Fluids.WATER, p));

    DeferredItem<net.minecraft.world.item.Item> COOKED_STARCAUGHT_FISH = ITEMS.registerItem("cooked_starcaught_fish", (p) -> new net.minecraft.world.item.Item(p.food(SCFoodProperties.BASIC_COOKED_FISH).usingConvertsTo(SCItems.FISH_BONES.get())));

    static DeferredItem<net.minecraft.world.item.Item> register(DeferredRegister.Items reg, String name, Function<net.minecraft.world.item.Item.Properties, ? extends net.minecraft.world.item.Item> item)
    {
        return reg.registerItem(name, item);
    }

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
