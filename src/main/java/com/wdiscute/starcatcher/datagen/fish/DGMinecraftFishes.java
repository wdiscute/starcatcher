package com.wdiscute.starcatcher.datagen.fish;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;


public class DGMinecraftFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {
        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(Items.COD)
                        .withBucketedFish(new MaybeStack(Items.COD_BUCKET))
                        .withEntityToSpawn(EntityType.COD.builtInRegistryHolder())
                        .withDifficulty(Difficulty.EASY.moving())
                        .withSizeAndWeight(80, 40, 12000, 7000)
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish(Items.PUFFERFISH)
                        .withBucketedFish(new MaybeStack(Items.PUFFERFISH_BUCKET))
                        .withEntityToSpawn(EntityType.PUFFERFISH.builtInRegistryHolder())
                        .withSizeAndWeight(70, 20, 10000, 3000)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .withRarity(Rarity.UNCOMMON)
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish(Items.TROPICAL_FISH)
                        .withBucketedFish(new MaybeStack(Items.TROPICAL_FISH_BUCKET))
                        .withEntityToSpawn(EntityType.TROPICAL_FISH.builtInRegistryHolder())
                        .withSizeAndWeight(70, 20, 10000, 3000)
                        .withRarity(Rarity.RARE)
        );

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish(Items.SALMON)
                        .withBucketedFish(new MaybeStack(Items.SALMON_BUCKET))
                        .withEntityToSpawn(EntityType.SALMON.builtInRegistryHolder())
                        .withSizeAndWeight(80, 40, 10000, 8000)
        );

        FishRegistration.register(context,
                FishProperties.empty()
                        .withFish(Items.NETHER_STAR)
                        .withAlwaysSpawnEntity()
                        .withEntityToSpawn(EntityType.WITHER.builtInRegistryHolder())
                        .withBaseChance(0)
                        .addRestriction(FluidRestriction.WATER)
                        .addBait(BaitRestriction.WITHER_SKELETON_SKULL)
                        .withDifficulty(Difficulty.WITHER)
                        .withItemToOverrideWith(new MaybeStack(SCItems.UNKNOWN_FISH))
                        .withRarity(Rarity.LEGENDARY)
        );

        FishRegistration.register(context,
                FishProperties.empty()
                        .withFish(Items.CREEPER_HEAD)
                        .withAlwaysSpawnEntity()
                        .withEntityToSpawn(EntityType.CREEPER.builtInRegistryHolder())
                        .withBaseChance(0)
                        .addRestrictions(DimensionRestriction.OVERWORLD)
                        .addRestrictions(FluidRestriction.WATER)
                        .addRestrictions(new BaitRestriction(java.util.Map.of(Starcatcher.rl("gunpowder_bait"), 200), ""))
                        .withDifficulty(Difficulty.CREEPER)
                        .withItemToOverrideWith(new MaybeStack(SCItems.UNKNOWN_FISH))
                        .withRarity(Rarity.EPIC)
        );

        FishRegistration.register(context,
                FishProperties.empty()
                        .withFish(Items.ROTTEN_FLESH)
                        .withEntityToSpawn(EntityType.DROWNED.builtInRegistryHolder())
                        .addRestriction(FluidRestriction.WATER)
                        .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                        .withBaseChance(1)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withWeather(WeatherRestriction.RAIN)
                        .withHasGuideEntry(false)
                        .withAlwaysSpawnEntity()
                        .withSkipsMinigame()
        );
    }
}
