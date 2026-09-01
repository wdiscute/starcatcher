package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RodSlotTooltipRenderer implements ClientTooltipComponent
{
    public static final ScreenUtils.Image TEXTURE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/rod_tooltip.png"), 56, 19);
    public static final ScreenUtils.Image BOBBER = new ScreenUtils.Image(Starcatcher.rl("textures/item/background/bobber_white.png"), 16, 16);
    public static final ScreenUtils.Image BAIT = new ScreenUtils.Image(Starcatcher.rl("textures/item/background/bait_white.png"), 16, 16);
    public static final ScreenUtils.Image HOOK = new ScreenUtils.Image(Starcatcher.rl("textures/item/background/hook_white.png"), 16, 16);

    ItemStack bobber;
    ItemStack bait;
    ItemStack hook;

    int width;

    public RodSlotTooltipRenderer(StarcatcherFishingRodItem.RodSlotTooltip tooltip)
    {
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
    public void renderImage(Font font, int x, int y, GuiGraphics g)
    {
        TEXTURE.render(g, x, y);

        if (bobber.isEmpty())
            BOBBER.render(g, x + 2, y + 1);
        else
        {
            ScreenUtils.item(g, bobber, x + 2, y + 1);
            g.renderItemDecorations(font, bobber, x + 2 + 2, y + 1);
        }

        if (bait.isEmpty())
            BAIT.render(g, x + 18 + 2, y + 1);
        else
        {
            ScreenUtils.item(g, bait, x + 18 + 2, y + 1);
            g.renderItemDecorations(font, bait, x + 18 + 2, y + 1);
        }

        if (hook.isEmpty())
            HOOK.render(g, x + 18 + 18 + 2, y + 1);
        else
        {
            ScreenUtils.item(g, hook, x + 18 + 18 + 2, y + 1);
            g.renderItemDecorations(font, hook, x + 18 + 18 + 2, y + 1);
        }
    }
}
