package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class ColorfulTackleSkin extends AbstractTackleSkin
{
    @Override
    public void onTick(FishingBobEntity bobEntity)
    {
        Vec3 pos = bobEntity.position();
        RandomSource r = bobEntity.getRandom();

        if(!bobEntity.level().isClientSide)
        {
            ServerLevel level = (ServerLevel) bobEntity.level();
            level.sendParticles(
                    new DustParticleOptions(Vec3.fromRGB24(
                            FastColor.ABGR32.color(255, r.nextInt(255), r.nextInt(255), r.nextInt(255))).toVector3f(), 0.5F),
                    pos.x + (r.nextFloat() - 0.5f) * 0.8,
                    pos.y + (r.nextFloat() - 0.5f) * 0.8,
                    pos.z + (r.nextFloat() - 0.5f) * 0.8,
                    1,
                    0, 0, 0, 0
            );
        }



        super.onTick(bobEntity);
    }
}
