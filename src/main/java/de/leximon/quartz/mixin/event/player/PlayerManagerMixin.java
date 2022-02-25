package de.leximon.quartz.mixin.event.player;

import com.mojang.authlib.GameProfile;
import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.player.PlayerJoinEvent;
import de.leximon.quartz.api.event.player.PlayerLoginEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;
import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow public abstract void broadcast(Text message, MessageType type, UUID sender);

    @Redirect(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private void inject(PlayerManager instance, Text message, MessageType type, UUID sender, ClientConnection connection, ServerPlayerEntity player) {
        PlayerJoinEvent event = new PlayerJoinEvent(player, connection, message);
        Quartz.callEvent(event);
        broadcast(Quartz.adventure().toNative(event.getJoinMessage()), type, sender);
    }

    @Inject(method = "checkCanJoin", at = @At("RETURN"), cancellable = true)
    private void inject(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir) {
        PlayerLoginEvent event = new PlayerLoginEvent(address, profile, cir.getReturnValue());
        Quartz.callEvent(event);
        var reason = event.getReason();
        cir.setReturnValue(reason == null ? null : Quartz.adventure().toNative(reason));
    }

}
