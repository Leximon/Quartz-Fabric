package de.leximon.quartz.api.event.block;

import de.leximon.quartz.api.block.BlockData;
import de.leximon.quartz.api.event.Cancellable;
import de.leximon.quartz.api.event.PlayerInteraction;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Getter
public class BlockBreakEvent extends BlockEvent implements Cancellable, PlayerInteraction {

    private final ServerPlayerEntity player;
    private boolean cancelled = false;

    public BlockBreakEvent(ServerPlayerEntity player, ServerWorld world, BlockState blockState, BlockEntity blockEntity, BlockPos pos) {
        super(new BlockData(world, pos, blockState, blockEntity));
        this.player = player;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
