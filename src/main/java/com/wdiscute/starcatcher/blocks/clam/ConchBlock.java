package com.wdiscute.starcatcher.blocks.clam;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ConchBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public ConchBlock(Properties p)
    {
        super(p
                .destroyTime(0.2f)
                .noOcclusion()
                .noCollision()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.BONE_BLOCK)
        );
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Block.box(3, 0, 3, 13, 3, 13);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if(!level.isClientSide())
        {
            level.destroyBlock(pos, true, player);

            if (state.getValue(BlockStateProperties.WATERLOGGED))
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected FluidState getFluidState(BlockState state)
    {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec()
    {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState bs = defaultBlockState();
        bs = bs.setValue(FACING, context.getHorizontalDirection());
        bs = bs.setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
        return bs;
    }
}
