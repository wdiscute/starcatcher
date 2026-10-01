package com.wdiscute.starcatcher.fishentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.fishmodels.*;
import com.wdiscute.starcatcher.registry.SCRenderTypes;
import com.wdiscute.starcatcher.shaders.GoldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FishRenderer extends MobRenderer<FishEntity, EntityModel<FishEntity>>
{
    public static Map<Item, EntityModel<FishEntity>> map = new HashMap<>();

    public FishRenderer(EntityRendererProvider.Context context)
    {
        super(context, null, 0.25f);
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
        map.put(SCItems.AMETHYSTBACK.get(), new Amethystback<>(modelSet.bakeLayer(Amethystback.LAYER_LOCATION)));
        map.put(SCItems.AQUAMARINE_PIKE.get(), new AquamarinePike<>(modelSet.bakeLayer(AquamarinePike.LAYER_LOCATION)));
        map.put(SCItems.BLOSSOMFISH.get(), new Blossomfish<>(modelSet.bakeLayer(Blossomfish.LAYER_LOCATION)));
        map.put(SCItems.BLUE_ICE_PIKE.get(), new BlueIcePike<>(modelSet.bakeLayer(BlueIcePike.LAYER_LOCATION)));
        map.put(SCItems.BLUEGIGI.get(), new Bluegigi<>(modelSet.bakeLayer(Bluegigi.LAYER_LOCATION)));
        map.put(SCItems.CHORUS_MINNOW.get(), new ChorusMinnow<>(modelSet.bakeLayer(ChorusMinnow.LAYER_LOCATION)));
        map.put(SCItems.CRYOSPINE.get(), new Cryospine<>(modelSet.bakeLayer(Cryospine.LAYER_LOCATION)));
        map.put(SCItems.CRYSTALBACK_STURGEON.get(), new CrystalbackSturgeon<>(modelSet.bakeLayer(CrystalbackSturgeon.LAYER_LOCATION)));
        map.put(SCItems.DARK_AMETHYST_SNAPPER.get(), new DarkAmethystSnapper<>(modelSet.bakeLayer(DarkAmethystSnapper.LAYER_LOCATION)));
        map.put(SCItems.DREAMLINER.get(), new Dreamliner<>(modelSet.bakeLayer(Dreamliner.LAYER_LOCATION)));
        map.put(SCItems.DRIPFIN.get(), new Dripfin<>(modelSet.bakeLayer(Dripfin.LAYER_LOCATION)));
        map.put(SCItems.END_GLOW.get(), new EndGlow<>(modelSet.bakeLayer(EndGlow.LAYER_LOCATION)));
        map.put(SCItems.FOSSILIZED_ANGELFISH.get(), new FossilizedAngelfish<>(modelSet.bakeLayer(FossilizedAngelfish.LAYER_LOCATION)));
        map.put(SCItems.GARNET_MACKEREL.get(), new GarnetMackerel<>(modelSet.bakeLayer(GarnetMackerel.LAYER_LOCATION)));
        map.put(SCItems.GLIMMERGILL.get(), new Glimmergill<>(modelSet.bakeLayer(Glimmergill.LAYER_LOCATION)));
        map.put(SCItems.GLOWING_DARK.get(), new GlowingDark<>(modelSet.bakeLayer(GlowingDark.LAYER_LOCATION)));
        map.put(SCItems.GLOWSTONE_PUFFERFISH.get(), new GlowstonePufferfish<>(modelSet.bakeLayer(GlowstonePufferfish.LAYER_LOCATION)));
        map.put(SCItems.GLOWSTONE_SEEKER.get(), new GlowstoneSeeker<>(modelSet.bakeLayer(GlowstoneSeeker.LAYER_LOCATION)));
        map.put(SCItems.GOLD_FAN.get(), new GoldFan<>(modelSet.bakeLayer(GoldFan.LAYER_LOCATION)));
        map.put(SCItems.LILAC_MINNOW.get(), new LilacMinnow<>(modelSet.bakeLayer(LilacMinnow.LAYER_LOCATION)));
        map.put(SCItems.LIVID_BAMBOO.get(), new LividBamboo<>(modelSet.bakeLayer(LividBamboo.LAYER_LOCATION)));
        map.put(SCItems.MOSSFIN.get(), new Mossfin<>(modelSet.bakeLayer(Mossfin.LAYER_LOCATION)));
        map.put(SCItems.MOTHFISH.get(), new Mothfish<>(modelSet.bakeLayer(Mothfish.LAYER_LOCATION)));
        map.put(SCItems.PALE_CARP.get(), new PaleCarp<>(modelSet.bakeLayer(PaleCarp.LAYER_LOCATION)));
        map.put(SCItems.PEAKDWELLER.get(), new Peakdweller<>(modelSet.bakeLayer(Peakdweller.LAYER_LOCATION)));
        map.put(SCItems.PETAL_BASS.get(), new PetalBass<>(modelSet.bakeLayer(PetalBass.LAYER_LOCATION)));
        map.put(SCItems.PURPLE_CARP.get(), new PurpleCarp<>(modelSet.bakeLayer(PurpleCarp.LAYER_LOCATION)));
        map.put(SCItems.RAINFIN.get(), new Rainfin<>(modelSet.bakeLayer(Rainfin.LAYER_LOCATION)));
        map.put(SCItems.SHADOWFIN.get(), new Shadowfin<>(modelSet.bakeLayer(Shadowfin.LAYER_LOCATION)));

        //10/oct update
        map.put(SCItems.AURORA.get(), new Aurora<>(modelSet.bakeLayer(Aurora.LAYER_LOCATION)));
        map.put(SCItems.AZURE_CRYSTALBACK_MINNOW.get(), new AzureCrystalbackMinnow<>(modelSet.bakeLayer(AzureCrystalbackMinnow.LAYER_LOCATION)));
        map.put(SCItems.CLOUDFIN.get(), new Cloudfin<>(modelSet.bakeLayer(Cloudfin.LAYER_LOCATION)));
        map.put(SCItems.DEEPSLATEFISH.get(), new Deepslatefish<>(modelSet.bakeLayer(Deepslatefish.LAYER_LOCATION)));
        map.put(SCItems.JOEL.get(), new Joel<>(modelSet.bakeLayer(Joel.LAYER_LOCATION)));
        map.put(SCItems.OASIS_STURGEON.get(), new OasisSturgeon<>(modelSet.bakeLayer(OasisSturgeon.LAYER_LOCATION)));
        map.put(SCItems.ROCKGILL.get(), new Rockgill<>(modelSet.bakeLayer(Rockgill.LAYER_LOCATION)));
        map.put(SCItems.ROSE_SIAMESE_FISH.get(), new RoseSiameseFish<>(modelSet.bakeLayer(RoseSiameseFish.LAYER_LOCATION)));
        map.put(SCItems.SAGE_CATFISH.get(), new SageCatfish<>(modelSet.bakeLayer(SageCatfish.LAYER_LOCATION)));
        map.put(SCItems.SANDTAIL.get(), new Sandtail<>(modelSet.bakeLayer(Sandtail.LAYER_LOCATION)));
        map.put(SCItems.SCALDING_PIKE.get(), new ScaldingPike<>(modelSet.bakeLayer(ScaldingPike.LAYER_LOCATION)));
        map.put(SCItems.SCORCHFISH.get(), new Scorchfish<>(modelSet.bakeLayer(Scorchfish.LAYER_LOCATION)));
        map.put(SCItems.SEA_BASS.get(), new SeaBass<>(modelSet.bakeLayer(SeaBass.LAYER_LOCATION)));
        map.put(SCItems.SHROOMFISH.get(), new Shroomfish<>(modelSet.bakeLayer(Shroomfish.LAYER_LOCATION)));
        map.put(SCItems.SPOREFISH.get(), new Sporefish<>(modelSet.bakeLayer(Sporefish.LAYER_LOCATION)));
        map.put(SCItems.STONEFISH.get(), new Stonefish<>(modelSet.bakeLayer(Stonefish.LAYER_LOCATION)));
        map.put(SCItems.SUNFLOWER_CARP.get(), new SunflowerCarp<>(modelSet.bakeLayer(SunflowerCarp.LAYER_LOCATION)));
        map.put(SCItems.VESANI.get(), new Vesani<>(modelSet.bakeLayer(Vesani.LAYER_LOCATION)));
        map.put(SCItems.VOIDFIN.get(), new Voidfin<>(modelSet.bakeLayer(Voidfin.LAYER_LOCATION)));
        map.put(SCItems.WARD.get(), new Ward<>(modelSet.bakeLayer(Ward.LAYER_LOCATION)));
        map.put(SCItems.BRIGHT_AMETHYST_SNAPPER.get(), new BrightAmethystSnapper<>(modelSet.bakeLayer(BrightAmethystSnapper.LAYER_LOCATION)));
        map.put(SCItems.RIPPLE_CATFISH.get(), new RippleCatfish<>(modelSet.bakeLayer(RippleCatfish.LAYER_LOCATION)));
    }

    @Override
    public void render(FishEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        ItemStack fish = entity.getFish();
        model = map.get(fish.getItem());

        if (fish.isEmpty())
            return;

        if (model == null)
        {
            model = map.get(SCItems.AGAVE_BREAM.asItem());
            if (!entity.hasWarned)
            {
                entity.hasWarned = true;
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable(entity.getFish().getDescriptionId()).append(Component.literal(" does not have a model made yet! Using agave bream model instead")));
            }
        }

        poseStack.pushPose();
        poseStack.translate(0, -0.2, 0);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        poseStack.popPose();
    }

    @Override
    protected @Nullable RenderType getRenderType(FishEntity livingEntity, boolean bodyVisible, boolean translucent, boolean glowing)
    {
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
    protected void setupRotations(FishEntity entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale)
    {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);

    }

    public static void renderFishFromItem(ItemRenderer itemRenderer, Map<Item, EntityModel<FishEntity>> map, ItemStack itemStack, MultiBufferSource buffer, PoseStack poseStack, int packedLight, int overlay, Level level)
    {
        if (map.containsKey(itemStack.getItem()))
        {
            Item item = itemStack.getItem();
            EntityModel<FishEntity> model = map.get(item);

            VertexConsumer vertexConsumer;
            if (Rarity.isGolden(itemStack))
                vertexConsumer = getGlintVertexConsumer(buffer, Starcatcher.rl("entity/fishes/" + BuiltInRegistries.ITEM.getKey(item).getPath()), model, itemStack);
            else
                vertexConsumer = buffer.getBuffer(getGoldRendertype(Starcatcher.rl("entity/fishes/" + BuiltInRegistries.ITEM.getKey(item).getPath()), model, itemStack));

            model.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay);
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

    public static VertexConsumer getGlintVertexConsumer(MultiBufferSource buffer, ResourceLocation texture, EntityModel<FishEntity> model, ItemStack fishItem)
    {
        VertexConsumer glint = buffer.getBuffer(SCRenderTypes.RENDERTYPE_GOLD_FISH_GLINT_ENTITY);
        VertexConsumer textureConsumer = buffer.getBuffer(getGoldRendertype(texture, model, fishItem));

        return VertexMultiConsumer.create(textureConsumer, glint);
    }
}
