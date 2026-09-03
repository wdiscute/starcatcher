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
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class DisplayBlockEntity extends BlockEntity
{
    private MaybeStack item = MaybeStack.EMPTY;

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

    public static void bookAnimationTick(Level level, BlockPos pos, BlockState state, DisplayBlockEntity enchantingTable)
    {
        enchantingTable.oOpen = enchantingTable.open;
        enchantingTable.oRot = enchantingTable.rot;
        Player player = level.getNearestPlayer((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 3.0, false);
        if (player != null)
        {
            double d0;
            double d1;

            //if (SableCompat.isLoaded())
            //    d0 = SableCompat.getPlayerX(player, pos) - ((double) pos.getX() + 0.5);
            //else
                d0 = player.getX() - ((double) pos.getX() + 0.5);

            //if (SableCompat.isLoaded())
            //    d1 = SableCompat.getPlayerZ(player, pos) - ((double) pos.getZ() + 0.5);
            //else
                d1 = player.getZ() - ((double) pos.getZ() + 0.5);

            enchantingTable.tRot = (float) Mth.atan2(d1, d0);
            enchantingTable.open += 0.1F;
            if (enchantingTable.open < 0.5F || Utils.r.nextInt(40) == 0)
            {
                float f1 = enchantingTable.flipT;

                do
                {
                    enchantingTable.flipT = enchantingTable.flipT + (float) (Utils.r.nextInt(4) - Utils.r.nextInt(4));
                } while (f1 == enchantingTable.flipT);
            }
        }
        else
            //enchantingTable.tRot += 0.02F;
            enchantingTable.open -= 0.1F;

        while (enchantingTable.rot >= (float) Math.PI)
            enchantingTable.rot -= (float) (Math.PI * 2);

        while (enchantingTable.rot < (float) -Math.PI)
            enchantingTable.rot += (float) (Math.PI * 2);

        while (enchantingTable.tRot >= (float) Math.PI)
            enchantingTable.tRot -= (float) (Math.PI * 2);

        while (enchantingTable.tRot < (float) -Math.PI)
            enchantingTable.tRot += (float) (Math.PI * 2);

        float f2 = enchantingTable.tRot - enchantingTable.rot;

        while (f2 >= (float) Math.PI)
            f2 -= (float) (Math.PI * 2);

        while (f2 < (float) -Math.PI)
            f2 += (float) (Math.PI * 2);

        enchantingTable.rot += f2 * 0.4F;
        enchantingTable.open = Mth.clamp(enchantingTable.open, 0.0F, 1.0F);
        enchantingTable.time++;
        enchantingTable.oFlip = enchantingTable.flip;
        float f = (enchantingTable.flipT - enchantingTable.flip) * 0.4F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        enchantingTable.flipA = enchantingTable.flipA + (f - enchantingTable.flipA) * 0.9F;
        enchantingTable.flip = enchantingTable.flip + enchantingTable.flipA;
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

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
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
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        CompoundTag tag = super.getUpdateTag(registries);

        RegistryOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);

        tag.store("item", MaybeStack.CODEC, ops, this.item);

        tag.putBoolean("rotating", fishRotating);

        return tag;
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

    public void clearContent()
    {
        item = MaybeStack.EMPTY;
        BlockState blockState = level.getBlockState(getBlockPos());
        if (blockState.is(SCBlocks.DISPLAY))
            level.setBlockAndUpdate(getBlockPos(), blockState.setValue(DisplayBlock.HAS_ITEM, false));

        sync();
    }

    public void sync()
    {
        setChanged();

        if (level instanceof ServerLevel serverLevel)
            serverLevel.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
