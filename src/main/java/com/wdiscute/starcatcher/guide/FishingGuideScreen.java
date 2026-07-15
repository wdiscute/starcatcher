package com.wdiscute.starcatcher.guide;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.starcatcher.*;
import com.wdiscute.starcatcher.compat.emi.StarcatcherEmiPlugin;
import com.wdiscute.starcatcher.compat.jei.StarcatcherJeiPlugin;
import com.wdiscute.starcatcher.data.SignedGuide;
import com.wdiscute.starcatcher.fish.*;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.data.network.SBTrackFishPayload;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.messageinabottle.message.MessageScreen;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.data.network.SignGuidePayload;
import com.wdiscute.starcatcher.data.network.SBFPsSeenPayload;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class FishingGuideScreen extends Screen
{
    private static final ResourceLocation BACKGROUND_COVER = Starcatcher.rl("textures/gui/guide/background/background_cover.png");
    private static final ResourceLocation BACKGROUND_LAST_PAGE = Starcatcher.rl("textures/gui/guide/background/background_last_page.png");
    private static final ResourceLocation BACKGROUND_INDEX_FIRST = Starcatcher.rl("textures/gui/guide/background/background_index_first.png");
    private static final ResourceLocation BACKGROUND_INDEX_SECOND = Starcatcher.rl("textures/gui/guide/background/background_index_second.png");
    private static final ResourceLocation BACKGROUND_ENTRY = Starcatcher.rl("textures/gui/guide/background/background_entry.png");
    private static final ResourceLocation BACKGROUND_BASICS = Starcatcher.rl("textures/gui/guide/background/background_basics.png");

    private static final ResourceLocation COMPASS = Starcatcher.rl("textures/gui/guide/compass.png");

    private static final ResourceLocation HIGHLIGHT_LEFT = Starcatcher.rl("textures/gui/guide/highlight_page_left.png");
    private static final ResourceLocation HIGHLIGHT_RIGHT = Starcatcher.rl("textures/gui/guide/highlight_page_right.png");

    private static final ResourceLocation FISHES_IN_AREA_TOP_RIGHT_DECORATION = Starcatcher.rl("textures/gui/guide/fishes_in_area_top_right_decoration.png");
    private static final ResourceLocation FISHES_IN_AREA_BOTTOM_LEFT_DECORATION = Starcatcher.rl("textures/gui/guide/fishes_in_area_bottom_left_decoration.png");
    private static final ResourceLocation FISHES_IN_AREA_BOTTOM_DECORATION = Starcatcher.rl("textures/gui/guide/fishes_in_area_bottom_decoration.png");
    private static final ResourceLocation FISHES_IN_AREA_FISH_DECORATION = Starcatcher.rl("textures/gui/guide/fishes_in_area_fish_decoration.png");

    private static final ResourceLocation HELP_PAGE_BASICS = Starcatcher.rl("textures/gui/guide/help/help_basics.png");
    private static final ResourceLocation HELP_PAGE_SWEETSPOTS = Starcatcher.rl("textures/gui/guide/help/help_sweetspots.png");
    private static final ResourceLocation HELP_PAGE_TREASURE = Starcatcher.rl("textures/gui/guide/help/help_treasure.png");
    private static final ResourceLocation HELP_PAGE_UPGRADES = Starcatcher.rl("textures/gui/guide/help/help_upgrades.png");
    private static final ResourceLocation HELP_PAGE_TACKLE_BOX = Starcatcher.rl("textures/gui/guide/help/help_tackle_box.png");
    private static final ResourceLocation HELP_PAGE_HOOKS_BOBBERS_BAITS = Starcatcher.rl("textures/gui/guide/help/help_hooks_bobbers_baits.png");

    private static final ResourceLocation HELP_PAGE_COSMETICS = Starcatcher.rl("textures/gui/guide/help/help_cosmetics.png");
    private static final ResourceLocation HELP_PAGE_TOURNAMENTS = Starcatcher.rl("textures/gui/guide/help/help_tournaments.png");
    private static final ResourceLocation HELP_PAGE_MESSAGES = Starcatcher.rl("textures/gui/guide/help/help_messages.png");
    private static final ResourceLocation HELP_PAGE_SELLING = Starcatcher.rl("textures/gui/guide/help/help_selling.png");
    private static final ResourceLocation HELP_PAGE_AQUARIUM = Starcatcher.rl("textures/gui/guide/help/help_aquarium.png");
    private static final ResourceLocation HELP_PAGE_DISPLAY = Starcatcher.rl("textures/gui/guide/help/help_display.png");
    private static final ResourceLocation HELP_PAGE_TROPHIES = Starcatcher.rl("textures/gui/guide/help/help_trophies.png");

    private static final ResourceLocation ARROW_PREVIOUS = Starcatcher.rl("textures/gui/guide/arrow_previous.png");
    private static final ResourceLocation ARROW_PREVIOUS_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_previous_pressed.png");
    private static final ResourceLocation ARROW_PREVIOUS_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_previous_highlight.png");

    public static final ResourceLocation ARROW_LEFT = Starcatcher.rl("textures/gui/guide/arrow_left.png");
    public static final ResourceLocation ARROW_RIGHT = Starcatcher.rl("textures/gui/guide/arrow_right.png");

    private static final ResourceLocation ARROW_NEXT = Starcatcher.rl("textures/gui/guide/arrow_next.png");
    private static final ResourceLocation ARROW_NEXT_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_next_pressed.png");
    private static final ResourceLocation ARROW_NEXT_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_next_highlight.png");

    private static final ResourceLocation ARROW_INDEX = Starcatcher.rl("textures/gui/guide/arrow_index.png");
    private static final ResourceLocation ARROW_INDEX_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_index_pressed.png");
    private static final ResourceLocation ARROW_INDEX_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_index_highlight.png");

    private static final ResourceLocation NEW_FISH = Starcatcher.rl("textures/gui/guide/new_fish.png");
    private static final ResourceLocation GLOW = Starcatcher.rl("textures/gui/guide/glow.png");
    private static final ResourceLocation TRACKED = Starcatcher.rl("textures/gui/guide/tracked.png");

    private static final ResourceLocation BUCKET = Starcatcher.rl("textures/gui/guide/bucketable.png");
    private static final ResourceLocation ENTITY = Starcatcher.rl("textures/gui/guide/entity.png");
    private static final ResourceLocation ALWAYS_ENTITY = Starcatcher.rl("textures/gui/guide/always_entity.png");

    public static final int MAX_HELP_PAGES = 12;

    private final List<ItemStack> tackleBoxes = new ArrayList<>();
    private final List<ItemStack> baits = new ArrayList<>();
    private final List<ItemStack> hooksBobbers = new ArrayList<>();
    private final List<ItemStack> equipments = new ArrayList<>();
    private final List<ItemStack> aquariumInteractions = new ArrayList<>();
    private final List<ItemStack> templates = new ArrayList<>();
    private final List<ItemStack> bottles;

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
    private static ItemStack tackleIcon;
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

    boolean hasNextPage = false;

    int menu = 0;
    int page = 0;

    public boolean isSigned = false;

    final boolean inLectern;

    ClientLevel level;
    LocalPlayer player;

    List<ResourceLocation> fpsSeen = new ArrayList<>();
    List<FishProperties> entries = new ArrayList<>(999);
    List<FishProperties> trophies = new ArrayList<>(999);
    List<ItemStack> trophiesIS = new ArrayList<>(999);
    List<ItemStack> messages = new ArrayList<>(999);
    List<FishProperties> fishInArea = new ArrayList<>();
    Map<ResourceLocation, FishCaughtCounter> fishCaughtCounterMap = new HashMap<>();

    EditBox editBox;

    FishProperties trackedFP;
    ResourceLocation trackedRL;

    @Override
    protected void init()
    {
        super.init();

        imageWidth = 420;
        imageHeight = 260;

        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;

        //editbox for cover
        this.editBox = new EditBox(this.font, uiX + 240, uiY + 102, 103, 12, translatable("container.repair"));
        this.editBox.setCanLoseFocus(false);
        this.editBox.setTextColor(0xff937d70);
        this.editBox.setBordered(false);
        this.editBox.setMaxLength(20);
        this.editBox.setTextShadow(false);
        this.editBox.setCanLoseFocus(true);
        this.editBox.setValue("");
        this.addWidget(this.editBox);
        this.editBox.setEditable(true);

        entries = new ArrayList<>();

        level = Minecraft.getInstance().level;
        player = Minecraft.getInstance().player;

        fishInArea = new ArrayList<>();

        for (FishProperties fp : FishApi.getAllFPs(level))
        {
            if (fp.hasGuideEntry() && fp.calculateChance(player, player.level(), ItemStack.EMPTY, AbstractFishRestriction.Context.GUIDE_FISHES_IN_AREA) > 0)
                fishInArea.add(fp);
        }

        fishCaughtCounterMap = FishingGuideAttachment.getFishesCaught(player);

        for (FishProperties fp : FishApi.getAllFPs(level)) if (fp.hasGuideEntry()) entries.add(fp);
        entries = sortEntries(SCConfig.SORT.get(), entries, player);
        fishInArea = sortEntries(SCConfig.SORT.get(), fishInArea, player);

        //set caught trophies
        trophies = FishApi.getTrophies(level);

        trophiesIS = new ArrayList<>();
        trophies.forEach(t ->
        {
            if (!SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get() || fishCaughtCounterMap.containsKey(level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(t)))
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
                menu = 2;
                page = i / 2;
            }
        }
    }

    ResourceLocation cachedRL = null;

    public void resolveTrackedFP()
    {
        //if needs refresh
        ResourceLocation newTrackedRL = player.getData(SCDataAttachments.TRACKED_FISH);
        if (cachedFp == null || !newTrackedRL.equals(cachedRL) && player != null && level != null)
        {
            //set page to tracked fished when opening
            if (player != null && level != null)
            {
                trackedRL = newTrackedRL;
                trackedFP = level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).get(trackedRL);
            }
        }
        cachedRL = newTrackedRL;
        if (trackedFP == null)
            trackedFP = FishProperties.empty();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(key) && !editBox.canConsumeInput())
        {
            if (menu == 0)
            {
                this.onClose();
                return true;
            }
            else
            {
                menu = 0;
                page = 0;
                return true;
            }
        }

        if (keyCode == 256 && this.shouldCloseOnEsc())
        {
            if (menu == 0)
            {
                this.onClose();
                return true;
            }
            else
            {
                menu = 0;
                page = 0;
                return true;
            }
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
            switch (menu)
            {
                case 0 ->
                {
                    //index <- previous page of index
                    if (page != 0)
                    {
                        player.playSound(SoundEvents.BOOK_PAGE_TURN);
                        page--;
                        return true;
                    }
                    //go to book cover
                    else if (!inLectern || isSigned)
                    {
                        player.playSound(SoundEvents.BOOK_PAGE_TURN);
                        menu = -1;
                    }
                }
                case 1 ->
                {
                    player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    //help -> index
                    if (page == 0)
                    {
                        menu = 0;
                        page = entries.size() / 49 - 1;
                        return true;
                    }
                    //help -> previous help page
                    page--;
                    return true;
                }
                case 2 ->
                {
                    player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    //entries -> index page (if signed)
                    if (isSigned)
                    {
                        page = 0;
                        menu = 0;
                        return true;
                    }
                    //entries -> last page of help
                    if (page == 0)
                    {
                        menu = 1;
                        page = MAX_HELP_PAGES;
                        return true;
                    }
                    //entries -> previous entry
                    page--;
                    return true;
                }

                case 3 ->
                {
                    player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    //end of the book -> last page of entries
                    if (page == 0)
                    {
                        menu = 2;
                        page = entries.size() / 2;
                        return true;
                    }
                    return true;
                }
            }
        }

        //next arrow
        if (x > 336 && x < 356 && y > 202 && y < 216)
        {
            switch (menu)
            {
                case -1 ->
                {
                    //cover -> index
                    player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    menu = 0;
                    page = 0;
                    return true;
                }
                case 0 ->
                {
                    //index -> next page of index
                    player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    if (hasNextPage)
                    {
                        page++;
                        return true;
                    }
                    //index -> first page of help
                    menu = 1;
                    page = 0;
                    if (isSigned) menu = 2;
                    return true;
                }
                case 1 ->
                {
                    //help -> next page of help
                    player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    if (page != MAX_HELP_PAGES)
                    {
                        player.playSound(SoundEvents.BOOK_PAGE_TURN);
                        page++;
                        return true;
                    }
                    //index -> first page of help
                    menu = 2;
                    page = 0;
                    return true;
                }
                case 2 ->
                {
                    //entries -> next entry
                    if (entries.size() > page * 2 + 2)
                    {
                        player.playSound(SoundEvents.BOOK_PAGE_TURN);
                        page++;
                    }
                    else
                    {
                        menu = 3;
                        page = 0;
                    }
                    return true;
                }
            }
        }

        //index arrow
        if (x > 174 && x < 196 && y > 202 && y < 216)
        {
            player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 0;
            page = 0;
            return true;
        }

        //track fish left
        if (x > 50 && x < 67 && y > 111 && y < 128 && entries.size() > page * 2)
        {
            FishProperties fishProperties = entries.get(page * 2);
            ResourceLocation key = level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(fishProperties);
            if (key != null)
                PacketDistributor.sendToServer(new SBTrackFishPayload(key));
            player.playSound(SoundEvents.GLASS_HIT);
            player.playSound(SoundEvents.AMETHYST_BLOCK_HIT, 0.3f, 0.6f);
        }

        //track fish right
        if (x > 210 && x < 227 && y > 111 && y < 128 && entries.size() > page * 2 + 1)
        {
            FishProperties fishProperties = entries.get(page * 2 + 1);
            ResourceLocation key = level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(fishProperties);
            if (key != null)
                PacketDistributor.sendToServer(new SBTrackFishPayload(key));
            player.playSound(SoundEvents.GLASS_HIT);
            player.playSound(SoundEvents.AMETHYST_BLOCK_HIT, 0.3f, 0.6f);
        }

        if (button == 0)
        {
            clicked = true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //if hovering scrollable on left
        if (x > 53 && x < 189 && y > 155 && y < 200 && menu == 1 &&
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
            if (x > 212 && x < 356 && y > 155 && y < 200 && menu == 1 &&
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
                    mouseReleased(uiX + 50, uiY + 210, 0);
                    arrowPreviousPressed = true;
                    arrowPressedFromScrollDecay = 2;
                }
                else
                {
                    mouseReleased(uiX + 340, uiY + 210, 0);
                    arrowNextPressed = true;
                    arrowPressedFromScrollDecay = 2;
                }
            }
        }


        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //System.out.println("clicked on x :" + x);
        //System.out.println("clicked on y :" + y);

        //shake compass
        if (x > 17 && x < 46 && y > 37 && y < 63)
        {
            player.playSound(SoundEvents.NOTE_BLOCK_CHIME.value(), 0.1f, 1.2f);
            player.playSound(SoundEvents.GLASS_STEP, 0.4f, 1.2f);
            compassRotation += Utils.r.nextInt(40) - 20;
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
            if (entries.size() > page * 2 + 2 && menu != 3)
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
    public void tick()
    {
        super.tick();
        highlightLeftAlpha -= 0.025f;
        highlightRightAlpha -= 0.025f;
        arrowPressedFromScrollDecay--;
    }

    static float compassRotation = 0;

    private void renderCompass(GuiGraphics guiGraphics)
    {
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

        PoseStack pose = guiGraphics.pose();

        pose.pushPose();
        pose.translate(uiX + 16 + 16.5, uiY + 16 + 34.5, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(-compassRotation - 45 - 180));
        pose.translate(-16, -16, 0);
        guiGraphics.blit(COMPASS, 0, 0, 0, 0, 32, 32, 32, 32);
        pose.popPose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        resolveTrackedFP();
        editBox.setEditable(false);

        switch (menu)
        {
            //render settings screen
            case -99 ->
            {
                Minecraft.getInstance().setScreen(new SettingsScreen());
                return;
            }

            //sign page
            case -1 ->
            {
                RenderSystem.enableBlend();
                renderImage(guiGraphics, BACKGROUND_COVER);
                RenderSystem.disableBlend();
                renderCoverText(guiGraphics, mouseX, mouseY);
                renderCompass(guiGraphics);
            }

            //render index
            case 0 ->
            {
                RenderSystem.enableBlend();
                if (page == 0) renderImage(guiGraphics, BACKGROUND_INDEX_FIRST);
                else renderImage(guiGraphics, BACKGROUND_INDEX_SECOND);
                RenderSystem.disableBlend();
                renderIndex(guiGraphics, mouseX, mouseY);
                renderCompass(guiGraphics);
            }

            //render help pages
            case 1 ->
            {
                RenderSystem.enableBlend();
                renderImage(guiGraphics, BACKGROUND_BASICS);
                RenderSystem.disableBlend();
                renderTheBasics(guiGraphics, mouseX, mouseY);
                renderCompass(guiGraphics);
            }

            //render entries
            case 2 ->
            {
                RenderSystem.enableBlend();
                renderImage(guiGraphics, BACKGROUND_ENTRY);
                RenderSystem.disableBlend();
                renderEntry(guiGraphics, mouseX, mouseY, 52, page * 2);
                renderEntry(guiGraphics, mouseX, mouseY, 212, page * 2 + 1);
                renderCompass(guiGraphics);
                guiGraphics.drawString(this.font, page + 1 + "/" + ((entries.size() + 1) / 2), uiX + 213, uiY + 206, 0x9c897c, false);
            }

            case 3 ->
            {
                RenderSystem.enableBlend();
                renderImage(guiGraphics, BACKGROUND_LAST_PAGE);
                RenderSystem.disableBlend();
                renderCompass(guiGraphics);
            }
        }

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //previous arrow should not render on book cover
        if (menu != -1)
        {
            //previous arrow
            if (x > 49 && x < 69 && y > 203 && y < 217)
                renderImage(guiGraphics, ARROW_PREVIOUS_HIGHLIGHT);
            renderImage(guiGraphics, arrowPreviousPressed ? ARROW_PREVIOUS_PRESSED : ARROW_PREVIOUS);
        }

        //index should not render on book cover and first page of index
        if (menu != -1 && !(menu == 0 && page == 0))
        {
            if (x > 174 && x < 196 && y > 202 && y < 216)
                renderImage(guiGraphics, ARROW_INDEX_HIGHLIGHT);
            renderImage(guiGraphics, arrowIndexPressed ? ARROW_INDEX_PRESSED : ARROW_INDEX);
        }

        //next arrow
        if (menu != 3)
        {
            if (x > 336 && x < 356 && y > 202 && y < 216)
                renderImage(guiGraphics, ARROW_NEXT_HIGHLIGHT);
            renderImage(guiGraphics, arrowNextPressed ? ARROW_NEXT_PRESSED : ARROW_NEXT);
        }

        if (arrowPressedFromScrollDecay == 0)
        {
            arrowNextPressed = false;
            arrowPreviousPressed = false;
        }
        clicked = false;
    }

    public void renderCoverText(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        String s = I18n.get("gui.guide.sign");

        int width1 = font.width(s) + 15;
        editBox.setEditable(true);

        //draw fitting rectangle
        guiGraphics.fill(uiX + 285 - width1 / 2, uiY + 117, uiX + 285 + width1 / 2, uiY + 117 + 12, 0xffb4a697);
        guiGraphics.renderOutline(uiX + 285 - width1 / 2, uiY + 117, width1, 12, 0xff937d70);
        renderCenteredString(guiGraphics, font, Component.literal(s), uiX + 285, uiY + 119, 0x937d70);

        //if hovering sign rectangle
        if (mouseX > uiX + 285 - width1 / 2 && mouseX < uiX + 285 + width1 / 2 && mouseY > uiY + 117 && mouseY < uiY + 117 + 12)
        {

            guiGraphics.renderTooltip(font, List.of(translatable("gui.guide.sign.locked.0"), translatable("gui.guide.sign.locked.1")), Optional.empty(), mouseX, mouseY);
            guiGraphics.renderOutline(uiX + 285 - width1 / 2, uiY + 117, width1, 12, 0xff000000);
            if (clicked)
            {
                SignGuidePayload payload = new SignGuidePayload(editBox.getValue());
                PacketDistributor.sendToServer(payload);
                onClose();
            }
        }

        editBox.render(guiGraphics, mouseX, mouseY, 0);
    }

    private void renderHelpText(GuiGraphics guiGraphics, int page)
    {
        for (int i = 0; i < 40; i++)
        {
            if (!I18n.exists("gui.guide.page." + page + ".left." + i)) break;
            Component comp = translatable("gui.guide.page." + page + ".left." + i).copy().withStyle(Style.EMPTY.withColor(SCColors.GUIDE_TEXT_SEMI_DARK));
            guiGraphics.drawString(this.font, comp, uiX + 52, uiY + 10 * i + 13, 0xff000000, false);
        }

        for (int i = 0; i < 40; i++)
        {
            if (!I18n.exists("gui.guide.page." + page + ".right." + i)) break;
            Component comp = translatable("gui.guide.page." + page + ".right." + i).copy().withStyle(Style.EMPTY.withColor(SCColors.GUIDE_TEXT_SEMI_DARK));
            guiGraphics.drawString(this.font, comp, uiX + 213, uiY + 10 * i + 13, 0xff000000, false);
        }

        if (I18n.exists("gui.guide.page." + page + ".left.title"))
            renderCenteredString(guiGraphics, this.font, translatable("gui.guide.page." + page + ".left.title"), uiX + 116, uiY + 45, SCColors.GUIDE_TEXT_DARK);

        if (I18n.exists("gui.guide.page." + page + ".right.title"))
            renderCenteredString(guiGraphics, this.font, translatable("gui.guide.page." + page + ".right.title"), uiX + 270, uiY + 45, SCColors.GUIDE_TEXT_DARK);

    }

    private void renderTheBasics(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        //shitty workaround for signed guides
        guiGraphics.drawString(this.font, page + "/" + MAX_HELP_PAGES, uiX + 213, uiY + 206, 0x9c897c, false);

        renderHelpText(guiGraphics, page);

        switch (page)
        {
            //the basics
            case 0 ->
            {
                renderImage(guiGraphics, HELP_PAGE_BASICS);
                renderItemWithHoverAndEmi(guiGraphics, rodIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //sweetspots
            case 1 ->
            {
                renderImage(guiGraphics, HELP_PAGE_TACKLE_BOX);

                //tackle boxes icon
                renderItemWithHoverAndEmi(guiGraphics, tackleBoxIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //left page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 54, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 171, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 54 && mouseX < uiX + 64 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    leftPageScroll--;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }
                if (clicked && mouseX > uiX + 175 && mouseX < uiX + 185 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                    leftPageScroll++;
                }

                //scrollable tackle boxes icons
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 72 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = tackleBoxes.get(Math.abs((leftPageScroll + i) % tackleBoxes.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
                        if (clicked)
                            displayRecipe(stack);
                    }

                    //scrollable background fill
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xffb4a697);
                }
            }

            //treasures
            case 2 ->
            {
                renderImage(guiGraphics, HELP_PAGE_TREASURE);
                renderItemWithHoverAndEmi(guiGraphics, treasureIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //sweet-spot types
            case 3 ->
            {
                renderImage(guiGraphics, HELP_PAGE_SWEETSPOTS);
                renderItemWithHoverAndEmi(guiGraphics, sweetspotsIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //upgrades
            case 4 ->
            {
                renderImage(guiGraphics, HELP_PAGE_UPGRADES);
                renderItemWithHoverAndEmi(guiGraphics, upgradeIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //hooks & bobbers & baits
            case 5 ->
            {
                renderImage(guiGraphics, HELP_PAGE_HOOKS_BOBBERS_BAITS);

                //hook icon
                renderItemWithHoverAndEmi(guiGraphics, hookIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //left page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 54, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 171, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 54 && mouseX < uiX + 64 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    leftPageScroll--;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }
                if (clicked && mouseX > uiX + 175 && mouseX < uiX + 185 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                    leftPageScroll++;
                }

                //hooks and bobbers items
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 72 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = hooksBobbers.get(Math.abs((leftPageScroll + i) % hooksBobbers.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
                        if (clicked)
                            displayRecipe(stack);
                    }

                    //scrollable background fill
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xffb4a697);
                }


                //bait icon
                renderItemWithHoverAndEmi(guiGraphics, baitIcon, uiX + 321, uiY + 39, mouseX, mouseY);

                //right page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 224, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 332, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 225 && mouseX < uiX + 235 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    rightPageScroll--;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }
                if (clicked && mouseX > uiX + 336 && mouseX < uiX + 346 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    rightPageScroll++;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }


                //scrollable baits icons
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 238 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = baits.get(Math.abs((rightPageScroll + i) % baits.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
                        if (clicked)
                            displayRecipe(stack);
                    }
                    //scrollable background fill
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xffb4a697);
                }

            }


            //cosmetics
            case 6 ->
            {
                renderImage(guiGraphics, HELP_PAGE_COSMETICS);
                renderItemWithHoverAndEmi(guiGraphics, cosmeticsIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //left page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 54, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 171, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 54 && mouseX < uiX + 64 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    leftPageScroll--;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }
                if (clicked && mouseX > uiX + 175 && mouseX < uiX + 185 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                    leftPageScroll++;
                }

                //hooks and bobbers items
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 72 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = equipments.get(Math.abs((leftPageScroll + i) % equipments.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
                        if (clicked)
                            displayRecipe(stack);
                    }

                    //scrollable background fill
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xffb4a697);
                }


                //tackle icon
                renderItemWithHoverAndEmi(guiGraphics, tackleIcon, uiX + 321, uiY + 39, mouseX, mouseY);

                //right page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 224, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 332, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 225 && mouseX < uiX + 235 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    rightPageScroll--;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }
                if (clicked && mouseX > uiX + 336 && mouseX < uiX + 346 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    rightPageScroll++;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }


                //scrollable tackles icons
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 238 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = templates.get(Math.abs((rightPageScroll + i) % templates.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
                        if (clicked)
                            displayRecipe(stack);
                    }
                    //scrollable background fill
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xffb4a697);
                }


            }

            //tournaments
            case 7 ->
            {
                renderImage(guiGraphics, HELP_PAGE_TOURNAMENTS);
                renderItemWithHoverAndEmi(guiGraphics, standIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //message-in-a-bottle
            case 8 ->
            {
                renderImage(guiGraphics, HELP_PAGE_MESSAGES);
                renderItemWithHoverAndEmi(guiGraphics, letterIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //letter, bottled letter, message-in-a-bottle, message
                for (int i = 0; i < 4; i++)
                {
                    renderItemWithHoverAndEmi(guiGraphics, bottles.get(i), uiX + 241 + (i * 24), uiY + 130, mouseX, mouseY);
                    guiGraphics.renderOutline(uiX + 239 + (i * 24), uiY + 128, 20, 20, 0xff9c897c);
                }


                //scrollable found messages
                //right page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 224, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 332, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 225 && mouseX < uiX + 235 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    rightPageScroll--;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }
                if (clicked && mouseX > uiX + 336 && mouseX < uiX + 346 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    rightPageScroll++;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }

                //messages found text
                renderCenteredString(guiGraphics, font, translatable("gui.guide.page.8.left.messages_found"),
                        uiX + 286, uiY + 157, SCColors.GUIDE_TEXT_DARK, false);

                //scrollable messages icons
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 238 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = messages.get(Math.abs((rightPageScroll + i) % messages.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
                        Message message = SCDataComponents.getOrDefault(stack, SCDataComponents.MESSAGE, Message.DEFAULT);
                        //if clicked open message screen
                        if (clicked && !message.equals(Message.DEFAULT))
                            Minecraft.getInstance().setScreen(
                                    new MessageScreen(message, this));
                    }
                    //scrollable background fill
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xffb4a697);
                }
            }

            //selling bin
            case 9 ->
            {
                renderImage(guiGraphics, HELP_PAGE_SELLING);
                renderItemWithHoverAndEmi(guiGraphics, sellingBinIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //aquariums
            case 10 ->
            {
                renderImage(guiGraphics, HELP_PAGE_AQUARIUM);
                renderItemWithHoverAndEmi(guiGraphics, aquariumIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //left page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 54, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 171, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 54 && mouseX < uiX + 64 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    leftPageScroll--;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }
                if (clicked && mouseX > uiX + 175 && mouseX < uiX + 185 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                    leftPageScroll++;
                }

                //hooks and bobbers items
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 72 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = aquariumInteractions.get(Math.abs((leftPageScroll + i) % aquariumInteractions.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
                        if (clicked)
                            displayRecipe(stack);
                    }

                    //scrollable background fill
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xffb4a697);
                }
            }

            //selling bin
            case 11 ->
            {
                renderImage(guiGraphics, HELP_PAGE_DISPLAY);
                renderItemWithHoverAndEmi(guiGraphics, displayIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                renderItemWithHoverAndEmi(guiGraphics, guideIcon, uiX + 321, uiY + 39, mouseX, mouseY);
            }


            //stats and trophies
            case 12 ->
            {
                renderImage(guiGraphics, HELP_PAGE_TROPHIES);
                renderItemWithHoverAndEmi(guiGraphics, letterIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //stats
                //fishes caught
                guiGraphics.drawString(font, translatable("gui.guide.stats.fishes_caught"), uiX + 53, uiY + 63, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, fishesCaught + "", uiX + 165, uiY + 63, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //trash
                guiGraphics.drawString(font, translatable("gui.guide.stats.trash_caught"), uiX + 53, uiY + 73, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, trashCaught + "", uiX + 165, uiY + 73, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //common
                guiGraphics.drawString(font, translatable("gui.guide.stats.common_caught"), uiX + 53, uiY + 83, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, commonCaught + "", uiX + 165, uiY + 83, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //uncommon
                guiGraphics.drawString(font, translatable("gui.guide.stats.uncommon_caught"), uiX + 53, uiY + 93, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, uncommonCaught + "", uiX + 165, uiY + 93, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //rare
                guiGraphics.drawString(font, translatable("gui.guide.stats.rare_caught"), uiX + 53, uiY + 103, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, rareCaught + "", uiX + 165, uiY + 103, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //epic
                guiGraphics.drawString(font, translatable("gui.guide.stats.epic_caught"), uiX + 53, uiY + 113, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, epicCaught + "", uiX + 165, uiY + 113, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //rare
                guiGraphics.drawString(font, translatable("gui.guide.stats.legendary_caught"), uiX + 53, uiY + 123, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, legendaryCaught + "", uiX + 165, uiY + 123, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //golden fishes caught
                guiGraphics.drawString(font, translatable("gui.guide.stats.golden_fishes_caught"), uiX + 53, uiY + 133, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, goldenFishesCaught + "", uiX + 165, uiY + 133, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //perfect fishes caught
                guiGraphics.drawString(font, translatable("gui.guide.stats.perfect_fishes_caught"), uiX + 53, uiY + 143, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, perfectFishesCaught + "", uiX + 165, uiY + 143, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //fishes missed
                guiGraphics.drawString(font, translatable("gui.guide.stats.starcaught_fish_missed"), uiX + 53, uiY + 153, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, fishMissed + "", uiX + 165, uiY + 153, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //treasures caught
                guiGraphics.drawString(font, translatable("gui.guide.stats.treasures_caught"), uiX + 53, uiY + 163, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, treasuresCaught + "", uiX + 165, uiY + 163, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //bait used
                guiGraphics.drawString(font, translatable("gui.guide.stats.bait_used"), uiX + 53, uiY + 173, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                guiGraphics.drawString(font, baitUsed + "", uiX + 165, uiY + 173, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //time spent
                guiGraphics.drawString(font, translatable("gui.guide.stats.time_spent"), uiX + 53, uiY + 183, SCColors.GUIDE_TEXT_SEMI_DARK, false);
                String text = Utils.calculateRealLifeTimeFromTicks(timeSpent);
                guiGraphics.drawString(font, text.isEmpty() ? "---" : text, uiX + 130, uiY + 183, SCColors.GUIDE_TEXT_SEMI_DARK, false);

                //trophy icon
                renderItemWithHoverAndEmi(guiGraphics, trophyIcon, uiX + 321, uiY + 39, mouseX, mouseY);

                //right page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 224, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 332, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 225 && mouseX < uiX + 235 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    rightPageScroll--;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }
                if (clicked && mouseX > uiX + 336 && mouseX < uiX + 346 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                {
                    rightPageScroll++;
                    player.playSound(SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON, 0.3f, 1f);
                }


                //scrollable trophy icons
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 238 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = trophiesIS.get(Math.abs((rightPageScroll + i) % trophiesIS.size()));
                    FishProperties fp = trophies.get(Math.abs((rightPageScroll + i) % trophies.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        FishCaughtCounter fishCaughtCounter = fishCaughtCounterMap.get(fp.toLoc(level));
                        ArrayList<Component> components = new ArrayList<>(getCachedTooltipForHoverEntry(fp, fishCaughtCounter == null ? 0 : fishCaughtCounter.count()));
                        if (!fp.equals(FishProperties.empty()))
                            guiGraphics.renderTooltip(this.font, components, Optional.empty(), mouseX, mouseY);

                        //if clicked on a trophy, display FP
                        if (clicked && !fp.equals(FishProperties.empty()))
                            Minecraft.getInstance().setScreen(new IsolatedFPScreen(fp, this));
                    }
                    //scrollable background fill
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xffb4a697);
                }
            }
        }
    }

    private void renderIndex(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {

//        if (Minecraft.getInstance().level.getDayTime() % 20 == 0)
//        {
//            var f = new ArrayList<>(fishInArea);
//            f.add(FishProperties.DEFAULT);
//            fishInArea = f;
//        }

        //render first line index
        int xx = uiX + 55;
        if (page == 0)
        {
            for (int i = 0; i < 7; i++)
            {
                renderItem(indexEntries.get(i).getFirst(), xx + i * 20, uiY + 47, 1);

                if (mouseX > xx + (i * 20) - 2 && mouseX < xx + (i * 20) + 17 && mouseY > uiY + 47 - 2 && mouseY < uiY + 47 + 17)
                    guiGraphics.renderTooltip(this.font, translatable(indexEntries.get(i).getSecond()), mouseX, mouseY);

                if (clicked && mouseX > xx + (i * 20) - 2 && mouseX < xx + (i * 20) + 17 && mouseY > uiY + 47 - 2 && mouseY < uiY + 47 + 17)
                {
                    clicked = false;
                    player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    menu = 1;
                    switch (i)
                    {
                        case 0 -> page = 0;
                        case 1 -> page = 1;
                        case 2 -> page = 2;
                        case 3 -> page = 3;
                        case 4 -> page = 4;
                        case 5 -> page = 5;
                    }
                    if (i == 6) menu = -99;
                }
            }

            xx = uiX + 55;
            //render second line index
            for (int i = 7; i < 14; i++)
            {
                renderItem(indexEntries.get(i).getFirst(), xx + (i - 7) * 20, uiY + 47 + 20, 1);

                if (mouseX > xx + ((i - 7) * 20) - 2 && mouseX < xx + ((i - 7) * 20) + 17 && mouseY > uiY + 47 + 20 - 2 && mouseY < uiY + 47 + 20 + 17)
                {
                    guiGraphics.renderTooltip(this.font, translatable(indexEntries.get(i).getSecond()), mouseX, mouseY);
                    if (clicked)
                    {
                        clicked = false;
                        player.playSound(SoundEvents.BOOK_PAGE_TURN);
                        menu = 1;
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

        //[sort] text
        if (page == 0)
        {
            renderCenteredString(guiGraphics, this.font, translatable("gui.guide.sort"), uiX + 171, uiY + 88, 0x937d70);
            if (mouseX > uiX + 145 && mouseX < uiX + 190 && mouseY > uiY + 86 && mouseY < uiY + 96)
            {
                guiGraphics.renderTooltip(this.font, translatable(SCConfig.SORT.get().getTranslationKey()), mouseX, mouseY);
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
                    if (fishInArea.size() > 6) renderImage(guiGraphics, FISHES_IN_AREA_TOP_RIGHT_DECORATION);

                    int numberOfRows = (fishInArea.size() - 1) / 7 + 1;

                    //render bottom decoration if there's space
                    if (numberOfRows < 3)
                        renderImage(guiGraphics, FISHES_IN_AREA_BOTTOM_DECORATION);


                    //render bottom left thingy, offset by the number of rows
                    if (!fishInArea.isEmpty() && numberOfRows < 5)
                        renderImage(guiGraphics, FISHES_IN_AREA_BOTTOM_LEFT_DECORATION, 0, (numberOfRows - 1) * 20 + 20);

                    //render fish skeleton unless there's no space for it
                    int xFishSkeletonOffset = 0;
                    if (fishInArea.size() % 7 > 4 || fishInArea.size() % 7 == 0) xFishSkeletonOffset = 20;
                    if (numberOfRows < 5)
                        renderImage(guiGraphics, FISHES_IN_AREA_FISH_DECORATION, 0, (numberOfRows - 1) * 20 + xFishSkeletonOffset + 20);
                    if (numberOfRows == 5 && fishInArea.size() % 7 < 5 && fishInArea.size() % 7 != 0)
                        renderImage(guiGraphics, FISHES_IN_AREA_FISH_DECORATION, 0, (numberOfRows - 1) * 20 + xFishSkeletonOffset + 20);
                }
            }

        }

        hasNextPage = false;
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
        hasNextPage = true;
    }

    private void renderFishIndex(GuiGraphics guiGraphics, int xOffset, int yOffset, int mouseX, int mouseY, FishProperties fp)
    {
        ResourceLocation rl = fp.toLoc(level);
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
            menu = 2;
            page = entries.indexOf(fp) / 2;

            if (entries.indexOf(fp) % 2 == 0)
                highlightLeftAlpha = 0.5f;
            else
                highlightRightAlpha = 0.5f;
        }

        //render fill
        guiGraphics.fill(xOffset - 1, yOffset - 1, xOffset + 17, yOffset + 17, SCColors.GUIDE_BACKGROUND_DARK);

        //glow color
        int color = switch (fp.rarity())
        {
            case TRASH, NONE, Rarity.COMMON -> FastColor.ARGB32.color(0, -1);
            case Rarity.UNCOMMON -> FastColor.ARGB32.color(255, 0x92f28d);
            case Rarity.RARE -> FastColor.ARGB32.color(255, 0x78c8ff);
            case Rarity.EPIC -> FastColor.ARGB32.color(255, 0xc060ff);
            case Rarity.LEGENDARY, Rarity.GOLDEN ->
                    FastColor.ARGB32.color(175, Color.HSBtoRGB((float) Util.getMillis() / 10000, 1, 1));
        };

        float red = FastColor.ARGB32.red(color) / 255f;
        float green = FastColor.ARGB32.green(color) / 255f;
        float blue = FastColor.ARGB32.blue(color) / 255f;
        float alpha = FastColor.ARGB32.alpha(color) / 255f;

        guiGraphics.setColor(red, green, blue, alpha);

        //render glow
        RenderSystem.enableBlend();
        guiGraphics.blit(
                GLOW, xOffset - 1, yOffset - 1,
                0, 0, 18, 18, 18, 18);
        RenderSystem.disableBlend();
        guiGraphics.setColor(1, 1, 1, 1);

        //render fish with missingno if not caught
        if (caught != 0 || !SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get())
            renderItem(is, xOffset, yOffset, 1);
        else
            renderItem(new ItemStack(SCItems.MISSINGNO.get()), xOffset, yOffset, 1);

        //render fish notification icon
        if (fcc != null && fcc.hasGuideNotification() && !fpsSeen.contains(FishApi.getKey(level, fp)))
            guiGraphics.renderOutline(xOffset - 1, yOffset - 1, 18, 18, 0xffc58c44);
        //guiGraphics.blit(STAR, xOffset + 10, yOffset + 7, 0, 0, 10, 10, 10, 10);


        //render tooltip
        if (mouseX > xOffset - 3 && mouseX < xOffset + 21 - 3 && mouseY > yOffset - 3 && mouseY < yOffset + 21 - 3)
        {
            ArrayList<Component> components = new ArrayList<>(getCachedTooltipForHoverEntry(fp, caught));
            components.add(1, translatable("gui.guide.rarity." + fp.rarity().getSerializedName()));
            components.add(translatable("gui.guide.click").withStyle(ChatFormatting.DARK_GRAY));

            guiGraphics.renderTooltip(this.font, components, Optional.empty(), mouseX, mouseY);

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
                components.add(translatable(fp.catchInfo().fish().toStack().getDescriptionId()));

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

    private void renderEntry(GuiGraphics guiGraphics, int mouseX, int mouseY, int xOffset, int entry)
    {
        if (level == null) level = getMinecraft().level;

        if (entries.size() <= entry) return;

        FishProperties fp = entries.get(entry);

        ResourceLocation loc = fp.toLoc(level);
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
            guiGraphics.blit(TRACKED,
                    uiX + 112 + xOffset, uiY + 20,
                    48, 48,
                    0, 0,
                    48, 48,
                    48, 48);

            //sound on clicking
            if (x > 124 + xOffset && x < 145 + xOffset && y > 44 && y < 64 && clicked)
                Minecraft.getInstance().player.playSound(SoundEvents.AMETHYST_BLOCK_HIT, 0.4f, 1.5f);
            //render golden spyglass if tracked
            int xxx = -1;
            int yyy = 111;
            guiGraphics.fill(uiX + xxx + xOffset, uiY + yyy, uiX + 16 + xxx + xOffset, uiY + 16 + yyy, SCColors.GUIDE_TEXT);
            guiGraphics.renderItem(SPYGLASS_GOLDEN, uiX + xOffset - 1, uiY + 111);
        }
        else
        {
            //render spyglass
            guiGraphics.renderItem(SPYGLASS, uiX + xOffset - 1, uiY + 111);
        }

        //render spyglass hover text
        if (x > xOffset - 3 && x < 17 + xOffset - 2 && y > 111 && y < 128)
            guiGraphics.renderTooltip(font, translatable("gui.guide.track"), mouseX, mouseY);


        //white highlight on jumping to
        if (highlightRightAlpha > 0)
        {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1, 1, 1, highlightRightAlpha);
            renderImage(guiGraphics, HIGHLIGHT_RIGHT);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.disableBlend();
        }

        if (highlightLeftAlpha > 0)
        {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1, 1, 1, highlightLeftAlpha);
            renderImage(guiGraphics, HIGHLIGHT_LEFT);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.disableBlend();
        }
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        renderImage(guiGraphics, rl, 0, 0);
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl, int xOffset, int yOffset)
    {
        guiGraphics.blit(rl, uiX + xOffset, uiY + yOffset, 0, 0, 420, 260, 420, 260);
    }

    private static void renderItem(ItemStack stack, int x, int y)
    {
        renderItem(stack, x, y, 3);
    }


    private void renderItemWithHoverAndEmi(GuiGraphics guiGraphics, ItemStack stack, int x, int y, int mouseX, int mouseY)
    {
        renderItem(stack, x, y, 1);

        if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
        {
            guiGraphics.renderTooltip(font, stack, mouseX, mouseY);
            if (clicked)
                displayRecipe(stack);
        }

    }

    public static void renderItem(ItemStack stack, int x, int y, float scale)
    {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        LivingEntity entity = mc.player;

        if (!stack.isEmpty())
        {
            ItemRenderer itemRenderer = mc.getItemRenderer();
            BakedModel bakedmodel = itemRenderer.getModel(stack, level, entity, 234234);

            PoseStack pose = new PoseStack();

            pose.pushPose();
            pose.translate((float) (x + 8), (float) (y + 8), (float) (150));

            pose.scale(16F * scale, -16F * scale, 16F * scale);
            boolean usesBlockLight = !bakedmodel.usesBlockLight();
            if (usesBlockLight)
            {
                Lighting.setupForFlatItems();
            }

            itemRenderer.render(
                    stack, ItemDisplayContext.GUI, false, pose, mc.renderBuffers().bufferSource(),
                    15728880, OverlayTexture.NO_OVERLAY, bakedmodel);

            //flush()
            RenderSystem.disableDepthTest();
            mc.renderBuffers().bufferSource().endBatch();
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
        PacketDistributor.sendToServer(new SBFPsSeenPayload(fpsSeen));
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
        //FLUID_UP("gui.guide.sort.fluid_up"),
        //FLUID_DOWN("gui.guide.sort.fluid_down"),
        //SEASON_UP("gui.guide.sort.season_up"),
        //SEASON_DOWN("gui.guide.sort.season_down")
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
            int lenght = vals.length - 2;
            if (ModList.get().isLoaded("sereneseasons") || ModList.get().isLoaded("eclipticseasons")) lenght += 2;

            if (this.ordinal() == 0) return vals[lenght - 1];
            return vals[(this.ordinal() - 1) % lenght];
        }

        public Sort next()
        {
            int lenght = vals.length - 2;
            if (ModList.get().isLoaded("sereneseasons") || ModList.get().isLoaded("eclipticseasons")) lenght += 2;

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
            //sort alphabetical first
            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(o -> BuiltInRegistries.ITEM.getKey(o.catchInfo().fish().toItem()).getPath())).toList();

            List<FishProperties> entriesSorted = new ArrayList<>();
            List<String> allNamespaces = new ArrayList<>();

            for (FishProperties fp : entriesToSort)
            {
                String namespace = BuiltInRegistries.ITEM.getKey(fp.catchInfo().fish().toItem()).getNamespace();
                if (!allNamespaces.contains(namespace)) allNamespaces.add(namespace);
            }

            for (String s : allNamespaces)
            {
                for (FishProperties fp : entriesToSort)
                {
                    String namespace = BuiltInRegistries.ITEM.getKey(fp.catchInfo().fish().toItem()).getNamespace();
                    if (namespace.equals(s)) entriesSorted.add(fp);
                }
            }

            entriesToSort = sort.equals(Sort.MOD_UP) ? entriesSorted : entriesSorted.reversed();
        }

        //caught
        if (sort.equals(Sort.CAUGHT_UP) || sort.equals(Sort.CAUGHT_DOWN))
        {
            //sort alphabetical first
            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(o -> BuiltInRegistries.ITEM.getKey(o.catchInfo().fish().toItem()).getPath())).toList();

            //add all fishes caught to start
            Map<ResourceLocation, FishCaughtCounter> fishesCaught = fishCaughtCounterMap;

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

        return entriesToSort;
    }

    public FishingGuideScreen(boolean inLectern, SignedGuide signedGuide)
    {
        super(Component.empty());
        this.inLectern = inLectern;
        if (signedGuide == null)
        {
            this.isSigned = false;
            this.fishCaughtCounterMap = SCDataAttachments.get(Minecraft.getInstance().player, SCDataAttachments.FISHING_GUIDE).fishesCaught;
        }
        else
        {
            this.isSigned = true;
            this.fishCaughtCounterMap = signedGuide.fishesCaught();
        }

        rodIcon = new ItemStack(SCItems.ROD.get());
        sweetspotsIcon = new ItemStack(SCItems.AURORA.get());
        treasureIcon = new ItemStack(Items.DIAMOND);
        cosmeticsIcon = new ItemStack(SCBlocks.FISHERMAN_HAT_BLUE.get());
        tackleBoxIcon = new ItemStack(SCBlocks.TACKLE_BOX.get());
        baitIcon = new ItemStack(SCItems.CHERRY_BAIT.get());
        tackleIcon = new ItemStack(SCItems.PEARL_SMITHING_TEMPLATE.get());
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
        BuiltInRegistries.ITEM.getTag(SCTags.TACKLE_BOXES).ifPresent(o -> o.stream().forEach(i -> tackleBoxes.add(i.value().getDefaultInstance())));
        BuiltInRegistries.ITEM.getTag(SCTags.HOOKS).ifPresent(o -> o.stream().forEach(i -> hooksBobbers.add(i.value().getDefaultInstance())));
        BuiltInRegistries.ITEM.getTag(SCTags.BOBBERS).ifPresent(o -> o.stream().forEach(i -> hooksBobbers.add(i.value().getDefaultInstance())));
        BuiltInRegistries.ITEM.getTag(SCTags.BAITS).ifPresent(o -> o.stream().forEach(i -> baits.add(i.value().getDefaultInstance())));
        BuiltInRegistries.ITEM.getTag(SCTags.TEMPLATES).ifPresent(o -> o.stream().forEach(i -> templates.add(i.value().getDefaultInstance())));
        BuiltInRegistries.ITEM.getTag(SCTags.EQUIPMENTS).ifPresent(o -> o.stream().forEach(i -> equipments.add(i.value().getDefaultInstance())));
        BuiltInRegistries.ITEM.getTag(SCTags.HATS).ifPresent(o -> o.stream().forEach(i -> equipments.add(i.value().getDefaultInstance())));

        Optional<HolderSet.Named<Item>> interactions = BuiltInRegistries.ITEM.getTag(SCTags.AQUARIUM_INTERACTIONS);
        interactions.ifPresent(h -> h.stream().forEach(o -> aquariumInteractions.add(new ItemStack(o.value()))));
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

        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null)
            return;

        List<Pair<Rarity, FishCaughtCounter>> list = new ArrayList<>();

        for (Map.Entry<ResourceLocation, FishCaughtCounter> entry : fishCaughtCounterMap.entrySet())
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
            timeSpent = Minecraft.getInstance().player.getStats().getValue(Stats.CUSTOM.get(SCStats.TICKS_SPENT_FISHING.get()));
            treasuresCaught = Minecraft.getInstance().player.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_TREASURES.get()));
            fishMissed = Minecraft.getInstance().player.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_FISH_MISSED.get()));
            baitUsed = Minecraft.getInstance().player.getStats().getValue(Stats.CUSTOM.get(SCStats.BAIT_USED.get()));
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

    public static void renderFishEntryPage(GuiGraphics guiGraphics, FishProperties fp, ItemStack fishToDisplay,
                                           FishCaughtCounter fcc, int x, int y, int absoluteMouseX, int absoluteMouseY)
    {
        Level level = Minecraft.getInstance().level;
        Font font = Minecraft.getInstance().font;

        int mouseX = absoluteMouseX - x;
        int mouseY = absoluteMouseY - y;

        //render caught:
        //caught:
        guiGraphics.drawString(
                font, translatable("gui.guide.caught"),
                x + 73, y + 64, 0x9c897c, false);

        //render caught count
        if (fcc == null)
        {
            //------
            guiGraphics.drawString(
                    font, translatable("gui.guide.not_caught"),
                    x + 73, y + 73, 0x9c897c, false);
        }
        else
        {
            //[324]
            Component c = Component.literal("[" + fcc.count() + "]").withStyle(Style.EMPTY.withColor(0x635040));
            guiGraphics.drawString(font, Component.empty().append(c), x + 73, y + 73, 0, false);
        }

        //render rarity (always shown)
        //rarity:
        guiGraphics.drawString(
                font, translatable("gui.guide.rarity"),
                x + 73, y + 84, 0x9c897c, false);
        //common
        guiGraphics.drawString(
                font, translatable("gui.guide.rarity." + fp.rarity().getSerializedName()),
                x + 73, y + 93, 0, false);

        //render bucketable
        if (!fp.catchInfo().bucketedFish().isEmpty())
        {
            guiGraphics.blit(BUCKET, x + 77, y + 103, 0, 0, 14, 14, 14, 14);
            if (mouseX > 75 && mouseX < 93 && mouseY > 105 && mouseY < 115)
                guiGraphics.renderTooltip(font, translatable("gui.guide.bucketable"), absoluteMouseX, absoluteMouseY);
        }

        //render almighty wormable
        if ((!fp.catchInfo().entityToSpawn().equals(Utils.holderEntity("starcatcher", "fish")) && !fp.catchInfo().alwaysSpawnEntity())
            || (fp.catchInfo().entityToSpawn().equals(Utils.holderEntity("starcatcher", "fish")) && fp.catchInfo().fish().toStack().is(SCTags.BUCKETABLE_FISHES)))
        {
            guiGraphics.blit(ENTITY, x + 93, y + 103, 0, 0, 14, 14, 14, 14);
            if (mouseX > 92 && mouseX < 107 && mouseY > 105 && mouseY < 115)
                guiGraphics.renderTooltip(font, translatable("gui.guide.entity"), absoluteMouseX, absoluteMouseY);
        }

        //render sword
        if (fp.catchInfo().alwaysSpawnEntity())
        {
            guiGraphics.blit(ALWAYS_ENTITY, x + 93, y + 103, 0, 0, 14, 14, 14, 14);
            if (mouseX > 92 && mouseX < 107 && mouseY > 105 && mouseY < 115)
                guiGraphics.renderTooltip(font, translatable("gui.guide.always_entity"), absoluteMouseX, absoluteMouseY);
        }

        //render name
        if (fishToDisplay != ItemStack.EMPTY)
        {
            renderItem(fishToDisplay, x + 26, y + 70);
            renderScrollingString(guiGraphics, font, fp.getDisplayName(), 20, x + 28, y + 36, x + 141, y + 46, true);
        }

        int color = switch (fp.rarity())
        {
            case Rarity.TRASH, Rarity.COMMON, Rarity.NONE -> FastColor.ARGB32.color(0, -1);
            case Rarity.UNCOMMON -> FastColor.ARGB32.color(200, 0x92f28d);
            case Rarity.RARE -> FastColor.ARGB32.color(200, 0x78c8ff);
            case Rarity.EPIC -> FastColor.ARGB32.color(200, 0xc060ff);
            case Rarity.LEGENDARY, GOLDEN ->
                    FastColor.ARGB32.color(175, Color.HSBtoRGB((float) Util.getMillis() / 10000, 1, 1));
        };

        float red = FastColor.ARGB32.red(color) / 255f;
        float green = FastColor.ARGB32.green(color) / 255f;
        float blue = FastColor.ARGB32.blue(color) / 255f;
        float alpha = FastColor.ARGB32.alpha(color) / 255f;

        guiGraphics.setColor(red, green, blue, alpha);

        //render glow
        RenderSystem.enableBlend();
        if (fcc != null) guiGraphics.blit(
                GLOW, x + 10, y + 55,
                0, 0, 48, 48, 48, 48);
        RenderSystem.disableBlend();
        guiGraphics.setColor(1, 1, 1, 1);

        //render new fish icon
        if (fcc != null && fcc.hasGuideNotification())
            guiGraphics.blit(NEW_FISH, x + 120, y + 95, 0, 0, 32, 32, 32, 32);

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

            renderScrollingString(guiGraphics, font, description, x, x, yOffset - 2, x + 128, yOffset + 10, hoveringMain);

            //if has hover and cursor is hovering
            if (!hover.isEmpty() && hoveringMain)
            {
                guiGraphics.renderTooltip(font, hover, Optional.empty(), absoluteMouseX, absoluteMouseY);
            }

            //if blacklist then render [!]
            if (!blacklist.isEmpty())
            {
                guiGraphics.drawString(font, "[!]", x + 129, yOffset, SCColors.GUIDE_RED, false);
                if (hoveringBlacklist)
                    guiGraphics.renderTooltip(font, blacklist, Optional.empty(), absoluteMouseX, absoluteMouseY);
            }

            yOffset += 12;
        }

        //render fish tooltip
        if (mouseX > 6 && mouseX < 61 && mouseY > 51 && mouseY < 105)
        {
            if (fp.catchInfo().alwaysSpawnEntity() && (fcc != null || !SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get()))
                guiGraphics.renderTooltip(font, fp.getDisplayName(), absoluteMouseX, absoluteMouseY);
            else if (fishToDisplay != ItemStack.EMPTY)
                guiGraphics.renderTooltip(font, fishToDisplay, absoluteMouseX, absoluteMouseY);
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
            components.add(translatable("gui.guide.percentile").append(Component.literal(fcc.percentile() + "%").withStyle(ChatFormatting.BOLD)));

            guiGraphics.renderTooltip(font, components, Optional.empty(), absoluteMouseX, absoluteMouseY);
        }

    }

    public static void renderScrollingString(GuiGraphics guiGraphics, Font font, Component text, int centerX, int minX, int minY, int maxX, int maxY, boolean hovering)
    {
        int i = font.width(text);
        int j = (minY + maxY - 9) / 2 + 1;
        int k = maxX - minX;
        if (i > k)
        {
            int l = i - k;
            double d0 = (double) Util.getMillis() / (double) 300.0F;
            double d1 = Math.max((double) l * (double) 0.5F, 3.0F);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / (double) 2.0F + (double) 0.5F;
            double d3 = Mth.lerp(d2, 0.0F, l);
            guiGraphics.enableScissor(minX, minY, maxX, maxY);
            int x = minX - (int) d3;
            if (!hovering) x = minX;
            guiGraphics.drawString(font, text, x, j, SCColors.GUIDE_TEXT_DARK, false);
            guiGraphics.disableScissor();
        }
        else
        {
            int i1 = Mth.clamp(centerX, minX + i / 2, maxX - i / 2);
            guiGraphics.drawString(font, text.getVisualOrderText(), i1 - font.width(text.getVisualOrderText()) / 2, j, SCColors.GUIDE_TEXT_DARK, false);
        }
    }

    private void displayRecipe(ItemStack stack)
    {
        if (ModList.get().isLoaded("emi"))
            StarcatcherEmiPlugin.displayRecipes(stack);
        else if (ModList.get().isLoaded("jei"))
            StarcatcherJeiPlugin.displayRecipes(stack);
    }

    public void renderCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color)
    {
        renderCenteredString(guiGraphics, font, text, x, y, color, false);
    }

    public void renderCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color, boolean shadow)
    {
        FormattedCharSequence formattedcharsequence = text.getVisualOrderText();
        guiGraphics.drawString(font, formattedcharsequence, x - font.width(formattedcharsequence) / 2, y, color, shadow);
    }

    public static MutableComponent translatable(String key)
    {
        return Tooltips.resolveTagsToComponentFromTranslationKey(key);
    }
}
