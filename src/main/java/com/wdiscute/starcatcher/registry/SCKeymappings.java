package com.wdiscute.starcatcher.registry;

import com.mojang.blaze3d.platform.InputConstants;
import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public interface SCKeymappings
{
    KeyMapping.Category CATEGORY = new KeyMapping.Category(Starcatcher.rl("starcatcher"));

    KeyMapping MINIGAME_HIT = new KeyMapping("key.starcatcher.minigame_hit", InputConstants.KEY_SPACE, CATEGORY);

    KeyMapping EXPAND_TOURNAMENT = new KeyMapping(
            "key.starcatcher.expand_tournament",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYBOARD,
            InputConstants.KEY_TAB,
            CATEGORY
    );

    KeyMapping OPEN_GUIDE = new KeyMapping(
            "key.starcatcher.open_guide",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYBOARD,
            InputConstants.UNKNOWN.getValue(),
            CATEGORY
    );

}
