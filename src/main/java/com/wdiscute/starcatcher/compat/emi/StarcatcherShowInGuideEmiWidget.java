package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.starcatcher.fish.FishProperties;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class StarcatcherShowInGuideEmiWidget extends Widget
{
    private final FishProperties fp;
    private final int x;
    private final int y;

    public StarcatcherShowInGuideEmiWidget(int x, int y, FishProperties fp)
    {
        this.fp = fp;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button)
    {
        if(mouseX > x && mouseX < x + 19 && mouseY > y && mouseY < y + 19)
        {
            Minecraft.getInstance().setScreen(new IsolatedEmiFPScreen(fp, Minecraft.getInstance().screen));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public Bounds getBounds()
    {
        return new Bounds(0, 0, 200, 20);
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta)
    {
        //render tooltip
        if(mouseX > x && mouseX < x + 19 && mouseY > y && mouseY < y + 19)
            draw.renderTooltip(Minecraft.getInstance().font, Component.translatable("emi.starcatcher.open_as_guide_entry"), mouseX, mouseY);
    }
}
