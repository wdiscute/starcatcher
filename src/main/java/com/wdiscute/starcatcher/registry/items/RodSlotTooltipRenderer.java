package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.registry.items.rod.StarcatcherFishingRodItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class RodSlotTooltipRenderer implements ClientTooltipComponent
{
    public static final Identifier TEXTURE = Starcatcher.rl("textures/gui/rod_tooltip.png");
    public static final Identifier BOBBER = Starcatcher.rl("textures/item/background/bobber_white.png");
    public static final Identifier BAIT = Starcatcher.rl("textures/item/background/bait_white.png");
    public static final Identifier HOOK = Starcatcher.rl("textures/item/background/hook_white.png");

    ItemStack bobber;
    ItemStack bait;
    ItemStack hook;

    int width;

    public StarcatcherFishingRodItem.RodSlotTooltip tooltip;

    public RodSlotTooltipRenderer(StarcatcherFishingRodItem.RodSlotTooltip tooltip)
    {
        this.tooltip = tooltip;
        ItemStack rod = tooltip.rod();
        bobber = SCDataComponents.getOrDefault(rod, SCDataComponents.BOBBER, SingleStackContainer.empty()).create();
        bait = SCDataComponents.getOrDefault(tooltip.rod(), SCDataComponents.BAIT, SingleStackContainer.empty()).create();
        hook = SCDataComponents.getOrDefault(tooltip.rod(), SCDataComponents.HOOK, SingleStackContainer.empty()).create();
    }

    @Override
    public int getHeight(Font font)
    {
        return 21;
    }

    @Override
    public int getWidth(Font font)
    {
        return Math.max(56, width);
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor guiGraphics)
    {
        ClientTooltipComponent.super.extractImage(font, x, y, w, h, guiGraphics);

        //todo render modifiers if holding shift
        guiGraphics.blit(TEXTURE, x, y, 0, 0, 56, 19, 56, 19);


        if (bobber.isEmpty())
            guiGraphics.blit(BOBBER, x + 2, y + 1, 0, 0, 16, 16, 16, 16);
        else
            guiGraphics.item(bobber, x + 2, y + 1);

        if (bait.isEmpty())
            guiGraphics.blit(BAIT, x + 18 + 2, y + 1, 0, 0, 16, 16, 16, 16);
        else
            guiGraphics.item(bait, x + 18 + 2, y + 1);

        if (hook.isEmpty())
            guiGraphics.blit(HOOK, x + 18 + 18 + 2, y + 1, 0, 0, 16, 16, 16, 16);
        else
            guiGraphics.item(hook, x + 18 + 18 + 2, y + 1);
    }
}
