package com.wdiscute.starcatcher.messageinabottle.letter;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.network.SBSetEditableMessagePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class EditableMessageScreen extends Screen
{
    private final String sender;
    private final List<String> text = new ArrayList<>();
    private final List<EditBox> boxes = new ArrayList<>();
    private EditBox nameBox = new EditBox(Minecraft.getInstance().font, 0, 0, Component.empty());

    public static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/message/message.png");

    public static void openEditableMessageScreen(EditableMessage message)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.BOOK_PAGE_TURN);
        Minecraft.getInstance().setScreen(new EditableMessageScreen(message));
    }

    public EditableMessageScreen(EditableMessage message)
    {
        super(Component.empty());

        text.addAll(message.text());
        sender = message.sender();
    }

    int uiX;
    int uiY;

    @Override
    protected void init()
    {
        super.init();
        uiX = (width - 512) / 2;
        uiY = (height - 256) / 2;

        //text
        boxes.clear();
        for (int i = 0; i < 15; i++)
        {
            EditBox box = new EditBox(this.font, uiX + 136, uiY + 55 + i * 10, 500, 12, Component.empty());
            box.setCanLoseFocus(true);
            box.setTextColor(0x635040);
            box.setBordered(false);
            box.setMaxLength(40);
            box.setTextShadow(false);
            box.setEditable(true);
            if (text.size() > i)
                box.setValue(text.get(i));
            addWidget(box);
            boxes.add(box);
        }

        //name
        nameBox = new EditBox(Minecraft.getInstance().font, uiX + 255, uiY + 208, 500, 12, Component.empty());
        nameBox.setCanLoseFocus(true);
        nameBox.setTextColor(0x635040);
        nameBox.setBordered(false);
        nameBox.setMaxLength(17);
        nameBox.setValue(sender);
        nameBox.setTextShadow(false);
        nameBox.setEditable(true);
        addWidget(nameBox);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height)
    {
        List<String> s = new ArrayList<>();

        String name = this.nameBox.getValue();

        for (int i = 0; i < 15; i++)
            s.add(this.boxes.get(i).getValue());

        this.init(minecraft, width, height);

        for (int i = 0; i < 15; i++)
            boxes.get(i).setValue(s.get(i));

        this.nameBox.setValue(name);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderImage(guiGraphics, BACKGROUND);
        boxes.forEach(b -> b.render(guiGraphics, mouseX, mouseY, partialTick));
        nameBox.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(key) && boxes.stream().noneMatch(EditBox::canConsumeInput) && !nameBox.canConsumeInput())
        {
            this.onClose();
            return true;
        }

        //if pressed enter, send arrow down to go to next line
        if (keyCode == GLFW.GLFW_KEY_ENTER)
        {
            keyPressed(GLFW.GLFW_KEY_DOWN, 0, 0);
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        boxes.forEach(o -> o.setFocused(false));
        nameBox.setFocused(false);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, 512, 256, 512, 256);
    }

    @Override
    public void onClose()
    {
        List<String> text = new ArrayList<>();
        boxes.forEach(b -> text.add(b.getValue()));
        PacketDistributor.sendToServer(new SBSetEditableMessagePayload(new EditableMessage(nameBox.getValue(), text)));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
