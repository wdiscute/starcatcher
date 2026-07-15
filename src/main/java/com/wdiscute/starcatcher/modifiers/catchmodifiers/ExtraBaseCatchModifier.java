package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.fish.FishApi;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.Utils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExtraBaseCatchModifier extends AbstractCatchModifier
{
    final int count;
    final boolean onlyForPerfectCatch;

    public static final MapCodec<ExtraBaseCatchModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("count").forGetter(o -> o.count),
                    Codec.BOOL.fieldOf("only_for_perfect_catch").forGetter(o -> o.onlyForPerfectCatch),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, ExtraBaseCatchModifier::new));

    public ExtraBaseCatchModifier(int count, boolean perfect, String translationOverride)
    {
        super(translationOverride);
        this.count = count;
        this.onlyForPerfectCatch = perfect;
    }

    @Override
    public List<Component> getNonOverriddenDescription(boolean shift)
    {
        return List.of(Component.translatable("tooltip.modifier.starcatcher.extra_base_catch", I18n.get("tooltip.modifier.number." + count)));
    }

    @Override
    public List<ItemStack> addToFishedItems(FishingBobEntity fbe, FishProperties fp, int time, boolean perfectCatch, int hits, boolean completedTreasure)
    {
        //return if non-fish aka trophy/secret/extra
        if (!fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH)) return List.of();

        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < count; i++)
        {
            float percentile = fbe.level().random.nextFloat() * 100;

            ItemStack itemStack = FishApi.makeItemStackNonBucket(fp, percentile, false, fbe.player, perfectCatch);

            list.add(itemStack);
        }

        return list;
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("extra_base_catch");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
