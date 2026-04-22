package com.wdiscute.starcatcher.secretnotes;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.network.SetMessagePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class MessageWriteScreen extends Screen
{
    private static final Identifier BACKGROUND = Starcatcher.rl("textures/gui/message/message.png");
    private final LetterItem.Message message;

    private final List<String> text = new ArrayList<>();
    private final List<EditBox> boxes = new ArrayList<>();
    private EditBox name = null;

    public MessageWriteScreen(LetterItem.Message message)
    {
        super(Component.empty());

        this.message = message;
        text.addAll(message.text());
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
            box.setTextColor(0xff635040);
            box.setBordered(false);
            box.setMaxLength(40);
            box.setTextShadow(false);
            box.setEditable(true);
            if (message.text().size() > i) box.setValue(message.text().get(i));
            addWidget(box);
            boxes.add(box);
        }

        //name
        name = new EditBox(this.font, uiX + 255, uiY + 208, 500, 12, Component.empty());
        name.setCanLoseFocus(true);
        name.setTextColor(0xff635040);
        name.setBordered(false);
        name.setMaxLength(17);
        name.setValue(message.senderDisplayName());
        name.setTextShadow(false);
        name.setEditable(true);
        addWidget(name);
    }

    @Override
    public void resize(int width, int height)
    {
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 15; i++) s.add(this.name.getValue());
        this.init(width, height);
        for (int i = 0; i < 15; i++) boxes.get(i).setValue(s.get(i));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
        renderImage(guiGraphics, BACKGROUND);
        boxes.forEach(b -> b.extractRenderState(guiGraphics, mouseX, mouseY, partialTick));
        if (name != null) name.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
    }


    @Override
    public boolean keyPressed(KeyEvent event)
    {
        InputConstants.Key key = InputConstants.getKey(event);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(key) && boxes.stream().noneMatch(EditBox::canConsumeInput) && !name.canConsumeInput())
        {
            this.onClose();
            return true;
        }
        return super.keyPressed(event);
    }


    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        boxes.forEach(o -> o.setFocused(false));
        name.setFocused(false);
        return super.mouseClicked(event, doubleClick);
    }

    private void renderImage(GuiGraphicsExtractor guiGraphics, Identifier rl)
    {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, rl, uiX, uiY, 0, 0, 512, 256, 512, 256);
    }

    @Override
    public void onClose()
    {
        List<String> list = new ArrayList<>();
        boxes.forEach(b -> list.add(b.getValue()));
        ClientPacketDistributor.sendToServer(new SetMessagePayload(list, name.getValue()));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
