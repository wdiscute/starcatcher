package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.io.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.secretnotes.LetterItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import java.util.List;

public record SetMessagePayload(List<String> text, String name) implements CustomPacketPayload
{
    public static final Type<SetMessagePayload> TYPE = new Type<>(Starcatcher.rl("set_message"), SetMessagePayload.class);

    public static final StreamCodec<SetMessagePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING.apply(ByteBufCodecs.list()), SetMessagePayload::text,
            ByteBufCodecs.STRING, SetMessagePayload::name,
            SetMessagePayload::new
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

                if(main.is(SCItems.LETTER.get())) is = main;
                if(off.is(SCItems.LETTER.get())) is = off;
                if(is == null) return;

                LetterItem.Message message = new LetterItem.Message(player.getUUID(), name, level.dimension().location(), text(), false);
                SCDataComponents.set(is, SCDataComponents.MESSAGE, message);
            }

        });
    }
}
