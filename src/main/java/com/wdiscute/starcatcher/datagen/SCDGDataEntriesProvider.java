package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.data.BonemealInteractionEntry;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.ExtraGoldenChanceModifier;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.FishMessagesModifier;
import com.wdiscute.starcatcher.modifiers.catchmodifiers.LittleJoysModifier;
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
        gen.addProvider(true,
                new DataEntryProvider.MultiEntry<>(output,
                        SCDataEntries.DIMENSION_TAGS,
                        List.of(
                                new Utils.Duo<>(Utils.rl("overworld"), "overworld"),
                                new Utils.Duo<>(Utils.rl("the_nether"), "the_nether"),
                                new Utils.Duo<>(Utils.rl("the_end"), "the_end")
                        )
                )
        );

        gen.addProvider(true,
                new DataEntryProvider.MultiEntry<>(output, SCDataEntries.DEFAULT_CATCH_MODIFIERS,
                        List.of(
                                new FishMessagesModifier(0.05f, ""),
                                new LittleJoysModifier(""),

                                //new QualityFoodModifier(""),

                                //new QualityFoodRollModifier(0, 1, false, false, 100, ""),
                                //new QualityFoodRollModifier(0, 4, true, false, 100, ""),
                                //new QualityFoodRollModifier(1, 0, false, true, 100, ""),
                                //new QualityFoodRollModifier(0.5f, 3, false, false, 20, ""),

                                new ExtraGoldenChanceModifier(0.01f, false, ""),
                                new ExtraGoldenChanceModifier(0.01f, true, "")
                        )
                )
        );

        gen.addProvider(true, new DataEntryProvider.MultiEntry<>(output, SCDataEntries.DEFAULT_MINIGAME_MODIFIERS,
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

        gen.addProvider(true, new DataEntryProvider.MultiEntry<>(output, SCDataEntries.BONEMEAL_INTERACTION_ENTRY,
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
