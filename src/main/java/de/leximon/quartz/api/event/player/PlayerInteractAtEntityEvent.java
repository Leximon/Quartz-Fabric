package de.leximon.quartz.api.event.player;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

@Getter
public class PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {

    private final Vec3d pos;

    public PlayerInteractAtEntityEvent(ServerPlayerEntity player, Entity entity, Hand hand, Vec3d pos) {
        super(player, entity, hand);
        this.pos = pos;
    }
}
