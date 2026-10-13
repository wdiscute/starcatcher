package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class RestrictedRaritiesModifier extends AbstractCatchModifier
{
    final List<Rarity> rarities;

    public static final MapCodec<RestrictedRaritiesModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Rarity.CODEC.listOf().fieldOf("rarities").forGetter(o -> o.rarities),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, RestrictedRaritiesModifier::new));

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("restricted_rarities");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    public RestrictedRaritiesModifier(List<Rarity> rarities, String translationOverride)
    {
        super(translationOverride);
        this.rarities = rarities;
    }

    @Override
    public List<FishProperties> modifyAvailablePool(FishingBobEntity fbe, List<FishProperties> available)
    {
        List<FishProperties> list = new ArrayList<>(available);

        return list.stream().filter(o -> !rarities.contains(o.rarity())).toList();
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        Component text;

        if (rarities.size() == 1)
        {
            text = Component.translatable(rarities.get(0).getSerializedName());
        }
        else
        {
            MutableComponent list = Component.empty();

            for (int i = 0; i < rarities.size(); i++)
            {
                if (i > 0)
                {
                    list.append(Component.translatable(
                            i == rarities.size() - 1
                                    ? "tooltip.modifier.starcatcher.allowed_rarities.list_last_separator"
                                    : "tooltip.modifier.starcatcher.allowed_rarities.list_separator"
                    ));
                }

                list.append(Component.translatable(rarities.get(i).getSerializedName()));
            }
            text = list;
        }

        return List.of(Component.translatable("tooltip.modifier.starcatcher.restricted_rarities", text));
    }
}
