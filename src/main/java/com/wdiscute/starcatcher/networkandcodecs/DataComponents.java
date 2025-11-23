package com.wdiscute.starcatcher.networkandcodecs;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.wdiscute.starcatcher.items.ColorfulBobber;
import com.wdiscute.starcatcher.secretnotes.SecretNote;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

public class DataComponents
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public enum Slots implements StringRepresentable
    {
        BOBBER("bobber"),
        BAIT("bait"),
        HOOK("hook");

        private final String key;

        Slots(String key)
        {
            this.key = key;
        }

        public String getSerializedName()
        {
            return this.key;
        }
    }


    //secret note data component
    public static void setSecretNote(ItemStack is, SecretNote.Note note)
    {
        is.getOrCreateTag().putString("secret_note", note.getSerializedName());
    }

    public static SecretNote.Note getSecretNote(ItemStack is)
    {
        if(is.hasTag())
            if(is.getTag().get("secret_note") != null)
            {
                return SecretNote.Note.getBySerializedName(is.getTag().getString("secret_note"));
            }

        return SecretNote.Note.SAMPLE_NOTE;
    }


    //fish properties data component
    public static FishProperties getFishProperties(ItemStack is)
    {
        if (is.hasTag())
        {
            if (is.getOrCreateTag().contains("fish_properties"))
                return FishProperties.CODEC.decode(NbtOps.INSTANCE, is.getTag().get("fish_properties")).result().get().getFirst();
        }
        return FishProperties.DEFAULT;
    }

    public static void setFishProperties(ItemStack is, FishProperties fp)
    {
        CompoundTag compoundTag = new CompoundTag();

        FishProperties.CODEC.encode(fp, NbtOps.INSTANCE, new CompoundTag())
                .resultOrPartial(LOGGER::warn).ifPresent(tag -> compoundTag.put("fish_properties", tag));

        is.getOrCreateTag().put("fish_properties", compoundTag);
    }


    //trophy properties data component
    public static TrophyProperties getTrophyProperties(ItemStack is)
    {
        if (is.hasTag())
        {
            if (is.getTag().contains("trophy_properties"))
            {
                CompoundTag trophyProperties = is.getOrCreateTag().getCompound("trophy_properties");
                DataResult<TrophyProperties> decode = TrophyProperties.CODEC.parse(NbtOps.INSTANCE, trophyProperties);
                return decode.result().orElse(TrophyProperties.DEFAULT);
            }
        }

        return TrophyProperties.DEFAULT;
    }

    public static void setTrophyProperties(ItemStack is, TrophyProperties tp)
    {
        TrophyProperties.CODEC.encode(tp, NbtOps.INSTANCE, new CompoundTag())
                .resultOrPartial(LOGGER::warn).ifPresent(tag -> is.getOrCreateTag().put("trophy_properties", tag));
    }

    public static void setSizeAndWeight(ItemStack is, SizeAndWeight sw)
    {
        is.getOrCreateTag().putInt("starcatcher_size", sw.sizeInCentimeters());
        is.getOrCreateTag().putInt("starcatcher_weight", sw.weightInGrams());
    }

    public static SizeAndWeight getSizeAndWeight(ItemStack is)
    {
        if (is.hasTag())
        {
            if (is.getTag().contains("starcatcher_size"))
            {
                return new SizeAndWeight(
                        is.getTag().getInt("starcatcher_size"),
                        is.getTag().getInt("starcatcher_weight"));
            }
        }

        return SizeAndWeight.DEFAULT;

    }


    //bobber color data component
    public static void setBobberColor(ItemStack is, ColorfulBobber.BobberColor bobberColor)
    {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("starcatcher_r", bobberColor.r());
        tag.putFloat("starcatcher_g", bobberColor.g());
        tag.putFloat("starcatcher_b", bobberColor.b());
        is.getOrCreateTag().put("starcatcher_bobber_color", tag);
    }

    public static ColorfulBobber.BobberColor getBobberColor(ItemStack is)
    {
        if (is.hasTag())
        {
            CompoundTag compound = is.getTag().getCompound("starcatcher_bobber_color");

            return new ColorfulBobber.BobberColor(
                    compound.getFloat("starcatcher_r"),
                    compound.getFloat("starcatcher_g"),
                    compound.getFloat("starcatcher_b")
            );
        }
        return ColorfulBobber.BobberColor.DEFAULT;
    }


    //bobber, bait, hook data component
    public static void setItemInSlot(ItemStack is, Slots slotToSave, ItemStack stackToSave)
    {
        CompoundTag tag = new CompoundTag();
        stackToSave.save(tag);
        is.getOrCreateTag().put(slotToSave.getSerializedName(), tag);
    }

    public static ItemStack getItemInSlot(ItemStack is, Slots slotToGet)
    {
        if (is.hasTag())
        {
            CompoundTag compound = is.getTag().getCompound(slotToGet.getSerializedName());
            return ItemStack.of(compound);
        }
        return ItemStack.EMPTY;
    }
}
