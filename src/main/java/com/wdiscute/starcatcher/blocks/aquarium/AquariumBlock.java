package com.wdiscute.starcatcher.blocks.aquarium;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.blocks.TickableBlockEntity;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.items.StarcaughtBucket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

public class AquariumBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");

    public AquariumBlock()
    {
        super(Properties.of()
                .pushReaction(PushReaction.IGNORE)
                .sound(SoundType.GLASS)
                .strength(2.0F)
                .noOcclusion());
    }

    @Override
    protected float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_)
    {
        return 1.0F;
    }

    protected VoxelShape getVisualShape(BlockState p_309057_, BlockGetter p_308936_, BlockPos p_308956_, CollisionContext p_309006_)
    {
        return Shapes.empty();
    }

    @Override //1.21 only
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if(level.getBlockEntity(pos) instanceof AquariumBlockEntity abe)
            abe.preRemoveSideEffects(pos, state);

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state)
    {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid)
    {
        return false;
    }

    @Override
    public boolean hasDynamicShape()
    {
        return true;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid)
    {
        return level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }

    private static final VoxelShape BOTTOM_SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    private static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 0, 16, 16, 2);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 14, 16, 16, 16);

    private static final VoxelShape WEST_SHAPE = Block.box(0, 0, 0, 2, 16, 16);
    private static final VoxelShape EAST_SHAPE = Block.box(14, 0, 0, 16, 16, 16);

    private static final VoxelShape[] SHAPES = new VoxelShape[32];

    static
    {
        for (int i = 0; i < 32; i++)
        {
            VoxelShape shape = Shapes.empty();

            if ((i & 1) == 0)
                shape = Shapes.or(shape, BOTTOM_SHAPE);

            if ((i & 2) == 0)
                shape = Shapes.or(shape, NORTH_SHAPE);

            if ((i & 4) == 0)
                shape = Shapes.or(shape, SOUTH_SHAPE);

            if ((i & 8) == 0)
                shape = Shapes.or(shape, WEST_SHAPE);

            if ((i & 16) == 0)
                shape = Shapes.or(shape, EAST_SHAPE);

            SHAPES[i] = shape.optimize();
        }
    }

    private static int getShapeIndex(BlockState state)
    {
        int index = 0;

        if (state.getValue(BOTTOM)) index |= 1;
        if (state.getValue(NORTH)) index |= 2;
        if (state.getValue(SOUTH)) index |= 4;
        if (state.getValue(WEST)) index |= 8;
        if (state.getValue(EAST)) index |= 16;

        return index;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (context instanceof EntityCollisionContext e)
        {
            if (e.getEntity() != null)
            {
                return SHAPES[getShapeIndex(state)];
            }
        }

        return Shapes.block();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        VoxelShape shape = SHAPES[getShapeIndex(state)];
        if (context instanceof EntityCollisionContext ecc)
        {
            if (ecc.getEntity() instanceof LivingEntity le)
                if (le.getMainHandItem().is(Tags.Items.BUCKETS_EMPTY) || le.getMainHandItem().is(SCBlocks.AQUARIUM.asItem()))
                    return Shapes.block();
                else
                    return shape;
        }
        if (shape.isEmpty())
            return Shapes.block();
        return shape;
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState)
    {
        return false;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state)
    {
        return false;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos)
    {
        return true;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        //remove
        if (hand.equals(InteractionHand.MAIN_HAND) && stack.isEmpty() && player.isCrouching())
        {
            if (level.getBlockEntity(pos) instanceof AquariumBlockEntity abe)
            {
                if (abe.decoration != null)
                {
                    abe.decoration.type().onDestroyed(abe.decoration, abe, player);
                    abe.setChanged();
                    return ItemInteractionResult.SUCCESS;
                }

                if (abe.ground != null)
                {
                    abe.ground = null;
                    abe.setChanged();
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        if (stack.getItem() instanceof BucketItem bucket && !(stack.getItem() instanceof StarcaughtBucket) && !stack.is(Tags.Items.BUCKETS_EMPTY))
        {
            bucket.checkExtraContent(player, level, stack, pos);
            player.setItemInHand(hand, BucketItem.getEmptySuccessItem(stack, player));
            return ItemInteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof AquariumBlockEntity abe)
        {
            Decoration dec = SCDataMaps.getOrDefault(stack, SCDataMaps.AQUARIUM_DECORATION, null);
            //if can place
            if (dec != null && dec.place(abe, stack, player))
            {
                if(!level.isClientSide())
                    level.sendBlockUpdated(pos, state, state, 0);

                abe.setChanged();
                return ItemInteractionResult.SUCCESS;
            }

            ResourceLocation ground = SCDataMaps.getOrDefault(stack, SCDataMaps.AQUARIUM_GROUND, null);
            if (ground != null)
            {
                if (ground.equals(Starcatcher.MISSINGNO))
                    if (stack.getItem() instanceof BlockItem bi)
                        abe.ground = BuiltInRegistries.BLOCK.getKey(bi.getBlock()).withPrefix("textures/block/").withSuffix(".png");
                    else
                        player.displayClientMessage(Component.literal("something went wrong. report to wd on discord"), true);
                else
                    abe.ground = ground;

                if(!level.isClientSide())
                    level.sendBlockUpdated(pos, state, state, 0);

                abe.setChanged();
                return ItemInteractionResult.SUCCESS;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos)
    {
        if (state.getValue(WATERLOGGED))
        {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState bs = defaultBlockState();

        bs = bs.setValue(WATERLOGGED, true);

        bs = bs.setValue(BOTTOM, level.getBlockState(pos.below()).is(SCBlocks.AQUARIUM));
        bs = bs.setValue(TOP, level.getBlockState(pos.above()).is(SCBlocks.AQUARIUM));
        bs = bs.setValue(EAST, level.getBlockState(pos.east()).is(SCBlocks.AQUARIUM));
        bs = bs.setValue(WEST, level.getBlockState(pos.west()).is(SCBlocks.AQUARIUM));
        bs = bs.setValue(NORTH, level.getBlockState(pos.north()).is(SCBlocks.AQUARIUM));
        bs = bs.setValue(SOUTH, level.getBlockState(pos.south()).is(SCBlocks.AQUARIUM));

        return bs;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, NORTH, SOUTH, EAST, WEST, BOTTOM, TOP);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
    {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        BlockState stateBellow = level.getBlockState(pos.below());

        //update connections
        state = state.setValue(BOTTOM, level.getBlockState(pos.below()).is(SCBlocks.AQUARIUM));
        state = state.setValue(TOP, level.getBlockState(pos.above()).is(SCBlocks.AQUARIUM));
        state = state.setValue(EAST, level.getBlockState(pos.east()).is(SCBlocks.AQUARIUM));
        state = state.setValue(WEST, level.getBlockState(pos.west()).is(SCBlocks.AQUARIUM));
        state = state.setValue(NORTH, level.getBlockState(pos.north()).is(SCBlocks.AQUARIUM));
        state = state.setValue(SOUTH, level.getBlockState(pos.south()).is(SCBlocks.AQUARIUM));

        level.setBlockAndUpdate(pos, state);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid)
    {
        return false;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec()
    {
        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return SCBlockEntities.AQUARIUM.get().create(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return TickableBlockEntity.getTicketHelper(level);
    }
}
