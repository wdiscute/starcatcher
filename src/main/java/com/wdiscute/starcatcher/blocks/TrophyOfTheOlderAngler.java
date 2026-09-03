package com.wdiscute.starcatcher.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.BaseMultiblock;
import net.nikdo53.tinymultiblocklib.block.IPreviewableMultiblock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrophyOfTheOlderAngler extends AbstractMultiBlock implements IPreviewableMultiblock
{
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public TrophyOfTheOlderAngler(Properties p)
    {
        super(p
                .destroyTime(2)
                .sound(SoundType.AMETHYST)
                .lightLevel((state) -> 15)
                .noOcclusion()
        );
    }

    public static final VoxelShape BOTTOM = Shapes.or(
            //base
            Block.box(0.0F, 0.0F, 0.0F, 16.0F, 2.0F, 16.0F),
            //pillars
            Block.box(2.0F, 2.0F, 2.0F, 5.0F, 16.0F, 5.0F),
            Block.box(11.0F, 2.0F, 2.0F, 14.0F, 16.0F, 5.0F),
            Block.box(11.0F, 2.0F, 11.0F, 14.0F, 16.0F, 14.0F),
            Block.box(2.0F, 2.0F, 11.0F, 5.0F, 16.0F, 14.0F),
            //centerpiece
            Block.box(6.0F, 2.0F, 6.0F, 10.0F, 8.0F, 10.0F),

            //base top
            Block.box(0.0F, 16.0F, 0.0F, 16.0F, 18.0F, 16.0F),
            //center top piece
            Block.box(5.0F, 18.0F, 5.0F, 11, 27.0F, 11.0F)
    );

    public static final VoxelShape TOP = Shapes.or(
            //base
            Block.box(0.0F, -16 + 0.0F, 0.0F, 16.0F, -16 + 2.0F, 16.0F),
            //pillars
            Block.box(2.0F, -16 + 2.0F, 2.0F, 5.0F, -16 + 16.0F, 5.0F),
            Block.box(11.0F, -16 + 2.0F, 2.0F, 14.0F, -16 + 16.0F, 5.0F),
            Block.box(11.0F, -16 + 2.0F, 11.0F, 14.0F, -16 + 16.0F, 14.0F),
            Block.box(2.0F, -16 + 2.0F, 11.0F, 5.0F, -16 + 16.0F, 14.0F),
            //centerpiece
            Block.box(6.0F, -16 + 2.0F, 6.0F, 10.0F, -16 + 8.0F, 10.0F),

            //base top
            Block.box(0.0F, -16 + 16.0F, 0.0F, 16.0F, -16 + 18.0F, 16.0F),
            //center top piece
            Block.box(5.0F, -16 + 18.0F, 5.0F, 11, -16 + 27.0F, 11.0F)
    );

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (state.getOptionalValue(BaseMultiblock.CENTER).orElse(false))
            return BOTTOM;
        else
            return TOP;
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
    public @Nullable EnumProperty<Direction> getDirectionProperty()
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
