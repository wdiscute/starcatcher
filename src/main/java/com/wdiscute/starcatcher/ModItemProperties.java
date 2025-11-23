package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.networkandcodecs.DataAttachments;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModItemProperties
{

    private static final Logger log = LoggerFactory.getLogger(ModItemProperties.class);

    public static void addCustomItemProperties()
    {
        ItemProperties.register(
                ModItems.ROD.get(),
                Starcatcher.rl("cast"),
                (stack, level, entity, seed) ->
                {
                    //todo fix this on dedicated server, doesnt work because client doesnt have the same list of players fishing uuids
                    if (entity == null) return 0.0f;

                    if (entity instanceof Player player)
                    {
                        return !DataAttachments.get(player).fishing().isEmpty() && (entity.getMainHandItem() == stack || (entity.getOffhandItem() == stack)) ? 1.0f : 0.0f;
                    }

                    return 1.0f;
                }
        );
    }


}
