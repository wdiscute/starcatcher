package com.wdiscute.starcatcher.particles;

import com.wdiscute.utils.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class FishingBitingLavaParticles extends SingleQuadParticle
{
    private final SpriteSet sprites;

    protected FishingBitingLavaParticles(ClientLevel level, double x, double y, double z, SpriteSet spriteSet)
    {
        super(level, x, y, z, spriteSet.first());

        this.xd = 0f + Utils.r.nextFloat(0.2f) - 0.1f;
        this.yd = 0f + Utils.r.nextFloat(0.2f) + 0.1f;
        this.zd = 0f + Utils.r.nextFloat(0.2f) - 0.1f;

        this.quadSize = Utils.r.nextFloat(0.2f) + 0.05f;

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
    protected Layer getLayer()
    {
        return Layer.bySprite(sprite);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public @org.jspecify.annotations.Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random)
        {
            return new FishingBitingLavaParticles(level, x, y, z, this.spriteSet);
        }
    }
}
