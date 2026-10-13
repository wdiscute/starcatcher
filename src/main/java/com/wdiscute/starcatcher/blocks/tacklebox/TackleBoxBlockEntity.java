package com.wdiscute.starcatcher.blocks.tacklebox;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.ValueHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.nikdo53.neobackports.io.components.DataComponents;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TackleBoxBlockEntity extends BlockEntity implements MenuProvider, TackleBoxWorldlyContainerHelper
{
    Codec<List<Utils.Duo<Integer, MaybeStack>>> ITEMS_CODEC = Utils.Duo.codec(Codec.INT, MaybeStack.CODEC).listOf();
    public TackleBoxContainer container = new TackleBoxContainer()
    {
        @Override
        public void setChanged()
        {
            TackleBoxBlockEntity.this.setChanged();
        }

        @Override
        public boolean stillValid(Player player)
        {
            return Container.stillValidBlockEntity(TackleBoxBlockEntity.this, player);
        }

        @Override
        public void startOpen(Player player)
        {
            TackleBoxBlockEntity.this.startOpen(player);
        }

        @Override
        public void stopOpen(Player player)
        {
            TackleBoxBlockEntity.this.stopOpen(player);
        }
    };

    public int openCount;
    @Nullable
    private final DyeColor color;
    private Component name;

    @Override
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

    @Override
    public void startOpen(Player player)
    {
        if (!this.remove && !player.isSpectator())
        {
            if (this.openCount < 0)
                this.openCount = 0;

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

    @Override
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

    public void applyImplicitComponents(ItemStack componentInput)
    {
        //apply fishes
        container.fishes.clear();
        for (MaybeStack fish : componentInput.getOrDefault(SCDataComponents.TACKLE_BOX_FISHES, List.of()))
            container.fishes.add(fish.toStack());

        //apply items
        for (Utils.Duo<Integer, MaybeStack> duo : componentInput.getOrDefault(SCDataComponents.TACKLE_BOX_ITEMS, List.of()))
            container.items.put(duo.first(), duo.second().toStack());

        //apply name
        if (componentInput.hasCustomHoverName())
            this.name = componentInput.getHoverName();
    }

    @Override
    public void saveToItem(ItemStack stack)
    {
        //save fishes to item
        List<MaybeStack> fishes = container.fishes.stream().filter(o -> !o.isEmpty()).map(MaybeStack::new).toList();
        stack.set(SCDataComponents.TACKLE_BOX_FISHES.get(), fishes);

        //apply items
        List<Utils.Duo<Integer, MaybeStack>> items = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> entry : container.items.entrySet())
            items.add(new Utils.Duo<>(entry.getKey(), new MaybeStack(entry.getValue())));
        stack.set(SCDataComponents.TACKLE_BOX_ITEMS, items);

        //apply name
        if (name != null)
            this.name = stack.get(DataComponents.CUSTOM_NAME.get());
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);

        if (tag.contains("CustomName", 8))
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));

        //load normal slots
        for (Utils.Duo<Integer, MaybeStack> duo : ValueHelper.read("Items", ITEMS_CODEC, tag).orElse(List.of()))
            container.items.put(duo.first(), duo.second().toStack());

        container.fishes.clear();
        for (MaybeStack fish : ValueHelper.read("Fishes", MaybeStack.CODEC.listOf(), tag).orElse(List.of()))
            container.fishes.add(fish.toStack());
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);

        //store fishes
        ValueHelper.store("Fishes", MaybeStack.CODEC.listOf(), container.fishes.stream().map(MaybeStack::new).toList(), tag);

        //store items
        List<Utils.Duo<Integer, MaybeStack>> list = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> entry : container.items.entrySet())
            list.add(new Utils.Duo<>(entry.getKey(), new MaybeStack(entry.getValue())));
        ValueHelper.store("Items", ITEMS_CODEC, list, tag);

        if (this.name != null)
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
    }

    @Nullable
    public DyeColor getColor()
    {
        return this.color;
    }

    public TackleBoxBlockEntity(@Nullable DyeColor color, BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.TACKLE_BOX.get(), pos, blockState);
        this.color = color;
    }

    public TackleBoxBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.TACKLE_BOX.get(), pos, blockState);
        this.color = TackleBoxBlock.getColorFromBlock(blockState.getBlock());
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
            return new TackleBoxMenu(containerId, playerInventory, container);
        else
            return null;
    }

    @Override
    public TackleBoxContainer getTackleBoxContainer()
    {
        return container;
    }
}
