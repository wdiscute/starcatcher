package com.wdiscute.starcatcher.registry.catchmodifiers;

import com.wdiscute.starcatcher.registry.FishProperties;

public class HideCatchModifier extends AbstractCatchModifier
{
    @Override
    public FishProperties overrideFpToClient(FishProperties fishProperties)
    {
        return fishProperties.withHideCatch();
    }
}
