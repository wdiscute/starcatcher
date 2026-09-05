package com.wdiscute.starcatcher.guide;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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

    @Override
    public void onClose()
    {
        super.onClose();
        if (screen != null)
            Minecraft.getInstance().setScreen(screen);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(g);
        super.render(g, mouseX, mouseY, partialTick);

        BACKGROUND.render(g, uiX, uiY);

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
