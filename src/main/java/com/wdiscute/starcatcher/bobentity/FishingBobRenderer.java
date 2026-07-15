package com.wdiscute.starcatcher.bobentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.tackles.*;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Float.NaN;

public class FishingBobRenderer extends EntityRenderer<FishingBobEntity>
{
    final EntityRendererProvider.Context context;

    public static final Map<ResourceLocation, EntityModel<FishEntity>> BOB_MODELS = new HashMap<>();

    public static void createMap(EntityModelSet modelSet)
    {
        if (!BOB_MODELS.isEmpty()) return;

        BOB_MODELS.put(Starcatcher.BASE, new BaseModel<>(modelSet.bakeLayer(BaseModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("clear"), new ClearModel<>(modelSet.bakeLayer(ClearModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("colorful"), new ColorfulModel<>(modelSet.bakeLayer(ColorfulModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("frog"), new FrogModel<>(modelSet.bakeLayer(FrogModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("kimbe"), new KimbeModel<>(modelSet.bakeLayer(KimbeModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("king"), new KingModel<>(modelSet.bakeLayer(KingModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("pearl"), new PearlModel<>(modelSet.bakeLayer(PearlModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("valley"), new ValleyModel<>(modelSet.bakeLayer(ValleyModel.LAYER_LOCATION)));
        BOB_MODELS.put(Starcatcher.rl("survivor"), new SurvivorModel<>(modelSet.bakeLayer(SurvivorModel.LAYER_LOCATION)));
    }

    public FishingBobRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        this.context = context;
        createMap(context.getModelSet());
    }

    @Override
    public ResourceLocation getTextureLocation(FishingBobEntity fishingBobEntity)
    {
        return Starcatcher.rl("textures/entity/fishing/base.png");
    }

    @Override
    public void render(FishingBobEntity fishingBobEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.5F, 0.0F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw));

        //render tackle based on tackle skin, defaults to BaseTackleSkin
        //data attachment returns starcatcher:base if there's no attachment
        ResourceLocation tackleRl = SCDataAttachments.get(fishingBobEntity, SCDataAttachments.TACKLE_SKIN);

        EntityModel<FishEntity> model = BOB_MODELS.getOrDefault(tackleRl, BOB_MODELS.get(Starcatcher.BASE));
        VertexConsumer vertexconsumer = buffer.getBuffer(model.renderType(Utils.rl(tackleRl.getNamespace(),
                "textures/entity/tackle/" + tackleRl.getPath() + ".png")));
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();


        //render fishing line from bobber to player
        if (fishingBobEntity.getOwner() instanceof Player player)
        {
            poseStack.pushPose();
            float f = player.getAttackAnim(partialTicks);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            Vec3 vec3 = this.getPlayerHandPos(player, f1, partialTicks);
            Vec3 vec31 = fishingBobEntity.getPosition(partialTicks).add(0.0, 0.25, 0.0);
            float f2 = (float) (vec3.x - vec31.x);
            float f3 = (float) (vec3.y - vec31.y);
            float f4 = (float) (vec3.z - vec31.z);
            VertexConsumer vertexconsumer1 = buffer.getBuffer(RenderType.lineStrip());
            PoseStack.Pose posestack$pose1 = poseStack.last();

            for (int j = 0; j <= 16; j++)
            {
                stringVertex(0xff000000, f2, f3, f4, vertexconsumer1, posestack$pose1, fraction(j, 16), fraction(j + 1, 16));
            }

            //PLEASE FOR THE LOVE OF GOD DONT REMOVE THIS LINE JUST DONT PLEASE THIS TOOK TOO FUCKING LONG DONT YOU DARE TOUCH IT
            vertexconsumer1.addVertex(NaN, NaN, NaN).setColor(0).setNormal(posestack$pose1, 0, 0, 0);

            poseStack.popPose();
            super.render(fishingBobEntity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        }
    }

    private static void stringVertex(int color, float x, float y, float z, VertexConsumer consumer, PoseStack.Pose pose, float stringFraction, float nextStringFraction)
    {
        if (color == 0xffff9999) color = -16777216;

        float f = x * stringFraction;
        float f1 = y * (stringFraction * stringFraction + stringFraction) * 0.5F + 0.25F;
        float f2 = z * stringFraction;
        float f3 = x * nextStringFraction - f;
        float f4 = y * (nextStringFraction * nextStringFraction + nextStringFraction) * 0.5F + 0.25F - f1;
        float f5 = z * nextStringFraction - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        consumer.addVertex(pose, f, f1, f2).setColor(color).setNormal(pose, f3, f4, f5);
    }

    private static float fraction(int numerator, int denominator)
    {
        return (float) numerator / (float) denominator;
    }

    private Vec3 getPlayerHandPos(Player player, float p_340872_, float partialTick)
    {
        int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        ItemStack itemstack = player.getMainHandItem();
        if (!itemstack.is(SCTags.RODS))
        {
            i = -i;
        }

        if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player)
        {
            double d4 = 960.0 / (double) this.entityRenderDispatcher.options.fov().get().intValue();
            Vec3 vec3 = this.entityRenderDispatcher
                    .camera
                    .getNearPlane()
                    .getPointOnPlane((float) i * 0.525F, -0.1F)
                    .scale(d4)
                    .yRot(p_340872_ * 0.5F)
                    .xRot(-p_340872_ * 0.7F);
            return player.getEyePosition(partialTick).add(vec3);
        }
        else
        {
            float f = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot) * (float) (Math.PI / 180.0);
            double d0 = (double) Mth.sin(f);
            double d1 = (double) Mth.cos(f);
            float f1 = player.getScale();
            double d2 = (double) i * 0.35 * (double) f1;
            double d3 = 0.8 * (double) f1;
            float f2 = player.isCrouching() ? -0.1875F : 0.0F;
            return player.getEyePosition(partialTick).add(-d1 * d2 - d0 * d3, (double) f2 - 0.45 * (double) f1, -d0 * d2 + d1 * d3);
        }
    }
}
