package servertools.teleport;

import net.minecraftforge.common.Configuration;

import java.io.File;
import java.util.logging.Level;

public class TeleportConfig {

    public static boolean ENABLE_TELEPORT_ACROSS_DIMENSION;
    public static boolean REQUIRE_OP_EDIT_TELEPORT;

    public static void init(File configFile) {

        Configuration teleportConfig = new Configuration(configFile);

        try {
            teleportConfig.load();

            ENABLE_TELEPORT_ACROSS_DIMENSION = teleportConfig.get(Configuration.CATEGORY_GENERAL, "enableTeleportAcrossDimension", false, "Enables teleports to be available across dimensions").getBoolean(false);
            REQUIRE_OP_EDIT_TELEPORT = teleportConfig.get(Configuration.CATEGORY_GENERAL, "requireOPEditTeleport", true, "Only server operators can edit teleports").getBoolean(true);

        } catch (Exception e) {
            e.printStackTrace();
            ServerToolsTeleport.teleportLog.log(Level.SEVERE, "Failed to load Teleport config");
        } finally {
            if (teleportConfig.hasChanged()) {
                teleportConfig.save();
            }
        }
    }
}
