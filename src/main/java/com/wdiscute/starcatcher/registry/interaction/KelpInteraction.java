package com.wdiscute.starcatcher.registry.interaction;

import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlockEntity;
import com.wdiscute.starcatcher.blocks.aquarium.Decoration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class KelpInteraction extends AbstractInteraction
{
    @Override
    public void onDestroyed(Decoration.Placed decoration, AquariumBlockEntity be, @Nullable Player player)
    {
        Level level = be.getLevel();
        BlockPos bs = be.getBlockPos();

        //for 100 blocks above block broken
        for (int i = 0; i < 100; i++)
        {
            if (level.getBlockEntity(bs.offset(0, i, 0)) instanceof AquariumBlockEntity abe)
            {
                //if no decoration break
                if(abe.decoration == null)
                    break;

                //if not kelp break
                if(!abe.decoration.bs().is(Blocks.KELP_PLANT) && !abe.decoration.bs().is(Blocks.KELP))
                    break;

                abe.decoration = null;
                abe.setChanged();
            }
            else
                break;
        }

        //if block under is kelp_plant, make into kelp
        if (level.getBlockEntity(bs.offset(0, -1, 0)) instanceof AquariumBlockEntity below && below.decoration != null)
        {
            if(below.decoration.bs().is(Blocks.KELP_PLANT))
            {
                below.decoration = below.decoration.withBS(Blocks.KELP.defaultBlockState());
            }
        }

        super.onDestroyed(decoration, be, player);
    }

    @Override
    public boolean interact(Decoration decoration, AquariumBlockEntity abe, ItemStack is, Player player)
    {
        if (abe.ground != null)
        {
            if(abe.decoration != null)
                return false;

            Level level = abe.getLevel();
            RandomSource r = level.getRandom();

            //set offset
            Vec3 offset = decoration.offset();
            Vec3 randomOffset = decoration.randomOffset();
            float randomX = (float) (r.nextFloat() * randomOffset.x - randomOffset.x / 2);
            float randomZ = (float) (r.nextFloat() * randomOffset.z - randomOffset.z / 2);

            BlockPos bp = abe.getBlockPos();
            level.playLocalSound(bp,
                    Blocks.KELP.getSoundType(Blocks.KELP.defaultBlockState(), level, bp, player).getBreakSound(),
                    SoundSource.BLOCKS,
                    1.0F, 1.0F,
                    false);

            abe.decoration = new Decoration.Placed(Blocks.KELP.defaultBlockState(), this, new Vec3(offset.x + randomX, 0, offset.z + randomZ), 1);
            return true;
        }
        else
        {
            player.displayClientMessage(Component.translatable("block.starcatcher.aquarium.needs_ground"), true);
            return false;
        }
    }
}
