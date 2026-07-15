package com.wdiscute.starcatcher.messageinabottle.message;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.libtooltips.Tooltips;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.List;

public class MessageScreen extends Screen
{
    private final Message message;
    private final Screen screenToReturnTo;

    public MessageScreen(Message message)
    {
        super(Component.empty());
        this.message = message;
        this.screenToReturnTo = null;
    }

    public MessageScreen(Message message, Screen screenToReturnTo)
    {
        super(Component.empty());
        this.message = message;
        this.screenToReturnTo = screenToReturnTo;
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
    }

    public static void openMessageScreen(Message message)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        Minecraft.getInstance().setScreen(new MessageScreen(message));
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

        renderImage(guiGraphics, message.background());

        //render main text
        List<String> text = message.text();
        for (int i = 0; i < text.size(); i++)
        {
            guiGraphics.drawString(this.font, Tooltips.resolveTagsToComponentFromTranslationKey(text.get(i)), uiX + 140, uiY + 55 + 9 * i, 0x635040, false);
        }

        //render name
        guiGraphics.drawString(this.font, Tooltips.resolveTagsToComponentFromTranslationKey(message.senderDisplayName()), uiX + 255, uiY + 208, 0x635040, false);

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
    public void onClose()
    {
        if(screenToReturnTo != null)
        {
            Minecraft.getInstance().setScreen(screenToReturnTo);
            Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        }
        else
            super.onClose();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
