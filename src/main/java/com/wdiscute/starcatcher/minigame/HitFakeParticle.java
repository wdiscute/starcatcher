package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.joml.Vector2d;

import java.util.Random;

public class HitFakeParticle
{
    public static final ResourceLocation TEXTURE = Starcatcher.rl("textures/gui/minigame/minigame.png");

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

    public void render(GuiGraphics guiGraphics, int width, int height)
    {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y, 0);
        RenderSystem.setShaderColor(r, g, b, a);

        guiGraphics.fill(width / 2, height / 2, width / 2 + 1, height / 2 + 1, 0xffffffff);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        poseStack.popPose();
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
    }

    public boolean tick()
    {
        lifetime++;

        pos = pos.add(new Vector2d(vecDirection.x * speed, vecDirection.y * speed));

        return lifetime > maxLifetime;
    }

}
