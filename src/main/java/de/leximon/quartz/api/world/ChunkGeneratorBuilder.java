package de.leximon.quartz.api.world;

import com.mojang.serialization.Lifecycle;
import de.leximon.quartz.api.Quartz;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import javax.annotation.CheckReturnValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ChunkGeneratorBuilder {

    private ChunkGeneratorBuilder() { }

    public ChunkGenerator build() { return null; }

    public static class Flat extends ChunkGeneratorBuilder {

        public static Flat builder() {
            return new Flat();
        }

        private RegistryKey<Biome> biome = BiomeKeys.PLAINS;
        private FlatChunkGeneratorLayer[] layers = new FlatChunkGeneratorLayer[] {
                new FlatChunkGeneratorLayer(1, Blocks.BEDROCK),
                new FlatChunkGeneratorLayer(2, Blocks.DIRT),
                new FlatChunkGeneratorLayer(1, Blocks.GRASS_BLOCK)
        };
        private Set<Identifier> structures;
        private boolean features = false;
        private boolean lakes = false;

        private Flat() {
            structures = WorldBuilder.getAllRegistryEntries(Registry.STRUCTURE_SET_KEY);
        }

        @CheckReturnValue
        public Flat biome(RegistryKey<Biome> biome) { this.biome = biome; return this; }

        @CheckReturnValue
        public Flat layers(FlatChunkGeneratorLayer... layers) { this.layers = layers; return this; }

        @SafeVarargs
        public final Flat structuresAllowed(RegistryKey<StructureSet>... structures) {
            this.structures.clear();
            for (RegistryKey<StructureSet> structure : structures)
                this.structures.add(structure.getValue());
            return this;
        }

        @SafeVarargs
        public final Flat structuresDisallowed(RegistryKey<StructureSet>... structures) {
            this.structures = WorldBuilder.getAllRegistryEntries(Registry.STRUCTURE_SET_KEY, structures);
            return this;
        }

        @CheckReturnValue
        public Flat features(boolean features) { this.features = features; return this; }

        @CheckReturnValue
        public Flat lakes(boolean lakes) { this.lakes = lakes; return this; }

        @Override
        public FlatChunkGenerator build() {
            MinecraftServer server = Quartz.getServer();
            DynamicRegistryManager.Immutable registryManager = server.getRegistryManager();
            Registry<StructureSet> structureRegistry = registryManager.get(Registry.STRUCTURE_SET_KEY);
            Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);

            List<RegistryEntry<StructureSet>> structureRegistryList = new ArrayList<>();
            for (Identifier structure : structures)
                structureRegistryList.add(RegistryEntry.of(structureRegistry.get(structure)));
            SimpleRegistry<StructureSet> structures = new SimpleRegistry<>(Registry.STRUCTURE_SET_KEY, Lifecycle.experimental(), null);
            for (Map.Entry<RegistryKey<StructureSet>, StructureSet> entry : registryManager.get(Registry.STRUCTURE_SET_KEY).getEntrySet())
                if (this.structures.contains(entry.getKey().getValue()))
                    structures.add(entry.getKey(), entry.getValue(), Lifecycle.experimental());

            FlatChunkGeneratorConfig config = new FlatChunkGeneratorConfig(Optional.of(RegistryEntryList.of(structureRegistryList)), biomeRegistry);
            config.setBiome(RegistryEntry.of(biomeRegistry.get(biome)));
            for (FlatChunkGeneratorLayer layer : layers)
                config.getLayers().add(layer);
            config.updateLayerBlocks();
            if (features)
                config.enableFeatures();
            if (lakes)
                config.enableLakes();

            return new FlatChunkGenerator(structures, config);
        }
    }

    // TODO
    public static class Noise extends ChunkGeneratorBuilder {

        private Set<Identifier> structures;

        @Override
        public NoiseChunkGenerator build() {

            return null;
        }
    }
}
