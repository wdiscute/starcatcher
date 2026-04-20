package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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
    public static List<Identifier> getRlsFromFps(Registry<FishProperties> registry, List<FishProperties> fishProperties)
    {
        List<Identifier> rls = new ArrayList<>();

        for (FishProperties fp : fishProperties)
        {
            Identifier resourceLocation = registry.getKey(fp);
            if (resourceLocation != null) rls.add(resourceLocation);
        }
        return rls;
    }

    public static List<Identifier> getRlsFromFps(RegistryAccess registryAccess, List<FishProperties> fps)
    {
        return getRlsFromFps(registryAccess.getOrThrow(Starcatcher.FISH_REGISTRY_KEY).value(), fps);
    }

    public static List<Identifier> getRlsFromFps(Level level, List<FishProperties> fps)
    {
        return getRlsFromFps(level.registryAccess(), fps);
    }


    //List<ResourceLocation> -> List<TrophyProperties>
    public static List<FishProperties> getFpsFromRls(Registry<FishProperties> registry, List<Identifier> resourceLocations)
    {
        List<FishProperties> fps = new ArrayList<>();

        for (Identifier rl : resourceLocations)
        {
            FishProperties fishProperties = registry.getValue(rl);
            if (fishProperties != null) fps.add(fishProperties);
        }
        return fps;
    }

    public static List<FishProperties> getFpsFromRls(RegistryAccess registryAccess, List<Identifier> rls)
    {
        return getFpsFromRls(registryAccess.getOrThrow(Starcatcher.FISH_REGISTRY_KEY).value(), rls);
    }

    public static List<FishProperties> getFpsFromRls(Level level, List<Identifier> rls)
    {
        return getFpsFromRls(level.registryAccess(), rls);
    }


    //ResourceLocation -> FishProperties
    public static FishProperties getFpFromRl(Registry<FishProperties> registry, Identifier resourceLocation)
    {
        FishProperties fp = registry.getValue(resourceLocation);
        return fp == null ? FishProperties.builder().build() : fp;
    }

    public static FishProperties getFpFromRl(RegistryAccess registryAccess, Identifier rl)
    {
        return getFpFromRl(registryAccess.getOrThrow(Starcatcher.FISH_REGISTRY_KEY).value(), rl);
    }

    public static FishProperties getFpFromRl(Level level, Identifier rl)
    {
        return getFpFromRl(level.registryAccess(), rl);
    }


    //resource location from fish properties
    public static Identifier getRlFromFp(Registry<FishProperties> registry, FishProperties fp)
    {
        Identifier rl = registry.getKey(fp);
        return rl == null ? Starcatcher.rl("missingno_rl") : rl;
    }

    public static Identifier getRlFromFp(RegistryAccess registryAccess, FishProperties tp)
    {
        return getRlFromFp(registryAccess.getOrThrow(Starcatcher.FISH_REGISTRY_KEY).value(), tp);
    }

    public static Identifier getRlFromFp(Level level, FishProperties tp)
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

    public static void renderString(GuiGraphicsExtractor guiGraphics, Font font, Component c, int x, int y, int color)
    {
        guiGraphics.text(font, c, x, y, color, false);
    }

    public static void renderFatString(GuiGraphicsExtractor guiGraphics, Font font, Component c, int x, int y, int color)
    {
        guiGraphics.text(font, c, x + 1, y, 0xffffff, false);
        guiGraphics.text(font, c, x - 1, y, 0xffffff, false);
        guiGraphics.text(font, c, x, y + 1, 0xffffff, false);
        guiGraphics.text(font, c, x, y - 1, 0xffffff, false);
        guiGraphics.text(font, c, x, y, color, false);
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

    public static Identifier rl(String ns, String path)
    {
        return Identifier.fromNamespaceAndPath(ns, path);
    }

    public static Identifier rl(String path)
    {
        return Identifier.fromNamespaceAndPath("minecraft", path);
    }

    public static Holder<Item> holderItem(String ns, String path)
    {
        return Holder.Reference.createStandAlone(BuiltInRegistries.ITEM.getOrThrow(), ResourceKey.create(Registries.ITEM, rl(ns, path)));
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
