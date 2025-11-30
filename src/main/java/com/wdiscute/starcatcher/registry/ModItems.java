package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideItem;
import com.wdiscute.starcatcher.items.*;
import com.wdiscute.starcatcher.items.cheater.*;
import com.wdiscute.starcatcher.rod.StarcatcherFishingRod;
import com.wdiscute.starcatcher.secretnotes.SecretNote;
import com.wdiscute.starcatcher.secretnotes.NoteContainer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public interface ModItems
{

    List<DeferredItem<Item>> fishes = new ArrayList<>();
    List<DeferredItem<Item>> trash = new ArrayList<>();

    DeferredRegister.Items ITEMS_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items RODS_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);
    DeferredRegister.Items OTHERS_REGISTRY = DeferredRegister.createItems(Starcatcher.MOD_ID);




    DeferredItem<Item> GUIDE = ITEMS_REGISTRY.register("starcatcher_guide", FishingGuideItem::new);

    DeferredItem<Item> FISH_SPOTTER = singleStackItem("fish_spotter");

    DeferredItem<Item> STARCATCHER_TWINE = basicItem("starcatcher_twine");

    //hooks
    DeferredItem<Item> HOOK = singleStackItem("hook");
    DeferredItem<Item> SHINY_HOOK = singleStackItem("shiny_hook");
    DeferredItem<Item> GOLD_HOOK = singleStackItem("gold_hook");
    DeferredItem<Item> MOSSY_HOOK = singleStackItem("mossy_hook");
    DeferredItem<Item> CRYSTAL_HOOK = singleStackItemFireResistant("crystal_hook");
    DeferredItem<Item> STONE_HOOK = singleStackItem("stone_hook");
    DeferredItem<Item> SPLIT_HOOK = singleStackItem("split_hook");
    DeferredItem<Item> STABILIZING_HOOK = singleStackItem("stabilizing_hook");
    DeferredItem<Item> HEAVY_HOOK = singleStackItem("heavy_hook");

    //bobbers
    DeferredItem<Item> CREEPER_BOBBER = singleStackItem("creeper_bobber");
    DeferredItem<Item> GLITTER_BOBBER = singleStackItem("glitter_bobber");
    DeferredItem<Item> COLORFUL_BOBBER = ITEMS_REGISTRY.register("colorful_bobber", ColorfulBobber::new);
    DeferredItem<Item> FRUGAL_BOBBER = singleStackItem("frugal_bobber");
    DeferredItem<Item> STEADY_BOBBER = singleStackItem("steady_bobber");
    DeferredItem<Item> IMPATIENT_BOBBER = singleStackItem("impatient_bobber");
    DeferredItem<Item> FROG_BOBBER = singleStackItem("frog_bobber");
    DeferredItem<Item> KIMBE_BOBBER = singleStackItem("kimbe_bobber");
    DeferredItem<Item> CLEAR_BOBBER = singleStackItem("clear_bobber");

    //baits
    DeferredItem<Item> CHERRY_BAIT = basicItem("cherry_bait");
    DeferredItem<Item> LUSH_BAIT = basicItem("lush_bait");
    DeferredItem<Item> SCULK_BAIT = basicItem("sculk_bait");
    DeferredItem<Item> DRIPSTONE_BAIT = basicItem("dripstone_bait");
    DeferredItem<Item> MURKWATER_BAIT = basicItem("murkwater_bait");
    DeferredItem<Item> LEGENDARY_BAIT = basicItem("legendary_bait");
    DeferredItem<Item> METEOROLOGICAL_BAIT = basicItem("meteorological_bait");


    //rods
    DeferredItem<Item> ROD = RODS_REGISTRY.register("starcatcher_rod", StarcatcherFishingRod::new);

    //fishing rod skins
    DeferredItem<Item> NATURALIST_ROD = RODS_REGISTRY.register("naturalist_rod", StarcatcherFishingRod::new);




    DeferredItem<Item> SETTINGS = ITEMS_REGISTRY.register("settings", () -> new Item(new Item.Properties()));


    //secrets
    DeferredItem<Item> SECRET_NOTE = ITEMS_REGISTRY.register("secret_note", SecretNote::new);
    DeferredItem<Item> BROKEN_BOTTLE = ITEMS_REGISTRY.register("broken_bottle", BrokenBottle::new);

    //notes
    DeferredItem<Item> DRIFTING_WATERLOGGED_BOTTLE = ITEMS_REGISTRY.register(
            "drifting_waterlogged_bottle", () ->
                    new NoteContainer(SecretNote.Note.CRYSTAL_HOOK));

    DeferredItem<Item> SCALDING_BOTTLE = ITEMS_REGISTRY.register(
            "scalding_bottle", () ->
                    new NoteContainer(new Item.Properties().stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_1));

    DeferredItem<Item> BURNING_BOTTLE = ITEMS_REGISTRY.register(
            "burning_bottle", () ->
                    new NoteContainer(new Item.Properties().stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_2));

    DeferredItem<Item> HOPEFUL_BOTTLE = ITEMS_REGISTRY.register(
            "hopeful_bottle", () ->
                    new NoteContainer(SecretNote.Note.HOPEFUL_NOTE));

    DeferredItem<Item> HOPELESS_BOTTLE = ITEMS_REGISTRY.register(
            "hopeless_bottle", () ->
                    new NoteContainer(SecretNote.Note.HOPELESS_NOTE));

    DeferredItem<Item> TRUE_BLUE_BOTTLE = ITEMS_REGISTRY.register(
            "true_blue_bottle", () ->
                    new NoteContainer(SecretNote.Note.TRUE_BLUE));





    //cheater items
    DeferredItem<Item> AWARD_ALL_FISHES = OTHERS_REGISTRY.register("award_all_fishes", AwardAllFishes::new);
    DeferredItem<Item> AWARD_ONE_FISH = OTHERS_REGISTRY.register("award_one_fish", AwardOneFish::new);
    DeferredItem<Item> REVOKE_ALL_FISHES = OTHERS_REGISTRY.register("revoke_all_fishes", RevokeAllFishes::new);

    DeferredItem<Item> AWARD_ALL_TROPHIES = OTHERS_REGISTRY.register("award_all_trophies", AwardAllTrophies::new);
    DeferredItem<Item> REVOKE_ALL_TROPHIES = OTHERS_REGISTRY.register("revoke_all_trophies", RevokeAllTrophies::new);

    DeferredItem<Item> AWARD_ALL_SECRETS = OTHERS_REGISTRY.register("award_all_secrets", AwardAllSecrets::new);
    DeferredItem<Item> REVOKE_ALL_SECRETS = OTHERS_REGISTRY.register("revoke_all_secrets", RevokeAllSecrets::new);

    DeferredItem<Item> REVOKE_ALL_EXTRAS = OTHERS_REGISTRY.register("revoke_all_extras", RevokeAllExtras::new);

    //treasure
    DeferredItem<Item> WATERLOGGED_SATCHEL = ITEMS_REGISTRY.register("waterlogged_satchel", () -> new FishingTreasure(Starcatcher.rl("treasure/waterlogged_satchel")));
    DeferredItem<Item> TREASURE = ITEMS_REGISTRY.register("treasure", () -> new FishingTreasure(Starcatcher.rl("treasure/treasure")));
    DeferredItem<Item> SCALDING_TREASURE = ITEMS_REGISTRY.register("scalding_treasure", () -> new FishingTreasure(Starcatcher.rl("treasure/scalding_treasure")));

    DeferredItem<Item> FISH_BONES = basicItem("fish_bones");

    DeferredItem<Item> MISSINGNO = basicItem("missingno");

    //
    //  ,---. ,--.         ,--.
    // /  .-' `--'  ,---.  |  ,---.   ,---.   ,---.
    // |  `-, ,--. (  .-'  |  .-.  | | .-. : (  .-'
    // |  .-' |  | .-'  `) |  | |  | \   --. .-'  `)
    // `--'   `--' `----'  `--' `--'  `----' `----'
    //


    //lake
    DeferredItem<Item> OBIDONTIEE = fish("obidontiee");
    DeferredItem<Item> SILVERVEIL_PERCH = fish("silverveil_perch");
    DeferredItem<Item> ELDERSCALE = fish("elderscale");
    DeferredItem<Item> DRIFTFIN = fish("driftfin");
    DeferredItem<Item> TWILIGHT_KOI = fish("twilight_koi");
    DeferredItem<Item> THUNDER_BASS = fish("thunder_bass");
    DeferredItem<Item> LIGHTNING_BASS = fish("lightning_bass");
    DeferredItem<Item> BOOT = trash("boot");

    //swamp
    DeferredItem<Item> SLUDGE_CATFISH = fish("sludge_catfish");
    DeferredItem<Item> LILY_SNAPPER = fish("lily_snapper");
    DeferredItem<Item> SAGE_CATFISH = fish("sage_catfish");
    DeferredItem<Item> MOSSY_BOOT = trash("mossy_boot");

    //darkoak_forest
    DeferredItem<Item> PALE_CARP = fish("pale_carp");
    DeferredItem<Item> PALE_PINFISH = fish("pale_pinfish");
    DeferredItem<Item> PINFISH = fish("pinfish");

    //icy lake
    DeferredItem<Item> FROSTJAW_TROUT = fish("frostjaw_trout");
    DeferredItem<Item> CRYSTALBACK_TROUT = fish("crystalback_trout");
    DeferredItem<Item> AURORA = fish("aurora");
    DeferredItem<Item> WINTERY_PIKE = fish("wintery_pike");

    //warm lake (desert/savanna etc)
    DeferredItem<Item> SANDTAIL = fish("sandtail");
    DeferredItem<Item> MIRAGE_CARP = fish("mirage_carp");
    DeferredItem<Item> SCORCHFISH = fish("scorchfish");
    DeferredItem<Item> CACTIFISH = fish("cactifish");
    DeferredItem<Item> AGAVE_BREAM = fish("agave_bream"); //TODO CHOSEN BY MANGO

    //mountain
    DeferredItem<Item> SUNNY_STURGEON = fish("sunny_sturgeon");
    DeferredItem<Item> ROCKGILL = fish("rockgill");
    DeferredItem<Item> PEAKDWELLER = fish("peakdweller");
    DeferredItem<Item> SUN_SEEKING_CARP = fish("sun_seeking_carp");

    //cherry grove
    DeferredItem<Item> BLOSSOMFISH = fish("blossomfish");
    DeferredItem<Item> PETALDRIFT_CARP = fish("petaldrift_carp");
    DeferredItem<Item> PINK_KOI = fish("pink_koi");
    DeferredItem<Item> MORGANITE = fish("morganite");
    DeferredItem<Item> ROSE_SIAMESE_FISH = fish("rose_siamese_fish");

    //icy mountain
    DeferredItem<Item> CRYSTALBACK_STURGEON = fish("crystalback_sturgeon");
    DeferredItem<Item> ICETOOTH_STURGEON = fish("icetooth_sturgeon");
    DeferredItem<Item> BOREAL = fish("boreal");
    DeferredItem<Item> CRYSTALBACK_BOREAL = fish("crystalback_boreal");

    //rivers
    DeferredItem<Item> SILVERFIN_PIKE = fish("silverfin_pike");
    DeferredItem<Item> WILLOW_BREAM = fish("willow_bream");
    DeferredItem<Item> DRIFTING_BREAM = fish("drifting_bream");
    DeferredItem<Item> DOWNFALL_BREAM = fish("downfall_bream");
    DeferredItem<Item> HOLLOWBELLY_DARTER = fish("hollowbelly_darter");
    DeferredItem<Item> MISTBACK_CHUB = fish("mistback_chub");
    DeferredItem<Item> DRIED_SEAWEED = trash("dried_seaweed");

    //icy river
    DeferredItem<Item> FROSTGILL_CHUB = fish("frostgill_chub");
    DeferredItem<Item> CRYSTALBACK_MINNOW = fish("crystalback_minnow");
    DeferredItem<Item> AZURE_CRYSTALBACK_MINNOW = fish("azure_crystalback_minnow");
    DeferredItem<Item> BLUE_CRYSTAL_FIN = fish("blue_crystal_fin");

    //saltwater
    DeferredItem<Item> IRONJAW_HERRING = fish("ironjaw_herring");
    DeferredItem<Item> DEEPJAW_HERRING = fish("deepjaw_herring");
    DeferredItem<Item> DUSKTAIL_SNAPPER = fish("dusktail_snapper");
    DeferredItem<Item> JOEL = fish("joel");
    DeferredItem<Item> REDSCALED_TUNA = fish("redscaled_tuna");
    DeferredItem<Item> BIGEYE_TUNA = fish("bigeye_tuna"); //added by Tuna Feesh
    DeferredItem<Item> SEA_BASS = fish("sea_bass");
    DeferredItem<Item> WATERLOGGED_BOTTLE = trash("waterlogged_bottle");

    //beaches
    DeferredItem<Item> CONCH = trash("conch");
    DeferredItem<Item> CLAM = trash("clam");

    //mushroom islands
    DeferredItem<Item> SHROOMFISH = fish("shroomfish");
    DeferredItem<Item> SPOREFISH = fish("sporefish");

    //underground
    DeferredItem<Item> GOLD_FAN = fish("gold_fan");
    DeferredItem<Item> GEODE_EEL = fish("geode_eel");

    //caves
    DeferredItem<Item> WHITEVEIL = fish("whiteveil");
    DeferredItem<Item> BLACK_EEL = fish("black_eel");
    DeferredItem<Item> AMETHYSTBACK = fish("amethystback");
    DeferredItem<Item> STONEFISH = fish("stonefish");

    //dripstone caves
    DeferredItem<Item> FOSSILIZED_ANGELFISH = fish("fossilized_angelfish");
    DeferredItem<Item> DRIPFIN = fish("dripfin");
    DeferredItem<Item> YELLOWSTONE_FISH = fish("yellowstone_fish");

    //lush caves
    DeferredItem<Item> LUSH_PIKE = fish("lush_pike");
    DeferredItem<Item> VIVID_MOSS = fish("vivid_moss");
    DeferredItem<Item> THE_QUARRISH = fish("the_quarrish");

    //deepslate
    DeferredItem<Item> GHOSTLY_PIKE = fish("ghostly_pike");
    DeferredItem<Item> AQUAMARINE_PIKE = fish("aquamarine_pike");
    DeferredItem<Item> GARNET_MACKEREL = fish("garnet_mackerel");
    DeferredItem<Item> BRIGHT_AMETHYST_SNAPPER = fish("bright_amethyst_snapper");
    DeferredItem<Item> DARK_AMETHYST_SNAPPER = fish("dark_amethyst_snapper");
    DeferredItem<Item> DEEPSLATEFISH = fish("deepslatefish");

    //deep dark
    DeferredItem<Item> SCULKFISH = fish("sculkfish");
    DeferredItem<Item> WARD = fish("ward");
    DeferredItem<Item> GLOWING_DARK = fish("glowing_dark");

    //overworld surface lava
    DeferredItem<Item> SUNEATER = fireResistantFish("suneater"); //description make sure to mention it eats sunfishes
    DeferredItem<Item> PYROTROUT = fireResistantFish("pyrotrout");
    DeferredItem<Item> OBSIDIAN_EEL = fireResistantFish("obsidian_eel");

    //overworld underground lava
    DeferredItem<Item> MOLTEN_SHRIMP = fireResistantFish("molten_shrimp");
    DeferredItem<Item> OBSIDIAN_CRAB = fireResistantFish("obsidian_crab");

    //overworld deepslate lava
    DeferredItem<Item> SCORCHED_BLOODSUCKER = fireResistantFish("scorched_bloodsucker");
    DeferredItem<Item> MOLTEN_DEEPSLATE_CRAB = fireResistantFish("molten_deepslate_crab");

    //nether
    DeferredItem<Item> EMBERGILL = fireResistantFish("embergill");
    DeferredItem<Item> SCALDING_PIKE = fireResistantFish("scalding_pike");
    DeferredItem<Item> CINDER_SQUID = fireResistantFish("cinder_squid");
    DeferredItem<Item> LAVA_CRAB = fireResistantFish("lava_crab");
    DeferredItem<Item> MAGMA_FISH = fireResistantFish("magma_fish");
    DeferredItem<Item> GLOWSTONE_SEEKER = fireResistantFish("glowstone_seeker");
    DeferredItem<Item> GLOWSTONE_PUFFERFISH = fireResistantFish("glowstone_pufferfish");
    DeferredItem<Item> WILLISH = fireResistantFish("willish");

    DeferredItem<Item> LAVA_CRAB_CLAW = fireResistantTrash("lava_crab_claw");

    //the end
    DeferredItem<Item> CHARFISH = fish("charfish"); //todo chosen by charry
    DeferredItem<Item> CHORUS_CRAB = fish("chorus_crab");
    DeferredItem<Item> END_GLOW = fish("end_glow");
    DeferredItem<Item> VOIDBITER = fish("voidbiter");


    private static DeferredItem<Item> fish(String name)
    {
        //chat didn't force me to write this comment
        DeferredItem<Item> item = ITEMS_REGISTRY.register(name, () -> new FishItem(new Item.Properties().food(ModFoodProperties.BASIC_RAW_FISH)));
        fishes.add(item);
        return item;
    }

    private static DeferredItem<Item> trash(String name)
    {
        DeferredItem<Item> item = ITEMS_REGISTRY.register(name, () -> new Item(new Item.Properties()));
        trash.add(item);
        return item;
    }

    private static DeferredItem<Item> fireResistantFish(String name)
    {
        DeferredItem<Item> item = ITEMS_REGISTRY.register(name, () -> new FishItem(new Item.Properties().fireResistant()));
        fishes.add(item);
        return item;
    }

    private static DeferredItem<Item> fireResistantTrash(String name)
    {
        DeferredItem<Item> item = ITEMS_REGISTRY.register(name, () -> new FishItem(new Item.Properties().fireResistant()));
        trash.add(item);
        return item;
    }

    private static DeferredItem<Item> singleStackItem(String name)
    {
        return ITEMS_REGISTRY.register(name, () -> new Item(new Item.Properties().stacksTo(1)));
    }

    private static DeferredItem<Item> singleStackItemFireResistant(String name)
    {
        return ITEMS_REGISTRY.register(name, () -> new Item(new Item.Properties().stacksTo(1).fireResistant()));
    }

    private static DeferredItem<Item> basicItem(String name)
    {
        return ITEMS_REGISTRY.register(name, () -> new Item(new Item.Properties()));
    }
}
