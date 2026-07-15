package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PearlTackleSkin extends AbstractTackleSkin
{
    @Override
    public void onTick(FishingBobEntity bobEntity)
    {
        Vec3 pos = bobEntity.position();
        RandomSource r = bobEntity.getRandom();

        if(!bobEntity.level().isClientSide)
        {
            if(r.nextFloat() > 0.3f) return;
            ServerLevel level = (ServerLevel) bobEntity.level();
            level.sendParticles(
                    ParticleTypes.END_ROD,
                    pos.x + (r.nextFloat() - 0.5f) * 0.8,
                    pos.y + (r.nextFloat() - 0.5f) * 0.8,
                    pos.z + (r.nextFloat() - 0.5f) * 0.8,
                    1,
                    0, 0, 0, 0
            );
        }
    }

    @Override
    public void onCast(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.NEUTRAL, 1F, 1f);
    }

    @Override
    public void onSuccessfulMinigame(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 2F, 1f);
    }
}
