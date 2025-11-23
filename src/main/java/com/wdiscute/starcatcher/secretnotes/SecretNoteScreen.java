package com.wdiscute.starcatcher.secretnotes;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.Tooltips;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SecretNoteScreen extends Screen
{
    private static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/secret_note.png");

    private final String translationKey;

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

        renderImage(guiGraphics, BACKGROUND);

        for (int i = 0; i < 20; i++)
        {
            String key = translationKey + i;
            if (I18n.exists(key))
            {
                guiGraphics.drawString(this.font, Tooltips.decodeTranslationKey(key), uiX + 140, uiY + 55 + 9 * i, 0x00000000, false);
            }
            else
            {
                break;
            }
        }
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

    public SecretNoteScreen(SecretNote.Note note)
    {
        super(Component.empty());
        this.translationKey = "gui.secret_note." + note.getSerializedName() + ".";
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
