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
                    .sync(new FishingSyncHandler())
                    .build()
    );

    public static final Supplier<AttachmentType<FishProperties>> FISH_SPOTTER = ATTACHMENT_TYPES.register(
            "fish_spotter", () -> AttachmentType.builder(() -> FishProperties.DEFAULT)
                    .serialize(FishProperties.CODEC)
                    .sync(new FishSpotterSyncHandler())
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<List<FishCaughtCounter>>> FISHES_CAUGHT = ATTACHMENT_TYPES.register(
            "fishes_caught", () ->
                    AttachmentType.builder(() -> List.of(new FishCaughtCounter(FishProperties.DEFAULT, 0, 0, 0)))
                            .serialize(FishCaughtCounter.LIST_CODEC)
                            .sync(new FishCounterSyncHandler())
                            .copyOnDeath()
                            .build()
    );

    public static final Supplier<AttachmentType<List<FishProperties>>> FISHES_NOTIFICATION = ATTACHMENT_TYPES.register(
            "fishes_notification", () ->
                    AttachmentType.builder(() -> List.of(FishProperties.DEFAULT))
                            .serialize(FishProperties.LIST_CODEC)
                            .sync(new FishPropertiesListSyncHandler())
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


    public static class FishSpotterSyncHandler implements AttachmentSyncHandler<FishProperties>
    {
        @Override
        public void write(@NotNull RegistryFriendlyByteBuf buf, @NotNull FishProperties attachment, boolean initialSync)
        {
            FishProperties.STREAM_CODEC.encode(buf, attachment);
        }

        @Override
        @Nullable
        public FishProperties read(@NotNull IAttachmentHolder holder, @NotNull RegistryFriendlyByteBuf buf, @Nullable FishProperties previousValue)
        {
            return FishProperties.STREAM_CODEC.decode(buf);
        }

        @Override
        public boolean sendToPlayer(@NotNull IAttachmentHolder holder, @NotNull ServerPlayer to)
        {
            return holder == to;
        }
    }

    public static class FishPropertiesListSyncHandler implements AttachmentSyncHandler<List<FishProperties>>
    {
        @Override
        public void write(@NotNull RegistryFriendlyByteBuf buf, @NotNull List<FishProperties> attachment, boolean initialSync)
        {
            FishProperties.STREAM_CODEC_LIST.encode(buf, attachment);
        }

        @Override
        @Nullable
        public List<FishProperties> read(@NotNull IAttachmentHolder holder, @NotNull RegistryFriendlyByteBuf buf, @Nullable List<FishProperties> previousValue)
        {
            return FishProperties.STREAM_CODEC_LIST.decode(buf);
        }

        @Override
        public boolean sendToPlayer(@NotNull IAttachmentHolder holder, @NotNull ServerPlayer to)
        {
            return holder == to;
        }
    }

    public static class FishingSyncHandler implements AttachmentSyncHandler<String>
    {
        @Override
        public void write(@NotNull RegistryFriendlyByteBuf buf, @NotNull String attachment, boolean initialSync)
        {
            ByteBufCodecs.STRING_UTF8.encode(buf, attachment);
        }

        @Override
        @Nullable
        public String read(@NotNull IAttachmentHolder holder, @NotNull RegistryFriendlyByteBuf buf, @Nullable String previousValue)
        {
            return ByteBufCodecs.STRING_UTF8.decode(buf);
        }

        @Override
        public boolean sendToPlayer(@NotNull IAttachmentHolder holder, @NotNull ServerPlayer to)
        {
            return holder == to;
        }
    }

    public static class FishCounterSyncHandler implements AttachmentSyncHandler<List<FishCaughtCounter>>
    {
        @Override
        public void write(@NotNull RegistryFriendlyByteBuf buf, @NotNull List<FishCaughtCounter> attachment, boolean initialSync)
        {
            FishCaughtCounter.STREAM_CODEC.encode(buf, attachment);
        }

        @Override
        @Nullable
        public List<FishCaughtCounter> read(@NotNull IAttachmentHolder holder, @NotNull RegistryFriendlyByteBuf buf, @Nullable List<FishCaughtCounter> previousValue)
        {
            return FishCaughtCounter.STREAM_CODEC.decode(buf);
        }

        @Override
        public boolean sendToPlayer(@NotNull IAttachmentHolder holder, @NotNull ServerPlayer to)
        {
            return holder == to;
        }
    }

    public static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }

}
