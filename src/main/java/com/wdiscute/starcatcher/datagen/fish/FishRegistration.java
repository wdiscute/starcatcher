package com.wdiscute.starcatcher.datagen.fish;

import com.mojang.datafixers.util.Pair;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.compat.CreateCompat;
import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.BurnOnMissModifier;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.TeleportModifier;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import com.wdiscute.starcatcher.registry.items.StarcaughtBucket;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FishRegistration
{
    public static List<FishProperties> ALL_FISHABLE = new ArrayList<>();
    public static Map<FishProperties, ResourceLocation> ALL_FISHABLE_MAP = new HashMap<>();
    public static List<FishProperties> STARCATCHER_FISHABLE = new ArrayList<>();

    //apply fp specific stuff and register
    public static void register(BootstapContext<FishProperties> context, FishProperties fp)
    {
        register(context, fp, "");
    }

    public static void register(BootstapContext<FishProperties> context, FishProperties input, String requiredModId)
    {
        FishProperties fp = prepare(input);
        registerInternal(context, key(fp), fp, requiredModId);
    }

    //register without applying fp specific stuff
    public static void registerRaw(BootstapContext<FishProperties> context, ResourceKey<FishProperties> key, FishProperties properties, String requiredModId)
    {
        registerInternal(context, key, properties, requiredModId);
    }

    public static void registerRaw(BootstapContext<FishProperties> context, ResourceKey<FishProperties> key, FishProperties properties)
    {
        registerInternal(context, key, properties, "");
    }

    private static void registerInternal(BootstapContext<FishProperties> context,
                                         ResourceKey<FishProperties> key,
                                         FishProperties fp,
                                         String requiredModId)
    {
        //if running during conditions generation, dont register as context is null
        if (DGSCFishProperties.runningOnlyForConditions)
        {
            //if has a required modid,
            if (!requiredModId.isEmpty())
                DGSCFishProperties.conditionsFps.add(Pair.of(key, requiredModId));
            return;
        }

        if (fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH) && fp.restrictions().stream().noneMatch(o -> o instanceof FluidRestriction))
            throw new IllegalStateException("No Fluid Restriction found for " + fp);

        //add to lists and maps
        ALL_FISHABLE.add(fp);
        ALL_FISHABLE_MAP.put(fp, key.location());
        //add every starcatcher fish that is not trash to STARCATCHER_FISHABLE tag
        if (fp.catchInfo().fish().identifier().getNamespace().equals("starcatcher") && fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH) && !fp.rarity().equals(Rarity.TRASH))
            STARCATCHER_FISHABLE.add(fp);

        if (DGSCFishProperties.MODS_TO_ACTUALLY_DATAGEN.contains(requiredModId))
            context.register(key, fp);
    }

    private static FishProperties prepare(FishProperties fp)
    {
        fp = sortRestrictions(fp);
        fp = applyModifiers(fp);
        fp = applyStarcaughtLogic(fp);
        fp = applyChanceModifiers(fp);
        fp = applyBaits(fp);
        return fp;
    }

    private static FishProperties applyModifiers(FishProperties fp)
    {
        if (fp.restrictions().stream().anyMatch(o -> o.equals(FluidRestriction.LAVA)))
            return fp.withDifficulty(fp.dif().addModifier(new BurnOnMissModifier(40, 10, 20, "")));

        if (fp.restrictions().stream().anyMatch(o -> o.equals(DimensionRestriction.END)))
            return fp.withDifficulty(fp.dif().addModifier(new TeleportModifier("")));

        return fp;
    }

    private static FishProperties applyBaits(FishProperties fp)
    {
        if (fp.rarity().equals(Rarity.LEGENDARY) && fp.restrictions().stream().noneMatch(o -> o.equals(BaitRestriction.LEGENDARY_BAIT)))
        {
            fp.addBait(BaitRestriction.LEGENDARY_BAIT);
        }
        return fp;
    }

    private static FishProperties applyChanceModifiers(FishProperties fp)
    {
        int chance = fp.baseChance();

        if (fp.restrictions().stream().anyMatch(o -> o.equals(WeatherRestriction.CLEAR)))
            chance += 1;

        if (fp.restrictions().stream().anyMatch(o -> o.equals(WeatherRestriction.RAIN)))
            chance += 5;

        if (fp.restrictions().stream().anyMatch(o -> o.equals(WeatherRestriction.THUNDER)))
            chance += 20;

        if (fp.restrictions().stream().anyMatch(o -> o.equals(DaytimeRestriction.DAY)))
            chance += 1;

        if (fp.restrictions().stream().anyMatch(o -> o.equals(DaytimeRestriction.NOON)))
            chance += 15;

        if (fp.restrictions().stream().anyMatch(o -> o.equals(DaytimeRestriction.NIGHT)))
            chance += 5;

        if (fp.restrictions().stream().anyMatch(o -> o.equals(DaytimeRestriction.MIDNIGHT)))
            chance += 15;

        if (fp.restrictions().stream().anyMatch(o -> o instanceof MoonPhaseRestriction))
            chance += 20;

        if (fp.rarity().equals(Rarity.TRASH))
            chance = (int) (chance * 0.5f);

        return fp.withBaseChance(chance);
    }

    private static FishProperties sortRestrictions(FishProperties fp)
    {
        return fp.withRestrictions(fp.restrictions().stream().sorted().toList());
    }

    private static FishProperties applyStarcaughtLogic(FishProperties fp)
    {
        ItemStack fish = fp.catchInfo().fish().toStack();

        boolean isBucketable = SCItems.BUCKETABLE_FISHES_REGISTRY.getEntries().stream().map(e -> e.getDelegate().value()).anyMatch(fish::is);

        if (isBucketable && fp.catchInfo().fishEntryType() == CatchInfo.FishEntryType.FISH)
        {
            fp = fp.withCatchInfo(fp.catchInfo().withBucket(new MaybeStack(StarcaughtBucket.getBucketForStack(fish))));

            fp = fp.withCatchInfo(fp.catchInfo().withEntityToSpawn(SCEntities.FISH.get().builtInRegistryHolder()));
        }

        return fp;
    }

    public static ResourceKey<FishProperties> key(ResourceLocation id)
    {
        return ResourceKey.create(Starcatcher.FISH_REGISTRY_KEY, id);
    }

    public static ResourceKey<FishProperties> key(FishProperties fp)
    {
        //if starcatcher create compat fish, make key have create instead
        if(CreateCompat.CREATE_COMPAT_FISH.stream().anyMatch(o -> fp.catchInfo().fish().identifier().equals(o.getId())))
            return ResourceKey.create(Starcatcher.FISH_REGISTRY_KEY, Utils.rl("create", fp.catchInfo().fish().identifier().getPath()));

        return ResourceKey.create(Starcatcher.FISH_REGISTRY_KEY, fp.catchInfo().fish().identifier());
    }

    public static ResourceKey<FishProperties> key(String string)
    {
        return ResourceKey.create(Starcatcher.FISH_REGISTRY_KEY, Starcatcher.rl(string));
    }
}
