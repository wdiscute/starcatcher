package com.wdiscute.starcatcher.blocks;

import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public interface ModBlocks
{
    DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Starcatcher.MOD_ID);

    RegistryObject<Block> TROPHY_GOLD = registerBlock("trophy_gold", TrophyBlock::new);
    RegistryObject<Block> TROPHY_SILVER = registerBlock("trophy_silver", TrophyBlock::new);
    RegistryObject<Block> TROPHY_BRONZE = registerBlock("trophy_bronze", TrophyBlock::new);


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block)
    {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
