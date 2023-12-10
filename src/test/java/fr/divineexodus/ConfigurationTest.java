package fr.divineexodus;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {
    @Test
    void testParseToObject() {
        Config config = Config.inMemory();
        config.set("server.ip", "127.0.0.1");
        config.set("server.port", 25569);
        Configuration configuration = new ObjectConverter().toObject(config, Configuration::new);
        assertEquals("127.0.0.1", configuration.serverIp);
        assertEquals(25569, configuration.serverPort);
    }

    @Test
    void testParseToFile() {
        Configuration config = new Configuration();
        config.serverIp = "0.0.0.0";
        config.serverPort = 25565;
        Config memory = new ObjectConverter().toConfig(config, Config::inMemory);
        assertEquals("0.0.0.0", memory.get("server.ip"));
        assertEquals(25565, (int) memory.get("server.port"));
    }

    @Test
    void testValidateIp() {
        Config config = Config.inMemory();
        config.set("server.ip", "domain.tld");
        config.set("server.port", 65536);
        ObjectConverter converter = new ObjectConverter();
        assertThrows(InvalidValueException.class, () -> converter.toObject(config, Configuration::new));
    }

    @Test
    void testValidatePort() {
        Config config = Config.inMemory();
        config.set("server.ip", "127.0.0.1");
        config.set("server.port", -1);
        ObjectConverter converter = new ObjectConverter();
        assertThrows(InvalidValueException.class, () -> converter.toObject(config, Configuration::new));
    }
}