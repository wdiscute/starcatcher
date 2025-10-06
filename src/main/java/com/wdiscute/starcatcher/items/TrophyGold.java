package com.wdiscute.starcatcher.items;

import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.networkandstuff.FishCaughtCounter;
import com.wdiscute.starcatcher.networkandstuff.FishProperties;
import com.wdiscute.starcatcher.networkandstuff.ModDataAttachments;
import com.wdiscute.starcatcher.networkandstuff.Payloads;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class TrophyGold extends Item
{
    public TrophyGold(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        //sets all fps on fishes caught to 1
        List<FishCaughtCounter> fishCounter = new ArrayList<>();
        List<FishProperties> fishes = new ArrayList<>();

        for (FishProperties fp : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            fishCounter.add(new FishCaughtCounter(fp, 1, Integer.MAX_VALUE, 99999));
        }

        player.setData(ModDataAttachments.FISHES_CAUGHT, fishCounter);

        for (FishProperties fp : level.registryAccess().registryOrThrow(Starcatcher.FISH_REGISTRY))
        {
            fishes.add(fp);
        }

        player.setData(ModDataAttachments.FISHES_NOTIFICATION, fishes);

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
