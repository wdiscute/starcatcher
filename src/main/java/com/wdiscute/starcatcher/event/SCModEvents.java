package com.wdiscute.starcatcher.event;

import com.wdiscute.sellingbin.event.SBevents;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.network.*;
import com.wdiscute.starcatcher.data.network.tournament.CBActiveTournamentUpdatePayload;
import com.wdiscute.starcatcher.data.network.tournament.CBClearTournamentPayload;
import com.wdiscute.starcatcher.data.network.tournament.CBFinishedTournamentsListPayload;
import com.wdiscute.starcatcher.data.network.tournament.SBStandTournamentNameChangePayload;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.trigger.FishCaughtTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.nikdo53.neobackports.event.RegisterDataMapTypesEvent;
import net.nikdo53.neobackports.event.RegisterPayloadHandlersEvent;
import net.nikdo53.neobackports.io.networking.PayloadRegistrar;
import net.nikdo53.neobackports.registry.ForgeRegistryHelper;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCModEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            Stats.CUSTOM.get(SCStats.TICKS_SPENT_FISHING.get(), StatFormatter.TIME);
            SCCriterionTriggers.FISH = CriteriaTriggers.register(new FishCaughtTrigger());
        });



    }
    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event)
    {
        PackSource packSource = new SBevents.DefaultPackSource()
        {
            @Override
            public boolean shouldAddAutomatically()
            {
                return true;
            }
        };

        //create
        event.addPackFinders(
                Starcatcher.rl("built_in_datapacks/create_compat"),
                PackType.SERVER_DATA,
                Component.literal("Starcatcher - Create Compat"),
                packSource,
                false,
                Pack.Position.BOTTOM
        );

        //tide
        event.addPackFinders(
                Starcatcher.rl("built_in_datapacks/tide_compat"),
                PackType.SERVER_DATA,
                Component.literal("Starcatcher - Tide Compat"),
                packSource,
                false,
                Pack.Position.BOTTOM
        );


        //
        //                 ,--. ,--. ,--.                      ,--.    ,--.
        //  ,---.   ,---.  |  | |  | `--' ,--,--,   ,---.      |  |-.  `--' ,--,--,
        // (  .-'  | .-. : |  | |  | ,--. |      \ | .-. |     | .-. ' ,--. |      \
        // .-'  `) \   --. |  | |  | |  | |  ||  | ' '-' '     | `-' | |  | |  ||  |
        // `----'   `----' `--' `--' `--' `--''--' .`-  /       `---'  `--' `--''--'
        //                                         `---'

        event.addPackFinders(
                Starcatcher.rl("built_in_datapacks/selling_bin_starcatcher_emeralds"),
                PackType.SERVER_DATA,
                Component.literal("Starcatcher - Emeralds"),
                packSource,
                false,
                Pack.Position.BOTTOM
        );

        event.addPackFinders(
                Starcatcher.rl("built_in_datapacks/selling_bin_fishes"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Fishes"),
                packSource,
                false,
                Pack.Position.BOTTOM
        );
    }

    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event)
    {
        ForgeRegistryHelper.getInstance(Starcatcher.SWEETSPOT_BEHAVIOUR)
                .create(event, reg -> Starcatcher.SWEETSPOT_BEHAVIOUR_REGISTRY = reg);

        ForgeRegistryHelper.getInstance(Starcatcher.TACKLE_SKIN)
                .create(event, reg -> Starcatcher.TACKLE_SKIN_REGISTRY = reg);

        ForgeRegistryHelper.getInstance(Starcatcher.FISH_RESTRICTIONS)
                .create(event, reg -> Starcatcher.FISH_RESTRICTIONS_REGISTRY = reg);
    }

    @SubscribeEvent
    public static void addDatapackRegistry(DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(
                Starcatcher.FISH_REGISTRY_KEY, FishProperties.CODEC, FishProperties.CODEC);
    }


    @SubscribeEvent
    public static void registerDataMaps(RegisterDataMapTypesEvent event)
    {
        event.register(SCDataMaps.AQUARIUM_INTERACTION);
        event.register(SCDataMaps.TACKLE_SKIN);
        event.register(SCDataMaps.TREASURE);
        event.register(SCDataMaps.MESSAGE_BACKGROUND);

        event.register(SCDataMaps.ITEM_MODIFIERS);
        event.register(SCDataMaps.ENCHANTMENT_MODIFIERS);
        event.register(SCDataMaps.EFFECT_MODIFIERS);
    }

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar("1", Starcatcher.MOD_ID);
        registrar.playToClient(
                CBFishingStartedPayload.TYPE,
                CBFishingStartedPayload.STREAM_CODEC,
                CBFishingStartedPayload::handle
        );

        registrar.playToServer(
                SBFishingCompletedPayload.TYPE,
                SBFishingCompletedPayload.STREAM_CODEC,
                SBFishingCompletedPayload::handle
        );

        registrar.playToClient(
                CBFishCaughtNotifsPayload.TYPE,
                CBFishCaughtNotifsPayload.STREAM_CODEC,
                CBFishCaughtNotifsPayload::handle
        );

        registrar.playToServer(
                SBFPsSeenPayload.TYPE,
                SBFPsSeenPayload.STREAM_CODEC,
                SBFPsSeenPayload::handle
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
                SignGuidePayload.TYPE,
                SignGuidePayload.STREAM_CODEC,
                SignGuidePayload::handle
        );

        registrar.playToClient(
                CBFinishedTournamentsListPayload.TYPE,
                CBFinishedTournamentsListPayload.STREAM_CODEC,
                CBFinishedTournamentsListPayload::handle
        );

        registrar.playToServer(
                SBTrackFishPayload.TYPE,
                SBTrackFishPayload.STREAM_CODEC,
                SBTrackFishPayload::handle
        );

        registrar.playToServer(
                SBSetEditableMessagePayload.TYPE,
                SBSetEditableMessagePayload.STREAM_CODEC,
                SBSetEditableMessagePayload::handle
        );

        registrar.playToClient(
                CBOpenEditableMessagePayload.TYPE,
                CBOpenEditableMessagePayload.STREAM_CODEC,
                CBOpenEditableMessagePayload::handle
        );

        registrar.playToClient(
                CBOpenMessagePayload.TYPE,
                CBOpenMessagePayload.STREAM_CODEC,
                CBOpenMessagePayload::handle
        );

        registrar.playToClient(
                CBPlayerStructuresPayload.TYPE,
                CBPlayerStructuresPayload.STREAM_CODEC,
                CBPlayerStructuresPayload::handle
        );
    }


    @SubscribeEvent
    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event)
    {
        SCAttributes.REGISTRY.getEntries().forEach(o -> event.add(EntityType.PLAYER, o.value(), 1.0));
    }

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event)
    {
        event.put(SCEntities.FISH.get(), FishEntity.createAttributes().build());
    }
}
