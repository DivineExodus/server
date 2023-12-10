package fr.divineexodus;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecIntInRange;
import com.electronwill.nightconfig.core.conversion.SpecValidator;

import java.util.function.Predicate;

public class Configuration {
    @Path("server.ip")
    @SpecValidator(IpValidator.class)
    String serverIp = "0.0.0.0";

    @Path("server.port")
    @SpecIntInRange(min = 0, max = 65535)
    int serverPort = 25565;

    @Override
    public String toString() {
        return "Configuration{" +
                "serverIp='" + serverIp + '\'' +
                ", serverPort=" + serverPort +
                '}';
    }

    // region Validators
    public static class IpValidator implements Predicate<Object> {
        @Override
        public boolean test(Object o) {
            return o instanceof String ip && ip.matches("^(?:\\d{1,3}\\.){3}\\d{1,3}$");
        }
    }
    // endregion
}
