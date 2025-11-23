package com.wdiscute.starcatcher.networkandcodecs;

import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bob.FishingBobEntity;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;


public class Payloads
{

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            Starcatcher.rl("fishing"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    private static int id()
    {
        return packetId++;
    }

    public static void register()
    {

        CHANNEL.registerMessage(
                id(), FishingBobUUIDPayload.class,
                FishingBobUUIDPayload::encode,
                FishingBobUUIDPayload::decode,
                FishingBobUUIDPayload::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(
                id(), FishesCaughtPayload.class,
                FishesCaughtPayload::encode,
                FishesCaughtPayload::decode,
                FishesCaughtPayload::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(
                id(), TrophiesCaughtPayload.class,
                TrophiesCaughtPayload::encode,
                TrophiesCaughtPayload::decode,
                TrophiesCaughtPayload::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(
                id(), FishesNotificationPayload.class,
                FishesNotificationPayload::encode,
                FishesNotificationPayload::decode,
                FishesNotificationPayload::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(
                id(), FishingPayload.class,
                FishingPayload::encode,
                FishingPayload::decode,
                FishingPayload::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(
                id(), FishCaughtPayload.class,
                FishCaughtPayload::encode,
                FishCaughtPayload::decode,
                FishCaughtPayload::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(
                id(), FishingCompletedPayload.class,
                FishingCompletedPayload::encode,
                FishingCompletedPayload::decode,
                FishingCompletedPayload::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        CHANNEL.registerMessage(
                id(), FishesSeenPayload.class,
                FishesSeenPayload::encode,
                FishesSeenPayload::decode,
                FishesSeenPayload::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

    }


    //fishes seen
    public static class FishesSeenPayload
    {
        private final List<ResourceLocation> fishesSeenRLs;

        public FishesSeenPayload(List<ResourceLocation> rls)
        {
            this.fishesSeenRLs = rls;
        }

        public static void encode(FishesSeenPayload fishesSeenPayload, FriendlyByteBuf buf)
        {
            buf.writeJsonWithCodec(ResourceLocation.CODEC.listOf(), fishesSeenPayload.fishesSeenRLs);
        }

        public static FishesSeenPayload decode(FriendlyByteBuf buf)
        {
            List<ResourceLocation> rls = buf.readJsonWithCodec(ResourceLocation.CODEC.listOf());

            return new FishesSeenPayload(rls);
        }

        public static void handle(FishesSeenPayload fishesSeenPayload, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {
                //set fishes on server
                ServerPlayer player = context.get().getSender();
                List<FishProperties> currentNotifications = DataAttachments.get(player).fishNotifications();
                Registry<FishProperties> registry = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY);

                List<FishProperties> fishesSeen = new ArrayList<>();

                for (ResourceLocation rl : fishesSeenPayload.fishesSeenRLs)
                {
                    FishProperties fp = registry.get(rl);

                    if (fp == null) fp = FishProperties.DEFAULT;

                    fishesSeen.add(fp);
                }

                List<FishProperties> newList = new ArrayList<>(currentNotifications);

                fishesSeen.stream().forEach(f -> currentNotifications.stream().forEach(n ->
                {
                    if (n.equals(f)) newList.remove(n);
                }));

                DataAttachments.get(player).setFishNotifications(newList);

            });
            context.get().setPacketHandled(true);
        }
    }

    //send fishing uuid to players around player who started fishing
    public static class FishingBobUUIDPayload
    {
        private final String playerUUID;
        private final String bobUUID;

        public FishingBobUUIDPayload(Player player, String uuid)
        {
            this.playerUUID = player.getStringUUID();
            this.bobUUID = uuid;
        }

        public FishingBobUUIDPayload(String player, String uuid)
        {
            this.playerUUID = player;
            this.bobUUID = uuid;
        }

        public static void encode(FishingBobUUIDPayload fishesCaughtPayload, FriendlyByteBuf buf)
        {
            buf.writeUtf(fishesCaughtPayload.playerUUID);
            buf.writeUtf(fishesCaughtPayload.bobUUID);
        }

        public static FishingBobUUIDPayload decode(FriendlyByteBuf buf)
        {
            String playerUUID = buf.readUtf();
            String bobUUID = buf.readUtf();

            return new FishingBobUUIDPayload(playerUUID, bobUUID);
        }

        public static void handle(FishingBobUUIDPayload fishingBobUUIDPayload, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {
                Player player = Minecraft.getInstance().level.getPlayerByUUID(UUID.fromString(fishingBobUUIDPayload.playerUUID));
                if (player != null)
                    DataAttachments.get(player).setFishing(fishingBobUUIDPayload.bobUUID);
            });
            context.get().setPacketHandled(true);
        }
    }

    //send fishes caught to client
    public static class FishesCaughtPayload
    {
        private final List<FishCaughtNetwork> fishesCaught;

        public FishesCaughtPayload(List<FishCaughtCounter> fishCaught, Player player)
        {
            List<FishCaughtNetwork> fishCaughtNetworks = new ArrayList<>();

            Registry<FishProperties> fishProperties = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY);

            for (FishCaughtCounter fcc : fishCaught)
            {
                ResourceLocation rl = null;

                for (FishProperties fp : fishProperties)
                {
                    if (fp.equals(fcc.fp())) rl = fishProperties.getKey(fp);
                }

                if (rl == null) rl = Starcatcher.rl("missingno");

                fishCaughtNetworks.add(new FishCaughtNetwork(
                        rl, fcc.count(),
                        fcc.fastestTicks(),
                        fcc.averageTicks(),
                        fcc.size(),
                        fcc.weight(),
                        fcc.caughtGolden()
                ));
            }

            this.fishesCaught = fishCaughtNetworks;
        }

        public FishesCaughtPayload(List<FishCaughtNetwork> fishCaught)
        {
            this.fishesCaught = fishCaught;
        }

        public static void encode(FishesCaughtPayload fishesCaughtPayload, FriendlyByteBuf buf)
        {
            buf.writeJsonWithCodec(FishCaughtNetwork.LIST_CODEC, fishesCaughtPayload.fishesCaught);
        }

        public static FishesCaughtPayload decode(FriendlyByteBuf buf)
        {
            List<FishCaughtNetwork> fishesCaught = buf.readJsonWithCodec(FishCaughtNetwork.LIST_CODEC);

            return new FishesCaughtPayload(fishesCaught);
        }

        public static void handle(FishesCaughtPayload fishesCaughtPayload, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {
                client(fishesCaughtPayload.fishesCaught);
            });
            context.get().setPacketHandled(true);
        }


        @OnlyIn(Dist.CLIENT)
        private static void client(List<FishCaughtNetwork> fishesCaught)
        {
            DataAttachments.setFishesCaughtClient(fishesCaught);
        }

    }

    //send trophies caught to client
    public static class TrophiesCaughtPayload
    {
        private final List<TrophyProperties> tps;

        public TrophiesCaughtPayload(List<TrophyProperties> tpsCaught)
        {
            this.tps = tpsCaught;
        }

        public static void encode(TrophiesCaughtPayload trophiesCaughtPayload, FriendlyByteBuf buf)
        {
            buf.writeJsonWithCodec(TrophyProperties.LIST_CODEC, trophiesCaughtPayload.tps);
        }

        public static TrophiesCaughtPayload decode(FriendlyByteBuf buf)
        {
            List<TrophyProperties> tps = buf.readJsonWithCodec(TrophyProperties.LIST_CODEC);

            return new TrophiesCaughtPayload(tps);
        }

        public static void handle(TrophiesCaughtPayload trophiesCaughtPayload, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {
                client(trophiesCaughtPayload.tps);
            });
            context.get().setPacketHandled(true);
        }

        @OnlyIn(Dist.CLIENT)
        private static void client(List<TrophyProperties> tps)
        {
            DataAttachments.get(Minecraft.getInstance().player).setTrophiesCaught(tps);
        }

    }

    //send fishes notifications to player
    public static class FishesNotificationPayload
    {
        private final List<ResourceLocation> fps;

        public FishesNotificationPayload(List<FishProperties> fps, Player player)
        {
            List<ResourceLocation> notifRLs = new ArrayList<>();

            Registry<FishProperties> fishProperties = player.level().registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY);

            for (FishProperties fp : fps)
            {
                ResourceLocation rl = null;

                for (FishProperties fpRegistry : fishProperties)
                {
                    if (fpRegistry.equals(fp)) rl = fishProperties.getKey(fpRegistry);
                }

                if (rl == null) rl = Starcatcher.rl("missingno");

                notifRLs.add(rl);
            }

            this.fps = notifRLs;
        }

        public FishesNotificationPayload(List<ResourceLocation> tpsCaught)
        {
            this.fps = tpsCaught;
        }

        public static void encode(FishesNotificationPayload fishesNotificationPayload, FriendlyByteBuf buf)
        {
            buf.writeJsonWithCodec(ResourceLocation.CODEC.listOf(), fishesNotificationPayload.fps);
        }

        public static FishesNotificationPayload decode(FriendlyByteBuf buf)
        {
            List<ResourceLocation> tps = buf.readJsonWithCodec(ResourceLocation.CODEC.listOf());

            return new FishesNotificationPayload(tps);
        }

        public static void handle(FishesNotificationPayload fishesNotificationPayload, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {
                client(fishesNotificationPayload.fps);
            });
            context.get().setPacketHandled(true);
        }

        @OnlyIn(Dist.CLIENT)
        private static void client(List<ResourceLocation> fps)
        {
            DataAttachments.setFishNotificationsClient(fps);
        }

    }


    //send fishing start to client
    public static class FishingPayload
    {
        private final FishProperties fp;
        private final ItemStack rod;

        public FishingPayload(FishProperties fp, ItemStack rod)
        {
            this.fp = fp;
            this.rod = rod;
        }

        public static void encode(FishingPayload fishingPayload, FriendlyByteBuf buf)
        {
            buf.writeJsonWithCodec(FishProperties.CODEC, fishingPayload.fp);
            buf.writeNbt(fishingPayload.rod.serializeNBT());
        }

        public static FishingPayload decode(FriendlyByteBuf buf)
        {
            FishProperties fp = buf.readJsonWithCodec(FishProperties.CODEC);

            CompoundTag tag = buf.readNbt();

            ItemStack rod = ItemStack.of(tag);

            return new FishingPayload(fp, rod);
        }

        public static void handle(FishingPayload fishingPayload, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() -> clientScreen(fishingPayload.fp, fishingPayload.rod));
            context.get().setPacketHandled(true);
        }

        @OnlyIn(Dist.CLIENT)
        private static void clientScreen(FishProperties fp, ItemStack rod)
        {
            Minecraft.getInstance().setScreen(new FishingMinigameScreen(fp, rod));
        }

    }


