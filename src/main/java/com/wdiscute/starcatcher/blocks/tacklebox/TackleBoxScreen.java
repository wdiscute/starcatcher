package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TackleBoxScreen extends AbstractContainerScreen<TackleBoxMenu>
{
    private static final Identifier TEXTURE = Starcatcher.rl("textures/gui/tackle_box/tackle_box.png");
    private static final Identifier ICONS = Starcatcher.rl("textures/gui/tackle_box/tackle_box_icons.png");

    public TackleBoxScreen(TackleBoxMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
    }

    @Override
    protected void extractMenuBackground(GuiGraphicsExtractor graphics)
    {
        super.extractMenuBackground(graphics);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI, TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        if (!menu.getRod().isEmpty())
        {
            graphics.blit(ICONS, x, y, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        }
    }
}
