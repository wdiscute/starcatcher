package com.wdiscute.starcatcher.fishentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fishentity.fishmodels.*;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.shaders.GoldRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
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

        //v3.3
        map.put(SCItems.AMETHYSTBACK.get(), new Amethystback(modelSet.bakeLayer(Amethystback.LAYER_LOCATION)));
        map.put(SCItems.AQUAMARINE_PIKE.get(), new AquamarinePike(modelSet.bakeLayer(AquamarinePike.LAYER_LOCATION)));
        map.put(SCItems.BLOSSOMFISH.get(), new Blossomfish(modelSet.bakeLayer(Blossomfish.LAYER_LOCATION)));
        map.put(SCItems.BLUE_ICE_PIKE.get(), new BlueIcePike(modelSet.bakeLayer(BlueIcePike.LAYER_LOCATION)));
        map.put(SCItems.BLUEGIGI.get(), new Bluegigi(modelSet.bakeLayer(Bluegigi.LAYER_LOCATION)));
        map.put(SCItems.CHORUS_MINNOW.get(), new ChorusMinnow(modelSet.bakeLayer(ChorusMinnow.LAYER_LOCATION)));
        map.put(SCItems.CRYOSPINE.get(), new Cryospine(modelSet.bakeLayer(Cryospine.LAYER_LOCATION)));
        map.put(SCItems.CRYSTALBACK_STURGEON.get(), new CrystalbackSturgeon(modelSet.bakeLayer(CrystalbackSturgeon.LAYER_LOCATION)));
        map.put(SCItems.DARK_AMETHYST_SNAPPER.get(), new DarkAmethystSnapper(modelSet.bakeLayer(DarkAmethystSnapper.LAYER_LOCATION)));
        map.put(SCItems.DREAMLINER.get(), new Dreamliner(modelSet.bakeLayer(Dreamliner.LAYER_LOCATION)));
        map.put(SCItems.DRIPFIN.get(), new Dripfin(modelSet.bakeLayer(Dripfin.LAYER_LOCATION)));
        map.put(SCItems.END_GLOW.get(), new EndGlow(modelSet.bakeLayer(EndGlow.LAYER_LOCATION)));
        map.put(SCItems.FOSSILIZED_ANGELFISH.get(), new FossilizedAngelfish(modelSet.bakeLayer(FossilizedAngelfish.LAYER_LOCATION)));
        map.put(SCItems.GARNET_MACKEREL.get(), new GarnetMackerel(modelSet.bakeLayer(GarnetMackerel.LAYER_LOCATION)));
        map.put(SCItems.GLIMMERGILL.get(), new Glimmergill(modelSet.bakeLayer(Glimmergill.LAYER_LOCATION)));
        map.put(SCItems.GLOWING_DARK.get(), new GlowingDark(modelSet.bakeLayer(GlowingDark.LAYER_LOCATION)));
        map.put(SCItems.GLOWSTONE_PUFFERFISH.get(), new GlowstonePufferfish(modelSet.bakeLayer(GlowstonePufferfish.LAYER_LOCATION)));
        map.put(SCItems.GLOWSTONE_SEEKER.get(), new GlowstoneSeeker(modelSet.bakeLayer(GlowstoneSeeker.LAYER_LOCATION)));
        map.put(SCItems.GOLD_FAN.get(), new GoldFan(modelSet.bakeLayer(GoldFan.LAYER_LOCATION)));
        map.put(SCItems.LILAC_MINNOW.get(), new LilacMinnow(modelSet.bakeLayer(LilacMinnow.LAYER_LOCATION)));
        map.put(SCItems.LIVID_BAMBOO.get(), new LividBamboo(modelSet.bakeLayer(LividBamboo.LAYER_LOCATION)));
        map.put(SCItems.MOSSFIN.get(), new Mossfin(modelSet.bakeLayer(Mossfin.LAYER_LOCATION)));
        map.put(SCItems.MOTHFISH.get(), new Mothfish(modelSet.bakeLayer(Mothfish.LAYER_LOCATION)));
        map.put(SCItems.PALE_CARP.get(), new PaleCarp(modelSet.bakeLayer(PaleCarp.LAYER_LOCATION)));
        map.put(SCItems.PEAKDWELLER.get(), new Peakdweller(modelSet.bakeLayer(Peakdweller.LAYER_LOCATION)));
        map.put(SCItems.PETAL_BASS.get(), new PetalBass(modelSet.bakeLayer(PetalBass.LAYER_LOCATION)));
        map.put(SCItems.PURPLE_CARP.get(), new PurpleCarp(modelSet.bakeLayer(PurpleCarp.LAYER_LOCATION)));
        map.put(SCItems.RAINFIN.get(), new Rainfin(modelSet.bakeLayer(Rainfin.LAYER_LOCATION)));
        map.put(SCItems.SHADOWFIN.get(), new Shadowfin(modelSet.bakeLayer(Shadowfin.LAYER_LOCATION)));

        //10/oct update
        map.put(SCItems.AURORA.get(), new Aurora(modelSet.bakeLayer(Aurora.LAYER_LOCATION)));
        map.put(SCItems.AZURE_CRYSTALBACK_MINNOW.get(), new AzureCrystalbackMinnow(modelSet.bakeLayer(AzureCrystalbackMinnow.LAYER_LOCATION)));
        map.put(SCItems.CLOUDFIN.get(), new Cloudfin(modelSet.bakeLayer(Cloudfin.LAYER_LOCATION)));
        map.put(SCItems.DEEPSLATEFISH.get(), new Deepslatefish(modelSet.bakeLayer(Deepslatefish.LAYER_LOCATION)));
        map.put(SCItems.JOEL.get(), new Joel(modelSet.bakeLayer(Joel.LAYER_LOCATION)));
        map.put(SCItems.OASIS_STURGEON.get(), new OasisSturgeon(modelSet.bakeLayer(OasisSturgeon.LAYER_LOCATION)));
        map.put(SCItems.ROCKGILL.get(), new Rockgill(modelSet.bakeLayer(Rockgill.LAYER_LOCATION)));
        map.put(SCItems.ROSE_SIAMESE_FISH.get(), new RoseSiameseFish(modelSet.bakeLayer(RoseSiameseFish.LAYER_LOCATION)));
        map.put(SCItems.SAGE_CATFISH.get(), new SageCatfish(modelSet.bakeLayer(SageCatfish.LAYER_LOCATION)));
        map.put(SCItems.SANDTAIL.get(), new Sandtail(modelSet.bakeLayer(Sandtail.LAYER_LOCATION)));
        map.put(SCItems.SCALDING_PIKE.get(), new ScaldingPike(modelSet.bakeLayer(ScaldingPike.LAYER_LOCATION)));
        map.put(SCItems.SCORCHFISH.get(), new Scorchfish(modelSet.bakeLayer(Scorchfish.LAYER_LOCATION)));
        map.put(SCItems.SEA_BASS.get(), new SeaBass(modelSet.bakeLayer(SeaBass.LAYER_LOCATION)));
        map.put(SCItems.SHROOMFISH.get(), new Shroomfish(modelSet.bakeLayer(Shroomfish.LAYER_LOCATION)));
        map.put(SCItems.SPOREFISH.get(), new Sporefish(modelSet.bakeLayer(Sporefish.LAYER_LOCATION)));
        map.put(SCItems.STONEFISH.get(), new Stonefish(modelSet.bakeLayer(Stonefish.LAYER_LOCATION)));
        map.put(SCItems.SUNFLOWER_CARP.get(), new SunflowerCarp(modelSet.bakeLayer(SunflowerCarp.LAYER_LOCATION)));
        map.put(SCItems.VESANI.get(), new Vesani(modelSet.bakeLayer(Vesani.LAYER_LOCATION)));
        map.put(SCItems.VOIDFIN.get(), new Voidfin(modelSet.bakeLayer(Voidfin.LAYER_LOCATION)));
        map.put(SCItems.WARD.get(), new Ward(modelSet.bakeLayer(Ward.LAYER_LOCATION)));
        map.put(SCItems.BRIGHT_AMETHYST_SNAPPER.get(), new BrightAmethystSnapper(modelSet.bakeLayer(BrightAmethystSnapper.LAYER_LOCATION)));
        map.put(SCItems.RIPPLE_CATFISH.get(), new RippleCatfish(modelSet.bakeLayer(RippleCatfish.LAYER_LOCATION)));
        map.put(SCItems.CERBERAY.get(), new Cerberay(modelSet.bakeLayer(Cerberay.LAYER_LOCATION)));
    }

    @Override
    public void extractRenderState(FishEntity entity, FishEntityRenderState state, float partialTicks)
    {
        super.extractRenderState(entity, state, partialTicks);
        state.fishStack = entity.getFish() == null ? ItemStack.EMPTY : entity.getFish();
        state.yRot = entity.getYRot(partialTicks);
        state.hasRedOverlay = entity.hurtTime > 0 || entity.deathTime > 0;
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
                new CaughtFishInfo(100, 100, 50, Rarity.COMMON)
        ).getScale();

        //todo needed?
        //poseStack.translate(be.x, be.y, be.z);

        //block centering
        poseStack.translate(offsetCenter.x, offsetCenter.y, offsetCenter.z);

        //scaling + pivot adjusting
        poseStack.translate(0, 1, 0);
        poseStack.scale(scale, -scale, scale);
        poseStack.translate(0, -1, 0);

        poseStack.rotate(Axis.YN.rotationDegrees(state.yRot + 180));

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

            Identifier rl = Starcatcher.rl("entity/fishes/" + BuiltInRegistries.ITEM.getKey(item).getPath());

            node.submitModel(
                    model, ir, poseStack, getGoldRendertype(rl, model, itemStack), ir.lightCoords,
                    LivingEntityRenderer.getOverlayCoords(ir, 0),
                    -1, null, ir.outlineColor
            );
        }
        else
        {
            poseStack.translate(0F, 1F, 0.0F);
            poseStack.rotate(Axis.YP.rotationDegrees(270.0F));
            poseStack.rotate(Axis.ZP.rotationDegrees(45.0F));
            //itemRenderer.appendItemLayers(itemStack, ItemDisplayContext.FIXED, packedLight,
            //OverlayTexture.NO_OVERLAY, poseStack, buffer, level, U.r.nextInt());
        }

    }

    public static RenderType getGoldRendertype(Identifier texture, EntityModel<FishEntityRenderState> model, ItemStack fishItem)
    {
        if (Rarity.isGolden(fishItem))
        {
            return GoldRenderer.INSTANCE.getOrCreateEntity(texture, RenderTypes::entityCutout).renderType;
        }
        return model.renderType(texture.withPrefix("textures/").withSuffix(".png"));
    }
}
