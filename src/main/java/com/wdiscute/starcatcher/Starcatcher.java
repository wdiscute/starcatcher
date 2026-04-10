package com.wdiscute.starcatcher;

import com.mojang.logging.LogUtils;
import com.wdiscute.starcatcher.registry.FishProperties.SizeAndWeight.Units;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.SCFishRestrictions;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.starcatcher.registry.catchmodifiers.AbstractCatchModifier;
import com.wdiscute.starcatcher.registry.catchmodifiers.SCCatchModifiers;
import com.wdiscute.starcatcher.registry.minigamemodifiers.SCMinigameModifiers;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.SCSweetSpotsBehaviour;
import com.wdiscute.starcatcher.blocks.SCBlockEntities;
import com.wdiscute.starcatcher.blocks.SCBlocks;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import com.wdiscute.starcatcher.io.*;
import com.wdiscute.starcatcher.registry.minigamemodifiers.AbstractMinigameModifier;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.AbstractSweetSpotBehaviour;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.sellingbin.SCProcessors;
import com.wdiscute.starcatcher.registry.FishProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(Starcatcher.MOD_ID)
public class Starcatcher
{
    public static final String MOD_ID = "starcatcher";
    public static final Logger LOGGER = LogUtils.getLogger();

    //resource keys
    public static final ResourceKey<Registry<FishProperties>> FISH_REGISTRY_KEY =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish"));

    public static final ResourceKey<Registry<AbstractFishRestriction>> FISH_RESTRICTIONS =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish_restrictions"));

    public static final ResourceKey<Registry<Supplier<AbstractMinigameModifier>>> MINIGAME_MODIFIERS =
            ResourceKey.createRegistryKey(Starcatcher.rl("minigame_modifiers"));

    public static final ResourceKey<Registry<Supplier<? extends AbstractSweetSpotBehaviour>>> SWEET_SPOT_BEHAVIOUR =
            ResourceKey.createRegistryKey(Starcatcher.rl("sweet_spot_behaviour"));

    public static final ResourceKey<Registry<Supplier<AbstractCatchModifier>>> CATCH_MODIFIERS =
            ResourceKey.createRegistryKey(Starcatcher.rl("catch_modifiers"));

    public static final ResourceKey<Registry<Supplier<AbstractTackleSkin>>> TACKLE_SKIN =
            ResourceKey.createRegistryKey(Starcatcher.rl("bobber_skin"));

    //registry
    public static final Registry<AbstractFishRestriction> FISH_RESTRICTIONS_REGISTRY = new RegistryBuilder<>(FISH_RESTRICTIONS)
            .sync(true)
            .defaultKey(Starcatcher.rl("empty"))
            .create();

    public static final Registry<Supplier<AbstractMinigameModifier>> MINIGAME_MODIFIERS_REGISTRY = new RegistryBuilder<>(MINIGAME_MODIFIERS)
            .sync(true)
            .defaultKey(Starcatcher.rl("slower_vanishing"))
            .create();

    public static final Registry<Supplier<? extends AbstractSweetSpotBehaviour>> SWEET_SPOT_BEHAVIOUR_REGISTRY = new RegistryBuilder<>(SWEET_SPOT_BEHAVIOUR)
            .sync(true)
            .defaultKey(Starcatcher.rl("normal"))
            .create();

    public static final Registry<Supplier<AbstractCatchModifier>> CATCH_MODIFIERS_REGISTRY = new RegistryBuilder<>(CATCH_MODIFIERS)
            .sync(true)
            .defaultKey(Starcatcher.rl("decrease_lure_time"))
            .create();

    public static final Registry<Supplier<AbstractTackleSkin>> TACKLE_SKIN_REGISTRY = new RegistryBuilder<>(TACKLE_SKIN)
            .sync(true)
            .defaultKey(Starcatcher.rl("pearl"))
            .create();

    public static ResourceLocation rl(String s)
    {
        return ResourceLocation.fromNamespaceAndPath(Starcatcher.MOD_ID, s);
    }

    @OnlyIn(Dist.CLIENT)
    public static void fishCaughtToast(FishProperties fp, boolean newFish, int sizeCM, int weightCM)
    {
        if (newFish) Minecraft.getInstance().getToasts().addToast(new FishCaughtToast(fp));

        Units units = SCConfig.UNIT.get();

        String size = units.getSizeAsString(sizeCM);
        String weight = units.getWeightAsString(weightCM);

        Minecraft.getInstance().player.displayClientMessage(
                Component.literal("")
                        .append(Component.translatable(fp.catchInfo().fish().value().getDescriptionId()))
                        .append(Component.literal(" - " + size + " - " + weight))
                , true);

        Minecraft.getInstance().gui.overlayMessageTime = 180;
    }


    public Starcatcher(IEventBus modEventBus, ModContainer modContainer)
    {
        SCCreativeModeTabs.register(modEventBus);

        SCItems.register(modEventBus);
        SCBlocks.register(modEventBus);
        SCBlockEntities.register(modEventBus);
        SCDataComponents.register(modEventBus);
        SCSounds.register(modEventBus);
        SCEntities.register(modEventBus);
        SCParticles.register(modEventBus);
        SCRecipes.register(modEventBus);
        SCMenuTypes.register(modEventBus);
        SCDataAttachments.register(modEventBus);
        SCSweetSpotsBehaviour.register(modEventBus);
        SCFishRestrictions.register(modEventBus);
        SCMinigameModifiers.register(modEventBus);
        SCCatchModifiers.register(modEventBus);
        SCTackleSkins.register(modEventBus);
        SCCriterionTriggers.register(modEventBus);
        SCProcessors.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.CLIENT, SCConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, SCConfig.SPEC_SERVER);

//        SCItems.registerExtra();
    }

    @Mod(value = Starcatcher.MOD_ID, dist = Dist.CLIENT)
    public static class Client
    {
        public Client(ModContainer modContainer)
        {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }
}
