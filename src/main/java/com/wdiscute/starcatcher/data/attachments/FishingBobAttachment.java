package com.wdiscute.starcatcher.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentHolder;

import java.util.UUID;

public class FishingBobAttachment
{
    private String uuid;

    public FishingBobAttachment(String uuid)
    {
        this.uuid = uuid;
    }

    public static final Codec<FishingBobAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("uuid").forGetter(data -> data.uuid)
            ).apply(instance, FishingBobAttachment::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FishingBobAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, data -> data.uuid,
            FishingBobAttachment::new
    );

    public boolean isEmpty()
    {
        return uuid.isEmpty();
    }

    public void setUuid(AttachmentHolder holder, UUID uuid) {
        this.uuid = uuid.toString();
        holder.syncData(SCDataAttachments.FISHING_BOB);
    }

    public UUID getUuid()
    {
        if(uuid.isEmpty()) return UUID.randomUUID();
        return UUID.fromString(uuid);
    }

}
