package com.wdiscute.starcatcher.blocks.plaque;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PlaqueBlock extends HorizontalDirectionalBlock implements EntityBlock
{
    public PlaqueBlock(Properties properties)
    {
        super(properties
                .destroyTime(1)
                .sound(SoundType.WOOD)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec()
    {
        return null;
    }

    private static final VoxelShape NORTH_SHAPE = Block.box(1, 3, 15, 15, 13, 16);
    private static final VoxelShape SOUTH_SHAPE = Block.box(1, 3, 0, 15, 13, 1);
    private static final VoxelShape EAST_SHAPE = Block.box(15, 3, 1, 16, 13, 15);
    private static final VoxelShape WEST_SHAPE = Block.box(0, 3, 1, 1, 13, 15);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return switch (state.getValue(FACING))
        {
            case NORTH -> NORTH_SHAPE;
            case EAST -> WEST_SHAPE;
            case WEST -> EAST_SHAPE;
            default -> SOUTH_SHAPE;
        };
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if (level.getBlockEntity(pos) instanceof PlaqueBlockEntity pbe)
        {
            if (!pbe.item.isEmpty())
            {
                ItemStack itemstack = pbe.getImmutableItem();
                ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + (double) 0.5F, (pos.getY() + 1), (double) pos.getZ() + (double) 0.5F, itemstack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
                pbe.clearContent();
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (level.getBlockEntity(pos) instanceof PlaqueBlockEntity pbe)
        {
            if (pbe.item.isEmpty())
            {
                if (stack.is(SCTags.BUCKETABLE_FISHES))
                {
                    if (level.isClientSide()) return InteractionResult.SUCCESS;

                    //only place item if stack in hand is not empty
                    if (!stack.isEmpty())
                    {
                        pbe.item = new MaybeStack(stack.copyWithCount(1));
                        stack.shrink(1);
                        pbe.sync();
                    }
                    return InteractionResult.SUCCESS;
                }

            }
            else
            {
                if (level.isClientSide()) return InteractionResult.SUCCESS;

                player.addItem(pbe.getImmutableItem().copy());
                pbe.clearContent();
                pbe.sync();
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState bs = defaultBlockState();
        Direction clickedFace = context.getClickedFace();

        if (clickedFace.getAxis().isHorizontal())
            bs = bs.setValue(FACING, clickedFace);
        else
            bs = bs.setValue(FACING, context.getHorizontalDirection().getOpposite());

        bs = bs.setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
        return bs;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return SCBlockEntities.FISH_PLAQUE.get().create(pos, state);
    }
}
