package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.registry.SignedGuide;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FishingSignedGuideScreen extends FishingGuideScreen
{
    SignedGuide signedGuide;

    public FishingSignedGuideScreen(SignedGuide signedGuide)
    {
        super();
        this.signedGuide = signedGuide;
        MAX_HELP_PAGES = 0;
    }

    @Override
    protected void init()
    {
        super.init();
        this.fishCaughtCounterMap = signedGuide.fishesCaught();
        menu = -1;
        editBox.setEditable(false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        editBox.setFocused(false);
    }

    @Override
    public void renderCoverText(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        Instant instant = Instant.ofEpochMilli(signedGuide.date());
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("hh:mm");
        String formatted = zdt.format(formatter);
        String formatted2 = zdt.format(formatter2);

        renderCenteredString(guiGraphics, font, Component.literal(signedGuide.signature()), uiX + 284, uiY + 102, 0x937d70, false);

        renderCenteredString(guiGraphics, font, Component.literal(formatted), uiX + 284, uiY + 118, 0x937d70, false);
        renderCenteredString(guiGraphics, font, Component.literal(formatted2), uiX + 284, uiY + 128, 0x937d70, false);

    }
}
