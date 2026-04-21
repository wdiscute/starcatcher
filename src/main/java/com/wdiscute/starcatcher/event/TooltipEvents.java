package com.wdiscute.starcatcher.event;

import com.wdiscute.starcatcher.SCColors;
import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.catchmodifiers.SCCatchModifiers;
import com.wdiscute.starcatcher.registry.minigamemodifiers.SCMinigameModifiers;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.starcatcher.secretnotes.LetterItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Starcatcher.MOD_ID, value = Dist.CLIENT)
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
        boolean hasShiftDown = event.getFlags().hasShiftDown();

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
        Identifier rl = SCTackleSkins.getTackleSkin(stack);
        if (!rl.equals(SCTackleSkins.BASE_TACKLE_SKIN))
        {
            comp.add(Component.translatable("tooltip.starcatcher.tackle").withStyle(ChatFormatting.GRAY));
            String s = I18n.get("tooltip.tackle." + rl.toLanguageKey());
            if (!s.isEmpty())
                comp.add(Component.literal(" -").append(Component.literal(s))
                        .withStyle(Style.EMPTY.withColor(SCColors.TOOLTIP_GRAY)));
        }

        //modifiers
        List<Identifier> minigameModifiersRLs = SCMinigameModifiers.getMinigameModifiersRLs(stack);
        List<Identifier> catchModifiersRLs = SCCatchModifiers.getCatchModifiersRLs(stack);
        if (!minigameModifiersRLs.isEmpty() || !catchModifiersRLs.isEmpty() && !stack.is(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
        {
            List<Component> modComp = new ArrayList<>();

            //add minigame modifiers
            Player entity = event.getEntity();
            if (entity != null)
            {
                minigameModifiersRLs.forEach(o ->
                {
                    if (entity.level().registryAccess().lookup(Starcatcher.CATCH_MODIFIERS).get().get(o).isPresent())
                    {
                        String s = I18n.get("tooltip.modifier." + o.toLanguageKey());
                        if (!s.isEmpty())
                            modComp.add(Component.literal(" -").append(Component.literal(s))
                                    .withStyle(Style.EMPTY.withColor(SCColors.TOOLTIP_GRAY)));
                    }
                });

                //add catch modifiers
                catchModifiersRLs.forEach(o ->
                {
                    if (entity.level().registryAccess().lookup(Starcatcher.CATCH_MODIFIERS).get().get(o).isPresent())
                    {
                        String s = I18n.get("tooltip.modifier." + o.toLanguageKey());
                        if (!s.isEmpty())
                            modComp.add(Component.literal(" -").append(Component.literal(s))
                                    .withStyle(Style.EMPTY.withColor(SCColors.TOOLTIP_GRAY)));
                    }
                });

                if (!modComp.isEmpty())
                    comp.add(Component.translatable("tooltip.starcatcher.modifiers").withStyle(ChatFormatting.GRAY));

                comp.addAll(modComp);
            }
        }

        //signed
        if (SCDataComponents.has(stack, SCDataComponents.SIGNED_GUIDE))
        {
            var sign = SCDataComponents.get(stack, SCDataComponents.SIGNED_GUIDE);

            if (event.getFlags().hasShiftDown())
                comp.add(Component.translatable("tooltip.starcatcher.starcatcher_guide.signed_shift", sign.owner().toString()).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
            else
                comp.add(Component.translatable("tooltip.starcatcher.starcatcher_guide.signed", sign.signature()).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
        }

        //bucket item creative
        ItemStack fish = SCDataComponents.getOrDefault(stack, SCDataComponents.BUCKETED_FISH, SingleStackContainer.empty()).create();
        if (stack.is(SCItems.STARCAUGHT_BUCKET) && fish.isEmpty())
        {
            comp.add(Component.translatable("tooltip.starcatcher.starcaught_bucket.creative.1").withColor(0x888888));
            comp.add(Component.translatable("tooltip.starcatcher.starcaught_bucket.creative.0").withColor(0x888888));
        }

        //letter
        if(SCDataComponents.has(stack, SCDataComponents.MESSAGE) && event.getFlags().isAdvanced())
        {
            LetterItem.Message wd = SCDataComponents.get(stack, SCDataComponents.MESSAGE);
            comp.add(Component.literal("written by uuid: " + wd.sender()).withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
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
