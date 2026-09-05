package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.BonemealInteractionEntry;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.data.network.tournament.CBFinishedTournamentsListPayload;
import com.wdiscute.starcatcher.data.TournamentSavedData;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.data.network.*;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nikdo53.neobackports.io.networking.PacketDistributorNeo;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SCEvents
{
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
}
