package com.wdiscute.starcatcher.secretnotes;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;

public class MessageScreen extends Screen
{
    private static final ResourceLocation BACKGROUND_OVERWORLD = Starcatcher.rl("textures/gui/message/message_overworld.png");
    private static final ResourceLocation BACKGROUND_NETHER = Starcatcher.rl("textures/gui/message/message_nether.png");
    private static final ResourceLocation BACKGROUND_END = Starcatcher.rl("textures/gui/message/message_end.png");

    private final LetterItem.Message message;
    private final ResourceLocation background;

    public MessageScreen(LetterItem.Message message)
    {
        super(Component.empty());
        this.message = message;

        if (message.dimension().equals(Level.NETHER.location()))
            background = BACKGROUND_NETHER;
        else if (message.dimension().equals(Level.END.location())) background = BACKGROUND_END;
        else background = BACKGROUND_OVERWORLD;
    }

    int uiX;
    int uiY;

    @Override
    protected void init()
    {
        super.init();
        uiX = (width - 512) / 2;
        uiY = (height - 256) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderImage(guiGraphics, background);

        //render main text
        List<String> text = message.text();
        for (int i = 0; i < text.size(); i++)
        {
            guiGraphics.drawString(this.font, Component.translatable(text.get(i)), uiX + 140, uiY + 55 + 9 * i, 0x635040, false);
        }

        //render name
        guiGraphics.drawString(this.font, Component.translatable(message.senderDisplayName()), uiX + 255, uiY + 208, 0x635040, false);

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(key))
        {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, 512, 256, 512, 256);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
