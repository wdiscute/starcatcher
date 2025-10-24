package com.wdiscute.starcatcher.networkandcodecs;

import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.bob.FishingBobEntity;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
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

                        //create itemStacks
                        ItemStack is = new ItemStack(fbe.fpToFish.fish());
                        ItemStack treasure = new ItemStack(BuiltInRegistries.ITEM.get(fbe.fpToFish.dif().treasure().loot()));

                        //assign custom name if fish has one
                        if (!fp.customName().isEmpty())
                            is.set(DataComponents.ITEM_NAME, Component.literal(fp.customName()));

                        //store fish properties in itemstack
                        is.set(ModDataComponents.FISH_PROPERTIES, fp);

                        //split hook double drops
                        if(data.perfectCatch() && fbe.hook.is(ModItems.SPLIT_HOOK)) is.setCount(2);

                        //make ItemEntities for fish and treasure
                        ItemEntity itemFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, is);
                        ItemEntity treasureFished = new ItemEntity(level, fbe.position().x, fbe.position().y + 1.2f, fbe.position().z, treasure);

                        //assign delta movement so fish flies towards player
                        double x = Math.clamp((player.position().x - fbe.position().x) / 25, -1, 1);
                        double y = Math.clamp((player.position().y - fbe.position().y) / 20, -1, 1);
                        double z = Math.clamp((player.position().z - fbe.position().z) / 25, -1, 1);
                        Vec3 vec3 = new Vec3(x, 0.7 + y, z);
                        itemFished.setDeltaMovement(vec3);
                        treasureFished.setDeltaMovement(vec3);

                        //add itemEntities to level
                        level.addFreshEntity(itemFished);
                        if(data.completedTreasure()) level.addFreshEntity(treasureFished);

                        //play sound
                        Vec3 p = player.position();
                        level.playSound(null, p.x, p.y, p.z, SoundEvents.VILLAGER_CELEBRATE, SoundSource.AMBIENT);

                        //award fish counter
                        if (FishCaughtCounter.AwardFishCaughtCounter(fbe.fpToFish, player, data.time()) && player instanceof ServerPlayer sp)
                            PacketDistributor.sendToPlayer(sp, new Payloads.FishCaughtPayload(fp));

                        //award fish counter
                        List<FishProperties> list = new ArrayList<>(player.getData(ModDataAttachments.FISHES_NOTIFICATION));
                        list.add(fbe.fpToFish);
                        player.setData(ModDataAttachments.FISHES_NOTIFICATION, list);

                        //award exp
                        int exp = 4;
                        if(fp.rarity() == FishProperties.Rarity.UNCOMMON) exp = 8;
                        if(fp.rarity() == FishProperties.Rarity.RARE) exp = 12;
                        if(fp.rarity() == FishProperties.Rarity.EPIC) exp = 20;
                        if(fp.rarity() == FishProperties.Rarity.LEGENDARY) exp = 35;
                        if(fbe.hook.is(ModItems.GOLD_HOOK)) exp *= (int) ((double) data.hits() / 3) + 1; //extra exp if gold hook is used
                        player.giveExperiencePoints(exp);

                        //todo trigger item fished event
                        //ItemFishedEvent event = new ItemFishedEvent(List.of(is), 0, null);
                        //NeoForge.EVENT_BUS.post(event);

                    }
                    else
                    {
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
