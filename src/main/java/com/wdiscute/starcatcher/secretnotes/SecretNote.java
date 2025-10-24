package com.wdiscute.starcatcher.secretnotes;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.ModItems;
import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.networkandcodecs.ModDataComponents;
import com.wdiscute.starcatcher.networkandcodecs.TrophyProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

public class SecretNote extends Item
{
    public SecretNote()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if(level.isClientSide) openScreen(player.getItemInHand(usedHand).get(ModDataComponents.SECRET_NOTE));
        return super.use(level, player, usedHand);
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen(Note note)
    {
        Minecraft.getInstance().setScreen(new SecretNoteScreen(note));
    }

    public enum Note implements StringRepresentable
    {
        CRYSTAL_HOOK("crystal_hook"),
        LAVA_NOTE("lava_note"),
        NOTE_1("note_1");

        public static final Codec<Note> CODEC = StringRepresentable.fromEnum(Note::values);
        public static final StreamCodec<FriendlyByteBuf, Note> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Note.class);
        private final String key;

        Note(String key)
        {
            this.key = key;
        }

        public @NotNull String getSerializedName()
        {
            return this.key;
        }
    }


}


