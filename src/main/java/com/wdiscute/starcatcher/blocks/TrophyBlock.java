package com.wdiscute.starcatcher.blocks;

import com.mojang.logging.LogUtils;
import com.wdiscute.starcatcher.networkandcodecs.DataComponents;
import com.wdiscute.starcatcher.networkandcodecs.TrophyProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

import javax.annotation.Nullable;

public class TrophyBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public TrophyBlock()
    {
        super(Properties.of()
                .noOcclusion()
                .lightLevel((state) -> 5)
                .sound(SoundType.AMETHYST)
        );
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
    {
        return !(Boolean) state.getValue(WATERLOGGED);
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState)
    {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (level.getBlockEntity(pos) instanceof TrophyBlockEntity tbe)
        {
            tbe.setTrophyProperties(DataComponents.getTrophyProperties(stack));
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS))
        {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof TrophyBlockEntity tbe)
            {
                ItemStack itemstack = new ItemStack(this);

                CompoundTag tag = itemstack.getOrCreateTag();

                if (!tbe.trophyProperties.equals(TrophyProperties.DEFAULT))
                {
                    TrophyProperties.CODEC.encode(tbe.trophyProperties, NbtOps.INSTANCE, new CompoundTag())
                            .resultOrPartial(LOGGER::warn).ifPresent(tag1 -> tag.put("trophy_properties", tag1));

                    if (!tbe.trophyProperties.customName().equals(TrophyProperties.DEFAULT.customName()))
                        itemstack.setHoverName(Component.literal(tbe.trophyProperties.customName()));
                }

                ItemEntity itementity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState bs = defaultBlockState();
        bs = bs.setValue(FACING, context.getHorizontalDirection().getOpposite());
        bs = bs.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
        return bs;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return ModBlockEntities.TROPHY.get().create(blockPos, blockState);
    }
}
