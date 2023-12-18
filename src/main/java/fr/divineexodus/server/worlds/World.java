package fr.divineexodus.server.worlds;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.UUID;

public interface World {
    static UUID MAIN_WORLD_UUID = UUID.nameUUIDFromBytes("MAIN_WORLD".getBytes());

    default void onPlayerJoin(Player player) {
        onPlayerSpawn(player);
    }
    default void onPlayerLeave(Player player) {
        onPlayerDisconnect(player);
    }

    default void onPlayerSpawn(Player player) {}

    default void onPlayerDisconnect(Player player) {};

    Pos getSpawnPoint();
}
