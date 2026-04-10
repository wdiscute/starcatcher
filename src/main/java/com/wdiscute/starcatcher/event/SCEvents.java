package com.wdiscute.starcatcher.event;

import com.wdiscute.sellingbin.event.SBvents;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCCommands;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import com.wdiscute.starcatcher.io.TournamentSavedData;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.io.network.*;
import com.wdiscute.starcatcher.io.network.tournament.CBActiveTournamentUpdatePayload;
import com.wdiscute.starcatcher.io.network.tournament.CBClearTournamentPayload;
import com.wdiscute.starcatcher.io.network.tournament.SBStandTournamentNameChangePayload;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.List;

@EventBusSubscriber(modid = Starcatcher.MOD_ID)
public class SCEvents
{
    @SubscribeEvent
    public static void serverStarted(RegisterSpawnPlacementsEvent event)
    {
        event.register(
                SCEntities.FISH.get(), SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                FishEntity::validSpawnPlacement,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void modifyItemAttribute(ItemAttributeModifierEvent event)
    {
        ItemStack itemStack = event.getItemStack();

        List<ResourceLocation> catchModifiers = SCDataMaps.getOrDefault(itemStack, SCDataMaps.CATCH_MODIFIERS, null);

        if (catchModifiers != null && SCDataComponents.get(itemStack, SCDataComponents.CATCH_MODIFIERS) == null)
        {
            SCDataComponents.set(itemStack, SCDataComponents.CATCH_MODIFIERS, catchModifiers);
        }

        List<ResourceLocation> minigameModifiers = SCDataMaps.getOrDefault(itemStack, SCDataMaps.MINIGAME_MODIFIERS, null);

        if (minigameModifiers != null && SCDataComponents.get(itemStack, SCDataComponents.MINIGAME_MODIFIERS) == null)
        {
            SCDataComponents.set(itemStack, SCDataComponents.MINIGAME_MODIFIERS, minigameModifiers);
        }

    }


    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event)
    {
        PackSource packSource = new SBvents.DefaultPackSource()
        {
            @Override
            public boolean shouldAddAutomatically()
            {
                return true;
            }
        };

        event.addPackFinders(
                Starcatcher.rl("built_in_datapacks/selling_bin_emeralds"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Emeralds"),
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
        if (event.getEntity() instanceof ServerPlayer sp)
        {
            //tournament
            var tournament = TournamentHandler.getTournamentForPlayer(sp);
            if (tournament != null)
                TournamentHandler.sendActiveTournamentUpdateToClient(sp, tournament);
            else
                TournamentHandler.clearTournamentToClient(sp);

            //guide
            FishingGuideAttachment fishingGuideAttachment = SCDataAttachments.get(sp, SCDataAttachments.FISHING_GUIDE);

            if (SCConfig.GIVE_GUIDE.get() && !fishingGuideAttachment.receivedGuide)
            {
                sp.addItem(new ItemStack(SCItems.GUIDE.get()));
                fishingGuideAttachment.receivedGuide = true;
            }
        }
    }


    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event)
    {
        event.register(Starcatcher.SWEET_SPOT_BEHAVIOUR_REGISTRY);
        event.register(Starcatcher.MINIGAME_MODIFIERS_REGISTRY);
        event.register(Starcatcher.CATCH_MODIFIERS_REGISTRY);
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

        if (event.getItemStack().is(Items.BONE_MEAL) && level.getBlockState(event.getPos()).getBlock() instanceof FarmBlock)
        {
            if (!level.isClientSide && SCConfig.ENABLE_BONE_MEAL_ON_FARMLAND_FOR_WORMS.getAsBoolean())
            {
                ItemStack is;
                float i = level.getRandom().nextFloat();
                if (i < 0.8f)
                    is = new ItemStack(SCItems.WORM.get());
                else if (i < 0.99f)
                    is = new ItemStack(SCItems.ALMIGHTY_WORM.get());
                else
                    is = new ItemStack(SCItems.SEEKING_WORM.get());

                Vec3 vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5F, 1.01, 0.5F).offsetRandom(level.random, 0.7F);
                ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), is);
                itementity.setDefaultPickUpDelay();
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
    }

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar("1");
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
}
