package com.wdiscute.starcatcher.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.io.components.DataComponents;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class GetNameMixin
{
    @WrapMethod(method = "getHoverName")
    public Component getHoverNameMixin(Operation<Component> original)
    {
        ItemStack stack = (ItemStack) (Object) this;
        Component baseName = original.call();

        if (SCDataComponents.has(stack, SCDataComponents.CAUGHT_FISH_INFO))
        {
            //get sw
            CaughtFishInfo sw = SCDataComponents.get(stack, SCDataComponents.CAUGHT_FISH_INFO);

            //if golden, use golden rarity color
            FishProperties.Rarity rarity = sw.golden() ? FishProperties.Rarity.GOLDEN : sw.rarity();

            //decode name string and return value
            return Tooltips.resolveTagsToComponent(rarity.wrapWithRarityMarkdownAsString(baseName.getString()));
        }

        return baseName;
    }
}
