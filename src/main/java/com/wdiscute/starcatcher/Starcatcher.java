package com.wdiscute.starcatcher;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.blocks.ModBlockEntities;
import com.wdiscute.starcatcher.blocks.ModBlocks;
import com.wdiscute.starcatcher.bob.FishingBobModel;
import com.wdiscute.starcatcher.bob.FishingBobRenderer;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.fishentity.fishmodels.*;
import com.wdiscute.starcatcher.fishspotter.FishTrackerLayer;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import com.wdiscute.starcatcher.io.*;
import com.wdiscute.starcatcher.io.network.*;
import com.wdiscute.starcatcher.particles.FishingBitingLavaParticles;
import com.wdiscute.starcatcher.particles.FishingBitingParticles;
import com.wdiscute.starcatcher.particles.FishingNotificationParticles;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.rod.FishingRodScreen;
import com.wdiscute.starcatcher.tournament.StandScreen;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.List;
import java.util.Random;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Starcatcher.MOD_ID)
public class Starcatcher {
    public static final String MOD_ID = "starcatcher";

    public static final ResourceKey<Registry<FishProperties>> FISH_REGISTRY =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish"));

    public static final ResourceKey<Registry<TrophyProperties>> TROPHY_REGISTRY =
            ResourceKey.createRegistryKey(Starcatcher.rl("trophy"));

    public static final Random r = new Random();

    public static double truncatedNormal(double mean, double deviation) {
        while (true) {
            double value = mean + deviation * r.nextGaussian();
            if (value >= mean - deviation && value <= mean + deviation) {
                return value;
            }
        }
    }

    public static ResourceLocation rl(String s) {
        return ResourceLocation.fromNamespaceAndPath(Starcatcher.MOD_ID, s);
    }

    @OnlyIn(Dist.CLIENT)
    public static void fishCaughtToast(FishProperties fp, boolean newFish, int sizeCM, int weightCM) {
        if (newFish) Minecraft.getInstance().getToasts().addToast(new FishCaughtToast(fp));

        SettingsScreen.Units units = Config.UNIT.get();

        String size = units.getSizeAsString(sizeCM);
        String weight = units.getWeightAsString(weightCM);

        Minecraft.getInstance().player.displayClientMessage(
                Component.literal("")
                        .append(Component.translatable(fp.fish().getDelegate().value().getDescriptionId()))
                        .append(Component.literal(" - " + size + " - " + weight))
                , true);

        Minecraft.getInstance().gui.overlayMessageTime = 180;

    }


