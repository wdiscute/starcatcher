package com.wdiscute.starcatcher.registry.interaction;

import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlockEntity;
import com.wdiscute.starcatcher.blocks.aquarium.Decoration;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class SimpleInteraction extends AbstractInteraction
{
    @Override
    public boolean interact(Decoration decoration, AquariumBlockEntity abe, ItemStack is, Player player)
    {
        if (is.getItem() instanceof BlockItem block)
        {
            if(abe.decoration != null)
                return false;

            RandomSource r = abe.getLevel().getRandom();

            abe.decoration = new Decoration.Placed(
                    block.getBlock().defaultBlockState(),
                    this,
                    getRandomOffset(decoration, r),
                    getRandomScale(decoration,r)
            );

            abe.getLevel().playLocalSound(abe.getBlockPos(),
                    block.getBlock().getSoundType(block.getBlock().defaultBlockState(), abe.getLevel(), abe.getBlockPos(), player).getPlaceSound(),
                    SoundSource.BLOCKS,
                    1.0F, 1.0F,
                    false);

            return true;
        }

        return false;
    }
}
