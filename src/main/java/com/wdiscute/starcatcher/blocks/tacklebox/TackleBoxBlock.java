package com.wdiscute.starcatcher.blocks.tacklebox;

import com.mojang.serialization.MapCodec;
import com.wdiscute.starcatcher.registry.SCBlockEntities;
import com.wdiscute.starcatcher.registry.SCBlocks;
import com.wdiscute.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredBlock;

import javax.annotation.Nullable;
import java.util.List;

public class TackleBoxBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{
    private static final Component UNKNOWN_CONTENTS = Component.translatable("container.starcatcher.tackle_box.unknownContents");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final ResourceLocation CONTENTS = Utils.rl("minecraft", "contents");
    @javax.annotation.Nullable
    private final DyeColor color;

    private static final VoxelShape NORTH_SOUTH = Block.box(0.5f, 0, 2.5f, 15.5f, 10f, 13.5f);
    private static final VoxelShape EAST_WEST = Block.box(2.5f, 0, 0.5f, 13.5f, 10f, 15.5f);

    public MapCodec<TackleBoxBlock> codec()
    {
        return null;
    }

    public TackleBoxBlock(@javax.annotation.Nullable DyeColor color, MapColor mapColor)
    {
        super(Properties.of()
                .mapColor(mapColor)
                .strength(2.0F)
                .dynamicShape()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY));
        this.color = color;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if(state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH) return NORTH_SOUTH;
        return EAST_WEST;
    }

    @Override
    protected FluidState getFluidState(BlockState state)
    {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new TackleBoxBlockEntity(this.color, pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (!level.isClientSide)
        {
            if (level.getBlockEntity(pos) instanceof TackleBoxBlockEntity tbbe)
            {
                //todo?
                //player.awardStat(Stats.OPEN_SHULKER_BOX);
                player.openMenu(tbbe, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState bs = defaultBlockState();
        bs = bs.setValue(FACING, context.getHorizontalDirection().getOpposite());
        bs = bs.setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
        return bs;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof TackleBoxBlockEntity tbbe)
        {
            if (!level.isClientSide && player.isCreative() && !tbbe.isEmpty())
            {
                ItemStack itemstack = getColoredItemStack(this.getColor());
                itemstack.applyComponents(blockentity.collectComponents());
                ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + (double) 0.5F, (double) pos.getY() + (double) 0.5F, (double) pos.getZ() + (double) 0.5F, itemstack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params)
    {
        BlockEntity blockentity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockentity instanceof TackleBoxBlockEntity tbbe)
        {
            params = params.withDynamicDrop(CONTENTS, (consumer) ->
            {
                for (int i = 0; i < tbbe.getContainerSize(); ++i)
                {
                    consumer.accept(tbbe.getItem(i));
                }

            });
        }

        return super.getDrops(state, params);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!state.is(newState.getBlock()))
        {
            BlockEntity blockentity = level.getBlockEntity(pos);
            super.onRemove(state, level, pos, newState, isMoving);
            if (blockentity instanceof TackleBoxBlockEntity)
            {
                level.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
        }

    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (stack.has(DataComponents.CONTAINER_LOOT))
        {
            tooltipComponents.add(UNKNOWN_CONTENTS);
        }

        int i = 0;
        int j = 0;

        for (ItemStack itemstack : stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems())
        {
            ++j;
            if (i <= 4)
            {
                ++i;
                tooltipComponents.add(Component.translatable("container.starcatcher.tackle_box.itemCount", itemstack.getHoverName(), itemstack.getCount()));
            }
        }

        if (j - i > 0)
        {
            tooltipComponents.add(Component.translatable("container.starcatcher.tackle_box.more", j - i).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
    {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state)
    {
        ItemStack itemstack = super.getCloneItemStack(level, pos, state);
        level.getBlockEntity(pos, SCBlockEntities.TACKLE_BOX.get()).ifPresent((ebbe) -> ebbe.saveToItem(itemstack, level.registryAccess()));
        return itemstack;
    }

    @Nullable
    public static DyeColor getColorFromItem(Item item)
    {
        return getColorFromBlock(Block.byItem(item));
    }

    @Nullable
    public static DyeColor getColorFromBlock(Block block)
    {
        return block instanceof TackleBoxBlock ? ((TackleBoxBlock) block).getColor() : null;
    }

    public static DeferredBlock<Block> getBlockByColor(@Nullable DyeColor color)
    {
        if (color == null)
        {
            return SCBlocks.TACKLE_BOX;
        }
        else
        {
            return switch (color)
            {
                case WHITE -> SCBlocks.TACKLE_BOX_WHITE;
                case ORANGE -> SCBlocks.TACKLE_BOX_ORANGE;
                case MAGENTA ->  SCBlocks.TACKLE_BOX_MAGENTA;
                case LIGHT_BLUE -> SCBlocks.TACKLE_BOX_LIGHT_BLUE;
                case YELLOW -> SCBlocks.TACKLE_BOX_YELLOW;
                case LIME -> SCBlocks.TACKLE_BOX_LIME;
                case PINK -> SCBlocks.TACKLE_BOX_PINK;
                case GRAY -> SCBlocks.TACKLE_BOX_GRAY;
                case LIGHT_GRAY -> SCBlocks.TACKLE_BOX_LIGHT_GRAY;
                case CYAN -> SCBlocks.TACKLE_BOX_CYAN;
                case BLUE -> SCBlocks.TACKLE_BOX_BLUE;
                case BROWN -> SCBlocks.TACKLE_BOX_BROWN;
                case GREEN -> SCBlocks.TACKLE_BOX_GREEN;
                case RED -> SCBlocks.TACKLE_BOX_RED;
                case BLACK -> SCBlocks.TACKLE_BOX_BLACK;
                case PURPLE -> SCBlocks.TACKLE_BOX_PURPLE;
            };
        }
    }

    @Nullable
    public DyeColor getColor()
    {
        return this.color;
    }

    public static ItemStack getColoredItemStack(@Nullable DyeColor color)
    {
        return new ItemStack(getBlockByColor(color));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot)
    {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @org.jetbrains.annotations.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return level.isClientSide() ? null : (level0, pos0, state0, blockEntity) ->
        {
            if(blockEntity instanceof TackleBoxBlockEntity tbbe && tbbe.openCount > 0) tbbe.tick();
        };
    }
}
