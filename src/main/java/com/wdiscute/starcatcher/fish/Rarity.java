package com.wdiscute.starcatcher.fish;

import com.mojang.serialization.Codec;
import com.wdiscute.starcatcher.data.CaughtFishInfo;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum Rarity implements StringRepresentable
{
    NONE("none", 0),
    TRASH("trash", 0),
    COMMON("common", 4),
    UNCOMMON("uncommon", 8),
    RARE("rare", 12),
    EPIC("epic", 20),
    LEGENDARY("legendary", 0),
    GOLDEN("golden", 0);

    public static final Codec<Rarity> CODEC = StringRepresentable.fromEnum(Rarity::values);
    public static final StreamCodec<FriendlyByteBuf, Rarity> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(Rarity.class);
    private final String key;
    private final int xp;

    Rarity(String key, int xp)
    {
        this.key = key;
        this.xp = xp;
    }

    public Component wrapWithRarityMarkdown(String s)
    {
        return Component.literal("<sc" + getSerializedName() + ">" + s + "</sc" + getSerializedName() + ">");
    }

    public String wrapWithRarityMarkdownAsString(String s)
    {
        return "<sc" + getSerializedName() + ">" + s + "</sc" + getSerializedName() + ">";
    }

    public String getSerializedName()
    {
        return this.key;
    }

    public int getId()
    {
        return this.ordinal();
    }

    public int getXp()
    {
        return xp;
    }

    public static boolean isGolden(ItemStack stack)
    {
        if (stack.has(SCDataComponents.CAUGHT_FISH_INFO))
        {
            CaughtFishInfo caughtFishInfo = stack.get(SCDataComponents.CAUGHT_FISH_INFO);
            return caughtFishInfo != null && caughtFishInfo.rarity().equals(GOLDEN);
        }
        return false;
    }
}
