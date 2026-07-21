package com.wdiscute.starcatcher;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Lifecycle;
import com.wdiscute.libtooltips.ExampleRGBEffect;
import com.wdiscute.libtooltips.Tooltips;
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
import com.wdiscute.starcatcher.tooltips.SCLegendary;
import com.wdiscute.starcatcher.tooltips.SCTooltipGradient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLanguageProvider;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Triple;
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
    public static IForgeRegistry<AbstractFishRestriction> FISH_RESTRICTIONS_REGISTRY;

    public static IForgeRegistry<Supplier<AbstractMinigameModifier>> MINIGAME_MODIFIERS_REGISTRY;

    public static IForgeRegistry<Supplier<? extends AbstractSweetSpotBehaviour>> SWEET_SPOT_BEHAVIOUR_REGISTRY;

    public static IForgeRegistry<Supplier<AbstractCatchModifier>> CATCH_MODIFIERS_REGISTRY;

    public static IForgeRegistry<Supplier<AbstractTackleSkin>> TACKLE_SKIN_REGISTRY;

    public static ResourceLocation rl(String s)
    {
        return U.rl(Starcatcher.MOD_ID, s);
    }

    //shitty fix for double toast because its caused by nikdos payload sender thingy
    static Holder<Item> lastToast = null;

    @OnlyIn(Dist.CLIENT)
    public static void fishCaughtToast(FishProperties fp, boolean newFish, int sizeCM, int weightCM)
    {
        if (newFish && !fp.catchInfo().fish().equals(lastToast)) Minecraft.getInstance().getToasts().addToast(new FishCaughtToast(fp));
        lastToast = fp.catchInfo().fish();
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


    public Starcatcher(FMLJavaModLoadingContext ctx)
    {
        IEventBus modEventBus = ctx.getModEventBus();

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
        SCLootModifiers.register(modEventBus);

        ctx.registerConfig(ModConfig.Type.CLIENT, SCConfig.SPEC);
        ctx.registerConfig(ModConfig.Type.SERVER, SCConfig.SPEC_SERVER);

        DistExecutor.safeRunWhenOn(Dist.CLIENT,
                () -> Client::init);

//        SCItems.registerExtra();
    }

    public static class Client
    {
        public static void init()
        {

            //register tooltip tag processors
            Tooltips.registerProcessor("scgolden",
                    (t, s, e) -> SCTooltipGradient.process(t,
                            Triple.of(202, 93, 5),
                            Triple.of(230, 204, 9)
                    ));

            Tooltips.registerProcessor("sclegendary", SCLegendary::process);

            Tooltips.registerProcessor("scepic",
                    (t, s, e) -> SCTooltipGradient.process(t,
                            Triple.of(61, 0, 255),
                            Triple.of(255, 0, 224)
                    ));

            Tooltips.registerProcessor("scrare",
                    (t, s, e) -> SCTooltipGradient.process(t,
                            Triple.of(20, 40, 120),
                            Triple.of(100, 180, 255)
                    ));

            Tooltips.registerProcessor("scuncommon",
                    (t, s, e) -> SCTooltipGradient.process(t,
                            Triple.of(11, 185, 2),
                            Triple.of(2, 185, 69)
                    ));

            Tooltips.registerProcessor("sccommon",
                    (t, s, e) -> Component.literal(t));

            Tooltips.registerProcessor("sctrash",
                    (t, s, e) -> Component.literal(t));

            Tooltips.registerProcessor("sclava",
                    (t, s, e) -> SCTooltipGradient.process(t,
                            Triple.of(197, 11, 11),
                            Triple.of(197, 64, 11)
                    ));

            Tooltips.registerProcessor("scnone",
                    (t, s, e) -> Component.literal(t));
        }
    }

}
