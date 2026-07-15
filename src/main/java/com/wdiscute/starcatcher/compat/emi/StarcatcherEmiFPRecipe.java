package com.wdiscute.starcatcher.compat.emi;

import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.fish.WeightedLootTable;
import com.wdiscute.starcatcher.fish.WeightedStack;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.fish.Treasure;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
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
import java.util.Comparator;
import java.util.List;

public class StarcatcherEmiFPRecipe implements EmiRecipe
{

    public static final EmiTexture ARROW = new EmiTexture(Starcatcher.rl("textures/gui/jemi/arrow.png"),
            0, 0, 16, 16, 16, 16, 16, 16);
    private static final EmiTexture SLOT_BACKGROUND_FILLED = new EmiTexture(Starcatcher.rl("textures/gui/jemi/slot_background_filled.png"),
            0, 0, 18, 18, 18, 18, 18, 18);

    private final ResourceLocation id;
    private final EmiIngredient output;
    private final EmiIngredient rod = EmiIngredient.of(Ingredient.of(SCItems.ROD));
    private final ItemStack is;
    private final EmiIngredient treasureIngredient;
    private final FishProperties fp;

    List<Component> restrictions = new ArrayList<>();
    List<Component> treasureTooltip = new ArrayList<>();

    public StarcatcherEmiFPRecipe(ResourceLocation id, FishProperties fp)
    {
        this.id = id;
        this.is = fp.catchInfo().fish().toStack();

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
            stacks.remove(0);

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

        this.treasureIngredient = EmiIngredient.of(Ingredient.of(stacks.stream()));

        this.fp = fp;
        this.output = EmiIngredient.of(List.of(
                EmiIngredient.of(Ingredient.of(fp.catchInfo().fish().toStack())),
                this.treasureIngredient
        ));

        //Aurora
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
        return 118;
    }

    @Override
    public int getDisplayHeight()
    {
        return 22;
    }


    @Override
    public void addWidgets(WidgetHolder widgets)
    {
        //rod
        widgets.addSlot(rod, 5, 2);

        //arrow + restrictions hover
        widgets.addTexture(ARROW, 25, 2).tooltipText(restrictions);

        //render treasure itemstack if available
        if(!treasureTooltip.isEmpty())
        {
            widgets.addTexture(SLOT_BACKGROUND_FILLED, 64, 2).tooltipText(treasureTooltip);
            widgets.add(new StarcatcherRenderItemEmiWidget(65, 3, treasureIngredient.getEmiStacks().getFirst().getItemStack()));
        }


        //fish
        widgets.addSlot(EmiIngredient.of(Ingredient.of(is)), 44, 2).recipeContext(this);


        //render slot background
        widgets.addTexture(SLOT_BACKGROUND_FILLED, 84, 2);
        //render guide book
        widgets.add(new StarcatcherRenderItemEmiWidget(85, 3, SCItems.GUIDE.toStack()));
        //show in guide hover
        widgets.add(new StarcatcherShowInGuideEmiWidget(83, 1, fp));


        if (fp.catchInfo().alwaysSpawnEntity())
        {
            List<Component> components = List.of(Component.translatable("emi.starcatcher.entity_entry", fp.getDisplayName()));

            widgets.addText(Component.literal("[!]").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)), 86 + (treasureIngredient.isEmpty() ? 0 : 20), 13, 0x000000, false);
            widgets.addTooltip(components.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList(),
                    86 + (treasureIngredient.isEmpty() ? 0 : 20), 13, 9, 9);
        }

    }
}
