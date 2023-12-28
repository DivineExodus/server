package fr.divineexodus.server.monitor;

import fr.divineexodus.server.entity.player.GamePlayer;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.core.metrics.Histogram;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.model.snapshots.Unit;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.time.Tick;

import java.io.IOException;
import java.time.Duration;

public class Monitor {
    public static final Gauge PLAYER_MONEY = Gauge.builder()
            .name("divineexodus_player_money")
            .help("Money of a player")
            .labelNames("player")
            .register();
    public static final Histogram TPS_DURATION = Histogram.builder()
            .name("divineexodus_tps")
            .help("TPS of the server")
            .unit(new Unit("ticks"))
            .register();
    public static final Gauge QUESTS = Gauge.builder()
            .name("divineexodus_quests")
            .help("Number of quests")
            .labelNames("status")
            .unit(new Unit("quests"))
            .register();
    public static final Gauge PING = Gauge.builder()
            .name("divineexodus_ping")
            .help("Ping of a player")
            .labelNames("player")
            .unit(new Unit("ms"))
            .register();
    public static final Gauge CHUNKS = Gauge.builder()
            .name("divineexodus_chunks")
            .help("Number of chunks")
            .labelNames("world")
            .unit(new Unit("chunks"))
            .register();
    public static final Gauge ENTITIES = Gauge.builder()
            .name("divineexodus_entities")
            .help("Number of entities")
            .labelNames("world", "type")
            .unit(new Unit("entities"))
            .register();
    public static final Gauge HDV_ITEMS = Gauge.builder()
            .name("divineexodus_hdv_items")
            .help("Number of items in the HDV")
            .labelNames("status")
            .unit(new Unit("items"))
            .register();
    public static final Gauge HDV_MONEY = Gauge.builder()
            .name("divineexodus_hdv_money")
            .help("Money in the HDV")
            .labelNames("status")
            .unit(new Unit("money"))
            .register();
    public static final Gauge PACKETS_SENT = Gauge.builder()
            .name("divineexodus_packets_sent")
            .help("Number of packets sent")
            .unit(new Unit("packets"))
            .register();
    public static final Gauge PACKETS_RECEIVED = Gauge.builder()
            .name("divineexodus_packets_received")
            .help("Number of packets received")
            .unit(new Unit("packets"))
            .register();

    public static void init() throws IOException {
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();
        eventHandler.addListener(PlayerPacketEvent.class, e -> PACKETS_RECEIVED.inc(1));
        eventHandler.addListener(PlayerPacketOutEvent.class, e -> PACKETS_SENT.inc(1));
        eventHandler.addListener(PlayerDisconnectEvent.class, e -> {
            if (e.getPlayer() instanceof GamePlayer player) {
                PING.remove(player.getUuid().toString());
            }
        });
        MinecraftServer.getBenchmarkManager().enable(Duration.ofSeconds(5));
        MinecraftServer.getSchedulerManager().scheduleTask(Monitor::monitor, TaskSchedule.immediate(), TaskSchedule.seconds(5));
        HTTPServer server = HTTPServer.builder()
                .port(9400)
                .buildAndStart();
    }

    private static void monitor() {
        PACKETS_RECEIVED.set(0);
        PACKETS_SENT.set(0);
        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (onlinePlayer instanceof GamePlayer player) {
//                QUESTS.labels("started").set(player.getQuests().size());
//                QUESTS.labels("progress").set(player.getFinishedQuests().size());
//                QUESTS.labels("finished").set(player.getFinishedQuests().size());
                PLAYER_MONEY.labelValues(player.getUuid().toString()).set(player.getMoney());
                PING.labelValues(player.getUuid().toString()).set(player.getLatency());
            }
        }
        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            CHUNKS.labelValues(instance.getDimensionName()).set(instance.getChunks().size());
            for (EntityType type : EntityType.values()) {
                ENTITIES.labelValues(instance.getDimensionName(), type.name()).set(instance.getEntities().stream().filter(entity -> entity.getEntityType() == type).count());
            }
        }
        TPS_DURATION.observe(Tick.SERVER_TICKS.getTicksPerSecond());
    }
}
