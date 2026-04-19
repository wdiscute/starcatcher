package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.attachments.FishingBobAttachment;
import com.wdiscute.starcatcher.io.attachments.FishingGuideAttachment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.nikdo53.neobackports.io.attachment.AdvancedCapabilityType;
import net.nikdo53.neobackports.io.attachment.AttachmentType;
import net.nikdo53.neobackports.io.attachment.DataAttachment;
import net.nikdo53.neobackports.io.attachment.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class SCDataAttachments
{
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Starcatcher.MOD_ID);


    public static class FishingBobAttachmentCap extends DataAttachment<FishingBobAttachment>{}
    public static final Capability<FishingBobAttachmentCap> FISHING_BOB_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Supplier<AttachmentType<FishingBobAttachment>> FISHING_BOB = ATTACHMENT_TYPES.register(
            "fishing_bob", () -> AttachmentType.builder(FISHING_BOB_CAP, () -> new FishingBobAttachment(""))
                    .sync(FishingBobAttachment.STREAM_CODEC)
                    .canAttachTo(AdvancedCapabilityType.NON_LIVING_ENTITY, AdvancedCapabilityType.PLAYER)
                    .build()
    );


    public static class FishingGuideAttachmentCap extends DataAttachment<FishingGuideAttachment>{}
    public static final Capability<FishingGuideAttachmentCap> FISHING_GUIDE_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Supplier<AttachmentType<FishingGuideAttachment>> FISHING_GUIDE = ATTACHMENT_TYPES.register(
            "fishing_guide", () -> AttachmentType.builder(FISHING_GUIDE_CAP, FishingGuideAttachment::createDefault)
                    .serialize(FishingGuideAttachment.CODEC)
                    .sync(FishingGuideAttachment.STREAM_CODEC)
                    .canAttachTo(AdvancedCapabilityType.PLAYER)
                    .copyOnDeath()
                    .build()
    );

    public static class TackleSkinAttachmentCap extends DataAttachment<ResourceLocation>{}
    public static final Capability<TackleSkinAttachmentCap> TACKLE_SKIN_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Supplier<AttachmentType<ResourceLocation>> TACKLE_SKIN = ATTACHMENT_TYPES.register(
            "tackle_skin", () ->
                    AttachmentType.builder(TACKLE_SKIN_CAP, () -> Starcatcher.rl("base"))
                            .serialize(ResourceLocation.CODEC)
                            .canAttachTo(AdvancedCapabilityType.NON_LIVING_ENTITY)
                            .sync(ByteBufCodecs.RESOURCE_LOCATION)
                            .build()
    );


    // sets the value to default
    public static <T> void remove(Entity holder, Supplier<AttachmentType<T>> attachmentType)
    {
        if(holder == null) return;
        holder.removeData(attachmentType);
    }

    // sets the value to default
    public static <T> void remove(Entity holder, AttachmentType<T> attachmentType)
    {
        if(holder == null) return;
        holder.removeData(attachmentType);
    }

    public static <T> void set(Entity holder, Supplier<AttachmentType<T>> attachmentType, T data)
    {
        if(holder == null) return;
        holder.setData(attachmentType, data);
    }

    public static <T> void set(Entity holder, AttachmentType<T> attachmentType, T data)
    {
        if(holder == null) return;
        holder.setData(attachmentType, data);
    }

    public static <T> T get(Entity holder, Supplier<AttachmentType<T>> attachmentType)
    {
        if(holder == null) throw new RuntimeException("Called Starcatcher DataAttachments Get() with a null entity");
        return holder.getData(attachmentType);
    }

    public static <T> T get(Entity holder, AttachmentType<T> attachmentType)
    {
        if(holder == null) throw new RuntimeException("Called Starcatcher DataAttachments Get() with a null entity");
        return holder.getData(attachmentType);
    }

    public static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }

}
