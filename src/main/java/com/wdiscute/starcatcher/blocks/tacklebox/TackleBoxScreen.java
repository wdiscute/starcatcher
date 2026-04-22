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
        super(menu, playerInventory, Component.empty());
        inventoryLabelY = 2314234;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        if (!menu.getRod().isEmpty())
        {
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        }
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }
}
