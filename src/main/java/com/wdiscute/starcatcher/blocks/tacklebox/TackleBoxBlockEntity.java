package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.blocks.SCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TackleBoxBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer
{
    private static final int[] SLOTS = IntStream.range(0, 27).toArray();
    private NonNullList<ItemStack> itemStacks;
    private int openCount;
    @Nullable
    private final DyeColor color;

    public TackleBoxBlockEntity(@Nullable DyeColor color, BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.TACKLE_BOX.get(), pos, blockState);
        this.itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
        this.color = color;
    }

    public TackleBoxBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.TACKLE_BOX.get(), pos, blockState);
        this.itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
        this.color = TackleBoxBlock.getColorFromBlock(blockState.getBlock());
    }

    public int getContainerSize()
    {
        return this.itemStacks.size();
    }

    public boolean triggerEvent(int id, int type)
    {
        if (id == 1)
        {
            this.openCount = type;
            return true;
        }
        else
        {
            return super.triggerEvent(id, type);
        }
    }

    public void startOpen(Player player)
    {
        if (!this.remove && !player.isSpectator())
        {
            if (this.openCount < 0)
            {
                this.openCount = 0;
            }

            ++this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount == 1)
            {
                this.level.gameEvent(player, GameEvent.CONTAINER_OPEN, this.worldPosition);
                this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.2F, this.level.random.nextFloat() * 0.1F + 0.9F);
                this.level.playSound(null, this.worldPosition, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.2F, this.level.random.nextFloat() * 0.1F + 0.9F);
                this.level.playSound(null, this.worldPosition, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 0.2F, this.level.random.nextFloat() * 0.1F + 0.4F);
            }
        }

    }

    public void stopOpen(Player player)
    {
        if (!this.remove && !player.isSpectator())
        {
            --this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount <= 0)
            {
                this.level.gameEvent(player, GameEvent.CONTAINER_CLOSE, this.worldPosition);
                this.level.playSound(null, this.worldPosition, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.2F, this.level.random.nextFloat() * 0.1F + 0.9F);
                this.level.playSound(null, this.worldPosition, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 0.2F, this.level.random.nextFloat() * 0.1F + 0.4F);
                this.level.playSound(null, this.worldPosition, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.3F, this.level.random.nextFloat() * 0.1F + 0.4F);
            }
        }

    }

    protected Component getDefaultName()
    {
        return Component.translatable("block.starcatcher.tackle_box");
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        this.loadFromTag(tag, registries);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        if (!this.trySaveLootTable(tag))
        {
            ContainerHelper.saveAllItems(tag, this.itemStacks, false);
        }
    }

    public void loadFromTag(CompoundTag tag, HolderLookup.Provider levelRegistry)
    {
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag) && tag.contains("Items", 9))
        {
            ContainerHelper.loadAllItems(tag, this.itemStacks);
        }

    }

    protected NonNullList<ItemStack> getItems()
    {
        return this.itemStacks;
    }

    protected void setItems(NonNullList<ItemStack> items)
    {
        this.itemStacks = items;
    }

    public int[] getSlotsForFace(Direction side)
    {
        return SLOTS;
    }

    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction)
    {
        return !(Block.byItem(itemStack.getItem()) instanceof TackleBoxBlock) && itemStack.getItem().canFitInsideContainerItems();
    }

    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return true;
    }


    @Nullable
    public DyeColor getColor()
    {
        return this.color;
    }

    protected AbstractContainerMenu createMenu(int id, Inventory player)
    {
        return new TackleBoxMenu(id, player, this);
    }
}
