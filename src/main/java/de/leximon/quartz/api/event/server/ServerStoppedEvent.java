package de.leximon.quartz.api.event.server;

import net.minecraft.server.MinecraftServer;

public class ServerStoppedEvent extends ServerEvent {
    public ServerStoppedEvent(MinecraftServer server) {
        super(server);
    }
}
