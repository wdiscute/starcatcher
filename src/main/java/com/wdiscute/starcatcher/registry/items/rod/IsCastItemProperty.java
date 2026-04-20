package com.wdiscute.starcatcher.registry.items.rod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record IsCastItemProperty(boolean isCast) implements RangeSelectItemModelProperty
{
    public static final MapCodec<IsCastItemProperty> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.BOOL.optionalFieldOf("cast", false).forGetter(IsCastItemProperty::isCast)
                    )
                    .apply(instance, IsCastItemProperty::new)
    );

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed)
    {
        if (owner instanceof Player player)
        {
            if (SCDataComponents.getOrDefault(stack, SCDataComponents.BOBBER, SingleStackContainer.empty()).isEmpty())
                return 1f;
            if (SCDataComponents.getOrDefault(stack, SCDataComponents.HOOK, SingleStackContainer.empty()).isEmpty())
                return 1f;
            return !SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB).isEmpty() &&
                    (player.getMainHandItem() == stack || (player.getOffhandItem() == stack)) ? 1.0f : 0.0f;
        }
        return 0f;
    }

    @Override
    public MapCodec<IsCastItemProperty> type()
    {
        return MAP_CODEC;
    }
}
