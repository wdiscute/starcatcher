package com.wdiscute.starcatcher;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.bob.FishingBobModel;
import com.wdiscute.starcatcher.bob.FishingBobRenderer;
import com.wdiscute.starcatcher.fishspotter.FishTrackerLayer;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import com.wdiscute.starcatcher.networkandcodecs.*;
import com.wdiscute.starcatcher.particles.FishingBitingParticles;
import com.wdiscute.starcatcher.particles.FishingNotificationParticles;
import com.wdiscute.starcatcher.rod.FishingRodScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
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

import java.util.List;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Starcatcher.MOD_ID)
public class Starcatcher
{
    public static final String MOD_ID = "starcatcher";

    public static final ResourceKey<Registry<FishProperties>> FISH_REGISTRY =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish"));

    public static final ResourceKey<Registry<TrophyProperties>> TROPHY_REGISTRY =
            ResourceKey.createRegistryKey(Starcatcher.rl("trophy"));

    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation rl(String s)
    {
        return ResourceLocation.fromNamespaceAndPath(Starcatcher.MOD_ID, s);
    }

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
                    FISH_REGISTRY, FishProperties.CODEC, FishProperties.CODEC,
                    builder -> builder.maxId(512));

            event.dataPackRegistry(
                    TROPHY_REGISTRY, TrophyProperties.CODEC, TrophyProperties.CODEC,
                    builder -> builder.maxId(256));
        }

        @SubscribeEvent
        public static void registerAttributed(EntityAttributeCreationEvent event)
        {
            event.put(ModEntities.FISH.get(), FishEntity.createAttributes().build());
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

    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ModClientEvents
    {

        @SubscribeEvent
        public static void trophyTooltip(ItemTooltipEvent event)
        {
            List<Component> tooltipComponents = event.getToolTip();
            ItemStack stack = event.getItemStack();

            if(stack.has(ModDataComponents.TROPHY))
            {
                TrophyProperties tp = stack.get(ModDataComponents.TROPHY);

                if (event.getFlags().hasShiftDown() && tp.trophyType() == TrophyProperties.TrophyType.TROPHY)
                {
                    tooltipComponents.add(Component.translatable("tooltip.libtooltips.generic.shift_down"));
                    tooltipComponents.add(Component.translatable("tooltip.libtooltips.generic.empty"));
                    tooltipComponents.add(Component.translatable("tooltip.starcatcher.trophy.0"));
                    tooltipComponents.add(Component.translatable("tooltip.starcatcher.trophy.1"));

                    if(tp.totalCaughtCount() != 0 && tp.uniqueFishCount() != 0)
                    {
                        String u = I18n.get("tooltip.starcatcher.trophy.both.0")
                                .replace("&", tp.uniqueFishCount() + "");
                        tooltipComponents.add(Tooltips.decodeTranslationKey(u));

                        String t = I18n.get("tooltip.starcatcher.trophy.both.1")
                                .replace("&", tp.totalCaughtCount() + "");
                        tooltipComponents.add(Tooltips.decodeTranslationKey(t));
                    }

                    if(tp.totalCaughtCount() == 0 && tp.uniqueFishCount() != 0)
                    {
                        String s = I18n.get("tooltip.starcatcher.trophy.unique")
                                .replace("&", tp.uniqueFishCount() + "");
                        tooltipComponents.add(Tooltips.decodeTranslationKey(s));
                    }

                    if(tp.totalCaughtCount() != 0 && tp.uniqueFishCount() == 0)
                    {
                        String s = I18n.get("tooltip.starcatcher.trophy.total")
                                .replace("&", tp.totalCaughtCount() + "");
                        tooltipComponents.add(Tooltips.decodeTranslationKey(s));
                    }

                }
                else
                {
                    tooltipComponents.add(Component.translatable("tooltip.libtooltips.generic.shift_up"));
                }

            }
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.FISHING_BOB.get(), FishingBobRenderer::new);
            EntityRenderers.register(ModEntities.FISH.get(), FishRenderer::new);
            ModItemProperties.addCustomItemProperties();
        }

        @SubscribeEvent
        public static void FishSpotterLayer(RegisterGuiLayersEvent event)
        {
            event.registerAboveAll(Starcatcher.rl("fish_tracker"), new FishTrackerLayer());
        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event)
        {
            event.registerSpriteSet(ModParticles.FISHING_NOTIFICATION.get(), FishingNotificationParticles.Provider::new);
            event.registerSpriteSet(ModParticles.FISHING_BITING.get(), FishingBitingParticles.Provider::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event)
        {
            event.register(ModMenuTypes.FISHING_ROD_MENU.get(), FishingRodScreen::new);
        }


        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
        {
            event.registerLayerDefinition(FishingBobModel.LAYER_LOCATION, FishingBobModel::createBodyLayer);
        }

    }

}
