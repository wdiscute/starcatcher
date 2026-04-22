package com.wdiscute.starcatcher.blocks.tacklebox;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TackleBoxRodSlot extends Slot
{
    private static final Identifier BACKGROUND = Starcatcher.rl("container/slot/rod");

    TackleBoxMenu tackleMenu;

    public TackleBoxRodSlot(TackleBoxMenu tackleMenu, Container container, int slot, int x, int y)
    {
        super(container, slot, x, y);
        this.tackleMenu = tackleMenu;
        tackleMenu.update();
    }

    @Override
    public @Nullable Identifier getNoItemIcon()
    {
        return BACKGROUND;
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
