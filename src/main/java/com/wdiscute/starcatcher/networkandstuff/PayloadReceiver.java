package com.wdiscute.starcatcher.networkandstuff;

import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fishingbob.FishingBobEntity;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public class PayloadReceiver
{
    public static void receiveFishingCompletedServer(final Payloads.FishingCompletedPayload data, final IPayloadContext context)
    {

        Player player = context.player();
        ServerLevel level = ((ServerLevel) context.player().level());

        List<Entity> entities = level.getEntities(null, new AABB(-25, -65, -25, 25, 65, 25).move(player.position()));

        for (Entity entity : entities)
        {
            if (entity.getUUID().toString().equals(player.getData(ModDataAttachments.FISHING.get())))
            {
                if (entity instanceof FishingBobEntity fbe)
                {
                    if (data.time() != -1)
                    {
                        FishProperties fp = fbe.fpToFish;

                        //MAKE THIS DATA DRIVEN
//                        if (fbe.stack.is(ModItems.THUNDERCHARGED_EEL))
//                        {
//                            LightningBolt strike = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
//                            strike.setPos(fbe.position());
//                            strike.setVisualOnly(true);
//                            level.addFreshEntity(strike);
//                        }

                        ItemStack is = new ItemStack(BuiltInRegistries.ITEM.get(fbe.fpToFish.fish()));

                        if (!fp.customName().isEmpty())
                            is.set(DataComponents.CUSTOM_NAME, Component.translatable("item.starcatcher." + fp.customName()));


                        Entity itemFished = new ItemEntity(
                                level,
                                fbe.position().x,
                                fbe.position().y + 1.2f,
                                fbe.position().z,
                                is);

                        double x = (player.position().x - fbe.position().x) / 25;
                        double y = (player.position().y - fbe.position().y) / 20;
                        double z = (player.position().z - fbe.position().z) / 25;

                        x = Math.clamp(x, -1, 1);
                        y = Math.clamp(y, -1, 1);
                        z = Math.clamp(z, -1, 1);

                        Vec3 vec3 = new Vec3(x, 0.7 + y, z);

                        itemFished.setDeltaMovement(vec3);

                        level.addFreshEntity(itemFished);

                        Vec3 p = player.position();
                        level.playSound(null, p.x, p.y, p.z, SoundEvents.VILLAGER_CELEBRATE, SoundSource.AMBIENT);


                        //award fish counter
                        if (FishCaughtCounter.AwardFishCaughtCounter(fbe.fpToFish, player) && player instanceof ServerPlayer sp)
                        {
                            PacketDistributor.sendToPlayer(sp, new Payloads.FishCaughtPayload(fp));
                        }

                        List<FishProperties> list = new ArrayList<>(level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY).stream().toList());

                        list.add(fbe.fpToFish);

                        player.setData(ModDataAttachments.FISHES_NOTIFICATION, list);
                    }
                    else
                    {
                        Vec3 p = player.position();
                        level.playSound(null, p.x, p.y, p.z, SoundEvents.VILLAGER_NO, SoundSource.AMBIENT);
                    }

                    fbe.kill();
                }
            }
        }

        player.setData(ModDataAttachments.FISHING.get(), "");


    }


    public static void receiveFPsSeen(final Payloads.FPsSeen data, final IPayloadContext context)
    {
        List<FishProperties> list = context.player().getData(ModDataAttachments.FISHES_NOTIFICATION);
        List<FishProperties> newList = new ArrayList<>();

        for(FishProperties fp : list)
        {
            if(!data.fps().contains(fp))
                newList.add(fp);
        }

        context.player().setData(ModDataAttachments.FISHES_NOTIFICATION, newList);
    }

    public static void receiveFishCaught(final Payloads.FishCaughtPayload data, final IPayloadContext context)
    {
        Starcatcher.fishCaughtToast(data.fp());
    }


    public static void receiveFishingClient(final Payloads.FishingPayload data, final IPayloadContext context)
    {
        client(data, context);
    }

    @OnlyIn(Dist.CLIENT)
    public static void client(Payloads.FishingPayload data, IPayloadContext context)
    {
        Minecraft.getInstance().setScreen(new FishingMinigameScreen(data.fp(), data.rod()));
    }
}
