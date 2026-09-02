package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bobentity.FishingBobEntity;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.data.attachments.FishingBobAttachment;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class StarcatcherFishingRodItem extends Item
{
    public StarcatcherFishingRodItem(Properties p)
    {
        super(new Properties()
                .rarity(Rarity.EPIC)
                .fireResistant()
                .durability(128)
                .stacksTo(1)
                .component(SCDataComponents.BOBBER.get(), new MaybeStack(SCItems.BOBBER.get()))
                .component(SCDataComponents.BAIT.get(), MaybeStack.EMPTY)
                .component(SCDataComponents.HOOK.get(), new MaybeStack(SCItems.HOOK.get()))
        );
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        //if rod has no hook & bobber
        if (SCDataComponents.getOrDefault(stack, SCDataComponents.HOOK, MaybeStack.EMPTY).toStack().isEmpty()
            || SCDataComponents.getOrDefault(stack, SCDataComponents.BOBBER, MaybeStack.EMPTY).toStack().isEmpty())
        {
            player.sendOverlayMessage(Component.translatable("gui.starcatcher.no_hook_or_bobber"));
            return InteractionResult.PASS;
        }

        //end client side pipeline
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        //get attachment
        FishingBobAttachment fishingBobAttachment = SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB.get());

        //get tackle skin
        AbstractTackleSkin tackleSkin = SCDataComponents.getOrDefault(stack, SCDataComponents.TACKLE_SKIN, Starcatcher.TACKLE_SKIN_REGISTRY.getValue(Starcatcher.BASE));

        //if player is not fishing, cast
        if (fishingBobAttachment.isEmpty())
        {
            ItemStack bait = SCDataComponents.getOrDefault(stack, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack();

            //display bait count messages
            if (bait.isEmpty())
                player.sendOverlayMessage(Component.translatable("gui.starcatcher.bait_out"));
            else if (bait.getCount() < 5)
                player.sendOverlayMessage(Component.translatable("gui.starcatcher.bait_running_low"));

            //spawn bobber
            Entity entity = new FishingBobEntity(level, player, stack, tackleSkin);
            level.addFreshEntity(entity);
            entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(player.getX(), entity.getEyeY(), player.getZ()));

            //play cast sound
            tackleSkin.onCast(player);

            //set player <> bobber link
            fishingBobAttachment.setUuid(player, entity.getUUID());
            SCDataAttachments.set(entity, SCDataAttachments.TACKLE_SKIN.get(), Starcatcher.TACKLE_SKIN_REGISTRY.getKey(tackleSkin));
        }
        //if player is fishing
        else
        {
            Entity maybeEntity = level.getEntity(fishingBobAttachment.getUuid());

            //if fish is not biting
            if (maybeEntity instanceof FishingBobEntity fbe && !fbe.checkBiting())
            {
                //play retrieve sound
                tackleSkin.onRetrieve(player);

                //kill bobber
                fbe.kill((ServerLevel) level);

                //remove fishing bobber uuid data attachment
                SCDataAttachments.remove(player, SCDataAttachments.FISHING_BOB.get());
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStackTemplate getCraftingRemainder(ItemInstance instance)
    {
        return switch (instance)
        {
            case ItemStack stack -> new ItemStackTemplate(instance.typeHolder(), instance.count(), stack.getComponentsPatch());
            case ItemStackTemplate template -> template;
            default -> null;
        };
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new RodSlotTooltip(stack));
    }

    public record RodSlotTooltip(ItemStack rod) implements TooltipComponent
    {
    }
}

