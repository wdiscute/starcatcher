package com.wdiscute.starcatcher.io.network;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.U;
import com.wdiscute.starcatcher.bob.FishingBobEntity;
import com.wdiscute.starcatcher.io.*;
import com.wdiscute.starcatcher.registry.ModCriterionTriggers;
import com.wdiscute.starcatcher.registry.ModItems;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record FishingCompletedPayload(int time, boolean completedTreasure, boolean perfectCatch, int hits) implements CustomPacketPayload {
    public static final Type<FishingCompletedPayload> TYPE = new Type<>(Starcatcher.rl("fishing_completed"));

    public static final StreamCodec<ByteBuf, FishingCompletedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            FishingCompletedPayload::time,
            ByteBufCodecs.BOOL,
            FishingCompletedPayload::completedTreasure,
            ByteBufCodecs.BOOL,
            FishingCompletedPayload::perfectCatch,
            ByteBufCodecs.INT,
            FishingCompletedPayload::hits,
            FishingCompletedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {

        Player player = context.player();
        ServerLevel level = ((ServerLevel) context.player().level());

        List<Entity> entities = level.getEntities(null, new AABB(-25, -65, -25, 25, 65, 25).move(player.position()));

        for (Entity entity : entities) {
            if (entity.getUUID().toString().equals(player.getData(ModDataAttachments.FISHING.get()))) {
                if (entity instanceof FishingBobEntity fbe) {
                    if (time() != -1) {
                        FishProperties fp = fbe.fpToFish;

                        ModCriterionTriggers.MINIGAME_COMPLETED.get().trigger((ServerPlayer) player, hits(), perfectCatch(), completedTreasure(), time(), fp.fish());
                        //MAKE THIS DATA DRIVEN
//                        if (fbe.stack.is(ModItems.THUNDERCHARGED_EEL))
//                        {
//                            LightningBolt strike = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
//                            strike.setPos(fbe.position());
//                            strike.setVisualOnly(true);
//                            level.addFreshEntity(strike);
//                        }

                        //create itemStacks
                        ItemStack is = new ItemStack(fp.fish());
                        ItemStack treasure = new ItemStack(BuiltInRegistries.ITEM.get(fp.dif().treasure().loot()));

                        //assign custom name if fish has one
                        if (!fp.customName().isEmpty())
                            is.set(DataComponents.ITEM_NAME, Component.translatable(fp.customName()));

                        //store fish properties in itemstack
                        is.set(ModDataComponents.FISH_PROPERTIES, fp);

                        //store size and weight data component
                        int size = FishCaughtCounter.getRandomSize(fp);
                        int weight = FishCaughtCounter.getRandomWeight(fp);
                        is.set(ModDataComponents.SIZE_AND_WEIGHT, new SizeAndWeight(size, weight));

                        //award fish counter
                        FishCaughtCounter.AwardFishCaughtCounter(fp, player, time(), size, weight, perfectCatch());

                        TournamentHandler.addScore(player, fp, perfectCatch());

                        //split hook double drops
                        if (perfectCatch() && fbe.hook.is(ModItems.SPLIT_HOOK)) is.setCount(2);

                        //make ItemEntities for fish and treasure
                        ItemEntity itemFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, is);
                        ItemEntity treasureFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, treasure);

                        //assign delta movement so fish flies towards uuid
                        double x = Math.clamp((player.position().x - fbe.position().x) / 25, -1, 1);
                        double y = Math.clamp((player.position().y - fbe.position().y) / 20, -1, 1);
                        double z = Math.clamp((player.position().z - fbe.position().z) / 25, -1, 1);
                        Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                        itemFished.setDeltaMovement(vec3);
                        treasureFished.setDeltaMovement(vec3);

                        //add itemEntities to level
                        level.addFreshEntity(itemFished);
                        if (completedTreasure()) level.addFreshEntity(treasureFished);

                        //play sound
                        Vec3 p = player.position();
                        level.playSound(null, p.x, p.y, p.z, SoundEvents.VILLAGER_CELEBRATE, SoundSource.AMBIENT);

                        //award fish counter
                        List<FishProperties> list = new ArrayList<>(U.getFpsFromRls(level, player.getData(ModDataAttachments.FISHES_NOTIFICATION)));
                        list.add(fbe.fpToFish);
                        player.setData(ModDataAttachments.FISHES_NOTIFICATION, U.getRlsFromFps(level, list));

                        //award exp
                        int exp = fp.rarity().getXp();
                        if (fbe.hook.is(ModItems.GOLD_HOOK))
                            exp *= (int) ((double) hits() / 3) + 1; //extra exp if gold hook is used
                        player.giveExperiencePoints(exp);

                    } else {
                        //if fish minigame failed/canceled, play sound
                        Vec3 p = player.position();
                        level.playSound(null, p.x, p.y, p.z, SoundEvents.VILLAGER_NO, SoundSource.AMBIENT);
                    }

                    fbe.kill();
                }
            }
        }

        player.setData(ModDataAttachments.FISHING.get(), "");
    }
}
