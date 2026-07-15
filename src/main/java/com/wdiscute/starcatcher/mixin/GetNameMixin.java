package com.wdiscute.starcatcher.mixin;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.fish.FishApi;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
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

            //get cfi
            CaughtFishInfo caughtFishInfo = SCDataComponents.get(stack, SCDataComponents.CAUGHT_FISH_INFO);

            //decode name string and return value
            cir.setReturnValue(Tooltips.resolveTagsToComponent(caughtFishInfo.rarity().wrapWithRarityMarkdownAsString(baseName.getString())));
            cir.cancel();
        }
    }
}
