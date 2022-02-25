package de.leximon.quartz.mixin.event.player;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.player.PlayerQuitEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow @Final private MinecraftServer server;
    @Shadow public ServerPlayerEntity player;
    @Shadow @Final public ClientConnection connection;

    /**
     * PlayerQuitEvent
     */
    @Redirect(method = "onDisconnected", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    private void inject(PlayerManager instance, Text message, MessageType type, UUID sender, Text reason) {
        PlayerQuitEvent event = new PlayerQuitEvent(player, connection, message, reason.toString());
        Quartz.callEvent(event);
        server.getPlayerManager().broadcast(Quartz.adventure().toNative(event.getQuitMessage()), type, sender);
    }

}
