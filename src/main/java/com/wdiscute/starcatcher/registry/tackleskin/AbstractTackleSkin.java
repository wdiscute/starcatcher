package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.bobberentity.FishingBobModel;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractTackleSkin
{
    protected FishingBobModel model;

    public abstract Identifier getName();

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
