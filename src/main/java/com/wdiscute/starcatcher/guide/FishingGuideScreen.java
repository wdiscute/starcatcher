package com.wdiscute.starcatcher.guide;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.starcatcher.*;
import com.wdiscute.starcatcher.compat.emi.StarcatcherEmiPlugin;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.io.network.SignGuidePayload;
import com.wdiscute.starcatcher.registry.FishProperties.SizeAndWeight.Units;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.io.network.FPsSeenPayload;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.secretnotes.NoteContainer;
import com.wdiscute.starcatcher.secretnotes.SecretNoteScreen;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
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

    private static final ResourceLocation ARROW_LEFT = Starcatcher.rl("textures/gui/guide/arrow_left.png");
    private static final ResourceLocation ARROW_RIGHT = Starcatcher.rl("textures/gui/guide/arrow_right.png");

    private static final ResourceLocation ARROW_NEXT = Starcatcher.rl("textures/gui/guide/arrow_next.png");
    private static final ResourceLocation ARROW_NEXT_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_next_pressed.png");
    private static final ResourceLocation ARROW_NEXT_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_next_highlight.png");

    private static final ResourceLocation ARROW_INDEX = Starcatcher.rl("textures/gui/guide/arrow_index.png");
    private static final ResourceLocation ARROW_INDEX_PRESSED = Starcatcher.rl("textures/gui/guide/arrow_index_pressed.png");
    private static final ResourceLocation ARROW_INDEX_HIGHLIGHT = Starcatcher.rl("textures/gui/guide/arrow_index_highlight.png");

    private static final ResourceLocation NEW_FISH = Starcatcher.rl("textures/gui/guide/new_fish.png");
    private static final ResourceLocation STAR = Starcatcher.rl("textures/gui/guide/star.png");
    private static final ResourceLocation GLOW = Starcatcher.rl("textures/gui/guide/glow.png");

    private static final ResourceLocation BUCKET = Starcatcher.rl("textures/gui/guide/bucketable.png");
    private static final ResourceLocation ENTITY = Starcatcher.rl("textures/gui/guide/entity.png");
    private static final ResourceLocation ALWAYS_ENTITY = Starcatcher.rl("textures/gui/guide/always_entity.png");

    public static int MAX_HELP_PAGES = 12;


    private final List<ItemStack> tackleBoxes = new ArrayList<>();
    private final List<ItemStack> baits = new ArrayList<>();
    private final List<ItemStack> hooksBobbers = new ArrayList<>();
    private final List<ItemStack> equipments = new ArrayList<>();
    private final List<ItemStack> aquariumInteractions = new ArrayList<>();
    private final List<ItemStack> templates = new ArrayList<>();
    private final List<ItemStack> bottles;

    private final ItemStack rodIcon;
    private final ItemStack sweetspotsIcon;
    private final ItemStack treasureIcon;
    private final ItemStack standIcon;
    private final ItemStack letterBottleIcon;
    private final ItemStack aquariumIcon;
    private final ItemStack displayIcon;
    private final ItemStack guideIcon;
    private final ItemStack sellingBinIcon;
    private final ItemStack trophyIcon;
    private final ItemStack cosmeticsIcon;
    private final ItemStack tackleBoxIcon;
    private final ItemStack hookIcon;
    private final ItemStack baitIcon;
    private final ItemStack upgradeIcon;


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
    int lastIndexPage = 0;

    int menu = 0;
    int page = 0;

    ClientLevel level;
    LocalPlayer player;

    List<ResourceLocation> fpsSeen = new ArrayList<>();
    List<FishProperties> entries = new ArrayList<>(999);
    List<FishProperties> trophies = new ArrayList<>(999);
    List<ItemStack> trophiesIS = new ArrayList<>(999);
    List<ItemStack> secretsIS = new ArrayList<>(999);
    List<FishProperties> secrets = new ArrayList<>(999);
    List<FishProperties> fishInArea = new ArrayList<>();
    Map<ResourceLocation, FishCaughtCounter> fishCaughtCounterMap = new HashMap<>();

    EditBox editBox;

    @Override
    protected void init()
    {
        super.init();

        imageWidth = 420;
        imageHeight = 260;

        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;

        //editbox for cover
        this.editBox = new EditBox(this.font, uiX + 240, uiY + 102, 103, 12, Component.translatable("container.repair"));
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

        for (FishProperties fp : FishProperties.getAllFPs(level))
        {
            if (fp.hasGuideEntry() && fp.calculateChance(player, player.level(), ItemStack.EMPTY, AbstractFishRestriction.Context.GUIDE_FISHES_HOVER) > 0)
                fishInArea.add(fp);
        }

        fishCaughtCounterMap = FishingGuideAttachment.getFishesCaught(player);

        for (FishProperties fp : FishProperties.getAllFPs(level)) if (fp.hasGuideEntry()) entries.add(fp);
        entries = sortEntries(SCConfig.SORT.get(), entries, player);
        fishInArea = sortEntries(SCConfig.SORT.get(), fishInArea, player);

        trophies = FishProperties.getTrophies(level);
        secrets = FishProperties.getSecrets(level);

        trophiesIS = new ArrayList<>();
        trophies.forEach(t ->
        {
            if (!SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get() || fishCaughtCounterMap.containsKey(level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(t)))
                trophiesIS.add(t.catchInfo().fish().value().getDefaultInstance());
            else
                trophiesIS.add(SCItems.MISSINGNO.asItem().getDefaultInstance());
        });

        secretsIS = new ArrayList<>();
        secrets.forEach(t ->
        {
            if (!SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get() || fishCaughtCounterMap.containsKey(level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY).getKey(t)))
                secretsIS.add(t.catchInfo().fish().value().getDefaultInstance());
            else
                secretsIS.add(SCItems.MISSINGNO.asItem().getDefaultInstance());
        });

        if (trophies.isEmpty()) trophies = List.of(FishProperties.DEFAULT);
        if (trophiesIS.isEmpty()) trophiesIS = List.of(Items.BARRIER.getDefaultInstance());

        if (secrets.isEmpty()) secrets = List.of(FishProperties.DEFAULT);
        if (secretsIS.isEmpty()) secretsIS = List.of(Items.BARRIER.getDefaultInstance());
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
                        minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                        page--;
                        return true;
                    }
                    //go to book cover
                    else
                    {
                        menu = -1;
                    }
                }
                case 1 ->
                {
                    minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    //help -> index
                    if (page == 0)
                    {
                        menu = 0;
                        page = lastIndexPage;
                        return true;
                    }
                    //help -> previous help page
                    page--;
                    return true;
                }
                case 2 ->
                {
                    minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    //entries -> last page of help
                    if (page == 0)
                    {
                        menu = 1;
                        page = MAX_HELP_PAGES;
                        if (MAX_HELP_PAGES == 0) menu = 0;
                        return true;
                    }
                    //entries -> previous entry
                    page--;
                    return true;
                }

                case 3 ->
                {
                    minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
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
                    minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    menu = 0;
                    page = 0;
                    return true;
                }
                case 0 ->
                {
                    //index -> next page of index
                    minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    if (hasNextPage)
                    {
                        page++;
                        return true;
                    }
                    //index -> first page of help
                    menu = 1;
                    page = 0;
                    if (MAX_HELP_PAGES == 0) menu = 2;
                    return true;
                }
                case 1 ->
                {
                    //help -> next page of help
                    minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    if (page != MAX_HELP_PAGES)
                    {
                        minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
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
                        minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
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
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 0;
            page = 0;
            return true;
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
        if (x > 53 && x < 189 && y > 145 && y < 200 && menu == 1 &&
                (page == 4 || page == 5 || page == 6 || page == 10 || page == 12))
        {
            if (scrollY < 0)
                leftPageScroll++;
            else
                leftPageScroll--;
        }
        else
        {
            //if hovering scrollable on right
            if (x > 212 && x < 356 && y > 145 && y < 200 && menu == 1 &&
                    (page == 5 || page == 12))
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
        //System.out.println("clicked on x :" + y);

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

        yRotExtra = (int) Mth.lerp(0.1f + Math.abs((float) yRotExtra / 60), yRotExtra, 0);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        editBox.setEditable(false);

        switch (menu)
        {
            case -1 ->
            {
                RenderSystem.enableBlend();
                renderImage(guiGraphics, BACKGROUND_COVER);
                RenderSystem.disableBlend();
                renderCoverText(guiGraphics, mouseX, mouseY);
                renderCompass(guiGraphics);
            }

            //render settings screen
            case -99 ->
            {
                Minecraft.getInstance().setScreen(
                        new SettingsScreen(
                                FishProperties.builder().withFish(SCItems.AURORA).build(),
                                new ItemStack(SCItems.ROD.get()
                                )
                        ));
                return;
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


    int yRotExtra = U.r.nextInt(40) - 20;

    private void renderCompass(GuiGraphics guiGraphics)
    {
        float yRot = Minecraft.getInstance().player.getYRot() + yRotExtra;

        PoseStack pose = guiGraphics.pose();

        pose.pushPose();
        pose.translate(uiX + 16 + 16.5, uiY + 16 + 34.5, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(-yRot - 45 - 180));
        pose.translate(-16, -16, 0);
        guiGraphics.blit(COMPASS, 0, 0, 0, 0, 32, 32, 32, 32);
        pose.popPose();
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
            Component comp = Component.translatable("gui.guide.page." + page + ".left." + i).copy().withStyle(Style.EMPTY.withColor(0x635040));
            guiGraphics.drawString(this.font, comp, uiX + 52, uiY + 10 * i + 13, 0xff000000, false);
        }

        for (int i = 0; i < 40; i++)
        {
            if (!I18n.exists("gui.guide.page." + page + ".right." + i)) break;
            Component comp = Component.translatable("gui.guide.page." + page + ".right." + i).copy().withStyle(Style.EMPTY.withColor(0x635040));
            guiGraphics.drawString(this.font, comp, uiX + 213, uiY + 10 * i + 13, 0xff000000, false);
        }

        if (I18n.exists("gui.guide.page." + page + ".left.title"))
            renderCenteredString(guiGraphics, this.font, Component.translatable("gui.guide.page." + page + ".left.title"), uiX + 116, uiY + 45, 0x635040);

        if (I18n.exists("gui.guide.page." + page + ".right.title"))
            renderCenteredString(guiGraphics, this.font, Component.translatable("gui.guide.page." + page + ".right.title"), uiX + 270, uiY + 45, 0x635040);

    }

    private void renderTheBasics(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {

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
                renderImage(guiGraphics, HELP_PAGE_SWEETSPOTS);
                renderItemWithHoverAndEmi(guiGraphics, sweetspotsIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //treasures
            case 2 ->
            {
                renderImage(guiGraphics, HELP_PAGE_TREASURE);
                renderItemWithHoverAndEmi(guiGraphics, treasureIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //upgrades
            case 3 ->
            {
                renderImage(guiGraphics, HELP_PAGE_UPGRADES);
                renderItemWithHoverAndEmi(guiGraphics, upgradeIcon, uiX + 166, uiY + 39, mouseX, mouseY);
            }

            //tackle boxes
            case 4 ->
            {
                renderImage(guiGraphics, HELP_PAGE_TACKLE_BOX);

                //tackle boxes icon
                renderItemWithHoverAndEmi(guiGraphics, tackleBoxIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //left page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 54, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 171, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 54 && mouseX < uiX + 64 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    leftPageScroll--;
                if (clicked && mouseX > uiX + 175 && mouseX < uiX + 185 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    leftPageScroll++;

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
                    leftPageScroll--;
                if (clicked && mouseX > uiX + 175 && mouseX < uiX + 185 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    leftPageScroll++;

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
                    rightPageScroll--;
                if (clicked && mouseX > uiX + 336 && mouseX < uiX + 346 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    rightPageScroll++;


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
                    leftPageScroll--;
                if (clicked && mouseX > uiX + 175 && mouseX < uiX + 185 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    leftPageScroll++;

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
                renderItemWithHoverAndEmi(guiGraphics, letterBottleIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                for (int i = 0; i < 4; i++)
                {
                    renderItemWithHoverAndEmi(guiGraphics, bottles.get(i), uiX + 240 + (i * 24), uiY + 140, mouseX, mouseY);
                    guiGraphics.renderOutline(uiX + 238 + (i * 24), uiY + 138, 20, 20, 0xff9c897c);
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
                    leftPageScroll--;
                if (clicked && mouseX > uiX + 175 && mouseX < uiX + 185 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    leftPageScroll++;

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


            //trophies and secrets
            case 12 ->
            {
                renderImage(guiGraphics, HELP_PAGE_TROPHIES);
                renderItemWithHoverAndEmi(guiGraphics, trophyIcon, uiX + 166, uiY + 39, mouseX, mouseY);

                //left page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 54, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 171, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 54 && mouseX < uiX + 64 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    leftPageScroll--;
                if (clicked && mouseX > uiX + 175 && mouseX < uiX + 185 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    leftPageScroll++;

                //trophies items
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 72 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = trophiesIS.get(Math.abs((leftPageScroll + i) % trophiesIS.size()));
                    FishProperties fp = trophies.get(Math.abs((leftPageScroll + i) % trophies.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        FishCaughtCounter fishCaughtCounter = fishCaughtCounterMap.get(U.getRlFromFp(level, fp));
                        ArrayList<Component> components = new ArrayList<>(getCachedTooltipForHoverEntry(fp, fishCaughtCounter == null ? 0 : fishCaughtCounter.count()));
                        if (fp != FishProperties.DEFAULT)
                            guiGraphics.renderTooltip(this.font, components, Optional.empty(), mouseX, mouseY);

                        //if clicked on a trophy, display FP
                        if (clicked && fp != FishProperties.DEFAULT)
                            Minecraft.getInstance().setScreen(new IsolatedFPScreen(fp, this));
                    }

                    //scrollable background fill
                    guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, 0xffb4a697);
                }


                //secret icon
                renderItemWithHoverAndEmi(guiGraphics, letterBottleIcon, uiX + 321, uiY + 39, mouseX, mouseY);

                //right page scroll arrows
                guiGraphics.blit(ARROW_LEFT, uiX + 224, uiY + 170, 0, 0, 16, 16, 16, 16);
                guiGraphics.blit(ARROW_RIGHT, uiX + 332, uiY + 170, 0, 0, 16, 16, 16, 16);
                if (clicked && mouseX > uiX + 225 && mouseX < uiX + 235 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    rightPageScroll--;
                if (clicked && mouseX > uiX + 336 && mouseX < uiX + 346 && mouseY > uiY + 170 && mouseY < uiY + 170 + 16)
                    rightPageScroll++;


                //scrollable secrets icons
                for (int i = 0; i < 5; i++)
                {
                    int x = uiX + 238 + (i * 20);
                    int y = uiY + 170;
                    ItemStack stack = secretsIS.get(Math.abs((rightPageScroll + i) % secretsIS.size()));
                    FishProperties fp = secrets.get(Math.abs((rightPageScroll + i) % secrets.size()));
                    //render item
                    renderItem(stack, x, y, 1);
                    //render hover item tooltip
                    if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
                    {
                        FishCaughtCounter fcc = fishCaughtCounterMap.get(U.getRlFromFp(level, fp));

                        if (fcc != null)
                            guiGraphics.renderTooltip(font, stack, mouseX, mouseY);

                        //if clicked on a trophy, display FP
                        if (clicked && fp != FishProperties.DEFAULT && fcc != null && stack.getItem() instanceof NoteContainer nc)
                            Minecraft.getInstance().setScreen(new SecretNoteScreen(nc.note, this));
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
                    guiGraphics.renderTooltip(this.font, Component.translatable(indexEntries.get(i).getSecond()), mouseX, mouseY);

                if (clicked && mouseX > xx + (i * 20) - 2 && mouseX < xx + (i * 20) + 17 && mouseY > uiY + 47 - 2 && mouseY < uiY + 47 + 17)
                {
                    clicked = false;
                    minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
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
                    guiGraphics.renderTooltip(this.font, Component.translatable(indexEntries.get(i).getSecond()), mouseX, mouseY);
                    if (clicked)
                    {
                        clicked = false;
                        minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
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
            renderCenteredString(guiGraphics, this.font, Component.translatable("gui.guide.sort"), uiX + 171, uiY + 88, 0x937d70);
            if (mouseX > uiX + 145 && mouseX < uiX + 190 && mouseY > uiY + 86 && mouseY < uiY + 96)
            {
                guiGraphics.renderTooltip(this.font, Component.translatable(SCConfig.SORT.get().getTranslationKey()), mouseX, mouseY);
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

                    renderFishIndex(guiGraphics, xpos, ypos, mouseX, mouseY, fp, 0xffc6bdaf);
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
                        renderImage(guiGraphics, FISHES_IN_AREA_BOTTOM_LEFT_DECORATION, 0, (Math.min(numberOfRows, 6) - 1) * 20 + 20);

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
                renderFishIndex(guiGraphics, xx + 160 + (i % 7 * 20), uiY + 56 + (i / 7 * 20), mouseX, mouseY, entries.get(i), 0xffc6bdaf);
            }
        }
        else
        {
            //render second page, left
            for (int i = 0; i < 49; i++)
            {
                int order = i + 49 * (page * 2 - 1);
                if (order > entries.size() - 1) break;
                renderFishIndex(guiGraphics, xx + (i % 7 * 20), uiY + 56 + (i / 7 * 20), mouseX, mouseY, entries.get(order), 0xffc6bdaf);
            }

            //render second page, right
            for (int i = 0; i < 49; i++)
            {
                int order = i + 49 + 49 * (page * 2 - 1);
                if (order > entries.size() - 1) return;
                renderFishIndex(guiGraphics, xx + 160 + (i % 7 * 20), uiY + 56 + (i / 7 * 20), mouseX, mouseY, entries.get(order), 0xffc6bdaf);
            }
        }
        lastIndexPage = Math.max(page + 1, lastIndexPage);
        hasNextPage = true;
    }

    private void renderFishIndex(GuiGraphics guiGraphics, int xOffset, int yOffset, int mouseX, int mouseY, FishProperties fp, int backgroundFillColor)
    {
        ResourceLocation rl = U.getRlFromFp(level, fp);
        FishCaughtCounter fishCaughtCounter = fishCaughtCounterMap.get(rl);
        ItemStack is = new ItemStack(fp.catchInfo().fish());

        //calculate caught counter
        int caught = fishCaughtCounter == null ? 0 : fishCaughtCounter.count();

        //handle click
        if (clicked && mouseX > xOffset - 3 && mouseX < xOffset + 21 - 3 && mouseY > yOffset - 3 && mouseY < yOffset + 21 - 3)
        {
            minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
            menu = 2;
            page = entries.indexOf(fp) / 2;

            if (entries.indexOf(fp) % 2 == 0)
                highlightLeftAlpha = 0.5f;
            else
                highlightRightAlpha = 0.5f;
        }

        //render fill
        guiGraphics.fill(xOffset - 1, yOffset - 1, xOffset + 17, yOffset + 17, backgroundFillColor);

        //glow color
        int color = switch (fp.rarity())
        {
            case TRASH, NONE -> FastColor.ARGB32.color(0, -1);
            case FishProperties.Rarity.COMMON -> FastColor.ARGB32.color(0, -1);
            case FishProperties.Rarity.UNCOMMON -> FastColor.ARGB32.color(255, 0x92f28d);
            case FishProperties.Rarity.RARE -> FastColor.ARGB32.color(255, 0x78c8ff);
            case FishProperties.Rarity.EPIC -> FastColor.ARGB32.color(255, 0xc060ff);
            case FishProperties.Rarity.LEGENDARY, FishProperties.Rarity.GOLDEN ->
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
        if (fishCaughtCounter != null && fishCaughtCounter.hasGuideNotification() && !fpsSeen.contains(FishProperties.getKey(level, fp)))
            guiGraphics.renderOutline(xOffset - 1, yOffset - 1, 18, 18, 0xffc58c44);
        //guiGraphics.blit(STAR, xOffset + 10, yOffset + 7, 0, 0, 10, 10, 10, 10);


        //render tooltip
        if (mouseX > xOffset - 3 && mouseX < xOffset + 21 - 3 && mouseY > yOffset - 3 && mouseY < yOffset + 21 - 3)
        {
            ArrayList<Component> components = new ArrayList<>(getCachedTooltipForHoverEntry(fp, caught));
            components.add(1, Component.translatable("gui.guide.rarity." + fp.rarity().getSerializedName()));

            guiGraphics.renderTooltip(this.font, components, Optional.empty(), mouseX, mouseY);

            if (fishCaughtCounter != null && fishCaughtCounter.hasGuideNotification() && SCConfig.REMOVE_NOTIFICATION_ON_HOVER.get() && !fpsSeen.contains(rl))
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

        boolean isFish = fp.catchInfo().fishEntryType().equals(FishProperties.CatchInfo.FishEntryType.FISH);
        if (caught == 0 && SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get())
        {
            components.add(Component.translatable("gui.guide.not_caught_fish_name"));
            if (isFish)
                components.add(Component.translatable("gui.guide.not_caught_yet").withStyle(Style.EMPTY.withColor(SCColors.GUIDE_RED)));
        }
        else
        {
            if (fp.catchInfo().alwaysSpawnEntity() && !fp.catchInfo().entityToSpawn().is(U.holderEntity(SCEntities.FISH)))
                components.add(Component.translatable("entity." + fp.catchInfo().entityToSpawn().getRegisteredName().replace(":", ".")));
            else
                components.add(Component.translatable(fp.catchInfo().fish().value().getDescriptionId()));

            if (isFish)
                components.add(Component.translatable("gui.guide.caught").append(Component.literal(" [" + caught + "]")).withStyle(Style.EMPTY.withColor(SCColors.GUIDE_GREEN)));
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
        FishCaughtCounter fcc = fishCaughtCounterMap.get(U.getRlFromFp(level, fp));

        ItemStack is = fcc == null && SCConfig.HIDE_ENTRIES_UNTIL_FOUND.get() ? ItemStack.EMPTY : new ItemStack(entries.get(entry).catchInfo().fish());
        if(fcc != null && fcc.caughtGolden())
            SCDataComponents.set(is, SCDataComponents.CAUGHT_FISH_INFO, new CaughtFishInfo(fcc.size(), fcc.weight(), fcc.percentile(), fp.rarity(), true));

        renderFishEntryPage(guiGraphics, fp, is, fcc, uiX + xOffset, uiY, mouseX, mouseY);

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

    private static void renderItem(ItemStack stack, int x, int y, float scale)
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
        PacketDistributor.sendToServer(new FPsSeenPayload(fpsSeen));
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
            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(o -> o.catchInfo().fish().unwrapKey().get().location().getPath())).toList();

            List<FishProperties> entriesSorted = new ArrayList<>();

            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(FishProperties.Rarity.COMMON)) entriesSorted.add(e);
            });
            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(FishProperties.Rarity.UNCOMMON)) entriesSorted.add(e);
            });
            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(FishProperties.Rarity.RARE)) entriesSorted.add(e);
            });
            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(FishProperties.Rarity.EPIC)) entriesSorted.add(e);
            });
            entriesToSort.forEach(e ->
            {
                if (e.rarity().equals(FishProperties.Rarity.LEGENDARY)) entriesSorted.add(e);
            });

            return sort.equals(Sort.RARITY_UP) ? entriesSorted : entriesSorted.reversed();
        }

        //alphabetical
        if (sort.equals(Sort.ALPHABETICAL_DOWN) || sort.equals(Sort.ALPHABETICAL_UP))
        {
            List<FishProperties> entriesSorted = entriesToSort.stream().sorted(Comparator.comparing(o -> o.catchInfo().fish().unwrapKey().get().location().getPath())).toList();
            return sort.equals(Sort.ALPHABETICAL_UP) ? entriesSorted : entriesSorted.reversed();
        }

        //mod
        if (sort.equals(Sort.MOD_DOWN) || sort.equals(Sort.MOD_UP))
        {
            //sort alphabetical first
            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(o -> o.catchInfo().fish().unwrapKey().get().location().getPath())).toList();

            List<FishProperties> entriesSorted = new ArrayList<>();
            List<String> allNamespaces = new ArrayList<>();

            for (FishProperties fp : entriesToSort)
            {
                String namespace = fp.catchInfo().fish().unwrapKey().get().location().getNamespace();
                if (!allNamespaces.contains(namespace)) allNamespaces.add(namespace);
            }

            for (String s : allNamespaces)
            {
                for (FishProperties fp : entriesToSort)
                {
                    String namespace = fp.catchInfo().fish().unwrapKey().get().location().getNamespace();
                    if (namespace.equals(s)) entriesSorted.add(fp);
                }
            }

            entriesToSort = sort.equals(Sort.MOD_UP) ? entriesSorted : entriesSorted.reversed();
        }

        //fluid
//        if (sort.equals(Sort.FLUID_DOWN) || sort.equals(Sort.FLUID_UP))
//        {
//            //sort alphabetical first
//            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(o -> o.catchInfo().fish().unwrapKey().get().location().getPath())).toList();
//
//            List<FishProperties> entriesSorted = new ArrayList<>();
//            List<FishProperties> entriesRemaining = new ArrayList<>(entriesToSort);
//
//            while (!entriesRemaining.isEmpty())
//            {
//                ResourceLocation rlBeingSorted = entriesRemaining.getFirst().wr().fluids().getFirst();
//                List<FishProperties> temp = new ArrayList<>(entriesRemaining);
//                temp.forEach(e ->
//                {
//                    if (e.wr().fluids().getFirst().equals(rlBeingSorted))
//                    {
//                        entriesSorted.add(e);
//                        entriesRemaining.remove(e);
//                    }
//                });
//            }
//
//            entriesToSort = sort.equals(Sort.FLUID_UP) ? entriesSorted : entriesSorted.reversed();
//        }

        //caught
        if (sort.equals(Sort.CAUGHT_UP) || sort.equals(Sort.CAUGHT_DOWN))
        {
            //sort alphabetical first
            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(o -> o.catchInfo().fish().unwrapKey().get().location().getPath())).toList();


            //add all fishes caught to start
            Map<ResourceLocation, FishCaughtCounter> fishesCaught = fishCaughtCounterMap;

            List<FishProperties> hasCaught = new ArrayList<>();
            List<FishProperties> hasNotCaught = new ArrayList<>();
            List<FishProperties> toReturn = new ArrayList<>();

            //populate hasCaught and hasNotCaught
            entriesToSort.forEach(e ->
            {
                if (e.hasGuideEntry() && fishesCaught.containsKey(U.getRlFromFp(player.level(), e)))
                    hasCaught.add(e);
                else
                    hasNotCaught.add(e);
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

//        //SEASONS
//        if (sort.equals(Sort.SEASON_DOWN) || sort.equals(Sort.SEASON_UP))
//        {
//            //sort alphabetical first
//            entriesToSort = entriesToSort.stream().sorted(Comparator.comparing(o -> o.catchInfo().fish().unwrapKey().get().location().getPath())).toList();
//
//            List<FishProperties> entriesSorted = new ArrayList<>();
//            List<FishProperties> entriesUnsorted = new ArrayList<>(entriesToSort);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.ALL)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.SPRING)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.EARLY_SPRING)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.MID_SPRING)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.LATE_SPRING)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.SUMMER)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.EARLY_SUMMER)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.MID_SUMMER)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.LATE_SUMMER)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.AUTUMN)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.EARLY_AUTUMN)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.MID_AUTUMN)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.LATE_AUTUMN)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.WINTER)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.EARLY_WINTER)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.MID_WINTER)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            for (FishProperties fp : entriesUnsorted)
//                if (fp.wr().seasons().contains(Seasons.LATE_WINTER)) entriesSorted.add(fp);
//            entriesUnsorted.removeAll(entriesSorted);
//
//            return sort.equals(Sort.SEASON_UP) ? entriesSorted : entriesSorted.reversed();
//        }

        return entriesToSort;
    }

    public FishingGuideScreen()
    {
        super(Component.empty());

        rodIcon = new ItemStack(SCItems.ROD.get());
        sweetspotsIcon = new ItemStack(SCItems.AURORA.get());
        treasureIcon = new ItemStack(SCItems.WATERLOGGED_SATCHEL.get());
        cosmeticsIcon = new ItemStack(SCBlocks.FISHERMAN_HAT_BLUE.get());
        tackleBoxIcon = new ItemStack(SCBlocks.TACKLE_BOX.get());
        baitIcon = new ItemStack(SCItems.CHERRY_BAIT.get());
        var auroraIcon = new ItemStack(SCItems.AURORA.get());
        upgradeIcon = new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        hookIcon = new ItemStack(SCItems.HOOK.get());
        standIcon = new ItemStack(SCBlocks.STAND.get());
        var fishermanHatIcon = new ItemStack(SCBlocks.FISHERMAN_HAT_BLUE.get());
        var settingsIcon = new ItemStack(SCItems.SETTINGS.get());
        var letterIcon = new ItemStack(SCItems.LETTER.get());
        letterBottleIcon = new ItemStack(SCItems.BOTTLED_LETTER.get());
        var messageBottleIcon = new ItemStack(SCItems.MESSAGE_IN_A_BOTTLE.get());
        var messageIcon = new ItemStack(SCItems.MESSAGE.get());
        sellingBinIcon = new ItemStack(SBBlocks.SELLING_BIN.get());
        aquariumIcon = new ItemStack(SCBlocks.AQUARIUM.get());
        displayIcon = new ItemStack(SCBlocks.DISPLAY.get());
        guideIcon = new ItemStack(SCItems.GUIDE.get());
        trophyIcon = new ItemStack(SCBlocks.TROPHY_EMERALD.get());


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
                Pair.of(auroraIcon, "gui.guide.index.sweetspots"),
                Pair.of(treasureIcon, "gui.guide.index.treasures"),
                Pair.of(upgradeIcon, "gui.guide.index.upgrades"),
                Pair.of(tackleBoxIcon, "gui.guide.index.tackle_box"),
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
                font, Component.translatable("gui.guide.caught"),
                x + 73, y + 64, 0x9c897c, false);

        //render caught count
        if (fcc == null)
        {
            //------
            guiGraphics.drawString(
                    font, Component.translatable("gui.guide.not_caught"),
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
                font, Component.translatable("gui.guide.rarity"),
                x + 73, y + 84, 0x9c897c, false);
        //common
        guiGraphics.drawString(
                font, Component.translatable("gui.guide.rarity." + fp.rarity().getSerializedName()),
                x + 73, y + 93, 0, false);

        //render bucketable
        if (!fp.catchInfo().bucketedFish().is(SCItems.MISSINGNO))
        {
            guiGraphics.blit(BUCKET, x + 77, y + 103, 0, 0, 14, 14, 14, 14);
            if (mouseX > 75 && mouseX < 93 && mouseY > 105 && mouseY < 115)
                guiGraphics.renderTooltip(font, Component.translatable("gui.guide.bucketable"), absoluteMouseX, absoluteMouseY);
        }

        //render almighty wormable
        if ((!fp.catchInfo().entityToSpawn().equals(U.holderEntity("starcatcher", "fish")) && !fp.catchInfo().alwaysSpawnEntity())
                || (fp.catchInfo().entityToSpawn().equals(U.holderEntity("starcatcher", "fish")) && fp.catchInfo().fish().is(SCTags.BUCKETABLE_FISHES)))
        {
            guiGraphics.blit(ENTITY, x + 93, y + 103, 0, 0, 14, 14, 14, 14);
            if (mouseX > 92 && mouseX < 107 && mouseY > 105 && mouseY < 115)
                guiGraphics.renderTooltip(font, Component.translatable("gui.guide.entity"), absoluteMouseX, absoluteMouseY);
        }

        //render sword
        if (fp.catchInfo().alwaysSpawnEntity())
        {
            guiGraphics.blit(ALWAYS_ENTITY, x + 93, y + 103, 0, 0, 14, 14, 14, 14);
            if (mouseX > 92 && mouseX < 107 && mouseY > 105 && mouseY < 115)
                guiGraphics.renderTooltip(font, Component.translatable("gui.guide.always_entity"), absoluteMouseX, absoluteMouseY);
        }

        if (fishToDisplay != ItemStack.EMPTY)
        {
            renderItem(fishToDisplay, x + 26, y + 70);
            guiGraphics.drawString(font, fp.getDisplayName(), x + 30, y + 36, 0x635040, false);
        }

        int color = switch (fp.rarity())
        {
            case FishProperties.Rarity.TRASH, FishProperties.Rarity.COMMON, FishProperties.Rarity.NONE ->
                    FastColor.ARGB32.color(0, -1);
            case FishProperties.Rarity.UNCOMMON -> FastColor.ARGB32.color(200, 0x92f28d);
            case FishProperties.Rarity.RARE -> FastColor.ARGB32.color(200, 0x78c8ff);
            case FishProperties.Rarity.EPIC -> FastColor.ARGB32.color(200, 0xc060ff);
            case FishProperties.Rarity.LEGENDARY, GOLDEN ->
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

        //render stats tooltip (at the end because of the scisor bug)
        if (mouseX > 70 && mouseX < 132 && mouseY > 62 && mouseY < 100 && fcc != null)
        {
            List<Component> components = new ArrayList<>();
            float averageTicks = (int) ((fcc.averageTicks() / 20) * 100) / 100.0f;

            Units unit = SCConfig.UNIT.get();
            String size = unit.getSizeAsString(fcc.size());
            String weight = unit.getWeightAsString(fcc.weight());

            //format first catch
            Instant instant = Instant.ofEpochSecond(fcc.firstCatch());
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("hh:ma");
            String formatted = zdt.format(formatter);
            String formatted2 = zdt.format(formatter2);

            components.add(Component.translatable("gui.guide.first"));
            components.add(Component.literal(formatted).withStyle(ChatFormatting.BOLD));
            components.add(Component.literal(formatted2).withStyle(ChatFormatting.BOLD));
            components.add(Component.literal(""));
            components.add(Component.translatable("gui.guide.fastest").append(Component.literal((((float) fcc.fastestTicks()) / 20) + "s").withStyle(ChatFormatting.BOLD)));
            components.add(Component.translatable("gui.guide.average").append(Component.literal(averageTicks + "s").withStyle(ChatFormatting.BOLD)));
            components.add(Component.literal(""));
            components.add(Component.translatable("gui.guide.biggest").append(Component.literal(size).withStyle(ChatFormatting.BOLD)));
            components.add(Component.translatable("gui.guide.heaviest").append(Component.literal(weight).withStyle(ChatFormatting.BOLD)));
            components.add(Component.translatable("gui.guide.percentile").append(Component.literal(fcc.percentile() + "%").withStyle(ChatFormatting.BOLD)));

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

        //if(ModList.get().isLoaded("jei"))
        //StarcatcherJeiPlugin.displayRecipes(stack);
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
}
