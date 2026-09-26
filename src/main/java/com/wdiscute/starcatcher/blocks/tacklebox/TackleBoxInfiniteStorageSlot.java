package com.wdiscute.starcatcher.blocks.tacklebox;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TackleBoxInfiniteStorageSlot extends Slot
{
    private static final ResourceLocation BACKGROUND = Starcatcher.rl("item/background/fish");

    public TackleBoxInfiniteStorageSlot(Container container, int slot, int x, int y)
    {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return container.getItem(index).isEmpty() && stack.is(SCTags.FISHABLE);
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon()
    {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, BACKGROUND);
    }

    @Override
    public void onTake(Player player, ItemStack stack)
    {
        super.onTake(player, stack);
        if(container instanceof TackleBoxContainer tbc)
            tbc.updateFishSlot();
    }
}
