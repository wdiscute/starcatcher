package com.wdiscute.starcatcher.compat.curios;

import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.registry.SCItems;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CuriosEvents {

    public static void registerRenderers() {
        SCBlocks.HATS.getEntries().forEach(block ->
                CuriosRendererRegistry.register(
                        SCItems.ITEMS.getEntries().stream()
                                .filter(i -> i.getId().getPath().equals(block.getId().getPath()))
                                .findFirst().orElseThrow().get(),
                        CurioHatRenderer::new
                )
        );
    }
}
