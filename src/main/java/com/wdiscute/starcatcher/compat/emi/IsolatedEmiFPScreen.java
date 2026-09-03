package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.guide.IsolatedFPScreen;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class IsolatedEmiFPScreen extends IsolatedFPScreen
{
    private final Screen screen;

    public IsolatedEmiFPScreen(FishProperties fishProperties, Screen screen)
    {
        super(fishProperties, null);
        this.screen = screen;
    }

    @Override
    public void onClose()
    {
        super.onClose();
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
        ScreenUtils.Tooltip.render(g,font, mouseX, mouseY);
    }
}
