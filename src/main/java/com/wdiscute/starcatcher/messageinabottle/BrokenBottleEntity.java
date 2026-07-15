package com.wdiscute.starcatcher.messageinabottle;

import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BrokenBottleEntity extends ThrowableItemProjectile
{
    public BrokenBottleEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level)
    {
        super(entityType, level);
    }

    public BrokenBottleEntity(Level level, LivingEntity shooter) {
        super(SCEntities.BROKEN_BOTTLE.get(), shooter, level);
    }

    public BrokenBottleEntity(Level level, double x, double y, double z) {
        super(SCEntities.BROKEN_BOTTLE.get(), x, y, z, level);
    }

    @Override
    protected void onHitBlock(BlockHitResult result)
    {
        //spawn item
        ItemEntity itemEntity = new ItemEntity(level(), position().x, position().y, position().z, getItem());
        level().addFreshEntity(itemEntity);
        super.onHitBlock(result);
    }

    @Override
    protected Item getDefaultItem()
    {
        return SCItems.BROKEN_BOTTLE.get();
    }

    private ParticleOptions getParticle()
    {
        ItemStack itemstack = this.getItem();
        return new ItemParticleOption(ParticleTypes.ITEM, itemstack);
    }

    public void handleEntityEvent(byte id)
    {
        //no idea what id 3 is
        if (id == 3)
            for (int i = 0; i < 8; ++i)
                this.level().addParticle(this.getParticle(), this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);

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
