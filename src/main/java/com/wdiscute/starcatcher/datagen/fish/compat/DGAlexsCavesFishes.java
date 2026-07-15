package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.registry.fishrestrictions.BiomeRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.DimensionRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.FluidRestriction;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGAlexsCavesFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {

        //
        //  ,---.   ,--.                    ,--.              ,-----.
        // /  O  \  |  |  ,---.  ,--.  ,--. |  |  ,---.      '  .--./  ,--,--. ,--.  ,--.  ,---.   ,---.
        //|  .-.  | |  | | .-. :  \  `'  /  `-'  (  .-'      |  |     ' ,-.  |  \  `'  /  | .-. : (  .-'
        //|  | |  | |  | \   --.  /  /.  \       .-'  `)     '  '--'\ \ '-'  |   \    /   \   --. .-'  `)
        //`--' `--' `--'  `----' '--'  '--'      `----'       `-----'  `--`--'    `--'     `----' `----'
        //

        final BiomeRestriction TOXIC_CAVES = BiomeRestriction.empty().biome(Utils.rl("alexscaves", "toxic_caves"));
        final BiomeRestriction CANDY_CAVITY = BiomeRestriction.empty().biome(Utils.rl("alexscaves", "candy_cavity"));
        final BiomeRestriction ABYSSAL_CHASM = BiomeRestriction.empty().biome(Utils.rl("alexscaves", "abyssal_chasm"));
        final BiomeRestriction PRIMORDIAL_CAVES = BiomeRestriction.empty().biome(Utils.rl("alexscaves", "primordial_caves"));

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("alexscaves", "trilocaris_tail")
                        .withBucketedFish(new MaybeStack("alexscaves", "trilocaris_bucket"))
                        .withEntityToSpawn("alexscaves", "trilocaris")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD,
                                PRIMORDIAL_CAVES,
                                FluidRestriction.WATER),
                "alexscaves"
        );

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("alexscaves", "radgill")
                        .withBucketedFish(new MaybeStack("alexscaves", "radgill_bucket"))
                        .withEntityToSpawn("alexscaves", "radgill")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD)
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD,
                                TOXIC_CAVES,
                                FluidRestriction.ACID),
                "alexscaves"
        );

        FishRegistration.register(context,
                PresetRestrictions.empty(context)
                        .withFish("alexscaves", "sweetish_fish_blue")
                        .withBucketedFish(new MaybeStack("alexscaves", "sweetish_fish_blue_bucket"))
                        .withEntityToSpawn("alexscaves", "sweetish_fish")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD,
                                CANDY_CAVITY,
                                FluidRestriction.PURPLE_SODA
                        ),
                "alexscaves"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish("alexscaves", "sweetish_fish_green")
                        .withBucketedFish(new MaybeStack("alexscaves", "sweetish_fish_blue_bucket"))
                        .withEntityToSpawn("alexscaves", "sweetish_fish")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD,
                                CANDY_CAVITY,
                                FluidRestriction.PURPLE_SODA
                        ),
                "alexscaves"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish("alexscaves", "sweetish_fish_pink")
                        .withBucketedFish(new MaybeStack("alexscaves", "sweetish_fish_pink_bucket"))
                        .withEntityToSpawn("alexscaves", "sweetish_fish")
                        .withSizeAndWeight(80, 40, 12000, 70000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD,
                                CANDY_CAVITY,
                                FluidRestriction.PURPLE_SODA
                        ),
                "alexscaves"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish("alexscaves", "sweetish_fish_red")
                        .withBucketedFish(new MaybeStack("alexscaves", "sweetish_fish_red_bucket"))
                        .withEntityToSpawn("alexscaves", "sweetish_fish")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD,
                                CANDY_CAVITY,
                                FluidRestriction.PURPLE_SODA
                        ),
                "alexscaves"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish("alexscaves", "sweetish_fish_yellow")
                        .withBucketedFish(new MaybeStack("alexscaves", "sweetish_fish_yellow_bucket"))
                        .withEntityToSpawn("alexscaves", "sweetish_fish")
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD,
                                CANDY_CAVITY,
                                FluidRestriction.PURPLE_SODA
                        ),
                "alexscaves"
        );


        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish("alexscaves", "lanternfish")
                        .withBucketedFish(new MaybeStack("alexscaves", "lanternfish_bucket"))
                        .withEntityToSpawn("alexscaves", "lanternfish")
                        .withSizeAndWeight(100, 50, 15000, 10000)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD.moving())
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD,
                                ABYSSAL_CHASM,
                                FluidRestriction.WATER
                        ),
                "alexscaves"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish("alexscaves", "tripodfish")
                        .withBucketedFish(new MaybeStack("alexscaves", "tripodfish_bucket"))
                        .withEntityToSpawn("alexscaves", "tripodfish")
                        .withSizeAndWeight(30, 10, 1000, 5000)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD.moving())
                        .addRestrictions(
                                DimensionRestriction.OVERWORLD,
                                ABYSSAL_CHASM,
                                FluidRestriction.WATER
                        ),
                "alexscaves"
        );
    }
}
