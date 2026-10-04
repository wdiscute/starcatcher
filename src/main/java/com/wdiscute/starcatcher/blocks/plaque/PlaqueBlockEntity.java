package com.wdiscute.starcatcher.blocks.plaque;

import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
        CompoundTag tag = super.getUpdateTag(registries);

        RegistryOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);

        tag.store("fish", MaybeStack.CODEC, ops, this.item);

        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);

        this.item = input.read("fish", MaybeStack.CODEC).orElse(MaybeStack.EMPTY);
    }

    public ItemStack getImmutableItem()
    {
        return this.item.toStack();
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);

        output.store("fish", MaybeStack.CODEC, new MaybeStack(getImmutableItem()));

        output.putBoolean("empty", true);
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
