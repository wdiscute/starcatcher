package com.wdiscute.starcatcher.minigame;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.ModDataComponents;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.networkandstuff.FishProperties;
import com.wdiscute.starcatcher.networkandstuff.Payloads;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Quaternionf;
import org.joml.Random;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class FishingMinigameScreen extends Screen implements GuiEventListener
{
    private static final Random r = new Random();
    private static final ResourceLocation TEXTURE = Starcatcher.rl("textures/gui/minigame.png");
    private static final ResourceLocation TEXTURE_TEST = Starcatcher.rl("textures/gui/minigame2.png");

    private static final int SIZE_1 = 5;
    private static final int SIZE_2 = 7;
    private static final int SIZE_3 = 12;
    private static final int SIZE_4 = 17;

    final FishProperties fp;
    final ItemStack itemBeingFished;
    final ItemStack bobber;
    final ItemStack bait;
    final ItemStack hook;

    final float speed;
    final int reward;
    final int rewardThin;
    final int treasureReward;
    final int penalty;
    final int decay;
    final boolean hasTreasure;
    final boolean changeRotation;

    int gracePeriod = 80;

    final InteractionHand hand;

    int pointerPos = 0;

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

    int tickCount = 0;
    List<HitFakeParticle> hitParticles = new ArrayList<>();


    public FishingMinigameScreen(FishProperties fp, ItemStack rod)
    {
        super(Component.empty());

        this.fp = fp;
        this.itemBeingFished = new ItemStack(BuiltInRegistries.ITEM.get(fp.fish()));
        this.bobber = rod.get(ModDataComponents.BOBBER).stack().copy();
        this.bait = rod.get(ModDataComponents.BAIT).stack().copy();
        this.hook = rod.get(ModDataComponents.HOOK).stack().copy();

        posTreasure = Integer.MIN_VALUE;

        //assign difficulty, if using mossy_hook it should make common, uncommon and rare into a harder difficulty
        FishProperties.Difficulty difficulty = bobber.is(ModItems.MOSSY_HOOK) &&
                (fp.rarity() == FishProperties.Rarity.COMMON ||
                        fp.rarity() == FishProperties.Rarity.UNCOMMON || fp.rarity() ==
                        FishProperties.Rarity.RARE) ?
                FishProperties.Difficulty.HARD : fp.dif();

        this.speed = difficulty.speed();
        this.reward = difficulty.reward();
        this.rewardThin = difficulty.rewardThin();
        this.treasureReward = difficulty.treasure().hitReward();
        this.penalty = difficulty.penalty();
        this.decay = difficulty.decay();
        this.hasTreasure = difficulty.treasure().hasTreasure();
        this.changeRotation = difficulty.changeRotationOnEveryHit();

        pos1 = difficulty.hasFirstMarker() ? getRandomFreePosition() : Integer.MIN_VALUE;
        pos2 = difficulty.hasSecondMarker() ? getRandomFreePosition() : Integer.MIN_VALUE;
        posThin1 = difficulty.hasFirstThinMarker() ? getRandomFreePosition() : Integer.MIN_VALUE;
        posThin2 = difficulty.hasSecondThinMarker() ? getRandomFreePosition() : Integer.MIN_VALUE;

        //make sweet spots fatter if difficulty bobber is being used
        if(bobber.is(ModItems.STEADY_BOBBER))
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

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        if (treasureActive)
        {
            //treasure bar
            guiGraphics.blit(
                    TEXTURE, width / 2 - 158, height / 2 - 42 + (int) (64 - (64f * treasureProgressSmooth) / 100),
                    5, 64 * treasureProgressSmooth / 100,
                    141, 6 + 64 - (float) (64 * treasureProgressSmooth) / 100,
                    5, 64 * treasureProgressSmooth / 100,
                    256, 256);

            //treasure chest?
            guiGraphics.blit(
                    TEXTURE, width / 2 - 16 - 155, height / 2 - 48,
                    32, 96, 96, 0, 32, 96, 256, 256);
        }

        //tank background
        guiGraphics.blit(
                TEXTURE, width / 2 - 42 - 100, height / 2 - 48,
                85, 97, 0, 0, 85, 97, 256, 256);

        //wheel background
        guiGraphics.blit(
                TEXTURE, width / 2 - 32, height / 2 - 32,
                64, 64, 0, 192, 64, 64, 256, 256);

        //spacebar
        guiGraphics.blit(
                TEXTURE, width / 2 - 16, height / 2 + 40,
                32, 16, 0, 112, 32, 16, 256, 256);


        //pos 1
        if (pos1 != Integer.MIN_VALUE)
        {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(pos1)));
            poseStack.translate(-centerX, -centerY, 0);

            guiGraphics.blit(
                    TEXTURE, width / 2 - 8, height / 2 - 8 - 25,
                    16, 16, 16 - difficultyBobberOffset, 160, 16, 16, 256, 256);

            poseStack.popPose();
        }


        //pos 2
        if (pos2 != Integer.MIN_VALUE)
        {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(pos2)));
            poseStack.translate(-centerX, -centerY, 0);

            guiGraphics.blit(
                    TEXTURE, width / 2 - 8, height / 2 - 8 - 25,
                    16, 16, 16 - difficultyBobberOffset, 160, 16, 16, 256, 256);

            poseStack.popPose();
        }

        //pos thin 1
        if (posThin1 != Integer.MIN_VALUE)
        {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(posThin1)));
            poseStack.translate(-centerX, -centerY, 0);

            guiGraphics.blit(
                    TEXTURE, width / 2 - 8, height / 2 - 8 - 25,
                    16, 16, 48 - difficultyBobberOffset, 160, 16, 16, 256, 256);

            poseStack.popPose();
        }

        //pos thin 2
        if (posThin2 != Integer.MIN_VALUE)
        {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(posThin2)));
            poseStack.translate(-centerX, -centerY, 0);

            guiGraphics.blit(
                    TEXTURE, width / 2 - 8, height / 2 - 8 - 25,
                    16, 16, 48 - difficultyBobberOffset, 160, 16, 16, 256, 256);

            poseStack.popPose();
        }

        //pos treasure
        if (posTreasure != Integer.MIN_VALUE)
        {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(posTreasure)));
            poseStack.translate(-centerX, -centerY, 0);

            guiGraphics.blit(
                    TEXTURE, width / 2 - 8, height / 2 - 8 - 25,
                    16, 16, 64, 160, 16, 16, 256, 256);

            poseStack.popPose();
        }

        //wheel second layer
        guiGraphics.blit(
                TEXTURE, width / 2 - 32, height / 2 - 32,
                64, 64, 64, 192, 64, 64, 256, 256);

        //POINTER
        {
            //TODO make it not use the partial ticks from rendering thread of whatever honestly its just nerd stuff that no one will care about
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(pointerPos + ((speed * partialTick) * currentRotation))));
            poseStack.translate(-centerX, -centerY, 0);

            //16 offset on y for texture centering
            guiGraphics.blit(
                    TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                    64, 64, 128, 192, 64, 64, 256, 256);

            poseStack.popPose();
        }

        //silver thing on top
        guiGraphics.blit(
                TEXTURE, width / 2 - 16, height / 2 - 16,
                32, 32, 208, 208, 32, 32, 256, 256);

        //fishing rod
        guiGraphics.blit(
                TEXTURE, width / 2 - 32 - 70, height / 2 - 24 - 57,
                64, 48, 192, 0, 64, 48, 256, 256);

        //fishing line
        guiGraphics.blit(
                TEXTURE, width / 2 - 6 - 102, height / 2 - 56 - 18,
                16, 112 - completionSmooth,
                176, 0 + completionSmooth,
                16, 112 - completionSmooth,
                256, 256);

        //Twitch chat didn't force me to write this comment
        //Lies, kuko010 force me to write said comment
        //FISH
        guiGraphics.renderItem(itemBeingFished, width / 2 - 8 - 100, height / 2 - 8 + 35 - completionSmooth);

        //particles
        for (HitFakeParticle instance : hitParticles)
        {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(instance.pos.x, instance.pos.y, 0);
            RenderSystem.setShaderColor(instance.r, instance.g, instance.b, instance.a);

            guiGraphics.blit(
                    TEXTURE, width / 2 - 8, height / 2 - 8,
                    16, 16, 80, 160, 16, 16, 256, 256);

            RenderSystem.setShaderColor(1, 1, 1, 1);
            guiGraphics.pose().popPose();
        }


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

            if (gracePeriod > 0) gracePeriod = 0;

            Minecraft.getInstance().player.swing(hand, true);

            boolean hitSomething = false;

            Vec3 pos = Minecraft.getInstance().player.position();
            ClientLevel level = Minecraft.getInstance().level;

            float pointerPosPrecise = (pointerPos + ((speed * partial) * currentRotation));


            //pos 1
            if ((Math.abs(pos1 - pointerPosPrecise) < bigForgiving || Math.abs(pos1 - pointerPosPrecise) > 360 - bigForgiving) && pos1 != Integer.MIN_VALUE)
            {
                addParticles(pos1, 15);
                pos1 = getRandomFreePosition();
                completion += reward;
                hitSomething = true;
            }

            //pos2
            if ((Math.abs(pos2 - pointerPosPrecise) < bigForgiving || Math.abs(pos2 - pointerPosPrecise) > 360 - bigForgiving) && pos2 != Integer.MIN_VALUE)
            {
                addParticles(pos2, 15);
                pos2 = getRandomFreePosition();
                completion += reward;
                hitSomething = true;
            }

            //pos thin 1
            if ((Math.abs(posThin1 - pointerPosPrecise) < thinForgiving || Math.abs(posThin1 - pointerPosPrecise) > 360 - thinForgiving) && posThin1 != Integer.MIN_VALUE)
            {
                addParticles(posThin1, 30);
                posThin1 = getRandomFreePosition();
                completion += rewardThin;
                hitSomething = true;
            }

            //pos thin 2
            if ((Math.abs(posThin2 - pointerPosPrecise) < thinForgiving || Math.abs(posThin2 - pointerPosPrecise) > 360 - thinForgiving) && posThin2 != Integer.MIN_VALUE)
            {
                addParticles(posThin2, 30);
                posThin2 = getRandomFreePosition();
                completion += rewardThin;
                hitSomething = true;
            }

            //if hit sweet spot treasure
            if ((Math.abs(posTreasure - pointerPosPrecise) < treasureForgiving || Math.abs(posTreasure - pointerPosPrecise) > 360 - treasureForgiving) && posTreasure != Integer.MIN_VALUE)
            {
                addParticles(posTreasure, 30, true);
                posTreasure = getRandomFreePosition();
                treasureProgress += treasureReward;
                hitSomething = true;
            }

            System.out.println("treasureReward " + treasureReward);
            System.out.println("treasureProgress " + treasureProgress);


            if (hitSomething)
            {
                consecutiveHits++;
                if ((hasTreasure && r.nextFloat() > 0.9 /*0.9*/ && completion < 40 && !treasureActive && treasureProgress == Integer.MIN_VALUE)
                ||
                (consecutiveHits == 3 && treasureProgress == Integer.MIN_VALUE && hook.is(ModItems.SHINY_HOOK)))
                {
                    treasureActive = true;
                    posTreasure = getRandomFreePosition();
                    treasureProgress = 0;
                    treasureProgressSmooth = 0;
                }

                if(hook.is(ModItems.STONE_HOOK))
                {
                    if(fp.rarity() == FishProperties.Rarity.COMMON) gracePeriod = 40;
                    if(fp.rarity() == FishProperties.Rarity.UNCOMMON) gracePeriod = 20;
                    if(fp.rarity() == FishProperties.Rarity.RARE) gracePeriod = 15;
                    if(fp.rarity() == FishProperties.Rarity.EPIC) gracePeriod = 10;
                    if(fp.rarity() == FishProperties.Rarity.LEGENDARY) gracePeriod = 5;
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
            this.onClose();
        }

        if (treasureProgressSmooth > 100)
        {
            posTreasure = Integer.MIN_VALUE;
        }

        if (completionSmooth > 75)
        {
            //if completed treasure minigame, or is a perfect catch with the mossy hook
            boolean awardTreasure = treasureProgressSmooth > 100 || (perfectCatch && hook.is(ModItems.MOSSY_HOOK));

            PacketDistributor.sendToServer(new Payloads.FishingCompletedPayload(tickCount, awardTreasure, perfectCatch, consecutiveHits));
            this.onClose();
        }

        hitParticles.removeIf(HitFakeParticle::tick);

    }

    @Override
    public void onClose()
    {
        PacketDistributor.sendToServer(new Payloads.FishingCompletedPayload(-1, false, false, consecutiveHits));
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
            if(bobber.is(ModItems.GLITTER_BOBBER))
            {
                hitParticles.add(new HitFakeParticle(xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1),
                        r.nextFloat(),
                        r.nextFloat(),
                        r.nextFloat(),
                        1
                ));
                continue;
            }

            if(bobber.is(ModItems.COLORFUL_BOBBER))
            {
                hitParticles.add(new HitFakeParticle(xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1),
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
                hitParticles.add(new HitFakeParticle(xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1),
                        0.7f + r.nextFloat() / 3, 0.5f, 0.5f, 1
                ));
            }
            else
            {
                hitParticles.add(new HitFakeParticle(xPos, yPos, new Vector2d(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1)));
            }

        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

}
