package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.registry.SCParticles;
import com.wdiscute.starcatcher.registry.SCSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ValleyTackleSkin extends AbstractTackleSkin
{
    @Override
    public void onBiting(Player player, Entity bobber)
    {
        if (player.level().isClientSide) return;
        ((ServerLevel) player.level()).sendParticles(
                SCParticles.VALLEY_NOTIFICATION.get(),
                bobber.position().x, bobber.position().y + 1f, bobber.position().z,
                1, 0, 0, 0, 0);

        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.VALLEY_BITING.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onCast(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.VALLEY_CAST.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onMissed(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.VALLEY_MISSED.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onFailedMinigame(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.VALLEY_FAILED.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onSuccessfulMinigame(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.VALLEY_BOOP.get(), SoundSource.NEUTRAL, 1f, 1f);
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.VALLEY_REEL.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onRetrieve(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.VALLEY_REEL.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onMinigameStarted(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.VALLEY_MINIGAME_STARTS.get(), SoundSource.NEUTRAL, 1f, 1f);
    }
}
