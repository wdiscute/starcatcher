package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockEntity;
import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.data.SignedGuide;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.SCStats;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SignGuidePayload(String signature, BlockPos bp) implements CustomPacketPayload
{
    public static final Type<SignGuidePayload> TYPE = new Type<>(Starcatcher.rl("sign_guide"));

    public static final StreamCodec<ByteBuf, SignGuidePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SignGuidePayload::signature,
            BlockPos.STREAM_CODEC,
            SignGuidePayload::bp,
            SignGuidePayload::new
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
            if (bp.equals(BlockPos.ZERO))
                SignedGuide.signGuide(signature, player);
            else
            {
                Level level = player.level();
                if(level.getBlockEntity(bp) instanceof DisplayBlockEntity dbe)
                {
                    ItemStack guide = dbe.getImmutableItem();
                    if(guide.is(SCItems.GUIDE))
                    {
                        if (SCDataComponents.has(guide, SCDataComponents.SIGNED_GUIDE)) return;

                        Map<ResourceLocation, FishCaughtCounter> map = SCDataAttachments.get(player, SCDataAttachments.FISHING_GUIDE).fishesCaught;

                        Map<ResourceLocation, FishCaughtCounter> mapToSave = new HashMap<>();
                        map.forEach((rl, fcc) -> mapToSave.put(rl, fcc.removeNotification()));

                        if (player instanceof ServerPlayer sp)
                        {
                            FishingGuideScreen.StatsData statsData = new FishingGuideScreen.StatsData(
                                    sp.getStats().getValue(Stats.CUSTOM.get(SCStats.TICKS_SPENT_FISHING.get())),
                                    sp.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_TREASURES.get())),
                                    sp.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_FISH_MISSED.get())),
                                    sp.getStats().getValue(Stats.CUSTOM.get(SCStats.BAIT_USED.get()))
                            );

                            SCDataComponents.set(guide, SCDataComponents.SIGNED_GUIDE,
                                    new SignedGuide(
                                            player.getUUID(),
                                            mapToSave,
                                            signature,
                                            Date.from(Instant.now()).getTime(),
                                            statsData,
                                            List.of()
                                    ));
                        }
                    }
                }
            }
        });
    }
}
