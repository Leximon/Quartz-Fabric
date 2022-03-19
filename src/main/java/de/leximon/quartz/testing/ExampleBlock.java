package de.leximon.quartz.testing;

import de.leximon.quartz.QuartzInitializer;
import de.leximon.quartz.api.block.ServersideBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ExampleBlock extends BlockWithEntity implements ServersideBlock {
    public ExampleBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(Properties.FACING, Direction.NORTH)
                .with(Properties.POWERED, false));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(Properties.FACING)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ExampleBlockEntity example) {
            player.openHandledScreen(example);
            player.incrementStat(Stats.INSPECT_DISPENSER);
        }
        player.swingHand(Hand.MAIN_HAND, true);
        return ActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock()))
            return;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ExampleBlockEntity example) {
            ItemScatterer.spawn(world, pos, example);
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockState getDisplayState(BlockState originalState) {
        return Blocks.OBSERVER.getDefaultState()
                .with(Properties.POWERED, originalState.get(Properties.POWERED))
                .with(Properties.FACING, originalState.get(Properties.FACING).getOpposite());
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isClient) {
            return;
        }
        boolean bl = state.get(Properties.POWERED);
        if (bl != world.isReceivingRedstonePower(pos)) {
            if (bl) {
                world.createAndScheduleBlockTick(pos, this, 4);
            } else {
                world.setBlockState(pos, state.cycle(Properties.POWERED), Block.NOTIFY_LISTENERS);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(Properties.POWERED).booleanValue() && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(Properties.POWERED), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING, Properties.POWERED);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExampleBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(world.isClient)
            return null;
        return state.get(Properties.POWERED).booleanValue() ? ExampleBlock.checkType(type, QuartzInitializer.EXAMPLE_BLOCK_ENTITY, ExampleBlockEntity::serverTick) : null;
    }
}
