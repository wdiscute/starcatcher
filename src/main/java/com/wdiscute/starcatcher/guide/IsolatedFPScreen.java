package com.wdiscute.starcatcher.guide;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class IsolatedFPScreen extends Screen
{
    protected static final ScreenUtils.Image BACKGROUND = new ScreenUtils.Image(Starcatcher.rl("textures/gui/jemi/entry.png"), 200, 200);

    protected final FishProperties fp;
    protected int uiX;
    protected int uiY;
    private Screen screen;

    public IsolatedFPScreen(FishProperties fishProperties, Screen screen)
    {
        super(Component.empty());
        this.fp = fishProperties;
        this.screen = screen;
    }

    @Override
    protected void init()
    {
        super.init();
        uiX = (width - 200) / 2;
        uiY = (height - 200) / 2;
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
            Minecraft.getInstance().setScreenAndShow(screen);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float a)
    {
        super.extractRenderState(g, mouseX, mouseY, a);

        FishingGuideScreen.renderFishEntryPage(
                g,
                fp,
                fp.catchInfo().fish().toStack(),
                FishCaughtCounter.get(Minecraft.getInstance().player, fp.toLoc(Minecraft.getInstance().level)),
                uiX + 31,
                uiY - 25,
                mouseX,
                mouseY
        );
    }
}
