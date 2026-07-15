package com.wdiscute.starcatcher.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.registry.SCItems;
import com.wdiscute.starcatcher.registry.SCStats;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record SignedGuide(UUID owner, Map<ResourceLocation, FishCaughtCounter> fishesCaught, String signature,
                          long date, FishingGuideScreen.StatsData stats)
{
    public static final Codec<SignedGuide> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(SignedGuide::owner),
                    Codec.unboundedMap(ResourceLocation.CODEC, FishCaughtCounter.CODEC)
                            .fieldOf("fishes_caught").forGetter(SignedGuide::fishesCaught),
                    Codec.STRING.fieldOf("signature").forGetter(SignedGuide::signature),
                    Codec.LONG.fieldOf("date_signed").forGetter(SignedGuide::date),
                    FishingGuideScreen.StatsData.CODEC.fieldOf("stats").forGetter(SignedGuide::stats)
            ).apply(instance, SignedGuide::new)
    );

    public static boolean SignGuide(String signature, Player player)
    {
        ItemStack book = null;
        if (player.getMainHandItem().is(SCItems.GUIDE))
            book = player.getMainHandItem();

        if (player.getOffhandItem().is(SCItems.GUIDE))
            book = player.getOffhandItem();

        if (book == null) return false;
        if (SCDataComponents.has(book, SCDataComponents.SIGNED_GUIDE)) return false;

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

            SCDataComponents.set(book, SCDataComponents.SIGNED_GUIDE,
                    new SignedGuide(
                            player.getUUID(),
                            mapToSave,
                            signature,
                            Date.from(Instant.now()).getTime(),
                            statsData
                    ));
        }

        return true;
    }


}
