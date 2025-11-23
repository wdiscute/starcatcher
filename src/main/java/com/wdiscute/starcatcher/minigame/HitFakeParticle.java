package com.wdiscute.starcatcher.minigame;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2d;

import java.util.Random;

public class HitFakeParticle
{

    private static final Random random = new Random();

    public float r;
    public float g;
    public float b;
    public float a;

    public Vector2d pos;
    public double speed;
    public Vector2d vecDirection;
    public int lifetime;
    public int maxLifetime;
    public ResourceLocation rl;

    public HitFakeParticle(int x, int y, Vector2d vec)
    {
        this(x, y, vec,0.5f, 0.7f + random.nextFloat() / 3, 0.5f + random.nextFloat() / 5, 1);
    }

    public HitFakeParticle(int x, int y, Vector2d vec, float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        pos = new Vector2d(x + random.nextFloat() * 10 - 5, y + random.nextFloat() * 10 - 5);
        this.vecDirection = vec.normalize();
        this.speed = 0.2 + random.nextFloat() / 3;
        this.maxLifetime = (int) (5 + random.nextFloat() * 20);

        if(random.nextFloat() > 0.9) this.maxLifetime += (int) (40 + random.nextFloat() * 30);

        this.rl = Starcatcher.rl("textures/gui/minigame.png");
    }

    public boolean tick()
    {
        lifetime++;

        pos = pos.add(new Vector2d(vecDirection.x * speed, vecDirection.y * speed));

        return lifetime > maxLifetime;
    }

}
