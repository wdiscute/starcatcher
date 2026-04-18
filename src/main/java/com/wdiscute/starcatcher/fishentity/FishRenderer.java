package com.wdiscute.starcatcher.fishentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.fishmodels.*;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class FishRenderer extends EntityRenderer<FishEntity, FishEntityRenderState>
{
    ItemModelResolver itemRenderer;
    public static Map<Item, EntityModel<FishEntityRenderState>> map = new HashMap<>();

    public FishRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        itemRenderer = context.getItemModelResolver();
        createMap(context.getModelSet());
    }

    @Override
    public FishEntityRenderState createRenderState()
    {
        return new FishEntityRenderState();
    }

    public static void createMap(EntityModelSet modelSet)
    {
        if (!map.isEmpty()) return;

        map.put(SCItems.AGAVE_BREAM.get(), new AgaveBream(modelSet.bakeLayer(AgaveBream.LAYER_LOCATION)));
        map.put(SCItems.BIGEYE_TUNA.get(), new BigeyeTuna(modelSet.bakeLayer(BigeyeTuna.LAYER_LOCATION)));
        map.put(SCItems.BOREAL.get(), new Boreal(modelSet.bakeLayer(Boreal.LAYER_LOCATION)));
        map.put(SCItems.CACTIFISH.get(), new CactiFish(modelSet.bakeLayer(CactiFish.LAYER_LOCATION)));
        map.put(SCItems.CHARFISH.get(), new Charfish(modelSet.bakeLayer(Charfish.LAYER_LOCATION)));
        map.put(SCItems.CRYSTALBACK_BOREAL.get(), new CrystalbackBoreal(modelSet.bakeLayer(CrystalbackBoreal.LAYER_LOCATION)));
        map.put(SCItems.CRYSTALBACK_MINNOW.get(), new CrystalbackMinnow(modelSet.bakeLayer(CrystalbackMinnow.LAYER_LOCATION)));
        map.put(SCItems.DEEPJAW_HERRING.get(), new DeepjawHerring(modelSet.bakeLayer(DeepjawHerring.LAYER_LOCATION)));
        map.put(SCItems.DOWNFALL_BREAM.get(), new DownfallBream(modelSet.bakeLayer(DownfallBream.LAYER_LOCATION)));
        map.put(SCItems.DRIFTFIN.get(), new Driftfin(modelSet.bakeLayer(Driftfin.LAYER_LOCATION)));
        map.put(SCItems.DRIFTING_BREAM.get(), new DriftingBream(modelSet.bakeLayer(DriftingBream.LAYER_LOCATION)));
        map.put(SCItems.DUSKTAIL_SNAPPER.get(), new DusktailSnapper(modelSet.bakeLayer(DusktailSnapper.LAYER_LOCATION)));
        map.put(SCItems.LILY_SNAPPER.get(), new LilySnapper(modelSet.bakeLayer(LilySnapper.LAYER_LOCATION)));
        map.put(SCItems.PINK_KOI.get(), new PinkKoi(modelSet.bakeLayer(PinkKoi.LAYER_LOCATION)));
        map.put(SCItems.SILVERVEIL_PERCH.get(), new SilverveilPerch(modelSet.bakeLayer(SilverveilPerch.LAYER_LOCATION)));
        map.put(SCItems.SLUDGE_CATFISH.get(), new SludgeCatfish(modelSet.bakeLayer(SludgeCatfish.LAYER_LOCATION)));
        map.put(SCItems.WHITEVEIL.get(), new Whiteveil(modelSet.bakeLayer(Whiteveil.LAYER_LOCATION)));
        map.put(SCItems.WINTERY_PIKE.get(), new WinteryPike(modelSet.bakeLayer(WinteryPike.LAYER_LOCATION)));
        map.put(SCItems.CRYSTALBACK_TROUT.get(), new CrystalbackTrout(modelSet.bakeLayer(CrystalbackTrout.LAYER_LOCATION)));
        map.put(SCItems.EMBERGILL.get(), new Embergill(modelSet.bakeLayer(Embergill.LAYER_LOCATION)));
        map.put(SCItems.FROSTGILL_CHUB.get(), new FrostgillChub(modelSet.bakeLayer(FrostgillChub.LAYER_LOCATION)));
        map.put(SCItems.FROSTJAW_TROUT.get(), new FrostjawTrout(modelSet.bakeLayer(FrostjawTrout.LAYER_LOCATION)));
        map.put(SCItems.HOLLOWBELLY_DARTER.get(), new HollowbellyDarter(modelSet.bakeLayer(HollowbellyDarter.LAYER_LOCATION)));
        map.put(SCItems.ICETOOTH_STURGEON.get(), new IcetoothSturgeon(modelSet.bakeLayer(IcetoothSturgeon.LAYER_LOCATION)));
        map.put(SCItems.MISTBACK_CHUB.get(), new MistbackChub(modelSet.bakeLayer(MistbackChub.LAYER_LOCATION)));
        map.put(SCItems.BLUE_CRYSTAL_FIN.get(), new BlueCrystalFin(modelSet.bakeLayer(BlueCrystalFin.LAYER_LOCATION)));
        map.put(SCItems.CARPENJOE.get(), new Carpenjoe(modelSet.bakeLayer(Carpenjoe.LAYER_LOCATION)));
        map.put(SCItems.ELDERSCALE.get(), new Elderscale(modelSet.bakeLayer(Elderscale.LAYER_LOCATION)));
        map.put(SCItems.GHOSTLY_PIKE.get(), new GhostlyPike(modelSet.bakeLayer(GhostlyPike.LAYER_LOCATION)));
        map.put(SCItems.IRONJAW_HERRING.get(), new IronjarHerring(modelSet.bakeLayer(IronjarHerring.LAYER_LOCATION)));
        map.put(SCItems.MIRAGE_CARP.get(), new MirageCarp(modelSet.bakeLayer(MirageCarp.LAYER_LOCATION)));
        map.put(SCItems.PETALDRIFT_CARP.get(), new PetaldriftCarp(modelSet.bakeLayer(PetaldriftCarp.LAYER_LOCATION)));
        map.put(SCItems.BLUE_HERRING.get(), new BlueHerring(modelSet.bakeLayer(BlueHerring.LAYER_LOCATION)));
        map.put(SCItems.LIGHTNING_BASS.get(), new LightningBass(modelSet.bakeLayer(LightningBass.LAYER_LOCATION)));
        map.put(SCItems.LUSH_PIKE.get(), new LushPike(modelSet.bakeLayer(LushPike.LAYER_LOCATION)));
        map.put(SCItems.MAGMA_FISH.get(), new MagmaFish(modelSet.bakeLayer(MagmaFish.LAYER_LOCATION)));
        map.put(SCItems.MORGANITE.get(), new Morganite(modelSet.bakeLayer(Morganite.LAYER_LOCATION)));
        map.put(SCItems.PALE_PINFISH.get(), new PalePinfish(modelSet.bakeLayer(PalePinfish.LAYER_LOCATION)));
        map.put(SCItems.PINFISH.get(), new Pinfish(modelSet.bakeLayer(Pinfish.LAYER_LOCATION)));
        map.put(SCItems.PYROTROUT.get(), new Pyrotrout(modelSet.bakeLayer(Pyrotrout.LAYER_LOCATION)));
        map.put(SCItems.SCULKFISH.get(), new Sculkfish(modelSet.bakeLayer(Sculkfish.LAYER_LOCATION)));
        map.put(SCItems.SILVERFIN_PIKE.get(), new SilverfinPike(modelSet.bakeLayer(SilverfinPike.LAYER_LOCATION)));
        map.put(SCItems.VIVID_MOSS.get(), new VividMoss(modelSet.bakeLayer(VividMoss.LAYER_LOCATION)));
        map.put(SCItems.WILLISH.get(), new Willish(modelSet.bakeLayer(Willish.LAYER_LOCATION)));
        map.put(SCItems.YELLOWSTONE_FISH.get(), new YellowstoneFish(modelSet.bakeLayer(YellowstoneFish.LAYER_LOCATION)));
        map.put(SCItems.VOIDBITER.get(), new Voidbiter(modelSet.bakeLayer(Voidbiter.LAYER_LOCATION)));
        map.put(SCItems.OBIDONTIEE.get(), new Obidontiee(modelSet.bakeLayer(Obidontiee.LAYER_LOCATION)));
        map.put(SCItems.REDSCALED_TUNA.get(), new RedscaledTuna(modelSet.bakeLayer(RedscaledTuna.LAYER_LOCATION)));
        map.put(SCItems.SUN_SEEKING_CARP.get(), new SunSeekingCarp(modelSet.bakeLayer(SunSeekingCarp.LAYER_LOCATION)));
        map.put(SCItems.SUNEATER.get(), new Suneater(modelSet.bakeLayer(Suneater.LAYER_LOCATION)));
        map.put(SCItems.SUNNY_STURGEON.get(), new SunnySturgeon(modelSet.bakeLayer(SunnySturgeon.LAYER_LOCATION)));
        map.put(SCItems.THE_QUARRISH.get(), new TheQuarrish(modelSet.bakeLayer(TheQuarrish.LAYER_LOCATION)));
        map.put(SCItems.THUNDER_BASS.get(), new ThunderBass(modelSet.bakeLayer(ThunderBass.LAYER_LOCATION)));
        map.put(SCItems.TWILIGHT_KOI.get(), new TwilightKoi(modelSet.bakeLayer(TwilightKoi.LAYER_LOCATION)));
        map.put(SCItems.WILLOW_BREAM.get(), new WillowBream(modelSet.bakeLayer(WillowBream.LAYER_LOCATION)));
    }

    @Override
    public void submit(FishEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        super.submit(state, poseStack, submitNodeCollector, camera);

        ItemStack fish = state.fishStack;

        poseStack.pushPose();

        Vec3 offsetCenter = new Vec3(0f, -0.75f, 0f);

        float scale = SCDataComponents.getOrDefault(
                fish, SCDataComponents.CAUGHT_FISH_INFO,
                new CaughtFishInfo(100, 100, 50, FishProperties.Rarity.COMMON, false)
        ).getScale();

        //todo needed?
        //poseStack.translate(be.x, be.y, be.z);

        //block centering
        poseStack.translate(offsetCenter.x, offsetCenter.y, offsetCenter.z);

        //scaling + pivot adjusting
        poseStack.translate(0, 1, 0);
        poseStack.scale(scale, -scale, scale);
        poseStack.translate(0, -1, 0);

        poseStack.mulPose(Axis.YN.rotationDegrees(entityYaw + 180));

        // Render model here
        if (!fish.isEmpty())
            FishRenderer.renderFishFromItem(state, fish, submitNodeCollector, poseStack);

        poseStack.popPose();
    }

    public static void renderFishFromItem(FishEntityRenderState ir, ItemStack itemStack, SubmitNodeCollector node, PoseStack poseStack)
    {
        if (map.containsKey(itemStack.getItem()))
        {
            Item item = itemStack.getItem();
            EntityModel<FishEntityRenderState> model = map.get(item);

            Identifier rl = Starcatcher.rl("textures/entity/fishes/" + BuiltInRegistries.ITEM.getKey(item).getPath() + ".png");

            node.submitModel(
                    model, ir, poseStack, RenderTypes.entityCutout(rl), ir.lightCoords, OverlayTexture.NO_OVERLAY,
                    ir.lightCoords, null, ir.outlineColor, null
            );
        }
        else
        {
            poseStack.translate(0F, 1F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));
            //itemRenderer.appendItemLayers(itemStack, ItemDisplayContext.FIXED, packedLight,
            //OverlayTexture.NO_OVERLAY, poseStack, buffer, level, U.r.nextInt());
        }

    }

    public static RenderType getGoldRendertype(Identifier texture, EntityModel<FishEntityRenderState> model, ItemStack fishItem)
    {
        if (FishProperties.Rarity.isGolden(fishItem))
        {
            //todo gold renderer
            return RenderTypes.entityCutout(texture);
        }
        return model.renderType(texture);
    }
}
