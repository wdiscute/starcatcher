package com.wdiscute.starcatcher.blocks.tacklebox;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.blocks.TickableBlockEntity;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.ContainerUser;
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
        public void startOpen(ContainerUser containerUser)
        {
            TackleBoxBlockEntity.this.startOpen(containerUser);
        }

        @Override
        public void stopOpen(ContainerUser containerUser)
        {
            TackleBoxBlockEntity.this.stopOpen(containerUser);
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
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
    }

    @Override
    public void startOpen(ContainerUser player)
    {
        if (!this.remove && !player.getLivingEntity().isSpectator())
        {
            if (this.openCount < 0)
                this.openCount = 0;

            ++this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount == 1)
            {
                this.level.gameEvent(player.getLivingEntity(), GameEvent.CONTAINER_OPEN, this.worldPosition);
                this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.2F, this.level.getRandom().nextFloat() * 0.1F + 0.9F);
                this.level.playSound(null, this.worldPosition, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.2F, this.level.getRandom().nextFloat() * 0.1F + 0.9F);
                this.level.playSound(null, this.worldPosition, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 0.2F, this.level.getRandom().nextFloat() * 0.1F + 0.4F);
            }
        }
    }

    @Override
    public void stopOpen(ContainerUser player)
    {
        if (!this.remove && !player.getLivingEntity().isSpectator())
        {
            --this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount <= 0)
            {
                this.level.gameEvent(player.getLivingEntity(), GameEvent.CONTAINER_CLOSE, this.worldPosition);
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

        //apply fishes
        container.fishes.clear();
        for (MaybeStack fish : componentInput.getOrDefault(SCDataComponents.TACKLE_BOX_FISHES, List.<MaybeStack>of()))
            container.fishes.add(fish.toStack());

        //apply items
        for (Utils.Duo<Integer, MaybeStack> duo : componentInput.getOrDefault(SCDataComponents.TACKLE_BOX_ITEMS, List.<Utils.Duo<Integer, MaybeStack>>of()))
            container.items.put(duo.first(), duo.second().toStack());

        //apply name
        this.name = componentInput.get(DataComponents.CUSTOM_NAME);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components)
    {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.name);

        //store fishes
        components.set(SCDataComponents.TACKLE_BOX_FISHES, container.fishes.stream().map(MaybeStack::new).toList());

        //store items
        List<Utils.Duo<Integer, MaybeStack>> list = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> entry : container.items.entrySet())
            list.add(new Utils.Duo<>(entry.getKey(), new MaybeStack(entry.getValue())));
        components.set(SCDataComponents.TACKLE_BOX_ITEMS, list);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);

        Component customName = parseCustomNameSafe(input, "CustomName");
        if (customName != null)
            this.name = customName;
        else
            this.name = null;

        //load normal slots
        for (Utils.Duo<Integer, MaybeStack> duo : input.read("Items", ITEMS_CODEC).orElse(List.of()))
            container.items.put(duo.first(), duo.second().toStack());

        container.fishes.clear();
        for (MaybeStack fish : input.read("Fishes", MaybeStack.CODEC.listOf()).orElse(List.of()))
            container.fishes.add(fish.toStack());
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);

        //store fishes
        output.store("Fishes", MaybeStack.CODEC.listOf(), container.fishes.stream().map(MaybeStack::new).toList());

        //store items
        List<Utils.Duo<Integer, MaybeStack>> list = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> entry : container.items.entrySet())
            list.add(new Utils.Duo<>(entry.getKey(), new MaybeStack(entry.getValue())));
        output.store("Items", ITEMS_CODEC, list);

        if (name != null)
            output.store("CustomName", ComponentSerialization.CODEC, name);
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
