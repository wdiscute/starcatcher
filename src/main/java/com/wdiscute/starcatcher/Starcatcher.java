package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.fishingbob.FishingBobModel;
import com.wdiscute.starcatcher.fishingbob.FishingBobRenderer;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import com.wdiscute.starcatcher.networkandstuff.FishProperties;
import com.wdiscute.starcatcher.networkandstuff.ModDataAttachments;
import com.wdiscute.starcatcher.networkandstuff.PayloadReceiver;
import com.wdiscute.starcatcher.networkandstuff.Payloads;
import com.wdiscute.starcatcher.particles.FishingBitingParticles;
import com.wdiscute.starcatcher.particles.FishingNotificationParticles;
import com.wdiscute.starcatcher.rod.FishTrackerLayer;
import com.wdiscute.starcatcher.rod.FishingRodScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Starcatcher.MOD_ID)
public class Starcatcher
{
    public static final String MOD_ID = "starcatcher";

    public static final ResourceKey<Registry<FishProperties>> FISH_REGISTRY =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish"));

    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation rl(String s)
    {
        return ResourceLocation.fromNamespaceAndPath(Starcatcher.MOD_ID, s);
    }

    public static float hue;

    @OnlyIn(Dist.CLIENT)
    public static void fishCaughtToast(FishProperties fp)
    {
        Minecraft.getInstance().getToasts().addToast(new FishCaughtToast(fp));
    }

    public Starcatcher(IEventBus modEventBus, ModContainer modContainer)
    {
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);
        ModParticles.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModDataAttachments.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    @EventBusSubscriber(modid = MOD_ID)
    public static class ModEvents
    {

        @SubscribeEvent
        public static void addRegistry(DataPackRegistryEvent.NewRegistry event)
        {
            event.dataPackRegistry(
                    FISH_REGISTRY,
                    FishProperties.CODEC,
                    FishProperties.CODEC,
                    builder -> builder.maxId(256));
        }

        @SubscribeEvent
        public static void registerAttributed(EntityAttributeCreationEvent event)
        {
            event.put(ModEntities.FISH.get(), FishEntity.createAttributes().build());
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.FISHING_BOB.get(), FishingBobRenderer::new);
            EntityRenderers.register(ModEntities.FISH.get(), FishRenderer::new);
            ModItemProperties.addCustomItemProperties();
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void FishSpotterLayer(RegisterGuiLayersEvent event)
        {
            event.registerAboveAll(Starcatcher.rl("fish_tracker"), new FishTrackerLayer());
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event)
        {
            event.registerSpriteSet(ModParticles.FISHING_NOTIFICATION.get(), FishingNotificationParticles.Provider::new);
            event.registerSpriteSet(ModParticles.FISHING_BITING.get(), FishingBitingParticles.Provider::new);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event)
        {
            event.register(ModMenuTypes.FISHING_ROD_MENU.get(), FishingRodScreen::new);
        }


        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
        {
            event.registerLayerDefinition(FishingBobModel.LAYER_LOCATION, FishingBobModel::createBodyLayer);
        }


        @SubscribeEvent
        public static void registerPayloads(final RegisterPayloadHandlersEvent event)
        {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playToClient(
                    Payloads.FishingPayload.TYPE,
                    Payloads.FishingPayload.STREAM_CODEC,
                    PayloadReceiver::receiveFishingClient
            );

            registrar.playToServer(
                    Payloads.FishingCompletedPayload.TYPE,
                    Payloads.FishingCompletedPayload.STREAM_CODEC,
                    PayloadReceiver::receiveFishingCompletedServer
            );

            registrar.playToClient(
                    Payloads.FishCaughtPayload.TYPE,
                    Payloads.FishCaughtPayload.STREAM_CODEC,
                    PayloadReceiver::receiveFishCaught
            );

            registrar.playToServer(
                    Payloads.FPsSeen.TYPE,
                    Payloads.FPsSeen.STREAM_CODEC,
                    PayloadReceiver::receiveFPsSeen
            );

        }

    }

}
