package com.wdiscute.starcatcher.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.U;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.Tags;

import java.util.Arrays;
import java.util.List;

public class Treasure
{
    public static final TreasureInstance VANILLA_FISHING_LOOT_TABLE = new LootTableTreasureInstance(BuiltInLootTables.FISHING_TREASURE.location());
    public static final TreasureInstance AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE = new ItemStackListTreasureInstance(SCItems.AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final TreasureInstance KIMBE_SMITHING_TEMPLATE = new ItemStackListTreasureInstance(SCItems.KIMBE_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final TreasureInstance COLORFUL_SMITHING_TEMPLATE = new ItemStackListTreasureInstance(SCItems.COLORFUL_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final TreasureInstance CLEAR_SMITHING_TEMPLATE = new ItemStackListTreasureInstance(SCItems.CLEAR_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final TreasureInstance KING_SMITHING_TEMPLATE = new ItemStackListTreasureInstance(SCItems.KING_SMITHING_TEMPLATE.value().getDefaultInstance());
    public static final TreasureInstance ICEBORN_SKIN_SMITHING_TEMPLATE = new ItemStackListTreasureInstance(SCItems.ICEBORN_SKIN_SMITHING_TEMPLATE.value().getDefaultInstance());

    public abstract static class TreasureInstance
    {
        public abstract boolean isLootTable();

        public abstract ItemStack unpack(Player player);
    }

    public static final Codec<TreasureInstance> TREASURE_CODEC = Codec.BOOL
            .dispatch("is_loot_table", TreasureInstance::isLootTable, isLootTable ->
                    isLootTable ? LootTableTreasureInstance.CODEC : ItemStackListTreasureInstance.CODEC);

    public static class LootTableTreasureInstance extends TreasureInstance
    {
        ResourceLocation rl;

        public static final MapCodec<LootTableTreasureInstance> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("location").forGetter(o -> o.rl)
                ).apply(instance, LootTableTreasureInstance::new));

        public LootTableTreasureInstance(ResourceLocation rl)
        {
            this.rl = rl;
        }

        @Override
        public boolean isLootTable()
        {
            return true;
        }

        @Override
        public ItemStack unpack(Player player)
        {
            if(player instanceof ServerPlayer)
            {
                LootParams lootparams = new LootParams.Builder((ServerLevel) player.level())
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .withParameter(LootContextParams.TOOL, player.getMainHandItem().is(Tags.Items.RODS) ? player.getMainHandItem() : player.getOffhandItem())
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withLuck(player.getLuck())
                        .create(LootContextParamSets.FISHING);

                LootTable table = player.level().getServer().reloadableRegistries().getLootTable(
                        ResourceKey.create(Registries.LOOT_TABLE, rl)
                );

                ObjectArrayList<ItemStack> randomItems = table.getRandomItems(lootparams);

                if(randomItems.isEmpty()) return ItemStack.EMPTY;

                return randomItems.get(U.r.nextInt(randomItems.size()));
            }
            else
            {
                return ItemStack.EMPTY;
            }

        }


    }

    public static class ItemStackListTreasureInstance extends TreasureInstance
    {
        List<ItemStack> items;

        public static final MapCodec<ItemStackListTreasureInstance> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ItemStack.OPTIONAL_CODEC.listOf().fieldOf("items").forGetter(o -> o.items)
                ).apply(instance, ItemStackListTreasureInstance::new));

        public ItemStackListTreasureInstance(List<ItemStack> items)
        {
            this.items = items;
        }

        public ItemStackListTreasureInstance(ItemStack... items)
        {
            this.items = Arrays.stream(items).toList();
        }

        @Override
        public boolean isLootTable()
        {
            return false;
        }

        @Override
        public ItemStack unpack(Player player)
        {
            if(items.isEmpty()) return ItemStack.EMPTY;
            return items.get(U.r.nextInt(items.size()));
        }
    }
}
