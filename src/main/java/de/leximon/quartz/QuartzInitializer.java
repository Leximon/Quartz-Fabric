package de.leximon.quartz;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.item.QBlockItem;
import de.leximon.quartz.api.scheduler.Scheduler;
import de.leximon.quartz.testing.AmogusBlock;
import de.leximon.quartz.testing.ExampleBlock;
import de.leximon.quartz.testing.ExampleBlockEntity;
import de.leximon.quartz.testing.TestListener;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.kyori.adventure.text.Component;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzInitializer implements DedicatedServerModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("quartz");

    private static FabricServerAudiences adventure;

    public static FabricServerAudiences adventure() {
        if (adventure == null)
            throw new IllegalStateException("Tried to access Adventure without a running server!");
        return adventure;
    }

    public static final AmogusBlock AMOGUS_BLOCK = new AmogusBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
    public static final ExampleBlock FLAMETHROWER = new ExampleBlock(FabricBlockSettings.copyOf(Blocks.DISPENSER));
    public static BlockEntityType<ExampleBlockEntity> EXAMPLE_BLOCK_ENTITY;

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> adventure = FabricServerAudiences.of(server));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> adventure = null);
        ServerTickEvents.START_SERVER_TICK.register(server -> Quartz.getScheduler().tick(server));

        Quartz.registerBlock(id("amogus"), AMOGUS_BLOCK);
        Quartz.registerBlock(id("flamethrower"), FLAMETHROWER);
        EXAMPLE_BLOCK_ENTITY = Quartz.registerBlockEntity(id("flamethrower"), ExampleBlockEntity::new, FLAMETHROWER);

        Quartz.registerItem(id("flamethrower"), new QBlockItem(FLAMETHROWER, Items.OBSERVER, Component.text("Flamethrower")));
        Quartz.registerItem(id("amogus"), new QBlockItem(AMOGUS_BLOCK, Items.REDSTONE_BLOCK, Component.text("Suspicious Block")));

        Quartz.registerEvents(TestListener.class);


    }

    public static Identifier id(String name) {
        return new Identifier("test", name);
    }
}
