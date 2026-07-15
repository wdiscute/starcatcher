package com.wdiscute.starcatcher.compat.curios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

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
