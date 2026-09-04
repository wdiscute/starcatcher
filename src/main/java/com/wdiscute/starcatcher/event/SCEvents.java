package com.wdiscute.starcatcher.event;

import com.wdiscute.sellingbin.event.SBevents;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.BonemealInteractionEntry;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxBlockEntity;
import com.wdiscute.starcatcher.data.network.tournament.CBFinishedTournamentsListPayload;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.data.TournamentSavedData;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.data.network.*;
import com.wdiscute.starcatcher.data.network.tournament.CBActiveTournamentUpdatePayload;
import com.wdiscute.starcatcher.data.network.tournament.CBClearTournamentPayload;
import com.wdiscute.starcatcher.data.network.tournament.SBStandTournamentNameChangePayload;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import com.wdiscute.starcatcher.trigger.FishCaughtTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.nikdo53.neobackports.event.RegisterDataMapTypesEvent;
import net.nikdo53.neobackports.event.RegisterPayloadHandlersEvent;
import net.nikdo53.neobackports.io.networking.PacketDistributorNeo;
import net.nikdo53.neobackports.io.networking.PayloadRegistrar;
import net.nikdo53.neobackports.registry.ForgeRegistryHelper;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID)
public class SCEvents
{
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
    public static void itemFished(ItemFishedEvent event)
    {
        if (!SCConfig.GIVE_ROD.get()) return;
        Player player = event.getHookEntity().getPlayerOwner();
        if (SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB).isEmpty())
        {
            if (!FishingGuideAttachment.getFishedRod(player))
            {
                FishingGuideAttachment.setFishedRod(player, true);

                FishingHook bobber = event.getHookEntity();

                double x = Mth.clamp((player.position().x - bobber.position().x) / 25, -1, 1);
                double y = Mth.clamp((player.position().y - bobber.position().y) / 20, -1, 1);
                double z = Mth.clamp((player.position().z - bobber.position().z) / 25, -1, 1);
                Vec3 vec3 = new Vec3(x, 0.7 + y, z);

                ItemEntity rodFished = new ItemEntity(player.level(),
                        bobber.position().x, bobber.position().y + 1.2f, bobber.position().z,
                        SCItems.ROD.toStack());

                ItemEntity guideFished = new ItemEntity(player.level(),
                        bobber.position().x, bobber.position().y + 1.2f, bobber.position().z,
                        SCItems.GUIDE.toStack());

                rodFished.setDeltaMovement(vec3);
                guideFished.setDeltaMovement(vec3);

                bobber.level().addFreshEntity(guideFished);
                bobber.level().addFreshEntity(rodFished);
            }
        }
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event)
    {
        TournamentHandler.setAll(TournamentSavedData.get(event.getServer().overworld()).getTournaments());
    }

    @SubscribeEvent
    public static void serverStopping(ServerStoppingEvent event)
    {
        TournamentSavedData.get(event.getServer().overworld()).setTournaments(TournamentHandler.getAll());
    }

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
    public static void levelTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.END))
            TournamentHandler.tick(event);
    }

    @SubscribeEvent
    public static void addCommand(RegisterCommandsEvent event)
    {
        SCCommands.register(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer sp)
        {
            //send stats for guide book
            sp.getStats().sendStats(sp);

            //tournament
            var tournament = TournamentHandler.getTournamentForPlayer(sp);
            if (tournament != null)
                TournamentHandler.sendActiveTournamentUpdateToClient(sp, tournament);
            else
                TournamentHandler.clearTournamentToClient(sp);

            //send list of finished tournaments to client
            PacketDistributorNeo.sendToPlayer(sp, new CBFinishedTournamentsListPayload(TournamentHandler.getFinishedTournaments()));

            //guide
            if (SCConfig.GIVE_GUIDE.get() && !FishingGuideAttachment.getReceivedGuide(player))
            {
                sp.addItem(new ItemStack(SCItems.GUIDE.get()));
                FishingGuideAttachment.setReceivedGuide(player, true);
            }
        }
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
    public static void dropWormsWhenBonemealing(PlayerInteractEvent.RightClickBlock event)
    {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        if (event.getItemStack().is(SCTags.HAS_FARMLAND_INTERACTION) && !level.isClientSide && SCConfig.ENABLE_BONE_MEAL_ON_FARMLAND_FOR_WORMS.get())
        {
            ItemStack is = BonemealInteractionEntry.getRandom(level.getBlockState(pos).getBlockHolder(), level.getRandom()).toStack();

            if (is.isEmpty())
                return;

            Vec3 vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5F, 1.01, 0.5F).offsetRandom(level.random, 0.7F);
            ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), is);
            level.addFreshEntity(itementity);

            level.playSound(null, pos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (event.getEntity() instanceof ServerPlayer player)
            {
                player.swing(event.getHand(), true);
                if (!player.isCreative())
                    event.getItemStack().shrink(1);
            }
        }

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
}
