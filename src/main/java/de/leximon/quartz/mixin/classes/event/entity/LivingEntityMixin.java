package de.leximon.quartz.mixin.classes.event.entity;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.entity.EntityDeathEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "drop", at = @At("HEAD"))
    private void inject(DamageSource source, CallbackInfo ci) {
        EntityDeathEvent event = new EntityDeathEvent((LivingEntity) (Object) this, source);
        Quartz.callEvent(event);
    }

}
