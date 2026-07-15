package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.ExtraGoldenChanceModifier;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.FishMessagesModifier;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.LuckAttributeModifier;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.KimbeMarkerModifier;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.SpawnTreasureModifier;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCDataEntries;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.datagen.DataEntryProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SCDGDataEntriesProvider
{
    public static void start(DataGenerator gen, PackOutput output, boolean includeServer)
    {
        gen.addProvider(includeServer,
                new DataEntryProvider<>(output, SCDataEntries.DIMENSION_TAGS,
                        Map.of(
                                "overworld", List.of(
                                        Utils.rl("overworld")
                                ),

                                "the_nether", List.of(
                                        Utils.rl("the_nether")
                                ),

                                "the_end", List.of(
                                        Utils.rl("the_end")
                                )
                        )
                )
        );

        gen.addProvider(includeServer,
                new DataEntryProvider<>(output, SCDataEntries.DEFAULT_CATCH_MODIFIERS,
                        List.of(
                                new FishMessagesModifier(0.05f, ""),
                                new LuckAttributeModifier(new HashMap<>()
                                {{
                                    put(Rarity.COMMON, 16);
                                    put(Rarity.UNCOMMON, 17);
                                    put(Rarity.RARE, 18);
                                    put(Rarity.EPIC, 19);
                                    put(Rarity.LEGENDARY, 20);
                                }}, "tooltip.modifier.starcatcher.luck_attribute"),
                                new ExtraGoldenChanceModifier(0.01f, false, ""),
                                new ExtraGoldenChanceModifier(0.01f, true, "")
                        )
                )
        );

        gen.addProvider(includeServer, new DataEntryProvider<>(output, SCDataEntries.DEFAULT_MINIGAME_MODIFIERS,
                List.of(
                        new KimbeMarkerModifier(""),
                        new SpawnTreasureModifier(0.02f, "")
                ))
        );

        ItemStack worm = SCItems.WORM.toStack();
        ItemStack almighty = SCItems.ALMIGHTY_WORM.toStack();
        ItemStack seeking = SCItems.SEEKING_WORM.toStack();
        SCDataComponents.set(worm, SCDataComponents.CAUGHT_FISH_INFO, CaughtFishInfo.GOLDEN);
        SCDataComponents.set(almighty, SCDataComponents.CAUGHT_FISH_INFO, CaughtFishInfo.GOLDEN);
        SCDataComponents.set(seeking, SCDataComponents.CAUGHT_FISH_INFO, CaughtFishInfo.GOLDEN);

        gen.addProvider(includeServer, new DataEntryProvider<>(output, SCDataEntries.FARMLAND_BONEMEAL_DROPS,
                List.of(
                        new Utils.Duo<>(SCItems.WORM.toStack(), 1485),
                        new Utils.Duo<>(SCItems.ALMIGHTY_WORM.toStack(), 396),
                        new Utils.Duo<>(SCItems.SEEKING_WORM.toStack(), 99),
                        new Utils.Duo<>(worm, 15),
                        new Utils.Duo<>(almighty, 4),
                        new Utils.Duo<>(seeking, 1)
                )
        ));
    }
}
