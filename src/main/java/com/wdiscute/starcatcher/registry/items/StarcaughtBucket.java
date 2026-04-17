package com.wdiscute.starcatcher.registry.items;

import com.wdiscute.starcatcher.fishentity.FishEntity;
import com.wdiscute.starcatcher.io.CaughtFishInfo;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.registry.FishProperties;
import com.wdiscute.starcatcher.registry.SCEntities;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.core.BlockPos;
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
import net.nikdo53.neobackports.io.components.DataComponents;
import net.nikdo53.neobackports.utils.TooltipContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class StarcaughtBucket extends BucketItem
{
    Supplier<EntityType<FishEntity>> entity;

    public StarcaughtBucket(Fluid fluid)
    {
        super(fluid, new Item.Properties().stacksTo(16));

        entity = SCEntities.FISH;
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
        FishEntity fishEntity = this.entity.get().spawn(serverLevel, bucketedMobStack, null, pos, MobSpawnType.BUCKET, true, false);
        if (SCDataComponents.has(bucketedMobStack, SCDataComponents.BUCKETED_FISH))
            fishEntity.setFish(getFish(bucketedMobStack));
        else
            fishEntity.setFish(SCItems.AURORA.toStack());
    }

    private static ItemStack getFish(ItemStack bucket)
    {
        return SCDataComponents.getOrDefault(bucket, SCDataComponents.BUCKETED_FISH, new SingleStackContainer(ItemStack.EMPTY)).stack();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        ItemStack fish = getFish(stack);
        if (fish.isEmpty())
        {
            tooltipComponents.add(1, Component.translatable("tooltip.starcatcher.starcaught_bucket.creative.1").withColor(0x888888));
            tooltipComponents.add(1, Component.translatable("tooltip.starcatcher.starcaught_bucket.creative.0").withColor(0x888888));
        }
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
            Component customName = ssc.stack().get(DataComponents.CUSTOM_NAME.get());
            Component itemName = ssc.stack().get(DataComponents.ITEM_NAME.get());

            if (customName != null)
            {
                baseName = customName;
            }
            else if (itemName != null)
            {
                baseName = itemName;
            }
            else baseName = Component.translatable(ssc.stack().getDescriptionId());

            CaughtFishInfo sw = SCDataComponents.get(ssc.stack(), SCDataComponents.CAUGHT_FISH_INFO);
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
        return Optional.of(new BucketTooltip(getFish(stack)));
    }

    public record BucketTooltip(ItemStack fish) implements TooltipComponent
    {
    }
}
