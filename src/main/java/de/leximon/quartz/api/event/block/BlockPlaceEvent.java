package de.leximon.quartz.api.event.block;

import de.leximon.quartz.api.block.BlockData;
import de.leximon.quartz.api.event.Cancellable;
import de.leximon.quartz.api.event.PlayerInteraction;
import lombok.Getter;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;

@Getter
public class BlockPlaceEvent extends BlockEvent implements Cancellable, PlayerInteraction {

    private final ServerPlayerEntity player;
    private final ItemPlacementContext placementContext;
    private final BlockData placedBlockData, replacedBlockData;
    private boolean cancelled = false;

    public BlockPlaceEvent(ServerPlayerEntity player, ItemPlacementContext placementContext, BlockData placedBlockData, BlockData replacedBlockData) {
        super(placedBlockData);
        this.placementContext = placementContext;
        this.placedBlockData = placedBlockData;
        this.replacedBlockData = replacedBlockData;
        this.player = player;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
