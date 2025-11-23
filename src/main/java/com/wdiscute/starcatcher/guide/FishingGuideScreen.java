package com.wdiscute.starcatcher.guide;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.Tooltips;
import com.wdiscute.starcatcher.blocks.ModBlocks;
import com.wdiscute.starcatcher.networkandcodecs.*;
import com.wdiscute.starcatcher.secretnotes.NoteContainer;
import com.wdiscute.starcatcher.secretnotes.SecretNoteScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.awt.*;
import java.util.*;
import java.util.List;

public class FishingGuideScreen extends Screen
{
    private static final ResourceLocation BACKGROUND_INDEX_FIRST = Starcatcher.rl("textures/gui/guide/background_index_first.png");
    private static final ResourceLocation BACKGROUND_INDEX_SECOND = Starcatcher.rl("textures/gui/guide/background_index_second.png");
    private static final ResourceLocation BACKGROUND_ENTRY = Starcatcher.rl("textures/gui/guide/background_entry.png");
    private static final ResourceLocation BACKGROUND_BASICS = Starcatcher.rl("textures/gui/guide/background_basics.png");

    private static final ResourceLocation FISHES_IN_AREA_TOP_RIGHT_DECORATION = Starcatcher.rl("textures/gui/guide/fishes_in_area_top_right_decoration.png");
    private static final ResourceLocation FISHES_IN_AREA_BOTTOM_LEFT_DECORATION = Starcatcher.rl("textures/gui/guide/fishes_in_area_bottom_left_decoration.png");
    private static final ResourceLocation FISHES_IN_AREA_FISH_DECORATION = Starcatcher.rl("textures/gui/guide/fishes_in_area_fish_decoration.png");

    private static final ResourceLocation ALL_FISHES_1_ROW = Starcatcher.rl("textures/gui/guide/all_fishes_1_rows.png");
    private static final ResourceLocation ALL_FISHES_2_ROW = Starcatcher.rl("textures/gui/guide/all_fishes_2_rows.png");
    private static final ResourceLocation ALL_FISHES_3_ROW = Starcatcher.rl("textures/gui/guide/all_fishes_3_rows.png");
    private static final ResourceLocation ALL_FISHES_4_ROW = Starcatcher.rl("textures/gui/guide/all_fishes_4_rows.png");

    private static final ResourceLocation HELP_PAGE_1 = Starcatcher.rl("textures/gui/guide/help_1.png");
    private static final ResourceLocation HELP_PAGE_2 = Starcatcher.rl("textures/gui/guide/help_2.png");
    private static final ResourceLocation HELP_PAGE_3 = Starcatcher.rl("textures/gui/guide/help_3.png");
    private static final ResourceLocation HELP_PAGE_4 = Starcatcher.rl("textures/gui/guide/help_4.png");
    private static final ResourceLocation HELP_PAGE_5 = Starcatcher.rl("textures/gui/guide/help_5.png");

    private static final ResourceLocation ARROW_PREVIOUS = Starcatcher.rl("textures/gui/guide/arrow_previous.png");
    private static final ResourceLocation ARROW_PREVIOUS_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_previous_pressed.png");
    private static final ResourceLocation ARROW_PREVIOUS_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_previous_highlight.png");

    private static final ResourceLocation ARROW_NEXT = Starcatcher.rl("textures/gui/guide/arrow_next.png");
    private static final ResourceLocation ARROW_NEXT_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_next_pressed.png");
    private static final ResourceLocation ARROW_NEXT_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_next_highlight.png");

    private static final ResourceLocation ARROW_INDEX = Starcatcher.rl("textures/gui/guide/arrow_index.png");
    private static final ResourceLocation ARROW_INDEX_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_index_pressed.png");
    private static final ResourceLocation ARROW_INDEX_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_index_highlight.png");

    private static final ResourceLocation NEW_FISH = Starcatcher.rl("textures/gui/guide/new_fish.png");
    private static final ResourceLocation STAR = Starcatcher.rl("textures/gui/guide/star.png");
    private static final ResourceLocation GLOW = Starcatcher.rl("textures/gui/guide/glow.png");

    private static final int MAX_HELP_PAGES = 4;

    final boolean advancedTooltips;

    private final ItemStack basics;
    private final ItemStack treasures;

    private final ItemStack ironHook;
    private final ItemStack shinyHook;
    private final ItemStack goldHook;
    private final ItemStack mossyHook;
    private final ItemStack crystalHook;
    private final ItemStack heavyHook;
    private final ItemStack stoneHook;
    private final ItemStack splitHook;
    private final ItemStack stabHook;

    private final ItemStack frugalBobber;
    private final ItemStack creeperBobber;
    private final ItemStack glitterBobber;
    private final ItemStack colorfulBobber;
    private final ItemStack steadyBobber;
    private final ItemStack impatientBobber;
    private final ItemStack frogBobber;
    private final ItemStack kimbeBobber;
    private final ItemStack clearBobber;

    private final ItemStack cherryBait;
    private final ItemStack lushBait;
    private final ItemStack sculkBait;
    private final ItemStack dripstoneBait;
    private final ItemStack murkwaterBait;
    private final ItemStack legendaryBait;
    private final ItemStack meteorologicalBait;

    private final ItemStack fishSpotter;

    private final ItemStack trophies;
    private final ItemStack secrets;

    private final ItemStack settings;

    int uiX;
    int uiY;

    int imageWidth;
    int imageHeight;

    int clickedX;
    int clickedY;

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
    List<TrophyProperties> trophiesTps = new ArrayList<>();
    List<TrophyProperties> secretsTps = new ArrayList<>();
    List<FishProperties> fishInArea = new ArrayList<>();
    List<FishCaughtCounter> fishCaughtCounterList = new ArrayList<>();

    TrophyProperties.RarityProgress all = new TrophyProperties.RarityProgress(0, DataAttachments.get(Minecraft.getInstance().player).fishesCaught().size() - 1); //-1 to remove the default
    TrophyProperties.RarityProgress common = new TrophyProperties.RarityProgress(0, -1);
    TrophyProperties.RarityProgress uncommon = TrophyProperties.RarityProgress.DEFAULT;
    TrophyProperties.RarityProgress rare = TrophyProperties.RarityProgress.DEFAULT;
    TrophyProperties.RarityProgress epic = TrophyProperties.RarityProgress.DEFAULT;
    TrophyProperties.RarityProgress legendary = TrophyProperties.RarityProgress.DEFAULT;

    public enum Sort
    {
        ALPHABETICAL_UP("gui.guide.sort.alphabetical_up"),
        ALPHABETICAL_DOWN("gui.guide.sort.alphabetical_down"),
        MOD_UP("gui.guide.sort.mod_up"),
        MOD_DOWN("gui.guide.sort.mod_down"),
        RARITY_UP("gui.guide.sort.rarity_up"),
        RARITY_DOWN("gui.guide.sort.rarity_down"),
        CAUGHT_UP("gui.guide.sort.caught_up"),
        CAUGHT_DOWN("gui.guide.sort.caught_down"),
        FLUID_UP("gui.guide.sort.fluid_up"),
        FLUID_DOWN("gui.guide.sort.fluid_down");

