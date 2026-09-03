package com.wdiscute.starcatcher.compat.jei;

import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.guide.IsolatedFPScreen;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

public class IsolatedJeiFPScreen extends IsolatedFPScreen
{
    private final Screen screen;

    public IsolatedJeiFPScreen(StarcatcherJeiFPRecipe.Recipe recipe, Screen screen)
    {
        super(recipe.fp(), null);
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
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float a)
    {
        super.extractRenderState(g, mouseX, mouseY, a);

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
        ScreenUtils.Tooltip.render(g, font, mouseX, mouseY);
    }
}
