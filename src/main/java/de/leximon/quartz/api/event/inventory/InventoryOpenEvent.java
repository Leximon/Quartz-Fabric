package de.leximon.quartz.api.event.inventory;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class InventoryOpenEvent extends InventoryEvent {

    public InventoryOpenEvent(ServerPlayerEntity player, ScreenHandler screenHandler) {
        super(player, screenHandler);
    }

}
