package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.*;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jetbrains.annotations.Nullable;

public class DGMinersDelightFishes
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {
        //
        //,--.   ,--. ,--.                          ,--.             ,------.           ,--. ,--.         ,--.        ,--.
        //|   `.'   | `--' ,--,--,   ,---.  ,--.--. |  |  ,---.      |  .-.  \   ,---.  |  | `--'  ,---.  |  ,---.  ,-'  '-.
        //|  |'.'|  | ,--. |      \ | .-. : |  .--' `-'  (  .-'      |  |  \  : | .-. : |  | ,--. | .-. | |  .-.  | '-.  .-'
        //|  |   |  | |  | |  ||  | \   --. |  |         .-'  `)     |  '--'  / \   --. |  | |  | ' '-' ' |  | |  |   |  |
        //`--'   `--' `--' `--''--'  `----' `--'         `----'      `-------'   `----' `--' `--' .`-  /  `--' `--'   `--'
        //                                                                                        `---'

        FishRegistration.register(context,
                PresetRestrictions.river(context)
                        .withFish("miners_delight", "squid")
                        .withEntityToSpawn("minecraft", "squid")
                        .withSizeAndWeight(40, 20, 1300, 700)
                        .withRarity(Rarity.UNCOMMON),
                "miners_delight"
        );

        FishRegistration.register(context,
                PresetRestrictions.caves(context)
                        .withFish("miners_delight", "glow_squid")
                        .withEntityToSpawn("minecraft", "glow_squid")
                        .withSizeAndWeight(40, 20, 1300, 700)
                        .withRarity(Rarity.COMMON),
                "miners_delight"
        );
    }
}
