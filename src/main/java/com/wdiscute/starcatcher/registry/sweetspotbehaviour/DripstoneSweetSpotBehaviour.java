package com.wdiscute.starcatcher.registry.sweetspotbehaviour;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.starcatcher.minigame.ActiveSweetSpot;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.sounds.SoundEvents;
import org.joml.Matrix3x2fStack;

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
            if (value == 10)
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
    public void render(GuiGraphicsExtractor guiGraphics, Matrix3x2fStack poseStack, float partialTick, FishingMinigameScreen instance, ActiveSweetSpot ass)
    {
        if (ass.removed) return;

        poseStack.pushMatrix();

        if (map.containsKey(ass))
            poseStack.translate(0, map.get(ass) + 1 * partialTick);

        //offsets vertically by the value in the map so it falls towards the center
        ass.texture.render(guiGraphics, -48, -48);

        poseStack.popMatrix();
    }
}
