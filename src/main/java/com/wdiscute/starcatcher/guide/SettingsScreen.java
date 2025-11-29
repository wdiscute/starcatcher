package com.wdiscute.starcatcher.guide;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.Config;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.minigame.HitFakeParticle;
import com.wdiscute.starcatcher.io.FishProperties;
import com.wdiscute.starcatcher.io.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Random;
import org.joml.Vector2d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SettingsScreen extends Screen
{
    private static final Random r = new Random();
    private static final ResourceLocation TEXTURE = Starcatcher.rl("textures/gui/minigame/minigame.png");
    private static final ResourceLocation TANK = Starcatcher.rl("textures/gui/minigame/surface.png");
    private static final ResourceLocation SETTINGS = Starcatcher.rl("textures/gui/minigame/settings.png");
    private static final ResourceLocation GUI_SCALE = Starcatcher.rl("textures/gui/minigame/gui_scale.png");

    private static final int SIZE_1 = 5;
    private static final int SIZE_2 = 7;
    private static final int SIZE_3 = 12;
    private static final int SIZE_4 = 17;
    private static final Logger log = LoggerFactory.getLogger(SettingsScreen.class);

    final FishProperties fp;
    final ItemStack itemBeingFished;
    final ItemStack bobber;
    final ItemStack bait;
    final ItemStack hook;

    float hitDelay;

    float speed;
    int reward;
    int rewardThin;
    int treasureReward;
    int penalty;
    int decay;
    boolean hasTreasure;
    boolean changeRotation;

    boolean moveMarkers = false;

    int gracePeriod = 80;

    final InteractionHand hand;

    int pointerPos = 0;
    float lastHitMarkerPos = 0;
    float lastLastHitMarkerPos = 0;
    float lastLastLastHitMarkerPos = 0;

    int pos1;
    int pos2;
    int posThin1;
    int posThin2;
    int posTreasure;

    int currentRotation = 1;

    float partial;

    int completion = 20;
    int completionSmooth = 20;

    boolean perfectCatch = true;
    int consecutiveHits = 0;

    boolean treasureActive;
    int treasureProgress = Integer.MIN_VALUE;
    int treasureProgressSmooth = Integer.MIN_VALUE;

    int difficultyBobberOffset = 0;
    int bigForgiving = SIZE_3;
    int thinForgiving = SIZE_1;
    int treasureForgiving = SIZE_2;

    int previousGuiScale;
    Units unitSelected;

    int tickCount = 0;
    List<HitFakeParticle> hitParticles = new ArrayList<>();

    boolean isHoldingSpace = false;

    public SettingsScreen(FishProperties fp, ItemStack rod)
    {
        super(Component.empty());

        previousGuiScale = Minecraft.getInstance().options.guiScale().get();
        Minecraft.getInstance().options.guiScale().set(Config.MINIGAME_GUI_SCALE.get());

        hitDelay = Config.HIT_DELAY.get().floatValue();

        this.fp = fp;
        this.itemBeingFished = new ItemStack(fp.fish());
        this.bobber = rod.get(ModDataComponents.BOBBER).stack().copy();
        this.bait = rod.get(ModDataComponents.BAIT).stack().copy();
        this.hook = rod.get(ModDataComponents.HOOK).stack().copy();

        posTreasure = Integer.MIN_VALUE;

        unitSelected = Config.UNIT.get();

        //assign difficulty, if using mossy_hook it should make common, uncommon and rare into a harder difficulty
        FishProperties.Difficulty difficulty = hook.is(ModItems.MOSSY_HOOK) &&
                (
                        fp.rarity() == FishProperties.Rarity.COMMON ||
                                fp.rarity() == FishProperties.Rarity.UNCOMMON ||
                                fp.rarity() == FishProperties.Rarity.RARE
                ) ? FishProperties.Difficulty.HARD : fp.dif();

        this.speed = difficulty.speed();
        this.reward = difficulty.reward();
        this.rewardThin = difficulty.rewardThin();
        this.treasureReward = difficulty.treasure().hitReward();
        this.penalty = difficulty.penalty();
        this.decay = difficulty.decay();
        this.hasTreasure = difficulty.treasure().hasTreasure();
        this.changeRotation = difficulty.extras().isFlip();

        pos1 = difficulty.markers().first() ? getRandomFreePosition() : Integer.MIN_VALUE;
        pos2 = difficulty.markers().second() ? getRandomFreePosition() : Integer.MIN_VALUE;
        posThin1 = difficulty.markers().firstThin() ? getRandomFreePosition() : Integer.MIN_VALUE;
        posThin2 = difficulty.markers().secondThin() ? getRandomFreePosition() : Integer.MIN_VALUE;

        //make sweet spots fatter if difficulty bobber is being used
        if (bobber.is(ModItems.STEADY_BOBBER))
        {
            bigForgiving = SIZE_4;
            thinForgiving = SIZE_2;
            treasureForgiving = SIZE_2;
            difficultyBobberOffset = 16;
        }

        hand = Minecraft.getInstance().player.getMainHandItem().is(ModItems.ROD) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    private int getRandomFreePosition()
    {
        for (int i = 0; i < 100; i++)
        {
            int posBeingChecked = r.nextInt(360);

            if ((Math.abs(pos1 - posBeingChecked) < 50 || Math.abs(pos1 - posBeingChecked) > 310) && pos1 != Integer.MIN_VALUE)
                continue;
            if ((Math.abs(pos2 - posBeingChecked) < 50 || Math.abs(pos2 - posBeingChecked) > 310) && pos2 != Integer.MIN_VALUE)
                continue;
            if ((Math.abs(posThin1 - posBeingChecked) < 50 || Math.abs(posThin1 - posBeingChecked) > 310) && posThin1 != Integer.MIN_VALUE)
                continue;
            if ((Math.abs(posThin2 - posBeingChecked) < 50 || Math.abs(posThin2 - posBeingChecked) > 310) && posThin2 != Integer.MIN_VALUE)
                continue;
            if ((Math.abs(posTreasure - posBeingChecked) < 50 || Math.abs(posTreasure - posBeingChecked) > 310) && posTreasure != Integer.MIN_VALUE)
                continue;

            return posBeingChecked;
        }

        return 0;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        partial = partialTick;
        PoseStack poseStack = guiGraphics.pose();

        int imageWidth = 512;
        int imageHeight = 256;

        int uiX = (width - imageWidth) / 2;
        int uiY = (height - imageHeight) / 2;

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //settings
        guiGraphics.blit(
                SETTINGS, width / 2 - 100, height / 2 - 128,
                256, 256, 0, 0, 256, 256, 256, 256);

        //GUI SCALE
        guiGraphics.blit(
                GUI_SCALE, width / 2 - 50, 0,
                100, 50, 0, 0, 100, 50, 100, 50);


        //move markers
        guiGraphics.drawString(this.font, "Move Markers", width / 2 + 72, height / 2 - 24, 0x000000, false);
        if (moveMarkers) guiGraphics.fill(
                width / 2 + 62,
                height / 2 - 22,

                width / 2 + 67,
                height / 2 - 17,

                0xffff00ff
        );

        //change rotation
        guiGraphics.drawString(this.font, "Flip Rotation", width / 2 + 72, height / 2 - 3, 0x000000, false);
        if (changeRotation) guiGraphics.fill(
                width / 2 + 62,
                height / 2 - 2,

                width / 2 + 67,
                height / 2 + 3,

                0xffff00ff
        );

        //hover
        if (x > 350 && x < 373 && y > 39 && y < 66)
        {
            List<Component> comp = new ArrayList<>();

            comp.add(Component.literal("This screen serves to help those"));
            comp.add(Component.literal("who might be playing on high input setups"));
            comp.add(Component.literal(""));
            comp.add(Component.literal("Use the Hit Delay to adjust the number of ticks"));
            comp.add(Component.literal("the minigame will calculate back in time for your input"));
            comp.add(Component.literal(""));
            comp.add(Component.literal("For most people, a delay of 0 works fine."));
            comp.add(Component.literal("Play around and see what feels natural to you"));

            guiGraphics.renderTooltip(this.font, comp, Optional.empty(), mouseX, mouseY);
        }

        //speed
        guiGraphics.drawString(this.font, "Speed: " + speed, width / 2 + 78, height / 2 + 63, 0x000000, false);

        //hit delay
        guiGraphics.drawString(this.font, "Hit Delay: " + hitDelay, width / 2 + 73, height / 2 - 53, 0x000000, false);

        //markers
        guiGraphics.drawString(this.font, "Markers:", width / 2 + 63, height / 2 + 13, 0x000000, false);

        if (pos1 != Integer.MIN_VALUE)
            guiGraphics.fill(
                    width / 2 + 64,
                    height / 2 + 27,

                    width / 2 + 69,
                    height / 2 + 32,

                    0xffff00ff
            );

        if (pos2 != Integer.MIN_VALUE)
            guiGraphics.fill(
                    width / 2 + 83,
                    height / 2 + 27,

                    width / 2 + 88,
                    height / 2 + 32,

                    0xffff00ff
            );

        if (posThin1 != Integer.MIN_VALUE)
            guiGraphics.fill(
                    width / 2 + 102,
                    height / 2 + 27,

                    width / 2 + 107,
                    height / 2 + 32,

                    0xffff00ff
            );

        if (posThin2 != Integer.MIN_VALUE)
            guiGraphics.fill(
                    width / 2 + 121,
                    height / 2 + 27,

                    width / 2 + 126,
                    height / 2 + 32,

                    0xffff00ff
            );

        if (posTreasure != Integer.MIN_VALUE)
            guiGraphics.fill(
                    width / 2 + 140,
                    height / 2 + 27,

                    width / 2 + 145,
                    height / 2 + 32,

                    0xffff00ff
            );


        //Steady Bobber
        guiGraphics.drawString(this.font, "Steady Bobber", width / 2 + 72, height / 2 + 43, 0x000000, false);
        if (thinForgiving != SIZE_1) guiGraphics.fill(
                width / 2 + 62,
                height / 2 + 44,

                width / 2 + 67,
                height / 2 + 49,

                0xffff00ff
        );

        //Units
        guiGraphics.drawString(this.font, Component.translatable(unitSelected.translationKey), width / 2 - 50, height / 2 + 102, 0x000000, false);


        if (treasureActive)
            renderTreasure(guiGraphics);


        FishingMinigameScreen.renderMainElements(guiGraphics, width, height, isHoldingSpace, TANK);

        //pos 1
        if (pos1 != Integer.MIN_VALUE)
            renderHitPos(guiGraphics, partialTick, poseStack, pos1, 1, false);


        //pos 2
        if (pos2 != Integer.MIN_VALUE)
            renderHitPos(guiGraphics, partialTick, poseStack, pos2, 1, false);


        //pos thin 1
        if (posThin1 != Integer.MIN_VALUE)
            renderHitPos(guiGraphics, partialTick, poseStack, posThin1, 1, true);


        //pos thin 2
        if (posThin2 != Integer.MIN_VALUE)
            renderHitPos(guiGraphics, partialTick, poseStack, posThin2, 1, true);



        //pos treasure
        if (posTreasure != Integer.MIN_VALUE)
             FishingMinigameScreen.renderPosTreasure(guiGraphics, poseStack,  width, height, posTreasure);


        //wheel second layer
        guiGraphics.blit(
                TEXTURE, width / 2 - 32, height / 2 - 32,
                64, 64, 64, 192, 64, 64, 256, 256);

        //POINTER
        FishingMinigameScreen.renderPointer(guiGraphics, partialTick, poseStack, width, height, isHoldingSpace, pointerPos, speed, currentRotation);

        //LAST HIT MARKER
        {
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(lastHitMarkerPos)));
            poseStack.translate(-centerX, -centerY, 0);

            //16 offset on y for texture centering
            guiGraphics.blit(
                    TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                    64, 64, 128, 128, 64, 64, 256, 256);

            poseStack.popPose();
        }

        //LAST LAST HIT MARKER
        {
            poseStack.pushPose();
            RenderSystem.enableBlend();
            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(lastLastHitMarkerPos)));
            poseStack.translate(-centerX, -centerY, 0);

            //16 offset on y for texture centering
            guiGraphics.blit(
                    TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                    64, 64, 192, 128, 64, 64, 256, 256);

            RenderSystem.disableBlend();
            poseStack.popPose();
        }

        //LAST LAST LAST HIT MARKER
        {
            poseStack.pushPose();
            RenderSystem.enableBlend();
            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(lastLastLastHitMarkerPos)));
            poseStack.translate(-centerX, -centerY, 0);

            //16 offset on y for texture centering
            guiGraphics.blit(
                    TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                    64, 64, 192, 64, 64, 64, 256, 256);

            RenderSystem.disableBlend();
            poseStack.popPose();
        }

        FishingMinigameScreen.renderDecor(guiGraphics, width, height, completionSmooth, itemBeingFished);

        for (HitFakeParticle instance : hitParticles)
        {
            FishingMinigameScreen.renderParticle(guiGraphics, instance, poseStack, width, height);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        super.mouseClicked(mouseX, mouseY, button);

        int imageWidth = 512;
        int imageHeight = 256;

        int uiX = (width - imageWidth) / 2;
        int uiY = (height - imageHeight) / 2;

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //gui less
        if (x > 226 && x < 240 && mouseY > 20 && mouseY < 50)
        {
            int current = Minecraft.getInstance().options.guiScale().get();
            if (current > 1)
                Minecraft.getInstance().options.guiScale().set(current - 1);
        }

        //gui more
        if (x > 267 && x < 280 && mouseY > 20 && mouseY < 50)
        {
            int current = Minecraft.getInstance().options.guiScale().get();
            Minecraft.getInstance().options.guiScale().set(current + 1);
        }

        //move markers
        if (x > 316 && x < 328 && y > 105 && y < 112)
        {
            moveMarkers = !moveMarkers;
        }

        //change rotation
        if (x > 316 && x < 328 && y > 172 && y < 180)
        {
            //no steady > steady
            if (thinForgiving == SIZE_1)
            {
                thinForgiving = SIZE_2;
                bigForgiving = SIZE_4;
                difficultyBobberOffset = 16;
            }
            else
            //steady > no steady
            {
                thinForgiving = SIZE_1;
                bigForgiving = SIZE_3;
                difficultyBobberOffset = 0;
            }
        }

        //change rotation
        if (x > 316 && x < 328 && y > 125 && y < 145)
        {
            changeRotation = !changeRotation;
        }

        //speed
        if (x > 316 && x < 330 && y > 185 && y < 225)
        {
            speed--;
            if (speed < 0) speed = 0;
        }

        //speed
        if (x > 395 && x < 410 && y > 185 && y < 225)
        {
            speed++;
        }

        //hit delay
        if (x > 316 && x < 325 && y > 70 && y < 100)
        {
            hitDelay = (float) ((int) (hitDelay * 10)) / 10;
            hitDelay -= 0.2f;
            hitDelay = (float) ((int) (hitDelay * 10)) / 10;
        }

        //hit delay
        if (x > 396 && x < 410 && y > 70 && y < 100)
        {
            hitDelay = (float) ((int) (hitDelay * 10)) / 10;
            hitDelay += 0.2f;
            hitDelay = (float) ((int) (hitDelay * 10)) / 10;
        }

        //hit delay next
        if (x > 312 && x < 330 && y > 226 && y < 240)
        {
            unitSelected = unitSelected.next();
            Config.UNIT.set(unitSelected);
            Config.UNIT.save();
        }

        //hit delay next
        if (x > 193 && x < 205 && y > 226 && y < 240)
        {
            unitSelected = unitSelected.previous();
            Config.UNIT.set(unitSelected);
            Config.UNIT.save();
        }

        //markers
        if (x > 319 && x < 330 && y > 153 && y < 166)
        {
            if (pos1 == Integer.MIN_VALUE)
                pos1 = getRandomFreePosition();
            else
                pos1 = Integer.MIN_VALUE;
        }

        if (x > 338 && x < 349 && y > 153 && y < 166)
        {
            if (pos2 == Integer.MIN_VALUE)
                pos2 = getRandomFreePosition();
            else
                pos2 = Integer.MIN_VALUE;
        }

        if (x > 357 && x < 368 && y > 153 && y < 166)
        {
            if (posThin1 == Integer.MIN_VALUE)
                posThin1 = getRandomFreePosition();
            else
                posThin1 = Integer.MIN_VALUE;
        }

        if (x > 376 && x < 387 && y > 153 && y < 166)
        {
            if (posThin2 == Integer.MIN_VALUE)
                posThin2 = getRandomFreePosition();
            else
                posThin2 = Integer.MIN_VALUE;
        }

        if (x > 395 && x < 406 && y > 153 && y < 166)
        {
            if (posTreasure == Integer.MIN_VALUE)
                posTreasure = getRandomFreePosition();
            else
                posTreasure = Integer.MIN_VALUE;
        }

        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == Minecraft.getInstance().options.keyJump.getKey().getValue()) {
            isHoldingSpace = false;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {

        //closes when pressing E
        InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey))
        {
            this.onClose();
            return true;
        }

        //spacebar input
        if (keyCode == Minecraft.getInstance().options.keyJump.getKey().getValue())
        {

            isHoldingSpace = true;

            if (gracePeriod > 0) gracePeriod = 0;

            Minecraft.getInstance().player.swing(hand, true);

            boolean hitSomething = false;

            Vec3 pos = Minecraft.getInstance().player.position();
            ClientLevel level = Minecraft.getInstance().level;

            float pointerPosPrecise = (pointerPos + ((speed * partial) * currentRotation));

            pointerPosPrecise += hitDelay * speed * currentRotation;

            lastLastLastHitMarkerPos = lastLastHitMarkerPos;
            lastLastHitMarkerPos = lastHitMarkerPos;
            lastHitMarkerPos = pointerPosPrecise;

            //pos 1
            if (FishingMinigameScreen.isHitSuccesful(pointerPosPrecise, pos1, bigForgiving))
            {
                addParticles(pos1, 15);
                if (moveMarkers) pos1 = getRandomFreePosition();
                completion += reward;
                hitSomething = true;
            }

            //pos2
            if (FishingMinigameScreen.isHitSuccesful(pointerPosPrecise, pos2, bigForgiving))
            {
                addParticles(pos2, 15);
                if (moveMarkers) pos2 = getRandomFreePosition();
                completion += reward;
                hitSomething = true;
            }

            //pos thin 1
            if (FishingMinigameScreen.isHitSuccesful(pointerPosPrecise, posThin1, thinForgiving))
            {
                addParticles(posThin1, 30);
                if (moveMarkers) posThin1 = getRandomFreePosition();
                completion += rewardThin;
                hitSomething = true;
            }

            //pos thin 2
            if (FishingMinigameScreen.isHitSuccesful(pointerPosPrecise, posThin2, bigForgiving))
            {
                addParticles(posThin2, 30);
                if (moveMarkers) posThin2 = getRandomFreePosition();
                completion += rewardThin;
                hitSomething = true;
            }

            //if hit sweet spot treasure
            if (FishingMinigameScreen.isHitSuccesful(pointerPosPrecise, posTreasure, treasureForgiving))
            {
                addParticles(posTreasure, 30, true);
                if (moveMarkers) posTreasure = getRandomFreePosition();
                treasureProgress += treasureReward;
                hitSomething = true;
            }


            if (hitSomething)
            {
                consecutiveHits++;

                if (hook.is(ModItems.STONE_HOOK))
                {
                    if (fp.rarity() == FishProperties.Rarity.COMMON) gracePeriod = 40;
                    if (fp.rarity() == FishProperties.Rarity.UNCOMMON) gracePeriod = 20;
                    if (fp.rarity() == FishProperties.Rarity.RARE) gracePeriod = 15;
                    if (fp.rarity() == FishProperties.Rarity.EPIC) gracePeriod = 10;
                    if (fp.rarity() == FishProperties.Rarity.LEGENDARY) gracePeriod = 5;
                }

                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1, 1, false);
                if (changeRotation) currentRotation *= -1;
            }
            else
            {
                consecutiveHits = 0;
                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1, 1, false);
                completion -= penalty;
                perfectCatch = false;
            }

        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void tick()
    {
        pointerPos += (int) (speed * currentRotation);

        if (pointerPos > 360) pointerPos -= 360;
        if (pointerPos < 0) pointerPos += 360;

        gracePeriod--;

        tickCount++;

        completionSmooth += (int) Math.signum(completion - completionSmooth);
        completionSmooth += (int) Math.signum(completion - completionSmooth);

        treasureProgressSmooth += (int) Math.signum(treasureProgress - treasureProgressSmooth);

        if (tickCount % 5 == 0 && gracePeriod < 0)
        {
            completion -= decay;
        }

        if (completionSmooth < 0)
        {
            //this.onClose();
            completion = 0;
            completionSmooth = 0;
        }

        if (treasureProgressSmooth > 100)
        {
            posTreasure = Integer.MIN_VALUE;
        }

        if (completionSmooth > 75)
        {
            completion = 75;
            completionSmooth = 75;
            //this.onClose();
        }

        hitParticles.removeIf(HitFakeParticle::tick);
    }

    @Override
    public void onClose()
    {
        Config.HIT_DELAY.set((double) hitDelay);
        Config.HIT_DELAY.save();

        Config.MINIGAME_GUI_SCALE.set(Minecraft.getInstance().options.guiScale().get());
        Config.MINIGAME_GUI_SCALE.save();

        Minecraft.getInstance().options.guiScale().set(previousGuiScale);

        this.minecraft.popGuiLayer();
    }

    private void addParticles(int posInDegrees, int count)
    {
        addParticles(posInDegrees, count, false);
    }

    private void addParticles(int posInDegrees, int count, boolean treasure)
    {
        int xPos = (int) (30 * Math.cos(Math.toRadians(posInDegrees - 90)));
        int yPos = (int) (30 * Math.sin(Math.toRadians(posInDegrees - 90)));

        for (int i = 0; i < count; i++)
        {
            if (bobber.is(ModItems.GLITTER_BOBBER))
            {
                hitParticles.add(new HitFakeParticle(
                        xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1),
                        r.nextFloat(),
                        r.nextFloat(),
                        r.nextFloat(),
                        1
                ));
                continue;
            }

            if (bobber.is(ModItems.COLORFUL_BOBBER))
            {
                hitParticles.add(new HitFakeParticle(
                        xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1),
                        bobber.get(ModDataComponents.BOBBER_COLOR).r(),
                        bobber.get(ModDataComponents.BOBBER_COLOR).g(),
                        bobber.get(ModDataComponents.BOBBER_COLOR).b(),
                        1
                ));
                continue;
            }

            if (treasure)
            {
                //red particles if treasure sweet spot was hit
                hitParticles.add(new HitFakeParticle(
                        xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1),
                        0.7f + r.nextFloat() / 3, 0.5f, 0.5f, 1
                ));
            }
            else
            {
                hitParticles.add(new HitFakeParticle(xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1)));
            }

        }
    }

    public enum Units
    {
        METRIC("gui.guide.units.metric", 1f, 1f),
        IMPERIAL("gui.guide.units.imperial", 0.3937f, 0.0352739619495804f),
        CHEESEBURGER("gui.guide.units.cheeseburger", 0.09f, 0.0087f),
        FOOTBALL("gui.guide.units.football", 0.04545f, 0.00233f),
        DEVELOPER_HEIGHT("gui.guide.units.developer", 0.00592f, 0.0000140845f),
        BANANA("gui.guide.units.banana", 0.05f, 0.00833f),
        DUCK("gui.guide.units.duck", 0.02f, 0.0006667f),
        SPACE_WHALE("gui.guide.units.space_whale", 1f, 1f),
        SCIENTIFIC("gui.guide.units.scientific", 1f, 1f),
        ;

        private static final Units[] vals = values();
        private final String translationKey;
        private final float multiplierSize;
        private final float multiplierWeight;

        Units(String translationKey, float multiplierSize, float multiplierWeight)
        {
            this.translationKey = translationKey;
            this.multiplierSize = multiplierSize;
            this.multiplierWeight = multiplierWeight;
        }

        public String getTranslationKey()
        {
            return this.translationKey;
        }

        public float getMultiplierSize()
        {
            return this.multiplierSize;
        }

        public float getMultiplierWeight()
        {
            return this.multiplierWeight;
        }

        public Units next()
        {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        public Units previous()
        {
            if (this.ordinal() == 0) return vals[vals.length - 1];
            return vals[(this.ordinal() - 1) % vals.length];
        }

        public String getSizeAsString(int sizeInCm)
        {
            //space whale is always infinite
            if (this.equals(Units.SPACE_WHALE)) return "∞ space whales";
            if (this.equals(Units.SCIENTIFIC)) return "0 AU";

            float size = sizeInCm * this.getMultiplierSize();
            String sizeString = ((float) (int) (size * 100)) / 100 + " " + I18n.get(this.getTranslationKey() + ".size");

            if (this.equals(Units.METRIC))
            {
                sizeString = ((int) size) + "cm";
                if (size > 100) sizeString = (float) ((int) (size / 100 * 100)) / 100 + "m";
            }

            if (this.equals(Units.IMPERIAL))
            {
                sizeString = ((int) size) + "''";
                if (size > 12) sizeString = ((int) (size / 12)) + "'" + ((int) (size % 12)) + "''";
            }

            return sizeString;
        }

        public String getWeightAsString(int weightInGrams)
        {
            //space whale is always infinite
            if (this.equals(Units.SPACE_WHALE)) return "∞ space whales";
            if (this.equals(Units.SCIENTIFIC)) return "0 R136a1's";

            float weight = weightInGrams * this.getMultiplierWeight();
            String weightString = ((float) (int) (weight * 100)) / 100 + " " + I18n.get(this.getTranslationKey() + ".weight");

            if (this.equals(Units.METRIC))
            {
                if (weight < 1000) weightString = ((int) weight) + "g";
                if (weight > 1000) weightString = (float) ((int) (weight / 1000 * 100)) / 100 + "kg";
            }

            if (this.equals(Units.IMPERIAL))
            {
                weightString = ((int) weight) + "oz";
                if (weight > 12) weightString = ((int) (weight / 16)) + " lb " + ((int) (weight % 16)) + " oz";
            }

            return weightString;
        }

    }

    public void renderHitPos(GuiGraphics guiGraphics, float partialTick, PoseStack poseStack, float hitPos, float hitPosVanishing, boolean isThin){
        FishingMinigameScreen.renderHitPos(guiGraphics, partialTick, poseStack, width, height, currentRotation, 0, difficultyBobberOffset, hitPos, hitPosVanishing, isThin);
    }

    public void renderTreasure(GuiGraphics guiGraphics){
        FishingMinigameScreen.renderTreasure(guiGraphics, width, height, treasureProgress, treasureProgressSmooth, ItemStack.EMPTY, bobber);
    }

    public void renderPointer(GuiGraphics guiGraphics, float partialTick, PoseStack poseStack){
       FishingMinigameScreen.renderPointer(guiGraphics, partialTick, poseStack, width, height, isHoldingSpace, pointerPos, speed, currentRotation);
    }

    public void renderKimbeMarker(GuiGraphics guiGraphics, PoseStack poseStack){
        FishingMinigameScreen.renderKimbeMarker(guiGraphics, poseStack, width, height, 0, lastHitMarkerPos, bobber);
    }


    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

}
