package com.wdiscute.starcatcher.guide;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.networkandcodecs.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FishingGuideScreen extends Screen
{

    private static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/guide/background.png");

    private static final ResourceLocation HELP_PAGE_1 = Starcatcher.rl("textures/gui/guide/help_1.png");
    private static final ResourceLocation HELP_PAGE_2 = Starcatcher.rl("textures/gui/guide/help_2.png");

    private static final ResourceLocation ARROW_PREVIOUS = Starcatcher.rl("textures/gui/guide/arrow_previous.png");
    private static final ResourceLocation ARROW_PREVIOUS_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_previous_pressed.png");
    private static final ResourceLocation ARROW_PREVIOUS_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_previous_highlight.png");

    private static final ResourceLocation ARROW_NEXT = Starcatcher.rl("textures/gui/guide/arrow_next.png");
    private static final ResourceLocation ARROW_NEXT_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_next_pressed.png");
    private static final ResourceLocation ARROW_NEXT_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_next_highlight.png");

    private static final ResourceLocation ARROW_INDEX = Starcatcher.rl("textures/gui/guide/arrow_index.png");
    private static final ResourceLocation ARROW_INDEX_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_index_pressed.png");
    private static final ResourceLocation ARROW_INDEX_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_index_highlight.png");

    private static final ResourceLocation STAR = Starcatcher.rl("textures/gui/guide/star.png");
    private static final ResourceLocation GLOW = Starcatcher.rl("textures/gui/guide/glow.png");

    private static final int MAX_HELP_PAGES = 4;

    private final ItemStack basics;
    private final ItemStack treasures;

    private final ItemStack ironHook;
    private final ItemStack shinyHook;
    private final ItemStack goldHook;
    private final ItemStack mossyHook;
    private final ItemStack crystalHook;
    private final ItemStack stoneHook;
    private final ItemStack splitHook;

    private final ItemStack frugalBobber;
    private final ItemStack creeperBobber;
    private final ItemStack glitterBobber;
    private final ItemStack colorfulBobber;
    private final ItemStack steadyBobber;
    private final ItemStack impatientBobber;
    private final ItemStack frogBobber;

    private final ItemStack fishSpotter;

    private final ItemStack trophies;
    private final ItemStack secrets;

    int uiX;
    int uiY;

    int imageWidth;
    int imageHeight;

    int clickedX;
    int clickedY;

    int clickedXDown;
    int clickedYDown;

    boolean arrowPreviousPressed;
    boolean arrowNextPressed;
    boolean arrowIndexPressed;

    int menu = 0;
    int page = 0;

    boolean hasNextEntryPage;

    ClientLevel level;
    LocalPlayer player;

    List<FishProperties> fpsSeen = new ArrayList<>();
    List<FishProperties> entries = new ArrayList<>(999);
    List<TrophyProperties> tps = new ArrayList<>();
    List<FishProperties> fishInArea = new ArrayList<>();
    List<FishCaughtCounter> fishCaughtCounterList = new ArrayList<>();


    @Override
    protected void init()
    {
        super.init();
        entries = new ArrayList<>(999);
        tps = new ArrayList<>(999);

        imageWidth = 512;
        imageHeight = 256;

        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;

        level = Minecraft.getInstance().level;
        player = Minecraft.getInstance().player;

        for (FishProperties fp : FishProperties.getFPs(level)) if (fp.hasGuideEntry()) entries.add(fp);
        for (TrophyProperties tp : level.registryAccess().registryOrThrow(Starcatcher.TROPHY_REGISTRY))
            if (tp.type() == TrophyProperties.TrophyType.TROPHY) tps.add(tp);

        fishInArea = FishProperties.getFpsWithGuideEntryForArea(player);
        fishCaughtCounterList = player.getData(ModDataAttachments.FISHES_CAUGHT);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(key))
        {
            this.onClose();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        arrowIndexPressed = false;
        arrowNextPressed = false;
        arrowPreviousPressed = false;

        //previous arrow
        if (x > 65 && x < 95 && y > 225 && y < 240)
        {
            //index <- previous page of index
            if (menu == 0 && page != 0)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                page--;
                return true;
            }

            //help -> index
            if (menu == 1 && page == 0)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                menu = 0;
                page = 0;
                return true;
            }

            //help -> previous help page
            if (menu == 1)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                page--;
                return true;
            }

            //entries -> last page of help
            if (menu == 2 && page == 0)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                menu = 1;
                page = MAX_HELP_PAGES;
                return true;
            }

            //entries -> previous entry
            if (menu == 2)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                page--;
                return true;
            }

        }

        //next arrow
        if (x > 420 && x < 440 && y > 230 && y < 240)
        {
            //index -> next page of index
            if (menu == 0 && hasNextEntryPage)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                page++;
                return true;
            }

            //index -> first page of help
            if (menu == 0)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                menu = 1;
                page = 0;
                return true;
            }

            //help -> next page of help
            if (menu == 1 && page != MAX_HELP_PAGES)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                page++;
                return true;
            }

            //index -> first page of help
            if (menu == 1)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                menu = 2;
                page = 0;
                return true;
            }

            //entries -> next entry
            if (menu == 2 && page <= entries.size() / 2 - 1)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                page++;
                return true;
            }

            //entries -> leaderboards??
            if (menu == 2)
            {
                //minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                //currentMenu = 3;
                //currentPage = 0;
                return true;
            }
        }

        //index arrow
        if (x > 225 && x < 245 && y > 223 && y < 243)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 0;
            page = 0;
            return true;
        }

        if (page == 0 && button == 0)
        {
            clickedX = (int) mouseX;
            clickedY = (int) mouseY;
            clickedXDown = 0;
            clickedYDown = 0;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //previous arrow
        if (x > 65 && x < 95 && y > 225 && y < 240)
        {
            if (!(menu == 0 && page == 0))
            {
                arrowPreviousPressed = true;
            }
        }

        //next arrow
        if (x > 420 && x < 440 && y > 230 && y < 240)
        {
            if (page <= entries.size() / 2 - 1)
            {
                arrowNextPressed = true;
            }
        }

        //index arrow
        if (x > 225 && x < 245 && y > 223 && y < 243)
        {
            arrowIndexPressed = true;
        }

        if (page == 0 && button == 0)
        {
            clickedXDown = (int) mouseX;
            clickedYDown = (int) mouseY;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderImage(guiGraphics, BACKGROUND);

        //render index
        if (menu == 0)
        {
            renderIndex(guiGraphics, mouseX, mouseY);
        }

        //render help page
        if (menu == 1)
        {
            renderTheBasics(guiGraphics, mouseX, mouseY);
        }

        //render entries
        if (menu == 2)
        {
            renderEntry(guiGraphics, mouseX, mouseY, 70, page * 2);
            renderEntry(guiGraphics, mouseX, mouseY, 276, page * 2 + 1);
        }


        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //previous arrow and index should not render on first page of the book
        if (!(menu == 0 && page == 0))
        {
            //previous arrow
            if (x > 65 && x < 95 && y > 225 && y < 240)
                guiGraphics.blit(ARROW_PREVIOUS_HIGHLIGHT, uiX + 65, uiY + 227, 0, 0, 23, 13, 23, 13);
            ResourceLocation previous = arrowPreviousPressed ? ARROW_PREVIOUS_PRESSED : ARROW_PREVIOUS;
            guiGraphics.blit(previous, uiX + 65, uiY + 227, 0, 0, 23, 13, 23, 13);

            //index
            if (x > 225 && x < 245 && y > 223 && y < 243)
                guiGraphics.blit(ARROW_INDEX_HIGHLIGHT, uiX + 225, uiY + 223, 0, 0, 20, 20, 20, 20);
            ResourceLocation index = arrowIndexPressed ? ARROW_INDEX_PRESSED : ARROW_INDEX;
            guiGraphics.blit(index, uiX + 225, uiY + 223, 0, 0, 20, 20, 20, 20);
        }

        //next arrow
        if (page <= entries.size() / 2 - 1)
        {
            if (x > 420 && x < 442 && y > 227 && y < 242)
                guiGraphics.blit(ARROW_NEXT_HIGHLIGHT, uiX + 420, uiY + 227, 0, 0, 23, 13, 23, 13);
            ResourceLocation next = arrowNextPressed ? ARROW_NEXT_PRESSED : ARROW_NEXT;
            guiGraphics.blit(next, uiX + 420, uiY + 227, 0, 0, 23, 13, 23, 13);

        }

        clickedX = 0;
        clickedY = 0;
    }


    private void renderHelpTitle(GuiGraphics guiGraphics, ItemStack is, Component comp, int x, int y)
    {
        renderItem(is, uiX + x + 10, uiY + y + 10, 2);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(2, 2, 2);
        guiGraphics.drawString(this.font, comp, uiX / 2 + x / 2 + 23, uiY / 2 + y / 2 + 6, 0xff000000, false);
        guiGraphics.pose().popPose();
        guiGraphics.renderOutline(uiX + x - 2, uiY + y - 2, 40, 40, 0xff000000);
        guiGraphics.renderOutline(uiX + x - 1, uiY + y - 1, 38, 38, 0xff000000);
    }

    private void renderHelpText(GuiGraphics guiGraphics)
    {
        for (int i = 0; i < 40; i++)
        {
            if (!I18n.exists("gui.guide.page" + page + ".left." + i)) break;

            Component comp = Tooltips.DecodeTranslationKeyTags("gui.guide.page" + page + ".left." + i);
            guiGraphics.drawString(this.font, comp, uiX + 70, uiY + 10 * i + 10, 0xff000000, false);
        }

        for (int i = 0; i < 40; i++)
        {
            if (!I18n.exists("gui.guide.page" + page + ".right." + i)) break;
            Component comp = Tooltips.DecodeTranslationKeyTags("gui.guide.page" + page + ".right." + i);
            guiGraphics.drawString(this.font, comp, uiX + 278, uiY + 10 * i + 10, 0xff000000, false);
        }
    }

    private void renderItemWithOutlineAndHover(GuiGraphics guiGraphics, ItemStack is, int x, int y, int mouseX, int mouseY)
    {
        renderItem(is, uiX + x, uiY + y, 1);
        guiGraphics.renderOutline(uiX + x - 2, uiY + y - 2, 20, 20, 0xff000000);
        if (mouseX > uiX + x - 2 && mouseX < uiX + x - 2 + 20 && mouseY > uiY + y - 2 - 2 && mouseY < uiY + y - 2 + 20)
            guiGraphics.renderTooltip(this.font, is, mouseX, mouseY);
    }

    private void renderTps(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        for (int i = 0; i < tps.size(); i++)
        {
            int rowSize = Math.min(7, (tps.size() - i / 7 * 7));
            int x = 90 - rowSize * 23 / 2;

            int xrender = x + (i % 7) * 23;
            int y = i / 7 * 25;

            //offset to page
            xrender += uiX + 280;
            y += uiY + 160;

            TrophyProperties tp = tps.get(i);

            ItemStack is;
            if (player.getData(ModDataAttachments.TROPHIES_CAUGHT).contains(tp))
            {
                is = new ItemStack(tp.baseItem());
                is.set(DataComponents.ITEM_NAME, Component.literal(tp.customName()));
                is.set(ModDataComponents.TROPHY, tp);
            }
            else
            {
                is = new ItemStack(ModItems.MISSINGNO.get());
            }

            guiGraphics.renderOutline(xrender - 10, y - 2, 20, 20, 0xff000000);
            renderItem(is, xrender - 8, y, 1);

            if (mouseX > xrender - 10 && mouseX < xrender + 10 && mouseY > y - 2 && mouseY < y + 18)
            {
                guiGraphics.renderTooltip(this.font, is, mouseX, mouseY);
            }


        }

    }

    private void renderTheBasics(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {

        renderHelpText(guiGraphics);

        if (page == 0)
        {
            renderImage(guiGraphics, HELP_PAGE_1);
            renderHelpTitle(guiGraphics, basics, Component.translatable("gui.guide.basics"), 70, 15);
        }

        if (page == 1)
        {
            renderImage(guiGraphics, HELP_PAGE_2);
            renderHelpTitle(guiGraphics, treasures, Component.translatable("gui.guide.treasures"), 70, 15);
        }

        if (page == 2)
        {
            renderHelpTitle(guiGraphics, ironHook, Component.translatable("gui.guide.hooks"), 70, 15);
            renderItemWithOutlineAndHover(guiGraphics, ironHook, 85, 170, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, goldHook, 125, 170, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, shinyHook, 165, 170, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, crystalHook, 205, 170, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, mossyHook, 105, 200, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, splitHook, 145, 200, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, stoneHook, 185, 200, mouseX, mouseY);

            renderHelpTitle(guiGraphics, frugalBobber, Component.translatable("gui.guide.bobbers"), 280, 15);
            renderItemWithOutlineAndHover(guiGraphics, creeperBobber, 288, 170, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, glitterBobber, 328, 170, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, colorfulBobber, 368, 170, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, frugalBobber, 408, 170, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, steadyBobber, 308, 200, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, impatientBobber, 348, 200, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, frogBobber, 388, 200, mouseX, mouseY);
        }

        if (page == 3)
        {
            //gadgets
            renderHelpTitle(guiGraphics, fishSpotter, Component.translatable("gui.guide.gadgets"), 70, 15);
            renderItemWithOutlineAndHover(guiGraphics, fishSpotter, 150, 170, mouseX, mouseY);

            //trophies
            renderHelpTitle(guiGraphics, trophies, Component.translatable("gui.guide.trophies"), 280, 15);
            renderTps(guiGraphics, mouseX, mouseY);
        }

        if (page == 4)
        {
            renderHelpTitle(guiGraphics, secrets, Component.translatable("gui.guide.secrets"), 70, 15);
            renderHelpText(guiGraphics);
        }
    }


    private void renderIndex(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        int x = uiX + 70;
        int y = uiY + 30;

        int columnNumber = -1;
        int rowNumber = -1;
        int semiPageNumber = -page * 2;

        hasNextEntryPage = false;


        //all about fishing
        //todo make this code not shitty
        guiGraphics.drawString(this.font, Component.translatable("gui.guide.fishing"), x + 25 + (columnNumber * 25) + (semiPageNumber * 205), y + 10 + (rowNumber * 25), 0, false);
        rowNumber++;

        int auxX;

        //fishing rod
        columnNumber++;
        auxX = x - 2 + (columnNumber * 25);
        guiGraphics.renderOutline(auxX, y - 2, 20, 20, 0xff000000);
        renderItem(new ItemStack(ModItems.ROD.get()), x + (columnNumber * 25), y, 1);
        if (mouseX > auxX && mouseX < auxX + 20 && mouseY > y - 2 && mouseY < y + 20)
        {
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.guide.basics"), mouseX, mouseY);
        }
        if (clickedX > auxX && clickedX < auxX + 20 && clickedY > y - 2 && clickedY < y + 20)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 1;
            page = 0;
        }

        //treasure
        columnNumber++;
        auxX = x - 2 + (columnNumber * 25);
        guiGraphics.renderOutline(auxX, y - 2, 20, 20, 0xff000000);
        renderItem(new ItemStack(ModItems.WATERLOGGED_SATCHEL.get()), x + (columnNumber * 25), y, 1);
        if (mouseX > auxX && mouseX < auxX + 20 && mouseY > y - 2 && mouseY < y + 20)
        {
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.guide.treasures"), mouseX, mouseY);
        }
        if (clickedX > auxX && clickedX < auxX + 20 && clickedY > y - 2 && clickedY < y + 20)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 1;
            page = 1;
        }

        //hooks
        columnNumber++;
        auxX = x - 2 + (columnNumber * 25);
        guiGraphics.renderOutline(auxX, y - 2, 20, 20, 0xff000000);
        renderItem(new ItemStack(ModItems.HOOK.get()), x + (columnNumber * 25), y, 1);
        if (mouseX > auxX && mouseX < auxX + 20 && mouseY > y - 2 && mouseY < y + 20)
        {
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.guide.hooks"), mouseX, mouseY);
        }
        if (clickedX > auxX && clickedX < auxX + 20 && clickedY > y - 2 && clickedY < y + 20)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 1;
            page = 2;
        }

        //bobbers
        columnNumber++;
        auxX = x - 2 + (columnNumber * 25);
        guiGraphics.renderOutline(auxX, y - 2, 20, 20, 0xff000000);
        renderItem(new ItemStack(ModItems.FRUGAL_BOBBER.get()), x + (columnNumber * 25), y, 1);
        if (mouseX > auxX && mouseX < auxX + 20 && mouseY > y - 2 && mouseY < y + 20)
        {
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.guide.bobbers"), mouseX, mouseY);
        }
        if (clickedX > auxX && clickedX < auxX + 20 && clickedY > y - 2 && clickedY < y + 20)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 1;
            page = 2;
        }

        //fish spotter
        columnNumber++;
        auxX = x - 2 + (columnNumber * 25);
        guiGraphics.renderOutline(auxX, y - 2, 20, 20, 0xff000000);
        renderItem(new ItemStack(ModItems.FISH_SPOTTER.get()), x + (columnNumber * 25), y, 1);
        if (mouseX > auxX && mouseX < auxX + 20 && mouseY > y - 2 && mouseY < y + 20)
        {
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.guide.gadgets"), mouseX, mouseY);
        }
        if (clickedX > auxX && clickedX < auxX + 20 && clickedY > y - 2 && clickedY < y + 20)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 1;
            page = 3;
        }

        //trophies
        columnNumber++;
        auxX = x - 2 + (columnNumber * 25);
        guiGraphics.renderOutline(auxX, y - 2, 20, 20, 0xff000000);
        renderItem(new ItemStack(ModItems.TROPHY_GOLD.get()), x + (columnNumber * 25), y, 1);
        if (mouseX > auxX && mouseX < auxX + 20 && mouseY > y - 2 && mouseY < y + 20)
        {
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.guide.trophies"), mouseX, mouseY);
        }
        if (clickedX > auxX && clickedX < auxX + 20 && clickedY > y - 2 && clickedY < y + 20)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 1;
            page = 3;
        }

        //secret messages
        columnNumber++;
        auxX = x - 2 + (columnNumber * 25);
        guiGraphics.renderOutline(auxX, y - 2, 20, 20, 0xff000000);
        renderItem(new ItemStack(ModItems.WATERLOGGED_BOTTLE.get()), x + (columnNumber * 25), y, 1);
        if (mouseX > auxX && mouseX < auxX + 20 && mouseY > y - 2 && mouseY < y + 20)
        {
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.guide.secrets"), mouseX, mouseY);
        }
        if (clickedX > auxX && clickedX < auxX + 20 && clickedY > y - 2 && clickedY < y + 20)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 1;
            page = 4;
        }


        //setup for next line
        rowNumber++;
        columnNumber = -1;

        //render fishes in area
        guiGraphics.drawString(this.font, Component.translatable("gui.guide.available"), x + 25 + (columnNumber * 25) + (semiPageNumber * 205), y + 10 + (rowNumber * 25), 0, false);
        rowNumber++;

        for (FishProperties fp : fishInArea)
        {
            columnNumber++;
            if (columnNumber > 6)
            {
                rowNumber++;
                columnNumber = 0;
            }

            if (rowNumber > 7)
            {
                semiPageNumber++;
                rowNumber = 0;
            }

            if (semiPageNumber > 1) break;
            if (semiPageNumber < 0) continue;

            renderFishIndex(guiGraphics, x + (columnNumber * 25) + (semiPageNumber * 205), y + (rowNumber * 25), mouseX, mouseY, fp);
        }

        columnNumber = -1;
        rowNumber++;

        if (rowNumber > 6)
        {
            rowNumber = 0;
            semiPageNumber++;
        }

        //render all fishes
        guiGraphics.drawString(this.font, Component.translatable("gui.guide.all"), x + 25 + (columnNumber * 25) + (semiPageNumber * 205), y + 10 + (rowNumber * 25), 0, false);
        rowNumber++;

        for (FishProperties fp : entries)
        {

            columnNumber++;
            if (columnNumber > 6)
            {
                rowNumber++;
                columnNumber = 0;
            }

            if (rowNumber > 7)
            {
                semiPageNumber++;
                rowNumber = 0;
            }

            if (semiPageNumber > 1) break;
            if (semiPageNumber < 0) continue;

            renderFishIndex(guiGraphics, x + (columnNumber * 25) + (semiPageNumber * 205), y + (rowNumber * 25), mouseX, mouseY, fp);
        }

        //TODO COUNT NUMBER OF PAGES SO WHEN GOING BACK FROM ENTRIES TO INDEX IT GOES TO THE LAST PAGE OF INDEX
        if (semiPageNumber > 1) hasNextEntryPage = true;
    }

    private void renderFishIndex(GuiGraphics guiGraphics, int xOffset, int yOffset, int mouseX, int mouseY, FishProperties fp)
    {
        List<FishCaughtCounter> fishCounterList = player.getData(ModDataAttachments.FISHES_CAUGHT);
        ItemStack is = new ItemStack(fp.fish());

        //calculate caught counter
        int caught = 0;
        for (FishCaughtCounter f : fishCounterList)
        {
            if (fp.equals(f.fp()))
            {
                caught = f.count();
                break;
            }
        }

        //handle click
        if (clickedX > xOffset - 3 && clickedX < xOffset + 21 - 3 && clickedY > yOffset - 3 && clickedY < yOffset + 21 - 3)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 2;
            page = entries.indexOf(fp) / 2;
        }

        //makes outline brighter when held down
        int outlineColor = 0xff112233;
        if (clickedXDown > xOffset - 3 && clickedXDown < xOffset + 21 - 3 && clickedYDown > yOffset - 3 && clickedYDown < yOffset + 21 - 3)
        {
            outlineColor = 0xff44aa44;
        }

        //outline
        guiGraphics.renderOutline(xOffset - 2, yOffset - 2, 20, 20, outlineColor);

        switch (fp.rarity())
        {
            case FishProperties.Rarity.COMMON -> guiGraphics.setColor(1, 1, 1, 1);
            case FishProperties.Rarity.UNCOMMON -> guiGraphics.setColor(0.7f, 1, 0.7f, 1);
            case FishProperties.Rarity.RARE -> guiGraphics.setColor(0.2f, 0.4f, 0.7f, 0.7f);
            case FishProperties.Rarity.EPIC -> guiGraphics.setColor(1f, 0, 1f, 0.5f);
            case FishProperties.Rarity.LEGENDARY ->
            {
                Color color = Color.getHSBColor(Tooltips.hue * 2, 1, 1);
                float r = (float) color.getRed() / 255;
                float g = (float) color.getGreen() / 255;
                float b = (float) color.getBlue() / 255;

                guiGraphics.setColor(r, g, b, 0.7f);
            }
        }

        //render glow
        RenderSystem.enableBlend();
        guiGraphics.blit(
                GLOW, xOffset - 1, yOffset - 1,
                0, 0, 18, 18, 18, 18);
        RenderSystem.disableBlend();
        guiGraphics.setColor(1, 1, 1, 1);

        //render item with missingno if not caught
        if (caught != 0)
            renderItem(is, xOffset, yOffset, 1);
        else
            renderItem(new ItemStack(ModItems.MISSINGNO.get()), xOffset, yOffset, 1);

        //render fish notification icon
        for (FishProperties fpNotif : player.getData(ModDataAttachments.FISHES_NOTIFICATION))
        {
            if (fp.equals(fpNotif))
                guiGraphics.blit(STAR, xOffset + 10, yOffset + 7, 0, 0, 10, 10, 10, 10);
        }

        //render tooltip
        if (mouseX > xOffset - 3 && mouseX < xOffset + 21 - 3 && mouseY > yOffset - 3 && mouseY < yOffset + 21 - 3)
        {
            List<Component> components = new ArrayList<>();

            if (caught == 0)
            {
                components.add(Component.translatable("gui.guide.not_caught_fish_name"));
                components.add(Component.translatable("gui.guide.not_caught").withColor(0xAA0000));
            }
            else
            {
                if (fp.customName().isEmpty())
                    components.add(Component.translatable("item." + fp.fish().getRegisteredName().replace(":", ".")));
                else
                    components.add(Component.translatable("item.starcatcher." + fp.customName()));

                components.add(Component.translatable("gui.guide.caught").append(Component.literal("[" + caught + "]")).withColor(0x00AA00));
            }

            guiGraphics.renderTooltip(this.font, components, Optional.empty(), mouseX, mouseY);
        }

    }


    private void renderEntry(GuiGraphics guiGraphics, int mouseX, int mouseY, int xOffset, int entry)
    {

        if (level == null) level = getMinecraft().level;

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        if (entries.size() <= entry) return;

        ItemStack is = new ItemStack(entries.get(entry).fish());
        FishProperties fp = entries.get(entry);

        if (!fpsSeen.contains(fp)) fpsSeen.add(fp);

        //get fishCaughtCount
        FishCaughtCounter fcc = null;
        for (FishCaughtCounter fccAll : fishCaughtCounterList)
        {
            if (fp.equals(fccAll.fp()))
            {
                fcc = fccAll;
                break;
            }
        }


        //render not caught text
        if (fcc == null)
        {
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.not_caught_fish_name"), uiX + xOffset + 46, uiY + 60, 0, false);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.not_caught").withColor(0xAA0000), uiX + xOffset + 46, uiY + 70, 0, false);
            renderItem(new ItemStack(ModItems.MISSINGNO.get()), uiX + xOffset + 10, uiY + 60);
        }
        else
        {
            //render fish name
            MutableComponent compName;
            if (fp.customName().isEmpty())
                compName = Component.translatable(fp.fish().value().getDescriptionId());
            else
                compName = Component.translatable("item.starcatcher." + fp.customName());

            //render fish name
            guiGraphics.drawString(this.font, compName, uiX + xOffset + 46, uiY + 55, 0, false);

            //render caught count
            Component c = Component.literal("[" + fcc.count() + "]").withColor(0x00AA00);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.caught").append(c).withColor(0x00AA00), uiX + xOffset + 46, uiY + 65, 0, false);

            //render rarity
            Component rarity = Tooltips.DecodeTranslationKeyTags("gui.guide.rarity." + fp.rarity().getSerializedName());
            guiGraphics.drawString(this.font, rarity, uiX + xOffset + 46, uiY + 75, 0, false);

            //render fish
            renderItem(is, uiX + xOffset + 10, uiY + 60);
            switch (fp.rarity())
            {
                case FishProperties.Rarity.COMMON -> guiGraphics.setColor(1, 1, 1, 1);
                case FishProperties.Rarity.UNCOMMON -> guiGraphics.setColor(0.7f, 1, 0.7f, 1);
                case FishProperties.Rarity.RARE -> guiGraphics.setColor(0.2f, 0.4f, 0.7f, 0.7f);
                case FishProperties.Rarity.EPIC -> guiGraphics.setColor(1f, 0, 1f, 0.5f);
                case FishProperties.Rarity.LEGENDARY ->
                {

                    Color color = Color.getHSBColor(Tooltips.hue, 1, 1);
                    float r = (float) color.getRed() / 255;
                    float g = (float) color.getGreen() / 255;
                    float b = (float) color.getBlue() / 255;

                    guiGraphics.setColor(r, g, b, 0.7f);
                }
            }

            //render glow
            RenderSystem.enableBlend();
            guiGraphics.blit(
                    GLOW, uiX + xOffset - 6, uiY + 40,
                    0, 0, 48, 48, 48, 48);
            RenderSystem.disableBlend();
            guiGraphics.setColor(1, 1, 1, 1);

            //render tooltip
            if (mouseX > uiX + xOffset + 46 && mouseX < uiX + xOffset + 150 && mouseY > uiY + 57 && mouseY < uiY + 80)
            {
                List<Component> components = new ArrayList<>();

                components.add(Component.literal("Fastest Catch: " + (float) (fcc.fastestTicks() / 20) + "s"));
                components.add(Component.literal("Average: " + (fcc.averageTicks() / 20) + "s"));

                guiGraphics.renderTooltip(this.font, components, Optional.empty(), mouseX, mouseY);
            }
        }

        int yOffset = 110;

        //dimension
        {
            Component comp;

            if (fp.wr().dims().isEmpty())
            {
                comp = Component.translatable("gui.guide.no_restriction");
            }
            else
            {
                //if theres only one dimension
                if (fp.wr().dims().size() == 1)
                {
                    comp = Component.translatable("dimension." + fp.wr().dims().getFirst().toLanguageKey());
                }
                else
                {
                    comp = Component.translatable("gui.guide.hover");

                    //show tooltip while hovering
                    if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
                    {
                        List<Component> c = new ArrayList<>();

                        c.add(Component.translatable("gui.guide.dimensions"));

                        for (int i = 0; i < fp.wr().dims().size(); i++)
                        {
                            c.add(Component.translatable("dimension." + fp.wr().dims().get(i).toLanguageKey()));
                        }
                        guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                    }
                }
            }

            if (fp.wr().dims().isEmpty())
            {
                comp = comp.copy().withColor(0x00AA00);
            }
            else
            {
                if (fp.wr().dims().contains(level.dimension().location()))
                {
                    comp = comp.copy().withColor(0x00AA00);
                }
                else
                {
                    comp = comp.copy().withColor(0xAA0000);
                }
            }


            Component start = Component.translatable("gui.guide.dimension");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0, false);
        }

        //dimension blacklist
        {
            if (!fp.wr().dimsBlacklist().isEmpty())
            {
                guiGraphics.drawString(this.font, Component.literal("[!]").withColor(0xAA0000), uiX + xOffset + 160, uiY + yOffset, 0, false);

                //show tooltip while hovering
                if (x > xOffset + 155 && x < xOffset + 175 && y > yOffset - 4 && y < yOffset + 12)
                {
                    List<Component> c = new ArrayList<>();

                    c.add(Component.translatable("gui.guide.blacklisted_dimensions"));

                    for (int i = 0; i < fp.wr().dimsBlacklist().size(); i++)
                    {
                        c.add(Component.literal(fp.wr().dimsBlacklist().get(i).toString()));
                    }
                    guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                }
            }
        }

        yOffset += 15;

        List<ResourceLocation> biomesBL = FishProperties.getBiomesBlacklistAsList(fp, level);
        List<ResourceLocation> biomes = FishProperties.getBiomesAsList(fp, level);
        //biome:
        {
            MutableComponent comp;
            if (biomes.isEmpty())
            {
                comp = Component.translatable("gui.guide.no_restriction");
                if (!biomesBL.isEmpty()) comp.append("*");
            }
            else
            {
                //if theres only one biome
                if (biomes.size() == 1)
                {
                    comp = Component.translatable("biome." + biomes.getFirst().toLanguageKey());
                }
                else if (fp.wr().biomesTags().size() == 1)
                {
                    comp = Component.translatable("tag." + fp.wr().biomesTags().getFirst().toLanguageKey());

                    //show tooltip while hovering
                    if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
                    {
                        List<Component> c = new ArrayList<>();
                        c.add(Component.translatable("gui.guide.biomes"));

                        for (ResourceLocation rl : biomes)
                        {
                            c.add(Component.translatable("biome." + rl.toLanguageKey()));
                        }

                        guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                    }
                }
                else
                {
                    comp = Component.translatable("gui.guide.hover");

                    //show tooltip while hovering
                    if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
                    {
                        List<Component> c = new ArrayList<>();
                        c.add(Component.translatable("gui.guide.biome"));

                        for (ResourceLocation rl : biomes)
                        {
                            c.add(Component.translatable("biome." + rl.toLanguageKey()));
                        }

                        guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                    }
                }
            }

            if (biomes.isEmpty())
            {
                comp = comp.copy().withColor(0x00AA00);
            }
            else
            {
                ResourceLocation rl = ResourceLocation.parse(level.getBiome(Minecraft.getInstance().player.blockPosition()).getRegisteredName());
                if (biomes.contains(rl))
                {
                    comp = comp.copy().withColor(0x00AA00);
                }
                else
                {
                    comp = comp.copy().withColor(0xAA0000);
                }
            }


            Component start = Component.translatable("gui.guide.biome");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0, false);
        }

        //biome blacklist
        {
            if (!biomesBL.isEmpty())
            {
                guiGraphics.drawString(this.font, Component.literal("[!]").withColor(0xAA0000), uiX + xOffset + 160, uiY + yOffset, 0, false);

                //show tooltip while hovering
                if (x > xOffset + 155 && x < xOffset + 175 && y > yOffset - 4 && y < yOffset + 12)
                {
                    List<Component> c = new ArrayList<>();

                    c.add(Component.translatable("gui.guide.blacklisted_biomes"));

                    for (ResourceLocation rl : biomesBL)
                        c.add(Component.translatable("biome." + rl.toLanguageKey()));

                    guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                }
            }
        }


        if (!fp.br().correctBait().isEmpty())
        {
            yOffset += 15;

            ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(fp.br().correctBait().getFirst()));
            guiGraphics.drawString(this.font, I18n.get("gui.guide.bait") + I18n.get(stack.getDescriptionId()), uiX + xOffset, uiY + yOffset, 0, false);

            if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
            {
                guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
            }
        }

        yOffset += 15;


        //weather
        {
            Component comp;

            if (fp.weather() == FishProperties.Weather.ALL)
            {
                comp = Component.translatable("gui.guide.no_restriction").withColor(0x00AA00);
            }
            else
            {
                comp = Component.translatable("gui.guide.no_restriction");
                if (fp.weather() == FishProperties.Weather.RAIN)
                {
                    if (level.getRainLevel(0) > 0.5)
                        comp = Component.translatable("gui.guide.raining").withColor(0x00AA00);
                    else
                        comp = Component.translatable("gui.guide.raining").withColor(0xAA0000);
                }

                if (fp.weather() == FishProperties.Weather.THUNDER)
                {
                    if (level.getThunderLevel(0) > 0.5)
                        comp = Component.translatable("gui.guide.thundering").withColor(0x00AA00);
                    else
                        comp = Component.translatable("gui.guide.thundering").withColor(0xAA0000);
                }

                if (fp.weather() == FishProperties.Weather.CLEAR)
                {
                    if (level.getRainLevel(0) > 0.5 || level.getThunderLevel(0) > 0.5)
                        comp = Component.translatable("gui.guide.clear").withColor(0xAA0000);
                    else
                        comp = Component.translatable("gui.guide.clear").withColor(0x00AA00);
                }
            }

            Component start = Component.translatable("gui.guide.weather");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0, false);

        }


        yOffset += 15;


        //daytime
        {
            Component comp;

            if (fp.daytime() == FishProperties.Daytime.ALL)
            {
                comp = Component.translatable("gui.guide.no_restriction").withColor(0x00AA00);
            }
            else
            {
                long time = level.getDayTime() % 24000;

                comp = switch (fp.daytime())
                {
                    case FishProperties.Daytime.DAY:
                        if (!(time > 23000 || time < 12700))
                            yield Component.translatable("gui.guide.day").withColor(0xAA0000);
                        else
                            yield Component.translatable("gui.guide.day").withColor(0x00AA00);

                    case FishProperties.Daytime.NOON:
                        if (!(time > 3500 && time < 8500))
                            yield Component.translatable("gui.guide.noon").withColor(0xAA0000);
                        else
                            yield Component.translatable("gui.guide.noon").withColor(0x00AA00);

                    case FishProperties.Daytime.NIGHT:
                        if (!(time < 23000 && time > 12700))
                            yield Component.translatable("gui.guide.night").withColor(0xAA0000);
                        else
                            yield Component.translatable("gui.guide.night").withColor(0x00AA00);

                    case FishProperties.Daytime.MIDNIGHT:
                        if (!(time > 16500 && time < 19500))
                            yield Component.translatable("gui.guide.midnight").withColor(0xAA0000);
                        else
                            yield Component.translatable("gui.guide.midnight").withColor(0x00AA00);

                    default:
                        yield Component.empty();
                };


            }

            Component start = Component.translatable("gui.guide.daytime");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0, false);
        }

        yOffset += 15;

        //elevation
        int above = fp.mustBeCaughtAboveY();
        int below = fp.mustBeCaughtBelowY();
        if (above != Integer.MIN_VALUE || below != Integer.MAX_VALUE)
        {
            MutableComponent belowAbove = Component.empty();

            if (above != Integer.MIN_VALUE)
                belowAbove.append(Component.translatable("gui.guide.above")).append("" + above);

            if (above != Integer.MIN_VALUE && below != Integer.MAX_VALUE)
                belowAbove.append(", ");

            if (below != Integer.MAX_VALUE)
                belowAbove.append(Component.translatable("gui.guide.below")).append("" + below);

            MutableComponent comp = belowAbove;

            if (above == 100 && below == Integer.MAX_VALUE)
                comp = Component.translatable("gui.guide.mountain");

            else if (above == 50 && below == 100)
                comp = Component.translatable("gui.guide.surface");

            else if (above == 0 && below == Integer.MAX_VALUE)
                comp = Component.translatable("gui.guide.surface");

            else if (above == Integer.MIN_VALUE && below == 50)
                comp = Component.translatable("gui.guide.underground");

            else if (above == 0 && below == 50)
                comp = Component.translatable("gui.guide.caves");

            else if (above == Integer.MIN_VALUE && below == 0)
                comp = Component.translatable("gui.guide.deepslate");


            //color the text
            if (player.getY() > above && player.getY() < below)
                comp.withColor(0x00AA00);
            else
                comp.withColor(0xAA0000);

            //tooltip only shows if a pre-defined named for the elevation range is used
            if (x > xOffset && x < xOffset + 140 && y > yOffset - 2 && y < yOffset + 10 && comp != belowAbove)
                guiGraphics.renderTooltip(this.font, belowAbove, mouseX, mouseY);

            guiGraphics.drawString(this.font, Component.translatable("gui.guide.elevation").append(comp), uiX + xOffset, uiY + yOffset, 0, false);

        }
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, 512, 256, 512, 256);
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl, int yOffset)
    {
        guiGraphics.blit(rl, uiX, uiY + yOffset, 0, 0, 512, 256, 512, 256);
    }

    private void renderItem(ItemStack stack, int x, int y)
    {
        renderItem(stack, x, y, 3);
    }

    private void renderItem(ItemStack stack, int x, int y, int scale)
    {

        Level level = Minecraft.getInstance().level;
        LivingEntity entity = Minecraft.getInstance().player;

        if (!stack.isEmpty())
        {
            BakedModel bakedmodel = this.minecraft.getItemRenderer().getModel(stack, level, entity, 234234);

            PoseStack pose = new PoseStack();

            pose.pushPose();
            pose.translate((float) (x + 8), (float) (y + 8), (float) (150));


            pose.scale(16F * scale, -16F * scale, 16F * scale);
            boolean usesBlockLight = !bakedmodel.usesBlockLight();
            if (usesBlockLight)
            {
                Lighting.setupForFlatItems();
            }

            this.minecraft.getItemRenderer().render(
                    stack, ItemDisplayContext.GUI, false, pose, Minecraft.getInstance().renderBuffers().bufferSource(),
                    15728880, OverlayTexture.NO_OVERLAY, bakedmodel);

            //flush()
            RenderSystem.disableDepthTest();
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            RenderSystem.enableDepthTest();

            if (usesBlockLight)
            {
                Lighting.setupFor3DItems();
            }

            pose.popPose();
        }

    }

    @Override
    public void onClose()
    {
        PacketDistributor.sendToServer(new Payloads.FPsSeen(fpsSeen));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    protected boolean shouldNarrateNavigation()
    {
        return false;
    }

    public FishingGuideScreen()
    {
        super(Component.empty());

        basics = new ItemStack(ModItems.ROD.get());
        treasures = new ItemStack(ModItems.WATERLOGGED_SATCHEL.get());

        ironHook = new ItemStack(ModItems.HOOK.get());
        shinyHook = new ItemStack(ModItems.SHINY_HOOK.get());
        goldHook = new ItemStack(ModItems.GOLD_HOOK.get());
        mossyHook = new ItemStack(ModItems.MOSSY_HOOK.get());
        crystalHook = new ItemStack(ModItems.CRYSTAL_HOOK.get());
        stoneHook = new ItemStack(ModItems.STONE_HOOK.get());
        splitHook = new ItemStack(ModItems.SPLIT_HOOK.get());

        frugalBobber = new ItemStack(ModItems.FRUGAL_BOBBER.get());
        creeperBobber = new ItemStack(ModItems.CREEPER_BOBBER.get());
        glitterBobber = new ItemStack(ModItems.GLITTER_BOBBER.get());
        colorfulBobber = new ItemStack(ModItems.COLORFUL_BOBBER.get());
        steadyBobber = new ItemStack(ModItems.STEADY_BOBBER.get());
        impatientBobber = new ItemStack(ModItems.IMPATIENT_BOBBER.get());
        frogBobber = new ItemStack(ModItems.FROG_BOBBER.get());

        fishSpotter = new ItemStack(ModItems.FISH_SPOTTER.get());
        trophies = new ItemStack(ModItems.TROPHY_GOLD.get());
        secrets = new ItemStack(ModItems.WATERLOGGED_BOTTLE.get());

    }
}
