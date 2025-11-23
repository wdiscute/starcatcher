package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.guide.FishingGuideItem;
import com.wdiscute.starcatcher.items.*;
import com.wdiscute.starcatcher.items.cheater.*;
import com.wdiscute.starcatcher.rod.StarcatcherFishingRod;
import com.wdiscute.starcatcher.secretnotes.NoteContainer;
import com.wdiscute.starcatcher.secretnotes.SecretNote;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public interface ModItems
{

    List<RegistryObject<Item>> fishes = new ArrayList<>();
    List<RegistryObject<Item>> trash = new ArrayList<>();

    DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Starcatcher.MOD_ID);

    RegistryObject<Item> GUIDE = ITEMS.register("starcatcher_guide", FishingGuideItem::new);

    RegistryObject<Item> FISH_SPOTTER = singleStackItem("fish_spotter");

    RegistryObject<Item> STARCATCHER_TWINE = basicItem("starcatcher_twine");

    //hooks
    RegistryObject<Item> HOOK = singleStackItem("hook");  //done
    RegistryObject<Item> SHINY_HOOK = singleStackItem("shiny_hook");  //done
    RegistryObject<Item> GOLD_HOOK = singleStackItem("gold_hook");    //done
    RegistryObject<Item> MOSSY_HOOK = singleStackItem("mossy_hook");  //done
    RegistryObject<Item> CRYSTAL_HOOK = singleStackItemFireResistant("crystal_hook"); //done
    RegistryObject<Item> STONE_HOOK = singleStackItem("stone_hook");  //done
    RegistryObject<Item> SPLIT_HOOK = singleStackItem("split_hook");  //done
    RegistryObject<Item> STABILIZING_HOOK = singleStackItem("stabilizing_hook");
    RegistryObject<Item> HEAVY_HOOK = singleStackItem("heavy_hook");

    //bobbers
    RegistryObject<Item> CREEPER_BOBBER = singleStackItem("creeper_bobber"); //done
    RegistryObject<Item> GLITTER_BOBBER = singleStackItem("glitter_bobber"); //done
    RegistryObject<Item> COLORFUL_BOBBER = ITEMS.register("colorful_bobber", ColorfulBobber::new); //done
    RegistryObject<Item> FRUGAL_BOBBER = singleStackItem("frugal_bobber"); //done
    RegistryObject<Item> STEADY_BOBBER = singleStackItem("steady_bobber"); //done
    RegistryObject<Item> IMPATIENT_BOBBER = singleStackItem("impatient_bobber"); //done
    RegistryObject<Item> FROG_BOBBER = singleStackItem("frog_bobber");
    RegistryObject<Item> KIMBE_BOBBER = singleStackItem("kimbe_bobber");
    RegistryObject<Item> CLEAR_BOBBER = singleStackItem("clear_bobber");

    //baits
    RegistryObject<Item> CHERRY_BAIT = basicItem("cherry_bait"); //done
    RegistryObject<Item> LUSH_BAIT = basicItem("lush_bait"); //done
    RegistryObject<Item> SCULK_BAIT = basicItem("sculk_bait"); //done
    RegistryObject<Item> DRIPSTONE_BAIT = basicItem("dripstone_bait"); //done
    RegistryObject<Item> MURKWATER_BAIT = basicItem("murkwater_bait"); //done
    RegistryObject<Item> LEGENDARY_BAIT = basicItem("legendary_bait"); //done
    RegistryObject<Item> METEOROLOGICAL_BAIT = basicItem("meteorological_bait"); //done

    RegistryObject<Item> ROD = ITEMS.register("starcatcher_rod", StarcatcherFishingRod::new); //missing better tooltip

    RegistryObject<Item> SETTINGS = singleStackItem("settings");


    //secrets
    RegistryObject<Item> SECRET_NOTE = ITEMS.register("secret_note", SecretNote::new);
    RegistryObject<Item> BROKEN_BOTTLE = ITEMS.register("broken_bottle", BrokenBottle::new);

    //notes
    RegistryObject<Item> DRIFTING_WATERLOGGED_BOTTLE = ITEMS.register("drifting_waterlogged_bottle", () ->
            new NoteContainer(SecretNote.Note.CRYSTAL_HOOK));

    RegistryObject<Item> SCALDING_BOTTLE = ITEMS.register("scalding_bottle", () ->
            new NoteContainer(new Item.Properties().stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_1));

    RegistryObject<Item> BURNING_BOTTLE = ITEMS.register("burning_bottle", () ->
            new NoteContainer(new Item.Properties().stacksTo(1).fireResistant(), SecretNote.Note.ARNWULF_2));

    RegistryObject<Item> HOPEFUL_BOTTLE = ITEMS.register("hopeful_bottle", () ->
            new NoteContainer(SecretNote.Note.HOPEFUL_NOTE));

    RegistryObject<Item> HOPELESS_BOTTLE = ITEMS.register("hopeless_bottle", () ->
            new NoteContainer(SecretNote.Note.HOPELESS_NOTE));

    RegistryObject<Item> TRUE_BLUE_BOTTLE = ITEMS.register("true_blue_bottle", () ->
            new NoteContainer(SecretNote.Note.TRUE_BLUE));



    //cheater items
    RegistryObject<Item> AWARD_ALL_FISHES = ITEMS.register("award_all_fishes", AwardAllFishes::new);
    RegistryObject<Item> AWARD_ONE_FISH = ITEMS.register("award_one_fish", AwardOneFish::new);
    RegistryObject<Item> REVOKE_ALL_FISHES = ITEMS.register("revoke_all_fishes", RevokeAllFishes::new);

    RegistryObject<Item> AWARD_ALL_TROPHIES = ITEMS.register("award_all_trophies", AwardAllTrophies::new);
    RegistryObject<Item> REVOKE_ALL_TROPHIES = ITEMS.register("revoke_all_trophies", RevokeAllTrophies::new);

    RegistryObject<Item> AWARD_ALL_SECRETS = ITEMS.register("award_all_secrets", AwardAllSecrets::new);
    RegistryObject<Item> REVOKE_ALL_SECRETS = ITEMS.register("revoke_all_secrets", RevokeAllSecrets::new);

    RegistryObject<Item> REVOKE_ALL_EXTRAS = ITEMS.register("revoke_all_extras", RevokeAllExtras::new);

    //treasure
    RegistryObject<Item> WATERLOGGED_SATCHEL = ITEMS.register("waterlogged_satchel", () -> new FishingTreasure(Starcatcher.rl("treasure/waterlogged_satchel")));
    RegistryObject<Item> TREASURE = ITEMS.register("treasure", () -> new FishingTreasure(Starcatcher.rl("treasure/treasure")));
    RegistryObject<Item> SCALDING_TREASURE = ITEMS.register("scalding_treasure", () -> new FishingTreasure(Starcatcher.rl("treasure/scalding_treasure")));

    RegistryObject<Item> FISH_BONES = basicItem("fish_bones");

    RegistryObject<Item> MISSINGNO = basicItem("missingno");

    //
    //  ,---. ,--.         ,--.
    // /  .-' `--'  ,---.  |  ,---.   ,---.   ,---.
    // |  `-, ,--. (  .-'  |  .-.  | | .-. : (  .-'
    // |  .-' |  | .-'  `) |  | |  | \   --. .-'  `)
    // `--'   `--' `----'  `--' `--'  `----' `----'
    //


    //lake
    RegistryObject<Item> OBIDONTIEE = fish("obidontiee"); //description
    RegistryObject<Item> SILVERVEIL_PERCH = fish("silverveil_perch"); //description
    RegistryObject<Item> ELDERSCALE = fish("elderscale"); //description
    RegistryObject<Item> DRIFTFIN = fish("driftfin"); //description
    RegistryObject<Item> TWILIGHT_KOI = fish("twilight_koi"); //description
    RegistryObject<Item> THUNDER_BASS = fish("thunder_bass"); //description
    RegistryObject<Item> LIGHTNING_BASS = fish("lightning_bass"); //description
    RegistryObject<Item> BOOT = trash("boot"); //description

    //swamp
    RegistryObject<Item> SLUDGE_CATFISH = fish("sludge_catfish"); //description
    RegistryObject<Item> LILY_SNAPPER = fish("lily_snapper"); //description
    RegistryObject<Item> SAGE_CATFISH = fish("sage_catfish"); //description
    RegistryObject<Item> MOSSY_BOOT = trash("mossy_boot"); //description

    //darkoak_forest
    RegistryObject<Item> PALE_CARP = fish("pale_carp"); //description
    RegistryObject<Item> PALE_PINFISH = fish("pale_pinfish"); //description
    RegistryObject<Item> PINFISH = fish("pinfish"); //description

    //icy lake
    RegistryObject<Item> FROSTJAW_TROUT = fish("frostjaw_trout"); //description
    RegistryObject<Item> CRYSTALBACK_TROUT = fish("crystalback_trout"); //description
    RegistryObject<Item> AURORA = fish("aurora"); //description
    RegistryObject<Item> WINTERY_PIKE = fish("wintery_pike"); //description

    //warm lake (desert/savanna etc)
    RegistryObject<Item> SANDTAIL = fish("sandtail"); //description
    RegistryObject<Item> MIRAGE_CARP = fish("mirage_carp"); //description
    RegistryObject<Item> SCORCHFISH = fish("scorchfish"); //description
    RegistryObject<Item> CACTIFISH = fish("cactifish"); //description
    RegistryObject<Item> AGAVE_BREAM = fish("agave_bream"); //TODO CHOSEN BY MANGO

    //mountain
    RegistryObject<Item> SUNNY_STURGEON = fish("sunny_sturgeon"); //description
    RegistryObject<Item> ROCKGILL = fish("rockgill"); //description
    RegistryObject<Item> PEAKDWELLER = fish("peakdweller"); //description
    RegistryObject<Item> SUN_SEEKING_CARP = fish("sun_seeking_carp"); //description

    //cherry grove
    RegistryObject<Item> BLOSSOMFISH = fish("blossomfish"); //description
    RegistryObject<Item> PETALDRIFT_CARP = fish("petaldrift_carp"); //description
    RegistryObject<Item> PINK_KOI = fish("pink_koi"); //description
    RegistryObject<Item> MORGANITE = fish("morganite"); //description
    RegistryObject<Item> ROSE_SIAMESE_FISH = fish("rose_siamese_fish"); //description

    //icy mountain
    RegistryObject<Item> CRYSTALBACK_STURGEON = fish("crystalback_sturgeon"); //description
    RegistryObject<Item> ICETOOTH_STURGEON = fish("icetooth_sturgeon"); //description
    RegistryObject<Item> BOREAL = fish("boreal"); //description
    RegistryObject<Item> CRYSTALBACK_BOREAL = fish("crystalback_boreal"); //description

    //rivers
    RegistryObject<Item> SILVERFIN_PIKE = fish("silverfin_pike"); //description
    RegistryObject<Item> WILLOW_BREAM = fish("willow_bream"); //description
    RegistryObject<Item> DRIFTING_BREAM = fish("drifting_bream"); //description
    RegistryObject<Item> DOWNFALL_BREAM = fish("downfall_bream"); //description
    RegistryObject<Item> HOLLOWBELLY_DARTER = fish("hollowbelly_darter"); //description
    RegistryObject<Item> MISTBACK_CHUB = fish("mistback_chub"); //description
    RegistryObject<Item> DRIED_SEAWEED = trash("dried_seaweed"); //description

    //icy river
    RegistryObject<Item> FROSTGILL_CHUB = fish("frostgill_chub"); //description
    RegistryObject<Item> CRYSTALBACK_MINNOW = fish("crystalback_minnow"); //description
    RegistryObject<Item> AZURE_CRYSTALBACK_MINNOW = fish("azure_crystalback_minnow"); //description
    RegistryObject<Item> BLUE_CRYSTAL_FIN = fish("blue_crystal_fin"); //description

    //saltwater
    RegistryObject<Item> IRONJAW_HERRING = fish("ironjaw_herring"); //description
    RegistryObject<Item> DEEPJAW_HERRING = fish("deepjaw_herring"); //description
    RegistryObject<Item> DUSKTAIL_SNAPPER = fish("dusktail_snapper"); //description
    RegistryObject<Item> JOEL = fish("joel"); //description
    RegistryObject<Item> REDSCALED_TUNA = fish("redscaled_tuna"); //description
    RegistryObject<Item> BIGEYE_TUNA = fish("bigeye_tuna"); //added by Tuna Feesh
    RegistryObject<Item> SEA_BASS = fish("sea_bass"); //description
    RegistryObject<Item> WATERLOGGED_BOTTLE = trash("waterlogged_bottle"); //description

    //beaches
    RegistryObject<Item> CONCH = trash("conch"); //description
    RegistryObject<Item> CLAM = trash("clam"); //description

    //mushroom islands
    RegistryObject<Item> SHROOMFISH = fish("shroomfish"); //description
    RegistryObject<Item> SPOREFISH = fish("sporefish"); //description

    //underground
    RegistryObject<Item> GOLD_FAN = fish("gold_fan"); //description
    RegistryObject<Item> GEODE_EEL = fish("geode_eel"); //description

    //caves
    RegistryObject<Item> WHITEVEIL = fish("whiteveil"); //description
    RegistryObject<Item> BLACK_EEL = fish("black_eel"); //description
    RegistryObject<Item> AMETHYSTBACK = fish("amethystback"); //description
    RegistryObject<Item> STONEFISH = fish("stonefish"); //description

    //dripstone caves
    RegistryObject<Item> FOSSILIZED_ANGELFISH = fish("fossilized_angelfish"); //description
    RegistryObject<Item> DRIPFIN = fish("dripfin"); //description
    RegistryObject<Item> YELLOWSTONE_FISH = fish("yellowstone_fish"); //description

    //lush caves
    RegistryObject<Item> LUSH_PIKE = fish("lush_pike"); //description
    RegistryObject<Item> VIVID_MOSS = fish("vivid_moss"); //description
    RegistryObject<Item> THE_QUARRISH = fish("the_quarrish");

    //deepslate
    RegistryObject<Item> GHOSTLY_PIKE = fish("ghostly_pike"); //description
    RegistryObject<Item> AQUAMARINE_PIKE = fish("aquamarine_pike"); //description
    RegistryObject<Item> GARNET_MACKEREL = fish("garnet_mackerel"); //description
    RegistryObject<Item> BRIGHT_AMETHYST_SNAPPER = fish("bright_amethyst_snapper"); //description
    RegistryObject<Item> DARK_AMETHYST_SNAPPER = fish("dark_amethyst_snapper"); //description
    RegistryObject<Item> DEEPSLATEFISH = fish("deepslatefish"); //description

    //deep dark
    RegistryObject<Item> SCULKFISH = fish("sculkfish"); //description
    RegistryObject<Item> WARD = fish("ward"); //description
    RegistryObject<Item> GLOWING_DARK = fish("glowing_dark"); //description

    //overworld surface lava
    RegistryObject<Item> SUNEATER = fireResistantFish("suneater"); //description make sure to mention it eats sunfishes
    RegistryObject<Item> PYROTROUT = fireResistantFish("pyrotrout"); //description
    RegistryObject<Item> OBSIDIAN_EEL = fireResistantFish("obsidian_eel"); //description

    //overworld underground lava
    RegistryObject<Item> MOLTEN_SHRIMP = fireResistantFish("molten_shrimp"); //description
    RegistryObject<Item> OBSIDIAN_CRAB = fireResistantFish("obsidian_crab"); //description

    //overworld deepslate lava
    RegistryObject<Item> SCORCHED_BLOODSUCKER = fireResistantFish("scorched_bloodsucker"); //description
    RegistryObject<Item> MOLTEN_DEEPSLATE_CRAB = fireResistantFish("molten_deepslate_crab"); //description


    //nether
    RegistryObject<Item> EMBERGILL = fireResistantFish("embergill"); //description
    RegistryObject<Item> SCALDING_PIKE = fireResistantFish("scalding_pike"); //description
    RegistryObject<Item> CINDER_SQUID = fireResistantFish("cinder_squid"); //description
    RegistryObject<Item> LAVA_CRAB = fireResistantFish("lava_crab"); //description
    RegistryObject<Item> MAGMA_FISH = fireResistantFish("magma_fish"); //description
    RegistryObject<Item> GLOWSTONE_SEEKER = fireResistantFish("glowstone_seeker"); //description
    RegistryObject<Item> GLOWSTONE_PUFFERFISH = fireResistantFish("glowstone_pufferfish"); //description
    RegistryObject<Item> LAVA_CRAB_CLAW = fireResistantTrash("lava_crab_claw"); //description

    //the end
    RegistryObject<Item> CHARFISH = fish("charfish"); //todo chosen by charry
    RegistryObject<Item> CHORUS_CRAB = fish("chorus_crab"); //description
    RegistryObject<Item> END_GLOW = fish("end_glow"); //description
    RegistryObject<Item> VOIDBITER = fish("voidbiter");


    private static RegistryObject<Item> fish(String name)
    {
        //chat didn't force me to write this comment
        RegistryObject<Item> item = ITEMS.register(name, () -> new FishItem(new Item.Properties().food(ModFoodProperties.BASIC_RAW_FISH)));
        fishes.add(item);
        return item;
    }

    private static RegistryObject<Item> trash(String name)
    {
        RegistryObject<Item> item = ITEMS.register(name, () -> new Item(new Item.Properties()));
        trash.add(item);
        return item;
    }

    private static RegistryObject<Item> fireResistantFish(String name)
    {
        RegistryObject<Item> item = ITEMS.register(name, () -> new FishItem(new Item.Properties().fireResistant()));
        fishes.add(item);
        return item;
    }

    private static RegistryObject<Item> fireResistantTrash(String name)
    {
        RegistryObject<Item> item = ITEMS.register(name, () -> new FishItem(new Item.Properties().fireResistant()));
        trash.add(item);
        return item;
    }

    static RegistryObject<Item> singleStackItem(String name)
    {
        return ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(1)));
    }

    static RegistryObject<Item> singleStackItemFireResistant(String name)
    {
        return ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(1).fireResistant()));
    }

    static RegistryObject<Item> basicItem(String name)
    {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }


    static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
