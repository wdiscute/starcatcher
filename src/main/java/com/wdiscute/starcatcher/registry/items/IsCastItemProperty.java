package com.wdiscute.starcatcher.registry.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record IsCastItemProperty(boolean isPrisma) implements ConditionalItemModelProperty
{
    public static final MapCodec<IsCastItemProperty> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.BOOL.optionalFieldOf("is_prisma", false).forGetter(IsCastItemProperty::isPrisma)
                    )
                    .apply(instance, IsCastItemProperty::new)
    );

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext)
    {
        if (owner == null) return false;
        if (SCDataComponents.getOrDefault(stack, SCDataComponents.BOBBER, MaybeStack.EMPTY).isEmpty())
            return true;
        if (SCDataComponents.getOrDefault(stack, SCDataComponents.HOOK, MaybeStack.EMPTY).isEmpty())
            return true;
        return !SCDataAttachments.get(owner, SCDataAttachments.FISHING_BOB).isEmpty() && (owner.getMainHandItem() == stack || (owner.getOffhandItem() == stack));
    }

    @Override
    public MapCodec<IsCastItemProperty> type()
    {
        return MAP_CODEC;
    }
}

