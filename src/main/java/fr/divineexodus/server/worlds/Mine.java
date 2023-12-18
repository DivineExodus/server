package fr.divineexodus.server.worlds;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.DynamicChunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.biomes.Biome;

import java.util.UUID;

public class Mine extends InstanceContainer implements World {
    public static final Mine INSTANCE;
    private static final DimensionType DIMENSION;
    private static final Biome BIOME;

    static {
        DIMENSION = DimensionType.builder(NamespaceID.from("divineexodus:mine"))
                .build();
        MinecraftServer.getDimensionTypeManager().addDimension(DIMENSION);
        BIOME = Biome.builder()
                .name(NamespaceID.from("divineexodus:moria"))
                .build();
        MinecraftServer.getBiomeManager().addBiome(BIOME);
        INSTANCE = new Mine();
        MinecraftServer.getInstanceManager().registerInstance(INSTANCE);
    }

    Mine() {
        super(UUID.nameUUIDFromBytes("MINE".getBytes()), DIMENSION);
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
