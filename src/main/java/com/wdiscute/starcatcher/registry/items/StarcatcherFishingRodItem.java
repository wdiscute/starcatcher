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
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class StarcatcherFishingRodItem extends Item
{
    public StarcatcherFishingRodItem()
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

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        //if rod has no hook & bobber
        if (SCDataComponents.getOrDefault(stack, SCDataComponents.HOOK, MaybeStack.EMPTY).toStack().isEmpty()
            || SCDataComponents.getOrDefault(stack, SCDataComponents.BOBBER, MaybeStack.EMPTY).toStack().isEmpty())
        {
            player.displayClientMessage(Component.translatable("gui.starcatcher.no_hook_or_bobber"), true);
            return InteractionResultHolder.fail(stack);
        }

        //end client side pipeline
        if (level.isClientSide) return InteractionResultHolder.success(stack);

        //get attachment
        FishingBobAttachment fishingBobAttachment = SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB.get());

        //get tackle skin
        AbstractTackleSkin tackleSkin = SCDataComponents.getOrDefault(stack, SCDataComponents.TACKLE_SKIN, Starcatcher.TACKLE_SKIN_REGISTRY.get(Starcatcher.BASE));

        //if player is not fishing, cast
        if (fishingBobAttachment.isEmpty())
        {
            ItemStack bait = SCDataComponents.getOrDefault(stack, SCDataComponents.BAIT, MaybeStack.EMPTY).toStack();

            //display bait count messages
            if (bait.isEmpty())
                player.displayClientMessage(Component.translatable("gui.starcatcher.bait_out"), true);
            else if (bait.getCount() < 5)
                player.displayClientMessage(Component.translatable("gui.starcatcher.bait_running_low"), true);

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
            Entity maybeEntity = ((ServerLevel) level).getEntity(fishingBobAttachment.getUuid());

            //if fish is not biting
            if (maybeEntity instanceof FishingBobEntity fbe && !fbe.checkBiting())
            {
                //play retrieve sound
                tackleSkin.onRetrieve(player);

                //kill bobber
                fbe.kill();

                //remove fishing bobber uuid data attachment
                SCDataAttachments.remove(player, SCDataAttachments.FISHING_BOB.get());
            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack)
    {
        return itemStack.copy();
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

