package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class GetNameMixin
{
    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
    public void getHoverNameMixin(CallbackInfoReturnable<Component> cir)
    {
        ItemStack stack = (ItemStack) (Object) this;

        stack = stack.copy();

        if (SCDataComponents.has(stack, SCDataComponents.CAUGHT_FISH_INFO))
        {
            Component baseName;
            Component customName = stack.get(DataComponents.CUSTOM_NAME);
            Component itemName = stack.get(DataComponents.ITEM_NAME);

            if (customName != null)
            {
                baseName = customName;
            }
            else if (itemName != null)
            {
                baseName = itemName;
            }
            else baseName = Component.translatable(stack.getDescriptionId());

            //get sw
            CaughtFishInfo sw = SCDataComponents.get(stack, SCDataComponents.CAUGHT_FISH_INFO);

            //if golden, use golden rarity color
            FishProperties.Rarity rarity = sw.golden() ? FishProperties.Rarity.GOLDEN : sw.rarity();

            //decode name string and return value
            cir.setReturnValue(rarity.wrapWithRarityMarkdown(baseName.getString()));
            cir.cancel();
        }
    }
}
