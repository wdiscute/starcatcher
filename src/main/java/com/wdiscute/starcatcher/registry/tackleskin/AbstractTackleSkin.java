package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractTackleSkin
{

    public void onBiting(Player player, Entity bobber)
    {
        RandomSource random = player.level().getRandom();
        bobber.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.4F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
    }

    public void onMinigameStarted(Player player)
    {
    }

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
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    public void onSuccessfulMinigame(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_CELEBRATE, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    public void onFailedMinigame(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    public void onTick(FishingBobEntity bobEntity)
    {

    }
}
