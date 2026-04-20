package com.wdiscute.starcatcher.registry.items.rod;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.bobberentity.FishingBobEntity;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.attachments.FishingBobAttachment;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class StarcatcherFishingRodItem extends Item
{
    public StarcatcherFishingRodItem()
    {
        super(new Properties()
                .rarity(Rarity.EPIC)
                .fireResistant()
                .stacksTo(1)
                .component(SCDataComponents.BOBBER.get(), new SingleStackContainer(new ItemStack(SCItems.BOBBER.get())))
                .component(SCDataComponents.BAIT.get(), SingleStackContainer.empty())
                .component(SCDataComponents.HOOK.get(), new SingleStackContainer(new ItemStack(SCItems.HOOK.get())))
        );
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack is = player.getItemInHand(hand);

        if (!is.is(SCTags.RODS))
            return InteractionResult.PASS;

        if (SCDataComponents.getOrDefault(is, SCDataComponents.HOOK, SingleStackContainer.empty()).stack().isEmpty()
                || SCDataComponents.getOrDefault(is, SCDataComponents.BOBBER, SingleStackContainer.empty()).stack().isEmpty())
        {
            player.sendOverlayMessage(Component.translatable("gui.starcatcher.no_hook_or_bobber"));
            return InteractionResult.FAIL;
        }


        if (level.isClientSide()) return InteractionResult.SUCCESS;


        FishingBobAttachment fishingBobAttachment = SCDataAttachments.get(player, SCDataAttachments.FISHING_BOB.get());
        if (fishingBobAttachment.isEmpty())
        {
            SCTackleSkins.get(player.level(), player.getItemInHand(hand)).onCast(player);

            if (level instanceof ServerLevel)
            {
                //TODO ADD CUSTOM STAT FOR NUMBER OF FISHES CAUGHT TOTAL ON STAT SCREEN

                Entity entity = new FishingBobEntity(level, player, is);
                level.addFreshEntity(entity);
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(player.getX(), entity.getEyeY(), player.getZ()));

                fishingBobAttachment.setUuid(player, entity.getUUID());
                if (SCDataComponents.has(is, SCDataComponents.TACKLE_SKIN))
                    SCDataAttachments.set(entity, SCDataAttachments.TACKLE_SKIN.get(), SCDataComponents.get(is, SCDataComponents.TACKLE_SKIN));
            }
        }
        else
        {

            List<Entity> entities = level.getEntities(null, new AABB(-25, -65, -25, 25, 65, 25).move(player.position()));

            for (Entity entity : entities)
            {
                if (entity.getUUID().equals(fishingBobAttachment.getUuid()))
                {
                    if (entity instanceof FishingBobEntity fbe && !fbe.checkBiting())
                    {
                        SCTackleSkins.get(player.level(), player.getItemInHand(hand)).onRetrieve(player);

                        fbe.kill(((ServerLevel) level));
                        SCDataAttachments.remove(player, SCDataAttachments.FISHING_BOB.get());
                        break;
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @org.jspecify.annotations.Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance)
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

