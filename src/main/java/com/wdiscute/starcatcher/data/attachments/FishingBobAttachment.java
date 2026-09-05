package com.wdiscute.starcatcher.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

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

    public static final StreamCodec<FishingBobAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, data -> data.uuid,
            FishingBobAttachment::new
    );

    public boolean isEmpty()
    {
        return uuid.isEmpty();
    }

    public void setUuid(Player holder, UUID uuid)
    {
        this.uuid = uuid.toString();
        holder.syncData(SCDataAttachments.FISHING_BOB);
        SCDataAttachments.set(holder, SCDataAttachments.FISHING_BOB.get(), this);
    }

    public UUID getUuid()
    {
        if (uuid.isEmpty()) return UUID.randomUUID();
        return UUID.fromString(uuid);
    }

}
