package com.wdiscute.starcatcher.registry.interaction;

import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlockEntity;
import com.wdiscute.starcatcher.blocks.aquarium.Decoration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class BoneMealInteraction extends AbstractInteraction
{
    public static final Map<Function<Decoration.Placed, Boolean>, PlacementFunction> MAP = new ConcurrentHashMap<>();

    public interface PlacementFunction
    {
        boolean place(Decoration placement, AquariumBlockEntity be, ItemStack is, Player player);
    }

    static
    {
        //kelp growing
        MAP.put((dec) -> dec.bs().is(Blocks.KELP) || dec.bs().is(Blocks.KELP_PLANT),
                (placement, abe, is, p) ->
                {
                    for (int i = 0; i < 100; i++)
                    {
                        BlockPos bp = abe.getBlockPos();
                        Level level = abe.getLevel();
                        if (level.getBlockEntity(bp.offset(0, i, 0)) instanceof AquariumBlockEntity above)
                        {
                            //if there's a decoration already there
                            if(above.decoration != null)
                            {
                                //if there's already a decoration there that is not kelp, break
                                if (!above.decoration.bs().is(Blocks.KELP) && !above.decoration.bs().is(Blocks.KELP_PLANT))
                                    break;

                                //if there's already a decoration there, and it is kelp, continue to next block
                                if ((above.decoration.bs().is(Blocks.KELP) || above.decoration.bs().is(Blocks.KELP_PLANT)))
                                    continue;
                            }

                            //set kelp
                            above.decoration = new Decoration.Placed(Blocks.KELP.defaultBlockState(), abe.decoration.type(), abe.decoration.offset(), abe.decoration.scale());
                            level.playLocalSound(bp, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                            ParticleUtils.spawnParticles(level, bp, 7, 1.0, 1.0, true, ParticleTypes.HAPPY_VILLAGER);

                            //set kelp under to plant block
                            if (level.getBlockEntity(bp.offset(0, i - 1, 0)) instanceof AquariumBlockEntity below)
                            {
                                below.decoration = new Decoration.Placed(Blocks.KELP_PLANT.defaultBlockState(), abe.decoration.type(), abe.decoration.offset(), abe.decoration.scale());
                            }

                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }

                    return false;
                });

        //seagrass growing
        MAP.put((bs) -> bs.bs().is(Blocks.SEAGRASS),
                (placement, abe, is, p) ->
                {
                    BlockPos bp = abe.getBlockPos();
                    Level level = abe.getLevel();
                    //if block above is an aquarium
                    if (level.getBlockEntity(bp.offset(0, 1, 0)) instanceof AquariumBlockEntity above)
                    {
                        //if there's already a decoration there
                        if (above.decoration != null)
                            return false;

                        //set seagrass
                        BlockState bs = Blocks.TALL_SEAGRASS.defaultBlockState();
                        abe.decoration = abe.decoration.withBS(bs.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.LOWER));

                        level.playLocalSound(bp, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                        ParticleUtils.spawnParticles(level, bp, 7, 1.0, 1.0, true, ParticleTypes.HAPPY_VILLAGER);

                        above.decoration = new Decoration.Placed(
                                bs.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER),
                                abe.decoration.type(),
                                abe.decoration.offset(),
                                abe.decoration.scale()
                        );

                        return true;
                    }
                    else
                        return false;
                });
    }

    @Override
    public boolean interact(Decoration decoration, AquariumBlockEntity abe, ItemStack is, Player player)
    {
        if (abe.decoration != null)
        {
            for (var entry : MAP.entrySet())
            {
                if (entry.getKey().apply(abe.decoration))
                {
                    return entry.getValue().place(decoration, abe, is, player);
                }
            }
        }

        return false;
    }
}
