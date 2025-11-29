package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.io.ModDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class RemoveFishSizeAndWeightWhenStacking
{

    @Inject(at = @At("HEAD"), method = "overrideStackedOnOther", cancellable = true)
    private void stackedOnMe(Slot slot, ClickAction action, Player player, CallbackInfoReturnable<Boolean> cir)
    {
        ItemStack itemBeingClickedOn = player.getSlot(slot.index).get();
        ItemStack itemInHand = (ItemStack) (Object)this;

        if(itemBeingClickedOn.is(itemInHand.getItem()))
        {
            itemInHand.remove(ModDataComponents.SIZE_AND_WEIGHT);
            itemBeingClickedOn.remove(ModDataComponents.SIZE_AND_WEIGHT);
        }

    }

}
