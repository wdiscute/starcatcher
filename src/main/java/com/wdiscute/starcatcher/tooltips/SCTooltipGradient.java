package com.wdiscute.starcatcher.tooltips;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.tuple.Triple;

public class SCTooltipGradient
{
    public static MutableComponent process(String text, Triple<Integer, Integer, Integer> firstColor, Triple<Integer, Integer, Integer> secondColor)
    {
        MutableComponent component = Component.empty();

        double time = System.currentTimeMillis() * 0.0006d;

        for (int i = 0; i < text.length(); i++)
        {
            component.append(Component.literal(String.valueOf(text.charAt(i))).withColor(getBlueColorForIndex(i, time, firstColor, secondColor)));
        }

        return component;
    }

    public static int getBlueColorForIndex(int seed, double time, Triple<Integer, Integer, Integer> firstColor, Triple<Integer, Integer, Integer> secondColor)
    {
        double wavelength = 50.0;
        double t = time + (seed / wavelength) * (2.0 * Math.PI);
        double blend = (Math.sin(t) + 1.0) / 2.0;

        // Dark blue
        int r1 = firstColor.getLeft();
        int g1 = firstColor.getMiddle();
        int b1 = firstColor.getRight();

        // Bright blue
        int r2 = secondColor.getLeft();
        int g2 = secondColor.getMiddle();
        int b2 = secondColor.getRight();

        //ThapPGT bit shifting bs
        int r = (int) (r1 + (r2 - r1) * blend);
        int g = (int) (g1 + (g2 - g1) * blend);
        int b = (int) (b1 + (b2 - b1) * blend);

        return (255 << 24) | (r << 16) | (g << 8) | b;
    }
}
