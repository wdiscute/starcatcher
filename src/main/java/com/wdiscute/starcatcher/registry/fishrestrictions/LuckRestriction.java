package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.FishProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LuckRestriction extends AbstractFishRestriction
{
    private final int luckRequirement;

    public static final MapCodec<LuckRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("requirement").forGetter(o -> o.luckRequirement),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, LuckRestriction::new));

    public LuckRestriction(int luckRequirement, String translationOverride)
    {
        super(translationOverride);
        this.luckRequirement = luckRequirement;
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.LUCK_RESTRICTION;
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        if (context.equals(Context.RADAR)) return 0;
        if (context.equals(Context.GUIDE_FISHES_IN_AREA)) return 0;

        int luck;

        //if fishing
        if (entity instanceof FishingBobEntity fbe)
        {
            if (!fbe.player.getAttributes().hasAttribute(Attributes.LUCK))
                luck = 0;
            else
            {
                AttributeInstance attribute = fbe.player.getAttribute(Attributes.LUCK);
                if (attribute == null)
                    luck = 0;
                else

                    luck = (int) attribute.getValue();
            }
        }
        else
        {
            //if guide
            if (entity instanceof Player player)
            {
                if (!player.getAttributes().hasAttribute(Attributes.LUCK))
                    luck = 0;
                else
                {
                    AttributeInstance attribute = player.getAttribute(Attributes.LUCK);
                    if (attribute == null)
                        luck = 0;
                    else
                        luck = (int) attribute.getValue();
                }
            }
            else
                luck = 0;
        }

        if(luck >= luckRequirement) return 0;

        return -9999;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.luck_restriction.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.luck_restriction.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.luck");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return Component.literal(luckRequirement + "");
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        return List.of();
    }
}
