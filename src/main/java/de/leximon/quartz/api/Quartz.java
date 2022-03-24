package de.leximon.quartz.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.leximon.quartz.QuartzInitializer;
import de.leximon.quartz.api.event.Event;
import de.leximon.quartz.api.event.EventHandler;
import de.leximon.quartz.api.scheduler.Scheduler;
import de.leximon.quartz.api.world.WorldBuilder;
import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@UtilityClass
public class Quartz {

    private static final Scheduler SCHEDULER = new Scheduler();
    private static final Multimap<Class<?>, Method> listeners = HashMultimap.create();

    public static void registerBlock(Identifier id, Block block) {
        Registry.register(Registry.BLOCK, id, block);
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Identifier id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(factory, blocks).build(null));
    }

    public static void registerItem(Identifier id, Item item) {
        Registry.register(Registry.ITEM, id, item);
    }

    public static void registerEvents(Class<?> listener) {
        for (Method method : listener.getMethods()) {
            if(!method.isAnnotationPresent(EventHandler.class))
                continue;
            var parameterTypes = method.getParameterTypes();
            if(!Modifier.isStatic(method.getModifiers()))
                throw new RuntimeException("Event handler must be static");
            if(parameterTypes.length != 1)
                throw new RuntimeException("Event handler must have one parameter");
//            if(!parameterTypes[0].is(Event.class))
//                throw new RuntimeException("The first parameter of the event handler is not a assignable of Event");
            listeners.put(parameterTypes[0], method);
        }
    }

    public static void callEvent(Event event) {
        for (Method method : listeners.get(event.getClass())) {
            try {
                method.invoke(null, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static WorldBuilder registerWorld(Identifier id) {
        return new WorldBuilder(id);
    }

    public static ServerScoreboard createNewScoreboard() {
        return new ServerScoreboard(getServer());
    }

    public static ServerScoreboard getMainScoreboard() {
        return getServer().getScoreboard();
    }

    public static FabricServerAudiences adventure() {
        return QuartzInitializer.getAdventure();
    }

    public static Scheduler getScheduler() {
        return SCHEDULER;
    }

    public static MinecraftServer getServer() {
        return QuartzInitializer.getServer();
    }

}
