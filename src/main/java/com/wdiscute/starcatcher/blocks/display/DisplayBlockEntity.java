package com.wdiscute.starcatcher.blocks.display;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.blocks.SCBlockEntities;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DisplayBlockEntity extends BlockEntity
{
    private ItemStack item = ItemStack.EMPTY;

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

    public static void bookAnimationTick(Level level, BlockPos pos, BlockState state, DisplayBlockEntity enchantingTable)
    {
        enchantingTable.oOpen = enchantingTable.open;
        enchantingTable.oRot = enchantingTable.rot;
        Player player = level.getNearestPlayer((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 3.0, false);
        if (player != null)
        {
            double d0 = player.getX() - ((double) pos.getX() + 0.5);
            double d1 = player.getZ() - ((double) pos.getZ() + 0.5);
            enchantingTable.tRot = (float) Mth.atan2(d1, d0);
            enchantingTable.open += 0.1F;
            if (enchantingTable.open < 0.5F || U.r.nextInt(40) == 0)
            {
                float f1 = enchantingTable.flipT;

                do
                {
                    enchantingTable.flipT = enchantingTable.flipT + (float) (U.r.nextInt(4) - U.r.nextInt(4));
                } while (f1 == enchantingTable.flipT);
            }
        }
        else
        {
            //enchantingTable.tRot += 0.02F;
            enchantingTable.open -= 0.1F;
        }

        while (enchantingTable.rot >= (float) Math.PI)
        {
            enchantingTable.rot -= (float) (Math.PI * 2);
        }

        while (enchantingTable.rot < (float) -Math.PI)
        {
            enchantingTable.rot += (float) (Math.PI * 2);
        }

        while (enchantingTable.tRot >= (float) Math.PI)
        {
            enchantingTable.tRot -= (float) (Math.PI * 2);
        }

        while (enchantingTable.tRot < (float) -Math.PI)
        {
            enchantingTable.tRot += (float) (Math.PI * 2);
        }

        float f2 = enchantingTable.tRot - enchantingTable.rot;

        while (f2 >= (float) Math.PI)
        {
            f2 -= (float) (Math.PI * 2);
        }

        while (f2 < (float) -Math.PI)
        {
            f2 += (float) (Math.PI * 2);
        }

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

    public ItemStack getItem()
    {
        return this.item;
    }

    public void setItem(ItemStack stack)
    {
        this.item = stack;
        this.setChanged();
    }


    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
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
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        if(level.isClientSide) System.out.println("RAN ON CLIENT!!!");
        if (tag.contains("Book"))
        {
            this.item = ItemStack.parse(registries, tag.getCompound("Book")).orElse(ItemStack.EMPTY);
        }
        else
        {
            this.item = ItemStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        if (!this.getItem().isEmpty())
        {
            tag.put("Book", this.getItem().save(registries));
        }else
        {
            //need to put a tag otherwise its not sent to client since the tag is empty
            tag.putBoolean("empty", true);
        }
    }

    public void clearContent()
    {
        item = ItemStack.EMPTY;
        BlockState blockState = level.getBlockState(getBlockPos());
        if (blockState.is(SCBlocks.DISPLAY))
            level.setBlockAndUpdate(getBlockPos(), blockState.setValue(DisplayBlock.HAS_ITEM, false));

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
}
