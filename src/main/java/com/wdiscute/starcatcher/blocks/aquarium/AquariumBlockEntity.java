package com.wdiscute.starcatcher.blocks.aquarium;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.data.NBTCodecHelper;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.blocks.TickableBlockEntity;
import com.wdiscute.starcatcher.registry.SCEntities;
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
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class AquariumBlockEntity extends BlockEntity implements TickableBlockEntity
{
    ItemStack fish = ItemStack.EMPTY;
    //server synced to client
    public Vec3 fishTarget = new Vec3(0, 0, 0);
    public BlockPos fishTargetBP = BlockPos.ZERO;
    private int cooldown = 0;

    //client only
    public double partialHelper = 0;

    public double x = 0;
    public double y = 0;
    public double z = 0;
    public double fishRotation = 0;

    public AquariumBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SCBlockEntities.AQUARIUM.get(), pos, blockState);
    }

    public void setFish(ItemStack fish)
    {
        this.fish = fish.copy();
    }

    public ItemStack getFish()
    {
        return fish;
    }

    @Override
    public void tick()
    {
        if (getFish().isEmpty()) return;
        //server only
        cooldown--;
        if (!(cooldown < 0)) return;

        cooldown = 150 + Utils.r.nextInt(100);
        Direction dir = Direction.getRandom(level.getRandom());
        BlockPos bp = getBlockPos();
        if (fishTargetBP == BlockPos.ZERO) fishTargetBP = bp;
        BlockPos bpToMoveTo = fishTargetBP;

        for (int i = 0; i < 5; i++)
        {
            BlockState bsToMoveTo = level.getBlockState(bpToMoveTo.relative(dir));
            if (bsToMoveTo.is(SCBlocks.AQUARIUM) && level.getRandom().nextFloat() > 0.5f)
            {
                //only move if decoration allows swimming inside
                if (bsToMoveTo.getValue(AquariumBlock.DECORATION).canFishSwimInside)
                    bpToMoveTo = bpToMoveTo.relative(dir);
            }
        }

        //calculate fish target for client
        fishTarget = new Vec3(
                bpToMoveTo.getX() - bp.getX(),
                bpToMoveTo.getY() - bp.getY(),
                bpToMoveTo.getZ() - bp.getZ()
        );

        //add some randomness to fish target for random fish rotation and positioning
        fishTarget = fishTarget.add(new Vec3(
                Utils.r.nextFloat() / 3 - 0.17f,
                Utils.r.nextFloat() / 2 - 0.4f,
                Utils.r.nextFloat() / 3 - 0.17f
        ));

        setChanged();

        if (level instanceof ServerLevel serverLevel)
            serverLevel.sendBlockUpdated(bp, this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);

        output.store("fish", MaybeStack.CODEC, new MaybeStack(getFish()));

        output.putDouble("fish_target_x", fishTarget.x);
        output.putDouble("fish_target_y", fishTarget.y);
        output.putDouble("fish_target_z", fishTarget.z);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);

        double x = input.getDoubleOr("fish_target_x", 0);
        double y = input.getDoubleOr("fish_target_y", 0);
        double z = input.getDoubleOr("fish_target_z", 0);

        fishTarget = new Vec3(x, y, z);

        fish = input.read("fish", MaybeStack.CODEC).orElse(MaybeStack.EMPTY).toStack();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        if (this.level != null)
        {
            popItem(level, pos);
        }
    }

    private void popItem(Level level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof AquariumBlockEntity abe && !level.isClientSide())
        {
            if (abe.getFish().is(SCTags.BUCKETABLE_FISHES))
            {
                ItemStack itemstack = abe.getFish().copy();
                FishEntity entity = SCEntities.FISH.get().create(level, EntitySpawnReason.TRIGGERED);

                entity.setFish(itemstack);

                entity.setPos(
                        abe.getBlockPos().getX() + abe.fishTarget.x + 0.5F,
                        abe.getBlockPos().getY() + abe.fishTarget.y + 0.5F,
                        abe.getBlockPos().getZ() + abe.fishTarget.z + 0.5F
                );

                level.addFreshEntity(entity);
            }
            else
            {
                ItemStack itemstack = abe.getFish().copy();
                ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + (double) 0.5F, (pos.getY() + 1), (double) pos.getZ() + (double) 0.5F, itemstack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
                abe.setFish(ItemStack.EMPTY);
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        //todo 26
        CompoundTag tag = super.getUpdateTag(registries);

        RegistryOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
        if (!fish.isEmpty())
            tag.store("item", ItemStack.CODEC, ops, getFish());

        tag.putDouble("fish_target_x", fishTarget.x);
        tag.putDouble("fish_target_y", fishTarget.y);
        tag.putDouble("fish_target_z", fishTarget.z);

        return tag;
    }
}
