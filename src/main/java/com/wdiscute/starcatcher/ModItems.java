package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.guide.FishingGuideItem;
import com.wdiscute.starcatcher.items.TrophyBronze;
import com.wdiscute.starcatcher.items.TrophyGold;
import com.wdiscute.starcatcher.items.TrophySilver;
import com.wdiscute.starcatcher.items.*;
import com.wdiscute.starcatcher.rod.StarcatcherFishingRod;
import com.wdiscute.starcatcher.secretnotes.SecretNote;
import com.wdiscute.starcatcher.secretnotes.NoteContainer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public interface ModItems
{

    List<DeferredItem<Item>> fishes = new ArrayList<>();
    List<DeferredItem<Item>> trash = new ArrayList<>();

    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Starcatcher.MOD_ID);

    DeferredItem<Item> GUIDE = ITEMS.register("starcatcher_guide", FishingGuideItem::new);

    DeferredItem<Item> FISH_SPOTTER = singleStackItem("fish_spotter");

    DeferredItem<Item> STARCATCHER_TWINE = basicItem("starcatcher_twine");

    DeferredItem<Item> HOOK = singleStackItem("hook");  //done
    DeferredItem<Item> SHINY_HOOK = singleStackItem("shiny_hook");  //done
    DeferredItem<Item> GOLD_HOOK = singleStackItem("gold_hook");    //done
    DeferredItem<Item> MOSSY_HOOK = singleStackItem("mossy_hook");  //done
    DeferredItem<Item> CRYSTAL_HOOK = singleStackItem("crystal_hook"); //done
    DeferredItem<Item> STONE_HOOK = singleStackItem("stone_hook");  //done
    DeferredItem<Item> SPLIT_HOOK = singleStackItem("split_hook");  //done

    DeferredItem<Item> CREEPER_BOBBER = singleStackItem("creeper_bobber"); //done
    DeferredItem<Item> GLITTER_BOBBER = singleStackItem("glitter_bobber"); //done
    DeferredItem<Item> COLORFUL_BOBBER = ITEMS.register("colorful_bobber", ColorfulBobber::new);
    DeferredItem<Item> FRUGAL_BOBBER = singleStackItem("frugal_bobber"); //done
    DeferredItem<Item> STEADY_BOBBER = singleStackItem("steady_bobber"); //done
    DeferredItem<Item> IMPATIENT_BOBBER = singleStackItem("impatient_bobber"); //done
    DeferredItem<Item> FROG_BOBBER = singleStackItem("frog_bobber");

    DeferredItem<Item> CHERRY_BAIT = basicItem("cherry_bait"); //missing datagen for fishes
    DeferredItem<Item> LUSH_BAIT = basicItem("lush_bait");
    DeferredItem<Item> SCULK_BAIT = basicItem("sculk_bait");
    DeferredItem<Item> DRIPSTONE_BAIT = basicItem("dripstone_bait");
    DeferredItem<Item> MURKWATER_BAIT = basicItem("murkwater_bait");
    DeferredItem<Item> LEGENDARY_BAIT = basicItem("legendary_bait");
    DeferredItem<Item> METEOROLOGICAL_BAIT = basicItem("meteorological_bait");

    DeferredItem<Item> ROD = ITEMS.register("starcatcher_rod", StarcatcherFishingRod::new);

    DeferredItem<Item> TROPHY_GOLD = ITEMS.register("trophy_gold", TrophyGold::new);
    DeferredItem<Item> TROPHY_SILVER = ITEMS.register("trophy_silver", TrophySilver::new);
    DeferredItem<Item> TROPHY_BRONZE = ITEMS.register("trophy_bronze", TrophyBronze::new);








    DeferredItem<Item> SECRET_NOTE = ITEMS.register("secret_note", SecretNote::new);
    DeferredItem<Item> BROKEN_BOTTLE = singleStackItem("broken_bottle");

    DeferredItem<Item> DRIFTING_WATERLOGGED_BOTTLE = ITEMS.register("drifting_waterlogged_bottle", () -> new NoteContainer(SecretNote.Note.CRYSTAL_HOOK));










    DeferredItem<Item> WATERLOGGED_SATCHEL = ITEMS.register("waterlogged_satchel", () -> new FishingTreasure(Starcatcher.rl("treasure/waterlogged_satchel")));
    DeferredItem<Item> TREASURE = ITEMS.register("treasure", () -> new FishingTreasure(Starcatcher.rl("treasure/treasure")));
    DeferredItem<Item> SCALDING_TREASURE = ITEMS.register("scalding_treasure", () -> new FishingTreasure(Starcatcher.rl("treasure/scalding_treasure")));

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
    DeferredItem<Item> OBIDONTIEE = fish("obidontiee"); //description
    DeferredItem<Item> SILVERVEIL_PERCH = fish("silverveil_perch"); //description
    DeferredItem<Item> ELDERSCALE = fish("elderscale"); //description
    DeferredItem<Item> DRIFTFIN = fish("driftfin"); //description
    DeferredItem<Item> TWILIGHT_KOI = fish("twilight_koi"); //description
    DeferredItem<Item> THUNDER_BASS = fish("thunder_bass"); //description
    DeferredItem<Item> LIGHTNING_BASS = fish("lightning_bass"); //description
    DeferredItem<Item> BOOT = trash("boot"); //description

    //swamp
    DeferredItem<Item> SLUDGE_CATFISH = fish("sludge_catfish"); //description
    DeferredItem<Item> LILY_SNAPPER = fish("lily_snapper"); //description
    DeferredItem<Item> SAGE_CATFISH = fish("sage_catfish"); //description
    DeferredItem<Item> MOSSY_BOOT = trash("mossy_boot"); //description

    //darkoak_forest
    DeferredItem<Item> PALE_CARP = fish("pale_carp"); //description
    DeferredItem<Item> PALE_PINFISH = fish("pale_pinfish"); //description
    DeferredItem<Item> PINFISH = fish("pinfish"); //description

    //icy lake
    DeferredItem<Item> FROSTJAW_TROUT = fish("frostjaw_trout"); //description
    DeferredItem<Item> CRYSTALBACK_TROUT = fish("crystalback_trout"); //description
    DeferredItem<Item> AURORA = fish("aurora"); //description
    DeferredItem<Item> WINTERY_PIKE = fish("wintery_pike"); //description

    //warm lake (desert/savanna etc)
    DeferredItem<Item> SANDTAIL = fish("sandtail"); //description
    DeferredItem<Item> MIRAGE_CARP = fish("mirage_carp"); //description
    DeferredItem<Item> SCORCHFISH = fish("scorchfish"); //description
    DeferredItem<Item> CACTIFISH = fish("cactifish"); //description
    DeferredItem<Item> AGAVE_BREAM = fish("agave_bream"); //TODO CHOSEN BY MANGO

    //mountain
    DeferredItem<Item> SUNNY_STURGEON = fish("sunny_sturgeon"); //description
    DeferredItem<Item> ROCKGILL = fish("rockgill"); //description
    DeferredItem<Item> PEAKDWELLER = fish("peakdweller"); //description
    DeferredItem<Item> SUN_SEEKING_CARP = fish("sun_seeking_carp"); //description

    //cherry grove
    DeferredItem<Item> BLOSSOMFISH = fish("blossomfish"); //description
    DeferredItem<Item> PETALDRIFT_CARP = fish("petaldrift_carp"); //description
    DeferredItem<Item> PINK_KOI = fish("pink_koi"); //description
    DeferredItem<Item> MORGANITE = fish("morganite"); //description
    DeferredItem<Item> ROSE_SIAMESE_FISH = fish("rose_siamese_fish"); //description

    //icy mountain
    DeferredItem<Item> CRYSTALBACK_STURGEON = fish("crystalback_sturgeon"); //description
    DeferredItem<Item> ICETOOTH_STURGEON = fish("icetooth_sturgeon"); //description
    DeferredItem<Item> BOREAL = fish("boreal"); //description
    DeferredItem<Item> CRYSTALBACK_BOREAL = fish("crystalback_boreal"); //description

    //rivers
    DeferredItem<Item> SILVERFIN_PIKE = fish("silverfin_pike"); //description
    DeferredItem<Item> WILLOW_BREAM = fish("willow_bream"); //description
    DeferredItem<Item> DRIFTING_BREAM = fish("drifting_bream"); //description
    DeferredItem<Item> DOWNFALL_BREAM = fish("downfall_bream"); //description
    DeferredItem<Item> HOLLOWBELLY_DARTER = fish("hollowbelly_darter"); //description
    DeferredItem<Item> MISTBACK_CHUB = fish("mistback_chub"); //description
    DeferredItem<Item> DRIED_SEAWEED = trash("dried_seaweed"); //description

    //icy river
    DeferredItem<Item> FROSTGILL_CHUB = fish("frostgill_chub"); //description
    DeferredItem<Item> CRYSTALBACK_MINNOW = fish("crystalback_minnow"); //description
    DeferredItem<Item> AZURE_CRYSTALBACK_MINNOW = fish("azure_crystalback_minnow"); //description
    DeferredItem<Item> BLUE_CRYSTAL_FIN = fish("blue_crystal_fin"); //description

    //saltwater
    DeferredItem<Item> IRONJAW_HERRING = fish("ironjaw_herring"); //description
    DeferredItem<Item> DEEPJAW_HERRING = fish("deepjaw_herring"); //description
    DeferredItem<Item> DUSKTAIL_SNAPPER = fish("dusktail_snapper"); //description
    DeferredItem<Item> JOEL = fish("joel"); //description
    DeferredItem<Item> REDSCALED_TUNA = fish("redscaled_tuna"); //description
    DeferredItem<Item> BIGEYE_TUNA = fish("bigeye_tuna"); //added by Tuna Feesh
    DeferredItem<Item> SEA_BASS = fish("sea_bass"); //description
    DeferredItem<Item> WATERLOGGED_BOTTLE = trash("waterlogged_bottle"); //description

    //beaches
    DeferredItem<Item> CONCH = trash("conch"); //description
    DeferredItem<Item> CLAM = trash("clam"); //description

    //mushroom islands
    DeferredItem<Item> SHROOMFISH = fish("shroomfish"); //description
    DeferredItem<Item> SPOREFISH = fish("sporefish"); //description

    //underground
    DeferredItem<Item> GOLD_FAN = fish("gold_fan"); //description
    DeferredItem<Item> GEODE_EEL = fish("geode_eel"); //description

    //caves
    DeferredItem<Item> WHITEVEIL = fish("whiteveil"); //description
    DeferredItem<Item> BLACK_EEL = fish("black_eel"); //description
    DeferredItem<Item> AMETHYSTBACK = fish("amethystback"); //description
    DeferredItem<Item> STONEFISH = fish("stonefish"); //description

    //dripstone caves
    DeferredItem<Item> FOSSILIZED_ANGELFISH = fish("fossilized_angelfish"); //description
    DeferredItem<Item> DRIPFIN = fish("dripfin"); //description
    DeferredItem<Item> YELLOWSTONE_FISH = fish("yellowstone_fish"); //description

    //lush caves
    DeferredItem<Item> LUSH_PIKE = fish("lush_pike"); //description
    DeferredItem<Item> VIVID_MOSS = fish("vivid_moss"); //description

    //deepslate
    DeferredItem<Item> GHOSTLY_PIKE = fish("ghostly_pike"); //description
    DeferredItem<Item> AQUAMARINE_PIKE = fish("aquamarine_pike"); //description
    DeferredItem<Item> GARNET_MACKEREL = fish("garnet_mackerel"); //description
    DeferredItem<Item> BRIGHT_AMETHYST_SNAPPER = fish("bright_amethyst_snapper"); //description
    DeferredItem<Item> DARK_AMETHYST_SNAPPER = fish("dark_amethyst_snapper"); //description
    DeferredItem<Item> DEEPSLATEFISH = fish("deepslatefish"); //description

    //deep dark
    DeferredItem<Item> SCULKFISH = fish("sculkfish"); //description
    DeferredItem<Item> WARD = fish("ward"); //description
    DeferredItem<Item> GLOWING_DARK = fish("glowing_dark"); //description

    //overworld surface lava
    DeferredItem<Item> SUNEATER = fireResistantFish("suneater"); //description make sure to mention it eats sunfishes
    DeferredItem<Item> PYROTROUT = fireResistantFish("pyrotrout"); //description
    DeferredItem<Item> OBSIDIAN_EEL = fireResistantFish("obsidian_eel"); //description

    //overworld underground lava
    DeferredItem<Item> MOLTEN_SHRIMP = fireResistantFish("molten_shrimp"); //description
    DeferredItem<Item> OBSIDIAN_CRAB = fireResistantFish("obsidian_crab"); //description

    //overworld deepslate lava
    DeferredItem<Item> SCORCHED_BLOODSUCKER = fireResistantFish("scorched_bloodsucker"); //description
    DeferredItem<Item> MOLTEN_DEEPSLATE_CRAB = fireResistantFish("molten_deepslate_crab"); //description


    //nether
    DeferredItem<Item> EMBERGILL = fireResistantFish("embergill"); //description
    DeferredItem<Item> SCALDING_PIKE = fireResistantFish("scalding_pike"); //description
    DeferredItem<Item> CINDER_SQUID = fireResistantFish("cinder_squid"); //description
    DeferredItem<Item> LAVA_CRAB = fireResistantFish("lava_crab"); //description
    DeferredItem<Item> MAGMA_FISH = fireResistantFish("magma_fish"); //description
    DeferredItem<Item> GLOWSTONE_SEEKER = fireResistantFish("glowstone_seeker"); //description
    DeferredItem<Item> GLOWSTONE_PUFFERFISH = fireResistantFish("glowstone_pufferfish"); //description
    DeferredItem<Item> LAVA_CRAB_CLAW = fireResistantTrash("lava_crab_claw"); //description

    //the end
    DeferredItem<Item> CHARFISH = fish("charfish"); //todo chosen by charry
    DeferredItem<Item> CHORUS_CRAB = fish("chorus_crab"); //description
    DeferredItem<Item> END_GLOW = fish("end_glow"); //description


    private static DeferredItem<Item> fish(String name)
    {
        //chat didn't force me to write this comment
        DeferredItem<Item> item = ITEMS.register(name, () -> new FishItem(new Item.Properties().food(ModFoodProperties.BASIC_RAW_FISH)));
        fishes.add(item);
        return item;
    }

    private static DeferredItem<Item> trash(String name)
    {
        DeferredItem<Item> item = ITEMS.register(name, () -> new Item(new Item.Properties()));
        trash.add(item);
        return item;
    }

    private static DeferredItem<Item> fireResistantFish(String name)
    {
        DeferredItem<Item> item = ITEMS.register(name, () -> new FishItem(new Item.Properties().fireResistant()));
        fishes.add(item);
        return item;
    }

    private static DeferredItem<Item> fireResistantTrash(String name)
    {
        DeferredItem<Item> item = ITEMS.register(name, () -> new FishItem(new Item.Properties().fireResistant()));
        trash.add(item);
        return item;
    }

    static DeferredItem<Item> singleStackItem(String name)
    {
        return ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(1)));
    }

    static DeferredItem<Item> basicItem(String name)
    {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }


    static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
