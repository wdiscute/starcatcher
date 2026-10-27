package com.wdiscute.starcatcher.registry.interaction;

import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlockEntity;
import com.wdiscute.starcatcher.blocks.aquarium.Decoration;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractInteraction
{
    public abstract boolean interact(Decoration decoration, AquariumBlockEntity abe, ItemStack is, Player player);

    public Vec3 getRandomOffset(Decoration decoration, RandomSource r)
    {
        Vec3 offset = decoration.offset();
        Vec3 randomOffset = decoration.randomOffset();
        float randomX = (float) (r.nextFloat() * randomOffset.x - randomOffset.x / 2);
        float randomY = (float) (r.nextFloat() * randomOffset.y - randomOffset.y / 2);
        float randomZ = (float) (r.nextFloat() * randomOffset.z - randomOffset.z / 2);
        return new Vec3(offset.x + randomX, offset.y + randomY, offset.z + randomZ);
    }

    public float getRandomScale(Decoration decoration, RandomSource r)
    {
        float randomScale = decoration.randomScale();
        return decoration.scale() + (r.nextFloat() * randomScale - randomScale / 2);
    }

    public void onDestroyed(Decoration.Placed decoration, AquariumBlockEntity abe, @Nullable Player player)
    {
        abe.decoration = null;
        playDestroyedSound(decoration, abe,player);
    }

    public void playPlacedSound(AquariumBlockEntity abe, BlockState bs)
    {
        abe.getLevel().playLocalSound(abe.getBlockPos(),
                bs.getBlock().getSoundType(bs, abe.getLevel(), abe.getBlockPos(), null).getPlaceSound(),
                SoundSource.BLOCKS,
                1.0F, 1.0F,
                false);
    }

    public void playDestroyedSound(Decoration.Placed decoration, AquariumBlockEntity abe, @Nullable Player player)
    {
        abe.getLevel().playLocalSound(abe.getBlockPos(),
                decoration.bs().getBlock().getSoundType(decoration.bs(), abe.getLevel(), abe.getBlockPos(), player).getBreakSound(),
                SoundSource.BLOCKS,
                1.0F, 1.0F,
                false);
    }
}
