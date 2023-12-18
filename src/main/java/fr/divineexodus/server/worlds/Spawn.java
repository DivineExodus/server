package fr.divineexodus.server.worlds;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.DynamicChunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.biomes.Biome;
import net.minestom.server.world.biomes.BiomeEffects;
import net.minestom.server.world.biomes.BiomeParticle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Spawn extends InstanceContainer implements World {
    public static final Spawn INSTANCE;
    private static final DimensionType DIMENSION;
    private static final Biome BIOME;

    static {
        DIMENSION = DimensionType.builder(NamespaceID.from("divineexodus:spawn"))
                .build();
        MinecraftServer.getDimensionTypeManager().addDimension(DIMENSION);
        BIOME = Biome.builder()
                .name(NamespaceID.from("divineexodus:spawn"))
                .build();
        MinecraftServer.getBiomeManager().addBiome(BIOME);
        INSTANCE = new Spawn();
        MinecraftServer.getInstanceManager().registerInstance(INSTANCE);
    }

    Spawn() {
        super(UUID.nameUUIDFromBytes("SPAWN".getBytes()), DIMENSION);
        setChunkSupplier((instance, chunkX, chunkZ) -> {
            DynamicChunk chunk = new DynamicChunk(instance, chunkX, chunkZ);
            for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                    for (int i = 0; i < 16; i++) {
                        chunk.setBiome(x, i, z, BIOME);
                    }
                }
            }
            return chunk;
        });
    }

    @Override
    public Pos getSpawnPoint() {
        return new Pos(0, 2, 0);
    }
}
