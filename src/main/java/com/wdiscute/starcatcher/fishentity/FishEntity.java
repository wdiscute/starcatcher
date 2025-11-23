package com.wdiscute.starcatcher.fishentity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FishEntity extends AbstractFish
{
    public FishEntity(EntityType<? extends FishEntity> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.TROPICAL_FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return SoundEvents.TROPICAL_FISH_HURT;
    }

    @Override
    protected SoundEvent getFlopSound()
    {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0F);
    }

    public void setFish(ItemStack is)
    {
        setItemSlot(EquipmentSlot.CHEST, is);
    }

    @Override
    public ItemStack getBucketItemStack()
    {
        return getItemBySlot(EquipmentSlot.CHEST);
    }
}
