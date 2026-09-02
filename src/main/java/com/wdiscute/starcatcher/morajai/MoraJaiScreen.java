package com.wdiscute.starcatcher.morajai;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3x2fStack;

public class MoraJaiScreen extends Screen
{

    final MoraJai.Grid grid;

    public MoraJaiScreen()
    {
        super(Component.empty());
        this.grid = new MoraJai.Grid();
    }

    @Override
    public boolean keyPressed(KeyEvent event)
    {
        InputConstants.Key mouseKey = InputConstants.getKey(event);
        if(Minecraft.getInstance().options.keyInventory.isActiveAndMatches(mouseKey))
        {
            onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        if(event.button() == 0 && grid.click(event.x() - width / 2f, event.y() - height / 2f)) return true;
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        Matrix3x2fStack pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(width / 2f, height / 2f);

        grid.render(graphics);

        pose.popMatrix();
    }
}
