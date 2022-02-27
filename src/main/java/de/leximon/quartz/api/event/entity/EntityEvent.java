package de.leximon.quartz.api.event.entity;

import de.leximon.quartz.api.event.Event;
import lombok.Getter;
import net.minecraft.entity.Entity;

@Getter
public class EntityEvent extends Event {

    private final Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }
}
