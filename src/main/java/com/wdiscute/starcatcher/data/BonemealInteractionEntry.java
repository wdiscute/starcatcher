package com.wdiscute.starcatcher.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.registry.SCDataEntries;
import com.wdiscute.utils.EntryOrTag;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record BonemealInteractionEntry(EntryOrTag<Block> block, MaybeStack stack, int weight)
{
    public static final Codec<BonemealInteractionEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    EntryOrTag.codec(Registries.BLOCK).fieldOf("block").forGetter(BonemealInteractionEntry::block),
                    MaybeStack.CODEC.fieldOf("item").forGetter(BonemealInteractionEntry::stack),
                    Codec.INT.fieldOf("weight").forGetter(BonemealInteractionEntry::weight)
            ).apply(instance, BonemealInteractionEntry::new));

    public static MaybeStack getRandom(Holder<Block> block, RandomSource random)
    {
        List<BonemealInteractionEntry> entries = SCDataEntries.BONEMEAL_INTERACTION_ENTRY.get()
                .stream()
                .filter(o -> o.block.matches(block, BuiltInRegistries.BLOCK))
                .filter(o -> o.weight > 0)
                .filter(o -> !o.stack.isEmpty())
                .toList();

        if (entries.isEmpty())
            return MaybeStack.EMPTY;

        int totalWeight = entries.stream()
                .mapToInt(BonemealInteractionEntry::weight)
                .sum();

        int roll = random.nextInt(totalWeight);

        for (BonemealInteractionEntry entry : entries)
        {
            roll -= entry.weight();

            if (roll < 0)
                return entry.stack();
        }

        return MaybeStack.EMPTY;
    }
}
