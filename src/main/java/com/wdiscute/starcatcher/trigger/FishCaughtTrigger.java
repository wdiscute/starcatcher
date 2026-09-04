package com.wdiscute.starcatcher.trigger;

import com.google.gson.JsonObject;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Rarity;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

public class FishCaughtTrigger extends SimpleCriterionTrigger<FishCaughtTrigger.Instance>
{
    public static final ResourceLocation ID = Starcatcher.rl("minigame_completed");

    @Override
    public @NotNull ResourceLocation getId()
    {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext)
    {
        boolean perfect = GsonHelper.getAsBoolean(json, "perfect", false);
        int time = GsonHelper.getAsInt(json, "time", 0);

        String rarityString = GsonHelper.getAsString(json, "rarity", "common");
        Rarity rarity = Rarity.COMMON;
        for (Rarity value : Rarity.values())
        {
            if(rarityString.equals(value.getSerializedName()))
                rarity = value;
        }

        ResourceLocation rl = ResourceLocation.parse(GsonHelper.getAsString(json, "rl", "starcatcher:missingno"));

        return new Instance(predicate, rl, rarity, time, perfect);
    }

    public void trigger(ServerPlayer player, ResourceLocation rl, Rarity rarity, int time, boolean perfectCatch)
    {
        this.trigger(player, instance -> instance.matches(rl, rarity, time, perfectCatch));
    }

    public static class Instance extends AbstractCriterionTriggerInstance
    {
        ResourceLocation rl;
        Rarity rarity;
        int time;
        boolean perfectCatch;

        @Override
        public JsonObject serializeToJson(SerializationContext conditions)
        {
            JsonObject json = super.serializeToJson(conditions);

            json.addProperty("rl", rl.toString());
            json.addProperty("perfect", perfectCatch);
            json.addProperty("rarity", rarity.toString());
            json.addProperty("rarity", time);

            return json;
        }

        public Instance(ContextAwarePredicate player,
                        ResourceLocation rl,
                        Rarity rarity,
                        int time,
                        boolean perfectCatch
        )
        {
            super(ID, player);

            this.rl = rl;
            this.rarity = rarity;
            this.time = time;
            this.perfectCatch = perfectCatch;
        }

        public boolean matches(ResourceLocation rl, Rarity rarity, int time, boolean perfectCatch)
        {
            //if advancement requires perfect catch, and it wasn't, return false
            if (this.perfectCatch && !perfectCatch) return false;

            //if time took is more than the max_time from advancement, return false
            if (time >= this.time) return false;

            //if adv requires a specific fp rl, and adv rl doesn't match fished rl, return false
            if (!this.rl.equals(Starcatcher.MISSINGNO) && !this.rl.equals(rl)) return false;

            //if adv requires a rarity, and adv rarity doesn't match fished rarity
            if (!this.rarity.equals(Rarity.NONE) && !this.rarity.equals(rarity)) return false;

            return true;
        }
    }
}
