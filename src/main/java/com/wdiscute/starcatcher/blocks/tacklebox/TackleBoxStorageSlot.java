package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TackleBoxStorageSlot extends Slot
{
    private final TackleBoxBlockEntity tbbe;

    public TackleBoxStorageSlot(Container container, int slot, int x, int y, TackleBoxBlockEntity tbbe)
    {
        super(container, slot, x, y);
        this.tbbe = tbbe;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return !SCConfig.RESTRICT_TACKLE_BOX_TO_TAG.get() || stack.is(SCTags.PLACEABLE_IN_TACKLE_BOX);
    }

    @Override
    public void set(ItemStack stack)
    {
        super.set(stack);
        if(tbbe != null) tbbe.updateFishSlot();
    }
}
