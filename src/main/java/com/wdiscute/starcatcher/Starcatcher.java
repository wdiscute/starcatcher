package com.wdiscute.starcatcher;

import com.mojang.logging.LogUtils;
import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.fish.SizeAndWeight;
import com.wdiscute.starcatcher.modifiers.Modifier;
import com.wdiscute.starcatcher.registry.fishrestrictions.AbstractFishRestriction;
import com.wdiscute.starcatcher.registry.fishrestrictions.SCFishRestrictions;
import com.wdiscute.starcatcher.registry.tackleskin.AbstractTackleSkin;
import com.wdiscute.starcatcher.registry.tackleskin.SCTackleSkins;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.SCSweetSpotsBehaviour;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.guide.FishCaughtToast;
import com.wdiscute.starcatcher.registry.sweetspotbehaviour.AbstractSweetSpotBehaviour;
import com.wdiscute.starcatcher.registry.*;
import com.wdiscute.starcatcher.sellingbin.SCProcessors;
import com.wdiscute.starcatcher.fish.FishProperties;
import com.wdiscute.starcatcher.tooltips.SCLegendary;
import com.wdiscute.starcatcher.tooltips.SCTooltipGradient;
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
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(Starcatcher.MOD_ID)
public class Starcatcher
{
    public static final String MOD_ID = "starcatcher";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation MISSINGNO = rl("missingno");
    public static final ResourceLocation BASE = rl("base");

    //resource keys
    public static final ResourceKey<Registry<FishProperties>> FISH_REGISTRY_KEY =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish"));

    public static final ResourceKey<Registry<AbstractFishRestriction>> FISH_RESTRICTIONS =
            ResourceKey.createRegistryKey(Starcatcher.rl("fish_restrictions"));

    public static final ResourceKey<Registry<Supplier<? extends AbstractSweetSpotBehaviour>>> SWEETSPOT_BEHAVIOUR =
            ResourceKey.createRegistryKey(Starcatcher.rl("sweetspot_behaviour"));

    public static final ResourceKey<Registry<AbstractTackleSkin>> TACKLE_SKIN =
            ResourceKey.createRegistryKey(Starcatcher.rl("bobber_skin"));

    //registry
    public static final Registry<AbstractFishRestriction> FISH_RESTRICTIONS_REGISTRY = new RegistryBuilder<>(FISH_RESTRICTIONS)
            .sync(true)
            .defaultKey(Starcatcher.rl("empty"))
            .create();

    public static final Registry<Supplier<? extends AbstractSweetSpotBehaviour>> SWEETSPOT_BEHAVIOUR_REGISTRY = new RegistryBuilder<>(SWEETSPOT_BEHAVIOUR)
            .sync(true)
            .defaultKey(Starcatcher.rl("normal"))
            .create();

    public static final Registry<AbstractTackleSkin> TACKLE_SKIN_REGISTRY = new RegistryBuilder<>(TACKLE_SKIN)
            .sync(true)
            .defaultKey(Starcatcher.BASE)
            .create();

    public static ResourceLocation rl(String s)
    {
        return ResourceLocation.fromNamespaceAndPath(Starcatcher.MOD_ID, s);
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
        SCTackleSkins.register(modEventBus);
        SCProcessors.register(modEventBus);
        SCLootModifiers.register(modEventBus);
        SCStats.register(modEventBus);
        SCAttributes.register(modEventBus);
        SCDataEntries.register(modEventBus);
        SCCriterionTriggers.register(modEventBus);

        Modifier.registerCatch();
        Modifier.registerMinigame();

        modContainer.registerConfig(ModConfig.Type.CLIENT, SCConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, SCConfig.SPEC_SERVER);

        //register mod-specific fishes
        SCItems.registerExtraItems();
    }

    @Mod(value = Starcatcher.MOD_ID, dist = Dist.CLIENT)
    public static class Client
    {
        public Client(ModContainer modContainer)
        {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

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

            Tooltips.registerProcessor("sclava",
                    (t, s, e) -> SCTooltipGradient.process(t,
                            Triple.of(219, 91, 41),
                            Triple.of(219, 129, 41)
                    ));
        }
    }
}
