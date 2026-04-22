package com.wdiscute.starcatcher.mixin;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(CompoundTag.class)
public class CompundTagDebugMixin {

    @Inject(method = "copy()Lnet/minecraft/nbt/CompoundTag;", at = @org.spongepowered.asm.mixin.injection.At("HEAD"))
    public void copy(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = (CompoundTag) (Object) this;
      //  System.out.println("copying tag:" + tag.toString());
    }

}
