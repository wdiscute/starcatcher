package com.wdiscute.starcatcher.blocks.stand;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.NBTCodecHelper;
import com.wdiscute.starcatcher.blocks.SCBlockEntities;
import com.wdiscute.starcatcher.tournament.StandMenu;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserNameToIdResolver;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StandBlockEntity extends AbstractMultiBlockEntity implements MenuProvider
{
    public Tournament tournament;
    public Map<UUID, String> profiles;
    private UUID uuid;

    public StandBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.STAND.get(), pos, blockState);
    }

    public Tournament makeOrGetTournament()
    {
        if (tournament != null) return tournament;

        Tournament t = TournamentHandler.getTournamentOrNull(getUuid());
        if (t != null)
        {
            tournament = t;
            return tournament;
        }

        tournament = Tournament.empty(getUuid());
        return tournament;
    }

    public UUID getUuid()
    {
        if (this.uuid == null)
        {
            setUuid(UUID.randomUUID());
        }
        return this.uuid;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
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
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);


        if (!isCenter()) return;

        if (uuid != null)
            NBTCodecHelper.encode(UUIDUtil.CODEC, uuid, output, "uuid");

        NBTCodecHelper.encode(Tournament.CODEC, tournament, output, "tournament");
        StarcatcherGameProfileCache cache = gameProfilesHelper(level, tournament);
        NBTCodecHelper.encode(StarcatcherGameProfileCache.GAME_PROFILES_CODEC, cache, output, "profiles");

    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);

        if (!isCenter()) return;

        uuid = NBTCodecHelper.decode(UUIDUtil.CODEC, input, "uuid");

        tournament = NBTCodecHelper.decode(Tournament.CODEC, input, "tournament");
        var awd = NBTCodecHelper.decode(StarcatcherGameProfileCache.GAME_PROFILES_CODEC, input, "profiles");
        if (awd != null)
            profiles = awd.map;
    }

    public record StarcatcherGameProfileCache(Map<UUID, String> map)
    {
        public static final Codec<StarcatcherGameProfileCache> GAME_PROFILES_CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.STRING).fieldOf("map").forGetter(StarcatcherGameProfileCache::map)
                        ).apply(instance, StarcatcherGameProfileCache::new)
                );
    }

    public static StarcatcherGameProfileCache gameProfilesHelper(Level level, Tournament tournament)
    {
        if (level.isClientSide()) return new StarcatcherGameProfileCache(new HashMap<>());
        if (tournament == null) return new StarcatcherGameProfileCache(new HashMap<>());

        Map<UUID, String> map = new HashMap<>();
        tournament.playerScores.forEach(entry ->
        {
            UserNameToIdResolver profileCache = level.getServer().services().nameToIdCache();
            Optional<NameAndId> nameAndId = profileCache.get(entry.playerUUID);
            nameAndId.ifPresent(i -> map.put(i.id(), i.name()));
        });

        return new StarcatcherGameProfileCache(map);
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
