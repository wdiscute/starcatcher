package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.tackleskin.BaseTackleSkin;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentOverlay;
import com.wdiscute.starcatcher.tournament.TournamentScoreSettings;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SettingsScreen extends FishingMinigameScreen
{
    SizeAndWeight.Units unitSelected;

    Tournament tournamentCached = null;

    List<Button> buttons = new ArrayList<>();

    public SettingsScreen()
    {
        super(FishProperties.empty().withFish(new MaybeStack(SCItems.AURORA)), SCItems.UNKNOWN_FISH.toStack(), List.of(), new BaseTackleSkin());
    }

    @Override
    protected void init()
    {
        super.init();

        hitDelay = (SCConfig.HIT_DELAY.get().floatValue());
        unitSelected = SCConfig.UNIT.get();

        renderBlur = true;

        tournamentCached = TournamentOverlay.tournament;
        TournamentOverlay.onTournamentReceived(new Tournament(
                UUID.randomUUID(),
                "example",
                Tournament.Status.ACTIVE,
                UUID.randomUUID(),
                "wd",
                List.of(
                        new Tournament.PlayerScore(UUID.randomUUID(), "wd", 10),
                        new Tournament.PlayerScore(UUID.randomUUID(), "nikdo", 67),
                        new Tournament.PlayerScore(UUID.randomUUID(), "day", 420),
                        new Tournament.PlayerScore(Minecraft.getInstance().player.getUUID(), "you", 2)
                ),
                TournamentScoreSettings.empty(),
                10,
                10
        ));


        buttons.add(new Button(SCConfig.MINIGAME_X_OFFSET, 70, -110, "x offset: ", 1));
        buttons.add(new Button(SCConfig.MINIGAME_Y_OFFSET, 70, -95, "y offset: ", 1));
        buttons.add(new Button(SCConfig.MINIGAME_RENDER_SCALE, 70, -80, "scale: ", 0.01f));

        buttons.add(new Button(SCConfig.TOURNAMENT_X_OFFSET, 70, -50, "x offset: ", 1));
        buttons.add(new Button(SCConfig.TOURNAMENT_Y_OFFSET, 70, -35, "y offset: ", 1));
        buttons.add(new Button(SCConfig.TOURNAMENT_SCALE, 70, -20, "scale: ", 0.01f));

        buttons.add(new Button(SCConfig.RADAR_X_OFFSET, 70, 10, "x offset: ", 1));
        buttons.add(new Button(SCConfig.RADAR_Y_OFFSET, 70, 25, "y offset: ", 1));
        buttons.add(new Button(SCConfig.RADAR_SCALE, 70, 40, "scale: ", 0.01f));

        buttons.add(new Button(SCConfig.TRACKER_X_OFFSET, 70, 70, "x offset: ", 1));
        buttons.add(new Button(SCConfig.TRACKER_Y_OFFSET, 70, 85, "y offset: ", 1));
        buttons.add(new Button(SCConfig.TRACKER_SCALE, 70, 100, "scale: ", 0.01f));

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        buttons.forEach(o -> o.render(guiGraphics, width, height, font));

        guiGraphics.drawString(font, "Minigame: ", width / 2 + 70, height / 2 - 120, 0xffffff00);
        guiGraphics.drawString(font, "Tournament: ", width / 2 + 70, height / 2 - 60, 0xffffff00);
        guiGraphics.drawString(font, "Radar: ", width / 2 + 70, height / 2, 0xffffff00);
        guiGraphics.drawString(font, "Tracker: ", width / 2 + 70, height / 2 + 60, 0xffffff00);
    }

    public record Button(ModConfigSpec.DoubleValue configSpec, int x, int y, String text, float increase)
    {
        public void render(GuiGraphics guiGraphics, int width, int height, Font font)
        {
            guiGraphics.fill(width / 2 + x, height / 2 + y, width / 2 + x + 100, height / 2 + y + 15, 0xff000000);
            guiGraphics.blit(FishingGuideScreen.ARROW_LEFT, width / 2 + x, height / 2 + y,
                    16, 16, 0, 0, 16, 16, 16, 16);
            guiGraphics.blit(FishingGuideScreen.ARROW_RIGHT, width / 2 + x + 7, height / 2 + y,
                    16, 16, 0, 0, 16, 16, 16, 16);

            guiGraphics.drawString(font, text + new DecimalFormat("#.##").format(configSpec.get()), width / 2 + x + 23, height / 2 + y + 2, 0xffffffff);
            guiGraphics.drawString(font, "X", width / 2 + x + 92, height / 2 + y + 2, 0xffff0000);

        }

        public void mouseClicked(double mouseX, double mouseY)
        {
            //left button
            if (mouseX > x && mouseX < x + 10 && mouseY > y && mouseY < y + 10)
            {
                if (hasShiftDown())
                    configSpec.set(configSpec.get() - increase * 10);
                else
                    configSpec.set(configSpec.get() - increase);
            }

            //right button
            if (mouseX > x + 10 && mouseX < x + 20 && mouseY > y && mouseY < y + 10)
            {
                if (hasShiftDown())
                    configSpec.set(configSpec.get() + increase * 10);
                else
                    configSpec.set(configSpec.get() + increase);
            }

            //X button
            if (mouseX > x + 92 && mouseX < x + 100 && mouseY > y && mouseY < y + 10)
            {
                configSpec.set(configSpec.getDefault());
            }
            configSpec.save();
        }

        public void mouseScrolled(double mouseX, double mouseY, double scroll)
        {
            //scroll
            if (mouseX > x && mouseX < x + 70 && mouseY > y && mouseY < y + 10)
            {
                if (hasShiftDown())
                    configSpec.set(configSpec.get() + (increase * 10) * scroll);
                else
                    configSpec.set(configSpec.get() + (increase) * scroll);
            }
            configSpec.save();
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        buttons.forEach(o -> o.mouseScrolled(mouseX - (double) width / 2, mouseY - (double) height / 2, scrollY));
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        buttons.forEach(o -> o.mouseClicked(mouseX - (double) width / 2, mouseY - (double) height / 2));

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isSettingsScreen()
    {
        return true;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (progress > 100) progress = 100;
        if (progress < 0) progress = 0;
    }

    @Override
    public void onClose()
    {
        modifiers.forEach(o -> o.onRemove(this));

        if (tournamentCached == null)
            TournamentOverlay.clear();
        else
            TournamentOverlay.onTournamentReceived(tournamentCached);

        this.minecraft.popGuiLayer();
    }
}
