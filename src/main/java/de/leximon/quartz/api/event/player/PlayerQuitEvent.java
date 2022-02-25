package de.leximon.quartz.api.event.player;

import de.leximon.quartz.api.Quartz;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Getter
public class PlayerQuitEvent extends PlayerEvent {

    private final ClientConnection clientConnection;
    private final String reason;
    @Setter private Component quitMessage;

    public PlayerQuitEvent(ServerPlayerEntity player, ClientConnection clientConnection, Text quitMessage, String reason) {
        super(player);
        this.clientConnection = clientConnection;
        this.reason = reason;
        this.quitMessage = Quartz.adventure().toAdventure(quitMessage);
    }
}
