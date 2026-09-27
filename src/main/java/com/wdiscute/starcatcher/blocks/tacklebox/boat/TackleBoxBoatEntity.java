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
import com.wdiscute.utils.Utils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TackleBoxBoatEntity extends Boat implements HasCustomInventoryScreen, TackleBoxWorldlyContainerHelper, MenuProvider
{
    public static final EntityDataAccessor<Integer> BOX_COLOR = SynchedEntityData.defineId(TackleBoxBoatEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> OPEN_COUNTER = SynchedEntityData.defineId(TackleBoxBoatEntity.class, EntityDataSerializers.INT);
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
            return !TackleBoxBoatEntity.this.isRemoved() && player.canInteractWithEntity(TackleBoxBoatEntity.this.getBoundingBox(), 4.0);
        }

        @Override
        public void startOpen(Player player)
        {
            TackleBoxBoatEntity.this.startOpen(player);
        }

        @Override
        public void stopOpen(Player player)
        {
            TackleBoxBoatEntity.this.stopOpen(player);
        }
    };

    public TackleBoxBoatEntity(EntityType<? extends Boat> entityType, Level level)
    {
        super(entityType, level);
        closeAnimationState.startIfStopped(-20);
    }

    public TackleBoxBoatEntity(Level level, double x, double y, double z)
    {
        super(SCEntities.TACKLE_BOX_BOAT_ENTITY.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        super.defineSynchedData(builder);
        builder.define(OPEN_COUNTER, 0);
        builder.define(BOX_COLOR, 0);
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
    protected void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);

        tag.putInt("color", entityData.get(BOX_COLOR));

        //store fishes
        tag.put("Fishes", MaybeStack.CODEC.listOf().encodeStart(NbtOps.INSTANCE, container.fishes.stream().map(MaybeStack::new).toList()).getOrThrow());

        //store items
        List<Utils.Duo<Integer, MaybeStack>> list = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> entry : container.items.entrySet())
            list.add(new Utils.Duo<>(entry.getKey(), new MaybeStack(entry.getValue())));
        tag.put("Items", ITEMS_CODEC.encodeStart(NbtOps.INSTANCE, list).getOrThrow());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);

        if (tag.contains("color"))
            entityData.set(BOX_COLOR, tag.getInt("color"));

        //load normal slots
        if (tag.contains("Items"))
        {
            for (Utils.Duo<Integer, MaybeStack> duo : ITEMS_CODEC.parse(NbtOps.INSTANCE, tag.get("Items")).getOrThrow())
                container.items.put(duo.first(), duo.second().toStack());
        }

        if (tag.contains("Fishes"))
        {
            container.fishes.clear();
            for (MaybeStack fish : MaybeStack.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("Fishes")).getOrThrow())
                container.fishes.add(fish.toStack());
        }
    }

    @Override
    public void destroy(DamageSource source)
    {
        this.kill();
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS))
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

            this.spawnAtLocation(stack);
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
    public InteractionResult interact(Player player, InteractionHand hand)
    {
        if (!player.isSecondaryUseActive())
        {
            InteractionResult interactionresult = super.interact(player, hand);
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
            return !player.level().isClientSide ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
        }
    }

    @Override
    public void openCustomInventoryScreen(Player player)
    {
        player.openMenu(this);
    }

    @Override
    public Item getDropItem()
    {
        return switch (this.getVariant())
        {
            case SPRUCE -> SCItems.SPRUCE_TACKLE_BOX_BOAT.get();
            case BIRCH -> SCItems.BIRCH_TACKLE_BOX_BOAT.get();
            case JUNGLE -> SCItems.JUNGLE_TACKLE_BOX_BOAT.get();
            case ACACIA -> SCItems.ACACIA_TACKLE_BOX_BOAT.get();
            case CHERRY -> SCItems.CHERRY_TACKLE_BOX_BOAT.get();
            case DARK_OAK -> SCItems.DARK_OAK_TACKLE_BOX_BOAT.get();
            case MANGROVE -> SCItems.MANGROVE_TACKLE_BOX_BOAT.get();
            case BAMBOO -> SCItems.BAMBOO_TACKLE_BOX_BOAT.get();
            default -> SCItems.OAK_TACKLE_BOX_BOAT.get();
        };
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
    public void stopOpen(Player player)
    {
        TackleBoxWorldlyContainerHelper.super.stopOpen(player);
        entityData.set(OPEN_COUNTER, Math.max(entityData.get(OPEN_COUNTER) - 1, 0));

        level().playSound(null, blockPosition(), SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.2F, level().random.nextFloat() * 0.1F + 0.9F);
        level().playSound(null, blockPosition(), SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 0.2F, level().random.nextFloat() * 0.1F + 0.4F);
        level().playSound(null, blockPosition(), SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.3F, level().random.nextFloat() * 0.1F + 0.4F);
    }

    @Override
    public void startOpen(Player player)
    {
        TackleBoxWorldlyContainerHelper.super.startOpen(player);
        entityData.set(OPEN_COUNTER, entityData.get(OPEN_COUNTER) + 1);

        level().playSound(null, blockPosition(), SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.2F, level().random.nextFloat() * 0.1F + 0.9F);
        level().playSound(null, blockPosition(), SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.2F, level().random.nextFloat() * 0.1F + 0.9F);
        level().playSound(null, blockPosition(), SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 0.2F, level().random.nextFloat() * 0.1F + 0.4F);
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
