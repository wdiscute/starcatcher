package com.wdiscute.starcatcher.registry.interaction;

import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlockEntity;
import com.wdiscute.starcatcher.blocks.aquarium.Decoration;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class CoralFanInteraction extends AbstractInteraction
{
    //first ground fan
    //second wall fan
    public static final Map<Item, Utils.Duo<Block, Block>> MAP = new ConcurrentHashMap<>();

    static
    {
        //corals
        MAP.put(Items.BRAIN_CORAL_FAN, new Utils.Duo<>(Blocks.BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_WALL_FAN));
        MAP.put(Items.BUBBLE_CORAL_FAN, new Utils.Duo<>(Blocks.BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_WALL_FAN));
        MAP.put(Items.FIRE_CORAL_FAN, new Utils.Duo<>(Blocks.FIRE_CORAL_FAN, Blocks.FIRE_CORAL_WALL_FAN));
        MAP.put(Items.HORN_CORAL_FAN, new Utils.Duo<>(Blocks.HORN_CORAL_FAN, Blocks.HORN_CORAL_WALL_FAN));
        MAP.put(Items.TUBE_CORAL_FAN, new Utils.Duo<>(Blocks.TUBE_CORAL_FAN, Blocks.TUBE_CORAL_WALL_FAN));

        //deadcorals
        MAP.put(Items.DEAD_BRAIN_CORAL_FAN, new Utils.Duo<>(Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_WALL_FAN));
        MAP.put(Items.DEAD_BUBBLE_CORAL_FAN, new Utils.Duo<>(Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_WALL_FAN));
        MAP.put(Items.DEAD_FIRE_CORAL_FAN, new Utils.Duo<>(Blocks.DEAD_FIRE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_WALL_FAN));
        MAP.put(Items.DEAD_HORN_CORAL_FAN, new Utils.Duo<>(Blocks.DEAD_HORN_CORAL_FAN, Blocks.DEAD_HORN_CORAL_WALL_FAN));
        MAP.put(Items.DEAD_TUBE_CORAL_FAN, new Utils.Duo<>(Blocks.DEAD_TUBE_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_WALL_FAN));
    }

    @Override
    public boolean interact(Decoration decoration, AquariumBlockEntity abe, ItemStack is, Player player)
    {
        RandomSource r = abe.getLevel().getRandom();

        Utils.Duo<Block, Block> coralBlocks = MAP.get(is.getItem());
        if (coralBlocks == null)
            return false;

        //place on ground
        if (abe.decoration == null && abe.hasGround())
        {
            BlockState bs = coralBlocks.first().defaultBlockState();
            abe.decoration = new Decoration.Placed(
                    bs,
                    this,
                    getRandomOffset(decoration, r),
                    getRandomScale(decoration, r)
            );

            playPlacedSound(abe, bs);

            return true;
        }

        //place on wall
        if (abe.decoration == null && !abe.hasGround())
        {
            BlockState bs = coralBlocks.second().defaultBlockState();
            abe.decoration = new Decoration.Placed(
                    bs,
                    this,
                    getRandomOffset(decoration, r),
                    getRandomScale(decoration, r)
            );

            playPlacedSound(abe, bs);

            return true;
        }

        //ground to wall
        if (abe.decoration.bs().is(coralBlocks.first()))
        {
            List<Direction> availableDirections = getAvailableDirections(abe);

            if (availableDirections.isEmpty())
                return false;
            BlockState bs = coralBlocks.second().defaultBlockState();
            bs = bs.trySetValue(HorizontalDirectionalBlock.FACING, availableDirections.get(r.nextInt(availableDirections.size())));

            if (!abe.getLevel().isClientSide)
                abe.decoration = new Decoration.Placed(
                        bs,
                        this,
                        getRandomOffset(decoration, r),
                        getRandomScale(decoration, r)
                );

            playPlacedSound(abe, bs);

            return true;
        }

        //rotate on wall
        if (abe.decoration.bs().is(coralBlocks.second()))
        {
            List<Direction> availableDirections = getAvailableDirections(abe);

            if (availableDirections.isEmpty())
                return false;
            BlockState bs = coralBlocks.second().defaultBlockState();
            bs = bs.trySetValue(HorizontalDirectionalBlock.FACING, availableDirections.get(r.nextInt(availableDirections.size())));

            if (!abe.getLevel().isClientSide)
                abe.decoration = new Decoration.Placed(
                        bs,
                        this,
                        getRandomOffset(decoration, r),
                        getRandomScale(decoration, r)
                );

            playPlacedSound(abe, bs);

            return true;
        }

        return false;
    }

    public List<Direction> getAvailableDirections(AquariumBlockEntity abe)
    {
        Level level = abe.getLevel();
        BlockPos bp = abe.getBlockPos();
        List<Direction> list = new ArrayList<>();

        if (!level.getBlockState(bp.north()).is(SCBlocks.AQUARIUM))
            list.add(Direction.SOUTH);

        if (!level.getBlockState(bp.south()).is(SCBlocks.AQUARIUM))
            list.add(Direction.NORTH);

        if (!level.getBlockState(bp.east()).is(SCBlocks.AQUARIUM))
            list.add(Direction.WEST);

        if (!level.getBlockState(bp.west()).is(SCBlocks.AQUARIUM))
            list.add(Direction.EAST);

        if (abe.decoration != null)
        {
            Direction facing = abe.decoration.bs().getOptionalValue(HorizontalDirectionalBlock.FACING).orElse(Direction.DOWN);
            list.remove(facing);
        }

        return list;
    }
}
