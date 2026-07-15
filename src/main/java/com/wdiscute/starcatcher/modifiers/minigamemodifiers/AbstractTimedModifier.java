package com.wdiscute.starcatcher.modifiers.minigamemodifiers;

import com.wdiscute.starcatcher.minigame.FishingMinigameScreen;

public abstract class AbstractTimedModifier extends AbstractInstancedMinigameModifier
{
    public int length;
    public int tickCount;

    public AbstractTimedModifier(int length)
    {
        this.length = length;
    }

    public int getLength()
    {
        return length;
    }

    @Override
    public void tick(FishingMinigameScreen instance)
    {
        tickCount++;
        if (length > 0 && tickCount >= length)
        {
            removed = true;
        }
        super.tick(instance);
    }
}
