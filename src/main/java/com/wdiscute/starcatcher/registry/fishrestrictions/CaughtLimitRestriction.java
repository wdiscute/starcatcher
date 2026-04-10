package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobberentity.FishingBobEntity;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.network.chat.Component;
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
    private final String translationOverride;

    public static final MapCodec<CaughtLimitRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("limit").forGetter(CaughtLimitRestriction::getLimit),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(CaughtLimitRestriction::getTranslationOverride)
            ).apply(instance, CaughtLimitRestriction::new));

    public CaughtLimitRestriction()
    {
        this.limit = Integer.MAX_VALUE;
        this.translationOverride = "";
    }

    public CaughtLimitRestriction(int limit, String translationOverride)
    {
        this.limit = limit;
        this.translationOverride = translationOverride;
    }

    public int getLimit()
    {
        return limit;
    }

    public String getTranslationOverride()
    {
        return translationOverride;
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
    public int getFishChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
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
        ResourceLocation fpkey = FishProperties.getKey(entity.level(), fp);
        if (fpkey == null) return 0;
        FishingGuideAttachment fishingGuideAttachment = SCDataAttachments.get(entity, SCDataAttachments.FISHING_GUIDE);
        FishCaughtCounter fcc = fishingGuideAttachment.fishesCaught.get(fpkey);

        if (fcc == null) return 0;
        return limit;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (getFishChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.caught_limit", getCaughtCounter(fp, player) + " / " + limit)
                    .withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.caught_limit", getCaughtCounter(fp, player) + " / " + limit)
                    .withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        int color = getFishChance(0, level, fp, player, ItemStack.EMPTY, context) >= 0 ? SCColors.GUIDE_GREEN : SCColors.GUIDE_RED;

        return Component.translatable("gui.guide.caught_limit").copy().append(
                translationOverride.isEmpty() ?
                        Component.literal(getCaughtCounter(fp, player) + " / " + limit).withStyle(Style.EMPTY.withColor(color)) :
                        Component.translatable(translationOverride).withStyle(Style.EMPTY.withColor(color))
        );
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        List<Component> hover = new ArrayList<>();

        return hover;
    }
}
