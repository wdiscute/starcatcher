package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobRenderer;
import com.wdiscute.starcatcher.bobentity.tackles.*;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.fishentity.fishmodels.*;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.locators.FishRadarLayer;
import com.wdiscute.starcatcher.locators.FishTrackerLayer;
import com.wdiscute.starcatcher.minigame.KonamiDetector;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.blocks.aquarium.AquariumRenderer;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockRenderer;
import com.wdiscute.starcatcher.blocks.display.DisplayBookModel;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxRenderer;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxScreen;
import com.wdiscute.starcatcher.registry.items.BucketTooltipRenderer;
import com.wdiscute.starcatcher.registry.items.RodSlotTooltipRenderer;
import com.wdiscute.starcatcher.registry.items.StarcaughtBucket;
import com.wdiscute.starcatcher.particles.FishingBitingLavaParticles;
import com.wdiscute.starcatcher.particles.FishingBitingParticles;
import com.wdiscute.starcatcher.particles.FishingNotificationParticles;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.compat.curios.CuriosEvents;
import com.wdiscute.starcatcher.registry.items.StarcatcherFishingRodItem;
import com.wdiscute.starcatcher.shaders.BakedModelRemapper;
import com.wdiscute.starcatcher.shaders.GoldRenderer;
import com.wdiscute.starcatcher.tournament.StandScreen;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT)
public class SCClientEvents
{
    @SubscribeEvent
    public static void keyPressed(InputEvent.Key event)
    {
        if (event.getAction() == 1)
            KonamiDetector.keyPressed(event.getKey());

        if (SCKeymappings.EXPAND_TOURNAMENT.consumeClick())
            TournamentOverlay.expandedType = TournamentOverlay.expandedType.next();

        if (SCKeymappings.OPEN_GUIDE.consumeClick() && SCConfig.ALLOW_GUIDE_KEYBIND.get())
            Minecraft.getInstance().setScreen(new FishingGuideScreen(false, null));

    }

