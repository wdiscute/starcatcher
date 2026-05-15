package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FrogTackleSkin extends AbstractTackleSkin
{

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl("frog"), "main");

    @Override
    public ModelLayerLocation getLayerLocation()
    {
        return MODEL_LAYER_LOCATION;
    }

    @Override
    public ResourceLocation getTexture()
    {
        return Starcatcher.rl("textures/entity/tackle/frog.png");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "root", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(10, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(10, 19).addBox(-1.0F, 1.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(6, 18).addBox(0.0F, 1.0F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 15).addBox(-2.0F, -4.0F, -0.5F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(-8, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 18).addBox(-3.0F, -2.0F, -2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 22).addBox(2.0F, -2.0F, -2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 9).addBox(2.0F, -2.0F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(12, 9).addBox(-3.0F, -2.0F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(4, 24).addBox(-1.0F, -1.5F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

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
