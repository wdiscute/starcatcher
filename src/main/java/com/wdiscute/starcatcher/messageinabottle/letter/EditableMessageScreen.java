package com.wdiscute.starcatcher.messageinabottle.letter;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.network.SBSetEditableMessagePayload;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditableMessageScreen extends Screen
{
    private final String sender;
    private final List<String> text = new ArrayList<>();
    private final List<EditBox> boxes = new ArrayList<>();
    private EditBox nameBox = new EditBox(Minecraft.getInstance().font, 0, 0, Component.empty());

    public static final ScreenUtils.Image BACKGROUND = new ScreenUtils.Image(Starcatcher.rl("textures/gui/message/message.png"), 512, 256);

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
    public void resize(int width, int height)
    {
        List<String> s = new ArrayList<>();

        String name = this.nameBox.getValue();

        for (int i = 0; i < 15; i++)
            s.add(this.boxes.get(i).getValue());

        this.init(width, height);

        for (int i = 0; i < 15; i++)
            boxes.get(i).setValue(s.get(i));

        this.nameBox.setValue(name);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick)
    {
        super.extractRenderState(g, mouseX, mouseY, partialTick);
        BACKGROUND.render(g, uiX, uiY);
        boxes.forEach(b -> b.extractRenderState(g, mouseX, mouseY, partialTick));
        nameBox.extractRenderState(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(KeyEvent event)
    {
        InputConstants.Key key = InputConstants.getKey(event);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(key) && boxes.stream().noneMatch(EditBox::canConsumeInput) && !nameBox.canConsumeInput())
        {
            this.onClose();
            return true;
        }

        //if pressed enter, send arrow down to go to next line
        if (event.key() == GLFW.GLFW_KEY_ENTER)
        {
            keyPressed(new KeyEvent(GLFW.GLFW_KEY_DOWN, 0, 0));
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        boxes.forEach(o -> o.setFocused(false));
        nameBox.setFocused(false);
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void onClose()
    {
        List<String> text = new ArrayList<>();
        boxes.forEach(b -> text.add(b.getValue()));
        ClientPacketDistributor.sendToServer(new SBSetEditableMessagePayload(new EditableMessage(nameBox.getValue(), text)));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