    //send fishing minigame completed to server
    public static class FishingCompletedPayload
    {
        private final int time;
        private final boolean completedTreasure;
        private final boolean perfectCatch;
        private final int hits;

        public FishingCompletedPayload(int tickCount, boolean awardTreasure, boolean perfectCatch, int consecutiveHits)
        {
            this.time = tickCount;
            this.completedTreasure = awardTreasure;
            this.perfectCatch = perfectCatch;
            this.hits = consecutiveHits;
        }

        public static void encode(FishingCompletedPayload fishingCompletedPayload, FriendlyByteBuf buf)
        {
            buf.writeInt(fishingCompletedPayload.time);
            buf.writeBoolean(fishingCompletedPayload.completedTreasure);
            buf.writeBoolean(fishingCompletedPayload.perfectCatch);
            buf.writeInt(fishingCompletedPayload.hits);
        }

        public static FishingCompletedPayload decode(FriendlyByteBuf buf)
        {
            int tickCount = buf.readInt();
            boolean awardTreasure = buf.readBoolean();
            boolean perfectCatch = buf.readBoolean();
            int consecutiveHits = buf.readInt();

            return new FishingCompletedPayload(tickCount, awardTreasure, perfectCatch, consecutiveHits);
        }

        public static void handle(FishingCompletedPayload data, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {

                ServerPlayer player = context.get().getSender();
                ServerLevel level = ((ServerLevel) player.level());

                List<Entity> entities = level.getEntities(null, new AABB(-25, -65, -25, 25, 65, 25).move(player.position()));

                for (Entity entity : entities)
                {
                    if (entity.getUUID().toString().equals(DataAttachments.get(player).fishing()))
                    {
                        if (entity instanceof FishingBobEntity fbe)
                        {
                            if (data.time != -1)
                            {
                                FishProperties fp = fbe.fpToFish;

                                //create itemStacks
                                ItemStack is = new ItemStack(fbe.fpToFish.fish());
                                ItemStack treasure = new ItemStack(BuiltInRegistries.ITEM.get(fbe.fpToFish.dif().treasure().loot()));

                                //assign custom name if fish has one
                                if (!fp.customName().isEmpty())
                                {
                                    is.setHoverName(Component.literal(fp.customName()));
                                }

                                //store fish properties in itemstack
                                DataComponents.setFishProperties(is, fp);

                                //store size and weight data component
                                int size = FishCaughtCounter.getRandomSize(fp);
                                int weight = FishCaughtCounter.getRandomWeight(fp);
                                DataComponents.setSizeAndWeight(is, new SizeAndWeight(size, weight));

                                //award fish counter
                                FishCaughtCounter.AwardFishCaughtCounter(fbe.fpToFish, player, data.time, size, weight);

                                //split hook double drops
                                if (data.perfectCatch && fbe.hook.is(ModItems.SPLIT_HOOK.get())) is.setCount(2);

                                //make ItemEntities for fish and treasure
                                ItemEntity itemFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, is);
                                ItemEntity treasureFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, treasure);

                                //assign delta movement so fish flies towards player
                                double x = Mth.clamp((player.position().x - fbe.position().x) / 25, -1, 1);
                                double y = Mth.clamp((player.position().y - fbe.position().y) / 20, -1, 1);
                                double z = Mth.clamp((player.position().z - fbe.position().z) / 25, -1, 1);
                                Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                                itemFished.setDeltaMovement(vec3);
                                treasureFished.setDeltaMovement(vec3);

                                //add itemEntities to level
                                level.addFreshEntity(itemFished);
                                if (data.completedTreasure) level.addFreshEntity(treasureFished);

                                //play sound
                                Vec3 p = player.position();
                                level.playSound(null, p.x, p.y, p.z, SoundEvents.VILLAGER_CELEBRATE, SoundSource.AMBIENT, 1f, 1f);

                                //award fish counter
                                List<FishProperties> list = new ArrayList<>(DataAttachments.get(player).fishNotifications());
                                list.add(fbe.fpToFish);
                                DataAttachments.get(player).setFishNotifications(list);

                                //award exp
                                int exp = 4;
                                if (fp.rarity() == FishProperties.Rarity.UNCOMMON) exp = 8;
                                if (fp.rarity() == FishProperties.Rarity.RARE) exp = 12;
                                if (fp.rarity() == FishProperties.Rarity.EPIC) exp = 20;
                                if (fp.rarity() == FishProperties.Rarity.LEGENDARY) exp = 35;
                                if (fbe.hook.is(ModItems.GOLD_HOOK.get()))
                                    exp *= (int) ((double) data.hits / 3) + 1; //extra exp if gold hook is used
                                player.giveExperiencePoints(exp);

                            }
                            else
                            {
                                //if fish minigame failed/canceled, play sound
                                Vec3 p = player.position();
                                level.playSound(null, p.x, p.y, p.z, SoundEvents.VILLAGER_NO, SoundSource.AMBIENT, 1f, 1f);
                            }

                            fbe.kill();
                        }
                    }

                }

