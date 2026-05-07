package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.guide.FishCaughtToast;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.guide.FishingSignedGuideScreen;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.SignedGuide;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.starcatcher.secretnotes.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Starcatcher.MOD_ID, dist = Dist.CLIENT)
public class StarcatcherClient
{
    public StarcatcherClient(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static Player getClientPlayer()
    {
        return Minecraft.getInstance().player;
    }

    public static void fishCaughtToast(FishProperties fp, boolean newFish, int sizeCM, int weightCM)
    {
        if (newFish) Minecraft.getInstance().getToastManager().addToast(new FishCaughtToast(fp));

        FishProperties.SizeAndWeight.Units units = SCConfig.UNIT.get();

        String size = units.getSizeAsString(sizeCM);
        String weight = units.getWeightAsString(weightCM);

        Minecraft.getInstance().player.sendOverlayMessage(
                Component.literal("")
                        .append(Component.translatable(fp.catchInfo().fish().value().getDescriptionId()))
                        .append(Component.literal(" - " + size + " - " + weight)));

        Minecraft.getInstance().gui.overlayMessageTime = 180;
    }

    public static void openGuideScreen()
    {
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        Minecraft.getInstance().setScreen(new FishingGuideScreen());
    }

    public static void openSignedGuideScreen(SignedGuide signedGuide)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        Minecraft.getInstance().setScreen(new FishingSignedGuideScreen(signedGuide));
    }

    public static void openMessageScreen(LetterItem.Message message)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        Minecraft.getInstance().setScreen(new MessageScreen(message));
    }

    public static void openNoteScreen(SecretNote.Note note)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        Minecraft.getInstance().setScreen(new SecretNoteScreen(note, null));
    }

    public static void openMessageWriteScreen(LetterItem.Message message)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        Minecraft.getInstance().setScreen(new MessageWriteScreen(message));
    }

    public static void openFishingMinigameScreen(FishProperties fp, ItemStack rod)
    {
        Minecraft.getInstance().setScreen(new FishingMinigameScreen(fp, rod));
    }
}
