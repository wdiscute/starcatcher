package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FishCaughtToast implements Toast
{
    private static final ResourceLocation BACKGROUND_SPRITE = Starcatcher.rl("toast/fish_caught");
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

    public Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible)
    {
        guiGraphics.blit(BACKGROUND_SPRITE, 0, 0, 0, 0, width(), height());

        guiGraphics.renderItem(is, 6, 29);

        guiGraphics.drawString(toastComponent.getMinecraft().font, this.title, 40, 13, 0x635040, false);

        Component comp = Component.literal("<sctoast>" + rarity.wrapWithRarityMarkdownAsString(description) + "</sctoast>");

        guiGraphics.drawString(toastComponent.getMinecraft().font, comp, 40, 22, 0x635040, false);

        if (timeSinceLastVisible < 10000)
        {
            return Visibility.SHOW;
        }
        else
        {
            return Visibility.HIDE;
        }
    }

}
