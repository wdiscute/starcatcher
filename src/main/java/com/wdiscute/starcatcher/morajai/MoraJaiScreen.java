package com.wdiscute.starcatcher.morajai;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MoraJaiScreen extends Screen
{

    final MoraJai.Grid grid;

    public MoraJaiScreen()
    {
        super(Component.empty());
        this.grid = new MoraJai.Grid();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
        if(Minecraft.getInstance().options.keyInventory.isActiveAndMatches(mouseKey))
        {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if(button == 0 && grid.click(mouseX - width / 2f, mouseY - height / 2f)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(width / 2f, height / 2f, 0);

        grid.render(guiGraphics);

        pose.popPose();
    }
}
