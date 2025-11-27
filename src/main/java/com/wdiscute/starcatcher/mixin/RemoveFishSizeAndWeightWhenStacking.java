package com.wdiscute.starcatcher.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class RemoveFishSizeAndWeightWhenStacking {
    @Inject(at = @At("HEAD"), method = "overrideStackedOnOther")
    private void stackedOnOther(Slot slot, ClickAction action, Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemBeingClickedOn = slot.getItem();
        ItemStack itemInHand = (ItemStack) (Object)this;

        if(itemBeingClickedOn.is(itemInHand.getItem())) {
            if(itemInHand.hasTag()) {
                itemInHand.getTag().remove("starcatcher_size");
                itemInHand.getTag().remove("starcatcher_weight");
            }

            if(itemBeingClickedOn.hasTag()) {
                itemBeingClickedOn.getTag().remove("starcatcher_size");
                itemBeingClickedOn.getTag().remove("starcatcher_weight");
            }
        }
    }

}
