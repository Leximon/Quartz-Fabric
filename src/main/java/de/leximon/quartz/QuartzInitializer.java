package de.leximon.quartz;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.server.ServerStartedEvent;
import de.leximon.quartz.api.event.server.ServerStartingEvent;
import de.leximon.quartz.api.event.server.ServerStoppedEvent;
import de.leximon.quartz.api.event.server.ServerStoppingEvent;
import de.leximon.quartz.api.inventory.HandledInventory;
import de.leximon.quartz.testing.AmogusBlock;
import de.leximon.quartz.testing.ExampleBlock;
import de.leximon.quartz.testing.ExampleBlockEntity;
import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzInitializer implements DedicatedServerModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("quartz");

    @Getter private static FabricServerAudiences adventure;
    @Getter private static MinecraftServer server;


    public static final AmogusBlock AMOGUS_BLOCK = new AmogusBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
    public static final ExampleBlock FLAMETHROWER = new ExampleBlock(FabricBlockSettings.copyOf(Blocks.DISPENSER));
    public static BlockEntityType<ExampleBlockEntity> EXAMPLE_BLOCK_ENTITY;

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

//        Quartz.registerBlock(id("amogus"), AMOGUS_BLOCK);
//        Quartz.registerBlock(id("flamethrower"), FLAMETHROWER);
//        EXAMPLE_BLOCK_ENTITY = Quartz.registerBlockEntity(id("flamethrower"), ExampleBlockEntity::new, FLAMETHROWER);
//
//        Quartz.registerItem(id("flamethrower"), new QBlockItem(FLAMETHROWER, Items.OBSERVER, Component.text("Flamethrower")));
//        Quartz.registerItem(id("amogus"), new AmogusBlockItem(AMOGUS_BLOCK));
//
//        Quartz.registerEvents(TestListener.class);

    }

    public static Identifier id(String name) {
        return new Identifier("test", name);
    }
}
