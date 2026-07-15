package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.recipe.StarcatcherRodRecipeBuilder;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DGSCRecipeProvider extends RecipeProvider
{
    public DGSCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        //guide
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, SCItems.GUIDE)
                .requires(SCItems.ROD)
                .requires(Items.BOOK)
                .unlockedBy("in_water", insideOf(Blocks.WATER))
                .save(output);

        //guide sign reset
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, SCItems.GUIDE)
                .requires(SCItems.GUIDE)
                .unlockedBy("has_guide", has(SCItems.GUIDE))
                .save(output, "guide_sign_reset");

        //rod
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SCItems.ROD)
                .define('S', Items.STICK)
                .define('B', SCItems.BOBBER)
                .define('H', SCItems.HOOK)
                .define('T', SCItems.STARCATCHER_TWINE)
                .pattern("  S")
                .pattern(" ST")
                .pattern("SHB")
                .unlockedBy("in_water", insideOf(Blocks.WATER))
                .save(output);

        //rod from vanilla
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, SCItems.ROD)
                .requires(Items.FISHING_ROD)
                .requires(SCItems.HOOK)
                .requires(SCItems.BOBBER)
                .requires(SCItems.STARCATCHER_TWINE)
                .unlockedBy("in_water", insideOf(Blocks.WATER))
                .save(output, Starcatcher.rl("rod_from_vanilla"));

        //dripstone bait
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.DRIPSTONE_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.DRIPSTONE_BLOCK)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.DRIPSTONE_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.POINTED_DRIPSTONE)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("dripstone_bait_from_pointed_dripstone"));

        //murkwater bait
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.MURKWATER_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.MANGROVE_LEAVES)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.MURKWATER_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.LILY_PAD)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("murkwater_bait_from_lilypad"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.MURKWATER_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.MANGROVE_ROOTS)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("murkwater_bait_from_mangrove_roots"));

        //cherry bait
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.CHERRY_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.PINK_PETALS)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //gunpowder bait
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.GUNPOWDER_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.GUNPOWDER)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //lush bait
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.LUSH_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.MOSS_BLOCK)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.LUSH_BAIT, 2)
                .requires(Items.BONE_MEAL)
                .requires(Items.MOSS_CARPET)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("lust_bait_from_moss_carpet"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.LUSH_BAIT, 8)
                .requires(Items.BONE_MEAL)
                .requires(SCItems.MOSSY_BOOT)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("lust_bait_from_mossy_boot"));

        //moss block from mossy boot
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MOSS_BLOCK, 1)
                .requires(SCItems.MOSSY_BOOT)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //leather from boot
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.LEATHER, 1)
                .requires(SCItems.BOOT)
                .unlockedBy("has_boot", has(SCItems.BOOT))
                .save(output);


        //sculk
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.SCULK_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.SCULK)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.SCULK_BAIT, 16)
                .requires(Items.BONE_MEAL)
                .requires(Items.SCULK_CATALYST)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("sculk_bait_from_sculk_catalyst"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.SCULK_BAIT, 16)
                .requires(Items.BONE_MEAL)
                .requires(SCItems.SCULKFISH)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("sculk_bait_from_sculkfish"));

        //legendary bait
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.LEGENDARY_BAIT, 4)
                .requires(Items.BONE_MEAL)
                .requires(Items.GOLDEN_APPLE)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.LEGENDARY_BAIT, 64)
                .requires(Items.BONE_MEAL)
                .requires(Items.ENCHANTED_GOLDEN_APPLE)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("legendary_bait_from_enchanted_golden_apple"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.LEGENDARY_BAIT, 16)
                .requires(Items.BONE_MEAL)
                .requires(SCTags.LEGENDARY_FISHES)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("legendary_bait_from_legendary_fish"));

        //meteorological
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.METEOROLOGICAL_BAIT, 32)
                .requires(Items.BONE_MEAL)
                .requires(Items.HEART_OF_THE_SEA)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.METEOROLOGICAL_BAIT, 8)
                .requires(Items.BONE_MEAL)
                .requires(SCTags.EPIC_FISHES)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("meteorological_bait_from_epic_fishes"));

        //radar
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.FISH_RADAR)
                .define('E', Items.ECHO_SHARD)
                .define('F', SCTags.LEGENDARY_FISHES)
                .define('I', Items.IRON_INGOT)
                .pattern(" E ")
                .pattern("IFI")
                .pattern(" I ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.BOBBER)
                .define('P', ItemTags.PLANKS)
                .define('W', ItemTags.WOOL)
                .define('S', Items.STICK)
                .pattern(" PS")
                .pattern("PWP")
                .pattern("SP ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //steady bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.STEADY_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('I', Items.IRON_INGOT)
                .define('C', Items.COPPER_INGOT)
                .pattern(" IS")
                .pattern("CBC")
                .pattern("SC ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //leaf bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.LEAF_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('L', ItemTags.LEAVES)
                .pattern(" LS")
                .pattern("LBL")
                .pattern("SL ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //leaf bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.SLIMEY_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('R', Items.SLIME_BALL)
                .pattern(" RS")
                .pattern("RBR")
                .pattern("SR ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //clear bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.CLEAR_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('G', Items.GLASS)
                .pattern(" GS")
                .pattern("GBG")
                .pattern("SG ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //dripstone bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.DRIPSTONE_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('D', Items.DRIPSTONE_BLOCK)
                .define('P', Items.POINTED_DRIPSTONE)
                .pattern(" DS")
                .pattern("DBD")
                .pattern("SP ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //vanilla bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.VANILLA_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('W', Items.RED_WOOL)
                .pattern(" WS")
                .pattern("WBW")
                .pattern("SW ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //glowing bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.GLOWING_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('W', Items.GLOW_INK_SAC)
                .define('G', Items.GLOW_BERRIES)
                .pattern(" WS")
                .pattern("GBG")
                .pattern("SG ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //golden bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.GOLDEN_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('G', Items.GOLD_BLOCK)
                .define('W', Items.GOLD_INGOT)
                .pattern(" GS")
                .pattern("WBW")
                .pattern("SW ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //cloud bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.CLOUD_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('F', Items.PHANTOM_MEMBRANE)
                .define('O', ItemTags.WOOL)
                .pattern(" FS")
                .pattern("OBO")
                .pattern("SO ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //vanilla bobber from rod
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.VANILLA_BOBBER)
                .requires(Items.FISHING_ROD)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("vanilla_bobber_from_vanilla_fishing_rod"));

        //vanilla hook bobber from rod
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.VANILLA_HOOK)
                .define('R', Items.FISHING_ROD)
                .define('N', Items.IRON_NUGGET)
                .pattern(" N ")
                .pattern(" R ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.HOOK)
                .define('I', Items.IRON_INGOT)
                .define('N', Items.IRON_NUGGET)
                .pattern("N  ")
                .pattern("I I")
                .pattern(" I ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //crystal hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.AMETHYST_HOOK)
                .define('H', SCItems.HOOK)
                .define('A', Items.AMETHYST_SHARD)
                .define('D', Items.DIAMOND)
                .pattern("D  ")
                .pattern("AHA")
                .pattern(" A ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //copper hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.COPPER_HOOK)
                .define('H', SCItems.HOOK)
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.COPPER_BLOCK)
                .pattern("B  ")
                .pattern("CHC")
                .pattern(" C ")
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(output);

        //exposed copper hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.EXPOSED_COPPER_HOOK)
                .define('H', SCItems.HOOK)
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.EXPOSED_COPPER)
                .pattern("B  ")
                .pattern("CHC")
                .pattern(" C ")
                .unlockedBy("has_exposed_copper", has(Items.EXPOSED_COPPER))
                .save(output);

        //weathered copper hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.WEATHERED_COPPER_HOOK)
                .define('H', SCItems.HOOK)
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.WEATHERED_COPPER)
                .pattern("B  ")
                .pattern("CHC")
                .pattern(" C ")
                .unlockedBy("has_weathered_copper_block", has(Items.WEATHERED_COPPER))
                .save(output);

        //weathered copper hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.OXIDISED_COPPER_HOOK)
                .define('H', SCItems.HOOK)
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.OXIDIZED_COPPER)
                .pattern("B  ")
                .pattern("CHC")
                .pattern(" C ")
                .unlockedBy("has_oxidised_copper_block", has(Items.OXIDIZED_COPPER))
                .save(output);

        //shiny hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.SHINY_HOOK)
                .define('H', SCItems.HOOK)
                .define('I', Items.IRON_NUGGET)
                .define('D', Items.DIAMOND)
                .pattern("I  ")
                .pattern("DHD")
                .pattern(" D ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //echoing hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.ECHOING_HOOK)
                .define('H', SCItems.HOOK)
                .define('I', Items.DIAMOND)
                .define('D', Items.ECHO_SHARD)
                .pattern("I  ")
                .pattern("DHD")
                .pattern(" D ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //frozen hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.FROZEN_HOOK)
                .define('H', SCItems.HOOK)
                .define('I', Items.BLUE_ICE)
                .define('D', Items.PACKED_ICE)
                .pattern("I  ")
                .pattern("DHD")
                .pattern(" D ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //gold hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.GOLD_HOOK)
                .define('H', SCItems.HOOK)
                .define('G', Items.GOLD_INGOT)
                .define('N', Items.GOLD_NUGGET)
                .pattern("N  ")
                .pattern("GHG")
                .pattern(" G ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //mossy
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.MOSSY_HOOK)
                .define('H', SCItems.HOOK)
                .define('M', Items.MOSS_BLOCK)
                .define('N', Items.IRON_NUGGET)
                .pattern("N  ")
                .pattern("MHM")
                .pattern(" M ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //stone hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.STONE_HOOK)
                .define('H', SCItems.HOOK)
                .define('S', Items.STONE)
                .define('N', Items.IRON_NUGGET)
                .pattern("N  ")
                .pattern("SHS")
                .pattern(" S ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //split hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.SPLIT_HOOK)
                .define('H', SCItems.HOOK)
                .define('C', Items.CHAIN)
                .define('N', Items.IRON_NUGGET)
                .pattern("N  ")
                .pattern("CHC")
                .pattern(" C ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //heavy hook
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.HEAVY_HOOK)
                .define('H', SCItems.HOOK)
                .define('I', Items.IRON_BLOCK)
                .define('N', Items.IRON_NUGGET)
                .pattern("N  ")
                .pattern("IHI")
                .pattern(" I ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //stand
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCBlocks.STAND)
                .define('G', Items.BOOK)
                .define('P', ItemTags.PLANKS)
                .define('B', Items.BARREL)
                .pattern(" G ")
                .pattern("PPP")
                .pattern(" B ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //display
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCBlocks.DISPLAY)
                .define('P', ItemTags.PLANKS)
                .define('B', ItemTags.WOODEN_SLABS)
                .pattern("   ")
                .pattern("BBB")
                .pattern(" P ")
                .unlockedBy("has_fish", has(SCTags.STARCAUGHT_FISHABLE))
                .save(output);

        //aquarium
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCBlocks.AQUARIUM)
                .define('G', Tags.Items.GLASS_PANES)
                .pattern("GGG")
                .pattern("G G")
                .pattern("GGG")
                .unlockedBy("has_glass", has(Items.GLASS))
                .save(output);

        //twine
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.STARCATCHER_TWINE)
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .pattern(" T ")
                .pattern("TST")
                .pattern(" T ")
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output);

        //bonemeal from clam
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 4)
                .requires(SCBlocks.CLAM)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("bone_meal_from_clam"));

        //bonemeal from conch
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 4)
                .requires(SCBlocks.CONCH)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("bone_meal_from_conch"));

        //bonemeal from fishbones
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 4)
                .requires(SCItems.FISH_BONES)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("bone_meal_from_fish_bones"));

        //starcaught fish
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.STARCAUGHT_FISH, 1)
                .requires(SCTags.STARCAUGHT_FISHABLE)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHABLE))
                .save(output, Starcatcher.rl("starcaught_fish_from_common"));

        //cooked fish from starcaught
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SCItems.STARCAUGHT_FISH), RecipeCategory.FOOD, SCItems.COOKED_STARCAUGHT_FISH, 0.35F, 200)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHABLE))
                .save(output, Starcatcher.rl("starcaught_fish_from_smelting_from_starcaught_fish"));

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SCItems.STARCAUGHT_FISH), RecipeCategory.FOOD, SCItems.COOKED_STARCAUGHT_FISH, 0.35F, 600)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHABLE))
                .save(output, Starcatcher.rl("starcaught_fish_from_campfire_from_starcaught_fish"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SCItems.STARCAUGHT_FISH), RecipeCategory.FOOD, SCItems.COOKED_STARCAUGHT_FISH, 0.35F, 100)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHABLE))
                .save(output, Starcatcher.rl("starcaught_fish_from_smoking_from_starcaught_fish"));

        //cooked fish from tag
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SCTags.STARCAUGHT_FISHABLE), RecipeCategory.FOOD, SCItems.COOKED_STARCAUGHT_FISH, 0.35F, 200)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHABLE))
                .save(output, Starcatcher.rl("starcaught_fish_from_smelting"));

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SCTags.STARCAUGHT_FISHABLE), RecipeCategory.FOOD, SCItems.COOKED_STARCAUGHT_FISH, 0.35F, 600)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHABLE))
                .save(output, Starcatcher.rl("starcaught_fish_from_campfire"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SCTags.STARCAUGHT_FISHABLE), RecipeCategory.FOOD, SCItems.COOKED_STARCAUGHT_FISH, 0.35F, 100)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHABLE))
                .save(output, Starcatcher.rl("starcaught_fish_from_smoking"));

        //
        //                   ,--.   ,--.   ,--.      ,--.
        // ,---.  ,--,--,--. `--' ,-'  '-. |  ,---.  `--' ,--,--,   ,---.
        //(  .-'  |        | ,--. '-.  .-' |  .-.  | ,--. |      \ | .-. |
        //.-'  `) |  |  |  | |  |   |  |   |  | |  | |  | |  ||  | ' '-' '
        //`----'  `--`--`--' `--'   `--'   `--' `--' `--' `--''--' .`-  /
        //                                                         `---'

        //netherite upgrade
        StarcatcherRodRecipeBuilder.netheriteUpgrade(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(Items.NETHERITE_INGOT)
                )
                .unlocks("has_netherite", has(Items.NETHERITE_INGOT))
                .save(output, Starcatcher.rl("netherite_upgrade")
                );

        //tackle
        //pearl
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.PEARL_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.PEARL_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', SCItems.PEARL)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_pearl", has(SCItems.PEARL_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.tackleSkin(
                        Ingredient.of(SCItems.PEARL_SMITHING_TEMPLATE),
                        Ingredient.of(SCItems.PEARL)
                )
                .unlocks("has_template_humble", has(SCItems.PEARL_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("pearl_tackle")
                );

        //kimbe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.KIMBE_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.KIMBE_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', SCItems.WILLISH)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_kimbe", has(SCItems.KIMBE_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.tackleSkin(
                        Ingredient.of(SCItems.KIMBE_SMITHING_TEMPLATE),
                        Ingredient.of(SCItems.WILLISH)
                )
                .unlocks("has_template_humble", has(SCItems.KIMBE_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("kimbe_tackle")
                );

        //colorful
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.COLORFUL_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.COLORFUL_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Tags.Items.DYES)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_colorful", has(SCItems.COLORFUL_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.tackleSkin(
                        Ingredient.of(SCItems.COLORFUL_SMITHING_TEMPLATE),
                        Ingredient.of(Tags.Items.DYES)
                )
                .unlocks("has_template_humble", has(SCItems.COLORFUL_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("colorful_tackle")
                );

        //clear
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.CLEAR_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.CLEAR_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.GLASS)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_clean", has(SCItems.CLEAR_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.tackleSkin(
                        Ingredient.of(SCItems.CLEAR_SMITHING_TEMPLATE),
                        Ingredient.of(Items.GLASS)
                )
                .unlocks("has_template_humble", has(SCItems.CLEAR_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("clear_tackle")
                );

        //frog
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.FROG_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.FROG_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.TADPOLE_BUCKET)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_frog", has(SCItems.FROG_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.tackleSkin(
                        Ingredient.of(SCItems.FROG_SMITHING_TEMPLATE),
                        Ingredient.of(Items.TADPOLE_BUCKET)
                )
                .unlocks("has_template_humble", has(SCItems.FROG_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("frog_tackle")
                );

        //king
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.KING_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.KING_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.GOLD_INGOT)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_king", has(SCItems.KING_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.tackleSkin(
                        Ingredient.of(SCItems.KING_SMITHING_TEMPLATE),
                        Ingredient.of(Items.GOLD_INGOT)
                )
                .unlocks("has_template_humble", has(SCItems.KING_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("king_tackle")
                );


        //rods
        //naturalist
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.NATURALIST_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.NATURALIST_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', ItemTags.SAPLINGS)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_naturalist", has(SCItems.NATURALIST_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.NATURALIST_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(ItemTags.SAPLINGS),
                        SCItems.NATURALIST_ROD.toStack()
                )
                .unlocks("has_template_naturalist", has(SCItems.NATURALIST_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("naturalist_rod")
                );

        //iceborn
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.ICEBORN_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.ICEBORN_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.PACKED_ICE)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_iceborn", has(SCItems.ICEBORN_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.ICEBORN_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(Items.PACKED_ICE),
                        SCItems.ICEBORN_ROD.toStack()
                )
                .unlocks("has_template_iceborn", has(SCItems.ICEBORN_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("iceborn_rod")
                );


        //magmaformed
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.MAGMAFORGED_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.MAGMAFORGED_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.MAGMA_CREAM)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_magmaforged", has(SCItems.MAGMAFORGED_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.MAGMAFORGED_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(Items.MAGMA_CREAM),
                        SCItems.MAGMAFORGED_ROD.toStack()
                )
                .unlocks("has_template_magmaforged", has(SCItems.MAGMAFORGED_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("magmaforged_rod")
                );



        //slimed
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.SLIMED_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.SLIMED_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.SLIME_BALL)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_slimed", has(SCItems.SLIMED_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.SLIMED_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(Items.SLIME_BALL),
                        SCItems.SLIMED_ROD.toStack()
                )
                .unlocks("has_template_slimed", has(SCItems.SLIMED_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("slimed_rod")
                );




        //azure
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', SCItems.AZURE_CRYSTALBACK_MINNOW)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_azure", has(SCItems.AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCItems.AZURE_CRYSTALBACK_MINNOW),
                        SCItems.AZURE_CRYSTAL_ROD.toStack()
                )
                .unlocks("has_template_azure", has(SCItems.AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("azure_rod")
                );



        //bamboo
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.BAMBOO)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_bamboo", has(SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(Items.BAMBOO),
                        SCItems.BAMBOO_ROD.toStack()
                )
                .unlocks("has_template_bamboo", has(SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("bamboo_rod")
                );


        //sharktooth
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.SHARKTOOTH_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.SHARKTOOTH_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', SCItems.JOEL)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_sharktooth", has(SCItems.SHARKTOOTH_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.SHARKTOOTH_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCItems.JOEL),
                        SCItems.SHARKTOOTH_ROD.toStack()
                )
                .unlocks("has_template_sharktooth", has(SCItems.SHARKTOOTH_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("sharktooth_rod")
                );

        //obsidian
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.OBSIDIAN_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.OBSIDIAN_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', SCItems.OBSIDIAN_EEL)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_obsidian", has(SCItems.OBSIDIAN_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.OBSIDIAN_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCItems.OBSIDIAN_EEL),
                        SCItems.OBSIDIAN_ROD.toStack()
                )
                .unlocks("has_template_obsidian", has(SCItems.OBSIDIAN_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("obsidian_rod")
                );

        //boner
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.BONER_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.BONER_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.BONE_BLOCK)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_boner", has(SCItems.BONER_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.BONER_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(Items.BONE_BLOCK),
                        SCItems.BONER_ROD.toStack()
                )
                .unlocks("has_template_boner", has(SCItems.BONER_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("boner_rod")
                );

        //sky
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.SKY_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.SKY_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.PHANTOM_MEMBRANE)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_sky", has(SCItems.SKY_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.SKY_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(Items.PHANTOM_MEMBRANE),
                        SCItems.SKY_ROD.toStack()
                )
                .unlocks("has_template_sky", has(SCItems.SKY_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("sky_rod")
                );


        //lush
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', SCItems.LUSH_PIKE)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_lush", has(SCItems.LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCItems.LUSH_PIKE),
                        SCItems.LUSH_GLOWBERRY_ROD.toStack()
                )
                .unlocks("has_template_lush", has(SCItems.LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("lush_rod")
                );


        //humble
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.HUMBLE_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.HUMBLE_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.STICK)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_humble", has(SCItems.HUMBLE_SKIN_SMITHING_TEMPLATE))
                .save(output);

        StarcatcherRodRecipeBuilder.rodSkin(
                        Ingredient.of(SCItems.HUMBLE_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(Items.STICK),
                        SCItems.HUMBLE_ROD.toStack()
                )
                .unlocks("has_template_humble", has(SCItems.HUMBLE_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("humble_rod")
                );

        //hats
        for (int i = 0; i < dyes.size(); i++)
        {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, hats.get(i), 1)
                    .define('P', SCItems.PEARL)
                    .define('C', carpets.get(i))
                    .define('W', wools.get(i))
                    .pattern(" P ")
                    .pattern("CWW")
                    .pattern("   ")
                    .unlockedBy("has_pearl", has(SCItems.PEARL))
                    .save(output);
        }

        //tackle boxes
        for (int i = 0; i < dyes.size(); i++)
        {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, tackle_boxes.get(i), 1)
                    .define('C', Items.COPPER_INGOT)
                    .define('H', Items.CHAIN)
                    .define('D', dyes.get(i))
                    .define('I', Items.IRON_INGOT)
                    .pattern("CCC")
                    .pattern("HDH")
                    .pattern("III")
                    .unlockedBy("has_fish", has(ItemTags.FISHES))
                    .save(output);
        }

        //tackle box
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCBlocks.TACKLE_BOX, 1)
                .define('C', Items.COPPER_INGOT)
                .define('H', Items.CHAIN)
                .define('I', Items.IRON_INGOT)
                .pattern("CCC")
                .pattern("H H")
                .pattern("III")
                .unlockedBy("has_fish", has(ItemTags.FISHES))
                .save(output);

        //letter
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.LETTER, 1)
                .requires(Items.PAPER)
                .requires(Items.INK_SAC)
                .requires(Items.FEATHER)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output);

        //worm > almighty
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.ALMIGHTY_WORM, 1)
                .requires(SCItems.WORM)
                .requires(SCItems.WORM)
                .requires(SCItems.WORM)
                .requires(SCItems.WORM)
                .unlockedBy("has_worm", has(SCItems.WORM))
                .save(output);

        //almighty > seeking
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.SEEKING_WORM, 1)
                .requires(SCItems.ALMIGHTY_WORM)
                .requires(SCItems.ALMIGHTY_WORM)
                .requires(SCItems.ALMIGHTY_WORM)
                .requires(SCItems.ALMIGHTY_WORM)
                .unlockedBy("has_almighty_worm", has(SCItems.WORM))
                .save(output);

        //fish recipes
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SUNFLOWER, 1)
                .requires(SCItems.SUNFLOWER_CARP)
                .unlockedBy("has_sunflower_carp", has(SCItems.SUNFLOWER_CARP))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BAMBOO, 1)
                .requires(SCItems.LIVID_BAMBOO)
                .unlockedBy("has_livid_bamboo", has(SCItems.LIVID_BAMBOO))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.OBSIDIAN, 4)
                .requires(SCItems.OBSIDIAN_CRAB)
                .unlockedBy("has_obsidian_crab", has(SCItems.OBSIDIAN_CRAB))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SEAGRASS, 1)
                .requires(SCItems.DRIED_SEAWEED)
                .requires(Items.POTION)
                .unlockedBy("has_dried_seaweed", has(SCItems.DRIED_SEAWEED))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SCULK, 1)
                .requires(SCItems.SCULKFISH)
                .unlockedBy("has_sculkfish", has(SCItems.SCULKFISH))
                .save(output);
    }

    List<Item> dyes = List.of(
            Items.BLACK_DYE,
            Items.BLUE_DYE,
            Items.BROWN_DYE,
            Items.CYAN_DYE,
            Items.GRAY_DYE,
            Items.GREEN_DYE,
            Items.LIGHT_BLUE_DYE,
            Items.LIGHT_GRAY_DYE,
            Items.LIME_DYE,
            Items.MAGENTA_DYE,
            Items.ORANGE_DYE,
            Items.PINK_DYE,
            Items.PURPLE_DYE,
            Items.RED_DYE,
            Items.YELLOW_DYE,
            Items.WHITE_DYE
    );

    List<Item> wools = List.of(
            Items.BLACK_WOOL,
            Items.BLUE_WOOL,
            Items.BROWN_WOOL,
            Items.CYAN_WOOL,
            Items.GRAY_WOOL,
            Items.GREEN_WOOL,
            Items.LIGHT_BLUE_WOOL,
            Items.LIGHT_GRAY_WOOL,
            Items.LIME_WOOL,
            Items.MAGENTA_WOOL,
            Items.ORANGE_WOOL,
            Items.PINK_WOOL,
            Items.PURPLE_WOOL,
            Items.RED_WOOL,
            Items.YELLOW_WOOL,
            Items.WHITE_WOOL
    );

    List<Item> carpets = List.of(
            Items.BLACK_CARPET,
            Items.BLUE_CARPET,
            Items.BROWN_CARPET,
            Items.CYAN_CARPET,
            Items.GRAY_CARPET,
            Items.GREEN_CARPET,
            Items.LIGHT_BLUE_CARPET,
            Items.LIGHT_GRAY_CARPET,
            Items.LIME_CARPET,
            Items.MAGENTA_CARPET,
            Items.ORANGE_CARPET,
            Items.PINK_CARPET,
            Items.PURPLE_CARPET,
            Items.RED_CARPET,
            Items.YELLOW_CARPET,
            Items.WHITE_CARPET
    );

    List<Item> hats = List.of(
            SCBlocks.FISHERMAN_HAT_BLACK.asItem(),
            SCBlocks.FISHERMAN_HAT_BLUE.asItem(),
            SCBlocks.FISHERMAN_HAT_BROWN.asItem(),
            SCBlocks.FISHERMAN_HAT_CYAN.asItem(),
            SCBlocks.FISHERMAN_HAT_GRAY.asItem(),
            SCBlocks.FISHERMAN_HAT_GREEN.asItem(),
            SCBlocks.FISHERMAN_HAT_LIGHT_BLUE.asItem(),
            SCBlocks.FISHERMAN_HAT_LIGHT_GRAY.asItem(),
            SCBlocks.FISHERMAN_HAT_LIME.asItem(),
            SCBlocks.FISHERMAN_HAT_MAGENTA.asItem(),
            SCBlocks.FISHERMAN_HAT_ORANGE.asItem(),
            SCBlocks.FISHERMAN_HAT_PINK.asItem(),
            SCBlocks.FISHERMAN_HAT_PURPLE.asItem(),
            SCBlocks.FISHERMAN_HAT_RED.asItem(),
            SCBlocks.FISHERMAN_HAT_YELLOW.asItem(),
            SCBlocks.FISHERMAN_HAT_WHITE.asItem()
    );

    List<Item> tackle_boxes = List.of(
            SCBlocks.TACKLE_BOX_BLACK.asItem(),
            SCBlocks.TACKLE_BOX_BLUE.asItem(),
            SCBlocks.TACKLE_BOX_BROWN.asItem(),
            SCBlocks.TACKLE_BOX_CYAN.asItem(),
            SCBlocks.TACKLE_BOX_GRAY.asItem(),
            SCBlocks.TACKLE_BOX_GREEN.asItem(),
            SCBlocks.TACKLE_BOX_LIGHT_BLUE.asItem(),
            SCBlocks.TACKLE_BOX_LIGHT_GRAY.asItem(),
            SCBlocks.TACKLE_BOX_LIME.asItem(),
            SCBlocks.TACKLE_BOX_MAGENTA.asItem(),
            SCBlocks.TACKLE_BOX_ORANGE.asItem(),
            SCBlocks.TACKLE_BOX_PINK.asItem(),
            SCBlocks.TACKLE_BOX_PURPLE.asItem(),
            SCBlocks.TACKLE_BOX_RED.asItem(),
            SCBlocks.TACKLE_BOX_YELLOW.asItem(),
            SCBlocks.TACKLE_BOX_WHITE.asItem()
    );
}
