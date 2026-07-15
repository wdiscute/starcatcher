package com.wdiscute.starcatcher.modifiers.catchmodifiers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.fish.CatchInfo;
import com.wdiscute.starcatcher.fish.Difficulty;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Optional;

public class VanillaFishingLootModifier extends AbstractCatchModifier
{
    private final float chance;
    public static final MapCodec<VanillaFishingLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("chance", 1f).forGetter(o -> o.chance),
                    Codec.STRING.optionalFieldOf("translation_override", "").forGetter(o -> o.translationOverride)
            ).apply(instance, VanillaFishingLootModifier::new));

    public VanillaFishingLootModifier(float chance, String translationOverride)
    {
        super(translationOverride);
        this.chance = chance;
    }

    @Override
    public Pair<FishProperties, ResourceLocation> forceSelectFishIfNoNonFishAvailable(FishingBobEntity fbe)
    {
        if(fbe.level().random.nextFloat() > chance) return null;

        Level level = fbe.level();
        Player player = fbe.player;

        LootParams lootparams = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, fbe.position())
                .withParameter(LootContextParams.TOOL, fbe.rod)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.ATTACKING_ENTITY, player)
                .withLuck(player.getLuck())
                .create(LootContextParamSets.FISHING);

        LootTable table = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.withDefaultNamespace("gameplay/fishing")));

        Optional<ItemStack> any = table.getRandomItems(lootparams).stream().findAny();

        if(any.isEmpty()) return null;

        FishProperties fp = FishProperties.empty()
                .withCatchInfo(CatchInfo.DEFAULT.withFish(new MaybeStack(any.get())).withFishType(CatchInfo.FishEntryType.EXTRA))
                .withDifficulty(Difficulty.EASY)
                ;

        return Pair.of(fp, Starcatcher.MISSINGNO);
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return Starcatcher.rl("vanilla_fishing_loot");
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return CODEC;
    }
}