                DataAttachments.get(player).setFishing("");

                context.get().setPacketHandled(true);
            });
        }

    }

    //send fish caught toast
    public static class FishCaughtPayload
    {
        private final FishProperties fp;
        private final boolean newFish;
        private final int size;
        private final int weight;

        public FishCaughtPayload(FishProperties fp, boolean newFish, int size, int weight)
        {
            this.fp = fp;
            this.newFish = newFish;
            this.size = size;
            this.weight = weight;
        }

        public static void encode(FishCaughtPayload fishCaughtPayload, FriendlyByteBuf buf)
        {
            buf.writeJsonWithCodec(FishProperties.CODEC, fishCaughtPayload.fp);
            buf.writeBoolean(fishCaughtPayload.newFish);
            buf.writeInt(fishCaughtPayload.size);
            buf.writeInt(fishCaughtPayload.weight);
        }

        public static FishCaughtPayload decode(FriendlyByteBuf buf)
        {
            FishProperties fp = buf.readJsonWithCodec(FishProperties.CODEC);
            boolean newFish = buf.readBoolean();
            int size = buf.readInt();
            int weight = buf.readInt();

            return new FishCaughtPayload(fp, newFish, size, weight);
        }

        public static void handle(FishCaughtPayload data, Supplier<NetworkEvent.Context> context)
        {
            context.get().enqueueWork(() ->
            {
                Starcatcher.fishCaughtToast(data.fp, data.newFish, data.size, data.weight);
            });
            context.get().setPacketHandled(true);
        }
    }
}
