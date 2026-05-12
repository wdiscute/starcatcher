package com.wdiscute.starcatcher.event;

import com.wdiscute.sellingbin.bin.SellingBinBlockEntity;
import com.wdiscute.sellingbin.event.SBevents;
import com.wdiscute.sellingbin.registry.SBBlockEntities;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.SCBlockEntities;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxBlockEntity;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.nikdo53.neobackports.event.RegisterDataMapTypesEvent;
import net.nikdo53.neobackports.event.RegisterPayloadHandlersEvent;
import net.nikdo53.neobackports.extensions.IDataMapHolderExtension;
import net.nikdo53.neobackports.io.networking.PayloadRegistrar;
import net.nikdo53.neobackports.registry.ForgeRegistryHelper;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SCEvents
{

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

        ResourceLocation tackleSkin = SCDataMaps.getOrDefault(itemStack, SCDataMaps.TACKLE_SKIN, null);

        if (tackleSkin != null && SCDataComponents.get(itemStack, SCDataComponents.TACKLE_SKIN) == null)
        {
            SCDataComponents.set(itemStack, SCDataComponents.TACKLE_SKIN, tackleSkin);
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
    public static void levelTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END) {
            TournamentHandler.tick(event);
        }
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

            if (SCConfig.GIVE_GUIDE.get() && !FishingGuideAttachment.getReceivedGuide(sp))
            {
                sp.addItem(new ItemStack(SCItems.GUIDE.get()));
                fishingGuideAttachment.setReceivedGuide(sp, true);
            }
        }
    }


    @SubscribeEvent
    public static void dropWormsWhenBonemealing(PlayerInteractEvent.RightClickBlock event)
    {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        if (event.getItemStack().is(Items.BONE_MEAL) && level.getBlockState(event.getPos()).getBlock() instanceof FarmBlock)
        {
            if (!level.isClientSide && SCConfig.ENABLE_BONE_MEAL_ON_FARMLAND_FOR_WORMS.get())
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
                    if (!player.isCreative())
                        event.getItemStack().shrink(1);
                }
            }
        }
    }


}
