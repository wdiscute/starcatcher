package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TackleBoxScreen extends AbstractContainerScreen<TackleBoxMenu>
{
    private static final ScreenUtils.Image TEXTURE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tackle_box/tackle_box.png"), 256, 256);
    private static final ScreenUtils.Image ICONS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tackle_box/tackle_box_icons.png"), 256, 256);

    public TackleBoxScreen(TackleBoxMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        ++this.imageHeight;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }


    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY)
    {
        super.renderBackground(g);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        TEXTURE.render(g, x, y);

        if (!menu.getRod().isEmpty())
            ICONS.render(g, x, y);
    }
}
