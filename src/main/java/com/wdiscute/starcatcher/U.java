package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class U
{
    public static final Random r = new Random();

    //List<FishProperties> -> List<ResourceLocation>
    public static List<ResourceLocation> getRlsFromFps(Registry<FishProperties> registry, List<FishProperties> fishProperties)
    {
        List<ResourceLocation> rls = new ArrayList<>();

        for (FishProperties fp : fishProperties)
        {
            ResourceLocation resourceLocation = registry.getKey(fp);
            if (resourceLocation != null) rls.add(resourceLocation);
        }
        return rls;
    }

    public static List<ResourceLocation> getRlsFromFps(RegistryAccess registryAccess, List<FishProperties> fps)
    {
        return getRlsFromFps(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY), fps);
    }

    public static List<ResourceLocation> getRlsFromFps(Level level, List<FishProperties> fps)
    {
        return getRlsFromFps(level.registryAccess(), fps);
    }


    //List<ResourceLocation> -> List<TrophyProperties>
    public static List<FishProperties> getFpsFromRls(Registry<FishProperties> registry, List<ResourceLocation> resourceLocations)
    {
        List<FishProperties> fps = new ArrayList<>();

        for (ResourceLocation rl : resourceLocations)
        {
            FishProperties fishProperties = registry.get(rl);
            if (fishProperties != null) fps.add(fishProperties);
        }
        return fps;
    }

    public static List<FishProperties> getFpsFromRls(RegistryAccess registryAccess, List<ResourceLocation> rls)
    {
        return getFpsFromRls(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY), rls);
    }

    public static List<FishProperties> getFpsFromRls(Level level, List<ResourceLocation> rls)
    {
        return getFpsFromRls(level.registryAccess(), rls);
    }


    //ResourceLocation -> FishProperties
    public static FishProperties getFpFromRl(Registry<FishProperties> registry, ResourceLocation resourceLocation)
    {
        FishProperties fp = registry.get(resourceLocation);
        return fp == null ? FishProperties.builder().build() : fp;
    }

    public static FishProperties getFpFromRl(RegistryAccess registryAccess, ResourceLocation rl)
    {
        return getFpFromRl(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY), rl);
    }

    public static FishProperties getFpFromRl(Level level, ResourceLocation rl)
    {
        return getFpFromRl(level.registryAccess(), rl);
    }


    //resource location from fish properties
    public static ResourceLocation getRlFromFp(Registry<FishProperties> registry, FishProperties fp)
    {
        ResourceLocation rl = registry.getKey(fp);
        return rl == null ? Starcatcher.rl("missingno_rl") : rl;
    }

    public static ResourceLocation getRlFromFp(RegistryAccess registryAccess, FishProperties tp)
    {
        return getRlFromFp(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY_KEY), tp);
    }

    public static ResourceLocation getRlFromFp(Level level, FishProperties tp)
    {
        return getRlFromFp(level.registryAccess(), tp);
    }

    public static String calculateRealLifeTimeFromTicks(long ticks)
    {
        long ticksRemainingToCalculate = ticks / 20;
        String finalString = "";

        //days
        if (ticksRemainingToCalculate > 86400)
        {
            finalString += ticksRemainingToCalculate / 86400 + "d ";
            ticksRemainingToCalculate = ticksRemainingToCalculate % 86400;
        }

        //hours
        if (ticksRemainingToCalculate > 3600)
        {
            finalString += ticksRemainingToCalculate / 3600 + "h ";
            ticksRemainingToCalculate = ticksRemainingToCalculate % 3600;
        }

        //minutes
        if (ticksRemainingToCalculate > 60)
        {
            finalString += ticksRemainingToCalculate / 60 + "m ";
            ticksRemainingToCalculate = ticksRemainingToCalculate % 60;
        }

        //seconds
        if (ticksRemainingToCalculate > 0)
        {
            finalString += ticksRemainingToCalculate + "s";
        }
        return finalString;
    }

    public static void renderString(GuiGraphics guiGraphics, Font font, Component c, int x, int y, int color)
    {
        guiGraphics.drawString(font, c, x, y, color, false);
    }

    public static void renderFatString(GuiGraphics guiGraphics, Font font, Component c, int x, int y, int color)
    {
        guiGraphics.drawString(font, c, x + 1, y, 0xffffff, false);
        guiGraphics.drawString(font, c, x - 1, y, 0xffffff, false);
        guiGraphics.drawString(font, c, x, y + 1, 0xffffff, false);
        guiGraphics.drawString(font, c, x, y - 1, 0xffffff, false);
        guiGraphics.drawString(font, c, x, y, color, false);
    }

    @SafeVarargs
    public static <T> boolean containsAny(List<T> list, T... contains)
    {
        for (T s : contains)
            if (list.contains(s)) return true;

        return false;
    }

    @SafeVarargs
    public static <T> boolean containsAll(List<T> list, T... contains)
    {
        for (T s : contains)
            if (!list.contains(s)) return false;
        return true;
    }

    @SafeVarargs
    public static <T> boolean containsNone(List<T> list, T... contains)
    {
        return !containsAny(list, contains);
    }

    public static ResourceLocation rl(String ns, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(ns, path);
    }

    public static ResourceLocation rl(String path)
    {
        return ResourceLocation.fromNamespaceAndPath("minecraft", path);
    }

    public static Holder<Item> holderItem(String ns, String path)
    {
        return Holder.Reference.createStandAlone(BuiltInRegistries.ITEM.holderOwner(), ResourceKey.create(Registries.ITEM, rl(ns, path)));
    }

    public static Holder<Item> holderItem(DeferredItem<Item> item)
    {
        return Holder.direct(item.get());
    }

    public static Holder<Item> holderItem(Item item)
    {
        return Holder.direct(item);
    }

    public static Holder<EntityType<?>> holderEntity(EntityType<?> entityType)
    {
        return Holder.direct(entityType);
    }

    public static Holder<EntityType<?>> holderEntity(String ns, String path)
    {
        return Holder.Reference.createStandAlone(BuiltInRegistries.ENTITY_TYPE.holderOwner(), ResourceKey.create(Registries.ENTITY_TYPE, rl(ns, path)));
    }

    public static Holder<EntityType<?>> holderEntity(Supplier<EntityType<FishEntity>> entity)
    {
        return Holder.direct(entity.get());
    }

    public static boolean alwaysTrue(Object... o)
    {
        return true;
    }

    public static boolean alwaysFalse(Object... o)
    {
        return false;
    }

    public static void nothing(Object... o)
    {
    }

    //0-255
    public static int intToRed(int packedColor)
    {
        return packedColor >> 16 & 255;
    }

    //0-255
    public static int intToGreen(int packedColor)
    {
        return packedColor >> 8 & 255;
    }

    //0-255
    public static int intToBlue(int packedColor)
    {
        return packedColor & 255;
    }

    public static int sign(float x)
    {
        return x >= 0 ? 1 : -1;
    }

    public static long getTime()
    {
        return java.time.Instant.now().getEpochSecond();
    }
}
