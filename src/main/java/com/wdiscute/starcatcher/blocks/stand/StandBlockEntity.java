package com.wdiscute.starcatcher.blocks.stand;

import com.wdiscute.starcatcher.data.NBTCodecHelper;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.tournament.StandMenu;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StandBlockEntity extends AbstractMultiBlockEntity implements MenuProvider
{
    public Tournament tournament;
    private UUID tournamentUUID;

    public StandBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.STAND.get(), pos, blockState);
    }

    public Tournament makeOrGetTournament(Player player)
    {
        if (tournament != null) return tournament;

        Tournament t = TournamentHandler.getActiveTournamentOrNull(getCurrentTournamentUuid());
        if (t != null)
        {
            tournament = t;
            return tournament;
        }
        else
        {
            tournament = Tournament.empty(getCurrentTournamentUuid(), player);
            return tournament;
        }
    }

    public UUID getCurrentTournamentUuid()
    {
        if (this.tournamentUUID == null) setUuid(UUID.randomUUID());
        return this.tournamentUUID;
    }

    public void setUuid(UUID uuid)
    {
        this.tournamentUUID = uuid;
        sync();
    }

    public void sync()
    {
        setChanged();

        if (level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
    {
        return new StandMenu(i, inventory, this);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        TournamentHandler.cancelTournament(level, tournament);

        super.preRemoveSideEffects(pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);

        if (!isCenter()) return;

        tournamentUUID = input.read("tournament_uuid", UUIDUtil.CODEC).orElse(tournamentUUID);

        tournament = input.read("preparing_tournament", Tournament.CODEC).orElse(tournament);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (!isCenter()) return;

        //blockentity needs to save tournament UUID to not forget which one is on this stand
        if (tournamentUUID != null)
            output.store("tournament_uuid", UUIDUtil.CODEC, tournamentUUID);

        //if status is preparing, we need to save the tournament as it's not stored in SavedData yet
        if (tournament != null && tournament.status.equals(Tournament.Status.PREPARING))
            output.store("preparing_tournament", Tournament.CODEC, tournament);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        CompoundTag tag = super.getUpdateTag(registries);

        if (tournament != null)
            NBTCodecHelper.encode(Tournament.CODEC, tournament, tag, "current_tournament");

        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.empty();
    }
}
