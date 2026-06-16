package com.wdiscute.starcatcher.guide;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class FishCaughtToast implements Toast
{
    private static final ResourceLocation BACKGROUND_SPRITE = Starcatcher.rl("textures/gui/sprites/toast/fish_caught.png");
    private final Component title;
    private final String fishName;
    private final ItemStack is;
    private FishProperties.Rarity rarity;

    public FishCaughtToast(FishProperties fp)
    {
        this.is = new ItemStack(fp.catchInfo().fish());
        this.title = Component.translatable("gui.starcatcher.toast.fish_caught");
        this.fishName = is.getHoverName().getString();
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

    private int old;

    public Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible)
    {
        guiGraphics.blit(BACKGROUND_SPRITE, 0, 0, this.width(), this.height(), 0, 0, 164, 51, 164, 51);
        guiGraphics.renderItem(this.is, 6, 29);
        guiGraphics.drawString(toastComponent.getMinecraft().font, this.title, 40, 13, 0, false);
        int lettersRevealed = (int) Mth.clamp((float) (timeSinceLastVisible - 500L) / 150L, 0, this.fishName.length());

        if (this.old != lettersRevealed)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.4F, U.r.nextFloat(0.2F) + 1.3F);
            this.old = lettersRevealed;
        }

        Component comp = Tooltips.resolveTagsToComponent(rarity.wrapWithRarityMarkdownAsString(this.fishName.substring(0, lettersRevealed))).append(Component.literal("§kaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".substring(0, this.fishName.length() - lettersRevealed + 2)));
        guiGraphics.drawString(toastComponent.getMinecraft().font, comp, 40, 22, 0x635040, false);

        if (timeSinceLastVisible < 10000)
            return Visibility.SHOW;
        else
            return Visibility.HIDE;
    }

}
