package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.registry.fishrestrictions.DaytimeRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.WeatherRestriction;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGHybridAquaticFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {

        //Literally The Coolest Mod Ever (this fact has been fact-checked by true fisherman ✅)

        //
        // ,--.  ,--.           ,--.            ,--.    ,--.       ,---.                               ,--.   ,--.
        // |  '--'  | ,--. ,--. |  |-.  ,--.--. `--'  ,-|  |      /  O  \   ,---.  ,--.,--.  ,--,--. ,-'  '-. `--'  ,---.
        // |  .--.  |  \  '  /  | .-. ' |  .--' ,--. ' .-. |     |  .-.  | | .-. | |  ||  | ' ,-.  | '-.  .-' ,--. | .--'
        // |  |  |  |   \   '   | `-' | |  |    |  | \ `-' |     |  | |  | ' '-' | '  ''  ' \ '-'  |   |  |   |  | \ `--.
        // `--'  `--' .-'  /     `---'  `--'    `--'  `---'      `--' `--'  `-|  |  `----'   `--`--'   `--'   `--'  `---'
        //            `---'                                                   `--'

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "clownfish")
                        .withEntityToSpawn("hybrid_aquatic", "clownfish")
                        .withSizeAndWeight(8, 3, 140, 60)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "surgeonfish")
                        .withEntityToSpawn("hybrid_aquatic", "surgeonfish")
                        .withSizeAndWeight(15, 5, 600, 200)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "blowfish")
                        .withEntityToSpawn("hybrid_aquatic", "blowfish")
                        .withSizeAndWeight(18, 4, 5, 3)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "boxfish")
                        .withEntityToSpawn("hybrid_aquatic", "boxfish")
                        .withSizeAndWeight(27, 13, 180, 90)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "damselfish")
                        .withEntityToSpawn("hybrid_aquatic", "damselfish")
                        .withSizeAndWeight(6, 3, 18, 5)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "moray_eel")
                        .withEntityToSpawn("hybrid_aquatic", "moray_eel")
                        .withSizeAndWeight(180, 70, 21000, 8000)
                        .withDifficulty(Difficulty.HARD.vanishing())
                        .withRarity(Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "blue_spotted_stingray")
                        .withEntityToSpawn("hybrid_aquatic", "stingray")
                        .withSizeAndWeight(50, 30, 4000, 1000)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "spotted_eagle_ray")
                        .withEntityToSpawn("hybrid_aquatic", "stingray")
                        .withSizeAndWeight(400, 100, 20000, 3000)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "lionfish")
                        .withEntityToSpawn("hybrid_aquatic", "lionfish")
                        .withSizeAndWeight(33, 5, 900, 150)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "needlefish")
                        .withEntityToSpawn("hybrid_aquatic", "needlefish")
                        .withSizeAndWeight(100, 10, 2300, 300)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "stonefish")
                        .withEntityToSpawn("hybrid_aquatic", "stonefish")
                        .withSizeAndWeight(35, 5, 1500, 500)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NOON)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "parrotfish")
                        .withEntityToSpawn("hybrid_aquatic", "parrotfish")
                        .withSizeAndWeight(40, 10, 1600, 1400)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "seahorse")
                        .withEntityToSpawn("hybrid_aquatic", "seahorse")
                        .withSizeAndWeight(15, 10, 20, 15)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "triggerfish")
                        .withEntityToSpawn("hybrid_aquatic", "triggerfish")
                        .withSizeAndWeight(90, 10, 4300, 2500)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.warmOcean(context)
                        .withFish("hybrid_aquatic", "flying_fish")
                        .withEntityToSpawn("hybrid_aquatic", "flying_fish")
                        .withSizeAndWeight(19, 11, 570, 330)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "anglerfish")
                        .withEntityToSpawn("hybrid_aquatic", "anglerfish")
                        .withSizeAndWeight(10.5f, 7.5f, 585, 295)
                        .withDifficulty(Difficulty.EASY.vanishing())
                        .withRarity(Rarity.UNCOMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "barreleye")
                        .withEntityToSpawn("hybrid_aquatic", "barreleye")
                        .withSizeAndWeight(15, 5, 99.25f, 42.5f)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.UNCOMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "dragonfish")
                        .withEntityToSpawn("hybrid_aquatic", "dragonfish")
                        .withSizeAndWeight(12, 3, 41.5f, 11.5f)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "coelacanth")
                        .withEntityToSpawn("hybrid_aquatic", "coelacanth")
                        .withSizeAndWeight(175, 25, 59500, 8000)
                        .withDifficulty(Difficulty.HARD.vanishing())
                        .withRarity(Rarity.RARE),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "oarfish")
                        .withEntityToSpawn("hybrid_aquatic", "oarfish")
                        .withSizeAndWeight(500, 300, 122727.25f, 61363.6f)
                        .withDifficulty(Difficulty.HARD.vanishing())
                        .withRarity(Rarity.RARE)
                        .withWeather(WeatherRestriction.THUNDER),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "ratfish")
                        .withEntityToSpawn("hybrid_aquatic", "ratfish")
                        .withSizeAndWeight(90, 10, 4300, 2500)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "snailfish")
                        .withEntityToSpawn("hybrid_aquatic", "snailfish")
                        .withSizeAndWeight(19.65f, 9.15f, 84, 76)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "john_dory")
                        .withEntityToSpawn("hybrid_aquatic", "john_dory")
                        .withSizeAndWeight(50, 15, 3846.15f, 1153.85f)
                        .withDifficulty(Difficulty.MEDIUM.vanishing())
                        .withRarity(Rarity.UNCOMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "flashlight_fish")
                        .withEntityToSpawn("hybrid_aquatic", "flashlight_fish")
                        .withSizeAndWeight(20, 10, 80, 40)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "squirrelfish")
                        .withEntityToSpawn("hybrid_aquatic", "squirrelfish")
                        .withSizeAndWeight(21, 3, 300, 100)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish("hybrid_aquatic", "tuna")
                        .withEntityToSpawn("hybrid_aquatic", "tuna")
                        .withSizeAndWeight(300, 100, 680250, 226750)
                        .withDifficulty(Difficulty.HARD.moving())
                        .withRarity(Rarity.UNCOMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish("hybrid_aquatic", "mahi")
                        .withEntityToSpawn("hybrid_aquatic", "mahi")
                        .withSizeAndWeight(90, 10, 4300, 2500)
                        .withDifficulty(Difficulty.HARD.moving())
                        .withRarity(Rarity.UNCOMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish("hybrid_aquatic", "mackerel")
                        .withEntityToSpawn("hybrid_aquatic", "mackerel")
                        .withSizeAndWeight(48, 18, 1250, 750)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.coldOcean(context)
                        .withFish("hybrid_aquatic", "herring")
                        .withEntityToSpawn("hybrid_aquatic", "herring")
                        .withSizeAndWeight(40, 12, 663.4f, 436.6f)
                        .withDifficulty(Difficulty.EASY.moving())
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish("hybrid_aquatic", "sea_bass")
                        .withEntityToSpawn("hybrid_aquatic", "sea_bass")
                        .withSizeAndWeight(40, 12, 1600, 1100)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish("hybrid_aquatic", "sheepshead_wrasse")
                        .withEntityToSpawn("hybrid_aquatic", "wrasse")
                        .withSizeAndWeight(90, 10, 4300, 2500)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish("hybrid_aquatic", "pearlfish")
                        .withEntityToSpawn("hybrid_aquatic", "pearlfish")
                        .withSizeAndWeight(14.45f, 2.15f, 2.675f, 1.435f)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.allOceans(context)
                        .withFish("hybrid_aquatic", "rockfish")
                        .withEntityToSpawn("hybrid_aquatic", "rockfish")
                        .withSizeAndWeight(90, 10, 4300, 2500)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "opah")
                        .withEntityToSpawn("hybrid_aquatic", "opah")
                        .withSizeAndWeight(90, 10, 4300, 2500)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withRarity(Rarity.UNCOMMON)
                        .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.deepOcean(context)
                        .withFish("hybrid_aquatic", "ocean_sunfish")
                        .withEntityToSpawn("hybrid_aquatic", "ocean_sunfish")
                        .withSizeAndWeight(230, 100, 1273500, 1026500)
                        .withDifficulty(Difficulty.HARD.moving())
                        .withRarity(Rarity.RARE)
                        .withDaytimeRestriction(DaytimeRestriction.DAY)
                        .withWeather(WeatherRestriction.CLEAR),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.cherryGrove(context)
                        .withFish("hybrid_aquatic", "goldfish")
                        .withEntityToSpawn("hybrid_aquatic", "goldfish")
                        .withSizeAndWeight(20, 5, 200, 100)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.UNCOMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish("hybrid_aquatic", "carp")
                        .withEntityToSpawn("hybrid_aquatic", "carp")
                        .withSizeAndWeight(60, 20, 8000, 4000)
                        .withDifficulty(Difficulty.EASY.vanishing())
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish("hybrid_aquatic", "golden_dorado")
                        .withEntityToSpawn("hybrid_aquatic", "golden_dorado")
                        .withSizeAndWeight(37, 20, 6500, 3500)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.RARE),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish("hybrid_aquatic", "oscar")
                        .withEntityToSpawn("hybrid_aquatic", "oscar")
                        .withSizeAndWeight(30, 10, 5290, 3710)
                        .withDifficulty(Difficulty.EASY.vanishing())
                        .withRarity(Rarity.UNCOMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish("hybrid_aquatic", "neon_tetra")
                        .withEntityToSpawn("hybrid_aquatic", "tetra")
                        .withSizeAndWeight(4, 1, 0.37f, 0.12f)
                        .withDifficulty(Difficulty.HARD)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish("hybrid_aquatic", "tiger_barb")
                        .withEntityToSpawn("hybrid_aquatic", "tiger_barb")
                        .withSizeAndWeight(10, 3, 1.9f, 0.5f)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish("hybrid_aquatic", "betta")
                        .withEntityToSpawn("hybrid_aquatic", "betta")
                        .withSizeAndWeight(7, 1, 1.8f, 0.3f)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.UNCOMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish("hybrid_aquatic", "danio")
                        .withEntityToSpawn("hybrid_aquatic", "danio")
                        .withSizeAndWeight(4.4f, 0.6f, 0.7f, 0.2f)
                        .withDifficulty(Difficulty.EASY.vanishing())
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish("hybrid_aquatic", "gourami")
                        .withEntityToSpawn("hybrid_aquatic", "gourami")
                        .withSizeAndWeight(90, 10, 4300, 2500)
                        .withDifficulty(Difficulty.MEDIUM)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish("hybrid_aquatic", "discus")
                        .withEntityToSpawn("hybrid_aquatic", "discus")
                        .withSizeAndWeight(90, 10, 4300, 2500)
                        .withDifficulty(Difficulty.MEDIUM.moving())
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );

        FishRegistration.register(context,
                PresetRestrictions.jungle(context)
                        .withFish("hybrid_aquatic", "pleco")
                        .withEntityToSpawn("hybrid_aquatic", "pleco")
                        .withSizeAndWeight(35.5f, 25.5f, 170.1f, 122.2f)
                        .withRarity(Rarity.COMMON),
                "hybrid_aquatic"
        );
    }
}
