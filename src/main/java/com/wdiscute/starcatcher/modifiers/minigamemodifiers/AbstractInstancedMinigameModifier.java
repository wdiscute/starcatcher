package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.modifiers.Modifier;
import net.minecraft.resources.ResourceLocation;

/**
 * This class should be used only for minigame modifiers created during runtime and not from codec!
 * Unlike codecs modifiers which are always the same modifier instance,
 * modifiers under this class should be created as a new instance each time and can not be serialized as they do not have a codec.
 */
public abstract class AbstractInstancedMinigameModifier extends AbstractMinigameModifier
{
    protected AbstractInstancedMinigameModifier()
    {
        super("");
    }

    @Override
    public ResourceLocation getIdentifier()
    {
        return null;
    }

    @Override
    public MapCodec<? extends Modifier> getCodec()
    {
        return null;
    }
}
