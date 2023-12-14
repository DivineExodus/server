package fr.divineexodus.server;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;

public class DivineExodus {
    public static FileConfig CONFIG_FILE;
    public static Configuration CONFIG;
    public static final Logger LOGGER = MinecraftServer.LOGGER;

    public DivineExodus(String filePath) {
        LOGGER.info("Starting DivineExodus");
        LOGGER.info("Loading configuration file {}", filePath);
        DivineExodus.CONFIG_FILE = FileConfig.builder(filePath)
                .concurrent()
                .autoreload()
                .autosave()
                .onFileNotFound((file, configFormat) -> {
                    LOGGER.error("The file {} was not found, creating it !", file);
                    FileConfig config = FileConfig.of(file);
                    new ObjectConverter().toConfig(new Configuration(), config);
                    config.save();
                    return true;
                })
                .charset(StandardCharsets.UTF_8)
                .writingMode(WritingMode.REPLACE)
                .sync()
                .build();
        DivineExodus.CONFIG_FILE.load();
        DivineExodus.CONFIG = new ObjectConverter().toObject(DivineExodus.CONFIG_FILE, Configuration::new);
        LOGGER.info("Configuration loaded");
    }

    public static void main(String[] args) {
        new DivineExodus(args.length > 0 ? args[0] : "config.toml");
    }
}
