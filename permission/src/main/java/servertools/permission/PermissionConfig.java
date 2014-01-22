package servertools.permission;

import net.minecraftforge.common.Configuration;

import java.io.File;
import java.util.logging.Level;

public class PermissionConfig {

    public static void init(File file) {

        Configuration configuration = new Configuration(file);

        try {

            configuration.load();


            if (configuration.hasChanged()) {
                configuration.save();
            }

        } catch (Exception e) {
            e.printStackTrace();
            ServerToolsPermission.log(Level.SEVERE, "Failed to load permission configuration");
        }
    }
}
