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
            () -> BlockEntityType.Builder.of(StandBlockEntity::new,
                            SCBlocks.STAND.get()
                    ).build(null));

    Supplier<BlockEntityType<DisplayBlockEntity>> DISPLAY = BLOCK_ENTITIES.register("display",
            () -> BlockEntityType.Builder.of(DisplayBlockEntity::new,
                    SCBlocks.DISPLAY.get()
            ).build(null));

    Supplier<BlockEntityType<AquariumBlockEntity>> AQUARIUM = BLOCK_ENTITIES.register("aquarium",
            () -> BlockEntityType.Builder.of(AquariumBlockEntity::new,
                    SCBlocks.AQUARIUM.get()
            ).build(null));

    Supplier<BlockEntityType<TackleBoxBlockEntity>> TACKLE_BOX = BLOCK_ENTITIES.register("tackle_box",
            () -> BlockEntityType.Builder.of(TackleBoxBlockEntity::new,
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
            ).build(null));


    static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
