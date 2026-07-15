package com.wdiscute.starcatcher.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.WeightedLootTable;
import com.wdiscute.starcatcher.fish.WeightedStack;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.fish.Treasure;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class StarcatcherJeiFPRecipe extends AbstractRecipeCategory<StarcatcherJeiFPRecipe.Recipe>
{
    public ItemStack rodIs;

    public StarcatcherJeiFPRecipe(IGuiHelper guiHelper)
    {
        super(
                Recipe.TYPE,
                Component.translatable("emi.category.starcatcher.fishing"),
                guiHelper.createDrawableItemLike(SCItems.ROD),
                98 + 16,
                20
        );
        rodIs = SCItems.ROD.get().getDefaultInstance();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses)
    {
        builder.addInputSlot(5, 2)
                .addItemStack(rodIs)
                .setStandardSlotBackground()
        ;

        if (!recipe.treasure.isEmpty())
            builder.addOutputSlot(64, 2)
                    .addIngredients(recipe.treasure)
                    .setStandardSlotBackground()
                    .addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.clear())
                    ;

        builder.addOutputSlot(44, 2)
                .addItemStack(recipe.fp.catchInfo().fish().toStack())
                .setStandardSlotBackground()
        ;
    }

    @Override
    public boolean handleInput(Recipe recipe, double mouseX, double mouseY, InputConstants.Key input)
    {
        if (mouseX > 90 && mouseX < 90 + 19 && mouseY > 0 && mouseY < 19)
        {
            Minecraft.getInstance().setScreen(new IsolatedJeiFPScreen(recipe, Minecraft.getInstance().screen));
            return true;
        }
        return false;
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        Font font = Minecraft.getInstance().font;
        guiGraphics.blit(StarcatcherJeiPlugin.ARROW, 25, 2, 16, 16, 0, 0, 16, 16, 16, 16);

        if (!recipe.treasureTooltips.isEmpty() && mouseX > 62 && mouseX < 62 + 19 && mouseY > 0 && mouseY < 19)
        {
            guiGraphics.renderTooltip(font, recipe.treasureTooltips, Optional.empty(), (int) mouseX, (int) mouseY);
        }

        bookIcon(guiGraphics, 83, 0, (int) mouseX, (int) mouseY);

        //restrictions on arrow hover
        if (mouseX > 25 && mouseX < 25 + 16 && mouseY > 0 && mouseY < 16)
        {
            guiGraphics.renderTooltip(font, recipe.components, Optional.empty(), ((int) mouseX), ((int) mouseY));
        }

        //[!] + hover
        if (recipe.fp.catchInfo().alwaysSpawnEntity())
        {
            guiGraphics.drawString(font, Component.literal("[!]").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)),
                    105, 12, 0x000000, false);

            if (mouseX > 105 && mouseX < 105 + 9 && mouseY > 12 && mouseY < 12 + 9)
            {
                guiGraphics.renderTooltip(font, Component.translatable("emi.starcatcher.entity_entry", recipe.fp.getDisplayName()), ((int) mouseX), ((int) mouseY));
            }
        }
    }

    public void bookIcon(GuiGraphics draw, int x, int y, int mouseX, int mouseY)
    {
        draw.blit(StarcatcherJeiPlugin.SLOT_BACKGROUND_FILLED, x, y, 0, 0, 20, 20, 20, 20);
        draw.renderItem(SCItems.GUIDE.toStack(), x + 2, y + 2, 20);
        if (mouseX > x && mouseX < x + 19 && mouseY > y && mouseY < y + 19)
        {
            draw.renderTooltip(Minecraft.getInstance().font, Component.translatable("emi.starcatcher.open_as_guide_entry"), mouseX, mouseY);
        }
    }

    @Override
    public ResourceLocation getRegistryName(Recipe recipe)
    {
        return Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(recipe.fp);
    }

    public record Recipe(FishProperties fp, List<Component> components, List<Component> treasureTooltips,
                         Ingredient treasure)
    {
        public static Recipe of(FishProperties fp)
        {
            List<Component> restrictions = new ArrayList<>();
            List<Component> treasureTooltip = new ArrayList<>();

            Holder<FishProperties> holder = Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).wrapAsHolder(fp);
            Treasure treasure = holder.getData(SCDataMaps.TREASURE);

            if (treasure == null) treasure = Treasure.EMPTY;

            List<WeightedLootTable> lts = new ArrayList<>(treasure.lootTables());
            lts.sort(Comparator.comparingInt(WeightedLootTable::weight));
            lts = lts.reversed();

            if (!lts.isEmpty())
            {
                treasureTooltip.add(Component.translatable("emi.starcatcher.fp.loot_tables").withStyle(ChatFormatting.BOLD)
                        .append(Component.translatable("emi.starcatcher.fp.weight").withStyle(Style.EMPTY.withBold(false)).withStyle(ChatFormatting.DARK_GRAY)));
                for (WeightedLootTable lootTable : lts)
                {
                    treasureTooltip.add(Component.literal(lootTable.weight() + " - " + lootTable.resourceLocation().getNamespace() + ":" + lootTable.resourceLocation().getPath()));
                }
            }

            List<WeightedStack> stks = new ArrayList<>(treasure.stacks());
            stks.sort(Comparator.comparingInt(WeightedStack::weight));
            stks = stks.reversed();

            List<ItemStack> stacks = new ArrayList<>();

            stacks.add(SCItems.TREASURE.toStack());

            if (!stks.isEmpty())
            {
                stacks.removeFirst();

                if (!treasureTooltip.isEmpty())
                    treasureTooltip.add(Component.literal(""));

                treasureTooltip.add(Component.translatable("emi.starcatcher.fp.individual_items").withStyle(ChatFormatting.BOLD)
                        .append(Component.translatable("emi.starcatcher.fp.weight").withStyle(Style.EMPTY.withBold(false)).withStyle(ChatFormatting.DARK_GRAY)));
                for (WeightedStack weightedStack : stks)
                {
                    treasureTooltip.add(Component.literal(weightedStack.weight() + " - ").append(weightedStack.stack().toStack().getHoverName()));
                    stacks.add(weightedStack.stack().toStack());
                }
            }

            if (!treasure.blacklist().isEmpty())
            {
                if (!treasureTooltip.isEmpty())
                    treasureTooltip.add(Component.literal(""));

                treasureTooltip.add(Component.translatable("emi.starcatcher.fp.blacklisted_items").withStyle(ChatFormatting.BOLD));
                for (Ingredient ing : treasure.blacklist())
                {
                    ItemStack[] items = ing.getItems();
                    for (ItemStack item : items)
                    {
                        treasureTooltip.add(item.getHoverName());
                    }
                }
            }

            Ingredient treasureIngredient;
            if ((!stacks.isEmpty() && !stacks.get(0).is(SCItems.TREASURE)) || !lts.isEmpty() || !treasure.blacklist().isEmpty())
                treasureIngredient = Ingredient.of(stacks.stream());
            else
                treasureIngredient = Ingredient.EMPTY;

            //aurora
            restrictions.add(fp.getDisplayName());

            //❌ Dimension
            //✅ Biome
            fp.restrictions().stream().filter(AbstractFishRestriction::isEnabled).forEach(o ->
                    restrictions.addAll(
                            o.getIndexHover(
                                    Minecraft.getInstance().level,
                                    fp,
                                    Minecraft.getInstance().player,
                                    AbstractFishRestriction.Context.JEMI)
                    )
            );

            return new Recipe(fp, restrictions, treasureTooltip, treasureIngredient);
        }

        public static final RecipeType<Recipe> TYPE = new RecipeType<>(Starcatcher.rl("fishing"), Recipe.class);
    }
}
