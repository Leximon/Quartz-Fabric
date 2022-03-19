package de.leximon.quartz.api.event.inventory;

import de.leximon.quartz.api.event.Cancellable;
import de.leximon.quartz.api.event.PlayerInteraction;
import lombok.Getter;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

@Getter
public class InventoryClickEvent extends InventoryEvent implements Cancellable, PlayerInteraction {

    public static final int BUTTON_LEFT = 0;
    public static final int BUTTON_RIGHT = 1;
    public static final int BUTTON_MIDDLE = 2;

    private final int slot;
    private final SlotActionType actionType;
    private final int button;
    private final Inventory clickedInventory;
    private boolean cancelled = false;

    public InventoryClickEvent(ServerPlayerEntity player, ScreenHandler screenHandler, int slot, SlotActionType actionType, int button) {
        super(player, screenHandler);
        this.slot = slot;
        this.actionType = actionType;
        this.button = button;
        this.clickedInventory = slot != -999 ? screenHandler.getSlot(slot).inventory : null;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
