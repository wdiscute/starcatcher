package com.wdiscute.starcatcher.blocks.stand;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.NBTCodecHelper;
import com.wdiscute.starcatcher.blocks.SCBlockEntities;
import com.wdiscute.starcatcher.tournament.StandMenu;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StandBlockEntity extends AbstractMultiBlockEntity implements MenuProvider
{
    public Tournament tournament;
    public Map<UUID, String> profiles;
    private UUID uuid;

    public final ItemStackHandler entryCost = new ItemStackHandler(9)
    {
        @Override
        protected int getStackLimit(int slot, ItemStack stack)
        {
            return 64;
        }

    };

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
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);

        if (!isCenter()) return;

        if (uuid != null)
            tag.putUUID("tournament_uuid", uuid);

        NBTCodecHelper.encode(Tournament.CODEC, tournament, tag, "tournament");
        StarcatcherGameProfileCache cache = gameProfilesHelper(level, tournament);
        NBTCodecHelper.encode(StarcatcherGameProfileCache.GAME_PROFILES_CODEC, cache, tag, "profiles");
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
        if (level.isClientSide) return new StarcatcherGameProfileCache(new HashMap<>());
        if (tournament == null) return new StarcatcherGameProfileCache(new HashMap<>());

        Map<UUID, String> map = new HashMap<>();
        tournament.playerScores.forEach(entry ->
        {
            GameProfileCache profileCache = level.getServer().getProfileCache();
            if (profileCache != null)
            {
                Optional<GameProfile> gameProfile = profileCache.get(entry.playerUUID);
                gameProfile.ifPresent(i -> map.put(i.getId(), i.getName()));
            }
        });

        return new StarcatcherGameProfileCache(map);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);

        if (!isCenter()) return;

        if (tag.contains("tournament_uuid"))
            uuid = tag.getUUID("tournament_uuid");

        tournament = NBTCodecHelper.decode(Tournament.CODEC, tag, "tournament");
        var awd = NBTCodecHelper.decode(StarcatcherGameProfileCache.GAME_PROFILES_CODEC, tag, "profiles");
        if (awd != null)
            profiles = awd.map;
    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {

        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
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
