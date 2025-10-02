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

import java.util.ArrayList;
import java.util.List;

public class ModItems
{

    public static List<DeferredItem<Item>> fishes = new ArrayList<>();
    public static List<DeferredItem<Item>> trash = new ArrayList<>();

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
    public static final DeferredItem<Item> TREASURE = ITEMS.register("treasure", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SCALDING_TREASURE = ITEMS.register("scalding_treasure", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FISH_BONES = ITEMS.register("fish_bones", () -> new Item(new Item.Properties()));


    //
    //  ,---. ,--.         ,--.
    // /  .-' `--'  ,---.  |  ,---.   ,---.   ,---.
    // |  `-, ,--. (  .-'  |  .-.  | | .-. : (  .-'
    // |  .-' |  | .-'  `) |  | |  | \   --. .-'  `)
    // `--'   `--' `----'  `--' `--'  `----' `----'
    //


    //lake
    public static final DeferredItem<Item> OBIDONTIEE = fish("obidontiee"); //description
    public static final DeferredItem<Item> SILVERVEIL_PERCH = fish("silverveil_perch"); //description
    public static final DeferredItem<Item> ELDERSCALE = fish("elderscale"); //description
    public static final DeferredItem<Item> DRIFTFIN = fish("driftfin"); //description
    public static final DeferredItem<Item> TWILIGHT_KOI = fish("twilight_koi"); //description
    public static final DeferredItem<Item> THUNDER_BASS = fish("thunder_bass"); //description
    public static final DeferredItem<Item> LIGHTNING_BASS = fish("lightning_bass"); //description
    public static final DeferredItem<Item> BOOT = trash("boot"); //description

    //swamp
    public static final DeferredItem<Item> SLUDGE_CATFISH = fish("sludge_catfish"); //description
    public static final DeferredItem<Item> LILY_SNAPPER = fish("lily_snapper"); //description
    public static final DeferredItem<Item> SAGE_CATFISH = fish("sage_catfish"); //description
    public static final DeferredItem<Item> MOSSY_BOOT = trash("mossy_boot"); //description

    //darkoak_forest
    public static final DeferredItem<Item> PALE_CARP = fish("pale_carp"); //description
    public static final DeferredItem<Item> PALE_PINFISH = fish("pale_pinfish"); //description
    public static final DeferredItem<Item> PINFISH = fish("pinfish"); //description

    //icy lake
    public static final DeferredItem<Item> FROSTJAW_TROUT = fish("frostjaw_trout"); //description
    public static final DeferredItem<Item> CRYSTALBACK_TROUT = fish("crystalback_trout"); //description
    public static final DeferredItem<Item> AURORA = fish("aurora"); //description
    public static final DeferredItem<Item> WINTERY_PIKE = fish("wintery_pike"); //description

    //warm lake (desert/savanna etc)
    public static final DeferredItem<Item> SANDTAIL = fish("sandtail"); //description
    public static final DeferredItem<Item> MIRAGE_CARP = fish("mirage_carp"); //description
    public static final DeferredItem<Item> SCORCHFISH = fish("scorchfish"); //description
    public static final DeferredItem<Item> CACTIFISH = fish("cactifish"); //description
    public static final DeferredItem<Item> AGAVE_BREAM = fish("agave_bream"); //TODO CHOSEN BY MANGO

    //mountain
    public static final DeferredItem<Item> SUNNY_STURGEON = fish("sunny_sturgeon"); //description
    public static final DeferredItem<Item> ROCKGILL = fish("rockgill"); //description
    public static final DeferredItem<Item> PEAKDWELLER = fish("peakdweller"); //description
    public static final DeferredItem<Item> SUN_SEEKING_CARP = fish("sun_seeking_carp"); //description

    //cherry grove
    public static final DeferredItem<Item> BLOSSOMFISH = fish("blossomfish"); //description
    public static final DeferredItem<Item> PETALDRIFT_CARP = fish("petaldrift_carp"); //description
    public static final DeferredItem<Item> PINK_KOI = fish("pink_koi"); //description
    public static final DeferredItem<Item> MORGANITE = fish("morganite"); //description
    public static final DeferredItem<Item> ROSE_SIAMESE_FISH = fish("rose_siamese_fish"); //description

    //icy mountain
    public static final DeferredItem<Item> CRYSTALBACK_STURGEON = fish("crystalback_sturgeon"); //description
    public static final DeferredItem<Item> ICETOOTH_STURGEON = fish("icetooth_sturgeon"); //description
    public static final DeferredItem<Item> BOREAL = fish("boreal"); //description
    public static final DeferredItem<Item> CRYSTALBACK_BOREAL = fish("crystalback_boreal"); //description

    //rivers
    public static final DeferredItem<Item> SILVERFIN_PIKE = fish("silverfin_pike"); //description
    public static final DeferredItem<Item> WILLOW_BREAM = fish("willow_bream"); //description
    public static final DeferredItem<Item> DRIFTING_BREAM = fish("drifting_bream"); //description
    public static final DeferredItem<Item> DOWNFALL_BREAM = fish("downfall_bream"); //description
    public static final DeferredItem<Item> HOLLOWBELLY_DARTER = fish("hollowbelly_darter"); //description
    public static final DeferredItem<Item> MISTBACK_CHUB = fish("mistback_chub"); //description
    public static final DeferredItem<Item> DRIED_SEAWEED = trash("dried_seaweed"); //description

    //icy river
    public static final DeferredItem<Item> FROSTGILL_CHUB = fish("frostgill_chub"); //description
    public static final DeferredItem<Item> CRYSTALBACK_MINNOW = fish("crystalback_minnow"); //description
    public static final DeferredItem<Item> AZURE_CRYSTALBACK_MINNOW = fish("azure_crystalback_minnow"); //description
    public static final DeferredItem<Item> BLUE_CRYSTAL_FIN = fish("blue_crystal_fin"); //description

    //saltwater
    public static final DeferredItem<Item> IRONJAW_HERRING = fish("ironjaw_herring"); //description
    public static final DeferredItem<Item> DEEPJAW_HERRING = fish("deepjaw_herring"); //description
    public static final DeferredItem<Item> DUSKTAIL_SNAPPER = fish("dusktail_snapper"); //description
    public static final DeferredItem<Item> JOEL = fish("joel"); //description
    public static final DeferredItem<Item> REDSCALED_TUNA = fish("redscaled_tuna"); //description
    public static final DeferredItem<Item> BIGEYE_TUNA = fish("bigeye_tuna"); //added by Tuna Feesh
    public static final DeferredItem<Item> SEA_BASS = fish("sea_bass"); //description
    public static final DeferredItem<Item> WATERLOGGED_BOTTLE = trash("waterlogged_bottle"); //description

    //beaches
    public static final DeferredItem<Item> CONCH = trash("conch"); //description
    public static final DeferredItem<Item> CLAM = trash("clam"); //description

    //mushroom islands
    public static final DeferredItem<Item> SHROOMFISH = fish("shroomfish"); //description
    public static final DeferredItem<Item> SPOREFISH = fish("sporefish"); //description

    //underground
    public static final DeferredItem<Item> GOLD_FAN = fish("gold_fan"); //description
    public static final DeferredItem<Item> GEODE_EEL = fish("geode_eel"); //description

    //caves
    public static final DeferredItem<Item> WHITEVEIL = fish("whiteveil"); //description
    public static final DeferredItem<Item> BLACK_EEL = fish("black_eel"); //description
    public static final DeferredItem<Item> AMETHYSTBACK = fish("amethystback"); //description
    public static final DeferredItem<Item> STONEFISH = fish("stonefish"); //description

    //dripstone caves
    public static final DeferredItem<Item> FOSSILIZED_ANGELFISH = fish("fossilized_angelfish"); //description
    public static final DeferredItem<Item> DRIPFIN = fish("dripfin"); //description
    public static final DeferredItem<Item> YELLOWSTONE_FISH = fish("yellowstone_fish"); //description

    //lush caves
    public static final DeferredItem<Item> LUSH_PIKE = fish("lush_pike"); //description
    public static final DeferredItem<Item> VIVID_MOSS = fish("vivid_moss"); //description

    //deepslate
    public static final DeferredItem<Item> GHOSTLY_PIKE = fish("ghostly_pike"); //description
    public static final DeferredItem<Item> AQUAMARINE_PIKE = fish("aquamarine_pike"); //description
    public static final DeferredItem<Item> GARNET_MACKEREL = fish("garnet_mackerel"); //description
    public static final DeferredItem<Item> BRIGHT_AMETHYST_SNAPPER = fish("bright_amethyst_snapper"); //description
    public static final DeferredItem<Item> DARK_AMETHYST_SNAPPER = fish("dark_amethyst_snapper"); //description
    public static final DeferredItem<Item> DEEPSLATEFISH = fish("deepslatefish"); //description

    //deep dark
    public static final DeferredItem<Item> SKULKFISH = fish("skulkfish"); //description
    public static final DeferredItem<Item> WARD = fish("ward"); //description
    public static final DeferredItem<Item> GLOWING_DARK = fish("glowing_dark"); //description

    //nether
    public static final DeferredItem<Item> EMBERGILL = fireResistantFish("embergill"); //description
    public static final DeferredItem<Item> SCALDING_PIKE = fireResistantFish("scalding_pike"); //description
    public static final DeferredItem<Item> CINDER_SQUID = fireResistantFish("cinder_squid"); //description
    public static final DeferredItem<Item> LAVA_CRAB = fireResistantFish("lava_crab"); //description
    public static final DeferredItem<Item> MAGMA_FISH = fireResistantFish("magma_fish"); //description
    public static final DeferredItem<Item> GLOWSTONE_SEEKER = fireResistantFish("glowstone_seeker"); //description
    public static final DeferredItem<Item> LAVA_CRAB_CLAW = fireResistantTrash("lava_crab_claw"); //description

    //the end
    public static final DeferredItem<Item> CHARFISH = fish("charfish"); //todo chosen by charry



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


    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
