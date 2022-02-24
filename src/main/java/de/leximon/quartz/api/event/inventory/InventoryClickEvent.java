package de.leximon.quartz.api.event.inventory;

import de.leximon.quartz.api.event.Cancellable;
import de.leximon.quartz.api.event.Event;
import de.leximon.quartz.api.event.PlayerInteraction;
import lombok.Getter;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Getter
public class InventoryClickEvent extends Event implements Cancellable, PlayerInteraction {

    public static final int BUTTON_LEFT = 0;
    public static final int BUTTON_RIGHT = 1;
    public static final int BUTTON_MIDDLE = 2;

    private final ServerPlayerEntity player;
    private final int slot;
    private final SlotActionType actionType;
    private final int button;
    private boolean cancelled = false;

    public InventoryClickEvent(ServerPlayerEntity player, int slot, SlotActionType actionType, int button) {
        this.player = player;
        this.slot = slot;
        this.actionType = actionType;
        this.button = button;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public ServerWorld getWorld() {
        return player.getWorld();
    }
}
