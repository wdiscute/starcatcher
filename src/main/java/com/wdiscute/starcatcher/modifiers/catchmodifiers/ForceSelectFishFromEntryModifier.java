package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class ForceSelectFishFromEntryModifier extends AbstractCatchModifier
{
    private final float chance;
    private final Identifier rl;
    public static final MapCodec<ForceSelectFishFromEntryModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("chance", 1f).forGetter(o -> o.chance),
                    Identifier.CODEC.fieldOf("entry").forGetter(o -> o.rl),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, ForceSelectFishFromEntryModifier::new));

    public ForceSelectFishFromEntryModifier(float chance, Identifier rl, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
        this.rl = rl;
    }

    @Override
    public Pair<FishProperties, Identifier> forceSelectFishIfNoNonFishAvailable(FishingBobEntity fbe)
    {
        //return if chance doesn't hit
        if(fbe.level().getRandom().nextFloat() > chance) return null;

        Optional<FishProperties> optionalFP = fbe.level().registryAccess().lookupOrThrow(Starcatcher.FISH_REGISTRY_KEY).getOptional(rl);

        if(optionalFP.isEmpty()) return null;

        return Pair.of(optionalFP.get(), rl);
    }

    @Override
    public Identifier getIdentifier()
    {
        return Starcatcher.rl("force_select_fish_from_entry");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
