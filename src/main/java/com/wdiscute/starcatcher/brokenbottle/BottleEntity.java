package com.wdiscute.starcatcher.brokenbottle;

import com.wdiscute.starcatcher.registry.ModEntities;
import com.wdiscute.starcatcher.registry.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BottleEntity extends ThrowableItemProjectile
{
    public BottleEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level)
    {
        super(entityType, level);
    }

    public BottleEntity(Level level, LivingEntity shooter) {
        super(ModEntities.BOTTLE.get(), shooter, level);
    }

    public BottleEntity(Level level, double x, double y, double z) {
        super(ModEntities.BOTTLE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem()
    {
        return ModItems.BROKEN_BOTTLE.get();
    }

    private ParticleOptions getParticle()
    {
        ItemStack itemstack = this.getItem();
        return new ItemParticleOption(ParticleTypes.ITEM, itemstack);
    }

    public void handleEntityEvent(byte id)
    {
        if (id == 3)
        {
            ParticleOptions particleoptions = this.getParticle();
            for (int i = 0; i < 8; ++i)
            {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), (double) 0.0F, (double) 0.0F, (double) 0.0F);
            }
        }

    }

    protected void onHitEntity(EntityHitResult result)
    {
        super.onHitEntity(result);
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
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float) 5);
    }

    protected void onHit(HitResult result)
    {
        super.onHit(result);
        if (!this.level().isClientSide)
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
