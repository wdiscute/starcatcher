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

public class SeagrassInteraction extends AbstractInteraction
{
    @Override
    public void onDestroyed(Decoration.Placed decoration, AquariumBlockEntity abe, Player player)
    {
        if (decoration.bs().is(Blocks.TALL_SEAGRASS))
        {
            DoubleBlockHalf half = decoration.bs().getOptionalValue(TallSeagrassBlock.HALF).orElse(DoubleBlockHalf.UPPER);
            BlockPos relatedBlock = half == DoubleBlockHalf.LOWER ? abe.getBlockPos().above() : abe.getBlockPos().below();

            if (abe.getLevel().getBlockEntity(relatedBlock) instanceof AquariumBlockEntity related)
            {
                related.decoration = null;
                related.setChanged();
            }
        }

        super.onDestroyed(decoration, abe, player);
    }

    public boolean interact(Decoration decoration, AquariumBlockEntity abe, ItemStack is, Player player)
    {
        if (abe.ground != null)
        {
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
                    Blocks.SEAGRASS.defaultBlockState(),
                    this,
                    finalOffset,
                    finalScale
            );

            abe.getLevel().playLocalSound(abe.getBlockPos(),
                    Blocks.SEAGRASS.getSoundType(Blocks.SEAGRASS.defaultBlockState(), abe.getLevel(), abe.getBlockPos(), player).getPlaceSound(),
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
