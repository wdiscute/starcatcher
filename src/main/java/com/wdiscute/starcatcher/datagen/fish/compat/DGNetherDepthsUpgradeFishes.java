package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGNetherDepthsUpgradeFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {

        //
        //,--.  ,--.           ,--.   ,--.                          ,------.                     ,--.   ,--.
        //|  ,'.|  |  ,---.  ,-'  '-. |  ,---.   ,---.  ,--.--.     |  .-.  \   ,---.   ,---.  ,-'  '-. |  ,---.   ,---.
        //|  |' '  | | .-. : '-.  .-' |  .-.  | | .-. : |  .--'     |  |  \  : | .-. : | .-. | '-.  .-' |  .-.  | (  .-'
        //|  | `   | \   --.   |  |   |  | |  | \   --. |  |        |  '--'  / \   --. | '-' '   |  |   |  | |  | .-'  `)
        //`--'  `--'  `----'   `--'   `--' `--'  `----' `--'        `-------'   `----' |  |-'    `--'   `--' `--' `----'
        //                                                                             `--'
        //
        //,--. ,--.                                     ,--.
        //|  | |  |  ,---.   ,---.  ,--.--.  ,--,--.  ,-|  |  ,---.
        //|  | |  | | .-. | | .-. | |  .--' ' ,-.  | ' .-. | | .-. :
        //'  '-'  ' | '-' ' ' '-' ' |  |    \ '-'  | \ `-' | \   --.
        // `-----'  |  |-'  .`-  /  `--'     `--`--'  `---'   `----'
        //          `--'    `---'


        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "bonefish")
                        .withBucketedFish("netherdepthsupgrade", "bonefish_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "bonefish")
                        .withSizeAndWeight(120, 40, 700, 200)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.MEDIUM.moving()),
                "netherdepthsupgrade"
        );

        //TODO ADD STRUCTURE RESTRICTION
        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "blazefish")
                        .withBucketedFish("netherdepthsupgrade", "blazefish_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "blazefish")
                        .withSizeAndWeight(160, 29, 5200, 1200)
                        .withRarity(Rarity.LEGENDARY)
                        .withDifficulty(Difficulty.HARD.vanishing()),
                "netherdepthsupgrade"
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "eyeball_fish")
                        .withBucketedFish("netherdepthsupgrade", "eyeball_fish_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "eyeball_fish")
                        .withSizeAndWeight(70, 40, 700, 200)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.HARD.moving()),
                "netherdepthsupgrade"
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "glowdine")
                        .withBucketedFish("netherdepthsupgrade", "glowdine_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "glowdine")
                        .withSizeAndWeight(130, 30, 3400, 900)
                        .withRarity(Rarity.RARE)
                        .withDifficulty(Difficulty.MEDIUM.vanishing().moving()),
                "netherdepthsupgrade"
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "lava_pufferfish")
                        .withBucketedFish("netherdepthsupgrade", "lava_pufferfish_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "lava_pufferfish")
                        .withSizeAndWeight(90, 30, 3700, 900)
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.moving()),
                "netherdepthsupgrade"
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "magmacubefish")
                        .withBucketedFish("netherdepthsupgrade", "magmacubefish_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "magmacubefish")
                        .withSizeAndWeight(120, 40, 3000, 400)
                        .withRarity(Rarity.UNCOMMON)
                        .withDifficulty(Difficulty.EASY.moving()),
                "netherdepthsupgrade"
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "obsidianfish")
                        .withBucketedFish("netherdepthsupgrade", "obsidianfish_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "obsidianfish")
                        .withSizeAndWeight(200, 50, 500000, 68000)
                        .withRarity(Rarity.UNCOMMON)
                , "netherdepthsupgrade"
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "searing_cod")
                        .withBucketedFish("netherdepthsupgrade", "searing_cod_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "searing_cod")
                        .withSizeAndWeight(500, 50, 80000, 20000)
                        .withRarity(Rarity.UNCOMMON)
                , "netherdepthsupgrade"
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "soulsucker")
                        .withBucketedFish("netherdepthsupgrade", "soulsucker_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "soulsucker")
                        .withSizeAndWeight(140, 30, 12000, 3000)
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.MEDIUM.vanishing()),
                "netherdepthsupgrade"
        );

        FishRegistration.register(context,
                PresetRestrictions.netherLava(context)
                        .withFish("netherdepthsupgrade", "wither_bonefish")
                        .withBucketedFish("netherdepthsupgrade", "wither_bonefish_bucket")
                        .withEntityToSpawn("netherdepthsupgrade", "wither_bonefish")
                        .withSizeAndWeight(400, 100, 32000, 7000)
                        .withRarity(Rarity.EPIC)
                        .withDifficulty(Difficulty.HARD.moving()),
                "netherdepthsupgrade"
        );
    }
}
