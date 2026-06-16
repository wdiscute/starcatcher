package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.Treasure;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StarcatcherEmiFPRecipe implements EmiRecipe
{

    public static final EmiTexture EMPTY_ARROW = new EmiTexture(Starcatcher.rl("textures/gui/emi/arrow.png"),
            0, 0, 16, 16, 16, 16, 16, 16);
    private final ResourceLocation id;
    private final EmiIngredient output;
    private final EmiIngredient rod = EmiIngredient.of(Ingredient.of(SCItems.ROD));
    private final ItemStack is;
    private final EmiIngredient treasure;
    private final FishProperties fp;

    List<Component> restrictions = new ArrayList<>();

    public StarcatcherEmiFPRecipe(ResourceLocation id, FishProperties fp)
    {
        this.id = id;
        this.is = new ItemStack(fp.catchInfo().fish());

        Holder<FishProperties> holder = Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).wrapAsHolder(fp);
        Treasure.TreasureInstance data = holder.getData(SCDataMaps.TREASURE);
        if(fp.catchInfo().treasureIs().isEmpty())
        {
            if (data == null)
                this.treasure = EmiIngredient.of(Ingredient.of(ItemStack.EMPTY));
            else
                this.treasure = EmiIngredient.of(Ingredient.of(data.unpack(Minecraft.getInstance().player)));
        }
        else
        {
            this.treasure = EmiIngredient.of(Ingredient.of(fp.catchInfo().treasureIs()));
        }

        this.fp = fp;
        this.output = EmiIngredient.of(List.of(
                EmiIngredient.of(Ingredient.of(fp.catchInfo().fish().value())),
                treasure
        ));

        //Aurora
        restrictions.add(fp.getDisplayName());

        //Dimension
        //Biome
        fp.restrictions().stream().filter(AbstractFishRestriction::isEnabled).forEach(o ->
                restrictions.addAll(
                        o.getIndexHover(
                                Minecraft.getInstance().level,
                                fp,
                                Minecraft.getInstance().player,
                                AbstractFishRestriction.Context.EMI)
                )
        );
    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return StarcatcherEmiPlugin.STARCATCHER_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId()
    {
        return Starcatcher.rl("/" + id.getPath());
    }

    @Override
    public List<EmiIngredient> getInputs()
    {
        return List.of();
    }

    @Override
    public List<EmiIngredient> getCatalysts()
    {
        return List.of(rod);
    }

    @Override
    public List<EmiStack> getOutputs()
    {
        return output.getEmiStacks();
    }

    @Override
    public int getDisplayWidth()
    {
        return 90 + (treasure.isEmpty() ? 0 : 20) + (!fp.catchInfo().alwaysSpawnEntity() ? 0 : 8);
    }

    @Override
    public int getDisplayHeight()
    {
        return 22;
    }


    @Override
    public void addWidgets(WidgetHolder widgets)
    {
        widgets.addSlot(rod, 5, 2);

        widgets.addTexture(EMPTY_ARROW, 25, 2).tooltipText(restrictions);

        widgets.addSlot(EmiIngredient.of(Ingredient.of(is)), 44, 2).recipeContext(this);

        if (!fp.skipMinigame() && !treasure.isEmpty())
            widgets.addSlot(treasure, 64, 2).recipeContext(this);

        widgets.add(new StarcatcherShowInGuideEmiWidget(64 + (treasure.isEmpty() ? 0 : 20), 1, fp, this));

        if (fp.catchInfo().alwaysSpawnEntity())
        {
            List<Component> components = List.of(Component.translatable("emi.starcatcher.entity_entry", fp.getDisplayName()));

            widgets.addText(Component.literal("[!]").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)), 86 + (treasure.isEmpty() ? 0 : 20), 13, 0x000000, false);
            widgets.addTooltip(components.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList(),
                    86 + (treasure.isEmpty() ? 0 : 20), 13, 9, 9);
        }

    }
}
