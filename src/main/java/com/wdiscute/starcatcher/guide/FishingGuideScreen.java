package com.wdiscute.starcatcher.guide;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.starcatcher.*;
import com.wdiscute.starcatcher.data.SignedGuide;
import com.wdiscute.starcatcher.fish.*;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.data.network.SBTrackFishPayload;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.messageinabottle.message.MessageScreen;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.data.network.SignGuidePayload;
import com.wdiscute.starcatcher.data.network.SBFPsSeenPayload;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.utils.ScreenUtils;
import com.wdiscute.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix3x2fStack;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class FishingGuideScreen extends Screen
{
    private static final ScreenUtils.Image BACKGROUND_COVER = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/background_cover.png"), 420, 260);
    private static final ScreenUtils.Image BACKGROUND_LAST_PAGE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/background_last_page.png"), 420, 260);
    private static final ScreenUtils.Image BACKGROUND_INDEX_FIRST = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/background_index_first.png"), 420, 260);
    private static final ScreenUtils.Image BACKGROUND_INDEX_SECOND = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/background_index_second.png"), 420, 260);
    private static final ScreenUtils.Image BACKGROUND_ENTRY = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/background_entry.png"), 420, 260);
    private static final ScreenUtils.Image BACKGROUND_ENTRY_2 = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/background_entry_2.png"), 420, 260);
    private static final ScreenUtils.Image BACKGROUND_ENTRY_3 = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/background_entry_3.png"), 420, 260);
    private static final ScreenUtils.Image BACKGROUND_ENTRY_4 = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/background_entry_4.png"), 420, 260);
    private static final ScreenUtils.Image BACKGROUND_BASICS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/background_basics.png"), 420, 260);

    private static final ScreenUtils.Image COMPASS_CLOSED = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/compass_closed.png"), 420, 260);
    private static final ScreenUtils.Image COMPASS_OPEN = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/background/compass_open.png"), 420, 260);

    private static final ScreenUtils.Image COMPASS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/compass.png"), 32, 32);

    private static final ScreenUtils.Image HIGHLIGHT_LEFT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/highlight_page_left.png"), 420, 260);
    private static final ScreenUtils.Image HIGHLIGHT_RIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/highlight_page_right.png"), 420, 260);

    private static final ScreenUtils.Image FISHES_IN_AREA_TOP_RIGHT_DECORATION = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/fishes_in_area_top_right_decoration.png"), 420, 260);
    private static final ScreenUtils.Image FISHES_IN_AREA_BOTTOM_LEFT_DECORATION = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/fishes_in_area_bottom_left_decoration.png"), 420, 260);
    private static final ScreenUtils.Image FISHES_IN_AREA_BOTTOM_DECORATION = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/fishes_in_area_bottom_decoration.png"), 420, 260);
    private static final ScreenUtils.Image FISHES_IN_AREA_FISH_DECORATION = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/fishes_in_area_fish_decoration.png"), 420, 260);

    private static final ScreenUtils.Image HELP_PAGE_BASICS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_basics.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_SWEETSPOTS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_sweetspots.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_TREASURE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_treasure.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_UPGRADES = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_upgrades.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_TACKLE_BOX = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_tackle_box.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_HOOKS_BOBBERS_BAITS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_hooks_bobbers_baits.png"), 420, 260);

    private static final ScreenUtils.Image HELP_PAGE_COSMETICS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_cosmetics.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_TOURNAMENTS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_tournaments.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_MESSAGES = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_messages.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_SELLING = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_selling.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_AQUARIUM = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_aquarium.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_DISPLAY = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_display.png"), 420, 260);
    private static final ScreenUtils.Image HELP_PAGE_TROPHIES = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/help/help_trophies.png"), 420, 260);

    private static final ScreenUtils.Image ARROW_PREVIOUS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_previous.png"), 420, 260);
    private static final ScreenUtils.Image ARROW_PREVIOUS_PRESSED = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_previous_pressed.png"), 420, 260);
    private static final ScreenUtils.Image ARROW_PREVIOUS_HIGHLIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_previous_highlight.png"), 420, 260);

    private static final ScreenUtils.Image ARROW_LEFT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_left.png"), 16, 16);
    private static final ScreenUtils.Image ARROW_LEFT_HIGHLIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_left_highlight.png"), 16, 16);
    private static final ScreenUtils.Image ARROW_RIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_right.png"), 16, 16);
    private static final ScreenUtils.Image ARROW_RIGHT_HIGHLIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_right_highlight.png"), 16, 16);

    private static final ScreenUtils.Image ARROW_NEXT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_next.png"), 420, 260);
    private static final ScreenUtils.Image ARROW_NEXT_PRESSED = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_next_pressed.png"), 420, 260);
    private static final ScreenUtils.Image ARROW_NEXT_HIGHLIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_next_highlight.png"), 420, 260);

    private static final ScreenUtils.Image ARROW_INDEX = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_index.png"), 420, 260);
    private static final ScreenUtils.Image ARROW_INDEX_PRESSED = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_index_pressed.png"), 420, 260);
    private static final ScreenUtils.Image ARROW_INDEX_HIGHLIGHT = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/arrow_index_highlight.png"), 420, 260);

    private static final ScreenUtils.Image NEW_FISH = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/new_fish.png"), 32, 32);
    private static final ScreenUtils.Image GLOW = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/glow.png"), 18, 18);
    private static final ScreenUtils.Image TRACKED = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/tracked.png"), 48, 48);

    private static final ScreenUtils.Image BUCKET = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/bucketable.png"), 14, 14);
    private static final ScreenUtils.Image ENTITY = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/entity.png"), 14, 14);
    private static final ScreenUtils.Image ALWAYS_ENTITY = new ScreenUtils.Image(Starcatcher.rl("textures/gui/guide/always_entity.png"), 14, 14);

    public static final int MAX_HELP_PAGES = 12;

    private final List<ItemStack> tackleBoxes = new ArrayList<>();
    private final List<ItemStack> baits = new ArrayList<>();
    private final List<ItemStack> hooksBobbers = new ArrayList<>();
    private final List<ItemStack> cosmetics = new ArrayList<>();
    private final List<ItemStack> aquariumInteractions = new ArrayList<>();
    private final List<ItemStack> templates = new ArrayList<>();
    private final List<ItemStack> bottles;

    private final ItemStack missingnoStack;
    private static ItemStack rodIcon;
    private static ItemStack sweetspotsIcon;
    private static ItemStack treasureIcon;
    private static ItemStack standIcon;
    private static ItemStack letterIcon;
    private static ItemStack aquariumIcon;
    private static ItemStack displayIcon;
    private static ItemStack guideIcon;
    private static ItemStack sellingBinIcon;
    private static ItemStack trophyIcon;
    private static ItemStack cosmeticsIcon;
    private static ItemStack tackleBoxIcon;
    private static ItemStack hookIcon;
    private static ItemStack baitIcon;
    private static ItemStack templateIcon;
    private static ItemStack upgradeIcon;

    //stats
    private int trashCaught;
    private int commonCaught;
    private int uncommonCaught;
    private int rareCaught;
    private int epicCaught;
    private int legendaryCaught;

    private int goldenFishesCaught;
    private int perfectFishesCaught;

    private int fishesCaught;
    private int treasuresCaught;
    private int fishMissed;
    private int baitUsed;
    private int timeSpent;

    private final List<Pair<ItemStack, String>> indexEntries;

    int uiX;
    int uiY;

    int imageWidth;
    int imageHeight;

    boolean clicked;

    int leftPageScroll = 0;
    int rightPageScroll = 0;

    float highlightLeftAlpha = 0;
    float highlightRightAlpha = 0;

    int arrowPressedFromScrollDecay;
    boolean arrowPreviousPressed;
    boolean arrowNextPressed;
    boolean arrowIndexPressed;

    MenuEntry menu = MenuEntry.INDEX;
    int page = 0;

    public SignedGuide signedGuide;
    final BlockPos displayBP;

    boolean isCompassOpen = true;
    static float compassRotation = 0;

    ClientLevel level;
    LocalPlayer player;

    List<Identifier> fpsSeen = new ArrayList<>();
    List<FishProperties> entries = new ArrayList<>(999);
    List<FishProperties> trophies = new ArrayList<>(999);
    List<ItemStack> trophiesIS = new ArrayList<>(999);
    List<ItemStack> messages = new ArrayList<>(999);
    List<FishProperties> fishInArea = new ArrayList<>();
    Map<Identifier, FishCaughtCounter> fishCaughtCounterMap = new HashMap<>();

    EditBox signedNameEditBox;

    FishProperties trackedFP;
    Identifier trackedRL;

    @Override
    protected void init()
    {
        super.init();

        imageWidth = 420;
        imageHeight = 260;

        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;

        //editbox for cover
        this.signedNameEditBox = new EditBox(this.font, uiX + 240, uiY + 102, 103, 12, translatable("container.repair"));
        this.signedNameEditBox.setTextColor(0xff937d70);
        this.signedNameEditBox.setBordered(false);
        this.signedNameEditBox.setMaxLength(20);
        this.signedNameEditBox.setTextShadow(false);
        this.signedNameEditBox.setCanLoseFocus(false);
        this.signedNameEditBox.setValue(signedGuide == null ? "" : signedGuide.signature());
        this.addWidget(this.signedNameEditBox);
        this.signedNameEditBox.setEditable(signedGuide == null);

        entries = new ArrayList<>();

        level = Minecraft.getInstance().level;
        player = Minecraft.getInstance().player;

        fishInArea = new ArrayList<>();

        for (FishProperties fp : FishApi.getAllFPs(level))
            if (fp.hasGuideEntry() && fp.calculateChance(player, player.level(), ItemStack.EMPTY, AbstractFishRestriction.Context.GUIDE_FISHES_IN_AREA) > 0)
                fishInArea.add(fp);

        for (FishProperties fp : FishApi.getAllFPs(level)) if (fp.hasGuideEntry()) entries.add(fp);
        entries = sortEntries(SCConfig.SORT.get(), entries, player);
        fishInArea = sortEntries(SCConfig.SORT.get(), fishInArea, player);

        //set caught trophies
        trophies = FishApi.getTrophies(level);

        trophiesIS = new ArrayList<>();
        trophies.forEach(t ->
        {
            if (!SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get() || fishCaughtCounterMap.containsKey(level.registryAccess().lookupOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(t)))
                trophiesIS.add(t.catchInfo().fish().toStack());
            else
                trophiesIS.add(SCItems.MISSINGNO.asItem().getDefaultInstance());
        });

        if (trophies.isEmpty()) trophies = List.of(FishProperties.empty());
        if (trophiesIS.isEmpty()) trophiesIS = List.of(Items.BARRIER.getDefaultInstance());

        //set caught messages
        //built-in messages
        messages = new ArrayList<>(FishApi.getMessages(level).stream().map(o ->
                {
                    if (fishCaughtCounterMap.containsKey(FishApi.getRegistry(level).getKey(o)))
                    {
                        //create message itemstack with message data from FP
                        ItemStack stack = SCItems.MESSAGE.toStack();
                        SCDataComponents.set(stack, SCDataComponents.MESSAGE,
                                SCDataComponents.getOrDefault(o.catchInfo().fish().toStack(), SCDataComponents.MESSAGE, Message.DEFAULT));
                        return stack;
                    }
                    else
                        return SCItems.MISSINGNO.toStack();
                }
        ).toList()
        );

        //player written messages
        messages.addAll(SCDataAttachments.get(player, SCDataAttachments.MESSAGES_CAUGHT).stream().map(o ->
        {
            ItemStack stack = SCItems.MESSAGE.toStack();
            SCDataComponents.set(stack, SCDataComponents.MESSAGE, o);
            return stack;
        }).toList());

        //if there are no registered messages add an empty missingno
        if (messages.isEmpty())
            messages.add(Items.BARRIER.getDefaultInstance());

        //tracked fp
        resolveTrackedFP();

        for (int i = 0; i < entries.size(); i++)
        {
            if (entries.get(i).equals(trackedFP))
            {
                menu = MenuEntry.ENTRY;
                page = i / 2;
            }
        }
    }

    Identifier cachedRL = null;

    public void resolveTrackedFP()
    {
        //if needs refresh
        Identifier newTrackedRL = player.getData(SCDataAttachments.TRACKED_FISH);
        if (cachedFp == null || !newTrackedRL.equals(cachedRL) && player != null && level != null)
        {
            //set page to tracked fished when opening
            if (player != null && level != null)
            {
                trackedRL = newTrackedRL;
                trackedFP = level.registryAccess().lookupOrThrow(Starcatcher.FISH_REGISTRY_KEY).getValue(trackedRL);
            }
        }
        cachedRL = newTrackedRL;
        if (trackedFP == null)
            trackedFP = FishProperties.empty();
    }

    @Override
    public boolean keyPressed(KeyEvent event)
    {
        InputConstants.Key key = InputConstants.getKey(event);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(key) && !signedNameEditBox.canConsumeInput())
        {
            if (menu.equals(MenuEntry.INDEX))
                this.onClose();
            else
            {
                menu = MenuEntry.INDEX;
                page = 0;
            }
            return true;
        }

        if (event.key() == 256 && this.shouldCloseOnEsc())
        {
            if (menu.equals(MenuEntry.INDEX))
                this.onClose();
            else
            {
                menu = MenuEntry.INDEX;
                page = 0;
            }
            return true;
        }

        return super.keyPressed(event);
    }

    public void previousPage()
    {
        switch (menu)
        {
            case COVER ->
            {

            }

            case INDEX ->
            {
                player.playSound(SoundEvents.BOOK_PAGE_TURN);

                if (page == 0)
                    menu = MenuEntry.COVER;
                else
                    page--;
            }

            case HELP ->
            {
                player.playSound(SoundEvents.BOOK_PAGE_TURN);

                if (page == 0)
                {
                    page = entries.size() <= 49 ? 0 : 1 + (entries.size() - 50) / 98;
                    menu = MenuEntry.INDEX;
                }
                else
                    page--;
            }

            case ENTRY ->
            {
                player.playSound(SoundEvents.BOOK_PAGE_TURN);

                if (page == 0)
                {
                    if (SCConfig.ENABLE_GUIDE_HELP_PAGES.get())
                    {
                        menu = MenuEntry.HELP;
                        page = MAX_HELP_PAGES;
                    }
                    else
                    {
                        page = entries.size() <= 49 ? 0 : 1 + (entries.size() - 50) / 98;
                        menu = MenuEntry.INDEX;
                    }
                }
                else
                    page--;
            }

            case LAST ->
            {
                player.playSound(SoundEvents.BOOK_PAGE_TURN);
                page = entries.size() / 2 - 1;
                menu = MenuEntry.ENTRY;
            }

        }
    }

    public void nextPage()
    {
        switch (menu)
        {
            case COVER ->
            {
                player.playSound(SoundEvents.BOOK_PAGE_TURN);
                menu = MenuEntry.INDEX;
                page = 0;
            }

            case INDEX ->
            {
                player.playSound(SoundEvents.BOOK_PAGE_TURN);

                int entriesOnCurrentAndPreviousPages = (page == 0) ? 49 : 49 + (page * 98);

                //if there is next page
                if (entries.size() > entriesOnCurrentAndPreviousPages)
                {
                    page++;
                }
                else
                {
                    page = 0;
                    //if help is enabled, go to help
                    if (SCConfig.ENABLE_GUIDE_HELP_PAGES.get())
                        menu = MenuEntry.HELP;
                    else
                        menu = MenuEntry.ENTRY;
                }
            }

            case HELP ->
            {
                player.playSound(SoundEvents.BOOK_PAGE_TURN);
                if (page == MAX_HELP_PAGES)
                {
                    menu = MenuEntry.ENTRY;
                    page = 0;
                }
                else
                {
                    page++;
                }
            }

            case ENTRY ->
            {
                player.playSound(SoundEvents.BOOK_PAGE_TURN);

                int entriesOnCurrentAndPreviousPages = (page + 1) * 2;

                //if there's next page
                if (entries.size() > entriesOnCurrentAndPreviousPages)
                    page++;
                else
                {
                    menu = MenuEntry.LAST;
                    page = 0;
                }
            }

            case LAST ->
            {

            }
        }
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event)
    {
        double x = event.x() - uiX;
        double y = event.y() - uiY;

        arrowIndexPressed = false;
        arrowNextPressed = false;
        arrowPreviousPressed = false;

        //previous arrow
        if (x > 49 && x < 69 && y > 203 && y < 217)
        {
            previousPage();
            return true;
        }

        //next arrow
        if (x > 336 && x < 356 && y > 202 && y < 216)
        {
            nextPage();
            return true;
        }

        //index arrow
        if (!menu.equals(MenuEntry.INDEX) && x > 174 && x < 196 && y > 202 && y < 216)
        {
            player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = MenuEntry.INDEX;
            page = 0;
            return true;
        }

        //track fish left
        if (menu.equals(MenuEntry.ENTRY) && entries.size() > page * 2 + 1 && x > 50 && x < 67 && y > 111 && y < 128)
        {
            FishProperties fishProperties = entries.get(page * 2);
            Identifier key = level.registryAccess().lookupOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(fishProperties);
            if (key != null)
                ClientPacketDistributor.sendToServer(new SBTrackFishPayload(key));
            player.playSound(SoundEvents.GLASS_HIT);
            player.playSound(SoundEvents.AMETHYST_BLOCK_HIT, 0.3f, 0.6f);
        }

        //track fish right
        if (menu.equals(MenuEntry.ENTRY) && entries.size() > page * 2 + 1 && x > 210 && x < 227 && y > 111 && y < 128)
        {
            FishProperties fishProperties = entries.get(page * 2 + 1);
            Identifier key = level.registryAccess().lookupOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(fishProperties);
            if (key != null)
                ClientPacketDistributor.sendToServer(new SBTrackFishPayload(key));
            player.playSound(SoundEvents.GLASS_HIT);
            player.playSound(SoundEvents.AMETHYST_BLOCK_HIT, 0.3f, 0.6f);
        }

        if (event.button() == 0)
            clicked = true;

        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //if hovering scrollable on left
        if (x > 53 && x < 189 && y > 155 && y < 200 && menu.equals(MenuEntry.HELP) &&
            (page == 1 || page == 5 || page == 6 || page == 10))
        {
            if (scrollY < 0)
                leftPageScroll++;
            else
                leftPageScroll--;
        }
        else
        {
            //if hovering scrollable on right
            if (x > 212 && x < 356 && y > 155 && y < 200 && menu.equals(MenuEntry.HELP) &&
                (page == 5 || page == 6 || page == 8 || page == 12))
            {
                if (scrollY < 0)
                    rightPageScroll++;
                else
                    rightPageScroll--;
            }
            else
            {
                //if not hovering either scrollables, scroll page
                if (scrollY > 0)
                {
                    nextPage();
                    arrowPreviousPressed = true;
                    arrowPressedFromScrollDecay = 2;
                }
                else
                {
                    previousPage();
                    arrowNextPressed = true;
                    arrowPressedFromScrollDecay = 2;
                }
            }
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        double x = event.x() - uiX;
        double y = event.y() - uiY;

        //compass bottom
        if (x > 17 && x < 46 && y > 37 && y < 63)
        {
            if (isCompassOpen)
            {
                player.playSound(SoundEvents.NOTE_BLOCK_CHIME.value(), 0.1f, 1.2f + level.getRandom().nextFloat() / 10);
                player.playSound(SoundEvents.GLASS_STEP, 0.4f, 1.2f);
                compassRotation += Utils.r.nextInt(40) - 20;
            }
            else
            {
                player.playSound(SoundEvents.NOTE_BLOCK_CHIME.value(), 0.5f, 1.3f);
                player.playSound(SoundEvents.GLASS_STEP, 0.8f, 1.2f);
                isCompassOpen = true;
            }
        }

        //compass top
        if (x > 40 && x < 76 && y > 1 && y < 39 && isCompassOpen)
        {
            player.playSound(SoundEvents.NOTE_BLOCK_CHIME.value(), 0.5f, 0.6f);
            player.playSound(SoundEvents.GLASS_STEP, 0.8f, 0.6f);
            isCompassOpen = false;
        }

        //previous arrow
        if (x > 49 && x < 69 && y > 203 && y < 217)
        {
            if (!menu.equals(MenuEntry.COVER))
            {
                arrowPreviousPressed = true;
            }
        }

        //next arrow
        if (x > 336 && x < 356 && y > 202 && y < 216)
        {
            if (entries.size() > page * 2 + 2 && !menu.equals(MenuEntry.ENTRY))
            {
                arrowNextPressed = true;
            }
        }

        //index arrow
        if (x > 174 && x < 196 && y > 202 && y < 216)
        {
            arrowIndexPressed = true;
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void tick()
    {
        super.tick();
        highlightLeftAlpha -= 0.025f;
        highlightRightAlpha -= 0.025f;
        arrowPressedFromScrollDecay--;
    }

    private void renderCompass(GuiGraphicsExtractor guiGraphics)
    {
        if (isCompassOpen)
            COMPASS_OPEN.render(guiGraphics, uiX, uiY);
        else
        {
            COMPASS_CLOSED.render(guiGraphics, uiX, uiY);
            return;
        }

        float targetRotation = ((player.yRotO % 360.0f) + 360.0f) % 360.0f;
        float smoothing = 0.05f;   // Lower = smoother, higher = faster

        // Calculate shortest difference (-180 to 180)
        float diff = targetRotation - compassRotation;
        if (diff > 180.0f) diff -= 360.0f;
        if (diff < -180.0f) diff += 360.0f;

        // Move a fraction of the remaining distance
        compassRotation += diff * smoothing;

        // Wrap back into 0-360
        if (compassRotation < 0.0f)
            compassRotation += 360.0f;
        else if (compassRotation >= 360.0f)
            compassRotation -= 360.0f;

        Matrix3x2fStack pose = guiGraphics.pose();

        pose.pushMatrix();
        pose.translate((float) (uiX + 16 + 16.5), (float) (uiY + 16 + 34.50));
        pose.rotate((float) Math.toRadians(-compassRotation - 45 - 180));
        pose.translate(-16, -16);
        COMPASS.render(guiGraphics);
        pose.popMatrix();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float a)
    {
        super.extractRenderState(g, mouseX, mouseY, a);
        resolveTrackedFP();

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        switch (menu)
        {
            //settings screen
            case SETTINGS ->
            {
                Minecraft.getInstance().setScreen(new SettingsScreen());
                return;
            }

            //cover page
            case COVER ->
            {
                BACKGROUND_COVER.render(g, uiX, uiY);

                if (signedGuide == null)
                    renderUnsignedCover(g, mouseX, mouseY);
                else
                    renderSignedCover(g, mouseX, mouseY);

                if (x > 91 && x < 159 && y > 89 && y < 217 && signedGuide != null && !signedGuide.visitors().isEmpty())
                {
                    List<Component> comps = new ArrayList<>();
                    comps.add(Component.translatable("gui.guide.visitors"));

                    signedGuide.visitors().forEach(o -> comps.add(Component.literal(o.second())));

                    ScreenUtils.Tooltip.set(comps);
                }
            }

            //index pages
            case INDEX ->
            {
                if (page == 0) BACKGROUND_INDEX_FIRST.render(g, uiX, uiY);
                else BACKGROUND_INDEX_SECOND.render(g, uiX, uiY);
                renderIndex(g, mouseX, mouseY);
            }

            //help pages
            case HELP ->
            {
                BACKGROUND_BASICS.render(g, uiX, uiY);
                renderTheBasics(g, mouseX, mouseY);
            }

            //entries pages
            case ENTRY ->
            {
                //pick random background for that page from player uuid
                long seed = player.getUUID().getMostSignificantBits()
                            ^ player.getUUID().getLeastSignificantBits()
                            ^ page;

                seed ^= seed >>> 33;
                seed *= 0xff51afd7ed558ccdL;
                seed ^= seed >>> 33;
                seed *= 0xc4ceb9fe1a85ec53L;
                seed ^= seed >>> 33;

                int variant = Math.floorMod(seed, 4);

                ScreenUtils.Image background = switch (variant)
                {
                    case 0 -> BACKGROUND_ENTRY;
                    case 1 -> BACKGROUND_ENTRY_2;
                    case 2 -> BACKGROUND_ENTRY_3;
                    default -> BACKGROUND_ENTRY_4;
                };

                background.render(g, uiX, uiY);

                renderEntry(g, mouseX, mouseY, 52, page * 2);
                renderEntry(g, mouseX, mouseY, 212, page * 2 + 1);
                ScreenUtils.text(g, this.font, page + 1 + "/" + ((entries.size() + 1) / 2), uiX + 213, uiY + 206, 0x9c897c, false);
            }

            case LAST ->
            {
                BACKGROUND_LAST_PAGE.render(g, uiX, uiY);
                renderCompass(g);
            }
        }

        renderCompass(g);

        //previous arrow should not render on book cover
        if (!menu.equals(MenuEntry.COVER))
        {
            //previous arrow
            if (x > 49 && x < 69 && y > 203 && y < 217)
                ARROW_PREVIOUS_HIGHLIGHT.render(g, uiX, uiY);

            if (arrowPreviousPressed)
                ARROW_PREVIOUS_PRESSED.render(g, uiX, uiY);
            else
                ARROW_PREVIOUS.render(g, uiX, uiY);
        }

        //index should not render on book cover and first page of index
        if (!menu.equals(MenuEntry.COVER) && !(menu.equals(MenuEntry.INDEX) && page == 0))
        {
            if (x > 174 && x < 196 && y > 202 && y < 216)
                ARROW_INDEX_HIGHLIGHT.render(g, uiX, uiY);

            if (arrowIndexPressed)
                ARROW_INDEX_PRESSED.render(g, uiX, uiY);
            else
                ARROW_INDEX.render(g, uiX, uiY);
        }

        //next arrow should not render on LAST
        if (!(menu.equals(MenuEntry.LAST)))
        {
            if (x > 336 && x < 356 && y > 202 && y < 216)
                ARROW_NEXT_HIGHLIGHT.render(g, uiX, uiY);

            if (arrowNextPressed)
                ARROW_NEXT_PRESSED.render(g, uiX, uiY);
            else
                ARROW_NEXT.render(g, uiX, uiY);
        }

        if (arrowPressedFromScrollDecay == 0)
        {
            arrowNextPressed = false;
            arrowPreviousPressed = false;
        }

        clicked = false;

        ScreenUtils.Tooltip.render(g, font, mouseX, mouseY);
    }

    public void renderUnsignedCover(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY)
    {
        String s = I18n.get("gui.guide.sign");

        int width1 = font.width(s) + 15;

        //draw fitting rectangle
        //todo fix the width calculation here
        ScreenUtils.fill(guiGraphics, uiX + 285 - width1 / 2, uiY + 117, width1, 12, SCColors.GUIDE_BACKGROUND_DARK);
        ScreenUtils.centeredText(guiGraphics, font, s, uiX + 285, uiY + 119, SCColors.GUIDE_TEXT_SEMI_DARK, false);
        ScreenUtils.outline(guiGraphics, uiX + 285 - width1 / 2, uiY + 117, width1, 12, SCColors.GUIDE_TEXT);

        //if hovering sign rectangle
        if (mouseX > uiX + 285 - width1 / 2 && mouseX < uiX + 285 + width1 / 2 && mouseY > uiY + 117 && mouseY < uiY + 117 + 12)
        {
            ScreenUtils.fill(guiGraphics, uiX + 285 - width1 / 2, uiY + 117, width1, 12, SCColors.GUIDE_SCROLLABLE_ITEM_BACKGROUND);
            ScreenUtils.centeredText(guiGraphics, font, s, uiX + 285, uiY + 119, SCColors.GUIDE_TEXT_DARK, false);
            ScreenUtils.outline(guiGraphics, uiX + 285 - width1 / 2, uiY + 117, width1, 12, SCColors.GUIDE_TEXT_DARK);
            if (clicked)
            {
                SignGuidePayload payload = new SignGuidePayload(signedNameEditBox.getValue(), displayBP);
                ClientPacketDistributor.sendToServer(payload);
                onClose();
            }
        }

        signedNameEditBox.extractRenderState(guiGraphics, mouseX, mouseY, 0);
    }

    public void renderSignedCover(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY)
    {
        ScreenUtils.centeredText(guiGraphics, font, signedNameEditBox.getValue(),
                uiX + 285, uiY + 102, SCColors.GUIDE_TEXT, false);
    }

    private void renderHelpText(GuiGraphicsExtractor guiGraphics, int page)
    {
        for (int i = 0; i < 40; i++)
        {
            if (!I18n.exists("gui.guide.page." + page + ".left." + i)) break;
            Component comp = translatable("gui.guide.page." + page + ".left." + i).copy().withStyle(Style.EMPTY.withColor(SCColors.GUIDE_TEXT_SEMI_DARK));
            ScreenUtils.text(guiGraphics, font, comp, uiX + 52, uiY + 10 * i + 13, 0xff000000, false);
        }

        for (int i = 0; i < 40; i++)
        {
            if (!I18n.exists("gui.guide.page." + page + ".right." + i)) break;
            Component comp = translatable("gui.guide.page." + page + ".right." + i).copy().withStyle(Style.EMPTY.withColor(SCColors.GUIDE_TEXT_SEMI_DARK));
            ScreenUtils.text(guiGraphics, this.font, comp, uiX + 213, uiY + 10 * i + 13, 0xff000000, false);
        }

        if (I18n.exists("gui.guide.page." + page + ".left.title"))
            ScreenUtils.centeredText(guiGraphics, this.font, translatable("gui.guide.page." + page + ".left.title"), uiX + 116, uiY + 45, SCColors.GUIDE_TEXT_DARK, false);

        if (I18n.exists("gui.guide.page." + page + ".right.title"))
            ScreenUtils.centeredText(guiGraphics, this.font, translatable("gui.guide.page." + page + ".right.title"), uiX + 270, uiY + 45, SCColors.GUIDE_TEXT_DARK, false);
    }

    private void renderTheBasics(GuiGraphicsExtractor g, int mouseX, int mouseY)
    {
        //shitty workaround for signed guides
        ScreenUtils.text(g, this.font, page + "/" + MAX_HELP_PAGES, uiX + 213, uiY + 206, 0x9c897c, false);

        renderHelpText(g, page);

        switch (page)
        {
            //the basics
            case 0 ->
            {
                HELP_PAGE_BASICS.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, rodIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //tackle boxes
            case 1 ->
            {
                HELP_PAGE_TACKLE_BOX.render(g, uiX, uiY);

                //tackle boxes icon
                renderItemWithHoverAndEmi(g, tackleBoxIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //render tackle boxes
                renderScrollableItems(g, tackleBoxes, mouseX, mouseY, true);
            }

            //treasures
            case 2 ->
            {
                HELP_PAGE_TREASURE.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, treasureIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //sweet-spot types
            case 3 ->
            {
                HELP_PAGE_SWEETSPOTS.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, sweetspotsIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //upgrades
            case 4 ->
            {
                HELP_PAGE_UPGRADES.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, upgradeIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //hooks & bobbers & baits
            case 5 ->
            {
                HELP_PAGE_HOOKS_BOBBERS_BAITS.render(g, uiX, uiY);

                //hook icon
                renderItemWithHoverAndEmi(g, hookIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //render hooks & bobbers
                renderScrollableItems(g, hooksBobbers, mouseX, mouseY, true);

                //bait icon
                renderItemWithHoverAndEmi(g, baitIcon, uiX + 321, uiY + 39, mouseX, mouseY);

                //render baits
                renderScrollableItems(g, baits, mouseX, mouseY, false);
            }

            //cosmetics
            case 6 ->
            {
                HELP_PAGE_COSMETICS.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, cosmeticsIcon, uiX + 166, uiY + 39, mouseX, mouseY);
                renderItemWithHoverAndEmi(g, templateIcon, uiX + 321, uiY + 39, mouseX, mouseY);

                //render cosmetics (hats, skins)
                renderScrollableItems(g, cosmetics, mouseX, mouseY, true);

                //render templates
                renderScrollableItems(g, templates, mouseX, mouseY, false);
            }

            //tournaments
            case 7 ->
            {
                HELP_PAGE_TOURNAMENTS.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, standIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //message-in-a-bottle
            case 8 ->
            {
                HELP_PAGE_MESSAGES.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, letterIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //letter, bottled letter, message-in-a-bottle, message
                for (int i = 0; i < 4; i++)
                {
                    if (mouseX > uiX + 241 + (i * 24) && mouseX < uiX + 241 + 16 + (i * 24) && mouseY > uiY + 130 && mouseY < uiY + 130 + 16)
                        ScreenUtils.outline(g, uiX + 241 + (i * 24) - 2, uiY + 128, 20, 20, SCColors.GUIDE_HIGHLIGHT);
                    else
                        ScreenUtils.outline(g, uiX + 239 + (i * 24), uiY + 128, 20, 20, 0xff9c897c);

                    renderItemWithHoverAndEmi(g, bottles.get(i), uiX + 241 + (i * 24), uiY + 130, mouseX, mouseY);
                }

                //scrollable found messages
                //right page scroll arrows
                ARROW_LEFT.render(g, uiX + 219, uiY + 170);
                ARROW_RIGHT.render(g, uiX + 337, uiY + 170);

                //if clicked on left arrow
                if (mouseX > uiX + 219 && mouseX < uiX + 235 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    ARROW_LEFT_HIGHLIGHT.render(g, uiX + 219, uiY + 170);
                    if (clicked)
                    {
                        rightPageScroll--;
                        player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                    }
                }
                //if clicked on right arrow
                if (mouseX > uiX + 337 && mouseX < uiX + 353 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    ARROW_RIGHT_HIGHLIGHT.render(g, uiX + 337, uiY + 170);
                    if (clicked)
                    {
                        rightPageScroll++;
                        player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                    }
                }

                //messages found text
                ScreenUtils.centeredText(g, font, translatable("gui.guide.page.8.left.messages_found"),
                        uiX + 286, uiY + 157, SCColors.GUIDE_TEXT_DARK, false);

                //scrollable messages icons
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 238 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = messages.get(Math.abs((rightPageScroll + i) % messages.size()));
                    //render item
                    ScreenUtils.item(g, stack, x, y);
                    //render hover item tooltip
                    if (mouseX > x - 2 && mouseX < x + 16 + 2 && mouseY > y - 2 && mouseY < y + 16 + 2)
                    {
                        ScreenUtils.outline(g, x - 2, y - 2, 20, 20, SCColors.GUIDE_HIGHLIGHT);

                        ScreenUtils.Tooltip.set(stack);
                        Message message = SCDataComponents.getOrDefault(stack, SCDataComponents.MESSAGE, Message.DEFAULT);
                        //if clicked open message screen
                        if (clicked && !message.equals(Message.DEFAULT))
                            Minecraft.getInstance().setScreen(
                                    new MessageScreen(message, this));
                    }
                    //scrollable background fill
                    ScreenUtils.fill(g, x - 1, y - 1, 18, 18, 0xffb4a697);
                }
            }

            //selling bin
            case 9 ->
            {
                HELP_PAGE_SELLING.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, sellingBinIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //aquariums
            case 10 ->
            {
                HELP_PAGE_AQUARIUM.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, aquariumIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //render scrollable items
                renderScrollableItems(g, aquariumInteractions, mouseX, mouseY, true);
            }

            //selling bin
            case 11 ->
            {
                HELP_PAGE_DISPLAY.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, displayIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                renderItemWithHoverAndEmi(g, guideIcon, uiX + 321, uiY + 39, mouseX, mouseY);
            }

            //stats and trophies
            case 12 ->
            {
                HELP_PAGE_TROPHIES.render(g, uiX, uiY);
                renderItemWithHoverAndEmi(g, letterIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //stats
                //fishes caught
                ScreenUtils.text(g, font, translatable("gui.guide.stats.fishes_caught"), uiX + 53, uiY + 63, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, fishesCaught + "", uiX + 165, uiY + 63, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //trash
                ScreenUtils.text(g, font, translatable("gui.guide.stats.trash_caught"), uiX + 53, uiY + 73, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, trashCaught + "", uiX + 165, uiY + 73, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //common
                ScreenUtils.text(g, font, translatable("gui.guide.stats.common_caught"), uiX + 53, uiY + 83, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, commonCaught + "", uiX + 165, uiY + 83, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //uncommon
                ScreenUtils.text(g, font, translatable("gui.guide.stats.uncommon_caught"), uiX + 53, uiY + 93, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, uncommonCaught + "", uiX + 165, uiY + 93, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //rare
                ScreenUtils.text(g, font, translatable("gui.guide.stats.rare_caught"), uiX + 53, uiY + 103, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, rareCaught + "", uiX + 165, uiY + 103, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //epic
                ScreenUtils.text(g, font, translatable("gui.guide.stats.epic_caught"), uiX + 53, uiY + 113, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, epicCaught + "", uiX + 165, uiY + 113, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //rare
                ScreenUtils.text(g, font, translatable("gui.guide.stats.legendary_caught"), uiX + 53, uiY + 123, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, legendaryCaught + "", uiX + 165, uiY + 123, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //golden fishes caught
                ScreenUtils.text(g, font, translatable("gui.guide.stats.golden_fishes_caught"), uiX + 53, uiY + 133, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, goldenFishesCaught + "", uiX + 165, uiY + 133, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //perfect fishes caught
                ScreenUtils.text(g, font, translatable("gui.guide.stats.perfect_fishes_caught"), uiX + 53, uiY + 143, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, perfectFishesCaught + "", uiX + 165, uiY + 143, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //fishes missed
                ScreenUtils.text(g, font, translatable("gui.guide.stats.starcaught_fish_missed"), uiX + 53, uiY + 153, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, fishMissed + "", uiX + 165, uiY + 153, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //treasures caught
                ScreenUtils.text(g, font, translatable("gui.guide.stats.treasures_caught"), uiX + 53, uiY + 163, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, treasuresCaught + "", uiX + 165, uiY + 163, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //bait used
                ScreenUtils.text(g, font, translatable("gui.guide.stats.bait_used"), uiX + 53, uiY + 173, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                ScreenUtils.text(g, font, baitUsed + "", uiX + 165, uiY + 173, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //time spent
                ScreenUtils.text(g, font, translatable("gui.guide.stats.time_spent"), uiX + 53, uiY + 183, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                String text = Utils.calculateRealLifeTimeFromTicks(timeSpent);
                ScreenUtils.text(g, font, text.isEmpty() ? "---" : text, uiX + 130, uiY + 183, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //trophy icon
                renderItemWithHoverAndEmi(g, trophyIcon, uiX + 321, uiY + 39, mouseX, mouseY);

                //right page scroll arrows
                ARROW_LEFT.render(g, uiX + 219, uiY + 170);
                ARROW_RIGHT.render(g, uiX + 337, uiY + 170);

                //if clicked on left arrow
                if (mouseX > uiX + 219 && mouseX < uiX + 235 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    ARROW_LEFT_HIGHLIGHT.render(g, uiX + 219, uiY + 170);
                    if (clicked)
                    {
                        rightPageScroll--;
                        player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                    }
                }
                //if clicked on right arrow
                if (mouseX > uiX + 337 && mouseX < uiX + 353 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    ARROW_RIGHT_HIGHLIGHT.render(g, uiX + 337, uiY + 170);
                    if (clicked)
                    {
                        rightPageScroll++;
                        player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                    }
                }

                //scrollable trophy icons
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 238 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = trophiesIS.get(Math.abs((rightPageScroll + i) % trophiesIS.size()));
                    FishProperties fp = trophies.get(Math.abs((rightPageScroll + i) % trophies.size()));
                    //render item
                    ScreenUtils.item(g, stack, x, y);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        ScreenUtils.outline(g, x - 2, y - 2, 20, 20, SCColors.GUIDE_HIGHLIGHT);

                        FishCaughtCounter fishCaughtCounter = fishCaughtCounterMap.get(fp.toLoc(level));
                        if (!fp.equals(FishProperties.empty()))
                            ScreenUtils.Tooltip.set(getCachedTooltipForHoverEntry(fp, fishCaughtCounter == null ? 0 : fishCaughtCounter.count()));

                        //if clicked on a trophy, display FP
                        if (clicked && !fp.equals(FishProperties.empty()))
                            Minecraft.getInstance().setScreen(new IsolatedFPScreen(fp, this));
                    }
                    //scrollable background fill
                    ScreenUtils.fill(g, x - 1, y - 1, 18, 18, 0xffb4a697);
                }
            }
        }
    }

    private void renderIndex(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY)
    {
        //render first line help shortcuts
        int xx = uiX + 55;
        if (SCConfig.ENABLE_GUIDE_HELP_PAGES.get())
        {
            if (page == 0)
            {
                for (int i = 0; i < 7; i++)
                {
                    ScreenUtils.fill(guiGraphics, xx + i * 20 - 1, uiY + 47 - 1, 18, 18, SCColors.GUIDE_WHITE);
                    ScreenUtils.item(guiGraphics, indexEntries.get(i).getFirst(), xx + i * 20, uiY + 47);

                    if (mouseX > xx + (i * 20) - 2 && mouseX < xx + (i * 20) + 17 && mouseY > uiY + 47 - 2 && mouseY < uiY + 47 + 17)
                        ScreenUtils.Tooltip.add(translatable(indexEntries.get(i).getSecond()));

                    if (clicked && mouseX > xx + (i * 20) - 2 && mouseX < xx + (i * 20) + 17 && mouseY > uiY + 47 - 2 && mouseY < uiY + 47 + 17)
                    {
                        clicked = false;
                        player.playSound(SoundEvents.BOOK_PAGE_TURN);
                        menu = MenuEntry.HELP;
                        switch (i)
                        {
                            case 0 -> page = 0;
                            case 1 -> page = 1;
                            case 2 -> page = 2;
                            case 3 -> page = 3;
                            case 4 -> page = 4;
                            case 5 -> page = 5;
                        }
                        if (i == 6) menu = MenuEntry.SETTINGS;
                    }
                }

                xx = uiX + 55;
                //render second line help shortcuts
                for (int i = 7; i < 14; i++)
                {
                    ScreenUtils.fill(guiGraphics, xx + (i - 7) * 20 - 1, uiY + 47 + 20 - 1, 18, 18, SCColors.GUIDE_WHITE);
                    ScreenUtils.item(guiGraphics, indexEntries.get(i).getFirst(), xx + (i - 7) * 20, uiY + 47 + 20);

                    if (mouseX > xx + ((i - 7) * 20) - 2 && mouseX < xx + ((i - 7) * 20) + 17 && mouseY > uiY + 47 + 20 - 2 && mouseY < uiY + 47 + 20 + 17)
                    {
                        ScreenUtils.Tooltip.add(translatable(indexEntries.get(i).getSecond()));
                        if (clicked)
                        {
                            clicked = false;
                            player.playSound(SoundEvents.BOOK_PAGE_TURN);
                            menu = MenuEntry.HELP;
                            switch (i)
                            {
                                case 7 -> page = 6;
                                case 8 -> page = 7;
                                case 9 -> page = 8;
                                case 10 -> page = 9;
                                case 11 -> page = 10;
                                case 12 -> page = 11;
                                case 13 -> page = 12;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            ScreenUtils.centeredText(guiGraphics, font,
                    translatable("gui.guide.disabled_by_server"),
                    uiX + 123, uiY + 60,
                    SCColors.GUIDE_TEXT, false);
        }


        //[sort] text
        if (page == 0)
        {
            ScreenUtils.centeredText(guiGraphics, this.font, translatable("gui.guide.sort"), uiX + 171, uiY + 88, 0x937d70, false);
            if (mouseX > uiX + 145 && mouseX < uiX + 190 && mouseY > uiY + 86 && mouseY < uiY + 96)
            {
                ScreenUtils.Tooltip.add(translatable(SCConfig.SORT.get().getTranslationKey()));
                if (clicked)
                {
                    SCConfig.SORT.set(SCConfig.SORT.get().next());
                    SCConfig.SORT.save();
                    entries = sortEntries(SCConfig.SORT.get(), entries, player);
                    fishInArea = sortEntries(SCConfig.SORT.get(), fishInArea, player);
                }
            }
        }

        //render fishes in area
        {
            if (page == 0)
            {
                //render fishes in area clickable squares and stuff
                for (int i = 0; i < fishInArea.size(); i++)
                {
                    if (i >= 35) break;
                    FishProperties fp = fishInArea.get(i);

                    int xpos = uiX + 53 + (i % 7) * 20;
                    int ypos = uiY + 67 + (i / 7 * 20) + 38;

                    renderFishIndex(guiGraphics, xpos, ypos, mouseX, mouseY, fp);
                }

                //render decorations and stuff
                {
                    //render top right deco unless theres no fish in the top right slot
                    if (fishInArea.size() > 6)
                        FISHES_IN_AREA_TOP_RIGHT_DECORATION.render(guiGraphics, uiX, uiY);

                    int numberOfRows = (fishInArea.size() - 1) / 7 + 1;

                    //render bottom decoration if there's space
                    if (numberOfRows < 3)
                        FISHES_IN_AREA_BOTTOM_DECORATION.render(guiGraphics, uiX, uiY);


                    //render bottom left thingy, offset by the number of rows
                    if (!fishInArea.isEmpty() && numberOfRows < 5)
                        FISHES_IN_AREA_BOTTOM_LEFT_DECORATION.render(guiGraphics, uiX, uiY + (numberOfRows - 1) * 20 + 20);

                    //render fish skeleton unless there's no space for it
                    int xFishSkeletonOffset = 0;
                    if (fishInArea.size() % 7 > 4 || fishInArea.size() % 7 == 0) xFishSkeletonOffset = 20;
                    if (numberOfRows < 5)
                        FISHES_IN_AREA_FISH_DECORATION.render(guiGraphics, uiX, uiY + (numberOfRows - 1) * 20 + xFishSkeletonOffset + 20);
                    if (numberOfRows == 5 && fishInArea.size() % 7 < 5 && fishInArea.size() % 7 != 0)
                        FISHES_IN_AREA_FISH_DECORATION.render(guiGraphics, uiX, uiY + (numberOfRows - 1) * 20 + xFishSkeletonOffset + 20);
                }
            }

        }

        //render all fishes
        if (page == 0)
        {
            for (int i = 0; i < 49; i++)
            {
                if (i > entries.size() - 1) return;
                renderFishIndex(guiGraphics, xx + 160 + (i % 7 * 20), uiY + 56 + (i / 7 * 20), mouseX, mouseY, entries.get(i));
            }
        }
        else
        {
            //render second page, left
            for (int i = 0; i < 49; i++)
            {
                int order = i + 49 * (page * 2 - 1);
                if (order > entries.size() - 1) break;
                renderFishIndex(guiGraphics, xx + (i % 7 * 20), uiY + 56 + (i / 7 * 20), mouseX, mouseY, entries.get(order));
            }

            //render second page, right
            for (int i = 0; i < 49; i++)
            {
                int order = i + 49 + 49 * (page * 2 - 1);
                if (order > entries.size() - 1) return;
                renderFishIndex(guiGraphics, xx + 160 + (i % 7 * 20), uiY + 56 + (i / 7 * 20), mouseX, mouseY, entries.get(order));
            }
        }
    }

    private void renderScrollableItems(GuiGraphicsExtractor guiGraphics, List<ItemStack> stacks, int mouseX,
                                       int mouseY, boolean isLeftPage)
    {
        int offset = isLeftPage ? 0 : 161;
        //left page scroll arrows
        ARROW_LEFT.render(guiGraphics, uiX + 53 + offset, uiY + 170);
        ARROW_RIGHT.render(guiGraphics, uiX + 171 + offset, uiY + 170);

        //if hovering on left arrow
        if (mouseX > uiX + 53 + offset && mouseX < uiX + 69 + offset && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
        {
            ARROW_LEFT_HIGHLIGHT.render(guiGraphics, uiX + 53 + offset, uiY + 170);
            if (clicked)
            {
                if (isLeftPage)
                    leftPageScroll--;
                else
                    rightPageScroll--;
                player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
            }
        }
        //if hovering on right arrow
        if (mouseX > uiX + 171 + offset && mouseX < uiX + 185 + offset && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
        {
            ARROW_RIGHT_HIGHLIGHT.render(guiGraphics, uiX + 171 + offset, uiY + 170);
            if (clicked)
            {
                player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                if (isLeftPage)
                    leftPageScroll++;
                else
                    rightPageScroll++;
            }
        }

        //scrollable tackle boxes icons
        for (int i = 0; i < 5; i++)
        {
            int x = uiX + 72 + offset + (i * 20);
            int y = uiY + 170;
            ItemStack stack = stacks.get(Math.abs(((isLeftPage ? leftPageScroll : rightPageScroll) + i) % stacks.size()));
            //render background
            ScreenUtils.fill(guiGraphics, x - 1, y - 1, 18, 18, SCColors.GUIDE_SCROLLABLE_ITEM_BACKGROUND);
            //render hover item tooltip
            if (mouseX > x - 2 && mouseX < x + 16 + 2 && mouseY > y - 2 && mouseY < y + 16 + 2)
            {
                ScreenUtils.outline(guiGraphics, x - 2, y - 2, 20, 20, SCColors.GUIDE_HIGHLIGHT);
                ScreenUtils.Tooltip.set(stack);
                ;
                if (clicked)
                    ScreenUtils.displayRecipe(stack);
            }
            //render item
            ScreenUtils.item(guiGraphics, stack, x, y);
        }
    }

    private void renderFishIndex(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset, int mouseX,
                                 int mouseY, FishProperties fp)
    {
        Identifier rl = fp.toLoc(level);
        FishCaughtCounter fcc = fishCaughtCounterMap.get(rl);
        ItemStack is = fp.catchInfo().fish().toStack();

        if (fcc != null && fcc.caughtGolden())
            SCDataComponents.set(is, SCDataComponents.CAUGHT_FISH_INFO, CaughtFishInfo.GOLDEN);

        //calculate caught counter
        int caught = fcc == null ? 0 : fcc.count();

        //handle click
        if (clicked && mouseX > xOffset - 3 && mouseX < xOffset + 21 - 3 && mouseY > yOffset - 3 && mouseY < yOffset + 21 - 3)
        {
            player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = MenuEntry.ENTRY;
            page = entries.indexOf(fp) / 2;

            if (entries.indexOf(fp) % 2 == 0)
                highlightLeftAlpha = 0.5f;
            else
                highlightRightAlpha = 0.5f;
        }

        //render fill
        ScreenUtils.fill(guiGraphics, xOffset - 1, yOffset - 1, 18, 18, SCColors.GUIDE_BACKGROUND_DARK);

        //glow color
        int color = switch (fp.rarity())
        {
            case Rarity.TRASH, Rarity.COMMON, Rarity.NONE -> Utils.toColorI(0, 0, 0, 50);
            case Rarity.UNCOMMON -> 0xff92f28d;
            case Rarity.RARE -> 0xff78c8ff;
            case Rarity.EPIC -> 0xffc060ff;
            case Rarity.LEGENDARY, Rarity.GOLDEN ->
            {
                int colorI = Color.HSBtoRGB((float) Util.getMillis() / 10000, 1, 1);

                yield Utils.toColorI(
                        Utils.intToRed(colorI),
                        Utils.intToGreen(colorI),
                        Utils.intToBlue(colorI),
                        175
                );
            }
        };

        //render glow
        ScreenUtils.setColor(color);
        GLOW.render(guiGraphics, xOffset - 1, yOffset - 1);

        //render fish with missingno if not caught
        if (caught != 0 || !SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get())
            ScreenUtils.item(guiGraphics, is, xOffset, yOffset);
        else
            ScreenUtils.item(guiGraphics, missingnoStack, xOffset, yOffset);

        //render fish notification icon
        if (fcc != null && fcc.hasGuideNotification() && !fpsSeen.contains(FishApi.getKey(level, fp)))
            ScreenUtils.outline(guiGraphics, xOffset - 1, yOffset - 1, 18, 18, 0xffc58c44);

        //render tooltip
        if (mouseX > xOffset - 3 && mouseX < xOffset + 21 - 3 && mouseY > yOffset - 3 && mouseY < yOffset + 21 - 3)
        {
            ScreenUtils.Tooltip.set(getCachedTooltipForHoverEntry(fp, caught));
            ScreenUtils.Tooltip.add(1, translatable("gui.guide.rarity." + fp.rarity().getSerializedName()));
            ScreenUtils.Tooltip.add(translatable("gui.guide.click").withStyle(ChatFormatting.DARK_GRAY));

            if (fcc != null && fcc.hasGuideNotification() && SCConfig.REMOVE_NOTIFICATION_ON_HOVER.get() && !fpsSeen.contains(rl))
                fpsSeen.add(rl);
        }
    }

    private FishProperties cachedFp = null;
    private List<Component> cachedHoverList = List.of();

    private List<Component> getCachedTooltipForHoverEntry(FishProperties fp, int caught)
    {
        if (fp == cachedFp && cachedFp != null) return cachedHoverList;
        cachedFp = fp;

        List<Component> components = new ArrayList<>();

        boolean isFish = fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH);
        if (caught == 0 && SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get())
        {
            components.add(translatable("gui.guide.not_caught_fish_name"));
            if (isFish)
                components.add(translatable("gui.guide.not_caught_yet").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
        }
        else
        {
            if (fp.catchInfo().alwaysSpawnEntity() && !fp.catchInfo().entityToSpawn().is(Utils.holderEntity(SCEntities.FISH.get())))
                components.add(translatable("entity." + fp.catchInfo().entityToSpawn().getRegisteredName().replace(":", ".")));
            else
                components.add(translatable(fp.catchInfo().fish().toStack().getItem().getDescriptionId()));

            if (isFish)
                components.add(translatable("gui.guide.caught").append(Component.literal(" [" + caught + "]")).withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
        }

        //Aurora
        //Legendary
        //Not Caught yet!
        //
        //✅ dimension
        //❌ biome
        //Not in Season!

        components.add(Component.empty());
        for (AbstractFishRestriction restriction : fp.restrictions())
        {
            if (!restriction.isEnabled()) continue;
            List<Component> indexHover = restriction.getIndexHover(level, fp, player, AbstractFishRestriction.Context.GUIDE_FISHES_HOVER);
            components.addAll(indexHover);
        }

        if (components.getLast().equals(Component.empty())) components.removeLast();

        cachedHoverList = components;
        return components;
    }

    public static final ItemStack SPYGLASS = new ItemStack(Items.SPYGLASS);
    public static final ItemStack SPYGLASS_GOLDEN = new ItemStack(Items.SPYGLASS);

    private void renderEntry(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int xOffset, int entry)
    {
        if (level == null) level = getMinecraft().level;

        if (entries.size() <= entry) return;

        FishProperties fp = entries.get(entry);

        Identifier loc = fp.toLoc(level);
        FishCaughtCounter fishCaughtCounter = fishCaughtCounterMap.get(loc);
        if (fishCaughtCounter != null && !fpsSeen.contains(loc) && fishCaughtCounter.hasGuideNotification())
            fpsSeen.add(loc);

        //get fishCaughtCount
        FishCaughtCounter fcc = fishCaughtCounterMap.get(loc);

        ItemStack is = fcc == null && SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get() ? ItemStack.EMPTY : entries.get(entry).catchInfo().fish().toStack();
        if (fcc != null && fcc.caughtGolden())
            SCDataComponents.set(is, SCDataComponents.CAUGHT_FISH_INFO, CaughtFishInfo.GOLDEN);

        renderFishEntryPage(guiGraphics, fp, is, fcc, uiX + xOffset, uiY, mouseX, mouseY);

        int x = mouseX - uiX;
        int y = mouseY - uiY;

        //render fish tracker icon if tracked fish matches fp being rendered
        if (trackedFP.equals(fp))
        {
            //render tracked icon
            TRACKED.render(guiGraphics, uiX + 112 + xOffset, uiY + 20);

            //sound on clicking
            if (x > 124 + xOffset && x < 145 + xOffset && y > 44 && y < 64 && clicked)
                Minecraft.getInstance().player.playSound(SoundEvents.AMETHYST_BLOCK_HIT, 0.4f, 1.5f);
            //render golden spyglass if tracked
            int xxx = -1;
            int yyy = 111;
            ScreenUtils.fill(guiGraphics, uiX + xxx + xOffset, uiY + yyy, 16, 16, SCColors.GUIDE_TEXT);
            ScreenUtils.item(guiGraphics, SPYGLASS_GOLDEN, uiX + xOffset - 1, uiY + 111);
        }
        else
        {
            //render spyglass
            ScreenUtils.item(guiGraphics, SPYGLASS, uiX + xOffset - 1, uiY + 111);
        }

        //render spyglass hover text
        if (x > xOffset - 3 && x < 17 + xOffset - 2 && y > 111 && y < 128)
            ScreenUtils.Tooltip.add(translatable("gui.guide.track"));

        //white highlight on jumping to
        if (highlightRightAlpha > 0)
        {
            ScreenUtils.setColorF(highlightRightAlpha, 1, 1, 1);
            HIGHLIGHT_RIGHT.render(guiGraphics, uiX, uiY);
        }

        if (highlightLeftAlpha > 0)
        {
            ScreenUtils.setColorF(highlightLeftAlpha, 1, 1, 1);
            HIGHLIGHT_LEFT.render(guiGraphics, uiX, uiY);
        }
    }

    private void renderItemWithHoverAndEmi(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y, int mouseX,
                                           int mouseY)
    {
        ScreenUtils.item(guiGraphics, stack, x, y);
        if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
        {
            ScreenUtils.Tooltip.set(stack);
            if (clicked)
                ScreenUtils.displayRecipe(stack);
        }
    }

    @Override
    public void onClose()
    {
        ClientPacketDistributor.sendToServer(new SBFPsSeenPayload(fpsSeen));
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
        CAUGHT_GOLDEN_UP("gui.guide.sort.caught_golden_up"),
        CAUGHT_GOLDEN_DOWN("gui.guide.sort.caught_golden_down"),
        ;

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
            int lenght = vals.length;
            if (this.ordinal() == 0) return vals[lenght - 1];
            return vals[(this.ordinal() - 1) % lenght];
        }

        public Sort next()
        {
            int lenght = vals.length;
            return vals[(this.ordinal() + 1) % lenght];
        }
    }

    public List<FishProperties> sortEntries(Sort sort, List<FishProperties> entriesToSort, Player player)
    {
        //rarity
        if (sort.equals(Sort.RARITY_DOWN) || sort.equals(Sort.RARITY_UP))
        {
            //sort alphabetical first
            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(
                    o -> BuiltInRegistries.ITEM.getKey(o.catchInfo().fish().toItem()).getPath())).toList();

            List<FishProperties> entriesSorted = new ArrayList<>();

            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(Rarity.NONE)) entriesSorted.add(e);
            });

            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(Rarity.TRASH)) entriesSorted.add(e);
            });

            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(Rarity.COMMON)) entriesSorted.add(e);
            });
            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(Rarity.UNCOMMON)) entriesSorted.add(e);
            });
            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(Rarity.RARE)) entriesSorted.add(e);
            });
            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(Rarity.EPIC)) entriesSorted.add(e);
            });
            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(Rarity.LEGENDARY)) entriesSorted.add(e);
            });

            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(Rarity.GOLDEN)) entriesSorted.add(e);
            });

            return sort.equals(Sort.RARITY_UP) ? entriesSorted : entriesSorted.reversed();
        }

        //alphabetical
        if (sort.equals(Sort.ALPHABETICAL_DOWN) || sort.equals(Sort.ALPHABETICAL_UP))
        {
            List<FishProperties> entriesSorted = entriesToSort.stream().sorted(Comparator.comparing(
                    o -> BuiltInRegistries.ITEM.getKey(o.catchInfo().fish().toItem()).getPath())).toList();
            return sort.equals(Sort.ALPHABETICAL_UP) ? entriesSorted : entriesSorted.reversed();
        }

        //mod
        if (sort.equals(Sort.MOD_DOWN) || sort.equals(Sort.MOD_UP))
        {
            List<FishProperties> entriesSorted = entriesToSort.stream().sorted(Comparator.comparing(
                    o -> BuiltInRegistries.ITEM.getKey(o.catchInfo().fish().toItem()).getNamespace())).toList();
            return sort.equals(Sort.MOD_DOWN) ? entriesSorted : entriesSorted.reversed();
        }

        //caught
        if (sort.equals(Sort.CAUGHT_UP) || sort.equals(Sort.CAUGHT_DOWN))
        {
            //sort alphabetical first
            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(o -> BuiltInRegistries.ITEM.getKey(o.catchInfo().fish().toItem()).getPath())).toList();

            //add all fishes caught to start
            Map<Identifier, FishCaughtCounter> fishesCaught = fishCaughtCounterMap;

            List<FishProperties> hasCaught = new ArrayList<>();
            List<FishProperties> hasNotCaught = new ArrayList<>();
            List<FishProperties> toReturn = new ArrayList<>();

            //populate hasCaught and hasNotCaught
            entriesToSort.forEach(fp ->
            {
                if (fp.hasGuideEntry() && fishesCaught.containsKey(fp.toLoc(level)))
                    hasCaught.add(fp);
                else
                    hasNotCaught.add(fp);
            });


            if (sort.equals(Sort.CAUGHT_UP))
            {
                toReturn.addAll(hasCaught);
                toReturn.addAll(hasNotCaught);
            }
            else
            {
                toReturn.addAll(hasNotCaught);
                toReturn.addAll(hasCaught);
            }

            return toReturn;
        }

        //caught golden
        if (sort.equals(Sort.CAUGHT_GOLDEN_UP) || sort.equals(Sort.CAUGHT_GOLDEN_DOWN))
        {
            //sort alphabetical first
            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(o -> BuiltInRegistries.ITEM.getKey(o.catchInfo().fish().toItem()).getPath())).toList();

            //add all fishes caught to start
            Map<Identifier, FishCaughtCounter> fishesCaught = fishCaughtCounterMap;

            List<FishProperties> hasCaught = new ArrayList<>();
            List<FishProperties> hasNotCaught = new ArrayList<>();
            List<FishProperties> toReturn = new ArrayList<>();

            //populate hasCaught and hasNotCaught
            entriesToSort.forEach(fp ->
            {
                if (fp.hasGuideEntry() && fishesCaught.containsKey(fp.toLoc(level)) && fishesCaught.get(fp.toLoc(level)).caughtGolden())
                    hasCaught.add(fp);
                else
                    hasNotCaught.add(fp);
            });


            if (sort.equals(Sort.CAUGHT_GOLDEN_UP))
            {
                toReturn.addAll(hasCaught);
                toReturn.addAll(hasNotCaught);
            }
            else
            {
                toReturn.addAll(hasNotCaught);
                toReturn.addAll(hasCaught);
            }

            return toReturn;
        }

        return entriesToSort;
    }

    public static void open(BlockPos displayBP, SignedGuide signedGuide)
    {
        Minecraft.getInstance().setScreen(new FishingGuideScreen(displayBP, signedGuide));
    }

    public FishingGuideScreen(BlockPos displayBP, SignedGuide signedGuide)
    {
        super(Component.empty());
        this.displayBP = displayBP;
        this.signedGuide = signedGuide;

        LocalPlayer player = Minecraft.getInstance().player;
        boolean shouldUseLocalMap = signedGuide == null || signedGuide.owner().equals(player.getUUID());
        this.fishCaughtCounterMap = shouldUseLocalMap ?
                SCDataAttachments.get(player, SCDataAttachments.FISHING_GUIDE).fishesCaught : signedGuide.fishesCaught();

        if (!shouldUseLocalMap)
            menu = MenuEntry.COVER;


        missingnoStack = SCItems.MISSINGNO.toStack();

        rodIcon = new ItemStack(SCItems.ROD.get());
        sweetspotsIcon = new ItemStack(SCItems.AURORA.get());
        tackleBoxIcon = new ItemStack(SCBlocks.TACKLE_BOX.get());
        treasureIcon = new ItemStack(Items.DIAMOND);
        cosmeticsIcon = new ItemStack(SCBlocks.FISHERMAN_HAT_BLUE.get());
        templateIcon = new ItemStack(SCItems.PEARL_SMITHING_TEMPLATE.get());
        baitIcon = new ItemStack(SCItems.CHERRY_BAIT.get());
        var auroraIcon = new ItemStack(SCItems.AURORA.get());
        upgradeIcon = new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        hookIcon = new ItemStack(SCItems.HOOK.get());
        standIcon = new ItemStack(SCBlocks.STAND.get());
        var fishermanHatIcon = new ItemStack(SCBlocks.FISHERMAN_HAT_BLUE.get());
        var settingsIcon = new ItemStack(SCItems.SETTINGS.get());
        letterIcon = new ItemStack(SCItems.LETTER.get());
        var letterBottleIcon = new ItemStack(SCItems.BOTTLED_LETTER.get());
        var messageBottleIcon = new ItemStack(SCItems.MESSAGE_IN_A_BOTTLE.get());
        var messageIcon = new ItemStack(SCItems.MESSAGE.get());
        sellingBinIcon = new ItemStack(SBBlocks.SELLING_BIN.get());
        aquariumIcon = new ItemStack(SCBlocks.AQUARIUM.get());
        displayIcon = new ItemStack(SCBlocks.DISPLAY.get());
        guideIcon = new ItemStack(SCItems.GUIDE.get());
        trophyIcon = new ItemStack(SCBlocks.TROPHY_GOLD.get());

        //populate lists
        BuiltInRegistries.ITEM.getTagOrEmpty(SCTags.TACKLE_BOXES).forEach(i -> tackleBoxes.add(i.value().getDefaultInstance()));
        BuiltInRegistries.ITEM.getTagOrEmpty(SCTags.HOOKS).forEach(i -> hooksBobbers.add(i.value().getDefaultInstance()));
        BuiltInRegistries.ITEM.getTagOrEmpty(SCTags.BOBBERS).forEach(i -> hooksBobbers.add(i.value().getDefaultInstance()));
        BuiltInRegistries.ITEM.getTagOrEmpty(SCTags.BAITS).forEach(i -> baits.add(i.value().getDefaultInstance()));
        BuiltInRegistries.ITEM.getTagOrEmpty(SCTags.TEMPLATES).forEach(i -> templates.add(i.value().getDefaultInstance()));
        BuiltInRegistries.ITEM.getTagOrEmpty(SCTags.EQUIPMENTS).forEach(i -> cosmetics.add(i.value().getDefaultInstance()));
        BuiltInRegistries.ITEM.getTagOrEmpty(SCTags.HATS).forEach(i -> cosmetics.add(i.value().getDefaultInstance()));

        Iterable<Holder<Item>> interactions = BuiltInRegistries.ITEM.getTagOrEmpty(SCTags.AQUARIUM_INTERACTIONS);
        interactions.forEach(o -> aquariumInteractions.add(new ItemStack(o.value())));
        if (aquariumInteractions.isEmpty()) aquariumInteractions.add(ItemStack.EMPTY);

        indexEntries = new ArrayList<>(List.of(
                Pair.of(rodIcon, "gui.guide.index.basics"),
                Pair.of(tackleBoxIcon, "gui.guide.index.tackle_box"),
                Pair.of(treasureIcon, "gui.guide.index.treasures"),
                Pair.of(auroraIcon, "gui.guide.index.sweetspots"),
                Pair.of(upgradeIcon, "gui.guide.index.upgrades"),
                Pair.of(hookIcon, "gui.guide.index.hooks_bobbers_baits"),
                Pair.of(settingsIcon, "gui.guide.index.settings"),

                Pair.of(fishermanHatIcon, "gui.guide.index.cosmetics"),
                Pair.of(standIcon, "gui.guide.index.tournaments"),
                Pair.of(letterIcon, "gui.guide.index.messages"),
                Pair.of(sellingBinIcon, "gui.guide.index.selling_bin"),
                Pair.of(aquariumIcon, "gui.guide.index.aquarium"),
                Pair.of(displayIcon, "gui.guide.index.display"),
                Pair.of(trophyIcon, "gui.guide.index.trophies_and_secrets")
        ));

        bottles = List.of(letterIcon, letterBottleIcon, messageBottleIcon, messageIcon);

        if (player == null || Minecraft.getInstance().level == null)
            return;

        List<Pair<Rarity, FishCaughtCounter>> list = new ArrayList<>();

        for (Map.Entry<Identifier, FishCaughtCounter> entry : fishCaughtCounterMap.entrySet())
        {
            FishProperties fp = FishApi.getFP(Minecraft.getInstance().level, entry.getKey());
            if (fp != null && fp.catchInfo().fishEntryType().equals(CatchInfo.FishEntryType.FISH) && fp.hasGuideEntry())
                list.add(Pair.of(fp.rarity(), entry.getValue()));
        }

        fishesCaught = 0;
        trashCaught = 0;
        commonCaught = 0;
        uncommonCaught = 0;
        rareCaught = 0;
        epicCaught = 0;
        legendaryCaught = 0;

        timeSpent = 0;
        goldenFishesCaught = 0;
        perfectFishesCaught = 0;

        for (var entry : list)
        {
            switch (entry.getFirst())
            {
                case TRASH -> trashCaught++;
                case COMMON -> commonCaught++;
                case UNCOMMON -> uncommonCaught++;
                case RARE -> rareCaught++;
                case EPIC -> epicCaught++;
                case LEGENDARY -> legendaryCaught++;
            }

            FishCaughtCounter ffc = entry.getSecond();
            if (ffc.caughtGolden())
                goldenFishesCaught++;

            if (ffc.perfectCatch())
                perfectFishesCaught++;

            fishesCaught += ffc.count();
        }

        if (signedGuide != null)
        {
            timeSpent = signedGuide.stats().timeSpent;
            treasuresCaught = signedGuide.stats().treasuresCaught;
            fishMissed = signedGuide.stats().fishMissed;
            baitUsed = signedGuide.stats().baitUsed;
        }
        else
        {
            timeSpent = player.getStats().getValue(Stats.CUSTOM.get(SCStats.TICKS_SPENT_FISHING.get()));
            treasuresCaught = player.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_TREASURES.get()));
            fishMissed = player.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_FISH_MISSED.get()));
            baitUsed = player.getStats().getValue(Stats.CUSTOM.get(SCStats.BAIT_USED.get()));
        }
    }

    public record StatsData(int timeSpent, int treasuresCaught, int fishMissed, int baitUsed)
    {
        public static final Codec<StatsData> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("time_spent").forGetter(StatsData::timeSpent),
                        Codec.INT.fieldOf("treasures_caught").forGetter(StatsData::treasuresCaught),
                        Codec.INT.fieldOf("fish_missed").forGetter(StatsData::fishMissed),
                        Codec.INT.fieldOf("bait_used").forGetter(StatsData::baitUsed)
                ).apply(instance, StatsData::new)
        );
    }

    public static void renderFishEntryPage(GuiGraphicsExtractor g, FishProperties fp, ItemStack fishToDisplay,
                                           FishCaughtCounter fcc, int x, int y, int absoluteMouseX, int absoluteMouseY)
    {
        Level level = Minecraft.getInstance().level;
        Font font = Minecraft.getInstance().font;

        int mouseX = absoluteMouseX - x;
        int mouseY = absoluteMouseY - y;

        //render caught:
        //caught:
        ScreenUtils.text(g, font, translatable("gui.guide.caught"), x + 73, y + 64, 0x9c897c, false);

        //render caught count
        if (fcc == null)
        {
            //------
            ScreenUtils.text(g, font, translatable("gui.guide.not_caught"), x + 73, y + 73, 0x9c897c, false);
        }
        else
        {
            //[324]
            Component c = Component.literal("[" + fcc.count() + "]").withStyle(Style.EMPTY.withColor(0x635040));
            ScreenUtils.text(g, font, Component.empty().append(c), x + 73, y + 73, 0, false);
        }

        //render rarity (always shown)
        //rarity:
        ScreenUtils.text(g,
                font, translatable("gui.guide.rarity"),
                x + 73, y + 84, 0x9c897c, false);

        //common
        ScreenUtils.text(g,
                font, translatable("gui.guide.rarity." + fp.rarity().getSerializedName()),
                x + 73, y + 93, 0, false);

        //render bucketable
        if (!fp.catchInfo().bucketedFish().isEmpty())
        {
            BUCKET.render(g, x + 77, y + 103);
            if (mouseX > 75 && mouseX < 93 && mouseY > 105 && mouseY < 115)
                ScreenUtils.Tooltip.set(translatable("gui.guide.bucketable"));
        }

        //render almighty wormable
        if ((!fp.catchInfo().entityToSpawn().equals(Utils.holderEntity("starcatcher", "fish")) && !fp.catchInfo().alwaysSpawnEntity())
            || (fp.catchInfo().entityToSpawn().equals(Utils.holderEntity("starcatcher", "fish")) && fp.catchInfo().fish().toStack().is(SCTags.BUCKETABLE_FISHES)))
        {
            ENTITY.render(g, x + 93, y + 103);
            if (mouseX > 92 && mouseX < 107 && mouseY > 105 && mouseY < 115)
                ScreenUtils.Tooltip.set(translatable("gui.guide.entity"));
        }

        //render sword
        if (fp.catchInfo().alwaysSpawnEntity())
        {
            ALWAYS_ENTITY.render(g, x + 93, y + 103);
            if (mouseX > 92 && mouseX < 107 && mouseY > 105 && mouseY < 115)
                ScreenUtils.Tooltip.set(translatable("gui.guide.always_entity"));
        }

        //render debug fish name
        if (SCConfig.DEBUG_MINIGAME.get())
        {
            ScreenUtils.text(g, font, fp.hashCode() + "", x + 6, y + 60, SCColors.GUIDE_TEXT_DARK, false);
            ScreenUtils.text(g, font, fp.getDisplayName(), x + 6, y + 70, SCColors.GUIDE_TEXT_DARK, false);
            ScreenUtils.text(g, font, fishToDisplay.toString(), x + 6, y + 80, SCColors.GUIDE_TEXT_DARK, false);
            ScreenUtils.text(g, font, fp.catchInfo().entityToSpawn().getRegisteredName(), x + 6, y + 90, SCColors.GUIDE_TEXT_DARK, false);
        }

        int color = switch (fp.rarity())
        {
            case Rarity.TRASH, Rarity.COMMON, Rarity.NONE -> Utils.toColorI(0, 0, 0, 50);
            case Rarity.UNCOMMON -> 0xff92f28d;
            case Rarity.RARE -> 0xff78c8ff;
            case Rarity.EPIC -> 0xffc060ff;
            case Rarity.LEGENDARY, Rarity.GOLDEN ->
            {
                int colorI = Color.HSBtoRGB((float) Util.getMillis() / 10000, 1, 1);

                yield Utils.toColorI(
                        Utils.intToRed(colorI),
                        Utils.intToGreen(colorI),
                        Utils.intToBlue(colorI),
                        175
                );
            }
        };

        //render glow
        if (fcc != null)
        {
            ScreenUtils.setColor(color);
            GLOW.render(g, x + 8, y + 53, 3);
        }

        //render fish & name at the top
        if (fishToDisplay != ItemStack.EMPTY)
        {
            ScreenUtils.item(g, fishToDisplay, x + 19, y + 64, g.pose(), 3);
            ScreenUtils.scrollingText(g, font, fp.getDisplayName(), x + 28, x + 141, y + 36, SCColors.GUIDE_TEXT_DARK, false);
        }

        //render new fish icon
        if (fcc != null && fcc.hasGuideNotification())
            NEW_FISH.render(g, x + 120, y + 95);

        int yOffset = y + 132;
        int counter = 0;


        //render restrictions
        for (var restriction : fp.restrictions())
        {
            if (!restriction.isEnabled()) continue;
            counter++;
            if (counter > 6) break;
            //todo make system to allow scrolling
            boolean hoveringMain = mouseX > 0 && mouseX < 126 && mouseY > yOffset - y - 2 && mouseY < yOffset - y - 2 + 12;
            boolean hoveringBlacklist = mouseX > 128 && mouseX < 139 && mouseY > yOffset - y - 2 && mouseY < yOffset - y - 2 + 12;

            Component description = restriction.getDescription(level, fp, Minecraft.getInstance().player, AbstractFishRestriction.Context.GUIDE_ENTRY);
            List<Component> hover = restriction.getHover(level, fp, Minecraft.getInstance().player, AbstractFishRestriction.Context.GUIDE_ENTRY);
            List<Component> blacklist = restriction.getBlacklist(level, fp, Minecraft.getInstance().player, AbstractFishRestriction.Context.GUIDE_ENTRY);

            if (description.equals(Component.empty())) continue;

            ScreenUtils.scrollingText(g, font, description, x, x + 128, yOffset, SCColors.GUIDE_TEXT_DARK, false);

            //if has hover and cursor is hovering
            if (!hover.isEmpty() && hoveringMain)
                ScreenUtils.Tooltip.set(hover);

            //if blacklist then render [!]
            if (!blacklist.isEmpty())
            {
                ScreenUtils.text(g, font, "[!]", x + 129, yOffset, SCColors.GUIDE_RED, false);
                if (hoveringBlacklist)
                    ScreenUtils.Tooltip.set(blacklist);
            }

            yOffset += 12;
        }

        //render fish tooltip
        if (mouseX > 6 && mouseX < 61 && mouseY > 51 && mouseY < 105)
        {
            if (fp.catchInfo().alwaysSpawnEntity() && (fcc != null || !SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get()))
                ScreenUtils.Tooltip.set(fp.getDisplayName());
            else if (fishToDisplay != ItemStack.EMPTY)
                ScreenUtils.Tooltip.set(fishToDisplay);
        }

        //render stats tooltip (at the end because of the scissor bug)
        if (mouseX > 70 && mouseX < 132 && mouseY > 62 && mouseY < 100 && fcc != null)
        {
            List<Component> components = new ArrayList<>();
            float averageTicks = (int) ((fcc.averageTicks() / 20) * 100) / 100.0f;

            SizeAndWeight.Units unit = SCConfig.UNIT.get();
            String size = unit.getSizeAsString(fp.sizeWeight().getSizeForPercentile(fcc.percentile()));
            String weight = unit.getWeightAsString(fp.sizeWeight().getWeightForPercentile(fcc.percentile()));

            //format first catch
            Instant instant = Instant.ofEpochSecond(fcc.firstCatch());
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("hh:ma");
            String formatted = zdt.format(formatter);
            String formatted2 = zdt.format(formatter2);

            components.add(translatable("gui.guide.first"));
            components.add(Component.literal(formatted).withStyle(ChatFormatting.BOLD));
            components.add(Component.literal(formatted2).withStyle(ChatFormatting.BOLD));
            components.add(Component.literal(""));
            components.add(translatable("gui.guide.fastest").append(Component.literal((((float) fcc.fastestTicks()) / 20) + "s").withStyle(ChatFormatting.BOLD)));
            components.add(translatable("gui.guide.average").append(Component.literal(averageTicks + "s").withStyle(ChatFormatting.BOLD)));
            components.add(Component.literal(""));
            components.add(translatable("gui.guide.biggest").append(Component.literal(size).withStyle(ChatFormatting.BOLD)));
            components.add(translatable("gui.guide.heaviest").append(Component.literal(weight).withStyle(ChatFormatting.BOLD)));
            components.add(translatable("gui.guide.percentile").append(Component.literal(Starcatcher.FORMAT.format(fcc.percentile()) + "%").withStyle(ChatFormatting.BOLD)));

            ScreenUtils.Tooltip.set(components);
        }
    }

    enum MenuEntry
    {
        SETTINGS,
        COVER,
        INDEX,
        HELP,
        ENTRY,
        LAST
    }

    public static MutableComponent translatable(String key)
    {
        return Tooltips.resolveTagsToComponentFromTranslationKey(key);
    }
}
