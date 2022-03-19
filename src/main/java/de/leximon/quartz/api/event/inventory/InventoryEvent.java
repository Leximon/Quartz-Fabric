package de.leximon.quartz.api.event.inventory;

import de.leximon.quartz.api.event.Event;
import lombok.Getter;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Getter
public class InventoryEvent extends Event {

    private final ServerPlayerEntity player;
    private final ScreenHandler screenHandler;

    public InventoryEvent(ServerPlayerEntity player, ScreenHandler screenHandler) {
        this.player = player;
        this.screenHandler = screenHandler;
    }

    public ServerWorld getWorld() {
        return player.getWorld();
    }
}
