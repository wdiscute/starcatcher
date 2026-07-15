package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface SCAttributes
{
    DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, Starcatcher.MOD_ID);

    Holder<Attribute> HANDLE_ROTATION_SPEED_MULTIPLIER = register("handle_rotation_speed_multiplier", 1, 0, 10);
    Holder<Attribute> PENALTY_MULTIPLIER = registerNegative("penalty_multiplier", 1, 0, 10);
    Holder<Attribute> BASE_DECAY_MULTIPLIER = registerNegative("base_decay_multiplier", 1, 0, 10);
    Holder<Attribute> REQUIRED_SCORE_MULTIPLIER = register("required_score_multiplier", 1, 0, 10);
    Holder<Attribute> VANISHING_RATE_MULTIPLIER = register("vanishing_rate_multiplier", 1, 0, 10);

    private static Holder<Attribute> register(String name, double defaultVal, double min, double max)
    {
        return REGISTRY.register(name, () -> new RangedAttribute(name, defaultVal, min, max).setSyncable(true));
    }

    private static Holder<Attribute> registerNegative(String name, double defaultVal, double min, double max)
    {
        return REGISTRY.register(name, () -> new RangedAttribute(name, defaultVal, min, max).setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE));
    }

    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
