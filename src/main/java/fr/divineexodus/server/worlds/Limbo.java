package fr.divineexodus.server.worlds;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
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

public class Limbo extends InstanceContainer implements World {
    public static final Limbo INSTANCE;
    private static final DimensionType DIMENSION;
    private static final Biome BIOME;
    private List<UUID> waiting = new ArrayList<>();

    static {
        DIMENSION = DimensionType.builder(NamespaceID.from("divineexodus:limbo"))
                .minY(0)
                .effects("minecraft:the_end")
                .infiniburn(NamespaceID.from("minecraft:infiniburn_overworld"))
                .build();
        MinecraftServer.getDimensionTypeManager().addDimension(DIMENSION);
        BIOME = Biome.builder()
                .name(NamespaceID.from("divineexodus:limbo"))
                .effects(BiomeEffects.builder()
                        .biomeParticle(new BiomeParticle(
                                1f,
                                new BiomeParticle.NormalOption(NamespaceID.from("minecraft:underwater"))
                        ))
                        .build())
                .build();
        MinecraftServer.getBiomeManager().addBiome(BIOME);
        INSTANCE = new Limbo();
        MinecraftServer.getInstanceManager().registerInstance(INSTANCE);
    }

    Limbo() {
        super(UUID.nameUUIDFromBytes("LIMBO".getBytes()), DIMENSION);
        setChunkSupplier((instance, chunkX, chunkZ) -> {
            DynamicChunk chunk = new DynamicChunk(instance, chunkX, chunkZ);
            for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                    chunk.setBlock(x, 0, z, Block.BARRIER);
                    for (int i = 0; i < 16; i++) {
                        chunk.setBiome(x, i, z, BIOME);
                    }
                }
            }
            return chunk;
        });
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            for (UUID uuid : waiting) {
                Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);
                if (player != null) {
                    if (MinecraftServer.getInstanceManager().getInstance(MAIN_WORLD_UUID) != null && player.getInstance() == INSTANCE) {
                        player.setInstance(MinecraftServer.getInstanceManager().getInstance(MAIN_WORLD_UUID), new Pos(0, 2, 0));
                    } else {
                        player.sendActionBar(Component.translatable("connection.awaiting_server"));
                    }
                }
            }
        },TaskSchedule.immediate(), TaskSchedule.nextTick());
    }

    @Override
    public void onPlayerSpawn(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        waiting.add(player.getUuid());
    }

    @Override
    public void onPlayerDisconnect(Player player) {
        waiting.remove(player.getUuid());
    }

    @Override
    public Pos getSpawnPoint() {
        return new Pos(0, 2, 0);
    }
}
