package com.wdiscute.starcatcher.fishentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.fishmodels.*;
import com.wdiscute.starcatcher.shaders.GoldRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FishRenderer extends MobRenderer<FishEntity, EntityModel<FishEntity>>
{
    ItemRenderer itemRenderer;
    public static Map<Item, EntityModel<FishEntity>> map = new HashMap<>();

    public FishRenderer(EntityRendererProvider.Context context)
    {
        super(context, null, 0.25f);
        itemRenderer = context.getItemRenderer();
        createMap(context.getModelSet());
    }

    public static void createMap(EntityModelSet modelSet)
    {
        if (!map.isEmpty()) return;

        map.put(SCItems.AGAVE_BREAM.get(), new AgaveBream<>(modelSet.bakeLayer(AgaveBream.LAYER_LOCATION)));
        map.put(SCItems.BIGEYE_TUNA.get(), new BigeyeTuna<>(modelSet.bakeLayer(BigeyeTuna.LAYER_LOCATION)));
        map.put(SCItems.BOREAL.get(), new Boreal<>(modelSet.bakeLayer(Boreal.LAYER_LOCATION)));
        map.put(SCItems.CACTIFISH.get(), new CactiFish<>(modelSet.bakeLayer(CactiFish.LAYER_LOCATION)));
        map.put(SCItems.CHARFISH.get(), new Charfish<>(modelSet.bakeLayer(Charfish.LAYER_LOCATION)));
        map.put(SCItems.CRYSTALBACK_BOREAL.get(), new CrystalbackBoreal<>(modelSet.bakeLayer(CrystalbackBoreal.LAYER_LOCATION)));
        map.put(SCItems.CRYSTALBACK_MINNOW.get(), new CrystalbackMinnow<>(modelSet.bakeLayer(CrystalbackMinnow.LAYER_LOCATION)));
        map.put(SCItems.DEEPJAW_HERRING.get(), new DeepjawHerring<>(modelSet.bakeLayer(DeepjawHerring.LAYER_LOCATION)));
        map.put(SCItems.DOWNFALL_BREAM.get(), new DownfallBream<>(modelSet.bakeLayer(DownfallBream.LAYER_LOCATION)));
        map.put(SCItems.DRIFTFIN.get(), new Driftfin<>(modelSet.bakeLayer(Driftfin.LAYER_LOCATION)));
        map.put(SCItems.DRIFTING_BREAM.get(), new DriftingBream<>(modelSet.bakeLayer(DriftingBream.LAYER_LOCATION)));
        map.put(SCItems.DUSKTAIL_SNAPPER.get(), new DusktailSnapper<>(modelSet.bakeLayer(DusktailSnapper.LAYER_LOCATION)));
        map.put(SCItems.LILY_SNAPPER.get(), new LilySnapper<>(modelSet.bakeLayer(LilySnapper.LAYER_LOCATION)));
        map.put(SCItems.PINK_KOI.get(), new PinkKoi<>(modelSet.bakeLayer(PinkKoi.LAYER_LOCATION)));
        map.put(SCItems.SILVERVEIL_PERCH.get(), new SilverveilPerch<>(modelSet.bakeLayer(SilverveilPerch.LAYER_LOCATION)));
        map.put(SCItems.SLUDGE_CATFISH.get(), new SludgeCatfish<>(modelSet.bakeLayer(SludgeCatfish.LAYER_LOCATION)));
        map.put(SCItems.WHITEVEIL.get(), new Whiteveil<>(modelSet.bakeLayer(Whiteveil.LAYER_LOCATION)));
        map.put(SCItems.WINTERY_PIKE.get(), new WinteryPike<>(modelSet.bakeLayer(WinteryPike.LAYER_LOCATION)));
        map.put(SCItems.CRYSTALBACK_TROUT.get(), new CrystalbackTrout<>(modelSet.bakeLayer(CrystalbackTrout.LAYER_LOCATION)));
        map.put(SCItems.EMBERGILL.get(), new Embergill<>(modelSet.bakeLayer(Embergill.LAYER_LOCATION)));
        map.put(SCItems.FROSTGILL_CHUB.get(), new FrostgillChub<>(modelSet.bakeLayer(FrostgillChub.LAYER_LOCATION)));
        map.put(SCItems.FROSTJAW_TROUT.get(), new FrostjawTrout<>(modelSet.bakeLayer(FrostjawTrout.LAYER_LOCATION)));
        map.put(SCItems.HOLLOWBELLY_DARTER.get(), new HollowbellyDarter<>(modelSet.bakeLayer(HollowbellyDarter.LAYER_LOCATION)));
        map.put(SCItems.ICETOOTH_STURGEON.get(), new IcetoothSturgeon<>(modelSet.bakeLayer(IcetoothSturgeon.LAYER_LOCATION)));
        map.put(SCItems.MISTBACK_CHUB.get(), new MistbackChub<>(modelSet.bakeLayer(MistbackChub.LAYER_LOCATION)));
        map.put(SCItems.BLUE_CRYSTAL_FIN.get(), new BlueCrystalFin<>(modelSet.bakeLayer(BlueCrystalFin.LAYER_LOCATION)));
        map.put(SCItems.CARPENJOE.get(), new Carpenjoe<>(modelSet.bakeLayer(Carpenjoe.LAYER_LOCATION)));
        map.put(SCItems.ELDERSCALE.get(), new Elderscale<>(modelSet.bakeLayer(Elderscale.LAYER_LOCATION)));
        map.put(SCItems.GHOSTLY_PIKE.get(), new GhostlyPike<>(modelSet.bakeLayer(GhostlyPike.LAYER_LOCATION)));
        map.put(SCItems.IRONJAW_HERRING.get(), new IronjarHerring<>(modelSet.bakeLayer(IronjarHerring.LAYER_LOCATION)));
        map.put(SCItems.MIRAGE_CARP.get(), new MirageCarp<>(modelSet.bakeLayer(MirageCarp.LAYER_LOCATION)));
        map.put(SCItems.PETALDRIFT_CARP.get(), new PetaldriftCarp<>(modelSet.bakeLayer(PetaldriftCarp.LAYER_LOCATION)));
        map.put(SCItems.BLUE_HERRING.get(), new BlueHerring<>(modelSet.bakeLayer(BlueHerring.LAYER_LOCATION)));
        map.put(SCItems.LIGHTNING_BASS.get(), new LightningBass<>(modelSet.bakeLayer(LightningBass.LAYER_LOCATION)));
        map.put(SCItems.LUSH_PIKE.get(), new LushPike<>(modelSet.bakeLayer(LushPike.LAYER_LOCATION)));
        map.put(SCItems.MAGMA_FISH.get(), new MagmaFish<>(modelSet.bakeLayer(MagmaFish.LAYER_LOCATION)));
        map.put(SCItems.MORGANITE.get(), new Morganite<>(modelSet.bakeLayer(Morganite.LAYER_LOCATION)));
        map.put(SCItems.PALE_PINFISH.get(), new PalePinfish<>(modelSet.bakeLayer(PalePinfish.LAYER_LOCATION)));
        map.put(SCItems.PINFISH.get(), new Pinfish<>(modelSet.bakeLayer(Pinfish.LAYER_LOCATION)));
        map.put(SCItems.PYROTROUT.get(), new Pyrotrout<>(modelSet.bakeLayer(Pyrotrout.LAYER_LOCATION)));
        map.put(SCItems.SCULKFISH.get(), new Sculkfish<>(modelSet.bakeLayer(Sculkfish.LAYER_LOCATION)));
        map.put(SCItems.SILVERFIN_PIKE.get(), new SilverfinPike<>(modelSet.bakeLayer(SilverfinPike.LAYER_LOCATION)));
        map.put(SCItems.VIVID_MOSS.get(), new VividMoss<>(modelSet.bakeLayer(VividMoss.LAYER_LOCATION)));
        map.put(SCItems.WILLISH.get(), new Willish<>(modelSet.bakeLayer(Willish.LAYER_LOCATION)));
        map.put(SCItems.YELLOWSTONE_FISH.get(), new YellowstoneFish<>(modelSet.bakeLayer(YellowstoneFish.LAYER_LOCATION)));
        map.put(SCItems.VOIDBITER.get(), new Voidbiter<>(modelSet.bakeLayer(Voidbiter.LAYER_LOCATION)));
        map.put(SCItems.OBIDONTIEE.get(), new Obidontiee<>(modelSet.bakeLayer(Obidontiee.LAYER_LOCATION)));
        map.put(SCItems.REDSCALED_TUNA.get(), new RedscaledTuna<>(modelSet.bakeLayer(RedscaledTuna.LAYER_LOCATION)));
        map.put(SCItems.SUN_SEEKING_CARP.get(), new SunSeekingCarp<>(modelSet.bakeLayer(SunSeekingCarp.LAYER_LOCATION)));
        map.put(SCItems.SUNEATER.get(), new Suneater<>(modelSet.bakeLayer(Suneater.LAYER_LOCATION)));
        map.put(SCItems.SUNNY_STURGEON.get(), new SunnySturgeon<>(modelSet.bakeLayer(SunnySturgeon.LAYER_LOCATION)));
        map.put(SCItems.THE_QUARRISH.get(), new TheQuarrish<>(modelSet.bakeLayer(TheQuarrish.LAYER_LOCATION)));
        map.put(SCItems.THUNDER_BASS.get(), new ThunderBass<>(modelSet.bakeLayer(ThunderBass.LAYER_LOCATION)));
        map.put(SCItems.TWILIGHT_KOI.get(), new TwilightKoi<>(modelSet.bakeLayer(TwilightKoi.LAYER_LOCATION)));
        map.put(SCItems.WILLOW_BREAM.get(), new WillowBream<>(modelSet.bakeLayer(WillowBream.LAYER_LOCATION)));
        map.put(SCItems.CERBERAY.get(), new Cerberay<>(modelSet.bakeLayer(Cerberay.LAYER_LOCATION)));
    }

    @Override
    public void render(FishEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        model = map.get(entity.getFish().getItem());
        if (model == null) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0, -0.2, 0);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);


        poseStack.popPose();
    }

    @Override
    protected @Nullable RenderType getRenderType(FishEntity livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        ItemStack fish = livingEntity.getFish();

        return getGoldRendertype(getTextureLocation(livingEntity), model, fish);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(FishEntity fish)
    {
        Item item = fish.getFish().getItem();
        if (map.containsKey(item))
           return Starcatcher.rl("entity/fishes/" + BuiltInRegistries.ITEM.getKey(item).getPath());

       return Starcatcher.MISSINGNO;
    }

    @Override
    protected void setupRotations(FishEntity entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);

    }

    public static void renderFishFromItem(ItemRenderer itemRenderer, Map<Item, EntityModel<FishEntity>> map, ItemStack itemStack, MultiBufferSource buffer, PoseStack poseStack, int packedLight, int overlay, Level level)
    {
        if (map.containsKey(itemStack.getItem()))
        {
            Item item = itemStack.getItem();
            EntityModel<FishEntity> model = map.get(item);
            VertexConsumer vertexconsumer = buffer.getBuffer(getGoldRendertype(Starcatcher.rl("entity/fishes/" + BuiltInRegistries.ITEM.getKey(item).getPath()), model, itemStack));
            model.renderToBuffer(poseStack, vertexconsumer, packedLight, overlay);
        }
        else
        {
            poseStack.translate(0F, 1F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight,
                    overlay, poseStack, buffer, level, 0);
        }

    }

    public static RenderType getGoldRendertype(ResourceLocation texture, EntityModel<FishEntity> model, ItemStack fishItem)
    {
        return Rarity.isGolden(fishItem)
                ? GoldRenderer.INSTANCE.getOrCreateEntity(texture, model::renderType).renderType
                : model.renderType(GoldRenderer.getTextureLoc(texture));
    }
}
