package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.IsolatedFPScreen;
import com.wdiscute.starcatcher.registry.FishProperties;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class StarcatcherShowInGuideEmiWidget extends Widget
{
    private static final ResourceLocation ICON = Starcatcher.rl("textures/gui/emi/emi_guide_icon.png");
    private final FishProperties fp;
    private final int x;
    private final int y;
    private final EmiRecipe emiRecipe;

    public StarcatcherShowInGuideEmiWidget(int x, int y, FishProperties fp, EmiRecipe emiRecipe)
    {
        this.fp = fp;
        this.x = x;
        this.y = y;
        this.emiRecipe = emiRecipe;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button)
    {
        if(mouseX > x && mouseX < x + 19 && mouseY > y && mouseY < y + 19)
        {
            Minecraft.getInstance().setScreen(new IsolatedEmiFPScreen(fp, emiRecipe));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public Bounds getBounds()
    {
        return new Bounds(0, 0, 100, 20);
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta)
    {
        draw.blit(ICON, x, y, 0, 0, 20, 20, 20, 20);
        if(mouseX > x && mouseX < x + 19 && mouseY > y && mouseY < y + 19)
        {
            draw.renderTooltip(Minecraft.getInstance().font, Component.translatable("emi.starcatcher.open_as_guide_entry"), mouseX, mouseY);
        }
    }
}
