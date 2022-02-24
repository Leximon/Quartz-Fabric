package de.leximon.quartz.mixin.event.player;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.player.PlayerInteractAtEntityEvent;
import de.leximon.quartz.api.event.player.PlayerInteractEntityEvent;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/server/network/ServerPlayNetworkHandler$1")
public class ServerPlayNetworkHandlerMixin {

    @Shadow @Final ServerPlayNetworkHandler field_28963;
    @Shadow @Final Entity field_28962;

    private PlayerInteractEntityEvent event;

    @Inject(method = "interact", at = @At("HEAD"))
    private void inject(Hand hand, CallbackInfo ci) {
        event = new PlayerInteractEntityEvent(field_28963.player, field_28962, hand);
        Quartz.callEvent(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "interactAt", at = @At("HEAD"))
    private void inject(Hand hand, Vec3d pos, CallbackInfo ci) {
        event = new PlayerInteractAtEntityEvent(field_28963.player, field_28962, hand, pos);
        Quartz.callEvent(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "processInteract", at = @At("HEAD"), cancellable = true)
    private void inject(Hand hand, ServerPlayNetworkHandler.Interaction action, CallbackInfo ci) {
    }

}
