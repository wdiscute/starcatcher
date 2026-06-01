package com.wdiscute.starcatcher.registry.tackleskin;

import net.minecraft.client.model.geom.builders.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FrogTackleSkin extends AbstractTackleSkin
{
    @Override
    public void onCast(Player player)
    {
        super.onCast(player);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FROG_AMBIENT, SoundSource.NEUTRAL, 1F, 1f);
    }

    @Override
    public void onRetrieve(Player player)
    {
        super.onRetrieve(player);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FROG_AMBIENT, SoundSource.NEUTRAL, 1F, 1f);
    }

    @Override
    public void onFailedMinigame(Player player)
    {
        super.onFailedMinigame(player);
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SoundEvents.FROG_DEATH, SoundSource.AMBIENT);
    }
}
