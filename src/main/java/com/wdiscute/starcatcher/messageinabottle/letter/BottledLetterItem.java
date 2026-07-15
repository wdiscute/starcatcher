package com.wdiscute.starcatcher.messageinabottle.letter;

import com.wdiscute.libtooltips.Tooltips;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class BottledLetterItem extends Item implements ProjectileItem
{
    public BottledLetterItem()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player)
    {
        player.displayClientMessage(Component.translatable("item.starcatcher.bottled_letter.dropped"), true);
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.GLASS_PLACE,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        if (!level.isClientSide)
        {
            BottledLetterEntity bottleEntity = new BottledLetterEntity(level, player);
            bottleEntity.setItem(itemstack);
            bottleEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(bottleEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction)
    {
        BottledLetterEntity bottleEntity = new BottledLetterEntity(level, pos.x(), pos.y(), pos.z());
        bottleEntity.setItem(stack);
        return bottleEntity;
    }
}
