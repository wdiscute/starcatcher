package com.wdiscute.starcatcher.tournament;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.network.tournament.SBStandTournamentNameChangePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.*;

public class StandScreen extends AbstractContainerScreen<StandMenu>
{
    public Tournament tournament;
    public static Map<UUID, String> gameProfilesCache;

    private EditBox nameEditBox;
    private boolean nameWasFocused;

    private static final Identifier BACKGROUND_OWNER = Starcatcher.rl("textures/gui/tournament/background_owner.png");
    private static final Identifier BACKGROUND = Starcatcher.rl("textures/gui/tournament/background.png");

    int uiX;
    int uiY;
    boolean isOwner = false;

    @Override
    protected void init()
    {
        super.init();
        tournament = menu.sbe.tournament;
        gameProfilesCache = menu.sbe.profiles;
        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;
        subInit();
    }

    protected void subInit()
    {
        nameEditBox = new EditBox(this.font, uiX + 53, uiY + 36, 210, 12, Component.translatable("container.repair"));
        nameEditBox.setCanLoseFocus(true);
        nameEditBox.setTextColor(SCColors.GUIDE_TEXT_DARK);
        nameEditBox.setBordered(false);
        nameEditBox.setMaxLength(20);
        nameEditBox.setValue("");
        nameEditBox.setTextShadow(false);
        nameEditBox.setEditable(false);
        addWidget(this.nameEditBox);
    }

    private void onFocusNameEditBox()
    {
        nameEditBox.setValue(tournament.name);
        tournament.name = "";
    }

