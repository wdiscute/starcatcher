package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.bob.FishingBobEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Starcatcher.MOD_ID);

    public static final Supplier<EntityType<FishingBobEntity>> FISHING_BOB =
            register("fishing_bob", FishingBobEntity::new, MobCategory.MISC,
                    b -> b.noSummon().noSave().sized(0.3f, 0.3f));

    public static final Supplier<EntityType<FishEntity>> FISH =
            register("fish", FishEntity::new, MobCategory.WATER_AMBIENT,
                    b -> b.sized(0.5f, 0.5f));

    public static void register(IEventBus eventBus)
    {
        ENTITY_TYPES.register(eventBus);
    }

    static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> provider) {
        return ENTITY_TYPES.register(name, () -> provider.apply(EntityType.Builder.of(factory, category)).build(name));
    }

}
