package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

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
                    AttachmentType.builder(() -> List.of(new FishCaughtCounter(Starcatcher.rl("missingno_rl"), 0, 0, 0, 0, 0, false, false)))
                            .serialize(FishCaughtCounter.LIST_CODEC)
                            .sync(FishCaughtCounter.LIST_STREAM_CODEC)
                            .copyOnDeath()
                            .build()
    );

    public static final Supplier<AttachmentType<List<ResourceLocation>>> TROPHIES_CAUGHT = ATTACHMENT_TYPES.register(
            "trophies_caught", () ->
                    AttachmentType.builder(() -> List.of(Starcatcher.rl("missingno_rl")))
                            .serialize(ResourceLocation.CODEC.listOf())
                            .sync(ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()))
                            .copyOnDeath()
                            .build()
    );

    public static final Supplier<AttachmentType<List<ResourceLocation>>> TREASURES_CAUGHT = ATTACHMENT_TYPES.register(
            "treasures_caught", () ->
                    AttachmentType.builder(() -> List.of(Starcatcher.rl("missingno")))
                            .serialize(ResourceLocation.CODEC.listOf())
                            .sync(ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()))
                            .copyOnDeath()
                            .build()
    );

    public static final Supplier<AttachmentType<List<ResourceLocation>>> FISHES_NOTIFICATION = ATTACHMENT_TYPES.register(
            "fishes_notification", () ->
                    AttachmentType.builder(() -> List.of(Starcatcher.rl("missingno_rl")))
                            .serialize(ResourceLocation.CODEC.listOf())
                            .sync(ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()))
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
