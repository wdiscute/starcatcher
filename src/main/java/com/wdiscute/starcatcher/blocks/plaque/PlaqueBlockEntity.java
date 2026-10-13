package com.wdiscute.starcatcher.blocks.plaque;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.ValueHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlaqueBlockEntity extends BlockEntity
{
    public MaybeStack item = MaybeStack.EMPTY;

    public PlaqueBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.FISH_PLAQUE.get(), pos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        super.getUpdateTag(registries);
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);

        this.item = ValueHelper.read("fish", MaybeStack.CODEC, tag).orElse(MaybeStack.EMPTY);
    }

    public ItemStack getImmutableItem()
    {
        return this.item.toStack();
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);

        ValueHelper.store("fish", MaybeStack.CODEC, new MaybeStack(getImmutableItem()), tag);

        ValueHelper.store("empty", Codec.BOOL, true, tag);
    }

    public void clearContent()
    {
        item = MaybeStack.EMPTY;
        sync();
    }

    public void sync()
    {
        setChanged();

        if (level instanceof ServerLevel serverLevel)
            serverLevel.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