    private void onUnfocusNameEditBox()
    {
        //send packet
        ClientPacketDistributor.sendToServer(new SBStandTournamentNameChangePayload(tournament.tournamentUUID, nameEditBox.getValue()));
        tournament.name = nameEditBox.getValue();
        nameEditBox.setValue("");
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        //render background
        if (isOwner)
            renderImage(guiGraphics, BACKGROUND_OWNER);
        else
            renderImage(guiGraphics, BACKGROUND);

        //update tournament in case menu received a new tournament from server
        if (tournament != menu.sbe.tournament) onTournamentReceived();
        if (tournament == null) return;
        if (gameProfilesCache == null) return;

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //handle Name editbox focusing
        if (nameWasFocused != nameEditBox.isFocused())
        {
            if (nameEditBox.isFocused())
                onFocusNameEditBox();
            else
                onUnfocusNameEditBox();
        }
        nameWasFocused = nameEditBox.isFocused();

        //render tournament name
        guiGraphics.text(this.font, tournament.name, uiX + 53, uiY + 36, SCColors.GUIDE_TEXT_DARK, false);
        nameEditBox.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        //organizer
        guiGraphics.text(this.font, getPlayerFromUUID(tournament.owner), uiX + 55, uiY + 56, SCColors.GUIDE_TEXT_DARK, false);
        guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.organizer"), uiX + 55, uiY + 68, 0xff9c897c, false);

        //status
        guiGraphics.text(this.font, Component.translatable(tournament.status.getSerializedName()), uiX + 130, uiY + 56, SCColors.GUIDE_TEXT_DARK, false);
        guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.status"), uiX + 130, uiY + 68, 0xff9c897c, false);

        //duration
        guiGraphics.text(this.font, U.calculateRealLifeTimeFromTicks(tournament.settings.durationInTicks), uiX + 55, uiY + 88, SCColors.GUIDE_TEXT_DARK, false);
        if (isOwner)
            guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.duration"), uiX + 60, uiY + 100, 0xff9c897c, false);
        else
            guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.duration"), uiX + 56, uiY + 100, 0xff9c897c, false);
        //duration hover
        if (x > 52 && x < 116 && y > 85 && y < 98)
        {
            List<Component> durationTooltip = new ArrayList<>();

            durationTooltip.add(Component.literal(tournament.settings.durationInTicks + " ticks"));

            MutableComponent durationComp = Component.literal(String.format("%.2f ", (float) tournament.settings.durationInTicks / 24000));
            if (tournament.settings.durationInTicks % 24000 == 0)
                durationComp = Component.literal(tournament.settings.durationInTicks / 24000 + " ");
            durationTooltip.add(durationComp.append(Component.translatable("gui.starcatcher.tournament.duration.days")));

            renderTooltip(guiGraphics, durationTooltip, mouseX, mouseY);
        }


        //scoring
        guiGraphics.text(this.font, Component.translatable(tournament.settings.scoring.getSerializedName()), uiX + 130, uiY + 88, SCColors.GUIDE_TEXT_DARK, false);
        if (isOwner)
            guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.scoring"), uiX + 134, uiY + 100, 0xff9c897c, false);
        else
            guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.scoring"), uiX + 130, uiY + 100, 0xff9c897c, false);

        //duration hover
        if (x > 129 && x < 190 && y > 85 && y < 98)
        {
            List<Component> durationTooltip = new ArrayList<>();
            durationTooltip.add(Component.translatable("gui.starcatcher.tournament.scoring.later"));

            renderTooltip(guiGraphics, durationTooltip,  mouseX, mouseY);
        }


        //signup button
        if (tournament.playerScores.stream().anyMatch(t -> t.playerUUID.equals(Minecraft.getInstance().player.getUUID())))
        {
            guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.signed_up"), uiX + 65, uiY + 116, 0xff9c897c, false);
        } else
        {
            int color = tournament.settings.canSignUp(minecraft.player) ? SCColors.GUIDE_GREEN : SCColors.GUIDE_RED;
            guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.sign_up"), uiX + 65, uiY + 116, color, false);
        }

        //list of players
        boolean drawOthers = false;
        List<Component> others = new ArrayList<>();
        others.add(Component.translatable("gui.starcatcher.tournament.other"));

        for (int i = 0; i < tournament.playerScores.size(); i++)
        {
            if (i < 5)
            {
                if (i == 0)
                    guiGraphics.text(this.font, getPlayerFromUUID(tournament.playerScores.get(i).playerUUID), uiX + 77, uiY + 128, SCColors.GUIDE_TEXT_DARK, false);
                else
                    guiGraphics.text(this.font, getPlayerFromUUID(tournament.playerScores.get(i).playerUUID), uiX + 65, uiY + 140 + (i - 1) * 12, SCColors.GUIDE_TEXT_DARK, false);
            } else
            {
                drawOthers = true;
                others.add(Component.literal(getPlayerFromUUID(tournament.playerScores.get(i).playerUUID)));
            }
        }

        //[others] hover
        if (drawOthers)
        {
            guiGraphics.text(this.font, Component.translatable("gui.guide.hover"), uiX + 65, uiY + 140 + 4 * 12, SCColors.GUIDE_TEXT_DARK, false);
            if (x > 62 && x < 180 && y > 186 && y < 197)
                renderTooltip(
                        guiGraphics,
                        others,
                        mouseX, mouseY);
        }


        //right page
        if (isOwner)
        {
            //[Start Tournament]
            if (tournament.status.equals(Tournament.Status.SETUP))
            {
                guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.start"), uiX + 236, uiY + 188, SCColors.GUIDE_TEXT_DARK, false);
                if (x > 226 && x < 340 && y > 183 && y < 200)
                {
                    renderTooltip(guiGraphics, Component.translatable("gui.starcatcher.tournament.undone"), mouseX, mouseY);
                }
            }

            //[Cancel Tournament]
            if (tournament.status.equals(Tournament.Status.ACTIVE))
            {
                if (x > 226 && x < 340 && y > 183 && y < 200)
                {
                    renderTooltip(guiGraphics, Component.translatable("gui.starcatcher.tournament.undone"), mouseX, mouseY);
                }
                guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.cancel"), uiX + 236, uiY + 188, SCColors.GUIDE_TEXT_DARK, false);
            }

            //[Cancel Tournament]
            if (tournament.status.isDone())
            {
                if (x > 226 && x < 340 && y > 183 && y < 200)
                {
                    renderTooltip(guiGraphics, Component.translatable("gui.starcatcher.tournament.undone"), mouseX, mouseY);
                }
                guiGraphics.text(this.font, Component.translatable("gui.starcatcher.tournament.new"), uiX + 236, uiY + 188, SCColors.GUIDE_TEXT_DARK, false);
            }
        } else
        {
            guiGraphics.text(
                    this.font, Component.translatable("gui.starcatcher.tournament.waiting")
                            .append(Component.literal(" " + gameProfilesCache.get(tournament.owner) + "...")),
                    uiX + 236, uiY + 188, SCColors.GUIDE_TEXT_DARK, false);
        }


    }

