package com.wdiscute.starcatcher.minigame;

import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.ModDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class FakeStarcatcherMinigameScreen extends FishingMinigameScreen
{

    public final MinigameOnCloseFunction onCloseFunction;

    @Override
    public void tick()
    {
        if (completionSmooth > 75)
        {
            //if completed treasure minigame, or is a perfect catch with the mossy hook
            boolean awardTreasure = treasureProgress > 100 || (perfectCatch && hook.is(ModItems.MOSSY_HOOK));
            onCloseFunction.onClose(this.itemBeingFished.getItem(), this.fp, tickCount, awardTreasure, perfectCatch, consecutiveHits);

            Minecraft.getInstance().options.guiScale().set(previousGuiScale);
            this.minecraft.popGuiLayer();
        }

        super.tick();
    }

    @Override
    public void onClose()
    {
        this.onCloseFunction.onClose(this.itemBeingFished.getItem(), this.fp, this.tickCount, false, false, this.consecutiveHits);
        Minecraft.getInstance().options.guiScale().set(previousGuiScale);
        this.minecraft.popGuiLayer();
    }

    public FakeStarcatcherMinigameScreen(FishProperties fp, ItemStack rod, MinigameOnCloseFunction onCloseFunction)
    {
        super(fp, rod);
        this.onCloseFunction = onCloseFunction;
    }

    public FakeStarcatcherMinigameScreen(FishProperties fishProperties, ItemStack bobber, ItemStack bait, ItemStack hook, MinigameOnCloseFunction onCloseFunction)
    {
        this(fishProperties, makeRod(bobber, bait, hook), onCloseFunction);
    }

    private static ItemStack makeRod(ItemStack bobber, ItemStack bait, ItemStack hook)
    {
        ItemStack rod = new ItemStack(ModItems.ROD.get());

        rod.set(ModDataComponents.BOBBER, new SingleStackContainer(bobber));
        rod.set(ModDataComponents.BAIT, new SingleStackContainer(bait));
        rod.set(ModDataComponents.HOOK, new SingleStackContainer(hook));
        return rod;
    }
}
