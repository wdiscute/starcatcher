package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.ModCriterionTriggers;
import com.wdiscute.starcatcher.registry.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Generator()));
    }

    private static class Generator implements AdvancementGenerator {

        @SuppressWarnings("removal")
        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
            consumer.accept(Advancement.Builder
                    .advancement()
                    .display(
                            ModItems.AURORA.get(),
                            Component.translatable("advancements.husbandry.starcatcher.fisherman.title"),
                            Component.translatable("advancements.husbandry.starcatcher.fisherman.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    ).addCriterion("perfect_catch", ModCriterionTriggers.MINIGAME_COMPLETED.get()
                            .builder()
                            .perfect()
                            .build()
                    ).parent(ResourceLocation.withDefaultNamespace("husbandry/fishy_business")).build(Starcatcher.rl("husbandry/fisherman"))
            );
        }
    }
}
