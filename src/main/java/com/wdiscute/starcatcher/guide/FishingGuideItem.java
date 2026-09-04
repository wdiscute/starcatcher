package com.wdiscute.starcatcher.guide;

import com.wdiscute.starcatcher.data.FishCaughtCounter;
import com.wdiscute.starcatcher.data.network.CBPlayerStructuresPayload;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.starcatcher.blocks.display.DisplayBlock;
import com.wdiscute.starcatcher.blocks.display.DisplayBlockEntity;
import com.wdiscute.starcatcher.registry.SCDataAttachments;
import com.wdiscute.starcatcher.registry.SCDataComponents;
import com.wdiscute.starcatcher.data.SignedGuide;
import com.wdiscute.starcatcher.registry.SCStats;
import com.wdiscute.utils.Utils;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.nikdo53.neobackports.io.networking.PacketDistributorNeo;

import java.time.Instant;
import java.util.*;

public class FishingGuideItem extends Item
{
    public FishingGuideItem(Properties p)
    {
        super(p.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        //if clicked on empty lectern
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(clickedPos);
        if (blockState.is(Blocks.LECTERN))
        {
            if (!blockState.getValue(LecternBlock.HAS_BOOK))
            {
                level.setBlockAndUpdate(clickedPos, SCBlocks.DISPLAY.get().defaultBlockState()
                        .setValue(DisplayBlock.HAS_ITEM, true)
                        .setValue(BlockStateProperties.WATERLOGGED, false)
                );

                if (level.getBlockEntity(clickedPos) instanceof DisplayBlockEntity dbe)
                {
                    dbe.setItem(context.getItemInHand().consumeAndReturn(1, context.getPlayer()));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
    {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide)
        {
            if (SCDataComponents.has(player.getItemInHand(usedHand), SCDataComponents.SIGNED_GUIDE))
                FishingGuideScreen.open(BlockPos.ZERO, SCDataComponents.get(player.getItemInHand(usedHand), SCDataComponents.SIGNED_GUIDE));
            else
                FishingGuideScreen.open(BlockPos.ZERO, null);
        }
        else
        {
            List<ResourceLocation> structures = new ArrayList<>();
            ServerLevel sl = (ServerLevel) level;
            StructureManager manager = sl.structureManager();
            Set<Structure> allStructuresOnBlockPos = manager.getAllStructuresAt(player.blockPosition()).keySet();
            Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

            for (Structure structure : allStructuresOnBlockPos)
                structures.add(registry.getKey(structure));

            PacketDistributorNeo.sendToPlayer((ServerPlayer) player, new CBPlayerStructuresPayload(structures));

            level.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS);


            //return if not signed
            if (!SCDataComponents.has(stack, SCDataComponents.SIGNED_GUIDE))
                return InteractionResultHolder.success(stack);

            SignedGuide signedGuide = SCDataComponents.get(stack, SCDataComponents.SIGNED_GUIDE);

            //if owner opening guide
            if (signedGuide.owner().equals(player.getUUID()))
            {
                Map<ResourceLocation, FishCaughtCounter> map = SCDataAttachments.get(player, SCDataAttachments.FISHING_GUIDE).fishesCaught;

                Map<ResourceLocation, FishCaughtCounter> mapToSave = new HashMap<>();
                map.forEach((rl, fcc) -> mapToSave.put(rl, fcc.removeNotification()));

                if (player instanceof ServerPlayer sp)
                {
                    FishingGuideScreen.StatsData statsData = new FishingGuideScreen.StatsData(
                            sp.getStats().getValue(Stats.CUSTOM.get(SCStats.TICKS_SPENT_FISHING.get())),
                            sp.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_TREASURES.get())),
                            sp.getStats().getValue(Stats.CUSTOM.get(SCStats.STARCAUGHT_FISH_MISSED.get())),
                            sp.getStats().getValue(Stats.CUSTOM.get(SCStats.BAIT_USED.get()))
                    );

                    SCDataComponents.set(stack, SCDataComponents.SIGNED_GUIDE,
                            new SignedGuide(
                                    player.getUUID(),
                                    mapToSave,
                                    signedGuide.signature(),
                                    Date.from(Instant.now()).getTime(),
                                    statsData,
                                    signedGuide.visitors()
                            ));
                }
            }
            //visitor opening guide
            else
            {
                HashSet<Utils.Duo<UUID, String>> visitors = new HashSet<>(signedGuide.visitors());
                visitors.add(new Utils.Duo<>(player.getUUID(), player.getScoreboardName()));
                SCDataComponents.set(stack, SCDataComponents.SIGNED_GUIDE, signedGuide.withVisitors(List.copyOf(visitors)));
            }
        }
        return InteractionResultHolder.success(stack);
    }
}
