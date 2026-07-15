package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishApi;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.fish.FishProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CaughtLimitRestriction extends AbstractFishRestriction
{
    private final int limit;

    public static final MapCodec<CaughtLimitRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("limit").forGetter(CaughtLimitRestriction::getLimit),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, CaughtLimitRestriction::new));

    public CaughtLimitRestriction()
    {
        super("");
        this.limit = Integer.MAX_VALUE;
    }

    public CaughtLimitRestriction(int limit, String translationOverride)
    {
        super(translationOverride);
        this.limit = limit;
    }

    public int getLimit()
    {
        return limit;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.MAX_CATCH_LIMIT;
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        if (entity instanceof FishingBobEntity fbe)
        {
            if (getCaughtCounter(fp, fbe.player) >= limit) return -9999;
        }
        else if (getCaughtCounter(fp, entity) >= limit) return -9999;
        return 0;
    }

    private int getCaughtCounter(FishProperties fp, Entity entity)
    {
        ResourceLocation fpKey = FishApi.getKey(entity.level(), fp);
        if (fpKey == null) return 0;
        FishingGuideAttachment fishingGuideAttachment = SCDataAttachments.get(entity, SCDataAttachments.FISHING_GUIDE);
        FishCaughtCounter fcc = fishingGuideAttachment.fishesCaught.get(fpKey);

        if (fcc == null) return 0;
        return limit;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (limit == 1)
            return List.of();

        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.caught_limit", getCaughtCounter(fp, player) + " / " + limit)
                    .withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.caught_limit", getCaughtCounter(fp, player) + " / " + limit)
                    .withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        if (limit == 1)
            return Component.empty();
        else
            return Component.translatable("gui.guide.caught_limit");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        int caughtCounter = getCaughtCounter(fp, player);

        if (limit == 1)
        {
            if (caughtCounter == 1)
                return Component.translatable("gui.guide.caught_limit.caught");
            else
                return Component.translatable("gui.guide.caught_limit.not_caught");
        }

        return Component.literal(caughtCounter + " / " + limit);
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> hover = new ArrayList<>();

        return hover;
    }
}
