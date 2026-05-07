package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.Starcatcher;
import net.minecraft.resources.Identifier;

public class BaseTackleSkin extends AbstractTackleSkin
{
    @Override
    public Identifier getName()
    {
        return Starcatcher.rl("base");
    }
}
