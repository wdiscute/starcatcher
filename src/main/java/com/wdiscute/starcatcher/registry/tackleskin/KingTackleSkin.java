package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCSounds;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class KingTackleSkin extends AbstractTackleSkin
{

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl("king"), "main");

    @Override
    public ModelLayerLocation getLayerLocation()
    {
        return MODEL_LAYER_LOCATION;
    }

    @Override
    public ResourceLocation getTexture()
    {
        return Starcatcher.rl("textures/entity/tackle/king.png");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition king = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(3.1F, -8.0F, -3.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(12, 12).addBox(-3.1F, -8.0F, -3.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-3.0F, -8.0F, -3.1F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(12, 22).addBox(-3.0F, -8.0F, 3.1F, 6.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public boolean skipMissSound()
    {
        return true;
    }

    @Override
    public boolean skipSuccessSound()
    {
        return true;
    }

    @Override
    public void onMissed(Player player)
    {
        super.onMissed(player);
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.KING_GRR.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onSuccessfulMinigame(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.KING_HEHEHA.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onFailedMinigame(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.KING_CRY.get(), SoundSource.NEUTRAL, 1f, 1f);
    }
}
