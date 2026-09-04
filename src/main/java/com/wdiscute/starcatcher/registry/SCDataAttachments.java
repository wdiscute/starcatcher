package com.wdiscute.starcatcher.registry;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.data.attachments.FishingBobAttachment;
import com.wdiscute.starcatcher.data.attachments.FishingGuideAttachment;
import com.wdiscute.starcatcher.messageinabottle.message.Message;
import com.wdiscute.starcatcher.modifiers.Modifier;
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
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public interface SCDataAttachments
{
    DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Starcatcher.MOD_ID);

    class FishingBobAttachmentCap extends DataAttachment<FishingBobAttachment> {}
    Capability<FishingBobAttachmentCap> FISHING_BOB_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    Supplier<AttachmentType<FishingBobAttachment>> FISHING_BOB = ATTACHMENT_TYPES.register(
            "fishing_bob", () -> AttachmentType.builder(FISHING_BOB_CAP, () -> new FishingBobAttachment(""))
                    .sync(FishingBobAttachment.STREAM_CODEC)
                    .build()
    );


    class MessagesCaughtAttachmentCap extends DataAttachment<List<Message>>{}
    Capability<MessagesCaughtAttachmentCap> MESSAGES_CAUGHT_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    Supplier<AttachmentType<List<Message>>> MESSAGES_CAUGHT = ATTACHMENT_TYPES.register(
            "messages_caught", () -> AttachmentType.builder(MESSAGES_CAUGHT_CAP, List::of)
                    .sync(Message.STREAM_CODEC.apply(ByteBufCodecs.list()))
                    .canAttachTo(AdvancedCapabilityType.PLAYER)
                    .build()
    );


    class TrackedFishAttachmentCap extends DataAttachment<ResourceLocation>{}
    Capability<TrackedFishAttachmentCap> TRACKED_FISH_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    Supplier<AttachmentType<ResourceLocation>> TRACKED_FISH = ATTACHMENT_TYPES.register(
            "tracked_fish", () -> AttachmentType.builder(TRACKED_FISH_CAP, () -> Starcatcher.MISSINGNO)
                    .serialize(ResourceLocation.CODEC)
                    .canAttachTo(AdvancedCapabilityType.PLAYER)
                    .sync(ByteBufCodecs.RESOURCE_LOCATION)
                    .build()
    );

    class FishingGuideAttachmentCap extends DataAttachment<FishingGuideAttachment>{}
    Capability<FishingGuideAttachmentCap> FISHING_GUIDE_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    Supplier<AttachmentType<FishingGuideAttachment>> FISHING_GUIDE = ATTACHMENT_TYPES.register(
            "fishing_guide", () -> AttachmentType.builder(FISHING_GUIDE_CAP, FishingGuideAttachment::createDefault)
                    .serialize(FishingGuideAttachment.CODEC)
                    .sync(FishingGuideAttachment.STREAM_CODEC)
                    .canAttachTo(AdvancedCapabilityType.PLAYER)
                    .copyOnDeath()
                    .build()
    );

    class TackleSkinAttachmentCap extends DataAttachment<ResourceLocation>{}
    Capability<TackleSkinAttachmentCap> TACKLE_SKIN_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    Supplier<AttachmentType<ResourceLocation>> TACKLE_SKIN = ATTACHMENT_TYPES.register(
            "tackle_skin", () ->
                    AttachmentType.builder(TACKLE_SKIN_CAP, () -> Starcatcher.BASE)
                            .serialize(ResourceLocation.CODEC)
                            .canAttachTo(AdvancedCapabilityType.PLAYER)
                            .sync(ByteBufCodecs.RESOURCE_LOCATION)
                            .build()
    );

    class ModifiersAttachmentCap extends DataAttachment<List<Modifier>>{}
    Capability<ModifiersAttachmentCap> MODIFIERS_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    Supplier<AttachmentType<List<Modifier>>> MODIFIERS = ATTACHMENT_TYPES.register(
            "modifiers", () ->
                    AttachmentType.builder(MODIFIERS_CAP, List::of)
                            .serialize(Modifier.CODEC.listOf())
                            .sync(ByteBufCodecs.fromCodec(Modifier.CODEC).apply(ByteBufCodecs.list()))
                            .build()
    );


    // sets the value to default
    static <T> void remove(Entity holder, Supplier<AttachmentType<T>> attachmentType)
    {
        if(holder == null) return;
        holder.removeData(attachmentType);
    }

    // sets the value to default
    static <T> void remove(Entity holder, AttachmentType<T> attachmentType)
    {
        if(holder == null) return;
        holder.removeData(attachmentType);
    }

    static <T> void set(Entity holder, Supplier<AttachmentType<T>> attachmentType, T data)
    {
        if(holder == null) return;
        holder.setData(attachmentType, data);
    }

    static <T> void set(Entity holder, AttachmentType<T> attachmentType, T data)
    {
        if(holder == null) return;
        holder.setData(attachmentType, data);
    }

    static <T> T get(Entity holder, Supplier<AttachmentType<T>> attachmentType)
    {
        if(holder == null) throw new RuntimeException("Called Starcatcher DataAttachments Get() with a null entity");
        return holder.getData(attachmentType);
    }

    static <T> T get(Entity holder, AttachmentType<T> attachmentType)
    {
        if(holder == null) throw new RuntimeException("Called Starcatcher DataAttachments Get() with a null entity");
        return holder.getData(attachmentType);
    }

    static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }

}
