package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class FishCaughtToast implements Toast
{
    private static final Identifier BACKGROUND_SPRITE = Starcatcher.rl("toast/fish_caught");
    private final Component title;
    private final String description;
    private final ItemStack is;
    private FishProperties.Rarity rarity;

    public FishCaughtToast(FishProperties fp)
    {
        this.is = new ItemStack(fp.catchInfo().fish());
        this.title = Component.translatable("gui.starcatcher.toast.fish_caught");
        this.description = is.getHoverName().getString();
        this.rarity = fp.rarity();
    }

    @Override
    public int width()
    {
        return 164;
    }

    @Override
    public int height()
    {
        return 51;
    }

    Visibility v = Visibility.HIDE;

    @Override
    public Visibility getWantedVisibility()
    {
        return v;
    }

    @Override
    public void update(ToastManager manager, long fullyVisibleForMs)
    {
        if (fullyVisibleForMs < 10000)
            v = Visibility.SHOW;
        else
            v = Visibility.HIDE;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, Font font, long fullyVisibleForMs)
    {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height());

        guiGraphics.item(is, 6, 29);

        guiGraphics.text(Minecraft.getInstance().font, this.title, 40, 13, 0x635040, false);

        Component comp = Component.literal("<sctoast>" + rarity.wrapWithRarityMarkdownAsString(description) + "</sctoast>");

        guiGraphics.text(Minecraft.getInstance().font, comp, 40, 22, 0x635040, false);
    }

}
