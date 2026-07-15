package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.compat.CreateCompat;
import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.*;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGCreateFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {

        //
        //                                   ,--.
        //  ,---. ,--.--.  ,---.   ,--,--. ,-'  '-.  ,---.
        // | .--' |  .--' | .-. : ' ,-.  | '-.  .-' | .-. :
        // \ `--. |  |    \   --. \ '-'  |   |  |   \   --.
        //  `---' `--'     `----'  `--`--'   `--'    `----'
        //


        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish(CreateCompat.COGGILL)
                        .withSizeAndWeight(80, 40, 12000, 7000)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.beach(context)
                        .withFish(CreateCompat.MECHANICAL_SNAIL)
                        .withSizeAndWeight(3, 1, 400, 100)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.EASY),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.beach(context)
                        .withFish(CreateCompat.MECHANICAL_BRASS_SNAIL)
                        .withSizeAndWeight(7, 1, 1400, 800)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish(CreateCompat.PHILLIPSFISH)
                        .withSizeAndWeight(7, 1, 100, 60)
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish(CreateCompat.VALVE)
                        .withSizeAndWeight(7, 1, 300, 100)
                        .withRarity(Rarity.COMMON)
                        .withDifficulty(Difficulty.EASY.moving()),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.allOceans(context)
                        .withFish(CreateCompat.PIPEHEAD)
                        .withSizeAndWeight(7, 1, 2400, 400)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.MEDIUM.moving()),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepOcean(context)
                        .withFish(CreateCompat.COGTOPUS)
                        .withSizeAndWeight(7, 1, 7400, 800)
                        .withRarity(Rarity.RARE)
                        .withBaseChance(3)
                        .withDifficulty(Difficulty.MEDIUM.moving()),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish(CreateCompat.EEL_DYNAMO)
                        .withSizeAndWeight(7, 1, 7400, 800)
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.vanishing()),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLava(context)
                        .withFish(CreateCompat.DRIVE_PIKE)
                        .withSizeAndWeight(7, 1, 7400, 800)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLava(context)
                        .withFish(CreateCompat.BRASSGILL)
                        .withSizeAndWeight(7, 1, 17400, 800)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.HARD),
                "create"
        );

        FishRegistration.register(
                context,
                PresetRestrictions.warmLake(context)
                        .withFish(CreateCompat.MEKA_AGAVE_BREAM)
                        .withSizeAndWeight(new SizeAndWeight(36, 12, 2000, 1000))
                        .withBaseChance(8)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD),
                "create"
        );
    }
}
