package com.wdiscute.starcatcher.registry.catchmodifiers;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class VanillaLootModifier extends AbstractCatchModifier
{
    @Override
    public void afterChoosingTheCatch(List<FishProperties> immutableAvailable)
    {
        this.instance.fpToFish = FishProperties.VANILLA_FISH;
        this.instance.rlToFish = Starcatcher.rl("missingno");
    }

    @Override
    public void modifyBaseItemStack(ItemStack is)
    {
        is.shrink(100);
    }

    @Override
    public List<ItemStack> addToFishedItems(int time, boolean perfectCatch, int hits, boolean completedTreasure, Player player)
    {
        Level level = instance.level();

        LootParams lootparams = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, instance.position())
                .withParameter(LootContextParams.TOOL, instance.rod)
                .withParameter(LootContextParams.THIS_ENTITY, instance)
                .withParameter(LootContextParams.ATTACKING_ENTITY, instance.getOwner())
                .withLuck(player.getLuck())
                .create(LootContextParamSets.FISHING);

        LootTable table = level.getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);

        return table.getRandomItems(lootparams);
    }
}
