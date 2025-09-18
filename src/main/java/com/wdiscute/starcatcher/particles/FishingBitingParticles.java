package com.wdiscute.starcatcher.particles;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FishingBitingParticles extends TextureSheetParticle
{
    private final SpriteSet sprites;

    protected FishingBitingParticles(ClientLevel level, double x, double y, double z, SpriteSet spriteSet)
    {
        super(level, x, y, z);


        Random r = new Random();

        this.xd = 0f + r.nextFloat(0.2f) - 0.1f;
        this.yd = 0f + r.nextFloat(0.2f) + 0.1f;
        this.zd = 0f + r.nextFloat(0.2f) - 0.1f;

        this.quadSize = r.nextFloat(0.2f) + 0.05f;

        this.lifetime = 20;

        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick()
    {
        this.setSpriteFromAge(this.sprites);


        this.yd -= 0.01f;

        this.xd *= 0.95f;
        this.yd *= 0.95f;
        this.zd *= 0.95f;

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) this.remove();

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new FishingBitingParticles(clientLevel, x, y, z, this.spriteSet);
        }
    }

}
