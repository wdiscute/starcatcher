package com.wdiscute.starcatcher.blocks.stand;

import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.tournament.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IPreviewableMultiblock;
import net.nikdo53.tinymultiblocklib.components.IBlockPosOffsetEnum;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class StandBlock extends AbstractMultiBlock implements IPreviewableMultiblock
{

    public static final EnumProperty<StandPart> PART = EnumProperty.create("stand_part", StandPart.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public StandBlock()
    {
        super(Properties.of().noOcclusion().destroyTime(2));
        registerDefaultState(defaultBlockState().setValue(PART, StandPart.BOTTOM_LEFT));
    }

    @Override
    public List<BlockPos> makeFullBlockShape(Level level, BlockPos center, BlockState blockState, @Nullable BlockEntity blockEntity, @Nullable Direction direction) {
        assert direction != null;
        return List.of(center, center.above(), center.relative(direction.getCounterClockWise()), center.relative(direction.getCounterClockWise()).above());
    }


    @Override
    public RenderShape getMultiblockRenderShape(BlockState state, boolean isCenter) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable DirectionProperty getDirectionProperty()
    {
        return FACING;
    }

    @Override
    public BlockState getStateForEachBlock(BlockState state, BlockPos pos, BlockPos centerOffset, Level level, @Nullable Direction direction)
    {
        state = state.setValue(PART, IBlockPosOffsetEnum.fromOffset(StandPart.class, centerOffset, direction, StandPart.BOTTOM_LEFT));

        return state;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return getStateForPlacementHelper(context, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState getDefaultStateForPreviews(Direction direction)
    {
        return IPreviewableMultiblock.super.getDefaultStateForPreviews(direction.getOpposite());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockPos center = IMultiBlock.getCenter(level, pos);
        if (level.getBlockEntity(center) instanceof StandBlockEntity sbe)
        {
            //initial tournament setup, makes new one if empty
            sbe.makeOrGetTournament(player);

            if(sbe.tournament.status.equals(Tournament.Status.FINISHED))
            {
                sbe.tournament = null;
                sbe.setUuid(UUID.randomUUID());
                sbe.makeOrGetTournament(player);
            }

            if (sbe.tournament.owner == null)
            {
                sbe.tournament.owner = player.getUUID();
            }
            sbe.sync();

            player.openMenu(new SimpleMenuProvider(sbe, Component.empty()), center);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
    {
        //before super since super removed BE
        BlockPos center = IMultiBlock.getCenter(level, pos);
        if (level.getBlockEntity(center) instanceof StandBlockEntity sbe && !level.isClientSide && sbe.tournament != null)
        {
            TournamentHandler.cancelTournament(level, sbe.tournament);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private static final VoxelShape SHAPE_NORTH = makeShapeNorth();
    private static final VoxelShape SHAPE_EAST = makeShapeEast();
    private static final VoxelShape SHAPE_SOUTH = makeShapeSouth();
    private static final VoxelShape SHAPE_WEST = makeShapeWest();

    public static VoxelShape makeShapeNorth()
    {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0, 1.5, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.875, 0, 2, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 1.5, 0.4375, 2, 2, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 1, 0.4375, 0.0625, 2, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(1.9375, 1, 0.4375, 2, 2, 0.5), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeEast()
    {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.5, 1, 1, 1.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.875, 0, 1, 1, 2), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 1.453125, 0, 0.5625, 1.953125, 2), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0.953125, 0, 0.5625, 1.953125, 0.0625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0.953125, 1.9375, 0.5625, 1.953125, 2), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeSouth()
    {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.5, 0, 0, 0.5, 1, 1.0), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-1, 0.875, 0, 1, 1, 1.0), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-1, 1.453125, 0.5, 1, 1.953125, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.9375, 0.953125, 0.5, 1, 1.953125, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-1, 0.953125, 0.5, -0.9375, 1.953125, 0.5625), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeShapeWest()
    {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, -0.5, 1.0, 1, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.875, -1, 1.0, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 1.453125, -1, 0.5625, 1.953125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0.953125, -1, 0.5625, 1.953125, -0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0.953125, 0.9375, 0.5625, 1.953125, 1), BooleanOp.OR);

        return shape;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        VoxelShape shape = switch (getDirection(state).getOpposite())
        {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> null;
        };

        return voxelShapeHelper(state, level, pos, shape);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        //the facing gets added automatically by the lib
        builder.add(PART);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return SCBlockEntities.STAND.get().create(blockPos, blockState);
    }

    @Override
    public boolean hasCustomBE() {
        return true;
    }

    public enum StandPart implements StringRepresentable, IBlockPosOffsetEnum
    {
        BOTTOM_LEFT("bottom_left", pos -> pos),
        BOTTOM_RIGHT("bottom_right", BlockPos::west),
        TOP_LEFT("top_left", BlockPos::above),
        TOP_RIGHT("top_right", pos -> pos.above().west());

        private final String name;
        public final Function<BlockPos, BlockPos> offset;

        StandPart(String name, Function<BlockPos, BlockPos> offset)
        {
            this.name = name;
            this.offset = offset;
        }

        public String toString()
        {
            return this.name;
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }

        @Override
        public Function<BlockPos, BlockPos> getOffsetFunction() {
            return offset;
        }
    }
}

