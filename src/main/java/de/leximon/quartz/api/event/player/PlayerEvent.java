package de.leximon.quartz.api.event.player;

import de.leximon.quartz.api.event.Event;
import de.leximon.quartz.api.event.PlayerInteraction;
import lombok.Getter;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Getter
public class PlayerEvent extends Event implements PlayerInteraction {

    private final ServerPlayerEntity player;

    public PlayerEvent(ServerPlayerEntity player) {
        this.player = player;
    }

    public ServerWorld getWorld() {
        return this.player.getWorld();
    }
}
