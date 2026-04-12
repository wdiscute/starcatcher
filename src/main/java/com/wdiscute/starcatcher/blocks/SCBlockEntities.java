package com.wdiscute.starcatcher.blocks;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlockEntity;
import com.wdiscute.starcatcher.blocks.Telescope.TelescopeBlockEntity;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockEntity;
import com.wdiscute.starcatcher.blocks.stand.StandBlockEntity;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public class SCBlockEntities
{
    public static final DeferredRegisterTyped<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegisterTyped.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Starcatcher.MOD_ID);

    public static final Supplier<BlockEntityType<StandBlockEntity>> STAND = BLOCK_ENTITIES.register("stand",
            () -> BlockEntityType.Builder.of(StandBlockEntity::new,
                            SCBlocks.STAND.get()
                    ).build(null));

    public static final Supplier<BlockEntityType<TelescopeBlockEntity>> TELESCOPE = BLOCK_ENTITIES.register("telescope",
            () -> BlockEntityType.Builder.of(TelescopeBlockEntity::new,
                    SCBlocks.TELESCOPE.get()
            ).build(null));

    public static final Supplier<BlockEntityType<DisplayBlockEntity>> DISPLAY = BLOCK_ENTITIES.register("display",
            () -> BlockEntityType.Builder.of(DisplayBlockEntity::new,
                    SCBlocks.DISPLAY.get()
            ).build(null));

    public static final Supplier<BlockEntityType<AquariumBlockEntity>> AQUARIUM = BLOCK_ENTITIES.register("aquarium",
            () -> BlockEntityType.Builder.of(AquariumBlockEntity::new,
                    SCBlocks.AQUARIUM.get()
            ).build(null));

    public static final Supplier<BlockEntityType<TackleBoxBlockEntity>> TACKLE_BOX = BLOCK_ENTITIES.register("tackle_box.png",
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


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
