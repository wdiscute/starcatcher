package com.wdiscute.starcatcher.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IPreviewableMultiblock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrophyOfTheOlderAngler extends AbstractMultiBlock implements IPreviewableMultiblock
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public TrophyOfTheOlderAngler()
    {
        super(Properties.of()
                .destroyTime(2)
                .lightLevel((state) -> 5)
                .noOcclusion()
        );
    }

    @Override
    public List<BlockPos> makeFullBlockShape(Level level, BlockPos center, BlockState blockState, @Nullable BlockEntity blockEntity, @Nullable Direction direction)
    {
        assert direction != null;
        return List.of(center, center.above());
    }

    @Override
    public RenderShape getMultiblockRenderShape(BlockState state, boolean c)
    {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable DirectionProperty getDirectionProperty()
    {
        return FACING;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        //open reset screen
        //if (level.isClientSide) screen();

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
