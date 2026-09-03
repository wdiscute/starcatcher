package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TackleBoxInfiniteStorageSlot extends Slot
{
    private static final Identifier BACKGROUND = Starcatcher.rl("fish");
    private final TackleBoxMenu menu;

    public TackleBoxInfiniteStorageSlot(TackleBoxMenu menu, Container container, int slot, int x, int y)
    {
        super(container, slot, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return false;
    }

    @Override
    public @Nullable Identifier getNoItemIcon()
    {
        return BACKGROUND;
    }

    @Override
    public void onTake(Player player, ItemStack stack)
    {
        super.onTake(player, stack);
        menu.be.updateFishSlot();
    }

    @Override
    public ItemStack safeTake(int count, int decrement, Player player)
    {
        return super.safeTake(count, decrement, player);
    }
}
