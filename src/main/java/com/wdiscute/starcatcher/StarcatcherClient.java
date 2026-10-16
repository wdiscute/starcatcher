package com.wdiscute.starcatcher;

import com.wdiscute.libtooltips.Tooltips;
import com.wdiscute.starcatcher.fishentity.SCArmPoses;
import com.wdiscute.starcatcher.tooltips.SCLegendary;
import com.wdiscute.starcatcher.tooltips.SCTooltipGradient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.apache.commons.lang3.tuple.Triple;

public class StarcatcherClient
{
    public static final IClientItemExtensions HOLD_ABOVE_HEAD = new IClientItemExtensions()
    {
        @Override
        public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack)
        {
            return SCArmPoses.HOLD_ABOVE_HEAD;
        }
    };

    public StarcatcherClient()
    {
        //modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        SCArmPoses.register();

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

        Tooltips.registerProcessor("scnone",
                (t, s, e) -> Component.literal(t));

        Tooltips.registerProcessor("sclava",
                (t, s, e) -> SCTooltipGradient.process(t,
                        Triple.of(219, 91, 41),
                        Triple.of(219, 129, 41)
                ));
    }

    public static Level getLevel()
    {
        return Minecraft.getInstance().level;
    }
}
