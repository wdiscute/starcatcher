package com.wdiscute.starcatcher.blocks.aquarium;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.NBTCodecHelper;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.blocks.SCBlockEntities;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.blocks.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

        cooldown = 150 + U.r.nextInt(100);
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
                U.r.nextFloat() / 3 - 0.17f,
                U.r.nextFloat() / 2 - 0.4f,
                U.r.nextFloat() / 3 - 0.17f
        ));

        setChanged();

        if (level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendBlockUpdated(bp, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);

        output.store("fish", SingleStackContainer.CODEC, SingleStackContainer.from(getFish().copy()));

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

        fish = NBTCodecHelper.decode(SingleStackContainer.CODEC, input, "fish", SingleStackContainer::empty).create();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
