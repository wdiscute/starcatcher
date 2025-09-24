package com.wdiscute.starcatcher.guide;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.networkandstuff.FishCaughtCounter;
import com.wdiscute.starcatcher.networkandstuff.FishProperties;
import com.wdiscute.starcatcher.networkandstuff.ModDataAttachments;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FishingGuideScreen extends Screen
{

    private static final ResourceLocation BACKGROUND = Starcatcher.rl("textures/gui/guide/background.png");

    private static final ResourceLocation ARROW_PREVIOUS = Starcatcher.rl("textures/gui/guide/arrow_previous.png");
    private static final ResourceLocation ARROW_NEXT = Starcatcher.rl("textures/gui/guide/arrow_next.png");

    int uiX;
    int uiY;

    int imageWidth;
    int imageHeight;

    int currentPage;

    ClientLevel level;
    LocalPlayer player;

    List<FishProperties> entries = new ArrayList<>(999);

    @Override
    protected void init()
    {
        super.init();

        entries = new ArrayList<>(999);

        imageWidth = 512;
        imageHeight = 256;

        uiX = (width - imageWidth) / 2;
        uiY = (height - imageHeight) / 2;

        ClientLevel level = Minecraft.getInstance().level;
        player = Minecraft.getInstance().player;

        for (FishProperties fp : FishProperties.getFPs(level)) if (fp.hasGuideEntry()) entries.add(fp);

    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(key))
        {
            this.onClose();
            return true;
        }

        if (this.minecraft.options.keyDrop.isActiveAndMatches(key))
        {
            Minecraft.getInstance().player.setData(
                    ModDataAttachments.FISH_SPOTTER,
                    entries.get(currentPage));

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //previous arrow
        if (x > 68 && x < 105 && y > 230 && y < 240)
        {
            if (currentPage != 0)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                currentPage--;
            }
        }


        //next arrow
        if (x > 420 && x < 440 && y > 230 && y < 240)
        {
            if (currentPage <= entries.size() / 2)
            {
                minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                currentPage++;
            }
        }

        if (currentPage == 0)
        {
            for (int i = 0; i < entries.size(); i++)
            {
                int offsetX = (i % 7) * 24 + 72;
                int offsetY = i / 7 * 24 + 40;

                if (mouseX > uiX + offsetX - 3 && mouseX < uiX + offsetX + 21 - 3 && mouseY > uiY + offsetY - 3 && mouseY < uiY + offsetY + 21 - 3)
                {
                    minecraft.player.playSound(SoundEvents.BOOK_PAGE_TURN);
                    currentPage = i / 2 + 1;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean shouldNarrateNavigation()
    {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderImage(guiGraphics, BACKGROUND);

        if (currentPage > 0)
        {
            renderEntry(guiGraphics, mouseX, mouseY, 70, currentPage * 2 - 2);

            renderEntry(guiGraphics, mouseX, mouseY, 276, currentPage * 2 - 1);
        }
        else
        {
            renderIndex(guiGraphics, mouseX, mouseY);
        }

        //render arrows above everything else
        if (currentPage != 0)
            guiGraphics.blit(ARROW_PREVIOUS, uiX + 65, uiY + 227, 0, 0, 23, 13, 23, 13);
        if (currentPage < entries.size() / 2 + 1)
            guiGraphics.blit(ARROW_NEXT, uiX + 420, uiY + 227, 0, 0, 23, 13, 23, 13);
    }


    private void renderIndex(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {

        for (int i = 0; i < entries.size(); i++)
        {
            List<FishCaughtCounter> fishCounterList = player.getData(ModDataAttachments.FISHES_CAUGHT);
            ItemStack is = new ItemStack(BuiltInRegistries.ITEM.get(entries.get(i).fish()));
            FishProperties fp = entries.get(i);

            int caught = 0;

            int offsetX = (i % 7) * 24 + 72;
            int offsetY = i / 7 * 24 + 40;

            for (FishCaughtCounter f : fishCounterList)
            {
                if (fp.equals(f.fp()))
                {
                    caught = f.count();
                    break;
                }

            }

            //outline
            guiGraphics.renderOutline(uiX + offsetX - 2, uiY + offsetY - 2, 20, 20, 0xff444444);

            for (FishProperties fpNotif : player.getData(ModDataAttachments.FISHES_NOTIFICATION))
            {
                if (fp.equals(fpNotif))
                    guiGraphics.renderOutline(uiX + offsetX - 1, uiY + offsetY - 1, 8, 7, 0xff444444);
            }

            guiGraphics.renderOutline(uiX + offsetX - 2, uiY + offsetY - 2, 20, 20, 0xff444444);

            if (caught != 0)
                renderItem(is, uiX + offsetX, uiY + offsetY, 1);
            else
                renderItem(new ItemStack(ModItems.MISSINGNO.get()), uiX + offsetX, uiY + offsetY, 1);

            //render tooltip when hovering
            if (mouseX > uiX + offsetX - 3 && mouseX < uiX + offsetX + 21 - 3 && mouseY > uiY + offsetY - 3 && mouseY < uiY + offsetY + 21 - 3)
            {
                List<Component> components = new ArrayList<>();

                if (caught == 0)
                {
                    components.add(Component.translatable("gui.guide.not_caught_fish_name"));
                    components.add(Component.translatable("gui.guide.not_caught").withColor(0xAA0000));
                }
                else
                {
                    if (fp.customName().isEmpty())
                        components.add(Component.translatable("item." + fp.fish().toLanguageKey()));
                    else
                        components.add(Component.translatable("item.starcatcher." + fp.customName()));

                    components.add(Component.translatable("gui.guide.caught").append(Component.literal("[" + caught + "]")).withColor(0x00AA00));
                }

                guiGraphics.renderTooltip(this.font, components, Optional.empty(), mouseX, mouseY);
            }


        }
    }


    private void renderEntry(GuiGraphics guiGraphics, int mouseX, int mouseY, int xOffset, int entry)
    {

        if (level == null) level = getMinecraft().level;

        double x = mouseX - uiX;
        double y = mouseY - uiY;

        if (entries.size() <= entry) return;

        ItemStack is = new ItemStack(BuiltInRegistries.ITEM.get(entries.get(entry).fish()));
        FishProperties fp = entries.get(entry);

        List<FishCaughtCounter> fishCaughtCounterList = player.getData(ModDataAttachments.FISHES_CAUGHT);

        //get fishCaughtCount
        int count = 0;
        for (FishCaughtCounter fcc : fishCaughtCounterList)
        {
            if (fp.equals(fcc.fp()))
            {
                count = fcc.count();
                break;
            }
        }


        if (count == 0)
        {
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.not_caught_fish_name"), uiX + xOffset + 46, uiY + 60, 0, false);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.not_caught").withColor(0xAA0000), uiX + xOffset + 46, uiY + 70, 0, false);
            renderItem(new ItemStack(ModItems.MISSINGNO.get()), uiX + xOffset + 10, uiY + 60);
        }
        else
        {

            //render fish name
            MutableComponent compName;
            if (fp.customName().isEmpty())
                compName = Component.translatable("item." + fp.fish().toLanguageKey());
            else
                compName = Component.translatable("item.starcatcher." + fp.customName());

            guiGraphics.drawString(this.font, compName, uiX + xOffset + 46, uiY + 60, 0, false);

            //render caught count
            Component c = Component.literal("[" + count + "]").withColor(0x00AA00);
            guiGraphics.drawString(this.font, Component.translatable("gui.guide.caught").append(c).withColor(0x00AA00), uiX + xOffset + 46, uiY + 70, 0, false);
            renderItem(is, uiX + xOffset + 10, uiY + 60);
        }


        //icon and count list
        {


        }

        int yOffset = 110;

        //planet
        {
            Component comp;

            if (fp.wr().dims().isEmpty())
            {
                comp = Component.translatable("gui.guide.no_restriction");
            }
            else
            {
                //if theres only one planet
                if (fp.wr().dims().size() == 1)
                {
                    comp = Component.translatable("dimension." + fp.wr().dims().getFirst().toLanguageKey());
                }
                else
                {
                    comp = Component.translatable("gui.guide.hover");

                    //show tooltip while hovering
                    if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
                    {
                        List<Component> c = new ArrayList<>();

                        for (int i = 0; i < fp.wr().dims().size(); i++)
                        {
                            c.add(Component.translatable("dimension." + fp.wr().dims().get(i).toLanguageKey()));
                        }
                        guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                    }
                }
            }

            if (fp.wr().dims().isEmpty())
            {
                comp = comp.copy().withColor(0x00AA00);
            }
            else
            {
                if (fp.wr().dims().contains(level.dimension().location()))
                {
                    comp = comp.copy().withColor(0x00AA00);
                }
                else
                {
                    comp = comp.copy().withColor(0xAA0000);
                }
            }


            Component start = Component.translatable("gui.guide.planet");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0, false);
        }

        //planet blacklist
        {
            if (!fp.wr().dimsBlacklist().isEmpty())
            {
                guiGraphics.drawString(this.font, Component.literal("[!]").withColor(0xAA0000), uiX + xOffset + 160, uiY + yOffset, 0, false);

                //show tooltip while hovering
                if (x > xOffset + 155 && x < xOffset + 175 && y > yOffset - 4 && y < yOffset + 12)
                {
                    List<Component> c = new ArrayList<>();

                    c.add(Component.translatable("gui.guide.blacklisted_dimensions"));

                    for (int i = 0; i < fp.wr().dimsBlacklist().size(); i++)
                    {
                        c.add(Component.literal(fp.wr().dimsBlacklist().get(i).toString()));
                    }
                    guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                }
            }
        }

        yOffset += 15;

        List<ResourceLocation> biomesBL = FishProperties.getBiomesBlacklistAsList(fp, level);
        List<ResourceLocation> biomes = FishProperties.getBiomesAsList(fp, level);
        //biome:
        {
            MutableComponent comp;
            if (biomes.isEmpty())
            {
                comp = Component.translatable("gui.guide.no_restriction");
                if (!biomesBL.isEmpty()) comp.append("*");
            }
            else
            {
                //if theres only one biome
                if (biomes.size() == 1)
                {
                    comp = Component.translatable("biome." + biomes.getFirst().toLanguageKey());
                }
                else if (fp.wr().biomesTags().size() == 1)
                {
                    comp = Component.translatable("tag." + fp.wr().biomesTags().getFirst().toLanguageKey());

                    //show tooltip while hovering
                    if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
                    {
                        List<Component> c = new ArrayList<>();
                        c.add(Component.translatable("gui.guide.biome"));

                        for (ResourceLocation rl : biomes)
                        {
                            c.add(Component.translatable("biome." + rl.toLanguageKey()));
                        }

                        guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                    }
                }
                else
                {
                    comp = Component.translatable("gui.guide.hover");

                    //show tooltip while hovering
                    if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
                    {
                        List<Component> c = new ArrayList<>();
                        c.add(Component.translatable("gui.guide.biome"));

                        for (ResourceLocation rl : biomes)
                        {
                            c.add(Component.translatable("biome." + rl.toLanguageKey()));
                        }

                        guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                    }
                }
            }

            if (biomes.isEmpty())
            {
                comp = comp.copy().withColor(0x00AA00);
            }
            else
            {
                ResourceLocation rl = ResourceLocation.parse(level.getBiome(Minecraft.getInstance().player.blockPosition()).getRegisteredName());
                if (biomes.contains(rl))
                {
                    comp = comp.copy().withColor(0x00AA00);
                }
                else
                {
                    comp = comp.copy().withColor(0xAA0000);
                }
            }


            Component start = Component.translatable("gui.guide.biome");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0, false);
        }

        //biome blacklist
        {
            if (!biomesBL.isEmpty())
            {
                guiGraphics.drawString(this.font, Component.literal("[!]").withColor(0xAA0000), uiX + xOffset + 160, uiY + yOffset, 0, false);

                //show tooltip while hovering
                if (x > xOffset + 155 && x < xOffset + 175 && y > yOffset - 4 && y < yOffset + 12)
                {
                    List<Component> c = new ArrayList<>();

                    c.add(Component.translatable("gui.guide.blacklisted_biomes"));

                    for (ResourceLocation rl : biomesBL)
                        c.add(Component.translatable("biome." + rl.toLanguageKey()));

                    guiGraphics.renderTooltip(this.font, c, Optional.empty(), mouseX, mouseY);
                }
            }
        }


        yOffset += 15;

        if (fp.br().correctBait().isEmpty())
        {
            guiGraphics.drawString(this.font, I18n.get("gui.guide.bait") + I18n.get("gui.guide.no_restriction"), uiX + xOffset, uiY + yOffset, 0, false);
        }
        else
        {
            ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(fp.br().correctBait().getFirst()));
            guiGraphics.drawString(this.font, I18n.get("gui.guide.bait") + I18n.get(stack.getDescriptionId()), uiX + xOffset, uiY + yOffset, 0, false);

            if (x > xOffset && x < xOffset + 100 && y > yOffset - 2 && y < yOffset + 10)
            {
                guiGraphics.renderTooltip(this.font, stack, mouseX, mouseY);
            }
        }

        yOffset += 15;


        //weather
        {
            Component comp;

            if (fp.weather() == FishProperties.Weather.ALL)
            {
                comp = Component.translatable("gui.guide.no_restriction").withColor(0x00AA00);
            }
            else
            {
                comp = Component.translatable("gui.guide.no_restriction");
                if (fp.weather() == FishProperties.Weather.RAIN)
                {
                    if (level.getRainLevel(0) > 0.5)
                        comp = Component.translatable("gui.guide.raining").withColor(0x00AA00);
                    else
                        comp = Component.translatable("gui.guide.raining").withColor(0xAA0000);
                }

                if (fp.weather() == FishProperties.Weather.THUNDER)
                {
                    if (level.getThunderLevel(0) > 0.5)
                        comp = Component.translatable("gui.guide.thundering").withColor(0x00AA00);
                    else
                        comp = Component.translatable("gui.guide.thundering").withColor(0xAA0000);
                }

                if (fp.weather() == FishProperties.Weather.CLEAR)
                {
                    if (level.getRainLevel(0) > 0.5 || level.getThunderLevel(0) > 0.5)
                        comp = Component.translatable("gui.guide.clear").withColor(0xAA0000);
                    else
                        comp = Component.translatable("gui.guide.clear").withColor(0x00AA00);
                }
            }

            Component start = Component.translatable("gui.guide.weather");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0, false);

        }


        yOffset += 15;


        //daytime
        {
            Component comp;

            if (fp.daytime() == FishProperties.Daytime.ALL)
            {
                comp = Component.translatable("gui.guide.no_restriction").withColor(0x00AA00);
            }
            else
            {
                long time = level.getDayTime() % 24000;

                comp = switch (fp.daytime())
                {
                    case FishProperties.Daytime.DAY:
                        if (!(time > 23000 || time < 12700))
                            yield Component.translatable("gui.guide.day").withColor(0xAA0000);
                        else
                            yield Component.translatable("gui.guide.day").withColor(0x00AA00);

                    case FishProperties.Daytime.NOON:
                        if (!(time > 3500 && time < 8500))
                            yield Component.translatable("gui.guide.noon").withColor(0xAA0000);
                        else
                            yield Component.translatable("gui.guide.noon").withColor(0x00AA00);

                    case FishProperties.Daytime.NIGHT:
                        if (!(time < 23000 && time > 12700))
                            yield Component.translatable("gui.guide.night").withColor(0xAA0000);
                        else
                            yield Component.translatable("gui.guide.night").withColor(0x00AA00);

                    case FishProperties.Daytime.MIDNIGHT:
                        if (!(time > 16500 && time < 19500))
                            yield Component.translatable("gui.guide.midnight").withColor(0xAA0000);
                        else
                            yield Component.translatable("gui.guide.midnight").withColor(0x00AA00);

                    default:
                        yield Component.empty();
                };


            }

            Component start = Component.translatable("gui.guide.daytime");

            guiGraphics.drawString(this.font, start.copy().append(comp), uiX + xOffset, uiY + yOffset, 0, false);

        }


        yOffset += 15;

        if (fp.mustBeCaughtAboveY() != Integer.MIN_VALUE)
        {
            guiGraphics.drawString(this.font, I18n.get("gui.guide.min_elevation") + fp.mustBeCaughtAboveY(), uiX + xOffset, uiY + yOffset, 0, false);
            yOffset += 15;
        }


        if (fp.mustBeCaughtBellowY() != Integer.MAX_VALUE)
        {
            guiGraphics.drawString(this.font, I18n.get("gui.guide.max_elevation") + fp.mustBeCaughtBellowY(), uiX + xOffset, uiY + yOffset, 0, false);
        }


    }


    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl)
    {
        guiGraphics.blit(rl, uiX, uiY, 0, 0, 512, 256, 512, 256);
    }

    private void renderImage(GuiGraphics guiGraphics, ResourceLocation rl, int yOffset)
    {
        guiGraphics.blit(rl, uiX, uiY + yOffset, 0, 0, 512, 256, 512, 256);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }


    private void renderItem(ItemStack stack, int x, int y)
    {
        renderItem(stack, x, y, 3);
    }

    private void renderItem(ItemStack stack, int x, int y, int scale)
    {

        Level level = Minecraft.getInstance().level;
        LivingEntity entity = Minecraft.getInstance().player;

        if (!stack.isEmpty())
        {
            BakedModel bakedmodel = this.minecraft.getItemRenderer().getModel(stack, level, entity, 234234);

            PoseStack pose = new PoseStack();

            pose.pushPose();
            pose.translate((float) (x + 8), (float) (y + 8), (float) (150));

            try
            {
                pose.scale(16F * scale, -16F * scale, 16F * scale);
                boolean flag = !bakedmodel.usesBlockLight();
                if (flag)
                {
                    Lighting.setupForFlatItems();
                }

                this.minecraft.getItemRenderer().render(stack, ItemDisplayContext.GUI, false, pose, Minecraft.getInstance().renderBuffers().bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);

                //flush()
                RenderSystem.disableDepthTest();
                Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
                RenderSystem.enableDepthTest();


                if (flag)
                {
                    Lighting.setupFor3DItems();
                }
            } catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
                crashreportcategory.setDetail("Item Components", () -> String.valueOf(stack.getComponents()));
                crashreportcategory.setDetail("Item Foil", () -> String.valueOf(stack.hasFoil()));
                throw new ReportedException(crashreport);
            }

            pose.popPose();
        }

    }


    public FishingGuideScreen()
    {
        super(Component.empty());
    }
}
