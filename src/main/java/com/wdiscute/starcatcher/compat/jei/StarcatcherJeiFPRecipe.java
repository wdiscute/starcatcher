package com.wdiscute.starcatcher.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.sellingbin.jei.SellingBinJeiPlugin;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.Treasure;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StarcatcherJeiFPRecipe extends AbstractRecipeCategory<StarcatcherJeiFPRecipe.Recipe>
{
    private static final ResourceLocation ICON = Starcatcher.rl("textures/gui/emi/emi_guide_icon.png");
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
        ;

        builder.addOutputSlot(44, 2)
                .addItemStack(recipe.fp.catchInfo().fish().value().getDefaultInstance())
        ;

        if (!recipe.fp.catchInfo().treasureIs().isEmpty())
            builder.addOutputSlot(64, 2)
                    .addItemStack(recipe.fp.catchInfo().treasureIs())
                    ;
    }

    public void bookIcon(GuiGraphics draw, int x, int y, int mouseX, int mouseY)
    {
        draw.blit(ICON, x, y, 0, 0, 20, 20, 20, 20);
        if (mouseX > x && mouseX < x + 19 && mouseY > y && mouseY < y + 19)
        {
            draw.renderTooltip(Minecraft.getInstance().font, Component.translatable("emi.starcatcher.open_as_guide_entry"), mouseX, mouseY);
        }
    }

    @Override
    public boolean handleInput(Recipe recipe, double mouseX, double mouseY, InputConstants.Key input)
    {
        if (mouseX > 90 && mouseX < 90 + 19 && mouseY > 0 && mouseY < 19)
        {
            Minecraft.getInstance().setScreen(new IsolatedJeiFPScreen(recipe));
            return true;
        }
        return false;
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        Font font = Minecraft.getInstance().font;
        guiGraphics.blit(StarcatcherJeiPlugin.SLOT_BACKGROUND, 4, 1, 18, 18, 0, 0, 18, 18, 18, 18);
        guiGraphics.blit(StarcatcherJeiPlugin.ARROW, 25, 2, 16, 16, 0, 0, 16, 16, 16, 16);
        guiGraphics.blit(SellingBinJeiPlugin.SLOT_BACKGROUND, 43, 1, 18, 18, 0, 0, 18, 18, 18, 18);

        if (!recipe.treasure.isEmpty())
            guiGraphics.blit(SellingBinJeiPlugin.SLOT_BACKGROUND, 63, 1, 18, 18, 0, 0, 18, 18, 18, 18);

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

    @Override
    public ResourceLocation getRegistryName(Recipe recipe)
    {
        return Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(recipe.fp);
    }

    public record Recipe(FishProperties fp, List<Component> components, ItemStack treasure)
    {
        public static Recipe of(FishProperties fp)
        {
            List<Component> restrictions = new ArrayList<>();

            ItemStack tre;


            Holder<FishProperties> holder = Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).wrapAsHolder(fp);
            Treasure.TreasureInstance data = holder.getData(SCDataMaps.TREASURE);
            if(fp.catchInfo().treasureIs().isEmpty())
            {
                if (data == null)
                    tre = ItemStack.EMPTY;
                else
                    tre = data.unpack(Minecraft.getInstance().player);
            }
            else
            {
                tre = fp.catchInfo().treasureIs();
            }

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
                                    AbstractFishRestriction.Context.EMI)
                    )
            );

            return new Recipe(fp, restrictions, tre);
        }

        public static final RecipeType<Recipe> TYPE = new RecipeType<>(Starcatcher.rl("fishing"), Recipe.class);
    }
}
