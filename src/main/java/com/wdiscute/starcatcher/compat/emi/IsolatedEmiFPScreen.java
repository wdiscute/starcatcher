package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.guide.IsolatedFPScreen;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.fish.FishProperties;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.blit(BACKGROUND, uiX, uiY, 0, 0, 200, 200, 200, 200);

        FishingGuideScreen.renderFishEntryPage(
                guiGraphics,
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
