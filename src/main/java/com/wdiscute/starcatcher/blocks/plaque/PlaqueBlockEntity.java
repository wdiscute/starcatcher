package com.wdiscute.starcatcher.blocks.plaque;

import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.utils.MaybeStack;
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
    MaybeStack item = MaybeStack.EMPTY;

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
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        if (tag.contains("fish"))
            this.item = new MaybeStack(ItemStack.parse(registries, tag.getCompound("fish")).orElse(ItemStack.EMPTY));
        else
            this.item = MaybeStack.EMPTY;
    }

    public ItemStack getImmutableItem()
    {
        return this.item.toStack();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        if (!this.getImmutableItem().isEmpty())
            tag.put("fish", this.getImmutableItem().save(registries));
        else
            //need to put a tag otherwise it's not sent to client since the tag is empty
            tag.putBoolean("empty", true);
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
