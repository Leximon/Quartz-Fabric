package de.leximon.quartz;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.server.ServerStartedEvent;
import de.leximon.quartz.api.event.server.ServerStartingEvent;
import de.leximon.quartz.api.event.server.ServerStoppedEvent;
import de.leximon.quartz.api.event.server.ServerStoppingEvent;
import de.leximon.quartz.api.inventory.HandledInventory;
import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzInitializer implements DedicatedServerModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("quartz");

    @Getter private static FabricServerAudiences adventure;
    @Getter private static MinecraftServer server;

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> Quartz.callEvent(new ServerStartedEvent(server)));
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            QuartzInitializer.adventure = FabricServerAudiences.of(server);
            QuartzInitializer.server = server;
            Quartz.callEvent(new ServerStartingEvent(server));
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            adventure = null;
            Quartz.callEvent(new ServerStoppedEvent(server));
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> Quartz.callEvent(new ServerStoppingEvent(server)));
        ServerTickEvents.START_SERVER_TICK.register(server -> Quartz.getScheduler().tick(server));

        Quartz.registerEvents(HandledInventory.class);
    }

    public static Identifier id(String name) {
        return new Identifier("test", name);
    }
}
