package com.wdiscute.starcatcher.blocks.tacklebox.boat;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.BundleFullness;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record BoxColorItemProperty() implements RangeSelectItemModelProperty
{
    public static final MapCodec<BoxColorItemProperty> MAP_CODEC = MapCodec.unit(BoxColorItemProperty::new);

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed)
    {
        return switch (stack.get(SCDataComponents.TACKLE_BOX_COLOR))
        {
            case null -> 9;
            case WHITE -> 0;
            case ORANGE -> 1;
            case MAGENTA -> 2;
            case LIGHT_BLUE -> 3;
            case YELLOW -> 4;
            case LIME -> 5;
            case PINK -> 6;
            case GRAY -> 7;
            case LIGHT_GRAY -> 8;
            case CYAN -> 9;
            case PURPLE -> 10;
            case BLUE -> 11;
            case BROWN -> 12;
            case GREEN -> 13;
            case RED -> 14;
            default -> 15;
        };
    }

    @Override
    public MapCodec<BoxColorItemProperty> type()
    {
        return MAP_CODEC;
    }
}

