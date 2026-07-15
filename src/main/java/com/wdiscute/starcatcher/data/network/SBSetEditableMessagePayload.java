package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.messageinabottle.letter.EditableMessage;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SBSetEditableMessagePayload(EditableMessage editableMessage) implements CustomPacketPayload
{
    public static final Type<SBSetEditableMessagePayload> TYPE = new Type<>(Starcatcher.rl("set_editable_message"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SBSetEditableMessagePayload> STREAM_CODEC = StreamCodec.composite(
            EditableMessage.STREAM_CODEC, SBSetEditableMessagePayload::editableMessage,
            SBSetEditableMessagePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() ->
        {
            Player player = context.player();

            Level level = player.level();
            if (level instanceof ServerLevel)
            {
                ItemStack is = null;
                ItemStack main = player.getMainHandItem();
                ItemStack off = player.getOffhandItem();

                if(main.is(SCItems.LETTER)) is = main;
                if(off.is(SCItems.LETTER)) is = off;
                if(is == null) return;
                SCDataComponents.set(is, SCDataComponents.EDITABLE_MESSAGE, editableMessage);
            }
        });
    }
}
