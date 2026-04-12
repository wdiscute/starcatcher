package com.wdiscute.starcatcher.compat.curios;

import com.wdiscute.starcatcher.registry.items.HatItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;

public class CuriosCompat
{
    public static boolean isLoaded() {
        return ModList.get().isLoaded("curios");
    }

    public static List<ItemStack> getItems(Player player) {
        List<ItemStack> items = new ArrayList<>();

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            IItemHandlerModifiable equippedCurios = handler.getEquippedCurios();
            for (int i = 0; i < equippedCurios.getSlots(); i++) {
                items.add(equippedCurios.getStackInSlot(i));
            }
        });

        return items;
    }
}
