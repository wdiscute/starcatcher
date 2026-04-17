package com.wdiscute.starcatcher.event;

import com.wdiscute.sellingbin.event.SBevents;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.io.network.*;
import com.wdiscute.starcatcher.io.network.tournament.CBActiveTournamentUpdatePayload;
import com.wdiscute.starcatcher.io.network.tournament.CBClearTournamentPayload;
import com.wdiscute.starcatcher.io.network.tournament.SBStandTournamentNameChangePayload;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.nikdo53.neobackports.event.RegisterDataMapTypesEvent;
import net.nikdo53.neobackports.event.RegisterPayloadHandlersEvent;
import net.nikdo53.neobackports.io.networking.PayloadRegistrar;
import net.nikdo53.neobackports.registry.ForgeRegistryHelper;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCModEvents {

    @SubscribeEvent
    public static void serverStarted(SpawnPlacementRegisterEvent event)
    {
        event.register(
                SCEntities.FISH.get(), SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                FishEntity::validSpawnPlacement,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event)
    {
        PackSource packSource = new SBevents.DefaultPackSource();

        event.addPackFinders(
                Starcatcher.rl("built_in_datapacks/selling_bin_starcatcher_emeralds"),
                PackType.SERVER_DATA,
                Component.literal("Starcatcher - Emeralds"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                Starcatcher.rl("built_in_datapacks/selling_bin_fishes"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Fishes"),
                packSource,
                false,
                Pack.Position.TOP
        );
    }

    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event)
    {
        ForgeRegistryHelper.getInstance(Starcatcher.SWEET_SPOT_BEHAVIOUR)
                .create(event, reg -> Starcatcher.SWEET_SPOT_BEHAVIOUR_REGISTRY = reg);

        ForgeRegistryHelper.getInstance(Starcatcher.MINIGAME_MODIFIERS)
                .create(event, reg -> Starcatcher.MINIGAME_MODIFIERS_REGISTRY = reg);

        ForgeRegistryHelper.getInstance(Starcatcher.CATCH_MODIFIERS)
                .create(event, reg -> Starcatcher.CATCH_MODIFIERS_REGISTRY = reg);

        ForgeRegistryHelper.getInstance(Starcatcher.TACKLE_SKIN)
                .create(event, reg -> Starcatcher.TACKLE_SKIN_REGISTRY = reg);

        ForgeRegistryHelper.getInstance(Starcatcher.FISH_RESTRICTIONS)
                .create(event, reg -> Starcatcher.FISH_RESTRICTIONS_REGISTRY = reg);

    }

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar("1", Starcatcher.MOD_ID);
        registrar.playToClient(
                FishingStartedPayload.TYPE,
                FishingStartedPayload.STREAM_CODEC,
                FishingStartedPayload::handle
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

        registrar.playToServer(
                SBStandTournamentNameChangePayload.TYPE,
                SBStandTournamentNameChangePayload.STREAM_CODEC,
                SBStandTournamentNameChangePayload::handle
        );

        registrar.playToClient(
                CBActiveTournamentUpdatePayload.TYPE,
                CBActiveTournamentUpdatePayload.STREAM_CODEC,
                CBActiveTournamentUpdatePayload::handle
        );

        registrar.playToClient(
                CBClearTournamentPayload.TYPE,
                CBClearTournamentPayload.STREAM_CODEC,
                CBClearTournamentPayload::handle
        );

        registrar.playToServer(
                SetMessagePayload.TYPE,
                SetMessagePayload.STREAM_CODEC,
                SetMessagePayload::handle
        );

        registrar.playToServer(
                SignGuidePayload.TYPE,
                SignGuidePayload.STREAM_CODEC,
                SignGuidePayload::handle
        );
    }

    @SubscribeEvent
    public static void registerAttributed(EntityAttributeCreationEvent event)
    {
        event.put(SCEntities.FISH.get(), FishEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerAttributed(RegisterDataMapTypesEvent event)
    {
        event.register(SCDataMaps.AQUARIUM_INTERACTION);
        event.register(SCDataMaps.CATCH_MODIFIERS);
        event.register(SCDataMaps.MINIGAME_MODIFIERS);
        event.register(SCDataMaps.TACKLE_SKIN);
    }

    @SubscribeEvent
    public static void addDatapackRegistry(DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(
                Starcatcher.FISH_REGISTRY_KEY, FishProperties.CODEC, FishProperties.CODEC);
    }

}
