package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCKeymappings;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SCClientForgeEvents {
    @SubscribeEvent
    public static void keyPressed(InputEvent.Key event)
    {
        if(event.getAction() == 0 && event.getKey() == SCKeymappings.EXPAND_TOURNAMENT.getKey().getValue())
        {
            TournamentOverlay.expandedType = TournamentOverlay.expandedType.next();
        }
    }

}
