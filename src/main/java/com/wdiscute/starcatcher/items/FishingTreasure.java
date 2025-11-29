package com.wdiscute.starcatcher.items;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class FishingTreasure extends Item
{
    private final ResourceLocation lootTable;

    public FishingTreasure(ResourceLocation rl)
    {
        super(new Properties().stacksTo(1));
        this.lootTable = rl;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if (level.isClientSide) return InteractionResultHolder.success(player.getItemInHand(usedHand));

        ResourceKey<LootTable> lootTable = ResourceKey.create(Registries.LOOT_TABLE, this.lootTable);
        LootParams params = new LootParams.Builder((ServerLevel) level).create(LootContextParamSets.EMPTY);
        ObjectArrayList<ItemStack> arrayOfItemStacks = level.getServer().reloadableRegistries().getLootTable(lootTable).getRandomItems(params);
        player.setItemInHand(usedHand, arrayOfItemStacks.get(level.random.nextIntBetweenInclusive(0, arrayOfItemStacks.size() - 1)));
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
