package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.ScreenUtils;
import com.wdiscute.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class BucketTooltipRenderer implements ClientTooltipComponent
{
    public StarcaughtBucket.BucketTooltip tooltip;
    public Component text = Component.empty();

    public BucketTooltipRenderer(StarcaughtBucket.BucketTooltip tooltip)
    {
        this.tooltip = tooltip;

        //caught fish info
        if (SCDataComponents.has(tooltip.fish(), SCDataComponents.CAUGHT_FISH_INFO))
        {
            SizeAndWeight.Units units = SCConfig.UNIT.get();
            CaughtFishInfo cfi = SCDataComponents.get(tooltip.fish(), SCDataComponents.CAUGHT_FISH_INFO);

            if (cfi.golden())
            {
                MutableComponent element = Component.empty().append(Component.translatable("gui.guide.rarity.golden")).withStyle(Style.EMPTY.withColor(0x888888));
                if (Utils.hasShiftDown())
                    element.append(Component.literal(" (top 0%)").withStyle(Style.EMPTY.withColor(0x707070)));
                text = element;
                return;
            }
            String size = units.getSizeAsString(cfi.size());
            String weight = units.getWeightAsString(cfi.weight());
            String percentile = " (top " + (int) cfi.percentile() + "%)";

            MutableComponent element = Component.literal(size + " - " + weight).withStyle(Style.EMPTY.withColor(0x888888));
            if (Utils.hasShiftDown())
                element.append(Component.literal(percentile).withStyle(Style.EMPTY.withColor(0x707070)));
            text = element;
        }

    }

    @Override
    public int getHeight(Font font)
    {
        return isEmpty() ? 0 : 18;
    }

    @Override
    public int getWidth(Font font)
    {
        if (isEmpty()) return 0;

        int ret = 16 + Math.round(text.getString().length() * 5.8f);
        return hasProperties() ? ret : 16;
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor g)
    {
        if (!isEmpty())
        {
            ScreenUtils.item(g, tooltip.fish(), x, y);

            if (hasProperties())
                ScreenUtils.text(g, Minecraft.getInstance().font, text, x + 20, y + 4, 0x888888, true);
        }
    }

    public boolean isEmpty()
    {
        return tooltip.fish().isEmpty();
    }

    public boolean hasProperties()
    {
        if (isEmpty()) return false;
        return SCDataComponents.has(tooltip.fish(), SCDataComponents.CAUGHT_FISH_INFO);
    }
}
