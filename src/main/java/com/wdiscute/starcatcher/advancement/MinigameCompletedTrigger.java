package com.wdiscute.starcatcher.advancement;

import com.google.gson.JsonObject;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MinigameCompletedTrigger extends SimpleCriterionTrigger<MinigameCompletedTrigger.Instance> {

    public static final ResourceLocation ID = Starcatcher.rl("minigame_completed");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext) {
        boolean perfect = GsonHelper.getAsBoolean(json, "perfect", false);
        boolean treasure = GsonHelper.getAsBoolean(json, "treasure", false);

        ItemPredicate items = ItemPredicate.fromJson(json.get("item"));
        MinMaxBounds.Ints time = MinMaxBounds.Ints.fromJson(json.get("time"));
        MinMaxBounds.Ints hits = MinMaxBounds.Ints.fromJson(json.get("hits"));
        return new Instance(predicate, perfect, treasure, items, time, hits);
    }

    public void trigger(ServerPlayer player, int hits, boolean perfect, boolean completedTreasure, int time, ItemStack caught) {
        this.trigger(player, p -> p.test(hits, perfect, completedTreasure, time, caught));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final boolean perfect;
        private final boolean completedTreasure;
        private final ItemPredicate caught;
        private final MinMaxBounds.Ints time;
        private final MinMaxBounds.Ints hits;

        public Instance(ContextAwarePredicate player, boolean perfect, boolean completedTreasure, ItemPredicate caught, MinMaxBounds.Ints time, MinMaxBounds.Ints hits) {
            super(ID, player);
            this.perfect = perfect;
            this.completedTreasure = completedTreasure;
            this.caught = caught;
            this.time = time;
            this.hits = hits;
        }

        public boolean test(int hits, boolean perfect, boolean completedTreasure, int time, ItemStack caught) {
            return !this.perfect || perfect &&
                    !this.completedTreasure || completedTreasure &&
                    this.caught.matches(caught) &&
                    this.time.matches(time) &&
                    this.hits.matches(hits);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            JsonObject value = super.serializeToJson(conditions);
            if (perfect) value.addProperty("perfect", true);
            if (completedTreasure) value.addProperty("treasure", true);
            if (this.caught != ItemPredicate.ANY) {
                value.add("item", this.caught.serializeToJson());
            }
            if (this.time != MinMaxBounds.Ints.ANY)
                value.add("time", this.time.serializeToJson());
            if (this.hits != MinMaxBounds.Ints.ANY)
                value.add("hits", this.hits.serializeToJson());
            return value;
        }
    }

    public static class Builder {
        private ContextAwarePredicate player = ContextAwarePredicate.ANY;
        private boolean perfect = false,
                treasure = false;
        private ItemPredicate caught = ItemPredicate.ANY;
        private MinMaxBounds.Ints time = MinMaxBounds.Ints.ANY, hits = MinMaxBounds.Ints.ANY;


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

        public Builder caught(ItemPredicate caught) {
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

        public Instance build() {
            return new Instance(
                    this.player,
                    perfect,
                    treasure,
                    this.caught,
                    this.time,
                    this.hits
            );
        }
    }
}
