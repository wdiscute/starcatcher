package com.wdiscute.starcatcher.guide;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.ScreenUtils;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class FishCaughtToast implements Toast
{
    private static final ScreenUtils.Image BACKGROUND_SPRITE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/fish_caught.png"), 164, 51);
    private final Component title;
    private final String fishName;
    private final ItemStack is;
    private Rarity rarity;

    private int old;
    Visibility v = Visibility.HIDE;

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
    public void extractRenderState(GuiGraphicsExtractor g, Font font, long fullyVisibleForMs)
    {
        BACKGROUND_SPRITE.render(g);
        ScreenUtils.item(g, is, 6, 29);
        ScreenUtils.text(g, font, this.title, 40, 13, 0, false);
        //todo 26
        int lettersRevealed = Math.clamp((fullyVisibleForMs - 500L) / 150L, 0, this.fishName.length());

        if (this.old != lettersRevealed)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.4F, Utils.r.nextFloat(0.2F) + 1.3F);
            this.old = lettersRevealed;
        }

        Component comp = Tooltips.resolveTagsToComponent(rarity.wrapWithRarityMarkdownAsString(this.fishName.substring(0, lettersRevealed))).append(Component.literal("§kaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".substring(0, this.fishName.length() - lettersRevealed + 2)));
        ScreenUtils.text(g, font, comp, 40, 22, 0x635040, false);
    }

    public static void newFish(FishProperties fp, boolean displayToast, float percentile, boolean golden)
    {
        if (displayToast || golden)
            Minecraft.getInstance().getToastManager().addToast(new FishCaughtToast(golden ? fp.withRarity(Rarity.GOLDEN) : fp));

        if(golden)
            Minecraft.getInstance().player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.4f, 0.3f);

        SizeAndWeight.Units units = SCConfig.UNIT.get();

        String size = units.getSizeAsString(fp.sizeWeight().getSizeForPercentile(percentile));
        String weight = units.getWeightAsString(fp.sizeWeight().getWeightForPercentile(percentile));

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        player.sendOverlayMessage(
                Component.literal("")
                        .append(Component.translatable(fp.catchInfo().fish().toStack().getItem().getDescriptionId()))
                        .append(Component.literal(" - " + size + " - " + weight))
        );

        Minecraft.getInstance().gui.overlayMessageTime = 180;
    }
}
