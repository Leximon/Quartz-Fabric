package de.leximon.quartz.api.event.server;

import net.minecraft.server.MinecraftServer;

public class ServerStartedEvent extends ServerEvent {
    public ServerStartedEvent(MinecraftServer server) {
        super(server);
    }
}
