package com.wdiscute.starcatcher.blocks.tacklebox.boat;

import com.wdiscute.starcatcher.blocks.tacklebox.TackleBoxBlockItem;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.utils.MaybeStack;
import com.wdiscute.utils.Utils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TackleBoxBoatItem extends Item
{
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    Boat.Type type;

    public TackleBoxBoatItem(Item.Properties properties, Boat.Type type)
    {
        super(properties.stacksTo(1));
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitresult.getType() == HitResult.Type.MISS)
        {
            return InteractionResultHolder.pass(stack);
        }
        else
        {
            Vec3 vec3 = player.getViewVector(1.0F);
            List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
            if (!list.isEmpty())
            {
                Vec3 vec31 = player.getEyePosition();
                for (Entity entity : list)
                    if (entity.getBoundingBox().inflate(entity.getPickRadius()).contains(vec31))
                        return InteractionResultHolder.pass(stack);
            }

            if (hitresult.getType() == HitResult.Type.BLOCK)
            {
                TackleBoxBoatEntity boat = this.getBoat(level, hitresult, stack, player);
                boat.setVariant(this.type);

                //set color
                DyeColor color = SCDataComponents.getOrDefault(stack, SCDataComponents.TACKLE_BOX_COLOR, DyeColor.CYAN);
                boat.getEntityData().set(TackleBoxBoatEntity.BOX_COLOR, color.getId());

                //set fishes
                var items = SCDataComponents.getOrDefault(stack, SCDataComponents.TACKLE_BOX_ITEMS, List.of());
                for (Utils.Duo<Integer, MaybeStack> duo : items)
                    boat.container.items.put(duo.first(), duo.second().toStack());

                //set fishes
                var fishes = SCDataComponents.getOrDefault(stack, SCDataComponents.TACKLE_BOX_FISHES, List.of());
                boat.container.fishes.clear();
                for (MaybeStack fish : fishes)
                    boat.container.fishes.add(fish.toStack());


                boat.setYRot(player.getYRot());
                if (!level.noCollision(boat, boat.getBoundingBox()))
                {
                    return InteractionResultHolder.fail(stack);
                }
                else
                {
                    if (!level.isClientSide)
                    {
                        level.addFreshEntity(boat);
                        level.gameEvent(player, GameEvent.ENTITY_PLACE, hitresult.getLocation());
                        stack.consume(1, player);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
                }
            }
            else
            {
                return InteractionResultHolder.pass(stack);
            }
        }
    }

    private TackleBoxBoatEntity getBoat(Level level, HitResult hitResult, ItemStack stack, Player player)
    {
        Vec3 vec3 = hitResult.getLocation();
        TackleBoxBoatEntity boat = new TackleBoxBoatEntity(level, vec3.x, vec3.y, vec3.z);
        if (level instanceof ServerLevel serverlevel)
            EntityType.<Boat>createDefaultStackConfig(serverlevel, stack, player).accept(boat);

        return boat;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack)
    {
        return Optional.of(new TackleBoxBlockItem.TackleBoxTooltip(stack));
    }
}
