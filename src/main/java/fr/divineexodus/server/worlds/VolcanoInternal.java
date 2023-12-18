package fr.divineexodus.server.worlds;

import fr.divineexodus.server.entity.player.GamePlayer;
import fr.divineexodus.server.entity.player.camera.ThirdPersonCamera;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.DynamicChunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.biomes.Biome;
import net.minestom.server.world.biomes.BiomeEffects;
import net.minestom.server.world.biomes.BiomeParticle;

import java.util.UUID;

public class VolcanoInternal extends InstanceContainer implements World {
    public static final VolcanoInternal INSTANCE;
    private static final DimensionType DIMENSION;
    private static final Biome BIOME;

    static {
        DIMENSION = DimensionType.builder(NamespaceID.from("divineexodus:volcano_internal"))
                .effects("minecraft:nether")
                .build();
        MinecraftServer.getDimensionTypeManager().addDimension(DIMENSION);
        BIOME = Biome.builder()
                .name(NamespaceID.from("divineexodus:volcano_internal"))
                .effects(BiomeEffects.builder()
                        .biomeParticle(new BiomeParticle(0.15f, new BiomeParticle.NormalOption(NamespaceID.from("minecraft:ash"))))
                        .ambientSound(NamespaceID.from("minecraft:ambient.basalt_deltas.loop"))
                        .build())
                .build();
        MinecraftServer.getBiomeManager().addBiome(BIOME);
        INSTANCE = new VolcanoInternal();
        MinecraftServer.getInstanceManager().registerInstance(INSTANCE);
    }

    VolcanoInternal() {
        super(UUID.nameUUIDFromBytes("VOLCANO-INTERNAL".getBytes()), DIMENSION);
        setChunkSupplier((instance, chunkX, chunkZ) -> {
            DynamicChunk chunk = new DynamicChunk(instance, chunkX, chunkZ);
            for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                    chunk.setBlock(x, 0, z, Block.BLACKSTONE);
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
        return new Pos(0, 20, 0);
    }

    @Override
    public void onPlayerJoin(Player player) {
        System.out.println("VolcanoInternal.onPlayerJoin");
        World.super.onPlayerJoin(player);
        try {
            if (player instanceof GamePlayer gamePlayer) {
                gamePlayer.setCamera(new ThirdPersonCamera(gamePlayer));
                System.out.println("gamePlayer.getCamera() = " + gamePlayer.getCamera().getClass().getName());
            } else {
                System.out.println("player = " + player.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
