package com.wdiscute.starcatcher.fishentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;

public class HeldFishLayer extends RenderLayer<AvatarRenderState, PlayerModel>
{
    public record HeldFishRenderData(ItemStack right, ItemStack left)
    {
        public static final ContextKey<HeldFishRenderData> KEY = new ContextKey<>(
                Identifier.fromNamespaceAndPath(Starcatcher.MOD_ID, "held_fish"));
    }

    public HeldFishLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent, EntityModelSet models)
    {
        super(parent);
        FishRenderer.createMap(models);
    }

    @Override
    public void submit(PoseStack ps, SubmitNodeCollector collector, int lightCoords,
                       AvatarRenderState state, float yRot, float xRot)
    {
        HeldFishRenderData held = state.getRenderData(HeldFishRenderData.KEY);
        if (held == null) return;

        renderArm(held.right(), getParentModel().rightArm, ps, collector, lightCoords);
        renderArm(held.left(), getParentModel().leftArm, ps, collector, lightCoords);
    }

    private void renderArm(ItemStack stack, ModelPart arm, PoseStack ps, SubmitNodeCollector collector, int lightCoords)
    {
        if (!FishRenderer.canRender(stack)) return;

        ps.pushPose();
        arm.translateAndRotate(ps);

        float scale = SCDataComponents.getOrDefault(
                stack, SCDataComponents.CAUGHT_FISH_INFO,
                CaughtFishInfo.AVERAGE
        ).getScale();

        //scaling + pivot adjusting
        ps.translate(0, 1, 0);
        ps.mulPose(Axis.XP.rotationDegrees(180));
        ps.scale(scale, scale, scale);
        ps.translate(0, -1, 0);

        ps.translate(0.4F, 0.35F, 0.0F);

        ps.translate((1 - scale) / 3, -scale / 4, 0.0F);
        ps.mulPose(Axis.YP.rotationDegrees(90));

        FishEntityRenderState ir = new FishEntityRenderState();
        ir.lightCoords = lightCoords;
        FishRenderer.renderFishFromItem(ir, stack, collector, ps);

        ps.popPose();
    }
}
