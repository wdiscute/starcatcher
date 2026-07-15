package com.wdiscute.starcatcher.datagen;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DGSCAdvancementProvider extends AdvancementProvider {
    public DGSCAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Generator()));
    }

    private static class Generator implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
//            consumer.accept(Advancement.Builder
//                    .advancement()
//                    .display(
//                            ModItems.AURORA.get(),
//                            Component.translatable("advancements.husbandry.starcatcher.fisherman.title"),
//                            Component.translatable("advancements.husbandry.starcatcher.fisherman.description"),
//                            null,
//                            AdvancementType.CHALLENGE,
//                            true,
//                            true,
//                            false
//                    ).addCriterion("perfect_catch", ModCriterionTriggers.MINIGAME_COMPLETED.get()
//                            .builder()
//                            .perfect()
//                            .build()
//                    ).parent(ResourceLocation.withDefaultNamespace("husbandry/fishy_business")).build(Starcatcher.rl("husbandry/fisherman"))
//            );
        }
    }
}
