package com.wdiscute.starcatcher.datagen.fish.compat;

import com.wdiscute.starcatcher.datagen.fish.FishRegistration;
import com.wdiscute.starcatcher.datagen.fish.PresetRestrictions;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.registry.fishrestrictions.*;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class DGTrophies
{
    public static void bootstrap(@Nullable BootstrapContext<FishProperties> context)
    {

        //
        //   ,--.                           ,--.      ,--.
        // ,-'  '-. ,--.--.  ,---.   ,---.  |  ,---.  `--'  ,---.   ,---.
        // '-.  .-' |  .--' | .-. | | .-. | |  .-.  | ,--. | .-. : (  .-'
        //   |  |   |  |    ' '-' ' | '-' ' |  | |  | |  | \   --. .-'  `)
        //   `--'   `--'     `---'  |  |-'  `--' `--' `--'  `----' `----'
        //                          `--'

        FishRegistration.register(
                context,
                FishProperties.empty()
                        .withFish(SCBlocks.TROPHY_COPPER)
                        .withMaxLimit(1)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withRarity(Rarity.COMMON)
                        .addRarityRestriction(
                                new RarityCountRestriction.RarityCount(Rarity.NONE, 25, RarityCountRestriction.RarityCount.CountType.TOTAL)
                        )
                        .trophy()
        );

        FishRegistration.register(
                context,
                FishProperties.empty()
                        .withFish(SCBlocks.TROPHY_IRON)
                        .withMaxLimit(1)
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.UNCOMMON)
                        .withHasGuideEntry(false)
                        .addRarityRestriction(
                                new RarityCountRestriction.RarityCount(Rarity.NONE, 35, RarityCountRestriction.RarityCount.CountType.UNIQUE)
                        )
                        .trophy()
        );

        FishRegistration.register(
                context,
                FishProperties.empty()
                        .withFish(SCBlocks.TROPHY_GOLD)
                        .withMaxLimit(1)
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.RARE)
                        .withHasGuideEntry(false)
                        .addRarityRestriction(
                                new RarityCountRestriction.RarityCount(Rarity.NONE, 100, RarityCountRestriction.RarityCount.CountType.TOTAL),
                                new RarityCountRestriction.RarityCount(Rarity.LEGENDARY, 2, RarityCountRestriction.RarityCount.CountType.UNIQUE),
                                new RarityCountRestriction.RarityCount(Rarity.GOLDEN, 2, RarityCountRestriction.RarityCount.CountType.UNIQUE)
                        )
                        .trophy()
        );

        FishRegistration.register(
                context,
                FishProperties.empty()
                        .withFish(SCBlocks.TROPHY_EMERALD)
                        .withMaxLimit(1)
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.EPIC)
                        .withHasGuideEntry(false)
                        .addRarityRestriction(
                                new RarityCountRestriction.RarityCount(Rarity.NONE, 200, RarityCountRestriction.RarityCount.CountType.TOTAL),
                                new RarityCountRestriction.RarityCount(Rarity.LEGENDARY, 10, RarityCountRestriction.RarityCount.CountType.UNIQUE)
                        )
                        .trophy()
        );

        FishRegistration.register(
                context,
                FishProperties.empty()
                        .withFish(SCBlocks.TROPHY_DIAMOND)
                        .withMaxLimit(1)
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.LEGENDARY)
                        .withHasGuideEntry(false)
                        .addRarityRestriction(
                                new RarityCountRestriction.RarityCount(Rarity.NONE, 500, RarityCountRestriction.RarityCount.CountType.TOTAL),
                                new RarityCountRestriction.RarityCount(Rarity.NONE, 0, RarityCountRestriction.RarityCount.CountType.ALL)
                        )
                        .trophy()
        );

        FishRegistration.register(
                context,
                FishProperties.empty()
                        .withFish(SCBlocks.TROPHY_OF_THE_OLDER_ANGLER)
                        .withMaxLimit(1)
                        .withDifficulty(Difficulty.TRASH)
                        .withRarity(Rarity.LEGENDARY)
                        .withHasGuideEntry(false)
                        .addRarityRestriction(
                                new RarityCountRestriction.RarityCount(Rarity.NONE, 0, RarityCountRestriction.RarityCount.CountType.ALL),
                                new RarityCountRestriction.RarityCount(Rarity.GOLDEN, 0, RarityCountRestriction.RarityCount.CountType.ALL)
                        )
                        .trophy()
        );

        //
        //                      ,--.
        //  ,---.  ,--.  ,--. ,-'  '-. ,--.--.  ,--,--.  ,---.
        // | .-. :  \  `'  /  '-.  .-' |  .--' ' ,-.  | (  .-'
        // \   --.  /  /.  \    |  |   |  |    \ '-'  | .-'  `)
        //  `----' '--'  '--'   `--'   `--'     `--`--' `----'
        //

        FishRegistration.register(
                context,
                PresetRestrictions.deepslate(context)
                        .withFish(Items.DIAMOND)
                        .withMaxLimit(1)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.GOLDEN, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLava(context)
                        .withFish(Items.NETHERITE_SCRAP)
                        .withMaxLimit(3)
                        .withDifficulty(Difficulty.TRASH)
                        .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.GOLDEN, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                        .withHasGuideEntry(false)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLava(context)
                        .withFish(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                        .withMaxLimit(1)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.GOLDEN, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                        .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.LEGENDARY, 5, RarityCountRestriction.RarityCount.CountType.TOTAL))
                        .extra()
        );

        //skin/tackle templates
        FishRegistration.register(
                context,
                PresetRestrictions.empty(context)
                        .withFish(SCItems.VALLEY_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .addRestriction(DimensionRestriction.OVERWORLD)
                        .addRestriction(StructureRestriction.VILLAGES)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.swamp(context)
                        .withFish(SCItems.FROG_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLavaBasaltDeltas(context)
                        .withFish(SCItems.SURVIVOR_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.iceSpikes(context)
                        .withFish(SCItems.ICEBORN_SKIN_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.river(context)
                        .withFish(SCItems.HUMBLE_SKIN_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.forest(context)
                        .withFish(SCItems.NATURALIST_SKIN_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.netherLavaBasaltDeltas(context)
                        .withFish(SCItems.MAGMAFORGED_SKIN_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.swamp(context)
                        .withFish(SCItems.SLIMED_SKIN_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.bambooJungle(context)
                        .withFish(SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.surfaceLava(context)
                        .withFish(SCItems.OBSIDIAN_SKIN_SMITHING_TEMPLATE)
                        .withWeather(WeatherRestriction.RAIN)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        FishRegistration.register(
                context,
                PresetRestrictions.soulSandValley(context)
                        .withFish(SCItems.BONER_SKIN_SMITHING_TEMPLATE)
                        .withDifficulty(Difficulty.TRASH)
                        .withHasGuideEntry(false)
                        .withPercentageChance(0.01f)
                        .extra()
        );

        //                                         ,--.
        // ,---.   ,---.   ,---. ,--.--.  ,---.  ,-'  '-.  ,---.
        //(  .-'  | .-. : | .--' |  .--' | .-. : '-.  .-' (  .-'
        //.-'  `) \   --. \ `--. |  |    \   --.   |  |   .-'  `)
        //`----'   `----'  `---' `--'     `----'   `--'   `----'
        //

        //amethyst hook
        {
            ItemStack stack = SCItems.MESSAGE_IN_A_BOTTLE.toStack();
            SCDataComponents.set(stack, SCDataComponents.MESSAGE, Message.BuiltIn.AMETHYST_HOOK);
            FishRegistration.registerRaw(context,
                    FishRegistration.key("message/amethyst_hook"),
                    PresetRestrictions.empty(context)
                            .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                            .withFish(stack)
                            .withMaxLimit(1)
                            .withDifficulty(Difficulty.TRASH)
                            .withHasGuideEntry(false)
                            .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.EPIC, 10, RarityCountRestriction.RarityCount.CountType.TOTAL))
                            .message()
            );
        }

        //hopeful
        {
            ItemStack stack = SCItems.MESSAGE_IN_A_BOTTLE.toStack();
            SCDataComponents.set(stack, SCDataComponents.MESSAGE, Message.BuiltIn.HOPEFUL);
            FishRegistration.registerRaw(context,
                    FishRegistration.key("message/hopeful"),
                    PresetRestrictions.empty(context)
                            .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                            .withFish(stack)
                            .withMaxLimit(1)
                            .withDifficulty(Difficulty.TRASH)
                            .withHasGuideEntry(false)
                            .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.RARE, 10, RarityCountRestriction.RarityCount.CountType.TOTAL))
                            .message()
            );
        }

        //hopeless
        {
            ItemStack stack = SCItems.MESSAGE_IN_A_BOTTLE.toStack();
            SCDataComponents.set(stack, SCDataComponents.MESSAGE, Message.BuiltIn.HOPELESS);
            FishRegistration.registerRaw(context,
                    FishRegistration.key("message/hopeless"),
                    PresetRestrictions.empty(context)
                            .addRestriction(ElevationRestriction.ABOVE_FIFTY)
                            .withFish(stack)
                            .withMaxLimit(1)
                            .withDifficulty(Difficulty.TRASH)
                            .withHasGuideEntry(false)
                            .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.RARE, 15, RarityCountRestriction.RarityCount.CountType.TOTAL))
                            .message()
            );
        }


        //lava
        {
            ItemStack stack = SCItems.MESSAGE_IN_A_BOTTLE.toStack();
            SCDataComponents.set(stack, SCDataComponents.MESSAGE, Message.BuiltIn.LAVA_PROOF_1);
            FishRegistration.registerRaw(context,
                    FishRegistration.key("message/lava_1"),
                    PresetRestrictions.surfaceLava(context)
                            .withFish(stack)
                            .withMaxLimit(1)
                            .withDifficulty(Difficulty.TRASH)
                            .withHasGuideEntry(false)
                            .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.LEGENDARY, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                            .message()
            );
        }

        //lava 2
        {
            ItemStack stack = SCItems.MESSAGE_IN_A_BOTTLE.toStack();
            SCDataComponents.set(stack, SCDataComponents.MESSAGE, Message.BuiltIn.LAVA_PROOF_2);
            FishRegistration.registerRaw(context,
                    FishRegistration.key("message/lava_2"),
                    PresetRestrictions.surfaceLava(context)
                            .withFish(stack)
                            .withMaxLimit(1)
                            .withDifficulty(Difficulty.TRASH)
                            .withHasGuideEntry(false)
                            .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.LEGENDARY, 2, RarityCountRestriction.RarityCount.CountType.TOTAL))
                            .message()
            );
        }


        //true blue
        {
            ItemStack stack = SCItems.MESSAGE_IN_A_BOTTLE.toStack();
            SCDataComponents.set(stack, SCDataComponents.MESSAGE, Message.BuiltIn.TRUE_BLUE);
            FishRegistration.registerRaw(context,
                    FishRegistration.key("message/true_blue"),
                    PresetRestrictions.empty(context)
                            .withPercentageChance(0.004f)
                            .withFish(stack)
                            .withMaxLimit(1)
                            .withDifficulty(Difficulty.TRASH)
                            .withHasGuideEntry(false)
                            .message()
            );
        }

        //wither
        {
            ItemStack stack = SCItems.MESSAGE_IN_A_BOTTLE.toStack();
            SCDataComponents.set(stack, SCDataComponents.MESSAGE, Message.BuiltIn.WITHER);
            FishRegistration.registerRaw(context,
                    FishRegistration.key("message/wither"),
                    PresetRestrictions.empty(context)
                            .withFish(stack)
                            .withMaxLimit(1)
                            .withDifficulty(Difficulty.TRASH)
                            .withHasGuideEntry(false)
                            .message()
                            .withBaseChance(0)
                            .addBait(BaitRestriction.WITHER_SKELETON_SKULL)
            );
        }


        //
        //  ,-----.                               ,--.
        // '  .-.  '   ,--.,--.  ,---.   ,---.  ,-'  '-.
        // |  | |  |   |  ||  | | .-. : (  .-'  '-.  .-'
        // '  '-'  '-. '  ''  ' \   --. .-'  `)   |  |
        //  `-----'--'  `----'   `----' `----'    `--'
        //

        {
            FishRegistration.registerRaw(context,
                    FishRegistration.key("quest/amethyst_hook"),
                    PresetRestrictions.deepslate(context)
                            .withFish(SCItems.AMETHYST_HOOK)
                            .withMaxLimit(1)
                            .withDifficulty(Difficulty.TRASH)
                            .withHasGuideEntry(false)
                            .addRarityRestriction(new RarityCountRestriction.RarityCount(Rarity.EPIC, 1, RarityCountRestriction.RarityCount.CountType.TOTAL))
                            .extra()
            );
        }

    }
}
