package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.fish.FishProperties;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.nikdo53.neobackports.registry.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StructureRestriction extends AbstractFishRestriction
{
    private final List<ResourceLocation> structures;

    public static List<ResourceLocation> playerInStructures = List.of();

    public static final MapCodec<StructureRestriction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.listOf().fieldOf("structure").forGetter(o -> o.structures),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, StructureRestriction::new));

    public StructureRestriction(List<ResourceLocation> rl, String translationOverride)
    {
        super(translationOverride);
        this.structures = rl;
    }

    @Override
    public MutableComponent getDescriptionPrefix()
    {
        return Component.translatable("gui.guide.structure");
    }

    @Override
    public MutableComponent getNonOverriddenDescription(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (structures.isEmpty()) return Component.empty();

        if (structures.size() == 1)
            return Component.translatable("structure." + structures.get(0).toLanguageKey());
        else
            return Component.translatable("gui.guide.hover");
    }

    @Override
    public List<Component> getHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (structures.size() > 1)
        {
            List<Component> list = new ArrayList<>();
            structures.forEach(o -> list.add(Component.translatable("structure." + o.toLanguageKey())));
            return list;
        }
        else
            return List.of();
    }

    @Override
    public MapCodec<? extends AbstractFishRestriction> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractFishRestriction, AbstractFishRestriction> getRegistryHolder()
    {
        return SCFishRestrictions.STRUCTURE_RESTRICTION;
    }

    @Override
    public List<Component> getIndexHover(Level level, FishProperties fp, @NotNull Player player, Context context)
    {
        if (adjustChance(0, level, fp, player, ItemStack.EMPTY, Context.GUIDE_FISHES_HOVER) >= 0)
            return List.of(Component.translatable("gui.guide.hover.structure.correct").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        else
            return List.of(Component.translatable("gui.guide.hover.structure.incorrect").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
    }

    @Override
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        //if client side use client-only structure cache
        if (level.isClientSide)
            return playerInStructures.stream().anyMatch(structures::contains) ? 0 : -9999;

        ServerLevel serverLevel = (ServerLevel) level;
        StructureManager structureManager = serverLevel.structureManager();

        var structureRegistry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);

        //for every structure allowed
        for (ResourceLocation structureId : structures)
        {
            //get structure from rl
            var structure = structureRegistry.get(ResourceKey.create(Registries.STRUCTURE, structureId));

            //if structure exists
            if (structure.isPresent())
                //if structure is at blockpos
                if (structureManager.getStructureWithPieceAt(entity.blockPosition(), structure.get().value()).isValid())
                    return 0;
        }

        return -9999;
    }

    //public static final StructureRestriction TRIAL_CHAMBERS = new StructureRestriction(List.of(BuiltinStructures.TRIAL_CHAMBERS.location()), "");
    public static final StructureRestriction OCEAN_MONUMENT = new StructureRestriction(List.of(BuiltinStructures.OCEAN_MONUMENT.location()), "");
    public static final StructureRestriction END_CITIES = new StructureRestriction(List.of(BuiltinStructures.END_CITY.location()), "");
    public static final StructureRestriction VILLAGES = new StructureRestriction(List.of(
            BuiltinStructures.VILLAGE_TAIGA.location(),
            BuiltinStructures.VILLAGE_SNOWY.location(),
            BuiltinStructures.VILLAGE_SAVANNA.location(),
            BuiltinStructures.VILLAGE_PLAINS.location(),
            BuiltinStructures.VILLAGE_DESERT.location()
    ), "");
}
