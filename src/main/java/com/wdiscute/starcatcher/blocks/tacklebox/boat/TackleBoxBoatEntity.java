package com.wdiscute.starcatcher.blocks.tacklebox.boat;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxContainer;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxMenu;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxWorldlyContainerHelper;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.StringRepresentableAutoForEnums;
import com.wdiscute.utils.Utils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TackleBoxBoatEntity extends Boat implements HasCustomInventoryScreen, TackleBoxWorldlyContainerHelper, MenuProvider
{
    public static final EntityDataAccessor<Integer> BOX_COLOR = SynchedEntityData.defineId(TackleBoxBoatEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> OPEN_COUNTER = SynchedEntityData.defineId(TackleBoxBoatEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> WOOD_TYPE = SynchedEntityData.defineId(TackleBoxBoatEntity.class, EntityDataSerializers.INT);
    public final AnimationState openAnimationState = new AnimationState();
    public final AnimationState closeAnimationState = new AnimationState();
    Codec<List<Utils.Duo<Integer, MaybeStack>>> ITEMS_CODEC = Utils.Duo.codec(Codec.INT, MaybeStack.CODEC).listOf();
    public TackleBoxContainer container = new TackleBoxContainer()
    {
        @Override
        public void setChanged()
        {
            TackleBoxBoatEntity.this.setChanged();
        }

        @Override
        public boolean stillValid(Player player)
        {
            return !TackleBoxBoatEntity.this.isRemoved() && player.position().distanceTo(position()) <= 8.0;
        }

        @Override
        public void startOpen(ContainerUser player)
        {
            TackleBoxBoatEntity.this.startOpen(player);
        }

        @Override
        public void stopOpen(ContainerUser player)
        {
            TackleBoxBoatEntity.this.stopOpen(player);
        }
    };

    public enum Type implements StringRepresentableAutoForEnums
    {
        OAK(0, SCItems.OAK_TACKLE_BOX_BOAT, false),
        SPRUCE(1, SCItems.OAK_TACKLE_BOX_BOAT, false),
        BIRCH(2, SCItems.BIRCH_TACKLE_BOX_BOAT, false),
        JUNGLE(3, SCItems.JUNGLE_TACKLE_BOX_BOAT, false),
        ACACIA(4, SCItems.ACACIA_TACKLE_BOX_BOAT, false),
        CHERRY(5, SCItems.CHERRY_TACKLE_BOX_BOAT, false),
        DARK_OAK(6, SCItems.DARK_OAK_TACKLE_BOX_BOAT, false),
        MANGROVE(7, SCItems.MANGROVE_TACKLE_BOX_BOAT, false),
        BAMBOO(8, SCItems.BAMBOO_TACKLE_BOX_BOAT, true),
        PALE_OAK(9, SCItems.PALE_OAK_TACKLE_BOX_BOAT, false);

        final int index;
        final Supplier<Item> supplier;
        final boolean isRaft;

        Type(int index, Supplier<Item> supplier, boolean isRaft)
        {
            this.index = index;
            this.supplier = supplier;
            this.isRaft = isRaft;
        }

        static Type byType(int i)
        {
            for (Type value : values())
            {
                if (value.index == i)
                    return value;
            }
            return OAK;
        }

        public boolean isRaft()
        {
            return isRaft;
        }
    }

    public TackleBoxBoatEntity(EntityType<? extends Boat> entityType, Level level)
    {
        super(entityType, level, SCItems.OAK_TACKLE_BOX_BOAT);
        closeAnimationState.startIfStopped(-20);
    }

    public TackleBoxBoatEntity(Level level, double x, double y, double z, Type type)
    {
        super(SCEntities.TACKLE_BOX_BOAT_ENTITY.get(), level, type.supplier);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        entityData.set(WOOD_TYPE, type.index);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        super.defineSynchedData(builder);
        builder.define(OPEN_COUNTER, 0);
        builder.define(BOX_COLOR, 0);
        builder.define(WOOD_TYPE, 0);
    }

    @Override
    protected float getSinglePassengerXOffset()
    {
        return 0.15F;
    }

    @Override
    protected int getMaxPassengers()
    {
        return 1;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        super.addAdditionalSaveData(output);

        output.putInt("color", entityData.get(BOX_COLOR));
        output.putInt("wood_type", entityData.get(WOOD_TYPE));

        //store fishes
        output.store("Fishes", MaybeStack.CODEC.listOf(), container.fishes.stream().map(MaybeStack::new).toList());

        //store items
        List<Utils.Duo<Integer, MaybeStack>> list = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> entry : container.items.entrySet())
            list.add(new Utils.Duo<>(entry.getKey(), new MaybeStack(entry.getValue())));
        output.store("Items", ITEMS_CODEC, list);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        super.readAdditionalSaveData(input);

        entityData.set(BOX_COLOR, input.getIntOr("color", 0));
        entityData.set(WOOD_TYPE, input.getIntOr("wood_type", 0));

        //load normal slots
        for (Utils.Duo<Integer, MaybeStack> duo : input.read("Items", ITEMS_CODEC).orElse(List.of()))
            container.items.put(duo.first(), duo.second().toStack());

        container.fishes.clear();
        for (MaybeStack fish : input.read("Fishes", MaybeStack.CODEC.listOf()).orElse(List.of()))
            container.fishes.add(fish.toStack());
    }

    @Override
    public void destroy(ServerLevel level, Item dropItem)
    {
        this.kill(level);
        if (level.getGameRules().get(GameRules.ENTITY_DROPS))
        {
            //get stack of boat + color
            ItemStack stack = getPickResult();

            //store custom name
            stack.set(DataComponents.CUSTOM_NAME, this.getCustomName());

            //store fishes in dropped item
            SCDataComponents.set(stack, SCDataComponents.TACKLE_BOX_FISHES, container.fishes.stream().map(MaybeStack::new).toList());

            //store items in dropped item
            List<Utils.Duo<Integer, MaybeStack>> list = new ArrayList<>();
            for (Map.Entry<Integer, ItemStack> entry : container.items.entrySet())
                list.add(new Utils.Duo<>(entry.getKey(), new MaybeStack(entry.getValue())));
            SCDataComponents.set(stack, SCDataComponents.TACKLE_BOX_ITEMS, list);

            this.spawnAtLocation(level, stack);
        }
    }

    @Override
    public ItemStack getPickResult()
    {
        ItemStack stack = super.getPickResult();
        SCDataComponents.set(stack, SCDataComponents.TACKLE_BOX_COLOR, DyeColor.byId(entityData.get(BOX_COLOR)));
        return stack;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location)
    {
        if (!player.isSecondaryUseActive())
        {
            InteractionResult interactionresult = super.interact(player, hand, location);
            if (interactionresult != InteractionResult.PASS)
                return interactionresult;
        }

        if (this.canAddPassenger(player) && !player.isSecondaryUseActive())
        {
            return InteractionResult.PASS;
        }
        else
        {
            player.openMenu(this);
            return !player.level().isClientSide() ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
        }
    }

    @Override
    public void openCustomInventoryScreen(Player player)
    {
        player.openMenu(this);
    }

    @Override
    public TackleBoxContainer getTackleBoxContainer()
    {
        return container;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
    {
        if (!player.isSpectator())
            return new TackleBoxMenu(containerId, playerInventory, container);
        else
            return null;
    }

    @Override
    public void setChanged()
    {
    }

    @Override
    public void stopOpen(ContainerUser containerUser)
    {
        TackleBoxWorldlyContainerHelper.super.stopOpen(containerUser);
        entityData.set(OPEN_COUNTER, Math.max(entityData.get(OPEN_COUNTER) - 1, 0));

        level().playSound(null, blockPosition(), SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.2F, level().getRandom().nextFloat() * 0.1F + 0.9F);
        level().playSound(null, blockPosition(), SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 0.2F, level().getRandom().nextFloat() * 0.1F + 0.4F);
        level().playSound(null, blockPosition(), SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.3F, level().getRandom().nextFloat() * 0.1F + 0.4F);
    }

    @Override
    public void startOpen(ContainerUser player)
    {
        TackleBoxWorldlyContainerHelper.super.startOpen(player);
        entityData.set(OPEN_COUNTER, entityData.get(OPEN_COUNTER) + 1);

        level().playSound(null, blockPosition(), SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.2F, level().getRandom().nextFloat() * 0.1F + 0.9F);
        level().playSound(null, blockPosition(), SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.2F, level().getRandom().nextFloat() * 0.1F + 0.9F);
        level().playSound(null, blockPosition(), SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 0.2F, level().getRandom().nextFloat() * 0.1F + 0.4F);
    }

    @Override
    public void tick()
    {
        super.tick();

        //if should be open
        if (entityData.get(OPEN_COUNTER) > 0)
        {
            openAnimationState.startIfStopped(tickCount);
            closeAnimationState.stop();
        }
        else
        {
            closeAnimationState.startIfStopped(tickCount);
            openAnimationState.stop();
        }
    }
}
