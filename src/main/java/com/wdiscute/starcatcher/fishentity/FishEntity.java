package com.wdiscute.starcatcher.fishentity;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FishEntity extends AbstractFish
{
    public FishEntity(EntityType<? extends FishEntity> entityType, Level level)
    {
        super(entityType, level);
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
        return getBodyArmorItem();
    }

    @Override
    protected SoundEvent getFlopSound()
    {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0F);
    }

    @Override
    public void tick()
    {
        super.tick();
        if (getBodyArmorItem().isEmpty() && !level().isClientSide)
        {
            shouldDropItem = false;
            List<FishProperties> available = new ArrayList<>();

            for (FishProperties fp : level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            {
                if (fp.calculateChance(this, level(), SCItems.ROD.toStack(), AbstractFishRestriction.Context.FISH_ENTITY) > 0 && fp.catchInfo().fish().is(SCTags.BUCKETABLE_FISHES))
                    available.add(fp);
            }

            if (available.isEmpty())
                kill();
            else
            {
                FishProperties fp = available.get(U.r.nextInt(available.size()));
                ItemStack is = new ItemStack(fp.catchInfo().fish());
                setBodyArmorItem(is);
            }
        }
    }

    @Override
    protected void dropAllDeathLoot(ServerLevel p_level, DamageSource damageSource)
    {
        if (shouldDropItem)
            super.dropAllDeathLoot(p_level, damageSource);
    }

    public void setFish(ItemStack is)
    {
        setBodyArmorItem(is);
        shouldDropItem = true;
    }

    public ItemStack getFish()
    {
        return getBodyArmorItem();
    }

    @Override
    public ItemStack getBucketItemStack()
    {
        ItemStack is = new ItemStack(SCItems.STARCAUGHT_BUCKET.get());
        SCDataComponents.set(is, SCDataComponents.BUCKETED_FISH, new SingleStackContainer(getBodyArmorItem().copy()));
        return is;
    }

    public static boolean validSpawnPlacement(EntityType<FishEntity> entity, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource)
    {
        //todo fix this shit
        for (FishProperties fp : serverLevelAccessor.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY_KEY))
            if (fp.calculateChance(entity.create(serverLevelAccessor.getLevel()), serverLevelAccessor.getLevel(), ItemStack.EMPTY, AbstractFishRestriction.Context.FISH_ENTITY) > 0)
                return true;

        return false;
    }
}
