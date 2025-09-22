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

public class
FishingMinigameScreen extends Screen implements GuiEventListener
{

    private static final ResourceLocation TEXTURE = Starcatcher.rl("textures/gui/minigame.png");

    final ItemStack itemBeingFished;
    final ItemStack bobber;
    final ItemStack bait;

    int completion = 20;
    int completionSmooth = 20;


    int tickCount = 0;

    Random r = new Random();

    int sweetSpot1Pos;
    int sweetSpot2Pos;
    int sweetSpotTreasure;

    int hitReward = 10;
    int hitRewardThin = 15;
    int missPenalty = 15;

    int pointerPos = 0;

    boolean shouldFlipRotation;
    boolean shouldChangeSpeedEveryHit;
    boolean shouldHaveThinSweetSpot;

    int currentRotation = 1;

    int currentSpeed;
    int minSpeed;
    int maxSpeed;

    float partial;

    boolean treasureActive;
    boolean treasureCompleted;
    int treasureProgress = 0;
    int treasureProgressSmooth = 0;

    public FishingMinigameScreen(FishProperties fp, ItemStack rod)
    {
        super(Component.empty());

        this.itemBeingFished = new ItemStack(BuiltInRegistries.ITEM.get(fp.fish()));
        this.bobber = rod.get(ModDataComponents.BOBBER).copyOne();
        this.bait = rod.get(ModDataComponents.BAIT).copyOne();

        sweetSpot1Pos = r.nextInt(360);
        sweetSpot2Pos = 60 + r.nextInt(240) + sweetSpot1Pos;

        sweetSpotTreasure = 345623482;

        if (sweetSpot2Pos > 360) sweetSpot2Pos -= 360;

        {
            if (10 == 1)
            {
                hitReward = 10;
                hitRewardThin = 20;
                missPenalty = 10;

                currentSpeed = 6;

                shouldFlipRotation = false;
                shouldChangeSpeedEveryHit = false;

                shouldHaveThinSweetSpot = true;
            }

            if (10 == 2)
            {
                hitReward = 10;
                hitRewardThin = 15;
                missPenalty = 15;

                currentSpeed = 8;

                shouldFlipRotation = false;
                shouldChangeSpeedEveryHit = false;

                shouldHaveThinSweetSpot = true;
            }

            if (3 == 3)
            {
                hitReward = 10;
                hitRewardThin = 15;
                missPenalty = 10;

                currentSpeed = 10;

                shouldFlipRotation = true;
                shouldChangeSpeedEveryHit = false;

                shouldHaveThinSweetSpot = true;
            }

            if (10 == 4)
            {
                hitReward = 5;
                hitRewardThin = 15;
                missPenalty = 16;

                currentSpeed = 7;

                shouldFlipRotation = true;
                shouldChangeSpeedEveryHit = true;
                minSpeed = 7;
                maxSpeed = 20;

                shouldHaveThinSweetSpot = true;
            }

        }

    }


    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        partial = partialTick;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        treasureActive = false;
        if (treasureActive)
        {

            //treasure bar
            guiGraphics.blit(
                    TEXTURE, width / 2 - 158, height / 2 - 42 + (int)(64 - (64f * treasureProgressSmooth) / 100),
                    5, 64 * treasureProgressSmooth / 100,
                    141, 6  + 64 - (float) (64 * treasureProgressSmooth) / 100,
                    5, 64 * treasureProgressSmooth / 100,
                    256, 256);

            //treasure bar
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


        //SWEET_SPOT_1
        {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(sweetSpot1Pos)));
            poseStack.translate(-centerX, -centerY, 0);

            guiGraphics.blit(
                    TEXTURE, width / 2 - 8, height / 2 - 8 - 25,
                    16, 16, 16, 160, 16, 16, 256, 256);

            poseStack.popPose();
        }

        //SWEET_SPOT_2
        if (shouldHaveThinSweetSpot)
        {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(sweetSpot2Pos)));
            poseStack.translate(-centerX, -centerY, 0);

            guiGraphics.blit(
                    TEXTURE, width / 2 - 8, height / 2 - 8 - 25,
                    16, 16, 48, 160, 16, 16, 256, 256);

            poseStack.popPose();
        }


        //SWEET SPOT TREASURE
        if (treasureActive && treasureProgress < 100)
        {
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            float centerX = width / 2f;
            float centerY = height / 2f;

            poseStack.translate(centerX, centerY, 0);
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(sweetSpotTreasure)));
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
            poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(pointerPos + ((currentSpeed * partialTick) * currentRotation))));
            poseStack.translate(-centerX, -centerY, 0);

            //16 offset on y for texture centering
            guiGraphics.blit(
                    TEXTURE, width / 2 - 32, height / 2 - 32 - 16,
                    64, 64, 128, 192, 64, 64, 256, 256);

            poseStack.popPose();
        }

        //
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

        //FISH
        {
            guiGraphics.renderItem(itemBeingFished, width / 2 - 8 - 100, height / 2 - 8 + 35 - completionSmooth);
        }
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey))
        {
            this.onClose();
            return true;
        }

        if (keyCode == Minecraft.getInstance().options.keyJump.getKey().getValue())
        {
            Minecraft.getInstance().player.swing(InteractionHand.MAIN_HAND, true);

            boolean safe = false;

            Vec3 pos = Minecraft.getInstance().player.position();
            ClientLevel level = Minecraft.getInstance().level;

            float pointerPrecise = (pointerPos + ((currentSpeed * partial) * currentRotation));


            //if hit sweet spot 1
            if (Math.abs(sweetSpot1Pos - pointerPrecise) < 12 || Math.abs(sweetSpot1Pos - pointerPrecise) > 348)
            {
                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1, 1, false);

                //reposition sweet spot without overlapping old position or the other sweet spot
                int attempted;
                do
                {
                    //pick random place at least 60 degrees away from current spot
                    attempted = (60 + r.nextInt(240) + sweetSpot1Pos) % 360;
                }
                //do while the picked spot is within 30 degrees of one of the other spots
                while (Math.abs(attempted - sweetSpot2Pos) < 30 || Math.abs(attempted - sweetSpotTreasure) < 30);

                sweetSpot1Pos = attempted;

                //difficulty checks
                if (shouldFlipRotation) currentRotation *= -1;
                if (shouldChangeSpeedEveryHit) currentSpeed = minSpeed + r.nextInt(maxSpeed - minSpeed);

                completion += hitReward;
                safe = true;
            }

            //if hit sweet spot 2
            if (Math.abs(sweetSpot2Pos - pointerPrecise) < 5 || Math.abs(sweetSpot2Pos - pointerPrecise) > 355)
            {
                if (!shouldHaveThinSweetSpot) return false;

                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1, 1, false);

                //reposition sweet spot without overlapping old position or the other sweet spot
                int attempted;
                do
                {
                    //pick random place at least 60 degrees away from current spot
                    attempted = (60 + r.nextInt(240) + sweetSpot2Pos) % 360;
                }
                //do while the picked spot is within 30 degrees of one of the other spots
                while (Math.abs(attempted - sweetSpot1Pos) < 30 || Math.abs(attempted - sweetSpotTreasure) < 30);

                sweetSpot2Pos = attempted;

                //difficulty checks
                if (shouldFlipRotation) currentRotation *= -1;
                if (shouldChangeSpeedEveryHit) currentSpeed = minSpeed + r.nextInt(maxSpeed - minSpeed);

                completion += hitRewardThin;
                safe = true;
            }

            //if hit sweet spot treasure
            if (Math.abs(sweetSpotTreasure - pointerPrecise) < 7 || Math.abs(sweetSpotTreasure - pointerPrecise) > 353 && treasureActive && !treasureCompleted)
            {

                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1, 1, false);

                //reposition sweet spot without overlapping old position or the other sweet spot
                int attempted;
                do
                {
                    //pick random place at least 60 degrees away from current spot
                    attempted = (60 + r.nextInt(240) + sweetSpotTreasure) % 360;
                }
                //do while the picked spot is within 30 degrees of one of the other spots
                while (Math.abs(attempted - sweetSpot1Pos) < 30 || Math.abs(attempted - sweetSpot2Pos) < 30);

                sweetSpotTreasure = attempted;

                //difficulty checks
                if (shouldFlipRotation) currentRotation *= -1;
                if (shouldChangeSpeedEveryHit) currentSpeed = minSpeed + r.nextInt(maxSpeed - minSpeed);

                treasureProgress += 15;
                safe = true;
            }


            if (!safe)
            {
                level.playLocalSound(pos.x, pos.y, pos.z, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1, 1, false);
                completion -= 5;
            }

        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public void tick()
    {
        pointerPos += currentSpeed * currentRotation;

        if (pointerPos > 360) pointerPos -= 360;
        if (pointerPos < 0) pointerPos += 360;

        tickCount++;

        if (!treasureActive)
        {
            float chance = 0.0005f;

            if(bobber.is(ModItems.TREASURE_BOBBER))
            {
                chance = 0.0025f;
            }

            if (r.nextFloat() < chance)
            {
                treasureActive = true;
                treasureProgress = 0;
                int attempted;
                do
                {
                    //pick random place at least 60 degrees away from current spot
                    attempted = (60 + r.nextInt(240) + sweetSpotTreasure) % 360;
                }
                //do while the picked spot is within 30 degrees of one of the other spots
                while (Math.abs(attempted - sweetSpot1Pos) < 30 || Math.abs(attempted - sweetSpot2Pos) < 30);
                sweetSpotTreasure = attempted;
            }
        }

        if (treasureProgress > 100)
        {
            treasureCompleted = true;
        }

        completionSmooth += (int) Math.signum(completion - completionSmooth);

        treasureProgressSmooth += (int) Math.signum(treasureProgress - treasureProgressSmooth);

        if (tickCount % 5 == 0)
        {
            completion--;
        }

        if (completionSmooth < 0)
        {
            this.onClose();
        }

        //if (completionSmooth > 10)
        if (completionSmooth > 75)
        {
            PacketDistributor.sendToServer(new Payloads.FishingCompletedPayload(tickCount));
            this.onClose();
        }

    }

    @Override
    public void onClose()
    {
        PacketDistributor.sendToServer(new Payloads.FishingCompletedPayload(-1));
        this.minecraft.popGuiLayer();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

}
