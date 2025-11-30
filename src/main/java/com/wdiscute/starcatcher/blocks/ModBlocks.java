package com.wdiscute.starcatcher.blocks;

import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ModBlocks
{
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Starcatcher.MOD_ID);

    DeferredBlock<Block> TROPHY_GOLD = registerBlock("trophy_gold", TrophyBlock::new);
    DeferredBlock<Block> TROPHY_SILVER = registerBlock("trophy_silver", TrophyBlock::new);
    DeferredBlock<Block> TROPHY_BRONZE = registerBlock("trophy_bronze", TrophyBlock::new);

    DeferredBlock<Block> STAND = registerStand("tournament_stand", StandBlock::new);




    private static <T extends Block> DeferredBlock<T> registerStand(String name, Supplier<T> block)
    {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);

        ModItems.OTHERS_REGISTRY.register(name, () -> new StandBlockItem(toReturn.get()));
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block)
    {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        ModItems.ITEMS_REGISTRY.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