    @SubscribeEvent
    public static void onAssetReload(RegisterClientReloadListenersEvent event)
    {
        event.registerReloadListener((ResourceManagerReloadListener) resourceManager ->
        {
            GoldRenderer.INSTANCE.close();
            BakedModelRemapper.REMAPPED_MODELS.clear();
        });
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(SCBlockEntities.DISPLAY.get(), DisplayBlockRenderer::new);
        event.registerBlockEntityRenderer(SCBlockEntities.AQUARIUM.get(), AquariumRenderer::new);
        //event.registerBlockEntityRenderer(ModBlockEntities.TACKLE_BOX.get(), TackleBoxRenderer::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        EntityRenderers.register(SCEntities.FISHING_BOB.get(), FishingBobRenderer::new);
        EntityRenderers.register(SCEntities.BROKEN_BOTTLE.get(), ThrownItemRenderer::new);
        EntityRenderers.register(SCEntities.BOTTLED_LETTER.get(), ThrownItemRenderer::new);
        EntityRenderers.register(SCEntities.FISH.get(), FishRenderer::new);
        event.enqueueWork(SCItemProperties::addCustomItemProperties);

        if (ModList.get().isLoaded("curios"))
        {
            CuriosEvents.registerRenderers();
        }
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event)
    {
        event.registerAboveAll(Starcatcher.rl("tracked_fish"), new FishTrackerLayer());
        event.registerAboveAll(Starcatcher.rl("fish_radar"), new FishRadarLayer());
        event.registerAboveAll(Starcatcher.rl("tournament"), new TournamentOverlay());
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(SCParticles.VALLEY_NOTIFICATION.get(), FishingNotificationParticles.Provider::new);
        event.registerSpriteSet(SCParticles.FISHING_BITING.get(), FishingBitingParticles.Provider::new);
        event.registerSpriteSet(SCParticles.FISHING_BITING_LAVA.get(), FishingBitingLavaParticles.Provider::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(SCMenuTypes.STAND_MENU.get(), StandScreen::new);
        event.register(SCMenuTypes.TACKLE_BOX.get(), TackleBoxScreen::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        //tackle skins
        event.registerLayerDefinition(BaseModel.LAYER_LOCATION, BaseModel::createBodyLayer);
        event.registerLayerDefinition(ClearModel.LAYER_LOCATION, ClearModel::createBodyLayer);
        event.registerLayerDefinition(ColorfulModel.LAYER_LOCATION, ColorfulModel::createBodyLayer);
        event.registerLayerDefinition(FrogModel.LAYER_LOCATION, FrogModel::createBodyLayer);
        event.registerLayerDefinition(KimbeModel.LAYER_LOCATION, KimbeModel::createBodyLayer);
        event.registerLayerDefinition(KingModel.LAYER_LOCATION, KingModel::createBodyLayer);
        event.registerLayerDefinition(PearlModel.LAYER_LOCATION, PearlModel::createBodyLayer);
        event.registerLayerDefinition(ValleyModel.LAYER_LOCATION, ValleyModel::createBodyLayer);
        event.registerLayerDefinition(SurvivorModel.LAYER_LOCATION, SurvivorModel::createBodyLayer);

        //tackle box
        event.registerLayerDefinition(TackleBoxRenderer.LAYER_LOCATION, TackleBoxRenderer::createBodyLayer);

        //book model
        event.registerLayerDefinition(DisplayBookModel.LAYER_LOCATION, DisplayBookModel::createBodyLayer);

        //fishes
        event.registerLayerDefinition(AgaveBream.LAYER_LOCATION, AgaveBream::createBodyLayer);
        event.registerLayerDefinition(BigeyeTuna.LAYER_LOCATION, BigeyeTuna::createBodyLayer);
        event.registerLayerDefinition(Boreal.LAYER_LOCATION, Boreal::createBodyLayer);
        event.registerLayerDefinition(CactiFish.LAYER_LOCATION, CactiFish::createBodyLayer);
        event.registerLayerDefinition(Charfish.LAYER_LOCATION, Charfish::createBodyLayer);
        event.registerLayerDefinition(CrystalbackBoreal.LAYER_LOCATION, CrystalbackBoreal::createBodyLayer);
        event.registerLayerDefinition(CrystalbackMinnow.LAYER_LOCATION, CrystalbackMinnow::createBodyLayer);
        event.registerLayerDefinition(DeepjawHerring.LAYER_LOCATION, DeepjawHerring::createBodyLayer);
        event.registerLayerDefinition(DownfallBream.LAYER_LOCATION, DownfallBream::createBodyLayer);
        event.registerLayerDefinition(Driftfin.LAYER_LOCATION, Driftfin::createBodyLayer);
        event.registerLayerDefinition(DriftingBream.LAYER_LOCATION, DriftingBream::createBodyLayer);
        event.registerLayerDefinition(DusktailSnapper.LAYER_LOCATION, DusktailSnapper::createBodyLayer);
        event.registerLayerDefinition(LilySnapper.LAYER_LOCATION, LilySnapper::createBodyLayer);
        event.registerLayerDefinition(PinkKoi.LAYER_LOCATION, PinkKoi::createBodyLayer);
        event.registerLayerDefinition(SilverveilPerch.LAYER_LOCATION, SilverveilPerch::createBodyLayer);
        event.registerLayerDefinition(SludgeCatfish.LAYER_LOCATION, SludgeCatfish::createBodyLayer);
        event.registerLayerDefinition(Whiteveil.LAYER_LOCATION, Whiteveil::createBodyLayer);
        event.registerLayerDefinition(WinteryPike.LAYER_LOCATION, WinteryPike::createBodyLayer);
        event.registerLayerDefinition(CrystalbackTrout.LAYER_LOCATION, CrystalbackTrout::createBodyLayer);
        event.registerLayerDefinition(Embergill.LAYER_LOCATION, Embergill::createBodyLayer);
        event.registerLayerDefinition(FrostgillChub.LAYER_LOCATION, FrostgillChub::createBodyLayer);
        event.registerLayerDefinition(FrostjawTrout.LAYER_LOCATION, FrostjawTrout::createBodyLayer);
        event.registerLayerDefinition(HollowbellyDarter.LAYER_LOCATION, HollowbellyDarter::createBodyLayer);
        event.registerLayerDefinition(IcetoothSturgeon.LAYER_LOCATION, IcetoothSturgeon::createBodyLayer);
        event.registerLayerDefinition(MistbackChub.LAYER_LOCATION, MistbackChub::createBodyLayer);
        event.registerLayerDefinition(BlueCrystalFin.LAYER_LOCATION, BlueCrystalFin::createBodyLayer);
        event.registerLayerDefinition(Carpenjoe.LAYER_LOCATION, Carpenjoe::createBodyLayer);
        event.registerLayerDefinition(Elderscale.LAYER_LOCATION, Elderscale::createBodyLayer);
        event.registerLayerDefinition(GhostlyPike.LAYER_LOCATION, GhostlyPike::createBodyLayer);
        event.registerLayerDefinition(IronjarHerring.LAYER_LOCATION, IronjarHerring::createBodyLayer);
        event.registerLayerDefinition(MirageCarp.LAYER_LOCATION, MirageCarp::createBodyLayer);
        event.registerLayerDefinition(PetaldriftCarp.LAYER_LOCATION, PetaldriftCarp::createBodyLayer);
        event.registerLayerDefinition(BlueHerring.LAYER_LOCATION, BlueHerring::createBodyLayer);
        event.registerLayerDefinition(LightningBass.LAYER_LOCATION, LightningBass::createBodyLayer);
        event.registerLayerDefinition(LushPike.LAYER_LOCATION, LushPike::createBodyLayer);
        event.registerLayerDefinition(MagmaFish.LAYER_LOCATION, MagmaFish::createBodyLayer);
        event.registerLayerDefinition(Morganite.LAYER_LOCATION, Morganite::createBodyLayer);
        event.registerLayerDefinition(PalePinfish.LAYER_LOCATION, PalePinfish::createBodyLayer);
        event.registerLayerDefinition(Pinfish.LAYER_LOCATION, Pinfish::createBodyLayer);
        event.registerLayerDefinition(Pyrotrout.LAYER_LOCATION, Pyrotrout::createBodyLayer);
        event.registerLayerDefinition(Sculkfish.LAYER_LOCATION, Sculkfish::createBodyLayer);
        event.registerLayerDefinition(SilverfinPike.LAYER_LOCATION, SilverfinPike::createBodyLayer);
        event.registerLayerDefinition(VividMoss.LAYER_LOCATION, VividMoss::createBodyLayer);
        event.registerLayerDefinition(Willish.LAYER_LOCATION, Willish::createBodyLayer);
        event.registerLayerDefinition(YellowstoneFish.LAYER_LOCATION, YellowstoneFish::createBodyLayer);
        event.registerLayerDefinition(Voidbiter.LAYER_LOCATION, Voidbiter::createBodyLayer);
        event.registerLayerDefinition(Obidontiee.LAYER_LOCATION, Obidontiee::createBodyLayer);
        event.registerLayerDefinition(RedscaledTuna.LAYER_LOCATION, RedscaledTuna::createBodyLayer);
        event.registerLayerDefinition(SunSeekingCarp.LAYER_LOCATION, SunSeekingCarp::createBodyLayer);
        event.registerLayerDefinition(Suneater.LAYER_LOCATION, Suneater::createBodyLayer);
        event.registerLayerDefinition(SunnySturgeon.LAYER_LOCATION, SunnySturgeon::createBodyLayer);
        event.registerLayerDefinition(TheQuarrish.LAYER_LOCATION, TheQuarrish::createBodyLayer);
        event.registerLayerDefinition(ThunderBass.LAYER_LOCATION, ThunderBass::createBodyLayer);
        event.registerLayerDefinition(TwilightKoi.LAYER_LOCATION, TwilightKoi::createBodyLayer);
        event.registerLayerDefinition(WillowBream.LAYER_LOCATION, WillowBream::createBodyLayer);
        event.registerLayerDefinition(Cerberay.LAYER_LOCATION, Cerberay::createBodyLayer);

    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
    {
        event.register(SCKeymappings.MINIGAME_HIT);
        event.register(SCKeymappings.EXPAND_TOURNAMENT);
        event.register(SCKeymappings.OPEN_GUIDE);
    }

    @SubscribeEvent
    public static void onRegisterTooltips(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(StarcaughtBucket.BucketTooltip.class, BucketTooltipRenderer::new);
        event.register(StarcatcherFishingRodItem.RodSlotTooltip.class, RodSlotTooltipRenderer::new);
    }

}
