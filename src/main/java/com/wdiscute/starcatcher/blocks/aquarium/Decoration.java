package com.wdiscute.starcatcher.blocks.aquarium;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.registry.interaction.AbstractInteraction;
import com.wdiscute.starcatcher.registry.interaction.EmptyInteraction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public record Decoration(AbstractInteraction type, Vec3 offset, float scale, Vec3 randomOffset, float randomScale)
{
    public static final Decoration EMPTY = new com.wdiscute.starcatcher.blocks.aquarium.Decoration(new EmptyInteraction());

    public Decoration(AbstractInteraction type)
    {
        this(type, Vec3.ZERO, 1, Vec3.ZERO, 0);
    }

    public Decoration(AbstractInteraction type, Vec3 offset, Vec3 randomOffset)
    {
        this(type, offset, 1, randomOffset, 0);
    }

    public boolean place(AquariumBlockEntity be, ItemStack is, Player player)
    {
        return type.interact(this, be, is, player);
    }

    public record Placed(BlockState bs, AbstractInteraction type, Vec3 offset, float scale)
    {
        public Placed withBS(BlockState bs)
        {
            return new Placed(bs, type, offset, scale);
        }

        public static final Codec<Placed> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BlockState.CODEC.fieldOf("blockstate").forGetter(Placed::bs),
                        Starcatcher.AQUARIUM_INTERACTION.byNameCodec().fieldOf("interaction").forGetter(Placed::type),
                        Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(Placed::offset),
                        Codec.FLOAT.optionalFieldOf("scale", 0f).forGetter(Placed::scale)
                ).apply(instance, Placed::new)
        );
    }

    public static final Codec<com.wdiscute.starcatcher.blocks.aquarium.Decoration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Starcatcher.AQUARIUM_INTERACTION.byNameCodec().fieldOf("interaction").forGetter(com.wdiscute.starcatcher.blocks.aquarium.Decoration::type),
                    Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(com.wdiscute.starcatcher.blocks.aquarium.Decoration::offset),
                    Codec.FLOAT.optionalFieldOf("scale", 0f).forGetter(com.wdiscute.starcatcher.blocks.aquarium.Decoration::scale),
                    Vec3.CODEC.optionalFieldOf("random_offset", Vec3.ZERO).forGetter(com.wdiscute.starcatcher.blocks.aquarium.Decoration::randomOffset),
                    Codec.FLOAT.optionalFieldOf("random_scale", 0f).forGetter(com.wdiscute.starcatcher.blocks.aquarium.Decoration::randomScale)
            ).apply(instance, com.wdiscute.starcatcher.blocks.aquarium.Decoration::new)
    );
}
