package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.messageinabottle.letter.BottledLetterEntity;
import com.wdiscute.starcatcher.messageinabottle.BrokenBottleEntity;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public interface SCEntities
{
    DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Starcatcher.MOD_ID);

    DeferredHolder<EntityType<?>, EntityType<FishingBobEntity>> FISHING_BOB =
            register("fishing_bob", FishingBobEntity::new, MobCategory.MISC,
                    b -> b.noSummon().noSave().sized(0.3f, 0.3f));

    DeferredHolder<EntityType<?>, EntityType<FishEntity>> FISH =
            register("fish", FishEntity::new, MobCategory.WATER_AMBIENT,
                    b -> b.sized(0.5f, 0.5f).fireImmune());

    DeferredHolder<EntityType<?>,EntityType<BrokenBottleEntity>> BROKEN_BOTTLE =
            register("broken_bottle", BrokenBottleEntity::new, MobCategory.MISC,
                    b -> b.sized(0.25f, 0.25f)
                            .clientTrackingRange(4).updateInterval(10));

    DeferredHolder<EntityType<?>,EntityType<BottledLetterEntity>> BOTTLED_LETTER =
            register("bottled_letter", BottledLetterEntity::new, MobCategory.MISC,
                    b -> b.sized(0.25f, 0.25f)
                            .clientTrackingRange(4).updateInterval(10));

    static void register(IEventBus eventBus)
    {
        ENTITY_TYPES.register(eventBus);
    }

    static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> provider) {
        return ENTITY_TYPES.register(name, () -> provider.apply(EntityType.Builder.of(factory, category)).build(name));
    }

}
