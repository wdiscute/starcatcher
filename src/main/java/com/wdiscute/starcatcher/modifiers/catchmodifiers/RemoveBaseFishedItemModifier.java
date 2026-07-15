package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RemoveBaseFishedItemModifier extends AbstractCatchModifier
{
    public static final MapCodec<RemoveBaseFishedItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, RemoveBaseFishedItemModifier::new));

    public RemoveBaseFishedItemModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public boolean shouldSkipAddingBaseItem(FishingBobEntity fbe, ItemStack is)
    {
        return true;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("remove_base_fished_item");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
