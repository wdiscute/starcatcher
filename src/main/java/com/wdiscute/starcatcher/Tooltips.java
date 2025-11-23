package com.wdiscute.starcatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.logging.Level;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT)
public class Tooltips
{
    public static float hue;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void renderFrame(TickEvent.LevelTickEvent event)
    {
        if(event.level.isClientSide)
            Tooltips.hue += 0.001f * 2f;
    }

    @SubscribeEvent
    public static void modifyItemTooltip(ItemTooltipEvent event)
    {
        List<Component> tooltipComponents = event.getToolTip();
        ItemStack stack = event.getItemStack();

        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String namespace = rl.getNamespace();
        String path = rl.getPath();
        String baseTooltip = "tooltip." + namespace + "." + path;

        if (I18n.exists(baseTooltip + ".name"))
        {
            tooltipComponents.remove(0);
            tooltipComponents.add(0, decodeTranslationKey(baseTooltip + ".name"));
        }

        if (I18n.exists(baseTooltip + ".0"))
        {
            if (Screen.hasShiftDown())
            {
                tooltipComponents.add(Component.translatable("tooltip.libtooltips.generic.shift_down"));
                tooltipComponents.add(Component.translatable("tooltip.libtooltips.generic.empty"));

                for (int i = 0; i < 100; i++)
                {
                    if (!I18n.exists(baseTooltip + "." + i))
                        break;
                    tooltipComponents.add(decodeTranslationKey(baseTooltip + "." + i));
                }
            }
            else
            {
                tooltipComponents.add(Component.translatable("tooltip.libtooltips.generic.shift_up"));
            }
        }
    }


    public static Component decodeTranslationKey(String s)
    {
        return decodeString(I18n.get(s));
    }

    public static Component decodeString(String s)
    {
        MutableComponent component = Component.empty();

        //transform all <rgb> and <gradient> into it's corresponding things
        for (int i = 0; i < 100; i++)
        {
            if (s.indexOf("<rgb>") < s.indexOf("<gradient-"))
            {
                if (s.contains("<rgb>") && s.contains("</rgb>"))
                {
                    component.append(Component.literal(s.substring(0, s.indexOf("<rgb>"))));
                    component.append(Tooltips.RGBEachLetter(s.substring(s.indexOf("<rgb>") + 5, s.indexOf("</rgb>"))));
                    s = s.substring(s.indexOf("</rgb>") + 6);
                    continue;
                }
            }

            if (s.indexOf("<gradient-") < s.indexOf("<rgb>"))
            {
                if (s.contains("<gradient-") && s.contains("</gradient-"))
                {
                    float min = Float.parseFloat("0." + s.substring(s.indexOf("<gradient") + 10, s.indexOf("<gradient") + 12));
                    float max = Float.parseFloat("0." + s.substring(s.indexOf("</gradient") + 11, s.indexOf("</gradient") + 13));

                    component.append(Component.literal(s.substring(0, s.indexOf("<gradient"))));
                    component.append(Tooltips.gradient(s.substring(s.indexOf("<gradient") + 13, s.indexOf("</gradient")), min, max));
                    s = s.substring(s.indexOf("</gradient-") + 14);
                    continue;
                }
            }

            if (s.contains("<rgb>") && s.contains("</rgb>"))
            {
                component.append(Component.literal(s.substring(0, s.indexOf("<rgb>"))));
                component.append(Tooltips.RGBEachLetter(s.substring(s.indexOf("<rgb>") + 5, s.indexOf("</rgb>"))));
                s = s.substring(s.indexOf("</rgb>") + 6);
                continue;
            }

            if (s.contains("<gradient-") && s.contains("</gradient-"))
            {
                float min = Float.parseFloat("0." + s.substring(s.indexOf("<gradient") + 10, s.indexOf("<gradient") + 12));
                float max = Float.parseFloat("0." + s.substring(s.indexOf("</gradient") + 11, s.indexOf("</gradient") + 13));

                component.append(Component.literal(s.substring(0, s.indexOf("<gradient"))));
                component.append(Tooltips.gradient(s.substring(s.indexOf("<gradient") + 13, s.indexOf("</gradient")), min, max));
                s = s.substring(s.indexOf("</gradient-") + 14);
                continue;
            }

            component.append(s);
            break;

        }

        return component;
    }

    public static Component gradient(String text, float min, float max)
    {
        MutableComponent c = Component.empty();

        for (int i = 0; i < text.length(); i++)
        {
            String s = text.substring(i, i + 1);
            float pingPongedHue = mapHuePingPong(i * 0.01f + hue, min, max);
            int color = hueToRGBInt(pingPongedHue);
            Component l = Component.literal(s).withStyle(Style.EMPTY.withColor((color)));
            c.append(l);
        }

        return c;
    }

    public static float mapHuePingPong(float h, float min, float max)
    {
        float t = Math.abs(2f * (h % 1) - 1f);
        return min + t * (max - min);
    }

    public static Component RGBEachLetter(String text)
    {
        return RGBEachLetter(text, 0.01f);
    }

    public static Component RGBEachLetter(String text, float speed)
    {
        MutableComponent c = Component.empty();

        for (int i = 0; i < text.length(); i++)
        {
            String s = text.substring(i, i + 1);

            int color = hueToRGBInt(i * speed + hue);

            Component l = Component.literal(s).withStyle(Style.EMPTY.withColor(color));

            c.append(l);
        }

        return c;
    }


    public static int hueToRGBInt(float hue)
    {
        int r = 0, g = 0, b = 0;

        float h = (hue - (float) Math.floor(hue)) * 6.0f;
        float f = h - (float) Math.floor(h);
        float q = 1.0f - f;
        float t = 1.0f - (1.0f - f);
        switch ((int) h)
        {
            case 0:
                r = (int) (255.0f + 0.5f);
                g = (int) (t * 255.0f + 0.5f);
                break;
            case 1:
                r = (int) (q * 255.0f + 0.5f);
                g = (int) (255.0f + 0.5f);
                break;
            case 2:
                g = (int) (255.0f + 0.5f);
                b = (int) (t * 255.0f + 0.5f);
                break;
            case 3:
                g = (int) (q * 255.0f + 0.5f);
                b = (int) (255.0f + 0.5f);
                break;
            case 4:
                r = (int) (t * 255.0f + 0.5f);
                g = 0;
                b = (int) (255.0f + 0.5f);
                break;
            case 5:
                r = (int) (255.0f + 0.5f);
                g = 0;
                b = (int) (q * 255.0f + 0.5f);
                break;
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b);
    }

}
