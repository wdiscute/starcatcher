package com.wdiscute.starcatcher.fishingbob;

import com.wdiscute.starcatcher.ModDataComponents;
import com.wdiscute.starcatcher.ModFoodProperties;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideItem;
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

    public static final DeferredItem<Item> FISHING_GUIDE = ITEMS.register("fishing_guide", () -> new FishingGuideItem(new Item.Properties()));
    public static final DeferredItem<Item> FISH_BONES = ITEMS.register("fish_bones", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STARCATCHER_TWINE = ITEMS.register("starcatcher_twine", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STARCATCHER_FISHING_ROD = ITEMS.register(
            "starcatcher_fishing_rod",
            () -> new StarcatcherFishingRod(
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .stacksTo(1)
                            .component(ModDataComponents.BOBBER.get(), ItemContainerContents.fromItems(List.of(ItemStack.EMPTY)))
                            .component(ModDataComponents.BAIT.get(), ItemContainerContents.fromItems(List.of(ItemStack.EMPTY))))
    );

    public static final DeferredItem<Item> CREEPER_BOBBER = ITEMS.register("creeper_bobber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TREASURE_BOBBER = ITEMS.register("treasure_bobber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BAIT_SAVING_BOBBER = ITEMS.register("bait_saving_bobber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIFFICULTY_BOBBER = ITEMS.register("difficulty_bobber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FAST_BITING_BOBBER = ITEMS.register("fast_biting_bobber", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FISH_SPOTTER = ITEMS.register("fish_spotter", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WATERLOGGED_SATCHEL = ITEMS.register("waterlogged_satchel", () -> new Item(new Item.Properties()));


    //
    //  ,---. ,--.         ,--.
    // /  .-' `--'  ,---.  |  ,---.   ,---.   ,---.
    // |  `-, ,--. (  .-'  |  .-.  | | .-. : (  .-'
    // |  .-' |  | .-'  `) |  | |  | \   --. .-'  `)
    // `--'   `--' `----'  `--' `--'  `----' `----'
    //

    //overworld

    //lake
    public static final DeferredItem<Item> OBIDONTIEE = basicFish("obidontiee"); //description

    //rivers
    public static final DeferredItem<Item> SILVERFIN_PIKE = basicFish("silverfin_pike"); //description
    public static final DeferredItem<Item> WILLOW_BREAM = basicFish("willow_bream"); //description
    public static final DeferredItem<Item> DRIFTING_BREAM = basicFish("drifting_bream"); //description
    public static final DeferredItem<Item> DOWNFALL_BREAM = basicFish("downfall_bream"); //description
    public static final DeferredItem<Item> HOLLOWBELLY_DARTER = basicFish("hollowbelly_darter"); //description
    public static final DeferredItem<Item> MISTBACK_CHUB = basicFish("mistback_chub"); //description

    //saltwater
    public static final DeferredItem<Item> IRONJAW_HERRING = basicFish("ironjaw_herring"); //description
    public static final DeferredItem<Item> DEEPJAW_HERRING = basicFish("deepjaw_herring"); //description
    public static final DeferredItem<Item> DUSKTAIL_SNAPPER = basicFish("dusktail_snapper"); //description

    //icy
    public static final DeferredItem<Item> FROSTGILL_CHUB = basicFish("frostgill_chub"); //description
    public static final DeferredItem<Item> CRYSTALBACK_MINNOW = basicFish("crystalback_minnow"); //description
    public static final DeferredItem<Item> AZURE_CRYSTALBACK_MINNOW = basicFish("azure_crystalback_minnow"); //description

    //nether
    public static final DeferredItem<Item> EMBERGILL = basicFish("embergill"); //description

    //the end



    private static DeferredItem<Item> basicFish(String name)
    {
        return ITEMS.register(name, () -> new Item(new Item.Properties().food(ModFoodProperties.BASIC_RAW_FISH)));
    }


    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
