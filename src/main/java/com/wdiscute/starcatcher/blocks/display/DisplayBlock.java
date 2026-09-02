package com.wdiscute.starcatcher.blocks.display;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.data.SignedGuide;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.*;

public class DisplayBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");
    private static final VoxelShape SHAPE_BASE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    private static final VoxelShape SHAPE_POST = Block.box(4.0, 2.0, 4.0, 12.0, 14.0, 12.0);
    private static final VoxelShape SHAPE_TOP_PLATE = Block.box(0.0, 10.0, 0.0, 16.0, 14.0, 16.0);
    private static final VoxelShape SHAPE = Shapes.or(SHAPE_BASE, SHAPE_POST, SHAPE_TOP_PLATE);

    @Override
    public MapCodec<DisplayBlock> codec()
    {
        return null;
    }

    public DisplayBlock()
    {
        super(BlockBehaviour.Properties.of().destroyTime(2));
        this.registerDefaultState(
                this.stateDefinition.any().setValue(POWERED, false).setValue(HAS_ITEM, false)
        );
    }

    @Override
    protected FluidState getFluidState(BlockState state)
    {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state)
    {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED, HAS_ITEM, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return SCBlockEntities.DISPLAY.get().create(pos, state);
    }

    @Override
    protected boolean isSignalSource(BlockState state)
    {
        return true;
    }

    @Override
    protected int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
    {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
    {
        return side == Direction.UP && blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction)
    {
        if (state.getValueOrElse(HAS_ITEM, false))
        {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof DisplayBlockEntity dbe)
                return dbe.getRedstoneSignal();
        }

        return 0;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston)
    {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
        if (level.getBlockEntity(pos) instanceof DisplayBlockEntity dbe)
            dbe.setChanged();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        //if has book, open screen
        if (state.getValue(HAS_ITEM) && !player.isCrouching())
        {
            if (level.getBlockEntity(pos) instanceof DisplayBlockEntity dbe)
            {
                ItemStack guide = dbe.getImmutableItem();
                if (guide.is(SCItems.GUIDE))
                {
                    if (level.isClientSide())
                    {
                        FishingGuideScreen.open(pos, SCDataComponents.get(dbe.getImmutableItem(), SCDataComponents.SIGNED_GUIDE));
                        return InteractionResult.SUCCESS;
                    }

                    //return if not signed
                    if (!SCDataComponents.has(guide, SCDataComponents.SIGNED_GUIDE))
                        return InteractionResult.SUCCESS;

                    SignedGuide signedGuide = SCDataComponents.get(guide, SCDataComponents.SIGNED_GUIDE);

                    //if owner opening guide
                    if (signedGuide.owner().equals(player.getUUID()))
                    {
                        Map<Identifier, FishCaughtCounter> map = SCDataAttachments.get(player, SCDataAttachments.FISHING_GUIDE).fishesCaught;

                        Map<Identifier, FishCaughtCounter> mapToSave = new HashMap<>();
                        map.forEach((rl, fcc) -> mapToSave.put(rl, fcc.removeNotification()));

                        if (player instanceof ServerPlayer sp)
                        {
                            FishingGuideScreen.StatsData statsData = new FishingGuideScreen.StatsData(
                                    sp.getStats().getValue(Stats.CUSTOM.get(SCStats.TICKS_SPENT_FISHING.get())),
                                    sp.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_TREASURES.get())),
                                    sp.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_FISH_MISSED.get())),
                                    sp.getStats().getValue(Stats.CUSTOM.get(SCStats.BAIT_USED.get()))
                            );

                            SCDataComponents.set(guide, SCDataComponents.SIGNED_GUIDE,
                                    new SignedGuide(
                                            player.getUUID(),
                                            mapToSave,
                                            signedGuide.signature(),
                                            Date.from(Instant.now()).getTime(),
                                            statsData,
                                            signedGuide.visitors()
                                    ));

                            dbe.setItem(guide);
                        }
                    }
                    //visitor opening guide
                    else
                    {
                        HashSet<Utils.Duo<UUID, String>> visitors = new HashSet<>(signedGuide.visitors());
                        visitors.add(new Utils.Duo<>(player.getUUID(), player.getScoreboardName()));
                        SCDataComponents.set(guide, SCDataComponents.SIGNED_GUIDE, signedGuide.withVisitors(List.copyOf(visitors)));
                        dbe.setItem(guide);
                    }

                }
                else
                    dbe.fishRotating = !dbe.fishRotating;
            }
            return ItemInteractionResult.SUCCESS;
        }

        //remove item if crouching
        if (state.getValue(HAS_ITEM) && player.isCrouching() && level.getBlockEntity(pos) instanceof DisplayBlockEntity dbe)
        {
            if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
            player.addItem(dbe.getImmutableItem().copy());
            dbe.clearContent();
            dbe.sync();
            level.setBlockAndUpdate(pos, state.setValue(HAS_ITEM, false));
            return ItemInteractionResult.SUCCESS;
        }

        //place book
        if (stack.is(SCTags.PLACEABLE_IN_DISPLAY) && !state.getValue(HAS_ITEM) && level.getBlockEntity(pos) instanceof DisplayBlockEntity dbe)
        {
            if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
            dbe.setItem(stack.copyWithCount(1));
            stack.shrink(1);
            level.setBlock(pos, state.setValue(HAS_ITEM, true), 0);
            dbe.sync();
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }


    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!state.is(newState.getBlock()))
        {
            if (state.getValue(HAS_ITEM))
                this.popItem(level, pos);

            super.onRemove(state, level, pos, newState, isMoving);
            if (state.getValue(POWERED))
                level.updateNeighborsAt(pos.below(), this);
        }
    }

    private void popItem(Level level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof DisplayBlockEntity displayBlockEntity)
        {
            ItemStack itemstack = displayBlockEntity.getImmutableItem().copy();
            ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + (double) 0.5F, (pos.getY() + 1), (double) pos.getZ() + (double) 0.5F, itemstack);
            itementity.setDefaultPickUpDelay();
            level.addFreshEntity(itementity);
            displayBlockEntity.clearContent();
        }
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        return !state.getValue(HAS_ITEM) ? null : super.getMenuProvider(state, level, pos);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return level.isClientSide() ? createTickerHelper(blockEntityType, SCBlockEntities.DISPLAY.get(), DisplayBlockEntity::bookAnimationTick) : null;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType)
    {
        return false;
    }
}
