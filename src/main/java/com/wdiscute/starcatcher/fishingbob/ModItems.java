package com.wdiscute.starcatcher.fishingbob;

import com.wdiscute.starcatcher.ModDataComponents;
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


    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
