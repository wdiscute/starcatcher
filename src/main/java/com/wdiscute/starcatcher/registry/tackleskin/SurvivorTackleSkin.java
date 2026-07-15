package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.registry.SCSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SurvivorTackleSkin extends AbstractTackleSkin
{
    @Override
    public void onBiting(Player player, Entity bobber)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.SURVIVOR_BITING.get(), SoundSource.NEUTRAL, 0.8f, 1f);
    }

    @Override
    public void onMinigameStarted(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.SURVIVOR_MINIGAME_STARTS.get(),
                SoundSource.NEUTRAL, 0.5f, 1f);
    }
}
