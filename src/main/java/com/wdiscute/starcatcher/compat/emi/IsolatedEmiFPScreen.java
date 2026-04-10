package com.wdiscute.starcatcher.compat.emi;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.guide.IsolatedFPScreen;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.registry.FishProperties;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class IsolatedEmiFPScreen extends IsolatedFPScreen
{
    private static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/emi/emi_entry.png");

    private final EmiRecipe emiRecipe;

    public IsolatedEmiFPScreen(FishProperties fishProperties, EmiRecipe emiRecipe)
    {
        super(fishProperties, null);
        this.emiRecipe = emiRecipe;
    }

    @Override
    public void onClose()
    {
        super.onClose();
        EmiApi.displayRecipe(emiRecipe);
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
                new ItemStack(fp.catchInfo().fish().value()),
                FishCaughtCounter.get(Minecraft.getInstance().player, fp),
                uiX + 31,
                uiY - 25,
                mouseX,
                mouseY
        );
    }
}
