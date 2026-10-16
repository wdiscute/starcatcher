package com.wdiscute.starcatcher.fishentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.items.FishItem;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
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
        for (InteractionHand hand : InteractionHand.values())
        {
            ItemStack stack;
            if (hand == InteractionHand.MAIN_HAND)
                stack = state.mainArm == HumanoidArm.RIGHT ? state.rightHandItemStack : state.leftHandItemStack;
            else
                stack = state.mainArm == HumanoidArm.RIGHT ? state.leftHandItemStack : state.rightHandItemStack;

            Item item = stack.getItem();
            if (!(item instanceof FishItem))
                continue;

            HumanoidArm mainArm = hand == InteractionHand.MAIN_HAND ? state.mainArm : state.mainArm.getOpposite();

            ModelPart arm = mainArm == HumanoidArm.RIGHT ? getParentModel().rightArm : getParentModel().leftArm;
            boolean modelOnRight = mainArm == HumanoidArm.RIGHT;

            ps.pushPose();
            arm.translateAndRotate(ps);
            ItemStack main = state.mainArm == HumanoidArm.RIGHT ? state.rightHandItemStack : state.leftHandItemStack;
            ItemStack off = state.mainArm == HumanoidArm.RIGHT ? state.leftHandItemStack : state.rightHandItemStack;

            SCArmPoses.ArmsUp armsUp = SCArmPoses.getArmsUp(mainArm, main, off);

            float scale = SCDataComponents.getOrDefault(
                    stack, SCDataComponents.CAUGHT_FISH_INFO,
                    CaughtFishInfo.AVERAGE
            ).getScale();

            ps.translate(0, -0.35 + scale * 0.2, 0);

            ps.translate(0, 1, 0);
            ps.mulPose(Axis.YN.rotationDegrees(180));
            ps.mulPose(Axis.XN.rotationDegrees(180));

            if (armsUp == SCArmPoses.ArmsUp.BOTH_ARMS)
                ps.translate(modelOnRight ? -0.3f : 0.3f, 0, 0);

            ps.mulPose(Axis.YP.rotationDegrees(90));

            if (armsUp == SCArmPoses.ArmsUp.BOTH_SPLIT)
                ps.mulPose(Axis.YP.rotationDegrees(90));

            ps.scale(scale, scale, scale);

            ps.translate(0, -1, 0);

            FishEntityRenderState ir = new FishEntityRenderState();
            ir.lightCoords = lightCoords;
            FishRenderer.renderFishFromItem(ir, stack, collector, ps);
            ps.popPose();
        }
    }
}
