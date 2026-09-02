package com.wdiscute.starcatcher.messageinabottle.message;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
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
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float a)
    {
        super.extractRenderState(g, mouseX, mouseY, a);

        ScreenUtils.Image image = new ScreenUtils.Image(message.background(), 512, 256);

        image.render(g, uiX, uiY);

        //render main text
        List<String> text = message.text();
        for (int i = 0; i < text.size(); i++)
            ScreenUtils.text(g, this.font,
                    Tooltips.resolveTagsToComponentFromTranslationKey(text.get(i)), uiX + 140, uiY + 55 + 9 * i,
                    SCColors.GUIDE_TEXT_DARK, false);

        //render name
        ScreenUtils.text(g, this.font,
                Tooltips.resolveTagsToComponentFromTranslationKey(message.senderDisplayName()),
                uiX + 255, uiY + 208, SCColors.GUIDE_TEXT_DARK, false);
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
