package com.wdiscute.starcatcher.registry.tackleskin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractTackleSkin
{
    public void onCast(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public void onRetrieve(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public void onMissed(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public void onSuccessfulMinigame(Player player)
    {

    }

    public void onFailedMinigame(Player player)
    {

    }

    public boolean skipMissSound()
    {
        return false;
    }

    public boolean skipSuccessSound()
    {
        return false;
    }
}
