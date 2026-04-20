package com.wdiscute.starcatcher.secretnotes;

import com.wdiscute.starcatcher.io.MessagesSavedData;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BottledLetterEntity extends ThrowableItemProjectile
{
    public BottledLetterEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level)
    {
        super(entityType, level);
    }

    public BottledLetterEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(SCEntities.BOTTLED_LETTER.get(), shooter, level, stack);
    }

    public BottledLetterEntity(Level level, double x, double y, double z, ItemStack stack) {
        super(SCEntities.BOTTLED_LETTER.get(), x, y, z, level, stack);
    }

    @Override
    protected void onHitBlock(BlockHitResult result)
    {
        if(level().getFluidState(result.getBlockPos().above()).isEmpty())
        {
            //spawn item if hit block
            ItemEntity itemEntity = new ItemEntity(level(), position().x, position().y, position().z, getItem());
            level().addFreshEntity(itemEntity);
        }
        else
        {
            //add to messages pool if hit fluid
            if(getOwner() != null)
            {
                if(getOwner() instanceof ServerPlayer sp)
                {
                    sp.sendOverlayMessage(Component.translatable("item.starcatcher.bottled_letter.thrown"));
                    if(SCDataComponents.has(getItem(), SCDataComponents.MESSAGE))
                    {
                        MessagesSavedData.get(((ServerLevel) level())).addMessage(SCDataComponents.get(getItem(), SCDataComponents.MESSAGE));
                    }
                }
            }
        }
        super.onHitBlock(result);
    }

    @Override
    protected Item getDefaultItem()
    {
        return SCItems.BOTTLED_LETTER.get();
    }

    private ParticleOptions getParticle()
    {
        ItemStack itemstack = this.getItem();
        return new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(itemstack));
    }

    @Override
    public void handleEntityEvent(byte id)
    {
        if (id == 3)
        {
            ParticleOptions particleoptions = this.getParticle();
            for (int i = 0; i < 8; ++i)
            {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }

    }

    protected void onHitEntity(EntityHitResult result)
    {
        super.onHitEntity(result);
        //spawn item
        ItemEntity itemEntity = new ItemEntity(level(), position().x, position().y, position().z, getItem());
        level().addFreshEntity(itemEntity);

        Entity entity = result.getEntity();
        level().playSound(
                null,
                getX(),
                getY(),
                getZ(),
                SoundEvents.GLASS_BREAK,
                SoundSource.NEUTRAL,
                0.5F,
                ((float) (1 + level().getRandom().nextFloat() * 0.3 + 0.15))
        );
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float) 4);
    }

    protected void onHit(HitResult result)
    {
        super.onHit(result);
        if (!this.level().isClientSide())
        {
            level().playSound(
                    null,
                    getX(),
                    getY(),
                    getZ(),
                    SoundEvents.GLASS_BREAK,
                    SoundSource.NEUTRAL,
                    0.5F,
                    ((float) (1 + level().getRandom().nextFloat() * 0.3 + 0.15))
            );
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }

    }


}
