package com.wdiscute.starcatcher.blocks.aquarium;

import com.wdiscute.starcatcher.data.NBTCodecHelper;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.blocks.TickableBlockEntity;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
        Direction dir = Direction.getRandom(level.random);
        BlockPos bp = getBlockPos();
        if (fishTargetBP == BlockPos.ZERO) fishTargetBP = bp;
        BlockPos bpToMoveTo = fishTargetBP;

        for (int i = 0; i < 5; i++)
        {
            BlockState bsToMoveTo = level.getBlockState(bpToMoveTo.relative(dir));
            if (bsToMoveTo.is(SCBlocks.AQUARIUM) && level.random.nextFloat() > 0.5f)
            {
                //only move if decoration allows swimming inside
                if(bsToMoveTo.getValue(AquariumBlock.DECORATION).canFishSwimInside) bpToMoveTo = bpToMoveTo.relative(dir);
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
        {
            serverLevel.sendBlockUpdated(bp, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);

        NBTCodecHelper.encode(MaybeStack.CODEC, new MaybeStack(getFish().copy()), tag, "fish");

        tag.putDouble("fish_target_x", fishTarget.x);
        tag.putDouble("fish_target_y", fishTarget.y);
        tag.putDouble("fish_target_z", fishTarget.z);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);

        double x = 0;
        double y = 0;
        double z = 0;

        if (tag.contains("fish_target_x")) x = tag.getDouble("fish_target_x");
        if (tag.contains("fish_target_y")) y = tag.getDouble("fish_target_y");
        if (tag.contains("fish_target_z")) z = tag.getDouble("fish_target_z");

        fishTarget = new Vec3(x, y, z);

        fish = NBTCodecHelper.decode(MaybeStack.CODEC, tag, "fish").toStack();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }
}
