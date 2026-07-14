package com.wdiscute.starcatcher.compat;

import com.wdiscute.starcatcher.registry.SCItems;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

public interface CreateCompat
{
    DeferredItem<Item> COGGILL = SCItems.registerNonBucketFish("coggill");
    DeferredItem<Item> MECHANICAL_BRASS_SNAIL = SCItems.registerNonBucketFish("mechanical_brass_snail");
    DeferredItem<Item> MECHANICAL_SNAIL = SCItems.registerNonBucketFish("mechanical_snail");
    DeferredItem<Item> PHILLIPSFISH = SCItems.registerNonBucketFish("phillipsfish");
    DeferredItem<Item> VALVE = SCItems.registerNonBucketFish("valve");
    DeferredItem<Item> PIPEHEAD = SCItems.registerNonBucketFish("pipehead");
    DeferredItem<Item> COGTOPUS = SCItems.registerNonBucketFish("cogtopus");
    DeferredItem<Item> EEL_DYNAMO = SCItems.registerNonBucketFish("eel_dynamo");
    DeferredItem<Item> DRIVE_PIKE = SCItems.registerNonBucketFish("drive_pike");
    DeferredItem<Item> BRASSGILL = SCItems.registerNonBucketFish("brassgill");
    DeferredItem<Item> MEKA_AGAVE_BREAM = SCItems.registerNonBucketFish("meka_agave_bream");

    List<DeferredItem<Item>> CREATE_COMPAT_FISH = List.of(
            COGGILL,
            MECHANICAL_BRASS_SNAIL,
            MECHANICAL_SNAIL,
            PHILLIPSFISH,
            VALVE,
            PIPEHEAD,
            COGTOPUS,
            EEL_DYNAMO,
            DRIVE_PIKE,
            BRASSGILL,
            MEKA_AGAVE_BREAM
    );

    static void register()
    {
    }
}
