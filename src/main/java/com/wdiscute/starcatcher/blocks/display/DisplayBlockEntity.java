package com.wdiscute.starcatcher.blocks.display;

import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class DisplayBlockEntity extends BlockEntity
{
    private MaybeStack item = MaybeStack.EMPTY;

    private static final RandomSource RANDOM = RandomSource.create();
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;

    public boolean fishRotating = true;

    //enchant table code
    public static void bookAnimationTick(Level level, BlockPos worldPosition, BlockState state, DisplayBlockEntity entity)
    {
        entity.oOpen = entity.open;
        entity.oRot = entity.rot;
        Player player = level.getNearestPlayer(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 3.0, false);
        if (player != null)
        {
            double xd = player.getX() - (worldPosition.getX() + 0.5);
            double zd = player.getZ() - (worldPosition.getZ() + 0.5);
            entity.tRot = (float) Mth.atan2(zd, xd);
            entity.open += 0.1F;
            if (entity.open < 0.5F || RANDOM.nextInt(40) == 0)
            {
                float old = entity.flipT;

                do
                {
                    entity.flipT = entity.flipT + (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while (old == entity.flipT);
            }
        }
        else
        {
            entity.tRot += 0.02F;
            entity.open -= 0.1F;
        }

        while (entity.rot >= (float) Math.PI)
            entity.rot -= (float) (Math.PI * 2);

        while (entity.rot < (float) -Math.PI)
            entity.rot += (float) (Math.PI * 2);

        while (entity.tRot >= (float) Math.PI)
            entity.tRot -= (float) (Math.PI * 2);

        while (entity.tRot < (float) -Math.PI)
            entity.tRot += (float) (Math.PI * 2);

        float rotDir = entity.tRot - entity.rot;

        while (rotDir >= (float) Math.PI)
            rotDir -= (float) (Math.PI * 2);

        while (rotDir < (float) -Math.PI)
            rotDir += (float) (Math.PI * 2);

        entity.rot += rotDir * 0.4F;
        entity.open = Mth.clamp(entity.open, 0.0F, 1.0F);
        entity.time++;
        entity.oFlip = entity.flip;
        float diff = (entity.flipT - entity.flip) * 0.4F;
        diff = Mth.clamp(diff, -0.2F, 0.2F);
        entity.flipA = entity.flipA + (diff - entity.flipA) * 0.9F;
        entity.flip = entity.flip + entity.flipA;
    }

    public DisplayBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.DISPLAY.get(), pos, blockState);
    }

    public ItemStack getImmutableItem()
    {
        return this.item.toStack();
    }

    public void setItem(ItemStack stack)
    {
        this.item = new MaybeStack(stack);
        this.setChanged();
        sync();
    }

    public int getRedstoneSignal()
    {
        if (item.isEmpty()) return 0;

        ItemStack stack = item.toStack();
        if (stack.is(SCItems.GUIDE))
        {
            if (SCDataComponents.has(stack, SCDataComponents.SIGNED_GUIDE)) return 5;
            return 15;
        }

        if (SCDataComponents.has(stack, SCDataComponents.CAUGHT_FISH_INFO))
        {
            double percentile = SCDataComponents.get(stack, SCDataComponents.CAUGHT_FISH_INFO).percentile();
            percentile = Math.clamp(percentile, 0, 100);
            double scaledValue = (percentile / 100.0) * 14 + 1;
            return (16 - (int) scaledValue);
        }

        return 15;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        if (state.getValue(DisplayBlock.HAS_ITEM))
            popItem(level, pos);

        super.preRemoveSideEffects(pos, state);

        if (state.getValue(DisplayBlock.POWERED))
            level.updateNeighborsAt(pos.below(), state.getBlock());

    }

    private void popItem(Level level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof DisplayBlockEntity displayBlockEntity)
        {
            ItemStack itemstack = displayBlockEntity.getImmutableItem().copy();
            ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + (double) 0.5F, (pos.getY() + 1), (double) pos.getZ() + (double) 0.5F, itemstack);
            itementity.setDefaultPickUpDelay();
            level.addFreshEntity(itementity);
            displayBlockEntity.clearContent();
        }
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);

        item = input.read("Book", MaybeStack.CODEC).orElse(MaybeStack.EMPTY);

        fishRotating = input.getBooleanOr("rotating", false);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);

        output.store("Book", MaybeStack.CODEC, item);

        output.putBoolean("rotating", fishRotating);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        CompoundTag tag = super.getUpdateTag(registries);

        RegistryOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);

        tag.store("Book", MaybeStack.CODEC, ops, this.item);

        tag.putBoolean("rotating", fishRotating);

        return tag;
    }

    public void clearContent()
    {
        item = MaybeStack.EMPTY;
        BlockState blockState = level.getBlockState(getBlockPos());
        if (blockState.is(SCBlocks.DISPLAY))
            level.setBlockAndUpdate(getBlockPos(), blockState.setValue(DisplayBlock.HAS_ITEM, false));

        sync();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void sync()
    {
        setChanged();

        if (level instanceof ServerLevel serverLevel)
            serverLevel.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
