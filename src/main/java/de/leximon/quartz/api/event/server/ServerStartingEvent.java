package de.leximon.quartz.api.event.server;

import net.minecraft.server.MinecraftServer;

public class ServerStartingEvent extends ServerEvent {
    public ServerStartingEvent(MinecraftServer server) {
        super(server);
    }
}
