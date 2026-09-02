package com.wdiscute.starcatcher.event;

import com.wdiscute.sellingbin.event.SBevents;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.compat.jei.StarcatcherJeiPlugin;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Starcatcher.MOD_ID)
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

                double x = Math.clamp((player.position().x - bobber.position().x) / 25, -1, 1);
                double y = Math.clamp((player.position().y - bobber.position().y) / 20, -1, 1);
                double z = Math.clamp((player.position().z - bobber.position().z) / 25, -1, 1);
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
    public static void addCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SCBlockEntities.TACKLE_BOX.get(),
                (container, side) ->
                {
                    if (container instanceof TackleBoxBlockEntity be)
                    {
                        return new SidedInvWrapper(container, side)
                        {
                            @Override
                            public void setStackInSlot(int slot, ItemStack stack)
                            {
                                super.setStackInSlot(slot, stack);
                                be.updateFishSlot();
                            }
                        };
                    }
                    return null;
                }
        );
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event)
    {
        if (ModList.get().isLoaded("jei"))
            event.sendRecipes(RecipeType.SMITHING);
    }

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event)
    {
        if (ModList.get().isLoaded("jei"))
            StarcatcherJeiPlugin.receiveRecipes(event);
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
        });
    }

    @SubscribeEvent
    public static void levelTick(ServerTickEvent.Post event)
    {
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
            PacketDistributor.sendToPlayer(sp, new CBFinishedTournamentsListPayload(TournamentHandler.getFinishedTournaments()));

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
        event.register(Starcatcher.SWEETSPOT_BEHAVIOUR_REGISTRY);
        event.register(Starcatcher.TACKLE_SKIN_REGISTRY);
        event.register(Starcatcher.FISH_RESTRICTIONS_REGISTRY);
    }

    @SubscribeEvent
    public static void addDatapackRegistry(DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(
                Starcatcher.FISH_REGISTRY_KEY, FishProperties.CODEC, FishProperties.CODEC,
                builder -> builder.maxId(512));
    }

    @SubscribeEvent
    public static void dropWormsWhenBonemealing(PlayerInteractEvent.RightClickBlock event)
    {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        if (event.getItemStack().is(SCTags.HAS_FARMLAND_INTERACTION) && !level.isClientSide && SCConfig.ENABLE_BONE_MEAL_ON_FARMLAND_FOR_WORMS.getAsBoolean())
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
                if (!player.hasInfiniteMaterials())
                    event.getItemStack().shrink(1);
            }
        }

    }

    @SubscribeEvent
    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event)
    {
        SCAttributes.REGISTRY.getEntries().forEach(o -> event.add(EntityType.PLAYER, o, 1.0));
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
        final PayloadRegistrar registrar = event.registrar("1");
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
                CBFishCaughtNotifs.TYPE,
                CBFishCaughtNotifs.STREAM_CODEC,
                CBFishCaughtNotifs::handle
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
    }
}
