package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

public class ForceSelectFishModifier extends AbstractCatchModifier
{
    private final float chance;
    private final FishProperties fp;
    public static final MapCodec<ForceSelectFishModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("chance", 1f).forGetter(o -> o.chance),
                    FishProperties.CODEC.fieldOf("fish_properties").forGetter(o -> o.fp),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, ForceSelectFishModifier::new));

    public ForceSelectFishModifier(float chance, FishProperties fp, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
        this.fp = fp;
    }

    @Override
    public Pair<FishProperties, ResourceLocation> forceSelectFishIfNoNonFishAvailable(FishingBobEntity fbe)
    {
        //return if chance doesn't hit
        if(fbe.level().random.nextFloat() > chance) return null;
        return Pair.of(fp, Starcatcher.MISSINGNO);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("force_select_fish");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
