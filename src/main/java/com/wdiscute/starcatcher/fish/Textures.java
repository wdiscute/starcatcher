package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.utils.ScreenUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record Textures(
        ScreenUtils.Image tank,
        ScreenUtils.Image rod,
        ScreenUtils.Image handle,
        ScreenUtils.Image buttons,
        ScreenUtils.Image treasure,
        ScreenUtils.Image wheels
)
{
    public Textures withTank(ScreenUtils.Image tank)
    {
        return new Textures(tank, rod, handle, buttons, treasure, wheels);
    }

    public static final ScreenUtils.Image SURFACE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/tanks/surface.png"), 96, 112);
    public static final ScreenUtils.Image SKY = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/tanks/sky.png"), 96, 112);
    public static final ScreenUtils.Image LAVA_OVERWORLD = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/tanks/lava_overworld.png"), 96, 112);
    public static final ScreenUtils.Image NETHER = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/tanks/nether.png"), 96, 112);
    public static final ScreenUtils.Image CAVE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/tanks/cave.png"), 96, 112);
    public static final ScreenUtils.Image ICY = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/tanks/icy.png"), 96, 112);
    public static final ScreenUtils.Image DEEP_DARK = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/tanks/deep_dark.png"), 96, 112);
    public static final ScreenUtils.Image END = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/tanks/end.png"), 96, 112);
    public static final ScreenUtils.Image END_VOID = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/tanks/end_void.png"), 96, 112);

    public static final ScreenUtils.Image ROD = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/rod.png"), 112, 112);
    public static final ScreenUtils.Image HANDLE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/handle.png"), 32, 64);
    public static final ScreenUtils.Image BUTTONS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/buttons.png"), 112, 64);
    public static final ScreenUtils.Image TREASURE = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/treasure.png"), 96, 96);
    public static final ScreenUtils.Image WHEELS = new ScreenUtils.Image(Starcatcher.rl("textures/gui/minigame/wheels.png"), 160, 64);

    public static final Textures DEFAULT = new Textures(SURFACE, ROD, HANDLE, BUTTONS, TREASURE, WHEELS);

    public static final Codec<Textures> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ScreenUtils.Image.codecFixedSize(96, 112).fieldOf("tank").forGetter(Textures::tank),
                    ScreenUtils.Image.codecFixedSize(112, 112).fieldOf("rod").forGetter(Textures::rod),
                    ScreenUtils.Image.codecFixedSize(32, 64).fieldOf("handle").forGetter(Textures::handle),
                    ScreenUtils.Image.codecFixedSize(112, 64).fieldOf("buttons").forGetter(Textures::buttons),
                    ScreenUtils.Image.codecFixedSize(96, 96).fieldOf("treasure").forGetter(Textures::treasure),
                    ScreenUtils.Image.codecFixedSize(160, 64).fieldOf("wheels").forGetter(Textures::wheels)
            ).apply(instance, Textures::new));

    public static final StreamCodec<ByteBuf, Textures> STREAM_CODEC = StreamCodec.composite(
            ScreenUtils.Image.streamCodecFixedSize(96, 112), Textures::tank,
            ScreenUtils.Image.streamCodecFixedSize(112, 112), Textures::rod,
            ScreenUtils.Image.streamCodecFixedSize(32, 64), Textures::handle,
            ScreenUtils.Image.streamCodecFixedSize(112, 64), Textures::buttons,
            ScreenUtils.Image.streamCodecFixedSize(96, 96), Textures::treasure,
            ScreenUtils.Image.streamCodecFixedSize(160, 64), Textures::wheels,
            Textures::new
    );
}
