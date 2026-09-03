package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlockEntity;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockEntity;
import com.wdiscute.starcatcher.blocks.stand.StandBlockEntity;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SCBlockEntities
{
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Starcatcher.MOD_ID);

    Supplier<BlockEntityType<StandBlockEntity>> STAND = BLOCK_ENTITIES.register("stand",
            () -> new BlockEntityType<>(StandBlockEntity::new, SCBlocks.STAND.get()));

    Supplier<BlockEntityType<DisplayBlockEntity>> DISPLAY = BLOCK_ENTITIES.register("display",
            () -> new BlockEntityType<>(DisplayBlockEntity::new,
                    SCBlocks.DISPLAY.get()));

    Supplier<BlockEntityType<AquariumBlockEntity>> AQUARIUM = BLOCK_ENTITIES.register("aquarium",
            () -> new BlockEntityType<>(AquariumBlockEntity::new,
                    SCBlocks.AQUARIUM.get()));

    Supplier<BlockEntityType<TackleBoxBlockEntity>> TACKLE_BOX = BLOCK_ENTITIES.register("tackle_box",
            () -> new BlockEntityType<>(TackleBoxBlockEntity::new,
                    SCBlocks.TACKLE_BOX.get(),
                    SCBlocks.TACKLE_BOX_WHITE.get(),
                    SCBlocks.TACKLE_BOX_LIME.get(),
                    SCBlocks.TACKLE_BOX_ORANGE.get(),
                    SCBlocks.TACKLE_BOX_RED.get(),
                    SCBlocks.TACKLE_BOX_GRAY.get(),
                    SCBlocks.TACKLE_BOX_LIGHT_GRAY.get(),
                    SCBlocks.TACKLE_BOX_BLACK.get(),
                    SCBlocks.TACKLE_BOX_BROWN.get(),
                    SCBlocks.TACKLE_BOX_YELLOW.get(),
                    SCBlocks.TACKLE_BOX_PINK.get(),
                    SCBlocks.TACKLE_BOX_MAGENTA.get(),
                    SCBlocks.TACKLE_BOX_PURPLE.get(),
                    SCBlocks.TACKLE_BOX_BLUE.get(),
                    SCBlocks.TACKLE_BOX_LIGHT_BLUE.get(),
                    SCBlocks.TACKLE_BOX_CYAN.get(),
                    SCBlocks.TACKLE_BOX_GREEN.get()
            ));


    static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
