package com.wdiscute.starcatcher.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

public class ColorfulBobber extends Item
{
    public ColorfulBobber()
    {
        super(new Properties()
                .component(ModDataComponents.BOBBER_COLOR, BobberColor.DEFAULT).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        tooltipComponents.add(Component.translatable("tooltip.starcatcher.colorful_bobber.999").withColor(stack.get(ModDataComponents.BOBBER_COLOR).getColorAsInt()));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {

        if (player instanceof ServerPlayer sp)
        {
            BobberColor bobberColor = BobberColor.random();
            player.getItemInHand(usedHand).set(ModDataComponents.BOBBER_COLOR, bobberColor);
            sp.displayClientMessage(
                    Component.literal("Your bobber shines differently...")
                            .withColor(bobberColor.getColorAsInt()), true);
        }

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }


    public record BobberColor(
            float r,
            float g,
            float b
    )
    {
        public static final Codec<BobberColor> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.FLOAT.fieldOf("r").forGetter(BobberColor::r),
                        Codec.FLOAT.fieldOf("g").forGetter(BobberColor::g),
                        Codec.FLOAT.fieldOf("b").forGetter(BobberColor::b)
                ).apply(instance, BobberColor::new));

        public static final BobberColor DEFAULT = new BobberColor(0.5f, 0.8f, 0.5f);

        public static BobberColor random()
        {
            Random r = new Random();
            return new BobberColor(r.nextFloat(), r.nextFloat(), r.nextFloat());
        }

        public int getColorAsInt()
        {
            return FastColor.ARGB32.color(
                    255,
                    ((int) (this.r * 255)),
                    ((int) (this.g * 255)),
                    ((int) (this.b * 255)));
        }

    }

}
