package com.wdiscute.starcatcher.data.network;

import com.wdiscute.starcatcher.SCTags;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.SCDataMaps;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.utils.MaybeStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record CBFishingStartedPayload(FishProperties fp, MaybeStack treasure,
                                      MaybeStack rod) implements CustomPacketPayload
{

    public static final Type<CBFishingStartedPayload> TYPE = new Type<>(Starcatcher.rl("fishing_started"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBFishingStartedPayload> STREAM_CODEC = StreamCodec.composite(
            FishProperties.STREAM_CODEC, CBFishingStartedPayload::fp,
            MaybeStack.STREAM_CODEC, CBFishingStartedPayload::treasure,
            MaybeStack.STREAM_CODEC, CBFishingStartedPayload::rod,
            CBFishingStartedPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }


    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() -> client(this, context));
    }

    @OnlyIn(Dist.CLIENT)
    public static void client(CBFishingStartedPayload data, IPayloadContext context)
    {
        //get rod
        ItemStack maybeRod = context.player().getMainHandItem().is(SCTags.RODS) ? context.player().getMainHandItem() : context.player().getOffhandItem();

        //get tackle skin, backup of default from registry
        AbstractTackleSkin tackleSkin = SCDataMaps.getOrDefault(maybeRod, SCDataMaps.TACKLE_SKIN, Starcatcher.TACKLE_SKIN_REGISTRY.get(Starcatcher.rl("rod")));

        //start minigame
        Minecraft.getInstance().setScreen(new FishingMinigameScreen(data.fp(), data.treasure.toStack(), List.of(), tackleSkin));
    }
}
