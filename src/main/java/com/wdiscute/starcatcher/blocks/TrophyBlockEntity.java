package com.wdiscute.starcatcher.blocks;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.wdiscute.starcatcher.networkandcodecs.TrophyProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class TrophyBlockEntity extends BlockEntity
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public TrophyProperties trophyProperties;

    public TrophyBlockEntity(BlockPos pPos, BlockState pBlockState)
    {
        super(ModBlockEntities.TROPHY.get(), pPos, pBlockState);
    }

    public void setTrophyProperties(TrophyProperties trophyProperties)
    {
        this.trophyProperties = trophyProperties;
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);

        if(this.trophyProperties == null) this.trophyProperties = TrophyProperties.DEFAULT;

        TrophyProperties.CODEC.encode(this.trophyProperties, NbtOps.INSTANCE, tag)
                .resultOrPartial(LOGGER::warn).ifPresent(tag1 -> tag.put("trophy_properties", tag1));
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);

        if (tag.contains("trophy_properties"))
        {
            CompoundTag trophyProperties = tag.getCompound("trophy_properties");
            DataResult<TrophyProperties> decode = TrophyProperties.CODEC.parse(NbtOps.INSTANCE, trophyProperties);
            this.trophyProperties = decode.result().orElse(TrophyProperties.DEFAULT);
        }
    }
}
