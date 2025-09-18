package com.wdiscute.starcatcher.particles;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class FishingNotificationParticles extends TextureSheetParticle
{
    private final SpriteSet sprites;

    protected FishingNotificationParticles(ClientLevel level, double x, double y, double z, SpriteSet spriteSet)
    {
        super(level, x, y, z);

        this.xd = 0f;
        this.yd = 0f;
        this.zd = 0f;

        this.quadSize = 0.5f;

        this.lifetime = 80;

        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick()
    {
        this.setSpriteFromAge(this.sprites);


        if(age % 20 > 10)
        {
            this.yd = 0.04f;
        }else
        {
            this.yd = -0.04f;
        }

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
            return new FishingNotificationParticles(clientLevel, x, y, z, this.spriteSet);
        }
    }

}
