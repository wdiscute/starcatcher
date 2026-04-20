package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BiomeBias extends AbstractFishRestriction
{
    private final List<Identifier> biomes;
    private final List<Identifier> biomesTags;
    private final int extraChance;
    private final String translationOverride;

    public static final MapCodec<BiomeBias> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Identifier.CODEC.listOf().fieldOf("biomes").forGetter(BiomeBias::getBiomes),
                    Identifier.CODEC.listOf().fieldOf("biomes_tags").forGetter(BiomeBias::getBiomesTags),
                    Codec.INT.fieldOf("extra_chance").forGetter(BiomeBias::getExtraChance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(BiomeBias::getTranslationOverride)
            ).apply(instance, BiomeBias::new));

    public BiomeBias()
    {
        this.biomes = List.of();
        this.biomesTags = List.of();
        this.extraChance = 0;
        this.translationOverride = "";
    }

    public BiomeBias(List<Identifier> biomes, List<Identifier> biomesTags, int extraChance, String translationOverride)
    {
        this.biomes = biomes;
        this.biomesTags = biomesTags;
        this.extraChance = extraChance;
        this.translationOverride = translationOverride;
    }

    public List<Identifier> getBiomes()
    {
        return biomes;
    }

    public List<Identifier> getBiomesTags()
    {
        return biomesTags;
    }

    public int getExtraChance()
    {
        return extraChance;
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
        return SCFishRestrictions.BIOME;
    }

    @Override
    public int getFishChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        Holder<Biome> biome = level.getBiome(entity.blockPosition());

        if (biomes.contains(biome.getKey().identifier())) return extraChance;

        if (biomesTags.stream().anyMatch(rl -> biome.is(TagKey.create(Registries.BIOME, rl))))
            return extraChance;

        return 0;
    }

    @Override
    public Component getDescription(Level level, FishProperties fp, @Nullable Player player, Context context)
    {
        //todo finish this :)
        MutableComponent comp = Component.literal("todo");

        Component start = Component.translatable("gui.guide.biome");

        if (!translationOverride.isEmpty())
            comp = Component.translatable(translationOverride);

        return start.copy().append(comp);
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        //todo finish this :)
        return List.of();
    }

    @Override
    public List<Component> getBlacklist(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        //todo finish this :)
        return List.of();
    }
}
