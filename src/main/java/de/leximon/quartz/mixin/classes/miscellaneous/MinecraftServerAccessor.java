package de.leximon.quartz.mixin.classes.miscellaneous;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor {

    @Accessor("worlds")
    Map<RegistryKey<World>, ServerWorld> getWorldMap();

    @Accessor
    Executor getWorkerExecutor();

    @Accessor
    LevelStorage.Session getSession();

    @Accessor
    WorldGenerationProgressListenerFactory getWorldGenerationProgressListenerFactory();

}
