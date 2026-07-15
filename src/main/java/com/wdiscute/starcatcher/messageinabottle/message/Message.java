package com.wdiscute.starcatcher.messageinabottle.message;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Message(
        UUID sender,
        String senderDisplayName,
        List<String> text,
        ResourceLocation dimension,
        ResourceLocation background
)
{
    public static final ResourceLocation BACKGROUND_OVERWORLD = Starcatcher.rl("textures/gui/message/message_overworld.png");
    public static final ResourceLocation BACKGROUND_NETHER = Starcatcher.rl("textures/gui/message/message_nether.png");
    public static final ResourceLocation BACKGROUND_END = Starcatcher.rl("textures/gui/message/message_end.png");

    public static final Message DEFAULT = new Message(
            UUID.randomUUID(),
            "<sclegendary>-dev (wd)</sclegendary>",
            List.of(
                    "",
                    "",
                    "This is a cheated message from creative.",
                    "",
                    "Usually players would write their own",
                    "message and send it to the ocean for",
                    "others to fish later.",
                    "(5%% weight if there is any available)",
                    "",
                    "<scgolden>It also supports text processors!</scgolden>",
                    "learn more about it in the LibTooltips wiki"
            ),
            Starcatcher.MISSINGNO,
            Message.BACKGROUND_END
    );

    public static final Codec<Message> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("sender").forGetter(Message::sender),
                    Codec.STRING.fieldOf("sender_display_name").forGetter(Message::senderDisplayName),
                    Codec.STRING.listOf().fieldOf("text").forGetter(Message::text),
                    ResourceLocation.CODEC.fieldOf("dimension").forGetter(Message::dimension),
                    ResourceLocation.CODEC.fieldOf("background").forGetter(Message::background)
            ).apply(instance, Message::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Message> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, Message::sender,
            ByteBufCodecs.STRING_UTF8, Message::senderDisplayName,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), Message::text,
            ResourceLocation.STREAM_CODEC, Message::dimension,
            ResourceLocation.STREAM_CODEC, Message::background,
            Message::new
    );

    public interface BuiltIn
    {
        Message AMETHYST_HOOK = create("amethyst_hook", Message.BACKGROUND_OVERWORLD, 16);
        Message LAVA_PROOF_1 = create("lava_proof_1", Message.BACKGROUND_OVERWORLD, 16);
        Message LAVA_PROOF_2 = create("lava_proof_2", Message.BACKGROUND_OVERWORLD, 16);
        Message HOPEFUL = create("hopeful", Message.BACKGROUND_OVERWORLD, 16);
        Message HOPELESS = create("hopeless", Message.BACKGROUND_OVERWORLD, 16);
        Message TRUE_BLUE = create("true_blue", Message.BACKGROUND_OVERWORLD, 16);
        Message WITHER = create("wither", Message.BACKGROUND_NETHER, 16);

        //creates a built-in message from a translation key
        static Message create(String translationKey, ResourceLocation background, int length)
        {
            List<String> text = new ArrayList<>();

            for (int i = 0; i < length; i++)
            {
                String key = "starcatcher.message." + translationKey + "." + i;
                text.add(key);
            }

            return new Message(
                    UUID.fromString("65b5b741-c418-4f3b-8e94-316702488ff5"),
                    Component.translatable("starcatcher.message." + translationKey + ".sender").getString(),
                    text,
                    Starcatcher.MISSINGNO,
                    background
            );
        }
    }
}

