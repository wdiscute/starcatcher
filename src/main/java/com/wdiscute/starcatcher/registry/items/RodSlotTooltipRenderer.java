package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RodSlotTooltipRenderer implements ClientTooltipComponent
{
    public static final ResourceLocation TEXTURE = Starcatcher.rl("textures/gui/rod_tooltip.png");
    public static final ResourceLocation BOBBER = Starcatcher.rl("textures/item/background/bobber_white.png");
    public static final ResourceLocation BAIT = Starcatcher.rl("textures/item/background/bait_white.png");
    public static final ResourceLocation HOOK = Starcatcher.rl("textures/item/background/hook_white.png");

    ItemStack bobber;
    ItemStack bait;
    ItemStack hook;

    int width;

    public StarcatcherFishingRodItem.RodSlotTooltip tooltip;

    public RodSlotTooltipRenderer(StarcatcherFishingRodItem.RodSlotTooltip tooltip)
    {
        this.tooltip = tooltip;
        ItemStack rod = tooltip.rod();
        bobber = SCDataComponents.getOrDefault(rod, SCDataComponents.BOBBER, MaybeStack.EMPTY).toStack();
        bait = SCDataComponents.getOrDefault(tooltip.rod(), SCDataComponents.BAIT, MaybeStack.EMPTY).toStack();
        hook = SCDataComponents.getOrDefault(tooltip.rod(), SCDataComponents.HOOK, MaybeStack.EMPTY).toStack();
    }

    @Override
    public int getHeight()
    {
        return 21;
    }

    @Override
    public int getWidth(Font font)
    {
        return Math.max(56, width);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics)
    {
        //todo render modifiers if holding shift
        guiGraphics.blit(TEXTURE, x, y, 0, 0, 56, 19, 56, 19);


        if (bobber.isEmpty())
            guiGraphics.blit(BOBBER, x + 2, y + 1, 0, 0, 16, 16, 16, 16);
        else
        {
            guiGraphics.renderItem(bobber, x + 2, y + 1);
            guiGraphics.renderItemDecorations(font, bobber, x + 2 + 2, y + 1);
        }

        if (bait.isEmpty())
            guiGraphics.blit(BAIT, x + 18 + 2, y + 1, 0, 0, 16, 16, 16, 16);
        else
        {
            guiGraphics.renderItem(bait, x + 18 + 2, y + 1);
            guiGraphics.renderItemDecorations(font, bait, x + 18 + 2, y + 1);
        }

        if (hook.isEmpty())
            guiGraphics.blit(HOOK, x + 18 + 18 + 2, y + 1, 0, 0, 16, 16, 16, 16);
        else
        {
            guiGraphics.renderItem(hook, x + 18 + 18 + 2, y + 1);
            guiGraphics.renderItemDecorations(font, hook, x + 18 + 18 + 2, y + 1);
        }
    }
}
