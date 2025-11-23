package com.wdiscute.starcatcher.secretnotes;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.networkandcodecs.DataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

public class SecretNote extends Item
{
    public SecretNote()
    {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        if (level.isClientSide) openScreen(DataComponents.getSecretNote(player.getItemInHand(usedHand)));
        return super.use(level, player, usedHand);
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen(Note note)
    {
        Minecraft.getInstance().setScreen(new SecretNoteScreen(note));
    }

    public enum Note implements StringRepresentable
    {
        SAMPLE_NOTE("sample_note"),
        CRYSTAL_HOOK("crystal_hook"),
        ARNWULF_1("lava_proof_bottle_1"),
        ARNWULF_2("lava_proof_bottle_2"),
        HOPEFUL_NOTE("hopeful_note"),
        HOPELESS_NOTE("hopeless_note"),
        TRUE_BLUE("true_blue");

        public static final Codec<Note> CODEC = StringRepresentable.fromEnum(Note::values);
        //public static final StreamCodec<FriendlyByteBuf, Note> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Note.class);
        private final String key;

        Note(String key)
        {
            this.key = key;
        }

        public String getSerializedName()
        {
            return this.key;
        }

        public static Note getBySerializedName(String s)
        {
            return Arrays.stream(Note.values()).filter(n -> n.getSerializedName().equals(s)).findFirst().orElse(SAMPLE_NOTE);
        }

    }


}


