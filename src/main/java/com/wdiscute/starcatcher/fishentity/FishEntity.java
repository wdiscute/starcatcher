package com.wdiscute.starcatcher.fishentity;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FishEntity extends AbstractFish
{
    boolean hasWarned = false;
    public FishEntity(EntityType<? extends FishEntity> entityType, Level level)
    {
        super(entityType, level);
        if (fireImmune()) this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
    }

    @Override
    public boolean isPersistenceRequired()
    {
        return false;
    }

    private boolean shouldDropItem = true;

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
    public @Nullable ItemStack getPickResult()
    {
        return getItemBySlot(EquipmentSlot.CHEST);
    }

    @Override
    protected SoundEvent getFlopSound()
    {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    @Override
    public boolean fireImmune()
    {
        return getFish().getItem().isFireResistant();
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0F);
    }

    @Override
    public boolean isInWater()
    {
        return !fireImmune() ? super.isInWater() : isInLava();
    }

    @Override
    public void tick()
    {
        super.tick();
        if (getPickResult().isEmpty() && !level().isClientSide())
        {
            //shouldDropItem = false;
            List<FishProperties> available = new ArrayList<>();

            for (FishProperties fp : level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            {
                if (fp.calculateChance(this, level(), SCItems.ROD.toStack(), AbstractFishRestriction.Context.FISH_ENTITY) > 0 && fp.catchInfo().fish().toStack().is(SCTags.BUCKETABLE_FISHES))
                    available.add(fp);
            }

            if (available.isEmpty())
                kill();
            else
            {
                FishProperties fp = available.get(Utils.r.nextInt(available.size()));
                ItemStack is = fp.catchInfo().fish().toStack();
                setItemSlot(EquipmentSlot.CHEST, is);
            }
        }
    }

    @Override
    public float getScale()
    {
        return SCDataComponents.getOrDefault(
                getFish(), SCDataComponents.CAUGHT_FISH_INFO,
                CaughtFishInfo.AVERAGE
        ).getScale();
    }

    @Override
    protected void dropAllDeathLoot(DamageSource damageSource)
    {
        if (shouldDropItem)
            super.dropAllDeathLoot(damageSource);
    }

    public void setFish(ItemStack is)
    {
        setItemSlot(EquipmentSlot.CHEST, is);
        shouldDropItem = true;
    }

    public ItemStack getFish()
    {
        return getPickResult();
    }

    @Override
    public ItemStack getBucketItemStack()
    {
        ItemStack is = new ItemStack(fireImmune() ? SCItems.STARCAUGHT_LAVA_BUCKET.asItem() : SCItems.STARCAUGHT_BUCKET.asItem());
        SCDataComponents.set(is, SCDataComponents.BUCKETED_FISH, new MaybeStack(getPickResult().copy()));
        return is;
    }
}
