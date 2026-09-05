package com.wdiscute.starcatcher;

import com.mojang.logging.LogUtils;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.SCFishRestrictions;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.SCSweetSpotsBehaviour;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.AbstractSweetSpotBehaviour;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.sellingbin.SCProcessors;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.tooltips.SCLegendary;
import com.wdiscute.starcatcher.tooltips.SCTooltipGradient;
import com.wdiscute.utils.Utils;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.nikdo53.neobackports.registry.ForgeRegistryHelper;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;

import java.text.DecimalFormat;
import java.util.function.Supplier;

@Mod(Starcatcher.MOD_ID)
public class Starcatcher
{
    public static final String MOD_ID = "starcatcher";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation MISSINGNO = rl("missingno");
    public static final ResourceLocation BASE = rl("base");
    public static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    //resource keys
    public static final ResourceKey<Registry<FishProperties>> FISH_REGISTRY_KEY =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish"));

    public static final ResourceKey<Registry<AbstractFishRestriction>> FISH_RESTRICTIONS =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish_restrictions"));

    public static final ResourceKey<Registry<Supplier<? extends AbstractSweetSpotBehaviour>>> SWEETSPOT_BEHAVIOUR =
            ResourceKey.createRegistryKey(Starcatcher.rl("sweetspot_behaviour"));

    public static final ResourceKey<Registry<AbstractTackleSkin>> TACKLE_SKIN =
            ResourceKey.createRegistryKey(Starcatcher.rl("tackle_skin"));

    //registry
    public static IForgeRegistry<AbstractFishRestriction> FISH_RESTRICTIONS_REGISTRY;

    public static IForgeRegistry<Supplier<? extends AbstractSweetSpotBehaviour>> SWEETSPOT_BEHAVIOUR_REGISTRY;

    public static IForgeRegistry<AbstractTackleSkin> TACKLE_SKIN_REGISTRY;


    public static ResourceLocation rl(String s)
    {
        return Utils.rl(Starcatcher.MOD_ID, s);
    }

    public Starcatcher()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext modContainer = ModLoadingContext.get();

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
        SCTackleSkins.register(modEventBus);
        SCProcessors.register(modEventBus);
        //SCLootModifiers.register(modEventBus);
        SCStats.register(modEventBus);
        SCAttributes.register(modEventBus);
        SCDataEntries.register(modEventBus);
        SCCriterionTriggers.register(modEventBus);
        SCFeatures.register(modEventBus);

        Modifier.registerCatch();
        Modifier.registerMinigame();

        modContainer.registerConfig(ModConfig.Type.CLIENT, SCConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, SCConfig.SPEC_SERVER);

        //register mod-specific fishes
        SCItems.registerExtraItems();

        if(FMLLoader.getDist().isClient())
            new StarcatcherClient();
    }
}
