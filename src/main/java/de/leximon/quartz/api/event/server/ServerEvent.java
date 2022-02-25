package de.leximon.quartz.api.event.server;

import de.leximon.quartz.api.event.Event;
import lombok.Getter;
import net.minecraft.server.MinecraftServer;

@Getter
public class ServerEvent extends Event {

    private final MinecraftServer server;

    public ServerEvent(MinecraftServer server) {
        this.server = server;
    }
}
