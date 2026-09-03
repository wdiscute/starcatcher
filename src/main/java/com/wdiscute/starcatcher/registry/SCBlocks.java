package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.blocks.*;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlock;
import com.wdiscute.starcatcher.blocks.clam.ClamBlock;
import com.wdiscute.starcatcher.blocks.clam.ConchBlock;
import com.wdiscute.starcatcher.blocks.display.DisplayBlock;
import com.wdiscute.starcatcher.blocks.stand.StandBlock;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxBlock;
import com.wdiscute.starcatcher.registry.items.HatItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public interface SCBlocks
{
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Starcatcher.MOD_ID);
    DeferredRegister.Blocks HATS = DeferredRegister.createBlocks(Starcatcher.MOD_ID);
    DeferredRegister.Blocks TACKLE_BOXES = DeferredRegister.createBlocks(Starcatcher.MOD_ID);

    DeferredBlock<Block> TROPHY_OF_THE_OLDER_ANGLER = registerBlock("trophy_of_the_older_angler", TrophyOfTheOlderAngler::new);
    DeferredBlock<Block> TROPHY_DIAMOND = registerBlock("trophy_diamond", TrophyBlock::new);
    DeferredBlock<Block> TROPHY_EMERALD = registerBlock("trophy_emerald", TrophyBlock::new);
    DeferredBlock<Block> TROPHY_GOLD = registerBlock("trophy_gold", TrophyBlock::new);
    DeferredBlock<Block> TROPHY_IRON = registerBlock("trophy_iron", TrophyBlock::new);
    DeferredBlock<Block> TROPHY_COPPER = registerBlock("trophy_copper", TrophyBlock::new);

    DeferredBlock<Block> STAND = registerBlock("tournament_stand", StandBlock::new);

    DeferredBlock<Block> DISPLAY = registerBlock("display", DisplayBlock::new);

    DeferredBlock<Block> AQUARIUM = registerBlock("aquarium", AquariumBlock::new);

    DeferredBlock<Block> CLAM = registerBlock("clam", ClamBlock::new);

    DeferredBlock<Block> CONCH = registerBlock("conch", ConchBlock::new);

    //tackle boxes
    DeferredBlock<Block> TACKLE_BOX = registerTackleBox("tackle_box", (p) -> new TackleBoxBlock(p, null, MapColor.TERRACOTTA_WHITE));
    DeferredBlock<Block> TACKLE_BOX_WHITE = registerTackleBox("tackle_box_white", (p) -> new TackleBoxBlock(p,DyeColor.WHITE, MapColor.SNOW));
    DeferredBlock<Block> TACKLE_BOX_LIME = registerTackleBox("tackle_box_lime", (p) -> new TackleBoxBlock(p,DyeColor.LIME, MapColor.COLOR_LIGHT_GREEN));
    DeferredBlock<Block> TACKLE_BOX_ORANGE = registerTackleBox("tackle_box_orange", (p) -> new TackleBoxBlock(p,DyeColor.ORANGE, MapColor.COLOR_ORANGE));
    DeferredBlock<Block> TACKLE_BOX_RED = registerTackleBox("tackle_box_red", (p) -> new TackleBoxBlock(p,DyeColor.RED, MapColor.COLOR_RED));
    DeferredBlock<Block> TACKLE_BOX_GRAY = registerTackleBox("tackle_box_gray", (p) -> new TackleBoxBlock(p,DyeColor.GRAY, MapColor.COLOR_GRAY));
    DeferredBlock<Block> TACKLE_BOX_LIGHT_GRAY = registerTackleBox("tackle_box_light_gray", (p) -> new TackleBoxBlock(p,DyeColor.LIGHT_GRAY, MapColor.COLOR_LIGHT_GRAY));
    DeferredBlock<Block> TACKLE_BOX_BLACK = registerTackleBox("tackle_box_black", (p) -> new TackleBoxBlock(p,DyeColor.BLACK, MapColor.COLOR_BLACK));
    DeferredBlock<Block> TACKLE_BOX_BROWN = registerTackleBox("tackle_box_brown", (p) -> new TackleBoxBlock(p,DyeColor.BROWN, MapColor.COLOR_BROWN));
    DeferredBlock<Block> TACKLE_BOX_YELLOW = registerTackleBox("tackle_box_yellow", (p) -> new TackleBoxBlock(p,DyeColor.YELLOW, MapColor.COLOR_YELLOW));
    DeferredBlock<Block> TACKLE_BOX_PINK = registerTackleBox("tackle_box_pink", (p) -> new TackleBoxBlock(p,DyeColor.PINK, MapColor.COLOR_PINK));
    DeferredBlock<Block> TACKLE_BOX_MAGENTA = registerTackleBox("tackle_box_magenta", (p) -> new TackleBoxBlock(p,DyeColor.MAGENTA, MapColor.COLOR_MAGENTA));
    DeferredBlock<Block> TACKLE_BOX_PURPLE = registerTackleBox("tackle_box_purple", (p) -> new TackleBoxBlock(p,DyeColor.PURPLE, MapColor.TERRACOTTA_PURPLE));
    DeferredBlock<Block> TACKLE_BOX_BLUE = registerTackleBox("tackle_box_blue", (p) -> new TackleBoxBlock(p,DyeColor.BLUE, MapColor.COLOR_BLUE));
    DeferredBlock<Block> TACKLE_BOX_LIGHT_BLUE = registerTackleBox("tackle_box_light_blue", (p) -> new TackleBoxBlock(p,DyeColor.LIGHT_BLUE, MapColor.COLOR_LIGHT_BLUE));
    DeferredBlock<Block> TACKLE_BOX_CYAN = registerTackleBox("tackle_box_cyan", (p) -> new TackleBoxBlock(p,DyeColor.CYAN, MapColor.COLOR_CYAN));
    DeferredBlock<Block> TACKLE_BOX_GREEN = registerTackleBox("tackle_box_green", (p) -> new TackleBoxBlock(p,DyeColor.GREEN, MapColor.COLOR_GREEN));

    //hats
    DeferredBlock<Block> FISHERMAN_HAT_WHITE = registerHat("fisherman_hat_white", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_LIME = registerHat("fisherman_hat_lime", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_ORANGE = registerHat("fisherman_hat_orange", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_RED = registerHat("fisherman_hat_red", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_GRAY = registerHat("fisherman_hat_gray", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_LIGHT_GRAY = registerHat("fisherman_hat_light_gray", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_BLACK = registerHat("fisherman_hat_black", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_BROWN = registerHat("fisherman_hat_brown", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_YELLOW = registerHat("fisherman_hat_yellow", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_PINK = registerHat("fisherman_hat_pink", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_MAGENTA = registerHat("fisherman_hat_magenta", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_PURPLE = registerHat("fisherman_hat_purple", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_BLUE = registerHat("fisherman_hat_blue", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_LIGHT_BLUE = registerHat("fisherman_hat_light_blue", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_CYAN = registerHat("fisherman_hat_cyan", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_GREEN = registerHat("fisherman_hat_green", HatBlock::new);

    private static DeferredBlock<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> block)
    {
        DeferredBlock<Block> toReturn = BLOCKS.registerBlock(name, block);
        SCItems.ITEMS.registerItem(name, (p) -> new BlockItem(toReturn.get(), p));
        return toReturn;
    }

    private static DeferredBlock<Block> registerHat(String name, Function<BlockBehaviour.Properties, Block> block)
    {
        DeferredBlock<Block> toReturn = HATS.registerBlock(name, block);
            SCItems.ITEMS.registerItem(name, (p) -> new HatItem(p, toReturn.get()));
        return toReturn;
    }

    private static DeferredBlock<Block> registerTackleBox(String name, Function<BlockBehaviour.Properties, Block> block)
    {
        DeferredBlock<Block> toReturn = TACKLE_BOXES.registerBlock(name, block);
        SCItems.ITEMS.registerItem(name, (p) -> new BlockItem(toReturn.get(), p
                .stacksTo(1)
                .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
        ));
        return toReturn;
    }

    static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
        HATS.register(eventBus);
        TACKLE_BOXES.register(eventBus);
    }
}