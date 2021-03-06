package de.leximon.quartz.api.event.player;

import de.leximon.quartz.api.Quartz;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Getter
public class PlayerJoinEvent extends PlayerEvent {

    private final ClientConnection clientConnection;
    @Setter private Component joinMessage;

    public PlayerJoinEvent(ServerPlayerEntity player, ClientConnection clientConnection, Text text) {
        super(player);
        this.clientConnection = clientConnection;
        this.joinMessage = Quartz.adventure().toAdventure(text);
    }
}
