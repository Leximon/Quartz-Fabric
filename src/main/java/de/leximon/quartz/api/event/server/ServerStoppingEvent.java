package de.leximon.quartz.api.event.server;

import net.minecraft.server.MinecraftServer;

public class ServerStoppingEvent extends ServerEvent {
    public ServerStoppingEvent(MinecraftServer server) {
        super(server);
    }
}
