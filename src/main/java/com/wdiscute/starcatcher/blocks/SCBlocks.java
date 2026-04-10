package com.wdiscute.starcatcher.blocks;

import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.aquarium.AquariumBlock;
import com.wdiscute.starcatcher.blocks.Telescope.TelescopeBlock;
import com.wdiscute.starcatcher.blocks.display.DisplayBlock;
import com.wdiscute.starcatcher.blocks.stand.StandBlock;
import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxBlock;
import com.wdiscute.starcatcher.registry.catchmodifiers.SCCatchModifiers;
import com.wdiscute.starcatcher.registry.items.HatItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

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
    DeferredBlock<Block> TACKLE_BOX = registerTackleBox("tackle_box", () -> new TackleBoxBlock(null, MapColor.TERRACOTTA_WHITE));
    DeferredBlock<Block> TACKLE_BOX_WHITE = registerTackleBox("tackle_box_white", () -> new TackleBoxBlock(DyeColor.WHITE, MapColor.SNOW));
    DeferredBlock<Block> TACKLE_BOX_LIME = registerTackleBox("tackle_box_lime", () -> new TackleBoxBlock(DyeColor.LIME, MapColor.COLOR_LIGHT_GREEN));
    DeferredBlock<Block> TACKLE_BOX_ORANGE = registerTackleBox("tackle_box_orange", () -> new TackleBoxBlock(DyeColor.ORANGE, MapColor.COLOR_ORANGE));
    DeferredBlock<Block> TACKLE_BOX_RED = registerTackleBox("tackle_box_red", () -> new TackleBoxBlock(DyeColor.RED, MapColor.COLOR_RED));
    DeferredBlock<Block> TACKLE_BOX_GRAY = registerTackleBox("tackle_box_gray", () -> new TackleBoxBlock(DyeColor.GRAY, MapColor.COLOR_GRAY));
    DeferredBlock<Block> TACKLE_BOX_LIGHT_GRAY = registerTackleBox("tackle_box_light_gray", () -> new TackleBoxBlock(DyeColor.LIGHT_GRAY, MapColor.COLOR_LIGHT_GRAY));
    DeferredBlock<Block> TACKLE_BOX_BLACK = registerTackleBox("tackle_box_black", () -> new TackleBoxBlock(DyeColor.BLACK, MapColor.COLOR_BLACK));
    DeferredBlock<Block> TACKLE_BOX_BROWN = registerTackleBox("tackle_box_brown", () -> new TackleBoxBlock(DyeColor.BROWN, MapColor.COLOR_BROWN));
    DeferredBlock<Block> TACKLE_BOX_YELLOW = registerTackleBox("tackle_box_yellow", () -> new TackleBoxBlock(DyeColor.YELLOW, MapColor.COLOR_YELLOW));
    DeferredBlock<Block> TACKLE_BOX_PINK = registerTackleBox("tackle_box_pink", () -> new TackleBoxBlock(DyeColor.PINK, MapColor.COLOR_PINK));
    DeferredBlock<Block> TACKLE_BOX_MAGENTA = registerTackleBox("tackle_box_magenta", () -> new TackleBoxBlock(DyeColor.MAGENTA, MapColor.COLOR_MAGENTA));
    DeferredBlock<Block> TACKLE_BOX_PURPLE = registerTackleBox("tackle_box_purple", () -> new TackleBoxBlock(DyeColor.PURPLE, MapColor.TERRACOTTA_PURPLE));
    DeferredBlock<Block> TACKLE_BOX_BLUE = registerTackleBox("tackle_box_blue", () -> new TackleBoxBlock(DyeColor.BLUE, MapColor.COLOR_BLUE));
    DeferredBlock<Block> TACKLE_BOX_LIGHT_BLUE = registerTackleBox("tackle_box_light_blue", () -> new TackleBoxBlock(DyeColor.LIGHT_BLUE, MapColor.COLOR_LIGHT_BLUE));
    DeferredBlock<Block> TACKLE_BOX_CYAN = registerTackleBox("tackle_box_cyan", () -> new TackleBoxBlock(DyeColor.CYAN, MapColor.COLOR_CYAN));
    DeferredBlock<Block> TACKLE_BOX_GREEN = registerTackleBox("tackle_box_green", () -> new TackleBoxBlock(DyeColor.GREEN, MapColor.COLOR_GREEN));

    //hats
    DeferredBlock<Block> FISHERMAN_HAT_WHITE = registerHat("fisherman_hat_white", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_LIME = registerHat("fisherman_hat_lime", HatBlock::new, SCCatchModifiers.BIG_DECREASES_LURE_TIME.getFirst(), SCCatchModifiers.ADD_CREEPER.getFirst());
    DeferredBlock<Block> FISHERMAN_HAT_ORANGE = registerHat("fisherman_hat_orange", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_RED = registerHat("fisherman_hat_red", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_GRAY = registerHat("fisherman_hat_gray", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_LIGHT_GRAY = registerHat("fisherman_hat_light_gray", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_BLACK = registerHat("fisherman_hat_black", HatBlock::new, SCCatchModifiers.HIDE_CATCH.getFirst(), SCCatchModifiers.EXTRA_ITEM.getFirst());
    DeferredBlock<Block> FISHERMAN_HAT_BROWN = registerHat("fisherman_hat_brown", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_YELLOW = registerHat("fisherman_hat_yellow", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_PINK = registerHat("fisherman_hat_pink", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_MAGENTA = registerHat("fisherman_hat_magenta", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_PURPLE = registerHat("fisherman_hat_purple", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_BLUE = registerHat("fisherman_hat_blue", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_LIGHT_BLUE = registerHat("fisherman_hat_light_blue", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_CYAN = registerHat("fisherman_hat_cyan", HatBlock::new);
    DeferredBlock<Block> FISHERMAN_HAT_GREEN = registerHat("fisherman_hat_green", HatBlock::new, SCCatchModifiers.BIG_DECREASES_LURE_TIME.getFirst());

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block)
    {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        SCItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerHat(String name, Supplier<T> block, ResourceLocation... modifiers)
    {
        DeferredBlock<T> toReturn = HATS.register(name, block);
        if (modifiers.length == 0)
            SCItems.ITEMS.register(name, () -> new HatItem(toReturn.get()));
        else
            SCItems.ITEMS.register(name, () -> new HatItem(toReturn.get(), modifiers));
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerTackleBox(String name, Supplier<T> block, ResourceLocation... modifiers)
    {
        DeferredBlock<T> toReturn = TACKLE_BOXES.register(name, block);
        SCItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties().stacksTo(1)));
        return toReturn;
    }

    static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
        HATS.register(eventBus);
        TACKLE_BOXES.register(eventBus);
    }
}