        private static final Sort[] vals = values();

        private final String translationKey;

        String getTranslationKey()
        {
            return this.translationKey;
        }

        Sort(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public Sort previous()
        {
            if (this.ordinal() == 0) return vals[vals.length - 1];
            return vals[(this.ordinal() - 1) % vals.length];
        }

        public Sort next()
        {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    private void sortEntries()
    {
        Sort sort = Config.SORT.get();

        //rarity
        if (sort.equals(Sort.RARITY_DOWN) || sort.equals(Sort.RARITY_UP))
        {
            List<FishProperties> entriesSorted = new ArrayList<>();

            entries.forEach(e -> {if (e.rarity().equals(FishProperties.Rarity.COMMON)) entriesSorted.add(e);});
            entries.forEach(e -> {if (e.rarity().equals(FishProperties.Rarity.UNCOMMON)) entriesSorted.add(e);});
            entries.forEach(e -> {if (e.rarity().equals(FishProperties.Rarity.RARE)) entriesSorted.add(e);});
            entries.forEach(e -> {if (e.rarity().equals(FishProperties.Rarity.EPIC)) entriesSorted.add(e);});
            entries.forEach(e -> {if (e.rarity().equals(FishProperties.Rarity.LEGENDARY)) entriesSorted.add(e);});

            if(!sort.equals(Sort.RARITY_UP)) Collections.reverse(entriesSorted);
            entries = entriesSorted;
        }

        //alphabetical
        if (sort.equals(Sort.ALPHABETICAL_DOWN) || sort.equals(Sort.ALPHABETICAL_UP))
        {
            List<FishProperties> entriesSorted = new ArrayList<>();
            Map<String, FishProperties> map = new HashMap<>();
            List<String> entriesString = new ArrayList<>();

            for (FishProperties fp : entries)
            {
                String path = fp.fish().unwrapKey().get().location().getPath();
                map.put(path, fp);
                entriesString.add(path);
            }

            entriesString = entriesString.stream().sorted().toList();

            for (String s : entriesString) entriesSorted.add(map.get(s));

            if(!sort.equals(Sort.ALPHABETICAL_UP)) Collections.reverse(entriesSorted);
            entries = entriesSorted;
        }

        //mod
        if (sort.equals(Sort.MOD_DOWN) || sort.equals(Sort.MOD_UP))
        {
            List<FishProperties> entriesSorted = new ArrayList<>();
            Map<String, FishProperties> map = new HashMap<>();
            List<String> entriesString = new ArrayList<>();

            for (FishProperties fp : entries)
            {
                String namespace = fp.fish().unwrapKey().get().location().getPath();
                map.put(namespace, fp);
                entriesString.add(namespace);
            }

            entriesString = entriesString.stream().sorted().toList();

            for (String s : entriesString) entriesSorted.add(map.get(s));

            if(!sort.equals(Sort.MOD_UP)) Collections.reverse(entriesSorted);
            entries = entriesSorted;
        }

        //fluid
        if (sort.equals(Sort.FLUID_DOWN) || sort.equals(Sort.FLUID_UP))
        {
            Config.SORT.set(Sort.ALPHABETICAL_UP);
            Config.SORT.save();
            sortEntries();
            Config.SORT.set(sort);
            Config.SORT.save();
            List<FishProperties> entriesSorted = new ArrayList<>();
            List<FishProperties> entriesRemaining = new ArrayList<>(entries);

            while (!entriesRemaining.isEmpty())
            {
                ResourceLocation rlBeingSorted = entriesRemaining.get(0).wr().fluids().get(0);
                List<FishProperties> temp = new ArrayList<>(entriesRemaining);
                temp.forEach(e ->
                {
                    if(e.wr().fluids().get(0).equals(rlBeingSorted))
                    {
                        entriesSorted.add(e);
                        entriesRemaining.remove(e);
                    }
                });
            }

            if(!sort.equals(Sort.FLUID_UP)) Collections.reverse(entriesSorted);
            entries = entriesSorted;
        }

        //caught
        if (sort.equals(Sort.CAUGHT_UP) || sort.equals(Sort.CAUGHT_DOWN))
        {
            Config.SORT.set(Sort.ALPHABETICAL_UP);
            Config.SORT.save();
            sortEntries();
            Config.SORT.set(sort);
            Config.SORT.save();
            List<FishProperties> entriesSorted = new ArrayList<>();

            entries.forEach(fp ->
            {
                FishCaughtCounter fcc = null;
                for (FishCaughtCounter fccAll : fishCaughtCounterList)
                {
                    if (fp.equals(fccAll.fp()))
                    {
                        fcc = fccAll;
                        break;
                    }
                }

                if(fcc != null) entriesSorted.add(fp);
            });


            entries.forEach(fp ->
            {
                FishCaughtCounter fcc = null;
                for (FishCaughtCounter fccAll : fishCaughtCounterList)
                {
                    if (fp.equals(fccAll.fp()))
                    {
                        fcc = fccAll;
                        break;
                    }
                }

                if(fcc == null) entriesSorted.add(fp);
            });

            if(!sort.equals(Sort.CAUGHT_UP)) Collections.reverse(entriesSorted);
            entries = entriesSorted;
        }

    }

    @Override
    protected void init()
    {
        super.init();

        entries = new ArrayList<>(999);
        trophiesTps = new ArrayList<>(999);
        secretsTps = new ArrayList<>(999);

        imageWidth = 420;
        imageHeight = 260;

        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;

        level = Minecraft.getInstance().level;
        player = Minecraft.getInstance().player;

        for (FishProperties fp : FishProperties.getFPs(level)) if (fp.hasGuideEntry()) entries.add(fp);
        sortEntries();

        for (TrophyProperties tp : level.registryAccess().registryOrThrow(Starcatcher.TROPHY_REGISTRY))
            if (tp.trophyType() == TrophyProperties.TrophyType.TROPHY) trophiesTps.add(tp);


        for (TrophyProperties tp : level.registryAccess().registryOrThrow(Starcatcher.TROPHY_REGISTRY))
        {
            if (tp.trophyType() == TrophyProperties.TrophyType.SECRET
                    && DataAttachments.get(player).trophiesCaught().contains(tp)) secretsTps.add(tp);
        }

        fishInArea = FishProperties.getFpsWithGuideEntryForArea(player);
        fishCaughtCounterList = DataAttachments.get(player).fishesCaught();

        //-1 on the common to account for the default "fish" unfortunately, theres probably a way to fix this
        all = new TrophyProperties.RarityProgress(0, DataAttachments.get(player).fishesCaught().size() - 1); //-1 to remove the default
        common = new TrophyProperties.RarityProgress(0, -1);
        uncommon = TrophyProperties.RarityProgress.DEFAULT;
        rare = TrophyProperties.RarityProgress.DEFAULT;
        epic = TrophyProperties.RarityProgress.DEFAULT;
        legendary = TrophyProperties.RarityProgress.DEFAULT;

        for (FishCaughtCounter fcc : DataAttachments.get(player).fishesCaught())
        {
            all = new TrophyProperties.RarityProgress(all.total() + fcc.count(), all.unique());

            if (fcc.fp().rarity() == FishProperties.Rarity.COMMON)
                common = new TrophyProperties.RarityProgress(common.total() + fcc.count(), common.unique() + 1);

            if (fcc.fp().rarity() == FishProperties.Rarity.UNCOMMON)
                uncommon = new TrophyProperties.RarityProgress(uncommon.total() + fcc.count(), uncommon.unique() + 1);

            if (fcc.fp().rarity() == FishProperties.Rarity.RARE)
                rare = new TrophyProperties.RarityProgress(rare.total() + fcc.count(), rare.unique() + 1);

            if (fcc.fp().rarity() == FishProperties.Rarity.EPIC)
                epic = new TrophyProperties.RarityProgress(epic.total() + fcc.count(), epic.unique() + 1);

            if (fcc.fp().rarity() == FishProperties.Rarity.LEGENDARY)
                legendary = new TrophyProperties.RarityProgress(legendary.total() + fcc.count(), legendary.unique() + 1);
        }

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
        if (x > 49 && x < 69 && y > 203 && y < 217)
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
        if (x > 336 && x < 356 && y > 202 && y < 216)
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
        if (x > 174 && x < 196 && y > 202 && y < 216)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 0;
            page = 0;
            return true;
        }

        if (button == 0)
        {
            clickedX = (int) mouseX;
            clickedY = (int) mouseY;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;

        int numberOfRows = (fishInArea.size() - 1) / 7 + 1;
        //sort
        if ((x > 168 && x < 191 && y > 121 && y < 128 && numberOfRows == 1) ||
                (x > 168 && x < 191 && y > 141 && y < 148 && numberOfRows == 2) ||
                (x > 168 && x < 191 && y > 161 && y < 168 && numberOfRows == 3) ||
                (x > 168 && x < 191 && y > 181 && y < 188 && numberOfRows == 4)
        )
        {
            if(button == 0) Config.SORT.set(Config.SORT.get().next());
            if(button == 1) Config.SORT.set(Config.SORT.get().previous());
            Config.SORT.save();
            sortEntries();
        }


        //previous arrow
        if (x > 49 && x < 69 && y > 203 && y < 217)
        {
            if (!(menu == 0 && page == 0))
            {
                arrowPreviousPressed = true;
            }
        }

        //next arrow
        if (x > 336 && x < 356 && y > 202 && y < 216)
        {
            if (page <= entries.size() / 2 - 1)
            {
                arrowNextPressed = true;
            }
        }

        //index arrow
        if (x > 174 && x < 196 && y > 202 && y < 216)
        {
            arrowIndexPressed = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        //render settings screen
        if (menu == -1)
        {
            Minecraft.getInstance().setScreen(
                    new SettingsScreen(
                            FishProperties.DEFAULT.withFish(ModItems.AURORA.getHolder().get()),
                            new ItemStack(ModItems.ROD.get()
                            )
                    ));
            return;
        }


        //render index
        if (menu == 0)
        {
            if (page == 0) renderImage(guiGraphics, BACKGROUND_INDEX_FIRST);
            else renderImage(guiGraphics, BACKGROUND_INDEX_SECOND);
            renderIndex(guiGraphics, mouseX, mouseY);
        }

        //render help page
        if (menu == 1)
        {
            renderImage(guiGraphics, BACKGROUND_BASICS);
            renderTheBasics(guiGraphics, mouseX, mouseY);
        }

        //render entries
        if (menu == 2)
        {
            renderImage(guiGraphics, BACKGROUND_ENTRY);
            renderEntry(guiGraphics, mouseX, mouseY, 52, page * 2);
            renderEntry(guiGraphics, mouseX, mouseY, 212, page * 2 + 1);
        }


        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //previous arrow and index should not render on first page of the book
        if (!(menu == 0 && page == 0))
        {
            //previous arrow
            if (x > 49 && x < 69 && y > 203 && y < 217)
                renderImage(guiGraphics, ARROW_PREVIOUS_HIGHLIGHT);
            renderImage(guiGraphics, arrowPreviousPressed ? ARROW_PREVIOUS_PRESSED : ARROW_PREVIOUS);

            //index
            if (x > 174 && x < 196 && y > 202 && y < 216)
                renderImage(guiGraphics, ARROW_INDEX_HIGHLIGHT);
            renderImage(guiGraphics, arrowIndexPressed ? ARROW_INDEX_PRESSED : ARROW_INDEX);
        }

        //next arrow
        if (page <= entries.size() / 2 - 1)
        {
            if (x > 336 && x < 356 && y > 202 && y < 216)
                renderImage(guiGraphics, ARROW_NEXT_HIGHLIGHT);
            renderImage(guiGraphics, arrowNextPressed ? ARROW_NEXT_PRESSED : ARROW_NEXT);
        }

        clickedX = 0;
        clickedY = 0;
    }

    private void renderHelpText(GuiGraphics guiGraphics)
    {
        for (int i = 0; i < 40; i++)
        {
            if (!I18n.exists("gui.guide.page" + page + ".left." + i)) break;

            Component comp = Tooltips.decodeTranslationKey("gui.guide.page" + page + ".left." + i).copy().withStyle(Style.EMPTY.withColor(0x635040));
            guiGraphics.drawString(this.font, comp, uiX + 52, uiY + 10 * i + 13, 0xff000000, false);
        }

        for (int i = 0; i < 40; i++)
        {
            if (!I18n.exists("gui.guide.page" + page + ".right." + i)) break;
            Component comp = Tooltips.decodeTranslationKey("gui.guide.page" + page + ".right." + i).copy().withStyle(Style.EMPTY.withColor(0x635040));
            guiGraphics.drawString(this.font, comp, uiX + 213, uiY + 10 * i + 13, 0xff000000, false);
        }
    }

    private void renderItemWithOutlineAndHover(GuiGraphics guiGraphics, ItemStack is, int x, int y, int mouseX, int mouseY)
    {
        renderItem(is, uiX + x, uiY + y, 1);
        //0xffe4e0d8 : 0xffc6bdaf
        guiGraphics.fill(uiX + x - 2, uiY + y - 2, uiX + x + 18, uiY + y + 18, 0xffb4a697);
        if (mouseX > uiX + x - 2 && mouseX < uiX + x - 2 + 20 && mouseY > uiY + y - 2 - 2 && mouseY < uiY + y - 2 + 20)
            guiGraphics.renderTooltip(this.font, is, mouseX, mouseY);
    }

    private void renderSecrets(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        for (int i = 0; i < secretsTps.size(); i++)
        {
            int rowSize = Math.min(6, (secretsTps.size() - i / 6 * 6));
            int x = 70 - rowSize * 23 / 2;

            int xrender = x + (i % 6) * 23;
            int y = i / 6 * 25;

            //offset to page
            xrender += uiX + 223;
            y += uiY + 110;

            TrophyProperties tp = secretsTps.get(i);

            ItemStack is;

            is = new ItemStack(tp.fp().fish());
            if (!tp.customName().isEmpty()) is.setHoverName(Component.literal(tp.customName()));
            DataComponents.setTrophyProperties(is, tp);


            guiGraphics.renderOutline(xrender - 10, y - 2, 20, 20, 0xff000000);
            renderItem(is, xrender - 8, y, 1);

            if (mouseX > xrender - 10 && mouseX < xrender + 10 && mouseY > y - 2 && mouseY < y + 18)
            {
                guiGraphics.renderTooltip(this.font, is, mouseX, mouseY);
            }

            if (clickedX > xrender - 10 && clickedX < xrender + 10 && clickedY > y - 2 && clickedY < y + 18)
            {
                if (is.getItem() instanceof NoteContainer nc)
                {
                    Minecraft.getInstance().setScreen(new SecretNoteScreen(nc.note));
                }

            }

        }
    }

    private void renderTrophies(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        for (int i = 0; i < trophiesTps.size(); i++)
        {
            int rowSize = Math.min(6, (trophiesTps.size() - i / 6 * 6));
            int x = 60 - rowSize * 23 / 2;

            int xrender = x + (i % 6) * 23;
            int y = i / 6 * 25;

            //offset to page
            xrender += uiX + 73;
            y += uiY + 120;

            TrophyProperties tp = trophiesTps.get(i);

            ItemStack is;
            boolean isMouseOnTop = mouseX > xrender - 10 && mouseX < xrender + 10 && mouseY > y - 2 && mouseY < y + 18;
            if (DataAttachments.get(player).trophiesCaught().contains(tp))
            {
                is = new ItemStack(tp.fp().fish());
                is.setHoverName(Component.literal(tp.customName()));
                DataComponents.setTrophyProperties(is, tp);
                if (isMouseOnTop)
                {
                    guiGraphics.renderTooltip(this.font, is, mouseX, mouseY);
                }
            }
            else
            {
                if (isMouseOnTop)
                {
                    List<Component> list = new ArrayList<>(List.of(Component.literal("Requirements:")));

                    if (tp.all().total() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.all.total")).append("[" + all.total() + "/" + tp.all().total() + "]"));
                    if (tp.all().unique() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.all.unique")).append("[" + all.unique() + "/" + tp.all().unique() + "]"));

                    if (tp.common().total() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.common.total")).append("[" + common.total() + "/" + tp.common().total() + "]"));
                    if (tp.common().unique() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.common.unique")).append("[" + common.unique() + "/" + tp.common().unique() + "]"));

                    if (tp.uncommon().total() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.uncommon.total")).append("[" + uncommon.total() + "/" + tp.uncommon().total() + "]"));
                    if (tp.uncommon().unique() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.uncommon.unique")).append("[" + uncommon.unique() + "/" + tp.uncommon().unique() + "]"));

                    if (tp.rare().total() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.rare.total")).append("[" + rare.total() + "/" + tp.rare().total() + "]"));
                    if (tp.rare().unique() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.rare.unique")).append("[" + rare.unique() + "/" + tp.rare().unique() + "]"));

                    if (tp.epic().total() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.epic.total")).append("[" + epic.total() + "/" + tp.epic().total() + "]"));
                    if (tp.epic().unique() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.epic.unique")).append("[" + epic.unique() + "/" + tp.epic().unique() + "]"));

                    if (tp.legendary().total() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.legendary.total")).append("[" + legendary.total() + "/" + tp.legendary().total() + "]"));
                    if (tp.legendary().unique() != 0)
                        list.add(Component.empty().append(Tooltips.decodeTranslationKey("gui.guide.trophy.legendary.unique")).append("[" + legendary.unique() + "/" + tp.legendary().unique() + "]"));

                    guiGraphics.renderTooltip(this.font, list, Optional.empty(), mouseX, mouseY);
                }
                is = new ItemStack(ModItems.MISSINGNO.get());
            }

            guiGraphics.renderOutline(xrender - 10, y - 2, 20, 20, 0xff000000);
            renderItem(is, xrender - 8, y, 1);


        }
    }

    private void renderTheBasics(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        renderHelpText(guiGraphics);

        if (page == 0)
        {
            renderImage(guiGraphics, HELP_PAGE_1);
            renderItem(basics, uiX + 166, uiY + 39, 1);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.basics"), uiX + 80, uiY + 45, 0xff000000, false);
        }

        if (page == 1)
        {
            renderImage(guiGraphics, HELP_PAGE_2);
            renderItem(treasures, uiX + 166, uiY + 39, 1);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.treasures"), uiX + 80, uiY + 45, 0xff000000, false);
        }

        if (page == 2)
        {
            renderImage(guiGraphics, HELP_PAGE_3);

            //hooks
            renderItem(ironHook, uiX + 166, uiY + 39, 1);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.hooks"), uiX + 80, uiY + 45, 0xff000000, false);

            renderItemWithOutlineAndHover(guiGraphics, ironHook, 56, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, goldHook, 84, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, shinyHook, 112, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, crystalHook, 140, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, heavyHook, 168, 157, mouseX, mouseY);

            renderItemWithOutlineAndHover(guiGraphics, mossyHook, 67, 182, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, splitHook, 99, 182, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, stoneHook, 131, 182, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, stabHook, 163, 182, mouseX, mouseY);

            //bobbers
            renderItem(frugalBobber, uiX + 321, uiY + 39, 1);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.bobbers"), uiX + 228, uiY + 45, 0xff000000, false);

            renderItemWithOutlineAndHover(guiGraphics, creeperBobber, 220, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, glitterBobber, 248, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, colorfulBobber, 276, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, frugalBobber, 304, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, clearBobber, 332, 157, mouseX, mouseY);

            renderItemWithOutlineAndHover(guiGraphics, steadyBobber, 233, 182, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, impatientBobber, 265, 182, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, frogBobber, 297, 182, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, kimbeBobber, 329, 182, mouseX, mouseY);
        }

        if (page == 3)
        {
            renderImage(guiGraphics, HELP_PAGE_4);

            //bait
            renderItem(cherryBait, uiX + 166, uiY + 39, 1);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.baits"), uiX + 80, uiY + 45, 0xff000000, false);

            renderItemWithOutlineAndHover(guiGraphics, cherryBait, 56, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, lushBait, 94, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, dripstoneBait, 132, 157, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, murkwaterBait, 170, 157, mouseX, mouseY);

            renderItemWithOutlineAndHover(guiGraphics, sculkBait, 76, 182, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, legendaryBait, 113, 182, mouseX, mouseY);
            renderItemWithOutlineAndHover(guiGraphics, meteorologicalBait, 150, 182, mouseX, mouseY);


            //gadgets
            renderItem(fishSpotter, uiX + 321, uiY + 39, 1);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.gadgets"), uiX + 228, uiY + 45, 0xff000000, false);
            renderItemWithOutlineAndHover(guiGraphics, fishSpotter, 276, 170, mouseX, mouseY);
        }

        if (page == 4)
        {
            renderImage(guiGraphics, HELP_PAGE_5);

            //trophies
            renderItem(trophies, uiX + 166, uiY + 39, 1);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.trophies"), uiX + 80, uiY + 45, 0xff000000, false);
            renderTrophies(guiGraphics, mouseX, mouseY);

            renderItem(secrets, uiX + 321, uiY + 39, 1);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.secrets"), uiX + 228, uiY + 45, 0xff000000, false);
            renderSecrets(guiGraphics, mouseX, mouseY);
        }
    }


    private void renderTheBasicsIndex(GuiGraphics guiGraphics, ItemStack is, int x, int y, int mouseX, int mouseY, String translationKey, int pageNr)
    {
        renderItem(is, x, y, 1);
        if (mouseX > x - 2 && mouseX < x + 17 && mouseY > y - 2 && mouseY < y + 17)
        {
            guiGraphics.renderTooltip(this.font, Component.translatable(translationKey), mouseX, mouseY);
        }
        if (clickedX > x - 2 && clickedX < x + 17 && clickedY > y - 2 && clickedY < y + 17)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 1;
            page = pageNr;
            if (pageNr == 5) menu = -1;
        }
    }

    private void renderIndex(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        int topLeftCorner = uiX + 53;
        int y = uiY + 47;

        int columnNumber = -1;
        int rowNumber = -1;
        int semiPageNumber = -page * 2;

        hasNextEntryPage = false;

        if (page == 0)
        {
            int auxX = topLeftCorner + 2;

            //all about fishing
            //The Basics
            renderTheBasicsIndex(guiGraphics, basics, auxX, y, mouseX, mouseY, "gui.guide.index.basics", 0);

            //Hooks
            auxX += 20;
            renderTheBasicsIndex(guiGraphics, ironHook, auxX, y, mouseX, mouseY, "gui.guide.index.hooks", 2);

            //Bobbers
            auxX += 20;
            renderTheBasicsIndex(guiGraphics, frugalBobber, auxX, y, mouseX, mouseY, "gui.guide.index.bobbers", 2);

            //baits
            auxX += 20;
            renderTheBasicsIndex(guiGraphics, cherryBait, auxX, y, mouseX, mouseY, "gui.guide.index.baits", 3);

            //fish spotter
            auxX += 20;
            renderTheBasicsIndex(guiGraphics, fishSpotter, auxX, y, mouseX, mouseY, "gui.guide.index.gadgets", 3);

            //trophies
            auxX += 20;
            renderTheBasicsIndex(guiGraphics, trophies, auxX, y, mouseX, mouseY, "gui.guide.index.trophies", 4);

            //settings
            auxX += 20;
            renderTheBasicsIndex(guiGraphics, settings, auxX, y, mouseX, mouseY, "gui.guide.index.settings", 5);
        }


        //render fishes in area
        rowNumber = 2;

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


            renderFishIndex(guiGraphics, topLeftCorner + (columnNumber * 20) + (semiPageNumber * 205), y - 2 + (rowNumber * 20), mouseX, mouseY, fp, 0xffc6bdaf);
        }

        //render decorations and stuff
        if (page == 0)
        {
            if (fishInArea.size() > 6) renderImage(guiGraphics, FISHES_IN_AREA_TOP_RIGHT_DECORATION);

            int numberOfRows = (fishInArea.size() - 1) / 7 + 1;

            int x = mouseX - uiX;
            int y2 = mouseY - uiY;

            if ((x > 168 && x < 191 && y2 > 121 && y2 < 128 && numberOfRows == 1) ||
                    (x > 168 && x < 191 && y2 > 141 && y2 < 148 && numberOfRows == 2) ||
                    (x > 168 && x < 191 && y2 > 161 && y2 < 168 && numberOfRows == 3) ||
                    (x > 168 && x < 191 && y2 > 181 && y2 < 188 && numberOfRows == 4)
            )
            {
                guiGraphics.renderTooltip(this.font, Component.translatable(Config.SORT.get().getTranslationKey()), mouseX, mouseY);
            }

            if (!fishInArea.isEmpty())
                renderImage(guiGraphics, FISHES_IN_AREA_BOTTOM_LEFT_DECORATION, 0, (numberOfRows - 1) * 20);

            if (columnNumber < 4) renderImage(guiGraphics, FISHES_IN_AREA_FISH_DECORATION, 0, (numberOfRows - 1) * 20);

            if (numberOfRows <= 1) renderImage(guiGraphics, ALL_FISHES_4_ROW, 0, 0);
            if (numberOfRows == 2) renderImage(guiGraphics, ALL_FISHES_3_ROW, 0, 0);
            if (numberOfRows == 3) renderImage(guiGraphics, ALL_FISHES_2_ROW, 0, 0);
            if (numberOfRows == 4) renderImage(guiGraphics, ALL_FISHES_1_ROW, 0, 0);
        }

        columnNumber = -1;
        rowNumber++;

        if (rowNumber > 6)
        {
            rowNumber = 0;
            semiPageNumber++;
        }

        //render all fishes
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

            renderFishIndex(guiGraphics, topLeftCorner + (columnNumber * 20) + (semiPageNumber * 159), y + 4 + (rowNumber * 20), mouseX, mouseY, fp, page == 0 && semiPageNumber == 0 ? 0xffe4e0d8 : 0xffc6bdaf);
        }

        //TODO COUNT NUMBER OF PAGES SO WHEN GOING BACK FROM ENTRIES TO INDEX IT GOES TO THE LAST PAGE OF INDEX
        if (semiPageNumber > 1) hasNextEntryPage = true;
    }

    private void renderFishIndex(GuiGraphics guiGraphics, int xOffset, int yOffset, int mouseX, int mouseY, FishProperties fp, int backgroundFillColor)
    {
        List<FishCaughtCounter> fishCounterList = DataAttachments.get(player).fishesCaught();
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

        //render fill
        guiGraphics.fill(xOffset - 1, yOffset - 1, xOffset + 17, yOffset + 17, backgroundFillColor);

        //glow color
        switch (fp.rarity())
        {
            case COMMON -> guiGraphics.setColor(1, 1, 1, 0);
            case UNCOMMON -> guiGraphics.setColor(0.7f, 1, 0.7f, 1);
            case RARE -> guiGraphics.setColor(0.2f, 0.4f, 0.7f, 0.7f);
            case EPIC -> guiGraphics.setColor(1f, 0, 1f, 0.5f);
            case LEGENDARY ->
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
        for (FishProperties fpNotif : DataAttachments.get(player).fishNotifications())
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
                components.add(Component.translatable("gui.guide.not_caught_yet").withStyle(Style.EMPTY.withColor(0xa34536)));
                components.add(Tooltips.decodeTranslationKey("gui.guide.rarity." + fp.rarity().getSerializedName()));
            }
            else
            {
                if (fp.customName().isEmpty())
                    components.add(Component.translatable(fp.fish().value().getDescriptionId()));
                else
                    components.add(Component.translatable("item.starcatcher." + fp.customName()));

                components.add(Component.translatable("gui.guide.caught").append(Component.literal("[" + caught + "]")).withStyle(Style.EMPTY.withColor(0x40752c)));
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

        //render caught: (always shown)
        //caught:
        guiGraphics.drawString(
                this.font, Component.translatable("gui.guide.caught"),
                uiX + xOffset + 73, uiY + 68, 0x9c897c, false);

        //render caught count
        if (fcc == null)
        {
            //------
            guiGraphics.drawString(
                    this.font, Component.translatable("gui.guide.not_caught"),
                    uiX + xOffset + 73, uiY + 78, 0x9c897c, false);
        }
        else
        {
            //[324]
            Component c = Component.literal("[" + fcc.count() + "]").withStyle(Style.EMPTY.withColor(0x635040));
            guiGraphics.drawString(this.font, Component.empty().append(c), uiX + xOffset + 73, uiY + 78, 0, false);
        }

        //render rarity (always shown)
        //rarity:
        guiGraphics.drawString(
                this.font, Component.translatable("gui.guide.rarity"),
                uiX + xOffset + 73, uiY + 90, 0x9c897c, false);
        //common
        guiGraphics.drawString(
                this.font, Tooltips.decodeTranslationKey("gui.guide.rarity." + fp.rarity().getSerializedName()),
                uiX + xOffset + 73, uiY + 100, 0, false);


        //render fish name
        if (fcc == null)
        {
            guiGraphics.drawString(
                    this.font, Component.translatable("gui.guide.not_caught_fish_name"),
                    uiX + xOffset + 30, uiY + 36, 0x635040, false);
        }
        else
        {
            MutableComponent compName;
            if (fp.customName().isEmpty())
                compName = Component.translatable(fp.fish().value().getDescriptionId());
            else
                compName = Component.translatable("item.starcatcher." + fp.customName());

            //todo fix this holy shit this has to be the worse hard coded offset possible omg wd why did you code it like this
            if (xOffset > 200)
                guiGraphics.drawString(this.font, compName, uiX + xOffset + 15, uiY + 36, 0x635040, false);
            else
                guiGraphics.drawString(this.font, compName, uiX + xOffset + 30, uiY + 36, 0x635040, false);
        }

        //render fish
        if (fcc != null) renderItem(is, uiX + xOffset + 26, uiY + 70);
        switch (fp.rarity())
        {
            case COMMON -> guiGraphics.setColor(1, 1, 1, 1);
            case UNCOMMON -> guiGraphics.setColor(0.7f, 1, 0.7f, 1);
            case RARE -> guiGraphics.setColor(0.2f, 0.4f, 0.7f, 0.7f);
            case EPIC -> guiGraphics.setColor(1f, 0, 1f, 0.5f);
            case LEGENDARY ->
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
        if (fcc != null) guiGraphics.blit(
                GLOW, uiX + xOffset + 10, uiY + 55,
                0, 0, 48, 48, 48, 48);
        RenderSystem.disableBlend();
        guiGraphics.setColor(1, 1, 1, 1);

        //render new fish icon
        if (DataAttachments.get(player).fishNotifications().contains(fp))
            renderImage(guiGraphics, NEW_FISH, xOffset - 52, 0);

        //render fish tooltip
        if (mouseX > uiX + xOffset + 0 && mouseX < uiX + xOffset + 65 && mouseY > uiY + 45 && mouseY < uiY + 110 && fcc != null)
        {
            guiGraphics.renderTooltip(this.font, is, mouseX, mouseY);
        }

        //render stats tooltip
        if (mouseX > uiX + xOffset + 66 && mouseX < uiX + xOffset + 140 && mouseY > uiY + 57 && mouseY < uiY + 110 && fcc != null)
        {
            List<Component> components = new ArrayList<>();
            float averageTicks = (int) ((fcc.averageTicks() / 20) * 100) / 100.0f;

            SettingsScreen.Units unit = Config.UNIT.get();
            String size = unit.getSizeAsString(fcc.size());
            String weight = unit.getWeightAsString(fcc.weight());

            components.add(Component.literal("Fastest Catch: ").append(Component.literal((((float) fcc.fastestTicks()) / 20) + "s").withStyle(ChatFormatting.BOLD)));
            components.add(Component.literal("Average Catch: ").append(Component.literal(averageTicks + "s").withStyle(ChatFormatting.BOLD)));
            components.add(Component.literal(""));
            components.add(Component.literal("Biggest Catch: ").append(Component.literal(size).withStyle(ChatFormatting.BOLD)));
            components.add(Component.literal("Heaviest Catch: ").append(Component.literal(weight).withStyle(ChatFormatting.BOLD)));

            guiGraphics.renderTooltip(this.font, components, Optional.empty(), mouseX, mouseY);
        }

        int yOffset = 121;

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
                    comp = Component.translatable("dimension." + fp.wr().dims().get(0).toLanguageKey());
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
                comp = comp.copy().withStyle(Style.EMPTY.withColor(0x40752c));
            }
            else
            {
                if (fp.wr().dims().contains(level.dimension().location()))
                {
                    comp = comp.copy().withStyle(Style.EMPTY.withColor(0x40752c));
                }
                else
                {
                    comp = comp.copy().withStyle(Style.EMPTY.withColor(0xa34536));
                }
            }


            Component start = Component.translatable("gui.guide.dimension");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0x635040, false);
        }

        //dimension blacklist
        {
            if (!fp.wr().dimsBlacklist().isEmpty())
            {
                guiGraphics.drawString(this.font, Component.literal("[!]").withStyle(Style.EMPTY.withColor(0xa34536)), uiX + xOffset + 160, uiY + yOffset, 0, false);

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

        yOffset += 12;

        List<ResourceLocation> biomesBL = FishProperties.getBiomesBlacklistAsList(fp, level);
        List<ResourceLocation> biomes = FishProperties.getBiomesAsList(fp, level);
        //biome:
        {
            MutableComponent comp;
            if (biomes.isEmpty())
            {
                comp = Component.translatable("gui.guide.no_restriction");
            }
            else
            {
                //if theres only one biome
                if (biomes.size() == 1)
                {
                    comp = Component.translatable("biome." + biomes.get(0).toLanguageKey());
                }
                else if (fp.wr().biomesTags().size() == 1)
                {
                    comp = Component.translatable("tag." + fp.wr().biomesTags().get(0).toLanguageKey());

                    //show tooltip while hovering
                    if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
                    {
                        List<Component> c = new ArrayList<>();

                        if (!fp.wr().biomesTags().isEmpty())
                        {
                            c.add(Component.translatable("gui.guide.biome_tags").withStyle(Style.EMPTY.withBold(true)));

                            for (ResourceLocation rl : fp.wr().biomesTags())
                                c.add(Component.translatable("tag." + rl.toLanguageKey()));
                            c.add(Component.empty());
                        }


                        c.add(Component.translatable("gui.guide.biomes").withStyle(Style.EMPTY.withBold(true)));

                        for (ResourceLocation rl : biomes)
                            c.add(Component.translatable("biome." + rl.toLanguageKey()));

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


            ResourceLocation rl = ResourceLocation.parse(level.getBiome(Minecraft.getInstance().player.blockPosition()).unwrapKey().get().location().toString());

            comp = comp.copy().withStyle(Style.EMPTY.withColor(0x40752c));

            if (!biomes.contains(rl) && !biomes.isEmpty())
            {
                comp = comp.copy().withStyle(Style.EMPTY.withColor(0xa34536));
            }

            if (biomesBL.contains(rl))
            {
                comp = comp.copy().withStyle(Style.EMPTY.withColor(0xa34536));
            }

            Component start = Component.translatable("gui.guide.biome");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0x635040, false);
        }

        //biome blacklist
        {
            if (!biomesBL.isEmpty())
            {
                guiGraphics.drawString(this.font, Component.literal("[!]").withStyle(Style.EMPTY.withColor(0xa34536)), uiX + xOffset + 130, uiY + yOffset - 1, 0, false);

                //show tooltip while hovering
                if (x > xOffset + 129 && x < xOffset + 140 && y > yOffset - 3 && y < yOffset + 8)
                {
                    List<Component> c = new ArrayList<>();

                    if (!fp.wr().biomesBlacklistTags().isEmpty())
                    {
                        c.add(Component.translatable("gui.guide.blacklisted_biome_tags").withStyle(Style.EMPTY.withBold(true)));

                        for (ResourceLocation rl : fp.wr().biomesBlacklistTags())
                            c.add(Component.translatable("tag." + rl.toLanguageKey()));
                        c.add(Component.empty());
                    }

                    c.add(Component.translatable("gui.guide.blacklisted_biomes").withStyle(Style.EMPTY.withBold(true)));

                    for (ResourceLocation rl : biomesBL)
                        c.add(Component.translatable("biome." + rl.toLanguageKey()));

                    guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                }
            }
        }

        yOffset += 12;

        if (fp.br().correctBait().isEmpty())
        {
            guiGraphics.drawString(
                    this.font,
                    Component.translatable("gui.guide.bait").append(Component.translatable("gui.guide.no_restriction")),
                    uiX + xOffset, uiY + yOffset, 0x635040, false);
        }
        else
        {
            ItemStack bait = new ItemStack(BuiltInRegistries.ITEM.get(fp.br().correctBait().get(0)));
            int bonus = fp.br().correctBaitChanceAdded() / fp.baseChance() * 100;
            Component extra = Component.literal(" (+" + bonus + "%)");

            if (bait.is(ModItems.LEGENDARY_BAIT.get()))
            {
                guiGraphics.drawString(
                        this.font,
                        Component.translatable("gui.guide.bait")
                                .append(Tooltips.RGBEachLetter(I18n.get(bait.getDescriptionId())))
                                .append(extra),
                        uiX + xOffset, uiY + yOffset, 0x635040, false);
            }
            else
            {
                guiGraphics.drawString(
                        this.font,
                        Component.translatable("gui.guide.bait")
                                .append(Component.translatable(bait.getDescriptionId()))
                                .append(extra),
                        uiX + xOffset, uiY + yOffset, 0x635040, false);
            }


            if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
            {
                guiGraphics.renderTooltip(this.font, bait, mouseX, mouseY);
            }
        }

        yOffset += 12;


        //weather
        {
            Component comp;

            if (fp.weather() == FishProperties.Weather.ALL)
            {
                comp = Component.translatable("gui.guide.no_restriction").withStyle(Style.EMPTY.withColor(0x635040));
            }
            else
            {
                comp = Component.translatable("gui.guide.no_restriction");
                if (fp.weather() == FishProperties.Weather.RAIN)
                {
                    if (level.getRainLevel(0) > 0.5)
                        comp = Component.translatable("gui.guide.raining").withStyle(Style.EMPTY.withColor(0x40752c));
                    else
                        comp = Component.translatable("gui.guide.raining").withStyle(Style.EMPTY.withColor(0xa34536));
                }

                if (fp.weather() == FishProperties.Weather.THUNDER)
                {
                    if (level.getThunderLevel(0) > 0.5)
                        comp = Component.translatable("gui.guide.thundering").withStyle(Style.EMPTY.withColor(0x40752c));
                    else
                        comp = Component.translatable("gui.guide.thundering").withStyle(Style.EMPTY.withColor(0xa34536));
                }

                if (fp.weather() == FishProperties.Weather.CLEAR)
                {
                    if (level.getRainLevel(0) > 0.5 || level.getThunderLevel(0) > 0.5)
                        comp = Component.translatable("gui.guide.clear").withStyle(Style.EMPTY.withColor(0xa34536));
                    else
                        comp = Component.translatable("gui.guide.clear").withStyle(Style.EMPTY.withColor(0x40752c));
                }
            }

            Component start = Component.translatable("gui.guide.weather");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0x635040, false);

        }


        yOffset += 12;


        //daytime
        {
            Component comp;

            if (fp.daytime() == FishProperties.Daytime.ALL)
            {
                comp = Component.translatable("gui.guide.no_restriction").withStyle(Style.EMPTY.withColor(0x635040));
            }
            else
            {
                long time = level.getDayTime() % 24000;

                comp = switch (fp.daytime())
                {
                    case DAY:
                        if (!(time > 23000 || time < 12700))
                            yield Component.translatable("gui.guide.day").withStyle(Style.EMPTY.withColor(0xa34536));
                        else
                            yield Component.translatable("gui.guide.day").withStyle(Style.EMPTY.withColor(0x40752c));

                    case NOON:
                        if (!(time > 3500 && time < 8500))
                            yield Component.translatable("gui.guide.noon").withStyle(Style.EMPTY.withColor(0xa34536));
                        else
                            yield Component.translatable("gui.guide.noon").withStyle(Style.EMPTY.withColor(0x40752c));

                    case NIGHT:
                        if (!(time < 23000 && time > 12700))
                            yield Component.translatable("gui.guide.night").withStyle(Style.EMPTY.withColor(0xa34536));
                        else
                            yield Component.translatable("gui.guide.night").withStyle(Style.EMPTY.withColor(0x40752c));

                    case MIDNIGHT:
                        if (!(time > 16500 && time < 19500))
                            yield Component.translatable("gui.guide.midnight").withStyle(Style.EMPTY.withColor(0xa34536));
                        else
                            yield Component.translatable("gui.guide.midnight").withStyle(Style.EMPTY.withColor(0x40752c));

                    default:
                        yield Component.empty();
                };


            }

            Component start = Component.translatable("gui.guide.daytime");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0x635040, false);
        }

        yOffset += 12;

        //elevation
        {
            int above = fp.wr().mustBeCaughtAboveY();
            int below = fp.wr().mustBeCaughtBelowY();

            String aboveBelow = above + ", " + below;

            //aboveBelow = "50, 100";
            MutableComponent hardCodedTranslations = switch (aboveBelow)
            {
                case "100, 2147483647" -> Component.translatable("gui.guide.mountain");
                case "50, 100", "50, 2147483647" -> Component.translatable("gui.guide.surface");
                case "-2147483648, 50" -> Component.translatable("gui.guide.underground");
                case "0, 50" -> Component.translatable("gui.guide.caves");
                case "-2147483648, 0" -> Component.translatable("gui.guide.deepslate");
                case "-2147483648, 2147483647" -> Component.translatable("gui.guide.no_restriction");

                default -> Component.literal("> " + above + ", < " + below);
            };

            //color the text
            if (player.getY() > above && player.getY() < below)
                hardCodedTranslations.withStyle(Style.EMPTY.withColor(0x40752c));
            else
                hardCodedTranslations.withStyle(Style.EMPTY.withColor(0xa34536));

            //tooltip only shows if a pre-defined named for the elevation range is used
            List<Component> hoverTooltip = new ArrayList<>(List.of());

            if (above > Integer.MIN_VALUE)
                hoverTooltip.add(Component.translatable("gui.guide.above").append("" + above));
            if (below < Integer.MAX_VALUE)
                hoverTooltip.add(Component.translatable("gui.guide.below").append("" + below));

            if (x > xOffset && x < xOffset + 140 && y > yOffset - 2 && y < yOffset + 10)
                guiGraphics.renderTooltip(this.font, hoverTooltip, Optional.empty(), mouseX, mouseY);

            guiGraphics.drawString(this.font, Component.translatable("gui.guide.elevation").append(hardCodedTranslations), uiX + xOffset, uiY + yOffset, 0x635040, false);
        }

        yOffset += 12;

        //fluid
        List<ResourceLocation> fluids = fp.wr().fluids();
        //if (!)
        {
            MutableComponent comp;
            if (fluids.size() == 1)
            {
                comp = Component.translatable("block." + fluids.get(0).toLanguageKey());
            }
            else
            {
                comp = Component.translatable("gui.guide.hover");

                //show tooltip while hovering
                if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
                {
                    List<Component> c = new ArrayList<>();
                    c.add(Component.translatable("gui.guide.fluid"));

                    for (ResourceLocation rl : fluids)
                    {
                        c.add(Component.translatable("block." + rl.toLanguageKey()));
                    }

                    guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                }
            }
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.fluid").append(comp), uiX + xOffset, uiY + yOffset, 0x635040, false);

        }


    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, 420, 260, 420, 260);
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl, int xOffset, int yOffset)
    {
        guiGraphics.blit(rl, uiX + xOffset, uiY + yOffset, 0, 0, 420, 260, 420, 260);
    }

    private void renderItem(ItemStack stack, int x, int y)
    {
        renderItem(stack, x, y, 3);
    }

    private void renderItem(ItemStack stack, int x, int y, float scale)
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
        Minecraft.getInstance().options.advancedItemTooltips = advancedTooltips;
        //todo send packet of fishes seen to remove notification
        //PacketDistributor.sendToServer(new Payloads.FPsSeen(fpsSeen));

        List<ResourceLocation> notifRLs = new ArrayList<>();

        Registry<FishProperties> fishProperties = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY);

        for (FishProperties fp : fpsSeen)
        {
            ResourceLocation rl = fishProperties.getKey(fp);

            if (rl == null) rl = Starcatcher.rl("missingno");

            notifRLs.add(rl);
        }

        Payloads.CHANNEL.send(
                PacketDistributor.SERVER.with(null),
                new Payloads.FishesSeenPayload(notifRLs)
        );

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
        heavyHook = new ItemStack(ModItems.HEAVY_HOOK.get());
        stoneHook = new ItemStack(ModItems.STONE_HOOK.get());
        splitHook = new ItemStack(ModItems.SPLIT_HOOK.get());
        stabHook = new ItemStack(ModItems.STABILIZING_HOOK.get());

        frugalBobber = new ItemStack(ModItems.FRUGAL_BOBBER.get());
        creeperBobber = new ItemStack(ModItems.CREEPER_BOBBER.get());
        glitterBobber = new ItemStack(ModItems.GLITTER_BOBBER.get());
        colorfulBobber = new ItemStack(ModItems.COLORFUL_BOBBER.get());
        steadyBobber = new ItemStack(ModItems.STEADY_BOBBER.get());
        impatientBobber = new ItemStack(ModItems.IMPATIENT_BOBBER.get());
        frogBobber = new ItemStack(ModItems.FROG_BOBBER.get());
        kimbeBobber = new ItemStack(ModItems.KIMBE_BOBBER.get());
        clearBobber = new ItemStack(ModItems.CLEAR_BOBBER.get());

        cherryBait = new ItemStack(ModItems.CHERRY_BAIT.get());
        lushBait = new ItemStack(ModItems.LUSH_BAIT.get());
        sculkBait = new ItemStack(ModItems.SCULK_BAIT.get());
        dripstoneBait = new ItemStack(ModItems.DRIPSTONE_BAIT.get());
        murkwaterBait = new ItemStack(ModItems.MURKWATER_BAIT.get());
        legendaryBait = new ItemStack(ModItems.LEGENDARY_BAIT.get());
        meteorologicalBait = new ItemStack(ModItems.METEOROLOGICAL_BAIT.get());


        fishSpotter = new ItemStack(ModItems.FISH_SPOTTER.get());
        trophies = new ItemStack(ModBlocks.TROPHY_GOLD.get());
        secrets = new ItemStack(ModItems.WATERLOGGED_BOTTLE.get());

        settings = new ItemStack(ModItems.SETTINGS.get());

        advancedTooltips = Minecraft.getInstance().options.advancedItemTooltips;
        Minecraft.getInstance().options.advancedItemTooltips = Minecraft.getInstance().player.isCreative() && advancedTooltips;
    }
}
