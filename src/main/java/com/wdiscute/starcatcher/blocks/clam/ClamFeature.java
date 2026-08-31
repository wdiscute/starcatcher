package com.wdiscute.starcatcher.blocks.clam;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.registry.SCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ClamFeature extends Feature<NoneFeatureConfiguration>
{
    public ClamFeature(Codec<NoneFeatureConfiguration> p_66219_)
    {
        super(p_66219_);
    }

    private boolean hasWater(WorldGenLevel level, BlockPos origin)
    {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (level.getBlockState(origin.offset(i, 0, j)).is(Blocks.WATER))
                    return true;

        return false;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        WorldGenLevel level = context.level();
        BlockPos originBP = context.origin();
        RandomSource r = context.random();

        int originHeight = level.getHeight(Heightmap.Types.WORLD_SURFACE, originBP.getX(), originBP.getZ());
        originBP = new BlockPos(originBP.getX(), originHeight, originBP.getZ());

        if (level.getBlockState(originBP).is(Blocks.AIR) && level.getBlockState(originBP.below()).is(BlockTags.SAND))
        {
            if (!hasWater(level, originBP.below()) && !hasWater(level, originBP.below().below()))
                return false;

            BlockState bs = r.nextBoolean() ? SCBlocks.CLAM.get().defaultBlockState() : SCBlocks.CONCH.get().defaultBlockState();
            bs = bs.setValue(BlockStateProperties.WATERLOGGED, false);
            bs = switch (r.nextInt(3))
            {
                case 0 -> bs.setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH);
                case 1 -> bs.setValue(HorizontalDirectionalBlock.FACING, Direction.EAST);
                case 2 -> bs.setValue(HorizontalDirectionalBlock.FACING, Direction.WEST);
                default -> bs.setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH);
            };

            if (bs.is(SCBlocks.CLAM))
            {
                if (r.nextFloat() < 0.05)
                    bs = bs.setValue(ClamBlock.HAS_PEARL, true);
                else
                    bs = bs.setValue(ClamBlock.HAS_PEARL, false);
            }

            level.setBlock(originBP, bs, 2);
        }

        return true;
    }
}