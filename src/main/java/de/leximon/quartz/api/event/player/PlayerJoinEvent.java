package de.leximon.quartz.api.event.player;

import de.leximon.quartz.api.Quartz;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Getter
public class PlayerJoinEvent extends PlayerEvent {

    @Setter private Component joinMessage;

    public PlayerJoinEvent(ServerPlayerEntity player, Text text) {
        super(player);
        this.joinMessage = Quartz.adventure().toAdventure(text);
    }
}
