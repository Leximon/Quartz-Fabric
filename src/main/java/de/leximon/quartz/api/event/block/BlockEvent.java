package de.leximon.quartz.api.event.block;

import de.leximon.quartz.api.block.BlockData;
import de.leximon.quartz.api.event.Event;
import lombok.Getter;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Getter
public class BlockEvent extends Event {

    private final BlockData blockData;

    public BlockEvent(BlockData blockData) {
        this.blockData = blockData;
    }

    public ServerWorld getWorld() {
        return this.blockData.getWorld();
    }

    public BlockPos getPos() {
        return this.blockData.getPos();
    }
}