    public Starcatcher(IEventBus modEventBus, ModContainer modContainer) {
        ModCreativeModeTabs.register(modEventBus);
        ModItems.ITEMS_REGISTRY.register(modEventBus);
        ModItems.RODS_REGISTRY.register(modEventBus);
        ModItems.OTHERS_REGISTRY.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);
        ModParticles.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModDataAttachments.register(modEventBus);
        ModCriterionTriggers.REGISTRY.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC_SERVER);
    }


    @EventBusSubscriber(modid = MOD_ID)
    public static class ModEvents {

        @SubscribeEvent
        public static void levelTick(ServerTickEvent.Post event) {
            TournamentHandler.tick(event);
        }

        @SubscribeEvent
        public static void addRegistry(DataPackRegistryEvent.NewRegistry event) {
            event.dataPackRegistry(
                    FISH_REGISTRY, FishProperties.CODEC, FishProperties.CODEC,
                    builder -> builder.maxId(512));

            event.dataPackRegistry(
                    TROPHY_REGISTRY, TrophyProperties.CODEC, TrophyProperties.CODEC,
                    builder -> builder.maxId(256));
        }

        @SubscribeEvent
        public static void registerAttributed(EntityAttributeCreationEvent event) {
            event.put(ModEntities.FISH.get(), FishEntity.createAttributes().build());
        }

        @SubscribeEvent
        public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playToClient(
                    FishingPayload.TYPE,
                    FishingPayload.STREAM_CODEC,
                    FishingPayload::handle
            );

            registrar.playToServer(
                    FishingCompletedPayload.TYPE,
                    FishingCompletedPayload.STREAM_CODEC,
                    FishingCompletedPayload::handle
            );

            registrar.playToClient(
                    FishCaughtPayload.TYPE,
                    FishCaughtPayload.STREAM_CODEC,
                    FishCaughtPayload::handle
            );

            registrar.playToServer(
                    FPsSeenPayload.TYPE,
                    FPsSeenPayload.STREAM_CODEC,
                    FPsSeenPayload::handle
            );

            registrar.playToClient(
                    TournamentDataToClientPayload.TYPE,
                    TournamentDataToClientPayload.STREAM_CODEC,
                    TournamentDataToClientPayload::handle
            );
        }

    }

    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ModClientEvents {

        @SubscribeEvent
        public static void trophyTooltip(ItemTooltipEvent event) {
            List<Component> comp = event.getToolTip();
            ItemStack stack = event.getItemStack();

            if (stack.has(ModDataComponents.SIZE_AND_WEIGHT)) {
                SizeAndWeight sw = stack.get(ModDataComponents.SIZE_AND_WEIGHT);

                SettingsScreen.Units units = Config.UNIT.get();

                String size = units.getSizeAsString(sw.sizeInCentimeters());
                String weight = units.getWeightAsString(sw.weightInGrams());

                comp.add(1, Component.literal(size + " - " + weight).withColor(0x888888));
            }

            if (stack.has(ModDataComponents.TROPHY)) {
                TrophyProperties tp = stack.get(ModDataComponents.TROPHY);

                if (tp.trophyType() == TrophyProperties.TrophyType.TROPHY)
                    if (event.getFlags().hasShiftDown()) {
                        comp.add(Component.translatable("tooltip.libtooltips.generic.shift_down"));
                        comp.add(Component.translatable("tooltip.libtooltips.generic.empty"));
                        comp.add(Component.translatable("tooltip.starcatcher.trophy.0"));
                        comp.add(Component.translatable("tooltip.starcatcher.trophy.1"));

                        List<Component> list = new java.util.ArrayList<>();

                        //all
                        if (tp.all().total() != 0) list.add(Tooltips.decodeString(
                                I18n.get("tooltip.starcatcher.trophy.total")
                                        .replace("&", tp.all().total() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.all"))
                        ));

                        if (tp.all().unique() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.unique")
                                        .replace("&", tp.all().unique() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.all"))));

                        //common
                        if (tp.common().total() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.total")
                                        .replace("&", tp.common().total() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.common"))));

                        if (tp.common().unique() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.unique")
                                        .replace("&", tp.common().unique() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.common"))));

                        //uncommon
                        if (tp.uncommon().total() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.total")
                                        .replace("&", tp.uncommon().total() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.uncommon"))));

                        if (tp.uncommon().unique() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.unique")
                                        .replace("&", tp.uncommon().unique() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.uncommon"))));

                        //rare
                        if (tp.rare().total() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.total")
                                        .replace("&", tp.rare().total() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.rare"))));

                        if (tp.rare().unique() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.unique")
                                        .replace("&", tp.rare().unique() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.rare"))));

                        //epic
                        if (tp.epic().total() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.total")
                                        .replace("&", tp.epic().total() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.epic"))));

                        if (tp.epic().unique() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.unique")
                                        .replace("&", tp.epic().unique() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.epic"))));

                        //legendary
                        if (tp.legendary().total() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.total")
                                        .replace("&", tp.legendary().total() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.legendary"))));

                        if (tp.legendary().unique() != 0) list.add(
                                Tooltips.decodeString(I18n.get("tooltip.starcatcher.trophy.unique")
                                        .replace("&", tp.legendary().unique() + "")
                                        .replace("$", I18n.get("tooltip.starcatcher.trophy.legendary"))));

                        if (list.size() == 1) {
                            comp.add(Component.translatable("tooltip.starcatcher.trophy.once")
                                    .append(list.getFirst())
                                    .append(Component.translatable("tooltip.starcatcher.trophy.have_been_caught")));
                        } else {
                            comp.add(Component.translatable("tooltip.starcatcher.trophy.2"));
                            comp.addAll(list);
                        }

                    } else {
                        comp.add(Component.translatable("tooltip.libtooltips.generic.shift_up"));
                    }

            }
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.FISHING_BOB.get(), FishingBobRenderer::new);
            EntityRenderers.register(ModEntities.BOTTLE.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntities.FISH.get(), FishRenderer::new);
            ModItemProperties.addCustomItemProperties();
        }

        @SubscribeEvent
        public static void FishSpotterLayer(RegisterGuiLayersEvent event) {
            event.registerAboveAll(Starcatcher.rl("fish_tracker"), new FishTrackerLayer());
        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.FISHING_NOTIFICATION.get(), FishingNotificationParticles.Provider::new);
            event.registerSpriteSet(ModParticles.FISHING_BITING.get(), FishingBitingParticles.Provider::new);
            event.registerSpriteSet(ModParticles.FISHING_BITING_LAVA.get(), FishingBitingLavaParticles.Provider::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.FISHING_ROD_MENU.get(), FishingRodScreen::new);
            event.register(ModMenuTypes.STAND_MENU.get(), StandScreen::new);
        }

        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(FishingBobModel.LAYER_LOCATION, FishingBobModel::createBodyLayer);
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
            event.registerLayerDefinition(WillowBream.LAYER_LOCATION, WillowBream::createBodyLayer);
            event.registerLayerDefinition(WinteryPike.LAYER_LOCATION, WinteryPike::createBodyLayer);
        }

    }

}
