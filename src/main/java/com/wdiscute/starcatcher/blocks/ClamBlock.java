package com.wdiscute.starcatcher.blocks;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClamBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public static final BooleanProperty HAS_PEARL = BooleanProperty.create("has_pearl");

    public ClamBlock()
    {
        super(Properties.of()
                .destroyTime(0.2f)
                .noOcclusion()
                .noCollission()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.BONE_BLOCK)
                .randomTicks()
        );
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Block.box(3, 0, 3, 13, 3, 13);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if(!state.getValue(HAS_PEARL) && random.nextFloat() > 0.99f && !level.isClientSide && level.getBlockState(pos.below()).is(BlockTags.SAND))
        {
            level.setBlockAndUpdate(pos, state.setValue(HAS_PEARL, true));
        }
        super.randomTick(state, level, pos, random);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (state.getValue(HAS_PEARL))
        {
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);
            level.playSound(null, pos, SoundEvents.BONE_BLOCK_PLACE, SoundSource.BLOCKS, 0.6f, 0.6f);
            level.setBlockAndUpdate(pos, state.setValue(HAS_PEARL, false));
            Vec3 vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5F, 0.4, 0.5F).offsetRandom(level.random, 0.7F);
            ItemStack stack = level.random.nextFloat() > 0.01f ? SCItems.PEARL.value().getDefaultInstance() : SCItems.PEARL_SMITHING_TEMPLATE.value().getDefaultInstance();
            ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), stack);
            itementity.setDefaultPickUpDelay();
            level.addFreshEntity(itementity);
            return InteractionResult.SUCCESS;
        }

        level.playSound(null, pos, SoundEvents.BONE_BLOCK_BREAK, SoundSource.BLOCKS);
        level.destroyBlock(pos, true, player);

        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
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
        builder.add(HAS_PEARL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState bs = defaultBlockState();
        bs = bs.setValue(HAS_PEARL, false);
        bs = bs.setValue(FACING, context.getHorizontalDirection());
        bs = bs.setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
        return bs;
    }
}
