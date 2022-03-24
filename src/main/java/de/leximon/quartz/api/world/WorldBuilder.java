package de.leximon.quartz.api.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.mixin.classes.miscellaneous.MinecraftServerAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;

import javax.annotation.CheckReturnValue;
import java.util.HashSet;
import java.util.Map;
import java.util.OptionalLong;
import java.util.Set;

public class WorldBuilder {

    private final Identifier id;
    private RegistryKey<DimensionType> dimensionType = DimensionType.OVERWORLD_REGISTRY_KEY;
    private Set<Identifier> biomes;
    private ChunkGenerator chunkGenerator;
    private OptionalLong seed = OptionalLong.empty();

    public WorldBuilder(Identifier id) {
        this.id = id;
        this.biomes = getAllRegistryEntries(Registry.BIOME_KEY);
    }

    @SafeVarargs
    protected static <T> Set<Identifier> getAllRegistryEntries(RegistryKey<Registry<T>> registry, RegistryKey<T>... disallowed) {
        MinecraftServer server = Quartz.getServer();
        DynamicRegistryManager.Immutable registryManager = server.getRegistryManager();

        var entries = registryManager.get(registry).getEntrySet();
        Set<Identifier> structures = new HashSet<>();

        registry:
        for (Map.Entry<RegistryKey<T>, T> entry : entries) {
            Identifier id = entry.getKey().getValue();
            for (RegistryKey<T> structure : disallowed)
                if(id.equals(structure.getValue()))
                    continue registry;
            structures.add(id);
        }
        return structures;
    }

    @CheckReturnValue
    public WorldBuilder dimensionType(RegistryKey<DimensionType> dimensionType) {
        this.dimensionType = dimensionType;
        return this;
    }

    @CheckReturnValue
    public WorldBuilder dimensionType(Identifier dimensionType) {
        this.dimensionType = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, dimensionType);
        return this;
    }

    @SafeVarargs
    @CheckReturnValue
    public final WorldBuilder biomesAllowed(RegistryKey<Biome>... biomes) {
        this.biomes.clear();
        for (RegistryKey<Biome> structure : biomes)
            this.biomes.add(structure.getValue());
        return this;
    }

    @SafeVarargs
    @CheckReturnValue
    public final WorldBuilder biomesDisallowed(RegistryKey<Biome>... biomes) {
        this.biomes = getAllRegistryEntries(Registry.BIOME_KEY, biomes);
        return this;
    }

    @CheckReturnValue
    public WorldBuilder chunkGenerator(ChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
        return this;
    }

    @CheckReturnValue
    public WorldBuilder seed(long seed) {
        this.seed = OptionalLong.of(seed);
        return this;
    }

    public ServerWorld register() {
        MinecraftServer server = Quartz.getServer();
        MinecraftServerAccessor serverAccess = (MinecraftServerAccessor) server;
        DynamicRegistryManager.Immutable registryManager = server.getRegistryManager();

        // allowed biomes
        SimpleRegistry<Biome> biomes = new SimpleRegistry<>(Registry.BIOME_KEY, Lifecycle.experimental(), null);
        for (Map.Entry<RegistryKey<Biome>, Biome> entry : registryManager.get(Registry.BIOME_KEY).getEntrySet())
            if (this.biomes.contains(entry.getKey().getValue()))
                biomes.add(entry.getKey(), entry.getValue(), Lifecycle.experimental());

        DimensionOptions options = new DimensionOptions(
                registryManager.get(Registry.DIMENSION_TYPE_KEY).getOrCreateEntry(dimensionType),
                chunkGenerator
        );

        RegistryKey<World> registryKey2 = RegistryKey.of(Registry.WORLD_KEY, id);
        RegistryEntry<DimensionType> registryEntry2 = options.getDimensionTypeSupplier();
        ChunkGenerator chunkGenerator2 = options.getChunkGenerator();
        UnmodifiableLevelProperties unmodifiableLevelProperties = new UnmodifiableLevelProperties(server.getSaveProperties(), server.getSaveProperties().getMainWorldProperties());

        ServerWorld serverWorld2 = new ServerWorld(server, serverAccess.getWorkerExecutor(), serverAccess.getSession(), unmodifiableLevelProperties,
                registryKey2, registryEntry2, serverAccess.getWorldGenerationProgressListenerFactory().create(11), chunkGenerator2, false,
                seed.orElseGet(() -> server.getOverworld().getSeed()), ImmutableList.of(), false);
        server.getOverworld().getWorldBorder().addListener(new WorldBorderListener.WorldBorderSyncer(serverWorld2.getWorldBorder()));
        serverAccess.getWorldMap().put(registryKey2, serverWorld2);

        return serverWorld2;
    }
}
