package com.wdiscute.starcatcher.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TrophyBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public TrophyBlock()
    {
        super(BlockBehaviour.Properties.of()
                .noOcclusion()
                .lightLevel((state) -> 5)
                .isSuffocating()
                .sound(SoundType.AMETHYST)
        );
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {

        if (state.getValue(FACING) == Direction.WEST || state.getValue(FACING).equals(Direction.EAST))
        {
            return Shapes.or(
                    Block.box(2.0F, 0.0F, 2.0F, 14.0F, 1.0F, 14.0F),
                    Block.box(4.0F, 1.0F, 4.0F, 12.0F, 3.0F, 12.0F),
                    Block.box(6.0F, 3.0F, 6.0F, 10.0F, 5.0F, 10.0F),
                    Block.box(7.0F, 5.0F, 7.0F, 9.0F, 8.0F, 9.0F),
                    Block.box(6.0F, 6.0F, 1.0F, 10.0F, 14.0F, 15.0F)
            );
        }
        else
        {
            return Shapes.or(
                    Block.box(2.0F, 0.0F, 2.0F, 14.0F, 1.0F, 14.0F),
                    Block.box(4.0F, 1.0F, 4.0F, 12.0F, 3.0F, 12.0F),
                    Block.box(6.0F, 3.0F, 6.0F, 10.0F, 5.0F, 10.0F),
                    Block.box(7.0F, 5.0F, 7.0F, 9.0F, 8.0F, 9.0F),
                    Block.box(1.0F, 6.0F, 6.0F, 15.0F, 14.0F, 10.0F)
            );
        }
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
        bs = bs.setValue(FACING, context.getHorizontalDirection().getOpposite());
        bs = bs.setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
        return bs;
    }
}
