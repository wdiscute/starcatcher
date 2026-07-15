package com.wdiscute.starcatcher.guide;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class FishCaughtToast implements Toast
{
    private static final ResourceLocation BACKGROUND_SPRITE = Starcatcher.rl("toast/fish_caught");
    private final Component title;
    private final String fishName;
    private final ItemStack is;
    private Rarity rarity;

    public FishCaughtToast(FishProperties fp)
    {
        this.is = new ItemStack(fp.catchInfo().fish().toItem());
        this.title = Component.translatable("gui.starcatcher.toast.fish_caught");
        this.fishName = is.getHoverName().getString();
        if(fp.rarity().equals(Rarity.GOLDEN))
            SCDataComponents.set(is, SCDataComponents.CAUGHT_FISH_INFO, CaughtFishInfo.GOLDEN);
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

    public Toast.Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible)
    {
        guiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
        guiGraphics.renderItem(this.is, 6, 29);
        guiGraphics.drawString(toastComponent.getMinecraft().font, this.title, 40, 13, 0, false);
        int lettersRevealed = Math.clamp((timeSinceLastVisible - 500L) / 150L, 0, this.fishName.length());

        if (this.old != lettersRevealed)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.4F, Utils.r.nextFloat(0.2F) + 1.3F);
            this.old = lettersRevealed;
        }

        Component comp = Tooltips.resolveTagsToComponent(rarity.wrapWithRarityMarkdownAsString(this.fishName.substring(0, lettersRevealed))).append(Component.literal("§kaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".substring(0, this.fishName.length() - lettersRevealed + 2)));
        guiGraphics.drawString(toastComponent.getMinecraft().font, comp, 40, 22, 0x635040, false);

        if (timeSinceLastVisible < 10000)
            return Visibility.SHOW;
        else
            return Visibility.HIDE;
    }

    public static void newFish(FishProperties fp, boolean displayToast, float percentile, boolean golden)
    {
        if (displayToast || golden)
            Minecraft.getInstance().getToasts().addToast(new FishCaughtToast(golden ? fp.withRarity(Rarity.GOLDEN) : fp));

        if(golden)
            Minecraft.getInstance().player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.4f, 0.3f);

        SizeAndWeight.Units units = SCConfig.UNIT.get();

        String size = units.getSizeAsString(fp.sizeWeight().getSizeForPercentile(percentile));
        String weight = units.getWeightAsString(fp.sizeWeight().getWeightForPercentile(percentile));

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        player.displayClientMessage(
                Component.literal("")
                        .append(Component.translatable(fp.catchInfo().fish().toStack().getDescriptionId()))
                        .append(Component.literal(" - " + size + " - " + weight))
                , true);

        Minecraft.getInstance().gui.overlayMessageTime = 180;
    }
}
