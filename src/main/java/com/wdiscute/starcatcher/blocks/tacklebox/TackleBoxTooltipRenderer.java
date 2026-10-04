package com.wdiscute.starcatcher.blocks.tacklebox;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.ScreenUtils;
import com.wdiscute.utils.Utils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TackleBoxTooltipRenderer implements ClientTooltipComponent
{
    public static final ScreenUtils.Image ROD_TEXTURE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tackle_box_tooltip_rod.png"), 73, 19);
    public static final ScreenUtils.Image BOX_TEXTURE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tackle_box_tooltip.png"), 132, 42);
    public static final ScreenUtils.Image FISHES_TEXTURE_TOP = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tackle_box_tooltip_fishes_top.png"), 132, 8);
    public static final Identifier FISHES_TEXTURE_MIDDLE = Starcatcher.rl("textures/gui/tackle_box_tooltip_fishes_middle.png");
    public static final ScreenUtils.Image FISHES_TEXTURE_BOTTOM = new ScreenUtils.Image(Starcatcher.rl("textures/gui/tackle_box_tooltip_fishes_bottom.png"), 132, 8);
    public static final ScreenUtils.Image BOBBER = new ScreenUtils.Image(Starcatcher.rl("textures/item/background/bobber_white.png"), 16, 16);
    public static final ScreenUtils.Image BAIT = new ScreenUtils.Image(Starcatcher.rl("textures/item/background/bait_white.png"), 16, 16);
    public static final ScreenUtils.Image HOOK = new ScreenUtils.Image(Starcatcher.rl("textures/item/background/hook_white.png"), 16, 16);


    final List<Utils.Duo<Integer, MaybeStack>> items;
    final List<MaybeStack> fishes;
    final ItemStack rod;
    final ItemStack bobber;
    final ItemStack hook;
    final ItemStack bait;
    int rows;

    public TackleBoxTooltipRenderer(TackleBoxBlockItem.TackleBoxTooltip tooltip)
    {
        ItemStack box = tooltip.box();
        List<Utils.Duo<Integer, MaybeStack>> allItems = SCDataComponents.getOrDefault(box, SCDataComponents.TACKLE_BOX_ITEMS, List.of());
        items = allItems.stream().filter(o -> o.first() > 4).filter(o -> !o.second().isEmpty()).toList();
        fishes = new ArrayList<>(SCDataComponents.getOrDefault(box, SCDataComponents.TACKLE_BOX_FISHES, List.of()));

        //get rod or empty
        ItemStack fishInSlot = allItems.stream()
                .filter(o -> o.first() == TackleBoxContainer.FISH_SLOT)
                .map(Utils.Duo::second)
                .map(MaybeStack::toStack)
                .findAny()
                .orElse(ItemStack.EMPTY);

        if (!fishInSlot.isEmpty())
            fishes.addFirst(new MaybeStack(fishInSlot));

        //get rod or empty
        rod = allItems.stream()
                .filter(o -> o.first() == TackleBoxContainer.ROD_SLOT)
                .map(Utils.Duo::second)
                .map(MaybeStack::toStack)
                .findAny()
                .orElse(ItemStack.EMPTY);

        bobber = SCDataComponents.getOrDefault(rod, SCDataComponents.BOBBER, MaybeStack.EMPTY).toStack();
        bait = SCDataComponents.getOrDefault(rod, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack();
        hook = SCDataComponents.getOrDefault(rod, SCDataComponents.HOOK, MaybeStack.EMPTY).toStack();

        rows = fishes.size() / 16 + 1;
    }

    @Override
    public int getHeight(Font font)
    {
        int height = 0;

        if (!items.isEmpty())
            height += 45;

        if (!rod.isEmpty())
            height += 20;

        if (!fishes.isEmpty())
            height += rows * 16 + 10;

        return height;
    }

    @Override
    public int getWidth(Font font)
    {
        int width = 0;

        if (!rod.isEmpty())
            width = 74;

        if (!fishes.isEmpty())
            width = 133;

        if (!items.isEmpty())
            width = 133;

        return width;
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor g)
    {
        int yOffset = 0;

        //render rod
        if (!rod.isEmpty())
        {
            ROD_TEXTURE.render(g, x, y);

            ScreenUtils.item(g, rod, x + 1, y + 1);
            g.itemDecorations(font, rod, x + 1 + 1, y + 1);

            if (bobber.isEmpty())
                BOBBER.render(g, x + 1 + 18, y + 1);
            else
            {
                ScreenUtils.item(g, bobber, x + 1 + 18, y + 1);
                g.itemDecorations(font, bobber, x + 1 + 2, y + 1);
            }

            if (bait.isEmpty())
                BAIT.render(g, x + 18 + 1 + 18, y + 1);
            else
            {
                ScreenUtils.item(g, bait, x + 18 + 1 + 18, y + 1);
                g.itemDecorations(font, bait, x + 18 + 1 + 18, y + 1);
            }

            if (hook.isEmpty())
                HOOK.render(g, x + 18 + 18 + 1 + 18, y + 1);
            else
            {
                ScreenUtils.item(g, hook, x + 18 + 18 + 1 + 18, y + 1);
                g.itemDecorations(font, hook, x + 18 + 18 + 1 + 18, y + 1);
            }
            yOffset += 20;
        }

        //render fish
        if (!items.isEmpty())
        {
            BOX_TEXTURE.render(g, x, y + yOffset);
            for (Utils.Duo<Integer, MaybeStack> item : items)
            {
                Integer slot = item.first();
                yOffset = slot == 12 ? yOffset + 18 : yOffset;
                if (slot < 5) continue;
                ItemStack stack = item.second().toStack();
                ScreenUtils.item(g, stack, x + 4 + (((slot - 5) % 7) * 18), y + 4 + yOffset);
                g.itemDecorations(font, stack, x + 18 + 18 + 2, y + 1);
            }
            yOffset += 28;
        }

        if (!fishes.isEmpty())
        {
            FISHES_TEXTURE_TOP.render(g, x, y + yOffset);

            int sizeOfMiddle = 16 * rows - 8;

            new ScreenUtils.Image(FISHES_TEXTURE_MIDDLE, 132, sizeOfMiddle)
                    .render(g, x, y + yOffset + 8);

            FISHES_TEXTURE_BOTTOM.render(g, x, y + yOffset + sizeOfMiddle + 8);

            for (int i = 0; i < fishes.size(); i++)
            {
                ItemStack stack = fishes.get(i).toStack();
                int xO = i % 16 * 7;
                int yO = i / 16 * 16;
                ScreenUtils.item(g, stack, x + 4 + xO, y + 4 + yOffset + yO);
            }
        }
    }
}
