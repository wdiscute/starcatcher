package com.wdiscute.starcatcher.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.FishCaughtCounter;
import com.wdiscute.starcatcher.io.SCDataAttachments;
import com.wdiscute.starcatcher.io.SCDataComponents;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record SignedGuide(UUID owner, Map<Identifier, FishCaughtCounter> fishesCaught, String signature, long date)
{
    public static final Codec<SignedGuide> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(SignedGuide::owner),
                    Codec.unboundedMap(Identifier.CODEC, FishCaughtCounter.CODEC)
                            .fieldOf("fishes_caught").forGetter(SignedGuide::fishesCaught),
                    Codec.STRING.fieldOf("signature").forGetter(SignedGuide::signature),
                    Codec.LONG.fieldOf("date_signed").forGetter(SignedGuide::date)
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

        Map<Identifier, FishCaughtCounter> map = SCDataAttachments.get(player, SCDataAttachments.FISHING_GUIDE).fishesCaught;

        Map<Identifier, FishCaughtCounter> mapToSave = new HashMap<>();
        map.forEach((rl, fcc) -> mapToSave.put(rl, fcc.removeNotification()));

        SCDataComponents.set(book, SCDataComponents.SIGNED_GUIDE,
                new SignedGuide(
                        player.getUUID(),
                        mapToSave,
                        signature,
                        Date.from(Instant.now()).getTime()
                ));

        return true;
    }
}
