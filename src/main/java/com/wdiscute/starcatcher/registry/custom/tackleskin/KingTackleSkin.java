package com.wdiscute.starcatcher.registry.custom.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.ModSounds;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class KingTackleSkin extends AbstractTackleSkin
{
    /* Not working with Arclight
    java.lang.NoClassDefFoundError: net/minecraft/client/model/geom/ModelLayerLocation
	at TRANSFORMER/starcatcher@2.1-NEOFORGE-1.21.1/com.wdiscute.starcatcher.registry.custom.tackleskin.KingTackleSkin.<clinit>(KingTackleSkin.java:16) ~[starcatcher-2.1-NEOFORGE-1.21.1.jar%23310!/:?] {re:classloading}
	at TRANSFORMER/starcatcher@2.1-NEOFORGE-1.21.1/com.wdiscute.starcatcher.registry.custom.tackleskin.ModTackleSkins.get(ModTackleSkins.java:61) ~[starcatcher-2.1-NEOFORGE-1.21.1.jar%23310!/:?] {re:classloading}
	at TRANSFORMER/starcatcher@2.1-NEOFORGE-1.21.1/com.wdiscute.starcatcher.rod.StarcatcherFishingRodItem.use(StarcatcherFishingRodItem.java:66) ~[starcatcher-2.1-NEOFORGE-1.21.1.jar%23310!/:?] {re:classloading}
    */
    //public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Starcatcher.rl("base"), "main");

    @Override
    public ModelLayerLocation getLayerLocation()
    {
        return new ModelLayerLocation(Starcatcher.rl("base"), "main");
        //return LAYER_LOCATION;
    }

    @Override
    public ResourceLocation getTexture()
    {
        return Starcatcher.rl("textures/entity/tackle/base.png");
    }

    public static LayerDefinition createBodyLayer()
    {
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
                        .texOffs(4, 24).addBox(-1.0F, -1.5F, 2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void onMissed(Player player)
    {
        super.onMissed(player);
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, ModSounds.KING_GRR.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onSuccessfulMinigame(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, ModSounds.KING_HEHEHA.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onFailedMinigame(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, ModSounds.KING_CRY.get(), SoundSource.NEUTRAL, 1f, 1f);
    }
}
