package com.wdiscute.starcatcher.blocks.clam;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

public class ClamFeature implements Feature
{
    public static final MapCodec<ClamFeature> CODEC = MapCodec.unit(ClamFeature::new);

    private boolean hasWater(WorldGenLevel level, BlockPos origin)
    {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (level.getBlockState(origin.offset(i, 0, j)).is(Blocks.WATER))
                    return true;

        return false;
    }

    @Override
    public MapCodec<? extends Feature> codec()
    {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource r, BlockPos originBP)
    {
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