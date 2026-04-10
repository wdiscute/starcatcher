package com.wdiscute.starcatcher.blocks.Telescope;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TelescopeScreen extends Screen
{
    List<FishProperties> stars = new ArrayList<>();

    double offsetX;
    double offsetY;

    int frames = 0;
    int fps = 999;
    int ticks = 0;

    Pair<Double, Double> cacheOffset = new Pair<>(0d, 0d);

    boolean omouseLocked = true;
    boolean mouseLocked = true;

    float zoomScale;
    float vanillaScale = (float) Minecraft.getInstance().getWindow().getGuiScale();

    protected TelescopeScreen(Component title)
    {
        super(title);

        offsetX = 2700;
        offsetY = 900;

        stars.addAll(Minecraft.getInstance().level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY)
                .stream().filter(o -> !o.star().equals(FishProperties.Star.DEFAULT)).toList());


        //unscale vanilla gui scaling
        float vanillaScale = (float) Minecraft.getInstance().getWindow().getGuiScale();
        zoomScale = 1 / vanillaScale;

        zoomScale += 2;

        //stars.add(FishProperties.builder().withStar(new FishProperties.Star("wda", 0, 0, List.of(), 0xffffffff)).build());
        //stars.add(FishProperties.builder().withStar(new FishProperties.Star("wda", 900, 0, List.of(), 0xffffffff)).build());

        for (int i = 0; i < 36; i++)
        {
            for (int j = 0; j < 48; j++)
            {
                FishProperties.Star star = FishProperties.Star.fromRaAndDec("wda", j / 2, j % 2 == 0 ? 30 : 0, 0, -90 + i * 5, -90 + i * 5 > 0 ? 0x55ffff00 : 0x55ff00ff);
                FishProperties fp = FishProperties.builder().withStar(star).build();

                //stars.add(fp);
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        frames++;

        int screenHeight = Minecraft.getInstance().getWindow().getScreenHeight();
        int screenWidth = Minecraft.getInstance().getWindow().getScreenWidth();
        int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        PoseStack pose = guiGraphics.pose();

        //background
        guiGraphics.fill(0, 0, width, height, 0xff000000);

        //scaling
        pose.pushPose();

        //translate to center for scaling
        pose.translate((float) width / 2, (float) height / 2, 0);

        //zoom scale
        guiGraphics.pose().scale(zoomScale / vanillaScale, zoomScale / vanillaScale, zoomScale / vanillaScale);

        int number = 0;
        for (FishProperties fp : stars)
        {
            FishProperties.Star star = fp.star();

            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 3; j++)
                {

                    double x = star.x() - offsetX;
                    x += 3600 * i;
                    double y = star.y() - offsetY;
                    y += 1800 * j;
                    if (j % 2 != 0) x -= 1800;

                    //culling
                    if (x > screenWidth / zoomScale / 2) continue;
                    if (x < -screenWidth / zoomScale / 2) continue;
                    if (y > screenHeight / zoomScale / 2) continue;
                    if (y < -screenHeight / zoomScale / 2) continue;

                    pose.pushPose();
                    pose.translate(x, y, 0);

                    //render lines to connections
                    for (String connectionStarName : star.connections())
                    {
                        Optional<FishProperties> optional = stars.stream().filter(fplol -> fplol.star().name().equals(connectionStarName)).findFirst();
                        //if rl is valid
                        if (optional.isPresent())
                        {
                            FishProperties.Star connectionStar = optional.get().star();
                            double distance = Vector2d.distance(star.x(), star.y(), connectionStar.x(), connectionStar.y());

                            var a = star.y() - connectionStar.y();
                            var b = star.x() - connectionStar.x();
                            var angle = Mth.atan2(a, b);

                            guiGraphics.pose().pushPose();
                            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float) Math.toDegrees(angle) + 90));
                            guiGraphics.fill(0, 0, 1, (int) distance, star.debugColor());
                            guiGraphics.pose().popPose();
                        }
                    }

                    if (Mth.abs((float) x) < 8 && Mth.abs((float) y) < 8)
                    {
                        guiGraphics.renderItem(new ItemStack(fp.catchInfo().fish().value()), 5, -7, 0xffffffff);
                        guiGraphics.drawString(this.font, fp.catchInfo().fish().value().getDescription(), 25, -3, 0xffffffff);
                    }

                    //render star 🔥🔥🔥🔥🔥
                    number++;
                    guiGraphics.fill(-2, -2, 2, 2, star.debugColor());
                    pose.popPose();
                }
            }
        }

        pose.popPose();

        //cursor
        pose.pushPose();
        guiGraphics.fill(width / 2 - 2, height / 2 - 2, width / 2 + 2, height / 2 + 2, 0xffff0000);

        pose.popPose();

        pose.pushPose();
        pose.scale(3, 3, 3);
        guiGraphics.drawString(this.font, "rendered " + number + " stars", 0, 0, 0xffffffff);
        guiGraphics.drawString(this.font, "fps: " + fps, 0, 10, 0xffffffff);
        pose.popPose();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        zoomScale += (float) (scrollY / 20);
        zoomScale = Math.clamp(zoomScale, 0.01f, 4);
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private void lockCursor()
    {
        omouseLocked = mouseLocked;
        if (mouseLocked)
        {
            GLFW.glfwSetInputMode(this.minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        } else
        {
            GLFW.glfwSetInputMode(this.minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == GLFW.GLFW_KEY_LEFT_ALT)
        {
            mouseLocked = !mouseLocked;
            lockCursor();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY)
    {
        //super.mouseMoved(mouseX, mouseY);

        if (omouseLocked && mouseLocked)
        {
            GLFW.glfwSetCursorPos(this.minecraft.getWindow().getWindow(), (double) this.minecraft.getWindow().getWidth() / 2, (double) this.minecraft.getWindow().getHeight() / 2);

            //dumb lil formula to make the mouse sensitivity feel better on non default zoom scales
            double scale = zoomScale <= 1f ? 4f - 3f * zoomScale : 1f - (zoomScale - 1f) / 4f;
            scale /= 6;

            offsetX += (mouseX - (double) this.minecraft.getWindow().getGuiScaledWidth() / 2) * scale * vanillaScale;
            offsetY += (mouseY - (double) this.minecraft.getWindow().getGuiScaledHeight() / 2) * scale * vanillaScale;

            if (offsetX > 3600) offsetX -= 3600;
            if (offsetX < 0) offsetX += 3600;

            if (offsetY > 3600) offsetY -= 3600;
            if (offsetY < 0) offsetY += 3600;
        }

    }

    @Override
    public void tick()
    {
        super.tick();
        ticks++;
        if(ticks % 20 == 0)
        {
            fps = frames;
            frames = 0;
        }
    }

    @Override
    protected void init()
    {
        super.init();
        double xpos = (double) this.minecraft.getWindow().getScreenWidth() / 2;
        double ypos = (double) this.minecraft.getWindow().getScreenHeight() / 2;
        InputConstants.grabOrReleaseMouse(this.minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR_HIDDEN, xpos, ypos);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