    public void onTournamentReceived()
    {
        tournament = menu.sbe.tournament;
        gameProfilesCache = menu.sbe.profiles;
        isOwner = this.tournament.owner.equals(Minecraft.getInstance().player.getUUID());
        nameEditBox.setEditable(isOwner);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //duration decrease, shift does x10
        if (x > 53 && x < 117 && y > 88 && y < 107 && isOwner && scrollY < -0.5f)
        {
            if (!hasShiftDown())
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 101);
            else
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 102);
        }

        //duration increase, shift does x10
        if (x > 53 && x < 117 && y > 88 && y < 107 && isOwner && scrollY > 0.5f)
        {
            if (!hasShiftDown())
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 103);
            else
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 104);
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        double x = event.x() - uiX;
        double y = event.y() - uiY;
        assert minecraft != null;
        assert minecraft.gameMode != null;

        //System.out.println("clicked relative x: " + x);
        //System.out.println("clicked relative y: " + y);

        //sign up
        if (x > 62 && x < 180 && y > 115 && y < 124)
        {
            minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 67);
        }

        //start
        if (x > 226 && x < 340 && y > 183 && y < 200)
        {
            if (tournament.status.equals(Tournament.Status.SETUP))
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 68);

            if (tournament.status.equals(Tournament.Status.ACTIVE))
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 69);

            if (tournament.status.isDone())
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 53);
        }

        //duration decrease, shift does x10
        if (x > 50 && x < 60 && y > 98 && y < 107 && isOwner)
        {
            if (!hasShiftDown())
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 101);
            else
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 102);
        }

        //duration increase, shift does x10
        if (x > 109 && x < 119 && y > 99 && y < 109 && isOwner)
        {
            if (!hasShiftDown())
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 103);
            else
                minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 104);
        }

        //scoring previous
        if (x > 124 && x < 134 && y > 99 && y < 109)
        {
        }

        //scoring next
        if (x > 182 && x < 192 && y > 99 && y < 109)
        {
        }

        nameEditBox.setFocused(false);
        return super.mouseClicked(event, doubleClick);
    }


    public static String getPlayerFromUUID(UUID uuid)
    {
        if (gameProfilesCache != null)
        {
            if (gameProfilesCache.containsKey(uuid))
            {
                return gameProfilesCache.get(uuid);
            }
        }
        return "Unknown";
    }

    private void renderImage(GuiGraphicsExtractor guiGraphics, Identifier rl)
    {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, rl, uiX, uiY, 0, 0, 420, 260, 420, 260);
    }

    private void renderImage(GuiGraphicsExtractor guiGraphics, Identifier rl, int xOffset, int yOffset)
    {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, rl, uiX + xOffset, uiY + yOffset, 0, 0, 420, 260, 420, 260);
    }

    @Override
    public boolean keyPressed(KeyEvent event)
    {
        if (event.key() == 256)
        {
            this.minecraft.player.closeContainer();
        }

        boolean editbox = this.nameEditBox.keyPressed(event) || this.nameEditBox.canConsumeInput();
        return editbox || super.keyPressed(event);
    }

    @Override
    public void onClose()
    {
        super.onClose();
        if (!nameEditBox.getValue().isEmpty())
            ClientPacketDistributor.sendToServer(new SBStandTournamentNameChangePayload(tournament.tournamentUUID, nameEditBox.getValue()));
    }

    public StandScreen(StandMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title, 420, 260);
        tournament = menu.sbe.tournament;
    }

    public static void renderTooltip(GuiGraphicsExtractor guiGraphics, List<Component> components, int mouseX, int mouseY)
    {
        var clientTooltipComponents = components.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList();

        Identifier identifier = ItemStack.EMPTY.get(DataComponents.TOOLTIP_STYLE);

        guiGraphics.tooltip(Minecraft.getInstance().font,
                clientTooltipComponents,
                mouseX,
                mouseY,
                DefaultTooltipPositioner.INSTANCE,
                identifier
        );
    }

    public static void renderTooltip(GuiGraphicsExtractor guiGraphics, Component component, int mouseX, int mouseY)
    {
        renderTooltip(guiGraphics, List.of(component), mouseX, mouseY);
    }

    public static boolean hasShiftDown() {
        return Minecraft.getInstance().hasShiftDown();
    }
}
