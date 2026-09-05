package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.minigame.KonamiDetector;
import com.wdiscute.starcatcher.registry.SCKeymappings;
import com.wdiscute.starcatcher.tournament.TournamentLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SCClientForgeEvents {
    @SubscribeEvent
    public static void keyPressed(InputEvent.Key event)
    {
        if (event.getAction() == 1)
            KonamiDetector.keyPressed(event.getKey());

        if (SCKeymappings.EXPAND_TOURNAMENT.consumeClick())
            TournamentLayer.expandedType = TournamentLayer.expandedType.next();

        if (SCKeymappings.OPEN_GUIDE.consumeClick() && SCConfig.ALLOW_GUIDE_KEYBIND.get())
            Minecraft.getInstance().setScreen(new FishingGuideScreen(BlockPos.ZERO, null));

    }
}
