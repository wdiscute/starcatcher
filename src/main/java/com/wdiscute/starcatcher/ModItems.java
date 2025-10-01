package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.guide.FishingGuideItem;
import com.wdiscute.starcatcher.items.FishItem;
import com.wdiscute.starcatcher.items.TrophyBronze;
import com.wdiscute.starcatcher.items.TrophyGold;
import com.wdiscute.starcatcher.items.TrophySilver;
import com.wdiscute.starcatcher.rod.StarcatcherFishingRod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems
{

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Starcatcher.MOD_ID);

    public static final DeferredItem<Item> GUIDE = ITEMS.register("starcatcher_guide", () -> new FishingGuideItem(new Item.Properties()));

    public static final DeferredItem<Item> FISH_SPOTTER = ITEMS.register("fish_spotter", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ROD = ITEMS.register(
            "starcatcher_rod",
            () -> new StarcatcherFishingRod(
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .stacksTo(1)
                            .component(ModDataComponents.BOBBER.get(), ItemContainerContents.fromItems(List.of(ItemStack.EMPTY)))
                            .component(ModDataComponents.BAIT.get(), ItemContainerContents.fromItems(List.of(ItemStack.EMPTY))))
    );

    public static final DeferredItem<Item> STARCATCHER_TWINE = ITEMS.register("starcatcher_twine", () -> new Item(new Item.Properties()));


    public static final DeferredItem<Item> HOOK = ITEMS.register("hook", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CREEPER_BOBBER = ITEMS.register("creeper_bobber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TREASURE_BOBBER = ITEMS.register("treasure_bobber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BAIT_SAVING_BOBBER = ITEMS.register("bait_saving_bobber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIFFICULTY_BOBBER = ITEMS.register("difficulty_bobber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAST_BITING_BOBBER = ITEMS.register("fast_biting_bobber", () -> new Item(new Item.Properties()));


    public static final DeferredItem<Item> TROPHY_OF_MASTERFUL_FISHING = ITEMS.register("trophy_of_masterful_fishing", () -> new TrophyGold(new Item.Properties()));
    public static final DeferredItem<Item> TROPHY_OF_FISHING = ITEMS.register("trophy_of_fishing", () -> new TrophySilver(new Item.Properties()));
    public static final DeferredItem<Item> TROPHY_OF_PITIFUL_FISHING = ITEMS.register("trophy_of_pitiful_fishing", () -> new TrophyBronze(new Item.Properties()));

    public static final DeferredItem<Item> MISSINGNO = ITEMS.register("missingno", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WATERLOGGED_SATCHEL = ITEMS.register("waterlogged_satchel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FISH_BONES = ITEMS.register("fish_bones", () -> new Item(new Item.Properties()));


    //
    //  ,---. ,--.         ,--.
    // /  .-' `--'  ,---.  |  ,---.   ,---.   ,---.
    // |  `-, ,--. (  .-'  |  .-.  | | .-. : (  .-'
    // |  .-' |  | .-'  `) |  | |  | \   --. .-'  `)
    // `--'   `--' `----'  `--' `--'  `----' `----'
    //


    //lake
    public static final DeferredItem<Item> OBIDONTIEE = basicFish("obidontiee"); //description
    public static final DeferredItem<Item> SILVERVEIL_PERCH = basicFish("silverveil_perch"); //description
    public static final DeferredItem<Item> ELDERSCALE = basicFish("elderscale"); //description
    public static final DeferredItem<Item> DRIFTFIN = basicFish("driftfin"); //description
    public static final DeferredItem<Item> TWILIGHT_KOI = basicFish("twilight_koi"); //description
    public static final DeferredItem<Item> THUNDER_BASS = basicFish("thunder_bass"); //description
    public static final DeferredItem<Item> LIGHTNING_BASS = basicFish("lightning_bass"); //description
    public static final DeferredItem<Item> BOOT = basicFish("boot"); //description

    //swamp
    public static final DeferredItem<Item> SLUDGE_CATFISH = basicFish("sludge_catfish"); //description
    public static final DeferredItem<Item> LILY_SNAPPER = basicFish("lily_snapper"); //description
    public static final DeferredItem<Item> SAGE_CATFISH = basicFish("sage_catfish"); //description
    public static final DeferredItem<Item> MOSSY_BOOT = basicFish("mossy_boot"); //description

    //darkoak_forest
    public static final DeferredItem<Item> PALE_CARP = basicFish("pale_carp"); //description
    public static final DeferredItem<Item> PALE_PINFISH = basicFish("pale_pinfish"); //description
    public static final DeferredItem<Item> PINFISH = basicFish("pinfish"); //description

    //icy lake
    public static final DeferredItem<Item> FROSTJAW_TROUT = basicFish("frostjaw_trout"); //description
    public static final DeferredItem<Item> CRYSTALBACK_TROUT = basicFish("crystalback_trout"); //description
    public static final DeferredItem<Item> AURORA = basicFish("aurora"); //description
    public static final DeferredItem<Item> WINTERY_PIKE = basicFish("wintery_pike"); //description

    //warm lake (desert/savanna etc)
    public static final DeferredItem<Item> SANDTAIL = basicFish("sandtail"); //description
    public static final DeferredItem<Item> MIRAGE_CARP = basicFish("mirage_carp"); //description
    public static final DeferredItem<Item> SCORCHFISH = basicFish("scorchfish"); //description
    public static final DeferredItem<Item> CACTIFISH = basicFish("cactifish"); //description
    public static final DeferredItem<Item> AGAVE_BREAM = basicFish("agave_bream"); //TODO CHOSEN BY MANGO

    //mountain
    public static final DeferredItem<Item> SUNNY_STURGEON = basicFish("sunny_sturgeon"); //description
    public static final DeferredItem<Item> ROCKGILL = basicFish("rockgill"); //description
    public static final DeferredItem<Item> PEAKDWELLER = basicFish("peakdweller"); //description
    public static final DeferredItem<Item> SUN_SEEKING_CARP = basicFish("sun_seeking_carp"); //description

    //cherry grove
    public static final DeferredItem<Item> BLOSSOMFISH = basicFish("blossomfish"); //description
    public static final DeferredItem<Item> PETALDRIFT_CARP = basicFish("petaldrift_carp"); //description
    public static final DeferredItem<Item> PINK_KOI = basicFish("pink_koi"); //description
    public static final DeferredItem<Item> MORGANITE = basicFish("morganite"); //description
    public static final DeferredItem<Item> ROSE_SIAMESE_FISH = basicFish("rose_siamese_fish"); //description

    //icy mountain
    public static final DeferredItem<Item> CRYSTALBACK_STURGEON = basicFish("crystalback_sturgeon"); //description
    public static final DeferredItem<Item> ICETOOTH_STURGEON = basicFish("icetooth_sturgeon"); //description
    public static final DeferredItem<Item> BOREAL = basicFish("boreal"); //description
    public static final DeferredItem<Item> CRYSTALBACK_BOREAL = basicFish("crystalback_boreal"); //description

    //rivers
    public static final DeferredItem<Item> SILVERFIN_PIKE = basicFish("silverfin_pike"); //description
    public static final DeferredItem<Item> WILLOW_BREAM = basicFish("willow_bream"); //description
    public static final DeferredItem<Item> DRIFTING_BREAM = basicFish("drifting_bream"); //description
    public static final DeferredItem<Item> DOWNFALL_BREAM = basicFish("downfall_bream"); //description
    public static final DeferredItem<Item> HOLLOWBELLY_DARTER = basicFish("hollowbelly_darter"); //description
    public static final DeferredItem<Item> MISTBACK_CHUB = basicFish("mistback_chub"); //description
    public static final DeferredItem<Item> DRIED_SEAWEED = basicFish("dried_seaweed"); //description

    //icy river
    public static final DeferredItem<Item> FROSTGILL_CHUB = basicFish("frostgill_chub"); //description
    public static final DeferredItem<Item> CRYSTALBACK_MINNOW = basicFish("crystalback_minnow"); //description
    public static final DeferredItem<Item> AZURE_CRYSTALBACK_MINNOW = basicFish("azure_crystalback_minnow"); //description
    public static final DeferredItem<Item> BLUE_CRYSTAL_FIN = basicFish("blue_crystal_fin"); //description

    //saltwater
    public static final DeferredItem<Item> IRONJAW_HERRING = basicFish("ironjaw_herring"); //description
    public static final DeferredItem<Item> DEEPJAW_HERRING = basicFish("deepjaw_herring"); //description
    public static final DeferredItem<Item> DUSKTAIL_SNAPPER = basicFish("dusktail_snapper"); //description
    public static final DeferredItem<Item> JOEL = basicFish("joel"); //description
    public static final DeferredItem<Item> REDSCALED_TUNA = basicFish("redscaled_tuna"); //description
    public static final DeferredItem<Item> BIGEYE_TUNA = basicFish("bigeye_tuna"); //added by Tuna Feesh
    public static final DeferredItem<Item> SEA_BASS = basicFish("sea_bass"); //description
    public static final DeferredItem<Item> WATERLOGGED_BOTTLE = basicFish("waterlogged_bottle"); //description

    //beaches
    public static final DeferredItem<Item> CONCH = basicFish("conch"); //description
    public static final DeferredItem<Item> CLAM = basicFish("clam"); //description

    //mushroom islands
    public static final DeferredItem<Item> SHROOMFISH = basicFish("shroomfish"); //description
    public static final DeferredItem<Item> SPOREFISH = basicFish("sporefish"); //description

    //underground
    public static final DeferredItem<Item> GOLD_FAN = basicFish("gold_fan"); //description
    public static final DeferredItem<Item> GEODE_EEL = basicFish("geode_eel"); //description

    //caves
    public static final DeferredItem<Item> WHITEVEIL = basicFish("whiteveil"); //description
    public static final DeferredItem<Item> BLACK_EEL = basicFish("black_eel"); //description
    public static final DeferredItem<Item> AMETHYSTBACK = basicFish("amethystback"); //description
    public static final DeferredItem<Item> STONEFISH = basicFish("stonefish"); //description

    //dripstone caves
    public static final DeferredItem<Item> FOSSILIZED_ANGELFISH = basicFish("fossilized_angelfish"); //description
    public static final DeferredItem<Item> DRIPFIN = basicFish("dripfin"); //description
    public static final DeferredItem<Item> YELLOWSTONE_FISH = basicFish("yellowstone_fish"); //description

    //lush caves
    public static final DeferredItem<Item> LUSH_PIKE = basicFish("lush_pike"); //description
    public static final DeferredItem<Item> VIVID_MOSS = basicFish("vivid_moss"); //description

    //deepslate
    public static final DeferredItem<Item> GHOSTLY_PIKE = basicFish("ghostly_pike"); //description
    public static final DeferredItem<Item> AQUAMARINE_PIKE = basicFish("aquamarine_pike"); //description
    public static final DeferredItem<Item> GARNET_MACKEREL = basicFish("garnet_mackerel"); //description
    public static final DeferredItem<Item> BRIGHT_AMETHYST_SNAPPER = basicFish("bright_amethyst_snapper"); //description
    public static final DeferredItem<Item> DARK_AMETHYST_SNAPPER = basicFish("dark_amethyst_snapper"); //description
    public static final DeferredItem<Item> DEEPSLATEFISH = basicFish("deepslatefish"); //description

    //deep dark
    public static final DeferredItem<Item> SKULKFISH = basicFish("skulkfish"); //description
    public static final DeferredItem<Item> WARD = basicFish("ward"); //description
    public static final DeferredItem<Item> GLOWING_DARK = basicFish("glowing_dark"); //description

    //nether
    public static final DeferredItem<Item> EMBERGILL = netherFish("embergill"); //description
    public static final DeferredItem<Item> SCALDING_PIKE = netherFish("scalding_pike"); //description
    public static final DeferredItem<Item> CINDER_SQUID = netherFish("cinder_squid"); //description
    public static final DeferredItem<Item> LAVA_CRAB = netherFish("lava_crab"); //description
    public static final DeferredItem<Item> MAGMA_FISH = netherFish("magma_fish"); //description
    public static final DeferredItem<Item> GLOWSTONE_SEEKER = netherFish("glowstone_seeker"); //description
    public static final DeferredItem<Item> LAVA_CRAB_CLAW = netherFish("lava_crab_claw"); //description

    //the end
    public static final DeferredItem<Item> CHARFISH = basicFish("charfish"); //todo chosen by charry



    private static DeferredItem<Item> basicFish(String name)
    {
        //chat didn't force me to write this comment
        return ITEMS.register(name, () -> new FishItem(new Item.Properties().food(ModFoodProperties.BASIC_RAW_FISH)));
    }

    private static DeferredItem<Item> basicItem(String name)
    {
        //chat didn't force me to write this comment
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    private static DeferredItem<Item> netherFish(String name)
    {
        //chat didn't force me to write this comment
        return ITEMS.register(name, () -> new Item(new Item.Properties().fireResistant()));
    }


    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
