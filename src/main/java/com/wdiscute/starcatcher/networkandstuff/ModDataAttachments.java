package com.wdiscute.starcatcher.networkandstuff;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ModDataAttachments
{
    // Create the DeferredRegister for attachment types
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Starcatcher.MOD_ID);


    public static final Supplier<AttachmentType<String>> FISHING = ATTACHMENT_TYPES.register(
            "fishing", () -> AttachmentType.builder(() -> "")
                    .serialize(Codec.unit(""))
                    .sync(ByteBufCodecs.STRING_UTF8)
                    .build()
    );

    public static final Supplier<AttachmentType<List<FishCaughtCounter>>> FISHES_CAUGHT = ATTACHMENT_TYPES.register(
            "fishes_caught", () ->
                    AttachmentType.builder(() -> List.of(new FishCaughtCounter(FishProperties.DEFAULT, 0, 0, 0)))
                            .serialize(FishCaughtCounter.LIST_CODEC)
                            .sync(FishCaughtCounter.STREAM_CODEC)
                            .copyOnDeath()
                            .build()
    );

    public static final Supplier<AttachmentType<List<FishProperties>>> FISHES_NOTIFICATION = ATTACHMENT_TYPES.register(
            "fishes_notification", () ->
                    AttachmentType.builder(() -> List.of(FishProperties.DEFAULT))
                            .serialize(FishProperties.LIST_CODEC)
                            .sync(FishProperties.STREAM_CODEC_LIST)
                            .copyOnDeath()
                            .build()
    );

    public static final Supplier<AttachmentType<SingleStackContainer>> BOBBER = ATTACHMENT_TYPES.register(
            "bobber", () ->
                    AttachmentType.builder(() -> SingleStackContainer.EMPTY)
                            .serialize(SingleStackContainer.CODEC)
                            .sync(SingleStackContainer.STREAM_CODEC)
                            .build()
    );

    public static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }

}
