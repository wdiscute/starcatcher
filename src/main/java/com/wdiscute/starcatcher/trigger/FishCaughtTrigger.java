package com.wdiscute.starcatcher.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.Rarity;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class FishCaughtTrigger extends SimpleCriterionTrigger<FishCaughtTrigger.FishCaughtTriggerInstance>
{
    public void trigger(ServerPlayer player, ResourceLocation rl, Rarity rarity, int time, boolean perfectCatch)
    {
        this.trigger(player, instance -> instance.matches(rl, rarity, time, perfectCatch));
    }

    @Override
    public Codec<FishCaughtTriggerInstance> codec()
    {
        return FishCaughtTriggerInstance.CODEC;
    }

    public record FishCaughtTriggerInstance(
            Optional<ContextAwarePredicate> player,
            ResourceLocation rl,
            Rarity rarity,
            int time,
            boolean perfectCatch
    )
            implements SimpleCriterionTrigger.SimpleInstance
    {

        public boolean matches(ResourceLocation rl, Rarity rarity, int time, boolean perfectCatch)
        {
            //if advancement requires perfect catch, and it wasn't, return false
            if(this.perfectCatch && !perfectCatch) return false;

            //if time took is more than the max_time from advancement, return false
            if(time >= this.time) return false;

            //if adv requires a specific fp rl, and adv rl doesn't match fished rl, return false
            if(!this.rl.equals(Starcatcher.MISSINGNO) && !this.rl.equals(rl)) return false;

            //if adv requires a rarity, and adv rarity doesn't match fished rarity
            if(!this.rarity.equals(Rarity.NONE) && !this.rarity.equals(rarity)) return false;

            return true;
        }

        public static final Codec<FishCaughtTriggerInstance> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(FishCaughtTriggerInstance::player),
                        ResourceLocation.CODEC.optionalFieldOf("fish", Starcatcher.MISSINGNO).forGetter(FishCaughtTriggerInstance::rl),
                        Rarity.CODEC.optionalFieldOf("rarity", Rarity.NONE).forGetter(FishCaughtTriggerInstance::rarity),
                        Codec.INT.optionalFieldOf("max_time", 999999).forGetter(FishCaughtTriggerInstance::time),
                        Codec.BOOL.optionalFieldOf("require_perfect_catch", false).forGetter(FishCaughtTriggerInstance::perfectCatch)
                ).apply(instance, FishCaughtTriggerInstance::new));
    }
}
