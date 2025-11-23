package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.blocks.ModBlockEntities;
import com.wdiscute.starcatcher.blocks.ModBlocks;
import com.wdiscute.starcatcher.bob.FishingBobModel;
import com.wdiscute.starcatcher.bob.FishingBobRenderer;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.fishentity.FishRenderer;
import com.wdiscute.starcatcher.fishspotter.FishTrackerLayer;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import com.wdiscute.starcatcher.networkandcodecs.*;
import com.wdiscute.starcatcher.particles.FishingBitingLavaParticles;
import com.wdiscute.starcatcher.particles.FishingBitingParticles;
import com.wdiscute.starcatcher.particles.FishingNotificationParticles;
import com.wdiscute.starcatcher.rod.FishingRodScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.DataPackRegistryEvent;

import java.util.List;
import java.util.Random;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Starcatcher.MOD_ID)
public class Starcatcher
{
    public static final String MOD_ID = "starcatcher";

    public static final ResourceKey<Registry<FishProperties>> FISH_REGISTRY =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish"));

    public static final ResourceKey<Registry<TrophyProperties>> TROPHY_REGISTRY =
            ResourceKey.createRegistryKey(Starcatcher.rl("trophy"));

    public static ResourceLocation rl(String s)
    {
        return ResourceLocation.fromNamespaceAndPath(Starcatcher.MOD_ID, s);
    }

    public static final Random r = new Random();

