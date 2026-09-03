package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.fish.Rarity;
import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

public class StarcaughtBucket extends BucketItem
{
    EntityType<FishEntity> entity;

    public StarcaughtBucket(Properties p, Fluid fluid)
    {
        super(fluid, p.stacksTo(1));

        entity = SCEntities.FISH.get();
    }

    public static Item getBucketForStack(ItemStack stack)
    {
        return stack.getItem() instanceof BucketItem bucketItem && bucketItem.content.equals(Fluids.LAVA)
                ? SCItems.STARCAUGHT_LAVA_BUCKET.get() : SCItems.STARCAUGHT_BUCKET.get();
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
            fishEntity.setFish(getFish(bucketedMobStack));
        else
            fishEntity.setFish(SCItems.CERBERAY.toStack());
    }

    private static ItemStack getFish(ItemStack bucket)
    {
        return SCDataComponents.getOrDefault(bucket, SCDataComponents.BUCKETED_FISH, new MaybeStack(ItemStack.EMPTY)).toStack();
    }

    @Override
    public Component getName(ItemStack stack)
    {
        MaybeStack maybeStack = SCDataComponents.get(stack, SCDataComponents.BUCKETED_FISH);

        if (maybeStack == null)
            return super.getName(stack);
        else
        {
            Component baseName;
            Component customName = maybeStack.toStack().get(DataComponents.CUSTOM_NAME);
            Component itemName = maybeStack.toStack().get(DataComponents.ITEM_NAME);

            if (customName != null)
            {
                baseName = customName;
            }
            else if (itemName != null)
            {
                baseName = itemName;
            }
            else baseName = Component.translatable(maybeStack.toStack().getItem().getDescriptionId());

            CaughtFishInfo sw = SCDataComponents.get(maybeStack.toStack(), SCDataComponents.CAUGHT_FISH_INFO);
            if (sw != null)
            {
                Rarity rarity = sw.golden() ? Rarity.GOLDEN : sw.rarity();
                return Component.translatable("tooltip.starcatcher.starcaught_bucket.name", rarity.wrapWithRarityMarkdown(baseName.getString()));
            }
            else
                return Component.translatable("tooltip.starcatcher.starcaught_bucket.name", baseName.getString());
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new BucketTooltip(getFish(stack)));
    }

    public record BucketTooltip(ItemStack fish) implements TooltipComponent
    {
    }
}
