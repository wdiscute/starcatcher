package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.Starcatcher;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class WaterloggedSatchel extends Item
{
    public WaterloggedSatchel()
    {
        super(new Properties().stacksTo(1).fireResistant());
    }
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand)
    {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        ResourceKey<LootTable> lootTable = ResourceKey.create(Registries.LOOT_TABLE, Starcatcher.rl("waterlogged_satchel/waterlogged_satchel"));
        LootParams params = new LootParams.Builder((ServerLevel) level).create(LootContextParamSets.EMPTY);
        ObjectArrayList<ItemStack> arrayOfItemStacks = level.getServer().reloadableRegistries().getLootTable(lootTable).getRandomItems(params);
        player.setItemInHand(usedHand, arrayOfItemStacks.get(level.getRandom().nextIntBetweenInclusive(0, arrayOfItemStacks.size() - 1)));
        return InteractionResult.SUCCESS;
    }
}
