package com.wdiscute.starcatcher.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MinigameCompletedTrigger extends SimpleCriterionTrigger<MinigameCompletedTrigger.Instance> {

    @Override
    public @NotNull Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player, int hits, boolean perfect, boolean completedTreasure, int time, Holder<Item> caught) {
        this.trigger(player, p -> p.test(hits, perfect, completedTreasure, time, caught));
    }

    public Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class Instance implements SimpleInstance {
        private static final Codec<Instance> CODEC = RecordCodecBuilder.create(i -> i.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(in -> in.player),
                Codec.BOOL.optionalFieldOf("perfect", false).forGetter(in -> in.perfect),
                Codec.BOOL.optionalFieldOf("treasure", false).forGetter(in -> in.completedTreasure),
                RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("caught").forGetter(in -> in.caught),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("time").forGetter(in -> in.time),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("hits").forGetter(in -> in.hits)
        ).apply(i, Instance::new));

        private final Optional<ContextAwarePredicate> player;
        private final boolean perfect;
        private final boolean completedTreasure;
        private final Optional<HolderSet<Item>> caught;
        private final Optional<MinMaxBounds.Ints> time;
        private final Optional<MinMaxBounds.Ints> hits;

        public Instance(Optional<ContextAwarePredicate> player, boolean perfect, boolean completedTreasure, Optional<HolderSet<Item>> caught, Optional<MinMaxBounds.Ints> time, Optional<MinMaxBounds.Ints> hits) {
            this.player = player;
            this.perfect = perfect;
            this.completedTreasure = completedTreasure;
            this.caught = caught;
            this.time = time;
            this.hits = hits;
        }

        @Override
        public @NotNull Optional<ContextAwarePredicate> player() {
            return player;
        }

        public boolean test(int hits, boolean perfect, boolean completedTreasure, int time, Holder<Item> caught) {
            return !this.perfect || perfect &&
                    !this.completedTreasure || completedTreasure &&
                    this.caught.map(s -> s.contains(caught)).orElse(true) &&
                    this.time.map(i -> i.matches(time)).orElse(true) &&
                    this.hits.map(i -> i.matches(hits)).orElse(true);
        }
    }

    public class Builder {
        private ContextAwarePredicate player;
        private boolean perfect = false,
        treasure = false;
        private HolderSet<Item> caught;
        private MinMaxBounds.Ints time, hits;


        public Builder setPlayer(ContextAwarePredicate player) {
            this.player = player;
            return this;
        }

        public Builder perfect() {
            this.perfect = true;
            return this;
        }

        public Builder treasure() {
            this.treasure = true;
            return this;
        }

        public Builder caught(HolderSet<Item> caught) {
            this.caught = caught;
            return this;
        }

        public Builder time(MinMaxBounds.Ints time) {
            this.time = time;
            return this;
        }

        public Builder hits(MinMaxBounds.Ints hits) {
            this.hits = hits;
            return this;
        }

        public Criterion<Instance> build() {
            return MinigameCompletedTrigger.this.createCriterion(new Instance(
                    Optional.ofNullable(this.player),
                    perfect,
                    treasure,
                    Optional.ofNullable(this.caught),
                    Optional.ofNullable(this.time),
                    Optional.ofNullable(this.hits)
            ));
        }
    }
}
