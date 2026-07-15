package com.wdiscute.starcatcher.blocks.tacklebox;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TackleBoxAttachmentSlot extends Slot
{
    ResourceLocation background;
    TackleBoxMenu tackleMenu;
    TagKey<Item> mayPlaceTag;

    public TackleBoxAttachmentSlot(TackleBoxMenu tackleMenu, TagKey<Item> mayPlaceTag, Container container, int slot, int x, int y, ResourceLocation background)
    {
        super(container, slot, x, y);
        this.background = background;
        this.tackleMenu = tackleMenu;
        this.mayPlaceTag = mayPlaceTag;
        tackleMenu.update();
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon()
    {
        if(tackleMenu.getRod().isEmpty()) return null;
        return Pair.of(InventoryMenu.BLOCK_ATLAS, background);
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return stack.is(mayPlaceTag) && !tackleMenu.getRod().isEmpty() && super.mayPlace(stack);
    }

    @Override
    public void onTake(Player player, ItemStack stack)
    {
        if(tackleMenu != null)
            tackleMenu.update();

        super.onTake(player, stack);
    }

    @Override
    public void setByPlayer(ItemStack stack)
    {
        super.setByPlayer(stack);
        if(tackleMenu != null)
        {
            tackleMenu.update();
        }
    }
}
