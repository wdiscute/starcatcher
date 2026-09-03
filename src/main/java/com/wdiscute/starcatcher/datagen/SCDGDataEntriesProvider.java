package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.data.BonemealInteractionEntry;
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
import com.wdiscute.utils.EntryOrTag;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import com.wdiscute.utils.datagen.DataEntryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SCDGDataEntriesProvider
{
    public static void start(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        gen.addProvider(true, new DataEntryProvider<>(output, lookup, SCDataEntries.DIMENSION_TAGS,
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

        gen.addProvider(true,
                new DataEntryProvider<>(output, lookup, SCDataEntries.DEFAULT_CATCH_MODIFIERS,
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

        gen.addProvider(true, new DataEntryProvider<>(output, lookup, SCDataEntries.DEFAULT_MINIGAME_MODIFIERS,
                List.of(
                        new KimbeMarkerModifier(""),
                        new SpawnTreasureModifier(0.02f, "")
                ))
        );


        MaybeStack goldenWorm = new MaybeStack(SCItems.WORM.getId(), 0,
                DataComponentPatch.builder()
                        .set(SCDataComponents.CAUGHT_FISH_INFO.get(), CaughtFishInfo.GOLDEN)
                        .set(SCDataComponents.MODIFIERS.get(), List.of(new ExtraGoldenChanceModifier(0.1f, false, "")))
                        .build()
        );

        MaybeStack goldenAlmightyWorm = new MaybeStack(SCItems.ALMIGHTY_WORM.getId(), 0,
                DataComponentPatch.builder()
                        .set(SCDataComponents.CAUGHT_FISH_INFO.get(), CaughtFishInfo.GOLDEN)
                        .set(SCDataComponents.MODIFIERS.get(), List.of(new ExtraGoldenChanceModifier(0.1f, false, "")))
                        .build()
        );

        MaybeStack goldenSeekingWorm = new MaybeStack(SCItems.SEEKING_WORM.getId(), 0,
                DataComponentPatch.builder()
                        .set(SCDataComponents.CAUGHT_FISH_INFO.get(), CaughtFishInfo.GOLDEN)
                        .set(SCDataComponents.MODIFIERS.get(), List.of(new ExtraGoldenChanceModifier(0.1f, false, "")))
                        .build()
        );

        gen.addProvider(true, new DataEntryProvider<>(output, lookup, SCDataEntries.BONEMEAL_INTERACTION_ENTRY,
                List.of(
                        //base worms
                        new BonemealInteractionEntry(
                                new EntryOrTag.Tag<>(Tags.Blocks.VILLAGER_FARMLANDS),
                                new MaybeStack(SCItems.WORM),
                                1485),

                        new BonemealInteractionEntry(
                                new EntryOrTag.Tag<>(Tags.Blocks.VILLAGER_FARMLANDS),
                                new MaybeStack(SCItems.ALMIGHTY_WORM),
                                396),

                        new BonemealInteractionEntry(
                                new EntryOrTag.Tag<>(Tags.Blocks.VILLAGER_FARMLANDS),
                                new MaybeStack(SCItems.SEEKING_WORM),
                                99),


                        //golden worms
                        new BonemealInteractionEntry(
                                new EntryOrTag.Tag<>(Tags.Blocks.VILLAGER_FARMLANDS),
                                goldenWorm,
                                15),

                        new BonemealInteractionEntry(
                                new EntryOrTag.Tag<>(Tags.Blocks.VILLAGER_FARMLANDS),
                                goldenAlmightyWorm,
                                4),

                        new BonemealInteractionEntry(
                                new EntryOrTag.Tag<>(Tags.Blocks.VILLAGER_FARMLANDS),
                                goldenSeekingWorm,
                                1),


                        //base worms on rich soil
                        new BonemealInteractionEntry(
                                new EntryOrTag.Entry<>(ResourceKey.create(Registries.BLOCK, Utils.rl("farmersdelight", "rich_soil_farmland"))),
                                new MaybeStack(SCItems.WORM),
                                50),

                        new BonemealInteractionEntry(
                                new EntryOrTag.Entry<>(ResourceKey.create(Registries.BLOCK, Utils.rl("farmersdelight", "rich_soil_farmland"))),
                                new MaybeStack(SCItems.WORM),
                                30),

                        new BonemealInteractionEntry(
                                new EntryOrTag.Entry<>(ResourceKey.create(Registries.BLOCK, Utils.rl("farmersdelight", "rich_soil_farmland"))),
                                new MaybeStack(SCItems.WORM),
                                20),


                        //golden worms on rich soil
                        new BonemealInteractionEntry(
                                new EntryOrTag.Entry<>(ResourceKey.create(Registries.BLOCK, Utils.rl("farmersdelight", "rich_soil_farmland"))),
                                goldenWorm,
                                5),

                        new BonemealInteractionEntry(
                                new EntryOrTag.Entry<>(ResourceKey.create(Registries.BLOCK, Utils.rl("farmersdelight", "rich_soil_farmland"))),
                                goldenAlmightyWorm,
                                3),

                        new BonemealInteractionEntry(
                                new EntryOrTag.Entry<>(ResourceKey.create(Registries.BLOCK, Utils.rl("farmersdelight", "rich_soil_farmland"))),
                                goldenSeekingWorm,
                                2)
                )
        ));
    }
}
