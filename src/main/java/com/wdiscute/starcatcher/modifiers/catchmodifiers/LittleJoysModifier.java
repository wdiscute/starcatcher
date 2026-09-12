package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.compat.LittleJoysCompat;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.Utils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.blay09.mods.littlejoys.api.LittleJoysAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class LittleJoysModifier extends AbstractCatchModifier
{
    public static final MapCodec<LittleJoysModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, LittleJoysModifier::new));

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("little_joys");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }

    public LittleJoysModifier(String translationOverride)
    {
        super(translationOverride);
    }

    @Override
    public List<ItemStack> addToFishedItems(FishingBobEntity fbe, FishProperties fp, int time, boolean perfectCatch, int hits, boolean completedTreasure)
    {
        if (fbe.level() instanceof ServerLevel sl &&
            fbe.player instanceof Player player &&
            LittleJoysCompat.isLoaded() &&
            LittleJoysCompat.isOnFishingSpot(fbe))
        {
            LootTable lootTable = sl.getServer().reloadableRegistries()
                    .getLootTable(ResourceKey.create(Registries.LOOT_TABLE, Utils.rl("littlejoys", "fishing_spot/water")));

            LootParams lootparams = new LootParams.Builder(sl)
                    .withParameter(LootContextParams.ORIGIN, fbe.position())
                    .withParameter(LootContextParams.TOOL, fbe.rod)
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withParameter(LootContextParams.ATTACKING_ENTITY, player)
                    .withLuck(player.getLuck())
                    .create(LootContextParamSets.FISHING);

            LittleJoysAPI.findFishingSpot(sl, fbe.blockPosition()).ifPresent(o -> LittleJoysAPI.consumeFishingSpot(player, sl, o));
            return lootTable.getRandomItems(lootparams);
        }
        return List.of();
    }

    @Override
    public void tick(FishingBobEntity fbe)
    {
        if (LittleJoysCompat.isLoaded())
            if (LittleJoysCompat.isOnFishingSpot(fbe))
                fbe.maxTicksToFish = 0;
        super.tick(fbe);
    }
}
