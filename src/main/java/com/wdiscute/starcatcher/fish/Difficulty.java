package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.ExtraComposites;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.modifiers.minigamemodifiers.*;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.SCSweetSpotsBehaviour;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Difficulty(
        int hp,
        int speed,
        int penalty,
        float decay,
        List<Modifier> modifiers,
        List<SweetSpot> sweetSpots
)
{
    public Difficulty(int hp, int speed, int penalty, float decay, List<Modifier> modifiers, SweetSpot... sweetSpots)
    {
        this(hp, speed, penalty, decay, modifiers, Arrays.stream(sweetSpots).toList());
    }

    public Difficulty addModifiers(List<Modifier> newModifier)
    {
        List<Modifier> list = new ArrayList<>();
        list.addAll(newModifier);
        list.addAll(this.modifiers);
        return new Difficulty(this.hp, this.speed, this.penalty, this.decay, list, this.sweetSpots);
    }

    public Difficulty addModifier(Modifier newModifier)
    {
        List<Modifier> list = new ArrayList<>();
        list.add(newModifier);
        list.addAll(this.modifiers);
        return new Difficulty(this.hp, this.speed, this.penalty, this.decay, list, this.sweetSpots);
    }

    public Difficulty withHP(int hp)
    {
        return new Difficulty(hp, speed, penalty, decay, modifiers, sweetSpots);
    }

    public Difficulty withPenalty(int penalty)
    {
        return new Difficulty(hp, speed, penalty, decay, modifiers, sweetSpots);
    }

    public Difficulty withDecay(int decay)
    {
        return new Difficulty(hp, speed, penalty, decay, modifiers, sweetSpots);
    }

    public Difficulty withSpeed(int speed)
    {
        return new Difficulty(hp, speed, penalty, decay, modifiers, sweetSpots);
    }

    public Difficulty vanishing()
    {
        List<SweetSpot> sss = new ArrayList<>();
        sweetSpots.forEach(s -> sss.add(s.vanishing(0.1f)));
        return new Difficulty(hp, speed, penalty, decay, modifiers, sss);
    }

    public Difficulty vanishing(float vanishingRate)
    {
        List<SweetSpot> sss = new ArrayList<>();
        sweetSpots.forEach(s -> sss.add(s.vanishing(vanishingRate)));
        return new Difficulty(hp, speed, penalty, decay, modifiers, sss);
    }

    public Difficulty moving(float movingRate)
    {
        List<SweetSpot> sss = new ArrayList<>();
        sweetSpots.forEach(s -> sss.add(s.moving(movingRate)));
        return new Difficulty(hp, speed, penalty, decay, modifiers, sss);
    }

    public Difficulty moving()
    {
        List<SweetSpot> sss = new ArrayList<>();
        sweetSpots.forEach(s -> sss.add(s.moving(1)));
        return new Difficulty(hp, speed, penalty, decay, modifiers, sss);
    }

    public Difficulty flip()
    {
        List<SweetSpot> sss = new ArrayList<>();
        sweetSpots.forEach(s -> sss.add(s.flip()));
        return new Difficulty(hp, speed, penalty, decay, modifiers, sss);
    }


    //
    //                                          ,--.
    // ,---.  ,--.--.  ,---.   ,---.   ,---.  ,-'  '-.
    //| .-. | |  .--' | .-. : (  .-'  | .-. : '-.  .-'
    //| '-' ' |  |    \   --. .-'  `) \   --.   |  |
    //|  |-'  `--'     `----' `----'   `----'   `--'
    //`--'
    //   ,--. ,--.  ,---.  ,---. ,--.                 ,--.   ,--.   ,--.
    // ,-|  | `--' /  .-' /  .-' `--'  ,---. ,--.,--. |  | ,-'  '-. `--'  ,---.   ,---.
    //' .-. | ,--. |  `-, |  `-, ,--. | .--' |  ||  | |  | '-.  .-' ,--. | .-. : (  .-'
    //\ `-' | |  | |  .-' |  .-' |  | \ `--. '  ''  ' |  |   |  |   |  | \   --. .-'  `)
    // `---'  `--' `--'   `--'   `--'  `---'  `----'  `--'   `--'   `--'  `----' `----'
    //

    public static Difficulty TRASH = new Difficulty(
            100, 10, 0, 0,
            List.of(),
            SweetSpot.TRASH, SweetSpot.TRASH
    );


    public static Difficulty EASY = new Difficulty(
            75, 7, 5, 1,
            List.of(),
            SweetSpot.NORMAL, SweetSpot.NORMAL
    );

    public static Difficulty EASY_STONE = new Difficulty(
            75, 7, 5, 1,
            List.of(),
            SweetSpot.NORMAL, SweetSpot.STONE_LOW_REWARD
    );

    public static Difficulty EASY_DEEP_DARK = new Difficulty(
            200, 7, 5, 1,
            List.of(new DeepDarkModifier("")),
            SweetSpot.SCULK, SweetSpot.SCULK, SweetSpot.GLOWING, SweetSpot.GLOWING
    );

    public static Difficulty EASY_DRIPSTONE = new Difficulty(
            75, 7, 5, 1,
            List.of(),
            SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.DRIPSTONE
    );

    public static Difficulty EASY_FROZEN = new Difficulty(
            75, 7, 5, 1,
            List.of(new FreezeOnMissModifier(40, 10, "")),
            SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.FROZEN
    );

    public static Difficulty EASY_MIRAGE = new Difficulty(
            75, 7, 5, 1,
            List.of(
                    new SpawnSweetSpotsModifier(140, 1, SweetSpot.MIRAGE_NORMAL, false, "")
            ),
            SweetSpot.NORMAL, SweetSpot.NORMAL
    );

    public static Difficulty EASY_MIRAGE_MOVING = new Difficulty(
            75, 7, 5, 1,
            List.of(
                    new SpawnSweetSpotsModifier(140, 1, SweetSpot.MIRAGE_NORMAL.moving(1), false, "")
            ),
            SweetSpot.NORMAL.moving(1), SweetSpot.NORMAL.moving(1)
    );

    public static Difficulty EASY_DEEPSLATE = new Difficulty(
            75, 7, 5, 1,
            List.of(),
            SweetSpot.NORMAL, SweetSpot.THIN, SweetSpot.DEEPSLATE
    );


    public static Difficulty MEDIUM = new Difficulty(
            100, 10, 20, 1,
            List.of(),
            SweetSpot.NORMAL, SweetSpot.THIN
    );

    public static Difficulty MEDIUM_DEEP_DARK = new Difficulty(
            300, 10, 15, 0.7f,
            List.of(new DeepDarkModifier("")),
            SweetSpot.SCULK, SweetSpot.SCULK, SweetSpot.GLOWING
    );

    public static Difficulty MEDIUM_STONE = new Difficulty(
            100, 10, 20, 1,
            List.of(),
            SweetSpot.STONE_LOW_REWARD, SweetSpot.THIN, SweetSpot.THIN
    );

    public static Difficulty MEDIUM_DRIPSTONE = new Difficulty(
            100, 10, 20, 1,
            List.of(),
            SweetSpot.NORMAL, SweetSpot.DRIPSTONE
    );

    public static Difficulty MEDIUM_FROZEN = new Difficulty(
            100, 10, 20, 1,
            List.of(new FreezeOnMissModifier(40, 10, "")),
            SweetSpot.NORMAL, SweetSpot.FROZEN
    );

    public static Difficulty MEDIUM_MIRAGE = new Difficulty(
            100, 10, 20, 1,
            List.of(
                    new SpawnSweetSpotsModifier(140, 1, SweetSpot.MIRAGE_NORMAL, false, ""),
                    new SpawnSweetSpotsModifier(100, 0.3f, SweetSpot.MIRAGE_NORMAL, false, "")
            ),
            SweetSpot.NORMAL
    );

    public static Difficulty MEDIUM_MIRAGE_MOVING = new Difficulty(
            100, 10, 20, 1,
            List.of(
                    new SpawnSweetSpotsModifier(140, 1, SweetSpot.MIRAGE_NORMAL.moving(1), false, ""),
                    new SpawnSweetSpotsModifier(100, 0.3f, SweetSpot.MIRAGE_NORMAL.moving(1), false, "")
            ),
            SweetSpot.NORMAL.moving(1)
    );

    public static Difficulty MEDIUM_DEEPSLATE = new Difficulty(
            100, 10, 20, 1,
            List.of(),
            SweetSpot.DEEPSLATE, SweetSpot.THIN, SweetSpot.DEEPSLATE
    );


    public static Difficulty HARD = new Difficulty(
            100, 11, 20, 1,
            List.of(),
            SweetSpot.THIN, SweetSpot.THIN);

    public static Difficulty HARD_DEEP_DARK = new Difficulty(
            500, 11, 20, 1,
            List.of(new DeepDarkModifier("")),
            SweetSpot.SCULK, SweetSpot.SCULK);

    public static Difficulty HARD_STONE = new Difficulty(
            100, 11, 20, 1,
            List.of(),
            SweetSpot.STONE_LOW_REWARD, SweetSpot.STONE_LOW_REWARD, SweetSpot.THIN);

    public static Difficulty HARD_DRIPSTONE = new Difficulty(
            100, 11, 20, 1,
            List.of(),
            SweetSpot.THIN, SweetSpot.DRIPSTONE);

    public static Difficulty HARD_FROZEN = new Difficulty(
            100, 11, 20, 1,
            List.of(new FreezeOnMissModifier(140, 10, "")),
            SweetSpot.FROZEN, SweetSpot.THIN);

    public static Difficulty HARD_MIRAGE = new Difficulty(
            100, 11, 20, 1,
            List.of(
                    new SpawnSweetSpotsModifier(140, 1, SweetSpot.MIRAGE_NORMAL, false, ""),
                    new SpawnSweetSpotsModifier(130, 0.3f, SweetSpot.MIRAGE_THIN, false, ""),
                    new SpawnSweetSpotsModifier(120, 0.1f, SweetSpot.MIRAGE_THIN, false, "")
            ),
            SweetSpot.THIN, SweetSpot.THIN);

    public static Difficulty HARD_MIRAGE_MOVING = new Difficulty(
            100, 11, 20, 1,
            List.of(
                    new SpawnSweetSpotsModifier(140, 1, SweetSpot.MIRAGE_NORMAL.moving(1), false, ""),
                    new SpawnSweetSpotsModifier(130, 0.3f, SweetSpot.MIRAGE_THIN.moving(1), false, ""),
                    new SpawnSweetSpotsModifier(120, 0.1f, SweetSpot.MIRAGE_THIN.moving(1), false, "")
            ),
            SweetSpot.THIN.moving(1), SweetSpot.THIN.moving(1));


    //
    //  ,---. ,--.         ,--.                                         ,--.  ,---. ,--.
    // /  .-' `--'  ,---.  |  ,---.       ,---.   ,---.   ,---.   ,---. `--' /  .-' `--'  ,---.
    // |  `-, ,--. (  .-'  |  .-.  |     (  .-'  | .-. | | .-. : | .--' ,--. |  `-, ,--. | .--'
    // |  .-' |  | .-'  `) |  | |  |     .-'  `) | '-' ' \   --. \ `--. |  | |  .-' |  | \ `--.
    // `--'   `--' `----'  `--' `--'     `----'  |  |-'   `----'  `---' `--' `--'   `--'  `---'
    //                                           `--'

    public static Difficulty CREEPER = new Difficulty(
            100, 10, 20, 1,
            List.of(new SpawnSweetSpotsModifier(10, 0.25f, Difficulty.SweetSpot.TNT_VANISHING, true, "")),
            SweetSpot.CREEPER, SweetSpot.CREEPER
    );

    //crabs
    public static Difficulty DEEPSLATE_CRAB = new Difficulty(
            200, 14, 10, 1,
            List.of(),
            SweetSpot.DEEPSLATE_CRAB_CLAW, SweetSpot.DEEPSLATE_CRAB_CLAW,
            SweetSpot.DEEPSLATE_CRAB_LEG, SweetSpot.DEEPSLATE_CRAB_LEG, SweetSpot.DEEPSLATE_CRAB_LEG,
            SweetSpot.DEEPSLATE_CRAB_LEG, SweetSpot.DEEPSLATE_CRAB_LEG, SweetSpot.DEEPSLATE_CRAB_LEG);

    public static Difficulty OBSIDIAN_CRAB = new Difficulty(
            200, 14, 10, 1,
            List.of(),
            SweetSpot.OBSIDIAN_CRAB_CLAW, SweetSpot.OBSIDIAN_CRAB_CLAW,
            SweetSpot.OBSIDIAN_CRAB_LEG, SweetSpot.OBSIDIAN_CRAB_LEG, SweetSpot.OBSIDIAN_CRAB_LEG,
            SweetSpot.OBSIDIAN_CRAB_LEG, SweetSpot.OBSIDIAN_CRAB_LEG, SweetSpot.OBSIDIAN_CRAB_LEG);

    public static Difficulty NETHER_CRAB = new Difficulty(
            200, 14, 10, 1,
            List.of(),
            SweetSpot.NETHER_CRAB_CLAW, SweetSpot.NETHER_CRAB_CLAW,
            SweetSpot.NETHER_CRAB_LEG, SweetSpot.NETHER_CRAB_LEG, SweetSpot.NETHER_CRAB_LEG,
            SweetSpot.NETHER_CRAB_LEG, SweetSpot.NETHER_CRAB_LEG, SweetSpot.NETHER_CRAB_LEG);

    public static Difficulty END_CRAB = new Difficulty(
            200, 14, 10, 1,
            List.of(),
            SweetSpot.END_CRAB_CLAW, SweetSpot.END_CRAB_CLAW,
            SweetSpot.END_CRAB_LEG, SweetSpot.END_CRAB_LEG, SweetSpot.END_CRAB_LEG,
            SweetSpot.END_CRAB_LEG, SweetSpot.END_CRAB_LEG, SweetSpot.END_CRAB_LEG);


    //
    //,--.                                     ,--.
    //|  |  ,---.   ,---.   ,---.  ,--,--,   ,-|  |  ,--,--. ,--.--. ,--. ,--.
    //|  | | .-. : | .-. | | .-. : |      \ ' .-. | ' ,-.  | |  .--'  \  '  /
    //|  | \   --. ' '-' ' \   --. |  ||  | \ `-' | \ '-'  | |  |      \   '
    //`--'  `----' .`-  /   `----' `--''--'  `---'   `--`--' `--'    .-'  /
    //             `---'                                             `---'

    public static Difficulty AURORA = new Difficulty(
            500, 14, 75, 3f,
            List.of(new FreezeOnMissModifier(40, 10, "")),
            SweetSpot.FROZEN, SweetSpot.FROZEN, SweetSpot.FROZEN, SweetSpot.FROZEN
    ).moving().vanishing();

    public static Difficulty BOREAL = new Difficulty(
            300, 14, 75, 3f,
            List.of(new FreezeOnMissModifier(40, 10, "")),
            SweetSpot.FROZEN, SweetSpot.FROZEN
    ).moving();

    public static Difficulty AZURE_CRYSTALBACK_MINNOW = new Difficulty(
            300, 14, 75, 3f,
            List.of(new FreezeOnMissModifier(40, 10, "")),
            SweetSpot.FROZEN, SweetSpot.FROZEN
    ).vanishing();

    public static Difficulty JOEL = new Difficulty(
            2000, 6, 300, 1,
            List.of(),
            SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL
    );

    public static Difficulty SHROOMFISH = new Difficulty(
            700, 12, 100, 1,
            List.of(
                    new SpawnSweetSpotsModifier(40, 0.5f, SweetSpot.RED_MUSHROOM, false, ""),
                    new SpawnSweetSpotsModifier(30, 0.5f, SweetSpot.BROWN_MUSHROOM, false, "")
            ),
            SweetSpot.NORMAL, SweetSpot.NORMAL, SweetSpot.NORMAL
    );

    public static Difficulty OASIS_SURGEON = new Difficulty(
            500, 12, 10, 1f,
            List.of(
                    new SpawnSweetSpotsModifier(100, 0.3f, SweetSpot.MIRAGE_THIN.moving(1), false, ""),
                    new SpawnSweetSpotsModifier(110, 0.3f, SweetSpot.MIRAGE_THIN.moving(1), false, ""),
                    new SpawnSweetSpotsModifier(120, 0.3f, SweetSpot.MIRAGE_THIN.moving(1), false, ""),
                    new SpawnSweetSpotsModifier(130, 0.3f, SweetSpot.MIRAGE_THIN.moving(1), false, ""),
                    new SpawnSweetSpotsModifier(140, 0.3f, SweetSpot.MIRAGE_THIN.moving(1), false, ""),
                    new SpawnSweetSpotsModifier(150, 0.3f, SweetSpot.MIRAGE_THIN.moving(1), false, ""),
                    new SpawnSweetSpotsModifier(260, 0.3f, SweetSpot.MIRAGE_THIN.moving(1), false, "")
            ),
            SweetSpot.THIN.moving(1)
    );

    public static Difficulty WITHER = new Difficulty(300,
            10, 30, 1,
            List.of(),
            SweetSpot.WITHER_BIG, SweetSpot.WITHER.moving(3), SweetSpot.WITHER.moving(-3)
    );

    public static Difficulty STONEFISH = new Difficulty(
            3000, 14, 30, 0f,
            List.of(),
            SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE, SweetSpot.STONE
    );

    public static Difficulty DEEPSLATEFISH = new Difficulty(
            3000, 14, 30, 0f,
            List.of(),
            SweetSpot.DEEPSLATE, SweetSpot.DEEPSLATE, SweetSpot.DEEPSLATE, SweetSpot.DEEPSLATE
    );

    public static Difficulty WARD = new Difficulty(
            1000, 10, 20, 1f,
            List.of(new DeepDarkModifier("")),
            SweetSpot.SCULK, SweetSpot.SCULK
    );

    public static Difficulty VESANI = new Difficulty(
            100, 10, 20, 1,
            List.of());

    public static Difficulty CERBERAY = new Difficulty(
            500, 14, 10, 1.5f,
            List.of(
                    new Nikdo53Modifier(2, "")
            ),
            SweetSpot.THIN, SweetSpot.THIN
    );

    public static Difficulty CLOUDFIN = new Difficulty(
            5000, 10, -10, 1,
            List.of(
                    new PullDownModifier(""),
                    new DisableHitSoundsModifier(""),
                    new DisableMissSoundsModifier(""),
                    new FlipSweetSpotsOnMissModifier(0.05f, "")
            ),
            SweetSpot.CLOUD_1, SweetSpot.CLOUD_2, SweetSpot.CLOUD_3, SweetSpot.CLOUD_4
    );

    public static Difficulty VOIDBITER = new Difficulty(
            500, 10, 30, 1,
            List.of(
                    new FreezeOnMissModifier(40, 10, ""),
                    new Nikdo53Modifier(1, ""),
                    new DeepDarkModifier(""),
                    new SpawnSweetSpotsModifier(100, 0.3f, SweetSpot.MIRAGE_NORMAL, false, ""),
                    new AddLeavesSweetspotsModifier(0.025f, "")
            ),
            SweetSpot.SCULK, SweetSpot.TNT, SweetSpot.DRIPSTONE, SweetSpot.GLOWING, SweetSpot.FROZEN, SweetSpot.VOIDBITER_SPOT, SweetSpot.DEEPSLATE, SweetSpot.STONE_LOW_REWARD);


    public static final Codec<Difficulty> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("hp").forGetter(Difficulty::hp),
                    Codec.INT.fieldOf("speed").forGetter(Difficulty::speed),
                    Codec.INT.fieldOf("missPenalty").forGetter(Difficulty::penalty),
                    Codec.FLOAT.fieldOf("decay").forGetter(Difficulty::decay),
                    Modifier.CODEC.listOf().fieldOf("modifiers").forGetter(Difficulty::modifiers),
                    SweetSpot.LIST_CODEC.fieldOf("sweetspots").forGetter(Difficulty::sweetSpots)
            ).apply(instance, Difficulty::new));


    public static final StreamCodec<FriendlyByteBuf, Difficulty> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Difficulty::hp,
            ByteBufCodecs.INT, Difficulty::speed,
            ByteBufCodecs.INT, Difficulty::penalty,
            ByteBufCodecs.FLOAT, Difficulty::decay,
            ByteBufCodecs.fromCodec(Modifier.CODEC).apply(ByteBufCodecs.list()), Difficulty::modifiers,
            SweetSpot.LIST_STREAM_CODEC, Difficulty::sweetSpots,
            Difficulty::new
    );

    public record SweetSpot(
            ResourceLocation sweetSpotType,
            ResourceLocation texturePath,
            int size,
            int reward,
            boolean isFlip,
            float vanishingRate,
            float movingRate,
            int particleColor,
            List<Modifier> modifiers
    )
    {
        public SweetSpot(ResourceLocation sweetSpotType, ResourceLocation texturePath, int size, int reward, int particleColor, List<Modifier> onHitModifiers)
        {
            this(sweetSpotType, texturePath, size, reward, false, 0, 0, particleColor, onHitModifiers);
        }

        public SweetSpot(ResourceLocation sweetSpotType, ResourceLocation texturePath, int size, int reward, int particleColor)
        {
            this(sweetSpotType, texturePath, size, reward, false, 0, 0, particleColor, List.of());
        }

        public SweetSpot(ResourceLocation sweetSpotType, ResourceLocation texturePath, int size, int reward, boolean isFlip, float vanishingRate, float movingRate, int particleColor)
        {
            this(sweetSpotType, texturePath, size, reward, isFlip, vanishingRate, movingRate, particleColor, List.of());
        }


        private static final ResourceLocation RL_NORMAL = Starcatcher.rl("textures/gui/minigame/spots/normal.png");
        private static final ResourceLocation RL_NORMAL_STEADY = Starcatcher.rl("textures/gui/minigame/spots/normal_steady.png");
        private static final ResourceLocation RL_THIN = Starcatcher.rl("textures/gui/minigame/spots/thin.png");
        private static final ResourceLocation RL_THIN_STEADY = Starcatcher.rl("textures/gui/minigame/spots/thin_steady.png");
        private static final ResourceLocation RL_FREEZE = Starcatcher.rl("textures/gui/minigame/spots/frozen.png");
        private static final ResourceLocation RL_TREASURE = Starcatcher.rl("textures/gui/minigame/spots/treasure.png");
        private static final ResourceLocation RL_RED_MUSHROOM = Starcatcher.rl("textures/gui/minigame/spots/red_mushroom.png");
        private static final ResourceLocation RL_BROWN_MUSHROOM = Starcatcher.rl("textures/gui/minigame/spots/brown_mushroom.png");
        private static final ResourceLocation RL_WITHER = Starcatcher.rl("textures/gui/minigame/spots/wither.png");
        private static final ResourceLocation RL_SCULK = Starcatcher.rl("textures/gui/minigame/spots/sculk.png");
        private static final ResourceLocation RL_WITHER_BIG = Starcatcher.rl("textures/gui/minigame/spots/wither_big.png");
        private static final ResourceLocation RL_CREEPER = Starcatcher.rl("textures/gui/minigame/spots/creeper.png");
        private static final ResourceLocation RL_TNT = Starcatcher.rl("textures/gui/minigame/spots/tnt.png");
        private static final ResourceLocation RL_STONE = Starcatcher.rl("textures/gui/minigame/spots/stone.png");
        private static final ResourceLocation RL_DEEPSLATE = Starcatcher.rl("textures/gui/minigame/spots/deepslate.png");
        private static final ResourceLocation RL_DRIPSTONE = Starcatcher.rl("textures/gui/minigame/spots/dripstone.png");
        private static final ResourceLocation RL_GLOWING = Starcatcher.rl("textures/gui/minigame/spots/glowing.png");
        private static final ResourceLocation RL_LEAF = Starcatcher.rl("textures/gui/minigame/spots/leaf.png");
        private static final ResourceLocation RL_CLOUD_1 = Starcatcher.rl("textures/gui/minigame/spots/cloud_1.png");
        private static final ResourceLocation RL_CLOUD_2 = Starcatcher.rl("textures/gui/minigame/spots/cloud_2.png");
        private static final ResourceLocation RL_CLOUD_3 = Starcatcher.rl("textures/gui/minigame/spots/cloud_3.png");
        private static final ResourceLocation RL_CLOUD_4 = Starcatcher.rl("textures/gui/minigame/spots/cloud_4.png");

        private static final ResourceLocation RL_NETHER_CRAB_CLAW = Starcatcher.rl("textures/gui/minigame/spots/nether_crab_claw.png");
        private static final ResourceLocation RL_NETHER_CRAB_LEG = Starcatcher.rl("textures/gui/minigame/spots/nether_crab_leg.png");

        private static final ResourceLocation RL_END_CRAB_LEG = Starcatcher.rl("textures/gui/minigame/spots/end_crab_leg.png");
        private static final ResourceLocation RL_END_CRAB_CLAW = Starcatcher.rl("textures/gui/minigame/spots/end_crab_claw.png");

        private static final ResourceLocation RL_DEEPSLATE_CRAB_LEG = Starcatcher.rl("textures/gui/minigame/spots/deepslate_crab_leg.png");
        private static final ResourceLocation RL_DEEPSLATE_CRAB_CLAW = Starcatcher.rl("textures/gui/minigame/spots/deepslate_crab_claw.png");

        private static final ResourceLocation RL_OBSIDIAN_CRAB_LEG = Starcatcher.rl("textures/gui/minigame/spots/obsidian_crab_leg.png");
        private static final ResourceLocation RL_OBSIDIAN_CRAB_CLAW = Starcatcher.rl("textures/gui/minigame/spots/obsidian_crab_claw.png");

        private static final ResourceLocation RL_THIN_STEADY_MOSSY = Starcatcher.rl("textures/gui/minigame/spots/thin_mossy.png");


        public SweetSpot flip()
        {
            return new SweetSpot(this.sweetSpotType, this.texturePath, this.size, this.reward, true, this.vanishingRate, this.movingRate, this.particleColor, this.modifiers);
        }

        public SweetSpot vanishing(float vanishingRate)
        {
            return new SweetSpot(this.sweetSpotType, this.texturePath, this.size, this.reward, this.isFlip, vanishingRate, this.movingRate, this.particleColor, this.modifiers);
        }

        public SweetSpot moving(float movingRate)
        {
            return new SweetSpot(this.sweetSpotType, this.texturePath, this.size, this.reward, this.isFlip, this.vanishingRate, movingRate, this.particleColor, this.modifiers);
        }

        public final SweetSpot withModifiers(Modifier... modifiers)
        {
            return new SweetSpot(this.sweetSpotType, this.texturePath, this.size, this.reward, this.isFlip, this.vanishingRate, this.movingRate, this.particleColor, Arrays.stream(modifiers).toList());
        }


        public static SweetSpot TRASH = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NORMAL,
                22,
                30,
                0xff00ff00
        );

        public static SweetSpot NORMAL_STEADY = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NORMAL_STEADY,
                33,
                15,
                0xff00ff00
        );

        public static SweetSpot NORMAL = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NORMAL,
                22,
                15,
                0xff00ff00
        );

        public static SweetSpot SCULK = new SweetSpot(
                SCSweetSpotsBehaviour.SCULK,
                RL_SCULK,
                22,
                15,
                0xff259ca3
        );

        public static SweetSpot THIN_STEADY = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_THIN_STEADY,
                20,
                20,
                0xff00ff00
        );

        public static SweetSpot THIN_STEADY_MOSSY = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_THIN_STEADY_MOSSY,
                20,
                10,
                true, 0.01f, 1,
                0xff00ff00
        );

        public static SweetSpot THIN = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_THIN,
                15,
                20,
                0xff00ff00
        );

        public static SweetSpot RED_MUSHROOM = new SweetSpot(
                SCSweetSpotsBehaviour.MUSHROOM,
                RL_RED_MUSHROOM,
                22,
                0,
                0xffe75252
        );


        public static SweetSpot BROWN_MUSHROOM = new SweetSpot(
                SCSweetSpotsBehaviour.MUSHROOM,
                RL_BROWN_MUSHROOM,
                22,
                0,
                0xff8b6e3b
        );

        public static SweetSpot FROZEN = new SweetSpot(
                SCSweetSpotsBehaviour.FROZEN,
                RL_FREEZE,
                33,
                15,
                0xffADD8E6
        );

        public static SweetSpot MIRAGE_NORMAL = new SweetSpot(
                SCSweetSpotsBehaviour.MIRAGE,
                RL_NORMAL,
                22,
                0,
                0xff00ff00
        );

        public static SweetSpot MIRAGE_THIN = new SweetSpot(
                SCSweetSpotsBehaviour.MIRAGE,
                RL_THIN,
                15,
                0,
                0xff00ff00
        );

        public static SweetSpot NORMAL_HEAVY = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NORMAL,
                22,
                1,
                0xff00ff00
        );

        public static SweetSpot TREASURE = new SweetSpot(
                SCSweetSpotsBehaviour.TREASURE,
                RL_TREASURE,
                20,
                15,
                0xffFFD700
        );

        public static SweetSpot WITHER = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_WITHER,
                22,
                15,
                0xff1f1f1f
        );

        public static SweetSpot WITHER_BIG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_WITHER_BIG,
                33,
                15,
                0xff1f1f1f
        );

        public static SweetSpot CREEPER = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_CREEPER,
                22,
                15,
                0xff515353
        );

        public static SweetSpot TNT = new SweetSpot(
                SCSweetSpotsBehaviour.TNT,
                RL_TNT,
                33,
                -30,
                0xffff0000
        );

        public static SweetSpot TNT_VANISHING = new SweetSpot(
                SCSweetSpotsBehaviour.TNT,
                RL_TNT,
                33,
                -30,
                0xffff0000
        ).vanishing(0.06f);

        public static SweetSpot STONE_LOW_REWARD = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_STONE,
                33,
                3,
                0xff494949
        );

        public static SweetSpot STONE = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_STONE,
                33,
                10,
                0xff494949
        );

        public static SweetSpot DEEPSLATE = new SweetSpot(
                SCSweetSpotsBehaviour.DEEPSLATE,
                RL_DEEPSLATE,
                24,
                10,
                0xff686868
        );

        public static SweetSpot CLOUD_1 = new SweetSpot(
                SCSweetSpotsBehaviour.CLOUD,
                RL_CLOUD_1,
                40,
                -50,
                0xffffffff
        ).moving(1);

        public static SweetSpot CLOUD_2 = new SweetSpot(
                SCSweetSpotsBehaviour.CLOUD,
                RL_CLOUD_2,
                30,
                -50,
                0xffffffff
        ).moving(-1);

        public static SweetSpot CLOUD_3 = new SweetSpot(
                SCSweetSpotsBehaviour.CLOUD,
                RL_CLOUD_3,
                41,
                -50,
                0xffffffff
        ).moving(1);

        public static SweetSpot CLOUD_4 = new SweetSpot(
                SCSweetSpotsBehaviour.CLOUD,
                RL_CLOUD_4,
                60,
                -50,
                0xffffffff
        ).moving(-1);

        public static SweetSpot DRIPSTONE = new SweetSpot(
                SCSweetSpotsBehaviour.DRIPSTONE,
                RL_DRIPSTONE,
                22,
                10,
                0xffdfbd81
        );

        public static SweetSpot GLOWING = new SweetSpot(
                SCSweetSpotsBehaviour.GLOWING,
                RL_GLOWING,
                20,
                0,
                0xfffdf55f
        );

        public static SweetSpot LEAF = new SweetSpot(
                SCSweetSpotsBehaviour.LEAF,
                RL_LEAF,
                30,
                15,
                0xff00ff00
        );

        public static SweetSpot DEEPSLATE_CRAB_CLAW = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_DEEPSLATE_CRAB_CLAW,
                24,
                10,
                0xffff8400
        );

        public static SweetSpot DEEPSLATE_CRAB_LEG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_DEEPSLATE_CRAB_LEG,
                15,
                1,
                0xffff8400
        );

        public static SweetSpot OBSIDIAN_CRAB_CLAW = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_OBSIDIAN_CRAB_CLAW, 24,
                10,
                0xff3b2754
        );

        public static SweetSpot OBSIDIAN_CRAB_LEG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_OBSIDIAN_CRAB_LEG, 15,
                1,
                0xff3b2754
        );

        public static SweetSpot NETHER_CRAB_CLAW = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NETHER_CRAB_CLAW, 24,
                10,
                0xffcd4545
        );

        public static SweetSpot NETHER_CRAB_LEG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_NETHER_CRAB_LEG, 15,
                1,
                0xffcd4545
        );

        public static SweetSpot END_CRAB_CLAW = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_END_CRAB_CLAW, 24,
                10,
                0xffc67ed9
        );

        public static SweetSpot END_CRAB_LEG = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_END_CRAB_LEG, 15,
                1,
                0xffc67ed9
        );

        public static SweetSpot VOIDBITER_SPOT = new SweetSpot(
                SCSweetSpotsBehaviour.NORMAL,
                RL_END_CRAB_LEG, 15,
                15,
                0xffc67ed9
        );

        public static final Codec<SweetSpot> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("sweetspot_type").forGetter(SweetSpot::sweetSpotType),
                        ResourceLocation.CODEC.fieldOf("texture_path").forGetter(SweetSpot::texturePath),
                        Codec.INT.fieldOf("hitbox_size_in_pixels").forGetter(SweetSpot::size),
                        Codec.INT.fieldOf("reward").forGetter(SweetSpot::reward),
                        Codec.BOOL.fieldOf("is_flip").forGetter(SweetSpot::isFlip),
                        Codec.FLOAT.fieldOf("vanishing_rate").forGetter(SweetSpot::vanishingRate),
                        Codec.FLOAT.fieldOf("moving_rate").forGetter(SweetSpot::movingRate),
                        Codec.INT.fieldOf("color_as_int").forGetter(SweetSpot::particleColor),
                        Modifier.CODEC.listOf().optionalFieldOf("add_modifiers_on_hit", List.of()).forGetter(SweetSpot::modifiers)
                ).apply(instance, SweetSpot::new));

        public static final Codec<List<SweetSpot>> LIST_CODEC = CODEC.listOf();

        public static final StreamCodec<FriendlyByteBuf, SweetSpot> STREAM_CODEC = ExtraComposites.composite(
                ResourceLocation.STREAM_CODEC, SweetSpot::sweetSpotType,
                ResourceLocation.STREAM_CODEC, SweetSpot::texturePath,
                ByteBufCodecs.INT, SweetSpot::size,
                ByteBufCodecs.INT, SweetSpot::reward,
                ByteBufCodecs.BOOL, SweetSpot::isFlip,
                ByteBufCodecs.FLOAT, SweetSpot::vanishingRate,
                ByteBufCodecs.FLOAT, SweetSpot::movingRate,
                ByteBufCodecs.INT, SweetSpot::particleColor,
                ByteBufCodecs.fromCodec(Modifier.CODEC).apply(ByteBufCodecs.list()), SweetSpot::modifiers,
                SweetSpot::new
        );

        public static final StreamCodec<FriendlyByteBuf, List<SweetSpot>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
    }
}
