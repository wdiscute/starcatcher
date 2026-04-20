package com.wdiscute.starcatcher.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.Tournament;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;

public class TournamentSavedData extends SavedData
{
    public static final Codec<List<Tournament>> CODEC = Tournament.CODEC.listOf();

    public static final SavedDataType<TournamentSavedData> ID = new SavedDataType<>(
            Starcatcher.rl("tournaments"),
            TournamentSavedData::new,
            _ ->
                    RecordCodecBuilder.create(instance -> instance.group(
                            Tournament.CODEC.listOf().fieldOf("tournaments").forGetter(o -> o.tournaments)
                    ).apply(instance, TournamentSavedData::new))
    );

    private List<Tournament> tournaments = new ArrayList<>();

    public TournamentSavedData(List<Tournament> tournaments)
    {
        this.tournaments = tournaments;
    }

    public TournamentSavedData(ServerLevel serverLevel)
    {
    }

    public static TournamentSavedData get(ServerLevel level)
    {
        return level.getDataStorage().computeIfAbsent(ID);
    }

    public List<Tournament> getTournaments()
    {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments)
    {
        this.tournaments = tournaments;
        setDirty();
    }
}
