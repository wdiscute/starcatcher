package com.wdiscute.starcatcher.registry.fishrestrictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sun.jna.platform.win32.COM.COMBindingBaseObject;
import com.wdiscute.starcatcher.fish.FishProperties;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StructureRestriction extends AbstractFishRestriction
{
    private final List<ResourceLocation> structures;

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
            return Component.translatable("structure." + structures.getFirst().toLanguageKey());
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
    public int adjustChance(int currentChance, Level level, FishProperties fp, @NotNull Entity entity, ItemStack rod, Context context)
    {
        if (level.isClientSide) return -9999;

        ServerLevel sl = (ServerLevel) level;
        StructureManager manager = sl.structureManager();
        Map<Structure, LongSet> allStructuresAt = manager.getAllStructuresAt(entity.blockPosition());

        for (Map.Entry<Structure, LongSet> entry : allStructuresAt.entrySet())
        {
            ResourceLocation key = BuiltInRegistries.STRUCTURE_TYPE.getKey(entry.getKey().type());

            if (structures.contains(key))
                return 0;
        }

        return -9999;
    }

    public static final StructureRestriction TRIAL_CHAMBERS = new StructureRestriction(List.of(BuiltinStructures.TRIAL_CHAMBERS.location()), "");
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
