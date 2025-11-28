package com.wdiscute.starcatcher.blocks;

import com.wdiscute.starcatcher.tournament.StandMenu;
import com.wdiscute.starcatcher.tournament.Tournament;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StandBlockEntity extends BlockEntity implements MenuProvider
{
    public Tournament tournament;

    public StandBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.STAND.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.empty();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
    {
        return new StandMenu(i, inventory, this);
    }
}
