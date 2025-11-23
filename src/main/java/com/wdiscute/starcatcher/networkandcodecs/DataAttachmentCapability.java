package com.wdiscute.starcatcher.networkandcodecs;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.List;

@AutoRegisterCapability
public interface DataAttachmentCapability
{
    Capability<DataAttachmentCapability> PLAYER_DATA = CapabilityManager.get(new CapabilityToken<>(){});

    String fishing();
    void setFishing(String s);

    List<FishCaughtCounter> fishesCaught();
    void setFishesCaught(List<FishCaughtCounter> list);

    List<TrophyProperties> trophiesCaught();
    void setTrophiesCaught(List<TrophyProperties> list);

    List<FishProperties> fishNotifications();
    void setFishNotifications(List<FishProperties> list);

}
