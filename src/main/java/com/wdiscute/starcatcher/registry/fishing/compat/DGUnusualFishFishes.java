package com.wdiscute.starcatcher.registry.fishing.compat;

import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.registry.fishing.FishingPropertiesRegistry;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import com.wdiscute.starcatcher.registry.FishProperties;

public class DGUnusualFishFishes extends FishingPropertiesRegistry
{
    public static void bootstrap()
    {

        //
        //,--. ,--.                                             ,--.
        //|  | |  | ,--,--,  ,--.,--.  ,---.  ,--.,--.  ,--,--. |  |
        //|  | |  | |      \ |  ||  | (  .-'  |  ||  | ' ,-.  | |  |
        //'  '-'  ' |  ||  | '  ''  ' .-'  `) '  ''  ' \ '-'  | |  |
        // `-----'  `--''--'  `----'  `----'   `----'   `--`--' `--'
        //,------. ,--.         ,--.
        //|  .---' `--'  ,---.  |  ,---.   ,---.   ,---.
        //|  `--,  ,--. (  .-'  |  .-.  | | .-. : (  .-'
        //|  |`    |  | .-'  `) |  | |  | \   --. .-'  `)
        //`--'     `--' `----'  `--' `--'  `----' `----'
        //

        //ocean
        register(fish(U.holderItem("unusualfishmod", "wizard_jelly_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "wizard_jelly_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "wizard_jelly"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.EPIC)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withDifficulty(FishProperties.Difficulty.HARD)
                .withBaseChance(2)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_aero_mono"))
                .withBucketedFish(U.holderItem("unusualfishmod", "aero_mono_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "aero_mono"))
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.EASY)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_beaked_herring"))
                .withBucketedFish(U.holderItem("unusualfishmod", "beaked_herring_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "beaked_herring"))
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );

        register(fish(U.holderItem("unusualfishmod", "brick_snail_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "brick_snail_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "brick_snail"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.RARE)
                .withWeather(WeatherRestriction.RAIN)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                .withBaseChance(10)
        );

        register(fish(U.holderItem("unusualfishmod", "celestial_fish_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "celestial"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_OCEAN)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                .withWeather(WeatherRestriction.CLEAR)
                .withDifficulty(FishProperties.Difficulty.TWO_AQUA)
                .withBaseChance(30)
        );

        //deep oceans
        register(fish(U.holderItem("unusualfishmod", "tribble_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "tribble"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN)
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("demon_herring")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN)
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
        );

        register(fish(U.holderItem("unusualfishmod", "sea_spider_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "sea_spider"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEP_OCEAN)
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
        );

        //cold oceans
        register(fish(U.holderItem("unusualfishmod", "volt_angler_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "volt_angler"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN)
                .withRarity(FishProperties.Rarity.RARE)
                .withWeather(WeatherRestriction.RAIN)
                .withDifficulty(FishProperties.Difficulty.HARD)
        );

        register(fish(U.holderItem("unusualfishmod", "blizzardfin_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "blizzardfin"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_snowflake"))
                .withBucketedFish(U.holderItem("unusualfishmod", "snowflake_tail_fish_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "snowflaketail"))
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_OCEAN)
                .withRarity(FishProperties.Rarity.EPIC)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
        );

        //lukewarm oceans
        register(fish(U.holderItem("unusualfishmod", "trumpet_squid_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "trumpet_squid_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "trumpet_squid"))
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "squoddle_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "squoddle_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "squoddle"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "spoon_shark_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "spoon_shark"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
        );

        register(helper("circus_fish")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.RARE)
                .withWeather(WeatherRestriction.CLEAR)
                .withDifficulty(FishProperties.Difficulty.HEAVY_FIVE_NORMAL)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
        );

        register(fish(U.holderItem("unusualfishmod", "sea_pancake_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "sea_pancake"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_copperflame_anthias"))
                .withBucketedFish(U.holderItem("unusualfishmod", "copperflame_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "copperflame"))
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(helper("forkfish")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );

        register(fish(U.holderItem("unusualfishmod", "picklefish_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "picklefish_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "picklefish"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withRarity(FishProperties.Rarity.COMMON)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
        );

        register(fish(U.holderItem("unusualfishmod", "porcupine_lobsta_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "porcupine_lobsta_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "porcupine_lobsta"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_LUKEWARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        //warm ocean
        register(fish(U.holderItem("unusualfishmod", "tiger_puffer_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "tiger_puffer_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "tiger_puffer"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.FOUR_THIN_VANISHING)
                .withBaseChance(2)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
        );

        register(fish(U.holderItem("unusualfishmod", "zebra_cornetfish_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "zebra_cornetfish"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.FOUR_THIN_MOVING)
                .withRarity(FishProperties.Rarity.EPIC)
                .withWeather(WeatherRestriction.RAIN)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withBaseChance(20)
        );

        register(helper("amber_goby")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.COMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_sneep_snorp"))
                .withBucketedFish(U.holderItem("unusualfishmod", "sneepsnorp_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "sneep_snorp"))
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );

        register(fish(U.holderItem("unusualfishmod", "sea_mosquito_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "sea_mosquito_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "sea_mosquito"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "clownthorn_shark_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "clownthorn_shark_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "clownthorn_shark"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "coral_skrimp_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "coral_skrimp_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "coral_skrimp"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "crimsonshell_squid_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "crimsonshell_squid_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "crimsonshell"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.THREE_BIG_TWO_THIN_VANISHING)
                .withRarity(FishProperties.Rarity.RARE)
                .withWeather(WeatherRestriction.RAIN)
        );

        register(helper("duality_damselfish")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withDifficulty(FishProperties.Difficulty.EASY_FAST_FISH)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("spindlefish")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_WARM_OCEAN)
                .withWeather(WeatherRestriction.THUNDER)
                .withDifficulty(FishProperties.Difficulty.SINGLE_AQUA)
                .withBaseChance(30)
                .withRarity(FishProperties.Rarity.LEGENDARY)
        );


        //river
        register(helper("triple_twirl_pleco")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(helperOnlyBucket("blackcap_snail")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withRarity(FishProperties.Rarity.RARE)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withBaseChance(1)
        );

        register(helperOnlyBucket("ripper")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_RIVER)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "pinkfin_idol_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "pinkfin_idol_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "pinkfin"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_COLD_RIVER)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDaytimeRestriction(DaytimeRestriction.DAY)
        );


        //swamp(s)
        register(helper("bark_angelfish")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMP_ONLY)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "roughback_guitarfish_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "roughback_guitarfish_spawn_egg"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMP_ONLY)
                .withDifficulty(FishProperties.Difficulty.HARD_MOVING)
                .withRarity(FishProperties.Rarity.RARE)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withBaseChance(7)
        );

        register(helperOnlyBucket("stout_bichir")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MANGROVE_SWAMP)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(helper("drooping_gourami")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MANGROVE_SWAMP)
                .withDifficulty(FishProperties.Difficulty.MEDIUM)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "lobed_skipper_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "lobed_skipper_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "skipper"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MANGROVE_SWAMP)
                .withDifficulty(FishProperties.Difficulty.SINGLE_BIG_FAST_MOVING)
                .withRarity(FishProperties.Rarity.EPIC)
                .withDaytimeRestriction(DaytimeRestriction.MIDNIGHT)
                .withBaseChance(15)
        );

        register(fish(U.holderItem("unusualfishmod", "muddytop_snail_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", "muddytop_snail_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "muddytop"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MANGROVE_SWAMP)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helper("sailor_barb")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SWAMPS)
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        //jungle
        register(fish(U.holderItem("unusualfishmod", "tiger_jungle_shark_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "jungleshark"))
                .withAlwaysSpawnEntity()
                .withDifficulty(FishProperties.Difficulty.EASY_VANISHING)
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(helper("eyelash")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helperOnlyBucket("freshwater_mantis")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING_MOVING)
                .withRarity(FishProperties.Rarity.RARE)
                .withBaseChance(1)
        );

        register(fish(U.holderItem("unusualfishmod", "gnasher_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "gnasher"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_JUNGLE)
                .withDifficulty(FishProperties.Difficulty.SINGLE_THIN_FAST)
                .withRarity(FishProperties.Rarity.LEGENDARY)
                .withDaytimeRestriction(DaytimeRestriction.NIGHT)
                .withWeather(WeatherRestriction.CLEAR)
                .withBaseChance(1)
        );

        //savanna
        register(fish(U.holderItem("unusualfishmod", "rhino_tetra_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "rhino_tetra"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_SAVANNA)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        //mushroom fields
        register(fish(U.holderItem("unusualfishmod", "kalappa_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "kalappa"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_MUSHROOM_FIELDS)
                .withDifficulty(FishProperties.Difficulty.MEDIUM_VANISHING)
                .withRarity(FishProperties.Rarity.RARE)
        );


        //underground
        register(fish(U.holderItem("unusualfishmod", "raw_blind_sailfin"))
                .withBucketedFish(U.holderItem("unusualfishmod", "blind_sailfin_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "blindsailfin"))
                .withAlwaysSpawnEntity()
                .addRestrictions(DimensionRestriction.OVERWORLD,
                        new ElevationRestriction(Integer.MIN_VALUE, 30, ""))
                .withDifficulty(FishProperties.Difficulty.HARD_VANISHING)
                .withRarity(FishProperties.Rarity.RARE)
                .withBaseChance(2)
        );

        register(helperOnlyBucket("deep_crawler")
                .addRestrictions(DimensionRestriction.OVERWORLD,
                        new ElevationRestriction(Integer.MIN_VALUE, 20, ""))
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "raw_hatchetfish"))
                .withBucketedFish(U.holderItem("unusualfishmod", "hatchet_fish_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "hatchet_fish"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DEEPSLATE)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withRarity(FishProperties.Rarity.UNCOMMON)
        );

        register(helperOnlyBucket("mossthorn")
                .addRestrictions(DimensionRestriction.OVERWORLD,
                        BiomeRestriction.LUSH_CAVES,
                        new ElevationRestriction(Integer.MIN_VALUE, 40, ""))
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
                .withRarity(FishProperties.Rarity.COMMON)
        );

        register(fish(U.holderItem("unusualfishmod", "prawn_spawn_egg"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", "prawn"))
                .withAlwaysSpawnEntity()
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD_DRIPSTONE_CAVES)
                .withDifficulty(FishProperties.Difficulty.SINGLE_THIN_FAST)
                .withRarity(FishProperties.Rarity.LEGENDARY)
        );

        register(helperOnlyBucket("shockcat")
                .addRestrictions(FishProperties.WorldRestrictions.OVERWORLD)
                .withRarity(FishProperties.Rarity.UNCOMMON)
                .withDifficulty(FishProperties.Difficulty.EASY_MOVING)
        );
    }

    public static FishProperties.Builder helperOnlyBucket(String s)
    {
        return fish(U.holderItem("unusualfishmod", s + "_bucket"))
                .withBucketedFish(U.holderItem("unusualfishmod", s + "_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", s))
                .withAlwaysSpawnEntity()
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000)
                );
    }

    public static FishProperties.Builder helper(String s)
    {
        return fish(U.holderItem("unusualfishmod", "raw_" + s))
                .withBucketedFish(U.holderItem("unusualfishmod", s + "_bucket"))
                .withEntityToSpawn(U.holderEntity("unusualfishmod", s))
                .withSizeAndWeight(FishProperties.sizeWeight(80, 40, 12000, 7000)
                );
    }
}
