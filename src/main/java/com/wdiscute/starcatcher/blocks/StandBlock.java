package com.wdiscute.starcatcher.blocks;

import com.mojang.authlib.GameProfile;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.network.TournamentDataToClientPayload;
import com.wdiscute.starcatcher.tournament.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.fml.ISystemReportExtender;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StandBlock extends Block implements EntityBlock
{

    public static final EnumProperty<StandPart> PART = EnumProperty.create("stand_part", StandPart.class);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public StandBlock()
    {
        super(Properties.of().noOcclusion());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof StandBlockEntity sbe)
        {
            if (sbe.tournament == null)
            {
                sbe.tournament = TournamentHandler.getTournament(sbe.uuid);
                System.out.println(sbe.uuid);
            }

            player.openMenu(new SimpleMenuProvider(sbe, Component.empty()), pos);

            List<GameProfile> list = new ArrayList<>();

            if(sbe.tournament.owner == null)
            {
                sbe.tournament.owner = player.getUUID();
                sbe.tournament.playerScores.put(player.getUUID(), TournamentPlayerScore.empty());
            }

            for (var entry : sbe.tournament.getPlayerScores().entrySet())
            {
                GameProfileCache profileCache = level.getServer().getProfileCache();
                if (profileCache != null)
                {
                    Optional<GameProfile> gameProfile = profileCache.get(entry.getKey());
                    gameProfile.ifPresent(list::add);
                }
            }

            PacketDistributor.sendToPlayer(
                    ((ServerPlayer) player),
                    new TournamentDataToClientPayload(list, sbe.tournament));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
    {
        super.onRemove(state, level, pos, newState, movedByPiston);

        Direction direction = state.getValue(FACING);

        if (state.getValue(PART) == StandPart.BOTTOM_LEFT)
        {
            level.destroyBlock(pos.above(), false);
            level.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
            level.destroyBlock(pos.relative(direction.getCounterClockWise()).above(), false);
        }

        if (state.getValue(PART) == StandPart.BOTTOM_RIGHT)
        {
            level.destroyBlock(pos.above(), false);
            level.destroyBlock(pos.relative(direction.getClockWise()), false);
            level.destroyBlock(pos.relative(direction.getClockWise()).above(), false);
        }

        if (state.getValue(PART) == StandPart.TOP_LEFT)
        {
            level.destroyBlock(pos.below(), false);
            level.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
            level.destroyBlock(pos.relative(direction.getCounterClockWise()).below(), false);
        }

        if (state.getValue(PART) == StandPart.TOP_RIGHT)
        {
            level.destroyBlock(pos.above(), false);
            level.destroyBlock(pos.relative(direction.getClockWise()), false);
            level.destroyBlock(pos.relative(direction.getClockWise()).above(), false);
        }


    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();

        Direction direction = context.getHorizontalDirection().getOpposite();

        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context)
                && blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.relative(direction.getCounterClockWise())).canBeReplaced(context)
                && blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above().relative(direction.getCounterClockWise())).canBeReplaced(context)
        )
        {
            return this.defaultBlockState()
                    .setValue(FACING, direction)
                    .setValue(PART, StandPart.BOTTOM_LEFT);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (level.isClientSide) return;
        UUID uuid = UUID.randomUUID();
        if (level.getBlockState(pos).is(ModBlocks.STAND))
        {
            Direction direction = level.getBlockState(pos).getValue(FACING);

            level.setBlock(
                    pos.above(), state
                            .setValue(PART, StandPart.TOP_LEFT)
                            .setValue(FACING, direction), 3);

            level.setBlock(
                    pos.relative(direction.getCounterClockWise()), state
                            .setValue(PART, StandPart.BOTTOM_RIGHT)
                            .setValue(FACING, direction), 3);

            level.setBlock(
                    pos.above().relative(direction.getCounterClockWise()), state
                            .setValue(PART, StandPart.TOP_RIGHT)
                            .setValue(FACING, direction), 3);

            //assign uuids to stand blocks entities played
            if (level.getBlockEntity(pos) instanceof StandBlockEntity sbe) sbe.uuid = uuid;
            if (level.getBlockEntity(pos.above()) instanceof StandBlockEntity sbe) sbe.uuid = uuid;
            if (level.getBlockEntity(pos.relative(direction.getCounterClockWise())) instanceof StandBlockEntity sbe) sbe.uuid = uuid;
            if (level.getBlockEntity(pos.above().relative(direction.getCounterClockWise())) instanceof StandBlockEntity sbe) sbe.uuid = uuid;
        }
    }

    private static final VoxelShape BOTTOM_LEFT = Shapes.or(Block.box(0.0F, 0.0F, 0.0F, 8.0F, 16.0F, 16.0F), Block.box(8.0F, 14.0F, 0.0F, 16.0F, 16.0F, 16.0F), Block.box(8.0F, 0.0F, 2.0F, 16.0F, 6.0F, 10.0F));
    private static final VoxelShape BOTTOM_LEFT_EAST = Shapes.or(Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 8.0F), Block.box(0.0F, 14.0F, 8.0F, 16.0F, 16.0F, 16.0F), Block.box(6.0F, 0.0F, 8.0F, 14.0F, 6.0F, 16.0F));
    private static final VoxelShape BOTTOM_LEFT_SOUTH = Shapes.or(Block.box(8.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F), Block.box(0.0F, 14.0F, 0.0F, 8.0F, 16.0F, 16.0F), Block.box(0.0F, 0.0F, 6.0F, 8.0F, 6.0F, 14.0F));
    private static final VoxelShape BOTTOM_LEFT_WEST = Shapes.or(Block.box(0.0F, 0.0F, 8.0F, 16.0F, 16.0F, 16.0F), Block.box(0.0F, 14.0F, 0.0F, 16.0F, 16.0F, 8.0F), Block.box(2.0F, 0.0F, 0.0F, 10.0F, 6.0F, 8.0F));
    private static final VoxelShape BOTTOM_RIGHT = Shapes.or(Block.box(8, 0, 0, 16, 16, 16), Block.box(0, 14, 0, 16, 16, 16));
    private static final VoxelShape BOTTOM_RIGHT_EAST = Shapes.or(Block.box(0, 0, 8, 16, 16, 16), Block.box(0, 14, 0, 16, 16, 16));
    private static final VoxelShape BOTTOM_RIGHT_SOUTH = Shapes.or(Block.box(0, 0, 0, 8, 16, 16), Block.box(0, 14, 0, 16, 16, 16));
    private static final VoxelShape BOTTOM_RIGHT_WEST = Shapes.or(Block.box(0, 0, 0, 16, 16, 8), Block.box(0, 14, 0, 16, 16, 16));
    private static final VoxelShape TOP_LEFT = Shapes.or(Block.box(15, 0, 7, 16, 16, 8), Block.box(0, 10, 7, 16, 16, 8));
    private static final VoxelShape TOP_LEFT_EAST = Shapes.or(Block.box(8, 0, 15, 9, 16, 16), Block.box(8, 10, 0, 9, 16, 16));
    private static final VoxelShape TOP_LEFT_SOUTH = Shapes.or(Block.box(0, 0, 8, 1, 16, 9), Block.box(0, 10, 8, 16, 16, 9));
    private static final VoxelShape TOP_LEFT_WEST = Shapes.or(Block.box(7, 0, 0, 8, 16, 1), Block.box(7, 10, 0, 8, 16, 16));
    private static final VoxelShape TOP_RIGHT = Shapes.or(Block.box(0, 0, 7, 1, 16, 8), Block.box(0, 10, 7, 16, 16, 8));
    private static final VoxelShape TOP_RIGHT_EAST = Shapes.or(Block.box(8, 0, 0, 9, 16, 1), Block.box(8, 10, 0, 9, 16, 16));
    private static final VoxelShape TOP_RIGHT_SOUTH = Shapes.or(Block.box(15, 0, 8, 16, 16, 9), Block.box(0, 10, 8, 16, 16, 9));
    private static final VoxelShape TOP_RIGHT_WEST = Shapes.or(Block.box(7, 0, 15, 8, 16, 16), Block.box(7, 10, 0, 8, 16, 16));

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        StandPart part = state.getValue(PART);
        Direction facing = state.getValue(FACING);

        if (part == StandPart.BOTTOM_LEFT)
        {
            if (facing.equals(Direction.NORTH)) return BOTTOM_LEFT;
            if (facing.equals(Direction.EAST)) return BOTTOM_LEFT_EAST;
            if (facing.equals(Direction.SOUTH)) return BOTTOM_LEFT_SOUTH;
            if (facing.equals(Direction.WEST)) return BOTTOM_LEFT_WEST;
        }

        if (part == StandPart.BOTTOM_RIGHT)
        {
            if (facing.equals(Direction.NORTH)) return BOTTOM_RIGHT;
            if (facing.equals(Direction.EAST)) return BOTTOM_RIGHT_EAST;
            if (facing.equals(Direction.SOUTH)) return BOTTOM_RIGHT_SOUTH;
            if (facing.equals(Direction.WEST)) return BOTTOM_RIGHT_WEST;
        }

        if (part == StandPart.TOP_LEFT)
        {
            if (facing.equals(Direction.NORTH)) return TOP_LEFT;
            if (facing.equals(Direction.EAST)) return TOP_LEFT_EAST;
            if (facing.equals(Direction.SOUTH)) return TOP_LEFT_SOUTH;
            if (facing.equals(Direction.WEST)) return TOP_LEFT_WEST;
        }

        if (part == StandPart.TOP_RIGHT)
        {
            if (facing.equals(Direction.NORTH)) return TOP_RIGHT;
            if (facing.equals(Direction.EAST)) return TOP_RIGHT_EAST;
            if (facing.equals(Direction.SOUTH)) return TOP_RIGHT_SOUTH;
            if (facing.equals(Direction.WEST)) return TOP_RIGHT_WEST;
        }

        return super.getShape(state, level, pos, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(PART);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return ModBlockEntities.STAND.get().create(blockPos, blockState);
    }

    public enum StandPart implements StringRepresentable
    {
        BOTTOM_LEFT("bottom_left"), BOTTOM_RIGHT("bottom_right"), TOP_LEFT("top_left"), TOP_RIGHT("top_right");

        private final String name;

        StandPart(String name)
        {
            this.name = name;
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
    }
}

