package com.wdiscute.starcatcher.sellingbin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class FishProcessor extends AbstractProcessor
{

    public static final MapCodec<FishProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("percentile_increase").forGetter(FishProcessor::getPercentileMultiplier),
                    Codec.FLOAT.fieldOf("golden_multiplier").forGetter(FishProcessor::getGoldenMultiplier)
            ).apply(instance, FishProcessor::new));

    private final float percentileMultiplier, goldenMultiplier;

    public FishProcessor()
    {
        this(0, 0);
    }

    public FishProcessor(float percentileMultiplier, float goldenMultiplier)
    {
        this.percentileMultiplier = percentileMultiplier;
        this.goldenMultiplier = goldenMultiplier;
    }

    public float getPercentileMultiplier()
    {
        return percentileMultiplier;
    }

    public float getGoldenMultiplier()
    {
        return goldenMultiplier;
    }

    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolder()
    {
        return SCProcessors.FISHES_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Component> getDescription()
    {
        return List.of(
                Component.translatableEscape("gui.starcatcher.processor.fishes_processor.percentile", ((int) (percentileMultiplier * 100))),
                Component.translatableEscape("gui.starcatcher.processor.fishes_processor.golden", goldenMultiplier)
        );
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, Player player)
    {
        if (!SCDataComponents.has(itemStack, SCDataComponents.CAUGHT_FISH_INFO)) return 0;

        CaughtFishInfo caughtFishInfo = SCDataComponents.get(itemStack, SCDataComponents.CAUGHT_FISH_INFO);

        if (caughtFishInfo.golden()) return (int) (baseValue * goldenMultiplier);

        if (caughtFishInfo.percentile() >= 50) return 0;

        //wd sucks at math
        float percentile = (100 - caughtFishInfo.percentile() - 50) / 50;

        float increase = percentile * (percentileMultiplier - 1) * baseValue;

        return (int) increase;
    }
}
