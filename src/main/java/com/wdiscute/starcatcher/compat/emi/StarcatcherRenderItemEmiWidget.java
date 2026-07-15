package com.wdiscute.starcatcher.compat.emi;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class StarcatcherRenderItemEmiWidget extends Widget
{
    private final ItemStack is;
    private final int x;
    private final int y;

    public StarcatcherRenderItemEmiWidget(int x, int y, ItemStack is)
    {
        this.x = x;
        this.y = y;
        this.is = is;
    }

    @Override
    public Bounds getBounds()
    {
        return new Bounds(0, 0, 200, 20);
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta)
    {
        draw.renderItem(is, x, y);
    }
}
