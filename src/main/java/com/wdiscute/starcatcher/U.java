package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.TrophyProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class U
{

    //List<TrophyProperties> -> List<ResourceLocation>
    public static List<TrophyProperties> getTpsFromRls(Registry<TrophyProperties> registry, List<ResourceLocation> resourceLocations)
    {
        List<TrophyProperties> tps = new ArrayList<>();

        for (ResourceLocation rl : resourceLocations)
        {
            TrophyProperties trophyProperties = registry.get(rl);
            if(trophyProperties != null) tps.add(trophyProperties);
        }
        return tps;
    }

    public static List<TrophyProperties> getTpsFromRls(RegistryAccess registryAccess, List<ResourceLocation> rls)
    {
        return getTpsFromRls(registryAccess.registryOrThrow(Starcatcher.TROPHY_REGISTRY), rls);
    }

    public static List<TrophyProperties> getTpsFromRls(Level level, List<ResourceLocation> rls)
    {
        return getTpsFromRls(level.registryAccess(), rls);
    }




    //List<FishProperties> -> List<ResourceLocation>
    public static List<ResourceLocation> getRlsFromFps(Registry<FishProperties> registry, List<FishProperties> fishProperties)
    {
        List<ResourceLocation> rls = new ArrayList<>();

        for (FishProperties fp : fishProperties)
        {
            ResourceLocation resourceLocation = registry.getKey(fp);
            if(resourceLocation != null) rls.add(resourceLocation);
        }
        return rls;
    }

    public static List<ResourceLocation> getRlsFromFps(RegistryAccess registryAccess, List<FishProperties> fps)
    {
        return getRlsFromFps(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY), fps);
    }

    public static List<ResourceLocation> getRlsFromFps(Level level, List<FishProperties> fps)
    {
        return getRlsFromFps(level.registryAccess(), fps);
    }








    //List<TrophyProperties> -> List<ResourceLocation>
    public static List<ResourceLocation> getRlsFromTps(Registry<TrophyProperties> registry, List<TrophyProperties> trophyProperties)
    {
        List<ResourceLocation> rls = new ArrayList<>();

        for (TrophyProperties tp : trophyProperties)
        {
            ResourceLocation resourceLocation = registry.getKey(tp);
            if(resourceLocation != null) rls.add(resourceLocation);
        }
        return rls;
    }

    public static List<ResourceLocation> getRlsFromTps(RegistryAccess registryAccess, List<TrophyProperties> tps)
    {
        return getRlsFromTps(registryAccess.registryOrThrow(Starcatcher.TROPHY_REGISTRY), tps);
    }

    public static List<ResourceLocation> getRlsFromTps(Level level, List<TrophyProperties> tps)
    {
        return getRlsFromTps(level.registryAccess(), tps);
    }





    //ResourceLocation -> TrophyProperties
    public static TrophyProperties getTpFromRl(Registry<TrophyProperties> registry, ResourceLocation resourceLocation)
    {
        TrophyProperties tp = registry.get(resourceLocation);
        return tp == null ? TrophyProperties.DEFAULT : tp;
    }

    public static TrophyProperties getTpFromRl(RegistryAccess registryAccess, ResourceLocation rl)
    {
        return getTpFromRl(registryAccess.registryOrThrow(Starcatcher.TROPHY_REGISTRY), rl);
    }

    public static TrophyProperties getTpFromRl(Level level, ResourceLocation rl)
    {
        return getTpFromRl(level.registryAccess(), rl);
    }





    //TrophyProperties -> ResourceLocation
    public static ResourceLocation getRlFromTp(Registry<TrophyProperties> registry, TrophyProperties tp)
    {
        ResourceLocation rl = registry.getKey(tp);
        return rl == null ? Starcatcher.rl("missingno_rl") : rl;
    }

    public static ResourceLocation getRlFromTp(RegistryAccess registryAccess, TrophyProperties tp)
    {
        return getRlFromTp(registryAccess.registryOrThrow(Starcatcher.TROPHY_REGISTRY), tp);
    }

    public static ResourceLocation getRlFromTp(Level level, TrophyProperties tp)
    {
        return getRlFromTp(level.registryAccess(), tp);
    }





    //List<ResourceLocation> -> List<TrophyProperties>
    public static List<FishProperties> getFpsFromRls(Registry<FishProperties> registry, List<ResourceLocation> resourceLocations)
    {
        List<FishProperties> fps = new ArrayList<>();

        for (ResourceLocation rl : resourceLocations)
        {
            FishProperties fishProperties = registry.get(rl);
            if(fishProperties != null) fps.add(fishProperties);
        }
        return fps;
    }

    public static List<FishProperties> getFpsFromRls(RegistryAccess registryAccess, List<ResourceLocation> rls)
    {
        return getFpsFromRls(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY), rls);
    }

    public static List<FishProperties> getFpsFromRls(Level level, List<ResourceLocation> rls)
    {
        return getFpsFromRls(level.registryAccess(), rls);
    }





    //ResourceLocation -> FishProperties
    public static FishProperties getFpFromRl(Registry<FishProperties> registry, ResourceLocation resourceLocation)
    {
        FishProperties fp = registry.get(resourceLocation);
        return fp == null ? FishProperties.DEFAULT : fp;
    }

    public static FishProperties getFpFromRl(RegistryAccess registryAccess, ResourceLocation rl)
    {
        return getFpFromRl(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY), rl);
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
        return getRlFromFp(registryAccess.registryOrThrow(Starcatcher.FISH_REGISTRY), tp);
    }

    public static ResourceLocation getRlFromFp(Level level, FishProperties tp)
    {
        return getRlFromFp(level.registryAccess(), tp);
    }


    @SafeVarargs
    public static <T> boolean containsAny(List<T> list, T... contains)
    {
        for (T s : contains)
            if(list.contains(s)) return true;

        return false;
    }

    @SafeVarargs
    public static <T> boolean containsAll(List<T> list, T... contains)
    {
        for (T s : contains)
            if(!list.contains(s)) return false;

        return true;
    }

    @SafeVarargs
    public static <T> boolean containsNone(List<T> list, T... contains)
    {
        return !containsAny(list, contains);
    }

}
