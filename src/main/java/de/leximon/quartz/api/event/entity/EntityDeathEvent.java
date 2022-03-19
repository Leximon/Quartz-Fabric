package de.leximon.quartz.api.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;

public class EntityDeathEvent extends EntityEvent {

    public EntityDeathEvent(Entity entity, DamageSource damageSource) {
        super(entity);
    }

}
