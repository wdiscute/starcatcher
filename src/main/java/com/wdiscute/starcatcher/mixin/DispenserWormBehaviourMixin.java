package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.SCConfig;
import com.wdiscute.starcatcher.event.SCEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net.minecraft.core.dispenser.DispenseItemBehavior$9")
public abstract class DispenserWormBehaviourMixin
{

    @Inject(
            method = "execute(Lnet/minecraft/core/dispenser/BlockSource;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void afterBlockPosComputed(
            BlockSource source,
            ItemStack stack,
            CallbackInfoReturnable<ItemStack> cir
    )
    {
        Level level = source.level();
        BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        if (level.getBlockState(blockpos).getBlock() instanceof FarmBlock && SCConfig.ENABLE_BONE_MEAL_ON_FARMLAND_FOR_WORMS.get())
        {
            ItemStack is = SCEvents.getWorm(level.getRandom());

            Vec3 vec3 = Vec3.atLowerCornerWithOffset(blockpos, 0.5F, 1.01, 0.5F).offsetRandom(level.random, 0.7F);
            ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), is);
            level.addFreshEntity(itementity);

            level.playSound(null, blockpos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

            stack.shrink(1);

            ((OptionalDispenseItemBehavior)(Object)this).setSuccess(true);
            cir.setReturnValue(stack);
            cir.cancel();
        }
    }
}
