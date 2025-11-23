package com.wdiscute.starcatcher.items;

import com.mojang.logging.LogUtils;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.StarcatcherTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.slf4j.Logger;

import java.util.List;

public class FishingTreasure extends Item
{
    private final ResourceLocation lootTable;

    private static final Logger LOGGER = LogUtils.getLogger();

    public FishingTreasure(ResourceLocation rl)
    {
        super(new Properties().stacksTo(1));
        this.lootTable = rl;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if (level.isClientSide) return InteractionResultHolder.success(player.getItemInHand(usedHand));

//        List<Holder<Item>> curatedTreasureInTag =
//                BuiltInRegistries.ITEM.getTag(StarcatcherTags.CURATED_TREASURE)
//                        .map(holders -> holders.stream().toList())
//                        .orElse(List.of());
//
//        if (!curatedTreasureInTag.isEmpty())
//        {
//            List<Item> curatedTreasureNotAlreadyCaught = new ArrayList<>();
//            List<ResourceLocation> treasuresCaught = new ArrayList<>(player.getData(ModDataAttachments.TREASURES_CAUGHT));
//            //List<ResourceLocation> treasuresCaught = new ArrayList<>(player.getData(ModDataAttachments.TREASURES_CAUGHT));
//
//            //add all curated treasure not already caught to curatedTreasureNotAlreadyCaught var
//            for (Holder<Item> holder : curatedTreasureInTag)
//                if (!treasuresCaught.contains(holder.getKey().location()))
//                    curatedTreasureNotAlreadyCaught.add(holder.value());
//
//            //if there is curated loot left, award it to the player
//            if (!curatedTreasureNotAlreadyCaught.isEmpty())
//            {
//                Item item = curatedTreasureNotAlreadyCaught.get(level.random.nextIntBetweenInclusive(0, curatedTreasureNotAlreadyCaught.size() - 1));
//                treasuresCaught.add(BuiltInRegistries.ITEM.getKey(item));
//                player.setData(ModDataAttachments.TREASURES_CAUGHT, treasuresCaught);
//                player.setItemInHand(usedHand, new ItemStack(item));
//                return InteractionResultHolder.success(player.getItemInHand(usedHand));
//            }
//        }

        //todo fix treasure loot_table stuff idk how to get it in 1.20
        //todo accept that im not going to fix curated loot :)
        //if no curated loot left, use default pool

        LootParams lootparams = (new LootParams.Builder((ServerLevel)level)).create(LootContextParamSets.EMPTY);
        LootTable loottable = level.getServer().getLootData().getLootTable(this.lootTable);
        List<ItemStack> arrayOfItemStacks = loottable.getRandomItems(lootparams);

        if (arrayOfItemStacks.isEmpty())
            LOGGER.warn("Loot table {} is empty, no treasure has been awarded.", this.lootTable);
        else
            player.setItemInHand(usedHand, arrayOfItemStacks.get(level.random.nextIntBetweenInclusive(0, arrayOfItemStacks.size() - 1)));

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
