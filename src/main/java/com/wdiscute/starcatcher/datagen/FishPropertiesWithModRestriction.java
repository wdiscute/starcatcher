package com.wdiscute.starcatcher.datagen;

import com.wdiscute.starcatcher.networkandcodecs.FishProperties;

public record FishPropertiesWithModRestriction
        (
                FishProperties fp,
                String modid
        )
{




}
