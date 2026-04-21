package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public class StarcaughtBucket extends BucketItem
{
    EntityType<FishEntity> entity;

    public StarcaughtBucket(Fluid fluid, Properties properties)
    {
        super(fluid, properties.stacksTo(16));

        entity = SCEntities.FISH.get();
    }

    @Override
    public void checkExtraContent(@org.jspecify.annotations.Nullable LivingEntity user, Level level, ItemStack itemStack, BlockPos pos)
    {
        if (level instanceof ServerLevel)
        {
            this.spawn((ServerLevel) level, itemStack, pos);
            level.gameEvent(user, GameEvent.ENTITY_PLACE, pos);
        }
    }

    private void spawn(ServerLevel serverLevel, ItemStack bucketedMobStack, BlockPos pos)
    {
        FishEntity fishEntity = this.entity.spawn(serverLevel, bucketedMobStack, null, pos, EntitySpawnReason.BUCKET, true, false);
        if (SCDataComponents.has(bucketedMobStack, SCDataComponents.BUCKETED_FISH))
            fishEntity.setFish(SCDataComponents.getOrDefault(bucketedMobStack, SCDataComponents.BUCKETED_FISH, SingleStackContainer.empty()).create());
        else
            fishEntity.setFish(SCItems.AURORA.toStack());
    }

    @Override
    public Component getName(ItemStack stack)
    {
        SingleStackContainer ssc = SCDataComponents.get(stack, SCDataComponents.BUCKETED_FISH);

        if (ssc == null)
            return super.getName(stack);
        else
        {
            Component baseName;
            Component customName = ssc.create().get(DataComponents.CUSTOM_NAME);
            Component itemName = ssc.create().get(DataComponents.ITEM_NAME);

            if (customName != null)
            {
                baseName = customName;
            }
            else if (itemName != null)
            {
                baseName = itemName;
            }
            else baseName = Component.translatable(ssc.create().getItem().getDescriptionId());

            CaughtFishInfo sw = SCDataComponents.get(ssc.create(), SCDataComponents.CAUGHT_FISH_INFO);
            if (sw != null)
            {
                FishProperties.Rarity rarity = sw.golden() ? FishProperties.Rarity.GOLDEN : sw.rarity();
                return Component.translatable("tooltip.starcatcher.starcaught_bucket.name", rarity.wrapWithRarityMarkdown(baseName.getString()));
            }
            else
                return Component.translatable("tooltip.starcatcher.starcaught_bucket.name", baseName.getString());
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new BucketTooltip(SCDataComponents.getOrDefault(stack, SCDataComponents.BUCKETED_FISH, SingleStackContainer.empty()).create()));
    }

    public record BucketTooltip(ItemStack fish) implements TooltipComponent
    {
    }
}
