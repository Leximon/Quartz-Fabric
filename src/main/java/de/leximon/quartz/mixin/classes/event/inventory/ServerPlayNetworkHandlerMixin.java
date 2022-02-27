package de.leximon.quartz.mixin.classes.event.inventory;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.inventory.InventoryClickEvent;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    /**
     * InventoryClickEvent
     */
    @Inject(method = "onClickSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;disableSyncing()V", shift = At.Shift.BEFORE), cancellable = true)
    private void inject(ClickSlotC2SPacket packet, CallbackInfo ci) {
        InventoryClickEvent event = new InventoryClickEvent(player, packet.getSlot(), packet.getActionType(), packet.getButton());
        Quartz.callEvent(event);
        if(event.isCancelled()) {
            player.currentScreenHandler.updateToClient();
            ci.cancel();
        }
    }

}
