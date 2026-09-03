package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.blocks.TickableBlockEntity;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TackleBoxBlockEntity extends BlockEntity implements WorldlyContainer, TickableBlockEntity, MenuProvider
{
    private NonNullList<ItemStack> itemStacks;
    private List<ItemStack> fishes;
    public int openCount;
    @Nullable
    private final DyeColor color;
    private Component name = Component.empty();

    @Override
    public void tick()
    {
        if (getItem(TackleBoxMenu.FISH_SLOT).isEmpty() && !fishes.isEmpty()) updateFishSlot();
    }

    public void updateFishSlot()
    {
        if (level.isClientSide()) return;

        //store & remove fish placed
        ItemStack itemInFishSlot = getItem(TackleBoxMenu.FISH_SLOT);
        //only runs if fishes stored is less than the max set by config
        for (int i = 5; SCConfig.MAX_TACKLE_BOX_FISH_STORAGE.get() - 1 > fishes.size() && i < itemStacks.size(); i++)
        {
            ItemStack item = itemStacks.get(i).copy();
            if (item.is(SCTags.FISHABLE))
            {
                //if slot is not empty, add the fish previously there to stored fishes
                if (!itemInFishSlot.isEmpty())
                    fishes.add(itemInFishSlot.copy());
                //remove the fish item found and put it on the fish slot
                itemStacks.get(i).setCount(0);
                itemStacks.set(i, ItemStack.EMPTY);
                setItem(i, ItemStack.EMPTY);
                setItem(TackleBoxMenu.FISH_SLOT, item);
                break;
            }
        }

        //refill fish slot from fishes stored
        if (itemInFishSlot.isEmpty() && !fishes.isEmpty())
        {
            setItem(TackleBoxMenu.FISH_SLOT, fishes.getLast());
            fishes.removeLast();
        }

        //todo re-resourceLocation fishes stored for better hopper and stuff interaction

        setChanged();
        if (level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public int getContainerSize()
    {
        return this.itemStacks.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.getItems())
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int i)
    {
        return this.getItems().get(i);
    }

    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        ItemStack itemstack = ContainerHelper.removeItem(this.getItems(), slot, amount);
        if (!itemstack.isEmpty())
        {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        return ContainerHelper.takeItem(this.getItems(), slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        this.getItems().set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player)
    {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction)
    {
        updateFishSlot();
        return slot >= 5 && direction != Direction.DOWN;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction)
    {
        updateFishSlot();
        return direction == Direction.DOWN && slot > 4;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack)
    {
        return WorldlyContainer.super.canPlaceItem(slot, stack);
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
                this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.2F, this.level.getRandom().nextFloat() * 0.1F + 0.9F);
                this.level.playSound(null, this.worldPosition, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.2F, this.level.getRandom().nextFloat() * 0.1F + 0.9F);
                this.level.playSound(null, this.worldPosition, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 0.2F, this.level.getRandom().nextFloat() * 0.1F + 0.4F);
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
                this.level.playSound(null, this.worldPosition, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.2F, this.level.getRandom().nextFloat() * 0.1F + 0.9F);
                this.level.playSound(null, this.worldPosition, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 0.2F, this.level.getRandom().nextFloat() * 0.1F + 0.4F);
                this.level.playSound(null, this.worldPosition, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.3F, this.level.getRandom().nextFloat() * 0.1F + 0.4F);
            }
        }

    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentInput)
    {
        super.applyImplicitComponents(componentInput);
        componentInput.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.getItems());
        fishes = new ArrayList<>(componentInput.getOrDefault(SCDataComponents.TACKLE_BOX_FISHES, List.of()));
        this.name = componentInput.getOrDefault(DataComponents.CUSTOM_NAME, Component.empty());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components)
    {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.name);
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
        components.set(SCDataComponents.TACKLE_BOX_FISHES, fishes);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);

        this.name = input.read("CustomName", ComponentSerialization.CODEC).orElse(Component.empty());
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);

        //save normal slots
        ContainerHelper.saveAllItems(output, this.itemStacks);

        output.store("CustomName", ComponentSerialization.CODEC, name);
    }

    protected NonNullList<ItemStack> getItems()
    {
        return this.itemStacks;
    }

    @Nullable
    public DyeColor getColor()
    {
        return this.color;
    }

    public TackleBoxBlockEntity(@Nullable DyeColor color, BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.TACKLE_BOX.get(), pos, blockState);
        this.itemStacks = NonNullList.withSize(TackleBoxMenu.CONTAINER_SIZE, ItemStack.EMPTY);
        this.fishes = new ArrayList<>();
        this.color = color;
    }

    public TackleBoxBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.TACKLE_BOX.get(), pos, blockState);
        this.itemStacks = NonNullList.withSize(TackleBoxMenu.CONTAINER_SIZE, ItemStack.EMPTY);
        this.fishes = new ArrayList<>();
        this.color = TackleBoxBlock.getColorFromBlock(blockState.getBlock());
    }

    @Override
    public void clearContent()
    {
        this.getItems().clear();
    }

    @Override
    public Component getDisplayName()
    {
        return Component.translatable("block.starcatcher.tackle_box");
    }

    @Override
    public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
    {
        if (!player.isSpectator())
            return new TackleBoxMenu(containerId, playerInventory, this, this);
        else
            return null;
    }
}
