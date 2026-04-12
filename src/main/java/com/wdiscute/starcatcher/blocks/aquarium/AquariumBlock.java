package com.wdiscute.starcatcher.blocks.aquarium;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockEntity;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.blocks.SCBlockEntities;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.blocks.TickableBlockEntity;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.nikdo53.neobackports.utils.ItemInteractionResult;
import org.jetbrains.annotations.NotNull;
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
    public static final EnumProperty<Decoration> DECORATION = EnumProperty.create("decoration", Decoration.class);
    public static final EnumProperty<Ground> GROUND = EnumProperty.create("ground", Ground.class);

    public AquariumBlock()
    {
        super(Properties.of()
                .pushReaction(PushReaction.IGNORE)
                .sound(SoundType.GLASS)
                .strength(2.0F)
                .noOcclusion());
    }

    @Override
    public float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_)
    {
        return 1.0F;
    }

    public VoxelShape getVisualShape(BlockState p_309057_, BlockGetter p_308936_, BlockPos p_308956_, CollisionContext p_309006_)
    {
        return Shapes.empty();
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!state.is(newState.getBlock()))
        {
            this.popItem(level, pos);
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    private void popItem(Level level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof AquariumBlockEntity abe && !level.isClientSide)
        {
            if(abe.getFish().is(SCTags.BUCKETABLE_FISHES))
            {
                ItemStack itemstack = abe.getFish().copy();
                FishEntity entity = SCEntities.FISH.get().create(level);

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
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid)
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

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (context instanceof EntityCollisionContext e)
        {
            if (e.getEntity() != null)
            {
                VoxelShape shape = Shapes.empty();

                if (!state.getValue(BOTTOM)) shape = Shapes.join(shape, Block.box(-2, -2, -2, 18, 2, 18), BooleanOp.OR);
                //if (!state.getValue(TOP)) shape = Shapes.join(shape, Block.box(-2, 14, -2, 16, 16, 16), BooleanOp.OR);

                if (!state.getValue(NORTH)) shape = Shapes.join(shape, Block.box(-2, -2, -2, 18, 18, 0), BooleanOp.OR);
                if (!state.getValue(WEST)) shape = Shapes.join(shape, Block.box(-2, -2, -2, 0, 18, 18), BooleanOp.OR);

                if (!state.getValue(EAST)) shape = Shapes.join(shape, Block.box(16, -2, -2, 18, 18, 18), BooleanOp.OR);
                if (!state.getValue(SOUTH)) shape = Shapes.join(shape, Block.box(-2, -2, 16, 18, 18, 18), BooleanOp.OR);

                return shape;
            }
        }

        return Shapes.block();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        VoxelShape shape = Shapes.empty();

        if (!state.getValue(BOTTOM)) shape = Shapes.join(shape, Block.box(-2, -2, -2, 18, 2, 18), BooleanOp.OR);
        //if (!state.getValue(TOP)) shape = Shapes.join(shape, Block.box(-2, 14, -2, 16, 16, 16), BooleanOp.OR);

        if (!state.getValue(NORTH)) shape = Shapes.join(shape, Block.box(-2, -2, -2, 18, 18, 4), BooleanOp.OR);
        if (!state.getValue(WEST)) shape = Shapes.join(shape, Block.box(-2, -2, -2, 4, 18, 18), BooleanOp.OR);

        if (!state.getValue(EAST)) shape = Shapes.join(shape, Block.box(12, -2, -2, 18, 18, 18), BooleanOp.OR);
        if (!state.getValue(SOUTH)) shape = Shapes.join(shape, Block.box(-2, -2, 12, 18, 18, 18), BooleanOp.OR);

        return shape;
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState)
    {
        return false;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state)
    {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos)
    {
        return true;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (stack.getItem() instanceof BucketItem bucket && !stack.is(SCItems.STARCAUGHT_BUCKET.get()) && !stack.is(Items.BUCKET))
        {
            bucket.checkExtraContent(player, level, stack, pos);
            player.setItemInHand(hand, BucketItem.getEmptySuccessItem(stack, player));
            return ItemInteractionResult.SUCCESS;
        }

        Interaction interaction = SCDataMaps.getOrDefault(stack, SCDataMaps.AQUARIUM_INTERACTION, Interaction.NOTHING);
        if (interaction.executePlace(level, pos, state, stack, player))
        {
            if (interaction.sound != null) player.playSound(interaction.sound);
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos)
    {
        if (state.getValue(WATERLOGGED))
        {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state)
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
        bs = bs.setValue(DECORATION, Decoration.NOTHING);
        bs = bs.setValue(GROUND, Ground.NOTHING);
        if (!level.getBlockState(pos.below()).is(SCBlocks.AQUARIUM)) bs = bs.setValue(GROUND, Ground.SAND);

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
        builder.add(WATERLOGGED, DECORATION, GROUND, NORTH, SOUTH, EAST, WEST, BOTTOM, TOP);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
    {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        Decoration decoration = state.getValue(DECORATION);
        Ground ground = state.getValue(GROUND);

        BlockState stateBellow = level.getBlockState(pos.below());
        Decoration decorationBelow = stateBellow.is(SCBlocks.AQUARIUM) ? stateBellow.getValue(DECORATION) : Decoration.NOTHING;
        Ground groundBelow = stateBellow.is(SCBlocks.AQUARIUM) ? stateBellow.getValue(GROUND) : Ground.NOTHING;

        BlockState stateAbove = level.getBlockState(pos.above());
        Decoration decorationAbove = stateAbove.is(SCBlocks.AQUARIUM) ? stateAbove.getValue(DECORATION) : Decoration.NOTHING;

        //update connections
        state = state.setValue(BOTTOM, level.getBlockState(pos.below()).is(SCBlocks.AQUARIUM));
        state = state.setValue(TOP, level.getBlockState(pos.above()).is(SCBlocks.AQUARIUM));
        state = state.setValue(EAST, level.getBlockState(pos.east()).is(SCBlocks.AQUARIUM));
        state = state.setValue(WEST, level.getBlockState(pos.west()).is(SCBlocks.AQUARIUM));
        state = state.setValue(NORTH, level.getBlockState(pos.north()).is(SCBlocks.AQUARIUM));
        state = state.setValue(SOUTH, level.getBlockState(pos.south()).is(SCBlocks.AQUARIUM));

        level.setBlockAndUpdate(pos, state);

        //update ground
        if (!ground.isEmpty())
            if (stateBellow.is(SCBlocks.AQUARIUM))
                level.setBlockAndUpdate(pos, state.setValue(GROUND, Ground.NOTHING));

        if (ground.isEmpty())
            if (!stateBellow.is(SCBlocks.AQUARIUM))
                level.setBlockAndUpdate(pos, state.setValue(GROUND, Ground.SAND));

        //update kelp top
        if (decoration == Decoration.KELP_TOP)
            if (decorationBelow != Decoration.KELP && ground.isEmpty())
                level.setBlockAndUpdate(pos, state.setValue(DECORATION, Decoration.NOTHING));

        //update kelp
        if (decoration == Decoration.KELP)
            if (decorationAbove != Decoration.KELP && decorationAbove != Decoration.KELP_TOP)
                level.setBlockAndUpdate(pos, state.setValue(DECORATION, Decoration.NOTHING));

        //update kelp
        if (decoration == Decoration.KELP)
            if (decorationBelow != Decoration.KELP && ground.isEmpty())
                level.setBlockAndUpdate(pos, state.setValue(DECORATION, Decoration.NOTHING));

        //update seagrass/castle/cave
        if (decoration.requiresGround() && ground.isEmpty())
            level.setBlockAndUpdate(pos, state.setValue(DECORATION, Decoration.NOTHING));

    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
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

    public enum Ground implements StringRepresentable
    {
        NOTHING("nothing"),
        SAND("sand"),
        RED_SAND("red_sand"),
        GRAVEL("gravel"),
        STONE("stone");

        final String name;

        Ground(String name)
        {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName()
        {
            return name;
        }

        public boolean isEmpty()
        {
            return this == NOTHING;
        }
    }

    public enum Decoration implements StringRepresentable
    {
        NOTHING("nothing", false, true),
        CLAM("clam", true, true),
        CONCH("conch", true, true),
        SEAGRASS("seagrass", true, true),
        KELP("kelp", false, true),
        KELP_TOP("kelp_top", false, true),
        CAVE("cave", true, false),
        CASTLE_SAND("castle_sand", true, false),
        CASTLE_RED_SAND("castle_red_sand", true, false);

        final String name;
        final boolean requiresGround;
        final boolean canFishSwimInside;

        Decoration(String name, boolean requiresGround, boolean canFishSwimInside)
        {
            this.name = name;
            this.requiresGround = requiresGround;
            this.canFishSwimInside = canFishSwimInside;
        }

        public boolean requiresGround()
        {
            return requiresGround;
        }

        @Override
        public @NotNull String getSerializedName()
        {
            return name;
        }

        public boolean isEmpty(Object... o)
        {
            return this == NOTHING;
        }
    }

    public enum Interaction implements StringRepresentable
    {
        NOTHING("nothing", null, U::alwaysFalse),

        PLACE_FISH("place_fish", SoundEvents.DOLPHIN_SPLASH, (l, bp, bs, is, p) ->
        {
            if (l.getBlockEntity(bp) instanceof AquariumBlockEntity abe)
            {
                if (!abe.getFish().isEmpty()) return false;
                if (SCDataComponents.has(is, SCDataComponents.BUCKETED_FISH))
                {
                    abe.setFish(SCDataComponents.get(is, SCDataComponents.BUCKETED_FISH).stack());
                    if (is.getItem() instanceof BucketItem)
                    {
                        ItemStack emptySuccessItem = BucketItem.getEmptySuccessItem(is, p);
                        if (p.getMainHandItem().equals(is))
                            p.setItemInHand(InteractionHand.MAIN_HAND, emptySuccessItem);
                        else
                            p.setItemInHand(InteractionHand.OFF_HAND, emptySuccessItem);
                    }
                }
                else
                    abe.setFish(is);
                return true;
            }

            return false;
        }),

        PLACE_FISH_CREATIVE("place_fish_creative", SoundEvents.DOLPHIN_SPLASH, (l, bp, bs, is, p) ->
        {
            if (!p.isCreative()) return false;
            if (l.getBlockEntity(bp) instanceof AquariumBlockEntity abe)
            {
                abe.setFish(is);
                return true;
            }

            return false;

        }),

        RETRIEVE_FISH("remove_fish", SoundEvents.DOLPHIN_SPLASH, (l, bp, bs, is, p) ->
        {
            if (l.getBlockEntity(bp) instanceof AquariumBlockEntity abe)
            {
                if (abe.getFish().isEmpty()) return false;
                is.shrink(1);

                ItemStack bucketToReturn = new ItemStack(SCItems.STARCAUGHT_BUCKET.get());

                SCDataComponents.set(bucketToReturn, SCDataComponents.BUCKETED_FISH, SingleStackContainer.from(abe.fish));
                p.addItem(bucketToReturn);

                abe.setFish(ItemStack.EMPTY);
                return true;
            }

            return false;
        }),

        PLACE_SAND("place_sand", SoundEvents.SAND_PLACE, (l, bp, bs, is, p) -> placeGround(l, bp, bs, Ground.SAND)),
        PLACE_RED_SAND("place_red_sand", SoundEvents.SAND_PLACE, (l, bp, bs, is, p) -> placeGround(l, bp, bs, Ground.RED_SAND)),
        PLACE_STONE("place_stone", SoundEvents.STONE_PLACE, (l, bp, bs, is, p) -> placeGround(l, bp, bs, Ground.STONE)),
        PLACE_GRAVEL("place_gravel", SoundEvents.GRAVEL_PLACE, (l, bp, bs, is, p) -> placeGround(l, bp, bs, Ground.GRAVEL)),

        PLACE_KELP("place_kelp", SoundEvents.WET_GRASS_PLACE, (l, bp, bs, is, p) ->
        {
            if (bs.getValue(DECORATION) == Decoration.KELP) return false;
            if (bs.getValue(DECORATION) == Decoration.KELP_TOP) return false;
            if (!bs.getValue(GROUND).isEmpty())
                return l.setBlockAndUpdate(bp, bs.setValue(DECORATION, Decoration.KELP_TOP));

            BlockState stateUnder = l.getBlockState(bp.below());
            if (stateUnder.is(SCBlocks.AQUARIUM))
                if (stateUnder.getValue(DECORATION) == Decoration.KELP_TOP)
                {
                    l.setBlockAndUpdate(bp.below(), stateUnder.setValue(DECORATION, Decoration.KELP));
                    return l.setBlockAndUpdate(bp, bs.setValue(DECORATION, Decoration.KELP_TOP));
                }

            return false;
        }),

        PLACE_SEAGRASS("place_seagrass", SoundEvents.WET_GRASS_PLACE, (l, bp, bs, is, p) ->
        {
            if (!bs.getValue(GROUND).isEmpty())
                return l.setBlockAndUpdate(bp, bs.setValue(DECORATION, Decoration.SEAGRASS));
            else return false;
        }),

        PLACE_CLAM("place_clam", SoundEvents.BONE_BLOCK_PLACE, (l, bp, bs, is, p) ->
        {
            if (!bs.getValue(GROUND).isEmpty())
                return l.setBlockAndUpdate(bp, bs.setValue(DECORATION, Decoration.CLAM));
            else return false;
        }),

        PLACE_CONCH("place_conch", SoundEvents.BONE_BLOCK_PLACE, (l, bp, bs, is, p) ->
        {
            if (!bs.getValue(GROUND).isEmpty())
                return l.setBlockAndUpdate(bp, bs.setValue(DECORATION, Decoration.CONCH));
            else return false;
        }),

        BUILD_CASTLE("build_castle", SoundEvents.SAND_HIT, (l, bp, bs, is, p) ->
        {
            if (!bs.getValue(GROUND).isEmpty())
            {
                Ground ground = bs.getValue(GROUND);
                if (ground == Ground.SAND && bs.getValue(DECORATION) != Decoration.CASTLE_SAND)
                    return l.setBlockAndUpdate(bp, bs.setValue(DECORATION, Decoration.CASTLE_SAND));

                if (ground == Ground.RED_SAND && bs.getValue(DECORATION) != Decoration.CASTLE_RED_SAND)
                    return l.setBlockAndUpdate(bp, bs.setValue(DECORATION, Decoration.CASTLE_RED_SAND));
            }
            return false;
        }),

        BUILD_CAVE("build_cave", SoundEvents.STONE_HIT, (l, bp, bs, is, p) ->
        {
            if (bs.getValue(GROUND) == Ground.STONE && bs.getValue(DECORATION) != Decoration.CAVE)
                return l.setBlockAndUpdate(bp, bs.setValue(DECORATION, Decoration.CAVE));

            return false;
        });

        final String name;
        final SoundEvent sound;
        final Consumer<Level, BlockPos, BlockState, ItemStack, Player, Boolean> place;

        public static final Codec<Interaction> CODEC = StringRepresentable.fromEnum(Interaction::values);

        Interaction(String name, SoundEvent sound, Consumer<Level, BlockPos, BlockState, ItemStack, Player, Boolean> place)
        {
            this.name = name;
            this.sound = sound;
            this.place = place;
        }

        static boolean placeGround(Level l, BlockPos pos, BlockState bs, Ground ground)
        {
            if (bs.getValue(GROUND) == ground) return false;
            return l.setBlockAndUpdate(pos, bs.setValue(GROUND, ground));
        }

        @Override
        public @NotNull String getSerializedName()
        {
            return name;
        }

        public boolean executePlace(Level level, BlockPos pos, BlockState state, ItemStack is, Player player)
        {
            return place.place(level, pos, state, is, player);
        }
    }

    public interface Consumer<P1, P2, P3, P4, P5, R>
    {
        R place(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5);
    }
}
