package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvents;

import java.awt.image.ImagingOpException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DripstoneSweetSpotBehaviour extends NormalSweetSpotBehaviour
{
    static Map<ActiveSweetSpot, Integer> map = new HashMap<>();

    @Override
    public void onAdd(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onAdd(instance, ass);

        //map cleanup just in case
        List<ActiveSweetSpot> list = map.keySet().stream()
                //filter for ass's not in active sweetspots currently
                .filter(o -> instance.getActiveSweetSpots().stream().noneMatch(p -> p.equals(o))).toList();


        //remove all of those from the map
        list.forEach(o -> map.remove(o));
    }

    @Override
    public void tick(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.tick(instance, ass);

        //if ass is in map, decrease time and remove at 0
        if (map.containsKey(ass))
        {
            Integer value = map.get(ass);
            if(value == 10)
                Minecraft.getInstance().player.playSound(SoundEvents.POINTED_DRIPSTONE_LAND, 0.4f, 1.6f);

            if (value > 20)
            {
                ass.removed = true;
                return;
            }
            map.put(ass, value + 1);
        }
    }

    @Override
    public void onMiss(FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        super.onMiss(instance, ass);

        ass.canHit = false;

        //if map doesn't have ass, add it
        if (!map.containsKey(ass))
            map.put(ass, 0);

        Minecraft.getInstance().player.playSound(SoundEvents.POINTED_DRIPSTONE_BREAK, 1f, 1f);
    }

    @Override
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float partialTick, FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        if (ass.removed) return;

        // allows modifier to change color
        float[] shaderColor = RenderSystem.getShaderColor();
        RenderSystem.setShaderColor(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3] * ass.alpha);

        RenderSystem.enableBlend();

        poseStack.pushPose();
        if(map.containsKey(ass))
            poseStack.translate(0, map.get(ass) + 1 * partialTick, 0);

        //offsets vertically by the value in the map so it falls towards the center
        FishingMinigameScreen.renderPoseCentered(guiGraphics, ass.texture, 96);

        poseStack.popPose();

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
