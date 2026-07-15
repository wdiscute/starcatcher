package com.wdiscute.starcatcher.blocks.tacklebox;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TackleBoxRodSlot extends Slot
{
    private static final ResourceLocation BACKGROUND = Starcatcher.rl("item/background/rod");

    TackleBoxMenu tackleMenu;

    public TackleBoxRodSlot(TackleBoxMenu tackleMenu, Container container, int slot, int x, int y)
    {
        super(container, slot, x, y);
        this.tackleMenu = tackleMenu;
        tackleMenu.update();
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon()
    {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, BACKGROUND);
    }

    @Override
    public void onTake(Player player, ItemStack stack)
    {
        if(tackleMenu != null)
            tackleMenu.update();

        super.onTake(player, stack);
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return stack.is(SCTags.RODS);
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack)
    {
        super.setByPlayer(newStack, oldStack);
        tackleMenu.onPlaceRod(newStack);
    }
}
