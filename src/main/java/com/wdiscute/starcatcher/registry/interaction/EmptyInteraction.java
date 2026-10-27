package com.wdiscute.starcatcher.registry.interaction;

import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlockEntity;
import com.wdiscute.starcatcher.blocks.aquarium.Decoration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EmptyInteraction extends AbstractInteraction
{
    @Override
    public boolean interact(Decoration placement, AquariumBlockEntity abe, ItemStack is, Player player)
    {
        return false;
    }
}
