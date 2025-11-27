package com.wdiscute.starcatcher.blocks;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Starcatcher.MOD_ID);


    public static final Supplier<BlockEntityType<TrophyBlockEntity>> TROPHY = REGISTRY.register("trophy",
            () -> BlockEntityType.Builder.of(TrophyBlockEntity::new,
                            ModBlocks.TROPHY_GOLD.get(),
                            ModBlocks.TROPHY_SILVER.get(),
                            ModBlocks.TROPHY_BRONZE.get()
                    )
                    .build(null));
}
