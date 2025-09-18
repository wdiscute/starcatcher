package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.util.Random;


public class FishCaughtToast implements Toast
{
    private static final ResourceLocation BACKGROUND_SPRITE = Starcatcher.rl("toast/entry");
    private final ResourceLocation icon;
    private final Component title;
    private final String description;
    private static final String gibberish = "§kaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private int old;
    private static final Random r = new Random();
    private final ItemStack is;

    public FishCaughtToast(ItemStack is)
    {
        this.is = is;
        this.icon = Starcatcher.rl("");
        this.title = Component.translatable("gui.laicaps.toast.fish_caught");
        this.description =  is.getHoverName().getString();
    }

    public Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible)
    {

        guiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        //guiGraphics.blit(icon, 6, 6, 0, 0, 20, 20, 20, 20);

        guiGraphics.renderItem(is, 8, 8);

        guiGraphics.drawString(toastComponent.getMinecraft().font, this.title, 30, 7, 16746751, false);

        int lettersRevealed = Math.clamp((timeSinceLastVisible - 500) / 150, 0, description.length());

        if(old != lettersRevealed)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.4f, r.nextFloat(0.2f) + 1.3f);
            old = lettersRevealed;
        }

        Component comp = Component.literal(description.substring(0, lettersRevealed))
                .append(Component.literal(gibberish.substring(0, description.length() - lettersRevealed + 2)));

        guiGraphics.drawString(toastComponent.getMinecraft().font, comp, 30, 18, 16777215, false);


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
