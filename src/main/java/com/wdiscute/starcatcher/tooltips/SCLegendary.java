package com.wdiscute.starcatcher.tooltips;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class SCLegendary
{
    private static final double WAVELENGTH = 100.0;
    private static final double SPEED = 0.1;

    public static MutableComponent process(String text, ItemStack stack, Entity entity)
    {
        MutableComponent component = Component.empty();

        double time = System.nanoTime() * 1.0e-9;

        //rgb each letter based on offset
        for (int i = 0, len = text.length(); i < len; i++)
        {
            char c = text.charAt(i);
            component.append(Component.literal(Character.toString(c)).withColor(getColorForIndex(i, time)));
        }

        return component;
    }

    public static int getColorForIndex(int index, double time)
    {
        //rainbow cycle character count thingy
        float hue = (float) ((time * SPEED + index / WAVELENGTH) % 1.0);
        return Color.HSBtoRGB(hue, 1.0f, 1.0f);
    }
}
