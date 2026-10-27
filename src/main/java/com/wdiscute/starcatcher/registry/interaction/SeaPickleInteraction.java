package com.wdiscute.starcatcher.registry.interaction;

import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlockEntity;
import com.wdiscute.starcatcher.blocks.aquarium.Decoration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;

public class SeaPickleInteraction extends AbstractInteraction
{
    public boolean interact(Decoration decoration, AquariumBlockEntity abe, ItemStack is, Player player)
    {
        if (abe.ground != null)
        {
            //if there's already a sea pickle there
            if (abe.decoration != null && abe.decoration.bs().is(Blocks.SEA_PICKLE))
            {
                int pickles = abe.decoration.bs().getOptionalValue(SeaPickleBlock.PICKLES).orElse(1);
                if (pickles == 4)
                    return false;

                abe.decoration = abe.decoration
                        .withBS(abe.decoration.bs()
                                .setValue(SeaPickleBlock.PICKLES, pickles + 1));

                abe.getLevel().playLocalSound(abe.getBlockPos(),
                        Blocks.SEA_PICKLE.getSoundType(Blocks.SEA_PICKLE.defaultBlockState(), abe.getLevel(), abe.getBlockPos(), player).getPlaceSound(),
                        SoundSource.BLOCKS,
                        1.0F, 1.0F,
                        false);
                return true;
            }

            //if there's a decoration and it is not a sea pickle, return false
            if (abe.decoration != null)
                return false;

            RandomSource r = abe.getLevel().getRandom();

            //set offset
            Vec3 offset = decoration.offset();
            Vec3 randomOffset = decoration.randomOffset();
            float randomX = (float) (r.nextFloat() * randomOffset.x - randomOffset.x / 2);
            float randomY = (float) (r.nextFloat() * randomOffset.y - randomOffset.y / 2);
            float randomZ = (float) (r.nextFloat() * randomOffset.z - randomOffset.z / 2);
            Vec3 finalOffset = new Vec3(offset.x + randomX, offset.y + randomY, offset.z + randomZ);

            //set scale
            float randomScale = decoration.randomScale();
            float finalScale = decoration.scale() + (r.nextFloat() * randomScale - randomScale / 2);

            abe.decoration = new Decoration.Placed(
                    Blocks.SEA_PICKLE.defaultBlockState(),
                    this,
                    finalOffset,
                    finalScale
            );

            abe.getLevel().playLocalSound(abe.getBlockPos(),
                    Blocks.SEA_PICKLE.getSoundType(Blocks.SEA_PICKLE.defaultBlockState(), abe.getLevel(), abe.getBlockPos(), player).getPlaceSound(),
                    SoundSource.BLOCKS,
                    1.0F, 1.0F,
                    false);

            return true;
        }
        else
        {
            player.displayClientMessage(Component.translatable("block.starcatcher.aquarium.needs_ground"), true);
            return false;
        }
    }
}
