package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.registry.minigamemodifiers.AbstractMinigameModifier;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SettingsScreen extends FishingMinigameScreen {
    public static final Identifier SETTINGS = Starcatcher.rl("textures/gui/minigame/settings.png");
    public static final Identifier GUI_SCALE = Starcatcher.rl("textures/gui/minigame/gui_scale.png");

    FishProperties.SizeAndWeight.Units unitSelected;

    public SettingsScreen(FishProperties fp, ItemStack rod) {
        super(fp, rod);
    }

    @Override
    protected void init() {
        super.init();

        hitDelay = (SCConfig.HIT_DELAY.get().floatValue());
        unitSelected = SCConfig.UNIT.get();
        //Use widgets instead of doing hovering/clicking logic manually
        // addRenderableWidget(new GuiScaleWidget(width / 2 - 50, 0, 100, 50));


        //new gui scale
        addRenderableWidget(new LeftRightButtonWidget<>(
                () -> renderScale, // the value to render
                () -> renderScale -= 0.1f, // left button action
                () -> renderScale += 0.1f, // right button action
                0.2f, //lower limit
                5.9f, //upper limit
                Component.literal("Scale"),
                width / 2 + 100, height / 2 - 90, 91, 19, 160, 69, 256, 256, SETTINGS, 13));


        //hit delay
        addRenderableWidget(new LeftRightButtonWidget<>(
                () -> hitDelay, // the value to render
                () -> hitDelay -= 0.2f, // left button action
                () -> hitDelay += 0.2f, // right button action
                -5f,  //lower limit
                5f, //upper limit
                Component.literal("Hit Delay"),
                width / 2 + 100, height / 2 - 40, 91, 19, 160, 69, 256, 256, SETTINGS, 13));

        //Speed
        addRenderableWidget(new LeftRightButtonWidget<>(
                () -> pointerSpeed, // the value to render
                () -> pointerSpeed -= 0.1f, // left button action
                () -> pointerSpeed += 0.1f, // right button action
                null,
                null,
                Component.literal("Speed"),
                width / 2 + 100, height / 2 + 10, 91, 19, 160, 69, 256, 256, SETTINGS, 13));

        //x offset
        addRenderableWidget(new LeftRightButtonWidget<>(
                () -> xOffset, // the value to render
                () -> xOffset--, // left button action
                () -> xOffset++, // right button action
                null,
                null,
                Component.literal("X Offset"),
                width / 2 + 100, height / 2 + 30, 91, 19, 160, 69, 256, 256, SETTINGS, 13));

        //y offset
        addRenderableWidget(new LeftRightButtonWidget<>(
                () -> yOffset, // the value to render
                () -> yOffset++, // left button action
                () -> yOffset--, // right button action
                null,
                null,
                Component.literal("Y Offset"),
                width / 2 + 100, height / 2 + 50, 91, 19, 160, 69, 256, 256, SETTINGS, 13));


        //Units
        addRenderableWidget(new LeftRightButtonWidget<>(
                () -> unitSelected, // the value to render
                () -> unitSelected = unitSelected.previous(), // left button action
                () -> unitSelected = unitSelected.next(), // right button action
                null,
                null,
                Component.literal("Units"),
                width / 2, height / 2 + 80, 136, 25, 34, 222, 256, 256, SETTINGS, 16));

    }

    public Options getOptions() {
        return getMinecraft().options;
    }

    private @NotNull OptionInstance<Integer> guiScale() {
        return getOptions().guiScale();
    }

    @Override
    public boolean isSettingsScreen() {
        return true;
    }

    @Override
    public void inputPressed() {
        if (!(isHoldingMouse && children().stream().anyMatch(GuiEventListener::isFocused)))
            super.inputPressed();

        if (progress > 100) progress = 100;
        if (progress < 0 ) progress = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (progress > 100) progress = 100;
        if (progress < 0 ) progress = 0;
    }

    @Override
    public void onClose() {
        //round it to 2 decimal points
        SCConfig.HIT_DELAY.set(Math.round(hitDelay * 10) / 10d);
        SCConfig.HIT_DELAY.save();


        SCConfig.MINIGAME_RENDER_SCALE.set((double) renderScale);
        SCConfig.MINIGAME_RENDER_SCALE.save();

        SCConfig.MINIGAME_X_OFFSET.set(xOffset);
        SCConfig.MINIGAME_Y_OFFSET.set(yOffset);
        SCConfig.MINIGAME_X_OFFSET.save();
        SCConfig.MINIGAME_Y_OFFSET.save();


        SCConfig.UNIT.set(unitSelected);
        SCConfig.UNIT.save();

        modifiers.forEach(AbstractMinigameModifier::onRemove);

        this.minecraft.popGuiLayer();
    }

    public class LeftRightButtonWidget<T extends Comparable<T>> extends AbstractWidget {
        int uOffset, vOffset, textureWidth, textureHeight, buttonWidth;
        Identifier texture;
        Supplier<T> value;
        @Nullable T rightLimit, leftLimit;
        Runnable rightAction, leftAction;
        Component name;

        // This is automatically centered
        public LeftRightButtonWidget(Supplier<T> value, Runnable leftAction, Runnable rightAction, @Nullable T leftLimit, @Nullable T rightLimit, MutableComponent name,
                                     int x, int y, int width, int height, int uOffset, int vOffset, int textureWidth, int textureHeight, Identifier texture, int buttonWidth) {

            super(x - (width >> 1), y - (height >> 1), width, height, Component.empty());

            this.uOffset = uOffset;
            this.vOffset = vOffset;
            this.texture = texture;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.buttonWidth = buttonWidth;

            this.rightAction = rightAction;
            this.leftAction = leftAction;
            this.rightLimit = rightLimit;
            this.leftLimit = leftLimit;

            this.value = value;
            this.name = name;
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
        {
            double mouseX = event.x();
            double mouseY = event.y();
            //confirm the mouse is on the element
            if (!(mouseX > getX() && mouseX < getRight() && mouseY > getY() && mouseY < getBottom()))
                return super.mouseClicked(event, doubleClick);

            //left button
            if (mouseX < getX() + buttonWidth){
                if (leftLimit != null && value.get().compareTo(leftLimit) <= 0) return false;

                leftAction.run();
            }


            //right button
            if (mouseX > getRight() - buttonWidth){
                if (rightLimit != null && value.get().compareTo(rightLimit) >= 0) return false;

                rightAction.run();
            }

            return true;

        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a)
        {
            Object o = value.get();
            if (o instanceof Float number){
                number = Math.round(number * 10) / 10f;
                o = number;
            }

            MutableComponent component = Component.empty().append(name).append(": ").append(String.valueOf(o));
            guiGraphics.centeredText(getMinecraft().font, component, getX() + (getWidth() / 2), getY() + (getHeight() / 4), 0x000000);

            guiGraphics.blit(RenderPipelines.GUI,
                    texture, getX(), getY(),
                    getWidth(), getHeight(), uOffset, vOffset, getWidth(), getHeight(), textureWidth, textureHeight);

        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
    }


    public class GuiScaleWidget extends AbstractWidget {
        public GuiScaleWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty());

            if (hasDistantHorizons()) {
                setTooltip(Tooltip.create(Component.literal("GUI Scale is not supported while Distant Horizons is installed. It causes a massive frame drop upon starting and ending the minigame.")));
            } else {
                setTooltip(Tooltip.create(Component.literal("Change the GUI Scale of the minigame.")));
            }
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
        {
            double mouseX = event.x();
            double mouseY = event.y();

            //confirm the mouse is on the element
            if (!(mouseX > getX() && mouseX < getRight() && mouseY > getY() && mouseY < getBottom()))
                return super.mouseClicked(event, doubleClick);

            int current = guiScale().get();

            // if it's on the right half
            if (mouseX < getX() + getWidth() / 2f) {
                if (current > 1)
                    guiScale().set(current - 1);

            } else {
                guiScale().set(current + 1);
            }
            return true;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a)
        {
            //GUI SCALE
            guiGraphics.blit(RenderPipelines.GUI,
                    GUI_SCALE, getX(), getY(),
                    getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());

        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
    }

}
