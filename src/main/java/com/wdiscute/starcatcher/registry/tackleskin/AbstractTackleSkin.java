package com.wdiscute.starcatcher.registry.tackleskin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.bobberentity.FishingBobEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractTackleSkin
{
    public abstract ModelLayerLocation getLayerLocation();

    public abstract Identifier getTexture();

    RenderType renderType = null;
    protected FishingBobModel<FishingBobEntity> model;

    public void renderTackle(EntityRendererProvider.Context context, FishingBobEntity fishingBobEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        if (renderType == null)
        {
            this.model = new BaseTackleSkin();
            this.renderType = RenderTypes.entityCutout(getTexture());
        }
        this.model.renderToBuffer(poseStack, buffer.getBuffer(renderType), packedLight, OverlayTexture.NO_OVERLAY, -1);
    }

    public void onCast(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public void onRetrieve(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public void onMissed(Player player)
    {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public void onSuccessfulMinigame(Player player)
    {

    }

    public void onFailedMinigame(Player player)
    {

    }

    public boolean skipMissSound()
    {
        return false;
    }

    public boolean skipSuccessSound()
    {
        return false;
    }
}
