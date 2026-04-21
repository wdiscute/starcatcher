package com.wdiscute.starcatcher.blocks;

import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlock;
import com.wdiscute.starcatcher.blocks.Telescope.TelescopeBlock;
import com.wdiscute.starcatcher.blocks.display.DisplayBlock;
import com.wdiscute.starcatcher.blocks.stand.StandBlock;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxBlock;
import com.wdiscute.starcatcher.registry.items.HatItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
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

    DeferredBlock<Block> TELESCOPE = registerBlock("telescope", TelescopeBlock::new);

    DeferredBlock<Block> AQUARIUM = registerBlock("aquarium", AquariumBlock::new);

    DeferredBlock<Block> CLAM = registerBlock("clam", ClamBlock::new);

    DeferredBlock<Block> CONCH = registerBlock("conch", ConchBlock::new);

    //tackle boxes
    DeferredBlock<Block> TACKLE_BOX = registerTackleBox("tackle_box", (p) -> new TackleBoxBlock(null, MapColor.TERRACOTTA_WHITE, p));
    DeferredBlock<Block> TACKLE_BOX_WHITE = registerTackleBox("tackle_box_white", (p) -> new TackleBoxBlock(DyeColor.WHITE, MapColor.SNOW, p));
    DeferredBlock<Block> TACKLE_BOX_LIME = registerTackleBox("tackle_box_lime", (p) -> new TackleBoxBlock(DyeColor.LIME, MapColor.COLOR_LIGHT_GREEN, p));
    DeferredBlock<Block> TACKLE_BOX_ORANGE = registerTackleBox("tackle_box_orange", (p) -> new TackleBoxBlock(DyeColor.ORANGE, MapColor.COLOR_ORANGE, p));
    DeferredBlock<Block> TACKLE_BOX_RED = registerTackleBox("tackle_box_red", (p) -> new TackleBoxBlock(DyeColor.RED, MapColor.COLOR_RED, p));
    DeferredBlock<Block> TACKLE_BOX_GRAY = registerTackleBox("tackle_box_gray", (p) -> new TackleBoxBlock(DyeColor.GRAY, MapColor.COLOR_GRAY, p));
    DeferredBlock<Block> TACKLE_BOX_LIGHT_GRAY = registerTackleBox("tackle_box_light_gray", (p) -> new TackleBoxBlock(DyeColor.LIGHT_GRAY, MapColor.COLOR_LIGHT_GRAY, p));
    DeferredBlock<Block> TACKLE_BOX_BLACK = registerTackleBox("tackle_box_black", (p) -> new TackleBoxBlock(DyeColor.BLACK, MapColor.COLOR_BLACK, p));
    DeferredBlock<Block> TACKLE_BOX_BROWN = registerTackleBox("tackle_box_brown", (p) -> new TackleBoxBlock(DyeColor.BROWN, MapColor.COLOR_BROWN, p));
    DeferredBlock<Block> TACKLE_BOX_YELLOW = registerTackleBox("tackle_box_yellow", (p) -> new TackleBoxBlock(DyeColor.YELLOW, MapColor.COLOR_YELLOW, p));
    DeferredBlock<Block> TACKLE_BOX_PINK = registerTackleBox("tackle_box_pink", (p) -> new TackleBoxBlock(DyeColor.PINK, MapColor.COLOR_PINK, p));
    DeferredBlock<Block> TACKLE_BOX_MAGENTA = registerTackleBox("tackle_box_magenta", (p) -> new TackleBoxBlock(DyeColor.MAGENTA, MapColor.COLOR_MAGENTA, p));
    DeferredBlock<Block> TACKLE_BOX_PURPLE = registerTackleBox("tackle_box_purple", (p) -> new TackleBoxBlock(DyeColor.PURPLE, MapColor.TERRACOTTA_PURPLE, p));
    DeferredBlock<Block> TACKLE_BOX_BLUE = registerTackleBox("tackle_box_blue", (p) -> new TackleBoxBlock(DyeColor.BLUE, MapColor.COLOR_BLUE, p));
    DeferredBlock<Block> TACKLE_BOX_LIGHT_BLUE = registerTackleBox("tackle_box_light_blue", (p) -> new TackleBoxBlock(DyeColor.LIGHT_BLUE, MapColor.COLOR_LIGHT_BLUE, p));
    DeferredBlock<Block> TACKLE_BOX_CYAN = registerTackleBox("tackle_box_cyan", (p) -> new TackleBoxBlock(DyeColor.CYAN, MapColor.COLOR_CYAN, p));
    DeferredBlock<Block> TACKLE_BOX_GREEN = registerTackleBox("tackle_box_green", (p) -> new TackleBoxBlock(DyeColor.GREEN, MapColor.COLOR_GREEN, p));

    //hats
    DeferredBlock<Block> FISHERMAN_HAT_WHITE = registerHat("fisherman_hat_white");
    DeferredBlock<Block> FISHERMAN_HAT_LIME = registerHat("fisherman_hat_lime");
    DeferredBlock<Block> FISHERMAN_HAT_ORANGE = registerHat("fisherman_hat_orange");
    DeferredBlock<Block> FISHERMAN_HAT_RED = registerHat("fisherman_hat_red");
    DeferredBlock<Block> FISHERMAN_HAT_GRAY = registerHat("fisherman_hat_gray");
    DeferredBlock<Block> FISHERMAN_HAT_LIGHT_GRAY = registerHat("fisherman_hat_light_gray");
    DeferredBlock<Block> FISHERMAN_HAT_BLACK = registerHat("fisherman_hat_black");
    DeferredBlock<Block> FISHERMAN_HAT_BROWN = registerHat("fisherman_hat_brown");
    DeferredBlock<Block> FISHERMAN_HAT_YELLOW = registerHat("fisherman_hat_yellow");
    DeferredBlock<Block> FISHERMAN_HAT_PINK = registerHat("fisherman_hat_pink");
    DeferredBlock<Block> FISHERMAN_HAT_MAGENTA = registerHat("fisherman_hat_magenta");
    DeferredBlock<Block> FISHERMAN_HAT_PURPLE = registerHat("fisherman_hat_purple");
    DeferredBlock<Block> FISHERMAN_HAT_BLUE = registerHat("fisherman_hat_blue");
    DeferredBlock<Block> FISHERMAN_HAT_LIGHT_BLUE = registerHat("fisherman_hat_light_blue");
    DeferredBlock<Block> FISHERMAN_HAT_CYAN = registerHat("fisherman_hat_cyan");
    DeferredBlock<Block> FISHERMAN_HAT_GREEN = registerHat("fisherman_hat_green");

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> block)
    {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, block);
        SCItems.ITEMS.registerItem(name, (p) -> new BlockItem(toReturn.get(), p
                .setId(ResourceKey.create(Registries.ITEM, Starcatcher.rl(name)))));
        return toReturn;
    }

    private static DeferredBlock<Block> registerHat(String name)
    {
        DeferredBlock<Block> toReturn = HATS.register(name, () -> new HatBlock(name));
        SCItems.ITEMS.register(name, () -> new HatItem(toReturn.get(), name));
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerTackleBox(String name, Function<BlockBehaviour.Properties, ? extends T> block)
    {
        DeferredBlock<T> toReturn = TACKLE_BOXES.registerBlock(name, block);
        SCItems.ITEMS.registerItem(name, (p) -> new BlockItem(toReturn.get(), p
                .setId(ResourceKey.create(Registries.ITEM, Starcatcher.rl(name)))));
        return toReturn;
    }

    static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
        HATS.register(eventBus);
        TACKLE_BOXES.register(eventBus);
    }
}