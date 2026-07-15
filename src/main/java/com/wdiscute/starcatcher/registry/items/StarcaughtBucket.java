package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.libtooltips.Tooltips;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class StarcaughtBucket extends BucketItem implements Tooltips.ItemTooltip
{
    EntityType<FishEntity> entity;

    public StarcaughtBucket(Fluid fluid)
    {
        super(fluid, new Item.Properties().stacksTo(1));

        entity = SCEntities.FISH.get();
    }

    public static Item getBucketForStack(ItemStack stack)
    {
        return stack.has(DataComponents.FIRE_RESISTANT) ? SCItems.STARCAUGHT_LAVA_BUCKET.get() : SCItems.STARCAUGHT_BUCKET.get();
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level level, ItemStack containerStack, BlockPos pos)
    {
        if (level instanceof ServerLevel)
        {
            this.spawn((ServerLevel) level, containerStack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    private void spawn(ServerLevel serverLevel, ItemStack bucketedMobStack, BlockPos pos)
    {
        FishEntity fishEntity = this.entity.spawn(serverLevel, bucketedMobStack, null, pos, MobSpawnType.BUCKET, true, false);
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
            else baseName = Component.translatable(maybeStack.stack().getDescriptionId());

            CaughtFishInfo sw = SCDataComponents.get(maybeStack.stack(), SCDataComponents.CAUGHT_FISH_INFO);
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

    @Override
    public List<String> getAlwaysTooltips(ItemStack stack)
    {
        if (SCDataComponents.has(stack, SCDataComponents.BUCKETED_FISH))
            return List.of();
        else
            return List.of("tooltip.starcatcher.starcaught_bucket.creative.0", "tooltip.starcatcher.starcaught_bucket.creative.1");
    }

    @Override
    public List<String> getShiftTooltips(ItemStack stack)
    {
        return List.of();
    }

    public record BucketTooltip(ItemStack fish) implements TooltipComponent
    {
    }
}
