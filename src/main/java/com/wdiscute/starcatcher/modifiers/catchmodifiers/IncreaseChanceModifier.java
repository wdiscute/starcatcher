package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLLoader;

import java.util.ArrayList;
import java.util.List;

public class IncreaseChanceModifier extends AbstractCatchModifier
{
    final ResourceLocation rl;
    final int extraChance;

    public static final MapCodec<IncreaseChanceModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("fish").forGetter(o -> o.rl),
                    Codec.INT.fieldOf("extra_chance").forGetter(o -> o.extraChance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, IncreaseChanceModifier::new));

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("increase_chance");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    public IncreaseChanceModifier(ResourceLocation fp, int extraChance, String translationOverride)
    {
        super(translationOverride);
        this.rl = fp;
        this.extraChance = extraChance;
    }

    @Override
    public List<FishProperties> modifyAvailablePool(FishingBobEntity fbe, List<FishProperties> available)
    {
        FishProperties fishProperties = fbe.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).get(rl);
        if(fishProperties != null)
        {
            if(available.contains(fishProperties))
            {
                ArrayList<FishProperties> newList = new ArrayList<>(available);
                for (int i = 0; i < extraChance; i++)
                    newList.add(fishProperties);
                return newList;
            }
        }
        return super.modifyAvailablePool(fbe, available);
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        if(!FMLLoader.getDist().isClient())
            return List.of();

        Level level = Starcatcher.Client.getLevel();

        if(level == null)
            return List.of();

        Component name = level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).get(rl).getDisplayName();

        if (shift)
            return List.of(Component.translatable("tooltip.modifier.starcatcher.increase_chance.shift", name, extraChance));
        else
            return List.of(Component.translatable("tooltip.modifier.starcatcher.increase_chance", name));
    }
}
