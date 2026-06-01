package com.wdiscute.starcatcher.registry.tackleskin;

import com.wdiscute.starcatcher.registry.SCSounds;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class KingTackleSkin extends AbstractTackleSkin
{
    @Override
    public boolean skipMissSound()
    {
        return true;
    }

    @Override
    public boolean skipSuccessSound()
    {
        return true;
    }

    @Override
    public void onMissed(Player player)
    {
        super.onMissed(player);
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.KING_GRR.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onSuccessfulMinigame(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.KING_HEHEHA.get(), SoundSource.NEUTRAL, 1f, 1f);
    }

    @Override
    public void onFailedMinigame(Player player)
    {
        Vec3 p = player.position();
        player.level().playSound(null, p.x, p.y, p.z, SCSounds.KING_CRY.get(), SoundSource.NEUTRAL, 1f, 1f);
    }
}
