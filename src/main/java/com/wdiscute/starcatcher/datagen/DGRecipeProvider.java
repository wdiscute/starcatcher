package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DGRecipeProvider extends RecipeProvider
{
    public DGRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
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

        //hats
        colorBlockWithDye(output, dyes, hats, "fisherman_hats");

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

        //aqua bobber
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.AQUA_BOBBER)
                .define('B', SCItems.BOBBER)
                .define('S', Items.STICK)
                .define('D', Items.DIAMOND)
                .define('H', Items.HEART_OF_THE_SEA)
                .pattern(" HS")
                .pattern("DBD")
                .pattern("SD ")
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

        //vanilla bobber from rod
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.VANILLA_BOBBER)
                .requires(Items.FISHING_ROD)
                .unlockedBy("has_starcatcher_rod", has(SCTags.RODS))
                .save(output, Starcatcher.rl("vanilla_bobber_from_vanilla_fishing_rod"));

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

        //cooked fish
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SCTags.STARCAUGHT_FISHES), RecipeCategory.FOOD, SCItems.COOKED_STARCAUGHT_FISH, 0.35F, 200)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHES))
                .save(output, Starcatcher.rl("starcaught_fish_from_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SCTags.STARCAUGHT_FISHES), RecipeCategory.FOOD, SCItems.COOKED_STARCAUGHT_FISH, 0.35F, 600)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHES))
                .save(output, Starcatcher.rl("starcaught_fish_from_campfire"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SCTags.STARCAUGHT_FISHES), RecipeCategory.FOOD, SCItems.COOKED_STARCAUGHT_FISH, 0.35F, 100)
                .unlockedBy("has_starcaught_fish", has(SCTags.STARCAUGHT_FISHES))
                .save(output, Starcatcher.rl("starcaught_fish_from_smoking"));


        //templates
        //tackle skins
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.PEARL_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.PEARL_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', SCItems.PEARL)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_pearl", has(SCItems.PEARL_SMITHING_TEMPLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.KIMBE_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.KIMBE_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', SCItems.WILLISH)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_kimbe", has(SCItems.KIMBE_SMITHING_TEMPLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.COLORFUL_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.COLORFUL_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Tags.Items.DYES)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_colorful", has(SCItems.COLORFUL_SMITHING_TEMPLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.CLEAR_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.CLEAR_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.GLASS)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_clean", has(SCItems.CLEAR_SMITHING_TEMPLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.FROG_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.FROG_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.TADPOLE_BUCKET)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_frog", has(SCItems.FROG_SMITHING_TEMPLATE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.KING_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.KING_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.GOLD_INGOT)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_king", has(SCItems.KING_SMITHING_TEMPLATE))
                .save(output);

        //rod skins
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.NATURALIST_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(ItemTags.SAPLINGS),
                        RecipeCategory.TOOLS,
                        SCItems.NATURALIST_ROD.get()
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.ICEBORN_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.PACKED_ICE),
                        RecipeCategory.TOOLS,
                        SCItems.ICEBORN_ROD.get()
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.MAGMAFORGED_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.MAGMA_CREAM),
                        RecipeCategory.TOOLS,
                        SCItems.MAGMAFORGED_ROD.get()
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.SLIMED_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.SLIME_BALL),
                        RecipeCategory.TOOLS,
                        SCItems.SLIMED_ROD.get()
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.AZURE_CRYSTAL_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(SCItems.AZURE_CRYSTALBACK_MINNOW),
                        RecipeCategory.TOOLS,
                        SCItems.AZURE_CRYSTAL_ROD.get()
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.BAMBOO),
                        RecipeCategory.TOOLS,
                        SCItems.BAMBOO_ROD.get()
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.BAMBOO),
                        RecipeCategory.TOOLS,
                        SCItems.SHARKTOOTH_ROD.get()
                )
                .unlocks("has_template_sharktooth", has(SCItems.BAMBOO_SKIN_SMITHING_TEMPLATE))
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.OBSIDIAN_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(SCItems.OBSIDIAN_EEL),
                        RecipeCategory.TOOLS,
                        SCItems.OBSIDIAN_ROD.get()
                )
                .unlocks("has_template_obsidian", has(SCItems.OBSIDIAN_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("obsidian_rod")
                );


        //alpha
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.ALPHA_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.ALPHA_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.GRASS_BLOCK)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_alpha", has(SCItems.ALPHA_SKIN_SMITHING_TEMPLATE))
                .save(output);

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.ALPHA_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.GRASS_BLOCK),
                        RecipeCategory.TOOLS,
                        SCItems.ALPHA_ROD.get()
                )
                .unlocks("has_template_alpha", has(SCItems.ALPHA_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("alpha_rod")
                );


        //good old
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SCItems.GOOD_OLD_SKIN_SMITHING_TEMPLATE, 2)
                .define('T', SCItems.GOOD_OLD_SKIN_SMITHING_TEMPLATE)
                .define('D', Items.DIAMOND)
                .define('C', Items.FISHING_ROD)
                .pattern("DTD")
                .pattern("DCD")
                .pattern("DDD")
                .unlockedBy("has_template_good_old", has(SCItems.GOOD_OLD_SKIN_SMITHING_TEMPLATE))
                .save(output);

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.GOOD_OLD_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.FISHING_ROD),
                        RecipeCategory.TOOLS,
                        SCItems.GOOD_OLD_ROD.get()
                )
                .unlocks("has_template_good_old", has(SCItems.GOOD_OLD_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("good_old_rod")
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.BONER_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.BONE_BLOCK),
                        RecipeCategory.TOOLS,
                        SCItems.BONER_ROD.get()
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.SKY_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.PHANTOM_MEMBRANE),
                        RecipeCategory.TOOLS,
                        SCItems.SKY_ROD.get()
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.LUSH_GLOWBERRY_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(SCItems.LUSH_PIKE),
                        RecipeCategory.TOOLS,
                        SCItems.LUSH_GLOWBERRY_ROD.get()
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

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(SCItems.HUMBLE_SKIN_SMITHING_TEMPLATE),
                        Ingredient.of(SCTags.RODS),
                        Ingredient.of(Items.STICK),
                        RecipeCategory.TOOLS,
                        SCItems.HUMBLE_ROD.get()
                )
                .unlocks("has_template_humble", has(SCItems.HUMBLE_SKIN_SMITHING_TEMPLATE))
                .save(output, Starcatcher.rl("humble_rod")
                );

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

        //letter
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SCItems.LETTER, 1)
                .requires(Items.PAPER)
                .requires(Items.INK_SAC)
                .requires(Items.FEATHER)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output);


    }

    protected static void colorBlockWithDye(RecipeOutput recipeOutput, List<Item> dyes, List<Item> dyeableItems, String group)
    {
        for (int i = 0; i < dyes.size(); ++i)
        {
            Item item = dyes.get(i);
            Item item1 = dyeableItems.get(i);
            ShapelessRecipeBuilder.shapeless(
                    RecipeCategory.BUILDING_BLOCKS, item1)
                    .requires(item)
                    .requires(Ingredient.of(dyeableItems.stream().filter((p_288265_) -> !p_288265_.equals(item1)).map(ItemStack::new))).group(group).unlockedBy("has_needed_dye", has(item)).save(recipeOutput, "dye_" + getItemName(item1));
        }

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
