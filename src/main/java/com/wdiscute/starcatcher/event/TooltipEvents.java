package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.catchmodifiers.SCCatchModifiers;
import com.wdiscute.starcatcher.registry.minigamemodifiers.SCMinigameModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT,  bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TooltipEvents
{

    static int cachedTimer = 0;
    static ItemStack cachedItem = ItemStack.EMPTY;
    static List<Component> cachedComps = List.of();
    static boolean cachedShift = false;

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event)
    {
        List<Component> comp = new ArrayList<>();
        ItemStack stack = event.getItemStack();
        boolean hasShiftDown = Screen.hasShiftDown();

        //cache check
        if (stack == cachedItem && cachedTimer > 0 && hasShiftDown == cachedShift)
        {
            cachedTimer--;
            if (!event.getToolTip().isEmpty())
                event.getToolTip().addAll(1, cachedComps);
            else
                event.getToolTip().addAll(cachedComps);
            return;
        }

        cachedTimer = 100;
        cachedShift = hasShiftDown;
        cachedItem = stack;

        //Netherite Upgrade
        if (SCDataComponents.has(stack, SCDataComponents.NETHERITE_UPGRADE))
        {
            if (Boolean.TRUE.equals(SCDataComponents.get(stack, SCDataComponents.NETHERITE_UPGRADE)))
            {
                comp.add(Component.translatable("tooltip.starcatcher.rod.netherite"));
            }
        }

        //tackle skin
        if (SCDataComponents.has(stack, SCDataComponents.TACKLE_SKIN))
        {
            ResourceLocation rl = SCDataComponents.getOrDefault(stack, SCDataComponents.TACKLE_SKIN, Starcatcher.rl("missingno"));
            comp.add(Component.translatable("tooltip.starcatcher.tackle").withStyle(ChatFormatting.GRAY));
            comp.add(Component.literal(" -").append(Component.translatable("tooltip.tackle." + rl.toLanguageKey()))
                    .withStyle(Style.EMPTY.withColor(SCColors.TOOLTIP_GRAY)));
        }

        //modifiers
        List<ResourceLocation> minigameModifiersRLs = SCMinigameModifiers.getMinigameModifiersRLs(stack);
        List<ResourceLocation> catchModifiersRLs = SCCatchModifiers.getCatchModifiersRLs(stack);
        if (!minigameModifiersRLs.isEmpty() || !catchModifiersRLs.isEmpty() && !stack.is(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
        {
            List<Component> modComp = new ArrayList<>();

            //add minigame modifiers
            Player entity = event.getEntity();
            if (entity != null)
            {
                minigameModifiersRLs.forEach(o ->
                {
                    if (entity.level().registryAccess().registryOrThrow(Starcatcher.CATCH_MODIFIERS).get(o) != null)
                    {
                        modComp.add(Component.literal(" -").append(Component.translatable("tooltip.modifier." + o.toLanguageKey()))
                                .withStyle(Style.EMPTY.withColor(SCColors.TOOLTIP_GRAY)));
                    }
                });

                //add catch modifiers
                catchModifiersRLs.forEach(o ->
                {
                    if (entity.level().registryAccess().registryOrThrow(Starcatcher.CATCH_MODIFIERS).get(o) != null)
                    {
                        modComp.add(Component.literal(" -").append(Component.translatable("tooltip.modifier." + o.toLanguageKey()))
                                .withStyle(Style.EMPTY.withColor(SCColors.TOOLTIP_GRAY)));
                    }
                });

                if (!modComp.isEmpty())
                    comp.add(Component.translatable("tooltip.starcatcher.modifiers").withStyle(ChatFormatting.GRAY));

                comp.addAll(modComp);
            }
        }

        //caught fish info
        if (SCDataComponents.has(stack, SCDataComponents.CAUGHT_FISH_INFO))
        {
            FishProperties.SizeAndWeight.Units units = SCConfig.UNIT.get();
            CaughtFishInfo sw = SCDataComponents.get(stack, SCDataComponents.CAUGHT_FISH_INFO);

            if (sw.golden())
            {
                MutableComponent element = Component.empty().append(Component.translatable("gui.guide.rarity.golden")).withStyle(Style.EMPTY.withColor(0x888888));
                if (hasShiftDown)
                    element.append(Component.literal(" (top 0%)").withStyle(Style.EMPTY.withColor(0x707070)));
                comp.add(element);
            }
            else
            {
                String size = units.getSizeAsString(sw.sizeInCentimeters());
                String weight = units.getWeightAsString(sw.weightInGrams());
                String percentile = " (top " + (int) sw.percentile() + "%)";

                MutableComponent element = Component.literal(size + " - " + weight).withStyle(Style.EMPTY.withColor(0x888888));
                if (hasShiftDown)
                    element.append(Component.literal(percentile).withStyle(Style.EMPTY.withColor(0x707070)));
                comp.add(element);
            }
        }

        cachedComps = comp;
        if (!event.getToolTip().isEmpty())
            event.getToolTip().addAll(1, comp);
        else
            event.getToolTip().addAll(comp);
    }
}