    public static double truncatedNormal(double mean, double deviation)
    {
        while (true)
        {
            double value = mean + deviation * r.nextGaussian();
            if (value >= mean - deviation && value <= mean + deviation)
            {
                return value;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void fishCaughtToast(FishProperties fp, boolean newFish, int sizeCM, int weightCM)
    {
        if(newFish) Minecraft.getInstance().getToasts().addToast(new FishCaughtToast(fp));

        SettingsScreen.Units units = Config.UNIT.get();

        String size = units.getSizeAsString(sizeCM);
        String weight = units.getWeightAsString(weightCM);

        Minecraft.getInstance().player.displayClientMessage(
                Component.literal("")
                        .append(Component.translatable(fp.fish().value().getDescriptionId()))
                        .append(Component.literal(" - " + size + " - " + weight))
                , true);

        Minecraft.getInstance().gui.overlayMessageTime = 180;

    }

    public Starcatcher(FMLJavaModLoadingContext context)
    {
        IEventBus eventBus = context.getModEventBus();

        ModCreativeModeTabs.register(eventBus);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModSounds.register(eventBus);
        ModEntities.register(eventBus);
        ModParticles.register(eventBus);
        ModMenuTypes.register(eventBus);

        Payloads.register();

        context.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        context.registerConfig(ModConfig.Type.SERVER, Config.SPEC_SERVER);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class IHateForge
    {
        @SubscribeEvent
        public static void attachEvent(AttachCapabilitiesEvent<Entity> event)
        {
            if (!(event.getObject() instanceof Player player)) return;

            DataAttachments modCapabilities = new DataAttachments(player);
            LazyOptional<DataAttachmentCapability> optionalStorage = LazyOptional.of(() -> modCapabilities);

            ICapabilityProvider provider = new ICapabilitySerializable<CompoundTag>() {
                @Override
                public CompoundTag serializeNBT()
                {
                    return modCapabilities.serializeNBT();
                }

                @Override
                public void deserializeNBT(CompoundTag tag)
                {
                    modCapabilities.deserializeNBT(tag);
                }

                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction direction) {
                    if (cap == DataAttachmentCapability.PLAYER_DATA) {
                        return optionalStorage.cast();
                    }
                    return LazyOptional.empty();
                }
            };

            event.addCapability(Starcatcher.rl("fish"), provider);
        }

        @SubscribeEvent
        public static void playerOnDeath(PlayerEvent.Clone event)
        {
            if(event.isWasDeath())
            {
                event.getOriginal().reviveCaps();
                event.getOriginal().getCapability(DataAttachments.PLAYER_DATA).ifPresent(oldData ->
                {
                    DataAttachments.get(event.getEntity()).setFishNotifications(oldData.fishNotifications());
                    DataAttachments.get(event.getEntity()).setTrophiesCaught(oldData.trophiesCaught());
                    DataAttachments.get(event.getEntity()).setFishesCaught(oldData.fishesCaught());
                });
            }
        }

        @SubscribeEvent
        public static void playerLogInEvent(PlayerEvent.PlayerLoggedInEvent event)
        {
            Player player = event.getEntity();

            if(player instanceof ServerPlayer sp)
            {
                DataAttachmentCapability playerCap = DataAttachments.get(player);

                Payloads.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp),
                        new Payloads.FishesCaughtPayload(playerCap.fishesCaught(), sp));

                Payloads.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp),
                        new Payloads.TrophiesCaughtPayload(playerCap.trophiesCaught()));

                Payloads.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp),
                        new Payloads.FishesNotificationPayload(playerCap.fishNotifications(), sp));
            }

        }

        @SubscribeEvent
        public static void trophyTooltip(ItemTooltipEvent event)
        {
            List<Component> comp = event.getToolTip();
            ItemStack stack = event.getItemStack();

            if (!DataComponents.getSizeAndWeight(stack).equals(SizeAndWeight.DEFAULT))
            {
                SizeAndWeight sw = DataComponents.getSizeAndWeight(stack);

                SettingsScreen.Units units = Config.UNIT.get();

                String size = units.getSizeAsString(sw.sizeInCentimeters());
                String weight = units.getWeightAsString(sw.weightInGrams());

                comp.add(1, Component.literal(size + " - " + weight).withStyle(Style.EMPTY.withColor(0x888888)));
            }

            if (!DataComponents.getTrophyProperties(stack).equals(TrophyProperties.DEFAULT))
            {
                TrophyProperties tp = DataComponents.getTrophyProperties(stack);

                comp.set(0, comp.get(0).copy().withStyle(Style.EMPTY.withItalic(false)));

                if (tp.trophyType() == TrophyProperties.TrophyType.TROPHY)
                    if (Screen.hasShiftDown())
                    {
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

                        if (list.size() == 1)
                        {
                            comp.add(Component.translatable("tooltip.starcatcher.trophy.once")
                                    .append(list.get(0))
                                    .append(Component.translatable("tooltip.starcatcher.trophy.have_been_caught")));
                        }
                        else
                        {
                            comp.add(Component.translatable("tooltip.starcatcher.trophy.2"));
                            comp.addAll(list);
                        }

                    }
                    else
                    {
                        comp.add(Component.translatable("tooltip.libtooltips.generic.shift_up"));
                    }

            }
        }


    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents
    {
        @SubscribeEvent
        public static void addRegistry(DataPackRegistryEvent.NewRegistry event)
        {
            event.dataPackRegistry(
                    FISH_REGISTRY, FishProperties.CODEC, FishProperties.CODEC);

            event.dataPackRegistry(
                    TROPHY_REGISTRY, TrophyProperties.CODEC, TrophyProperties.CODEC);
        }

        @SubscribeEvent
        public static void registerAttributed(EntityAttributeCreationEvent event)
        {
            event.put(ModEntities.FISH.get(), FishEntity.createAttributes().build());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModClientEvents
    {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.FISHING_BOB.get(), FishingBobRenderer::new);
            EntityRenderers.register(ModEntities.BOTTLE.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntities.FISH.get(), FishRenderer::new);

            ModItemProperties.addCustomItemProperties();

            MenuScreens.register(ModMenuTypes.FISHING_ROD_MENU.get(), FishingRodScreen::new);
        }

        @SubscribeEvent
        public static void FishSpotterLayer(RegisterGuiOverlaysEvent event)
        {
            event.registerAboveAll("fish_tracker", new FishTrackerLayer());
        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event)
        {
            event.registerSpriteSet(ModParticles.FISHING_NOTIFICATION.get(), FishingNotificationParticles.Provider::new);
            event.registerSpriteSet(ModParticles.FISHING_BITING.get(), FishingBitingParticles.Provider::new);
            event.registerSpriteSet(ModParticles.FISHING_BITING_LAVA.get(), FishingBitingLavaParticles.Provider::new);
        }

        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
        {
            event.registerLayerDefinition(FishingBobModel.LAYER_LOCATION, FishingBobModel::createBodyLayer);
        }

    }

}
