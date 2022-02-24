package de.leximon.quartz.api.block;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Getter
public class BlockData {

    private final ServerWorld world;
    private final BlockPos pos;
    private BlockState blockState;
    private BlockEntity blockEntity;

    public BlockData(ServerWorld world, BlockPos pos, BlockState blockState, BlockEntity blockEntity) {
        this.world = world;
        this.pos = pos;
        this.blockState = blockState;
        this.blockEntity = blockEntity;
    }

    public void setBlockState(BlockState state) {
        this.setBlockState(state, Block.NOTIFY_ALL);
    }

    public void setBlockState(BlockState state, int flags) {
        this.setBlockState(state, flags, 512);
    }

    public void setBlockState(BlockState state, int flags, int maxUpdateDepth) {
        this.world.setBlockState(this.pos, state, flags, maxUpdateDepth);
        this.blockState = state;
        this.blockEntity = this.world.getBlockEntity(this.pos);
    }

    public boolean hasBlockEntity() {
        return this.blockEntity != null;
    }

    public Block getBlock() {
        return blockState.getBlock();
    }
}
