package com.wdiscute.starcatcher.secretnotes;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class SecretNoteScreen extends Screen
{
    private final Identifier background;

    private final String translationKey;
    private final Screen screen;

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
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        renderImage(guiGraphics, background);

        for (int i = 0; i < 20; i++)
        {
            String key = translationKey + i;
            if (I18n.exists(key))
            {
                guiGraphics.text(this.font, Component.translatable(key), uiX + 140, uiY + 55 + 9 * i, 0x635040, false);
            }
            else
            {
                break;
            }
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event)
    {
        InputConstants.Key key = InputConstants.getKey(event);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(key))
        {
            this.onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void onClose()
    {
        super.onClose();
        if (screen != null)
            Minecraft.getInstance().setScreen(screen);
    }

    public SecretNoteScreen(SecretNote.Note note, Screen screen)
    {
        super(Component.empty());
        this.screen = screen;
        this.translationKey = "gui.secret_note." + note.getSerializedName() + ".";
        this.background = Starcatcher.rl("textures/gui/message/" + note.getTexture() + ".png");
    }

    private void renderImage(GuiGraphicsExtractor guiGraphics, Identifier rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, 512, 256, 512, 256);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
