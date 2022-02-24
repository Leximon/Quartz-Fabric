package de.leximon.quartz.api.event.player;

import de.leximon.quartz.api.event.Cancellable;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

@Getter
public class PlayerInteractEntityEvent extends PlayerEvent implements Cancellable {

    private final Entity entity;
    private final Hand hand;
    private boolean cancelled;

    public PlayerInteractEntityEvent(ServerPlayerEntity player, Entity entity, Hand hand) {
        super(player);
        this.entity = entity;
        this.hand = hand;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
