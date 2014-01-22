package servertools.core.config;

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.Configuration;
import servertools.core.CoreConstants;

import java.io.File;

public class CoreConfig {

    public static void init(File configFile) {

        Configuration configuration = new Configuration(configFile);

        try {
            configuration.load();

            ConfigSettings.DEBUG_MODE = configuration.get(Configuration.CATEGORY_GENERAL, "debugMode", false, "Spams the logs").getBoolean(false);
            ConfigSettings.SEND_MOTD_ON_LOGIN = configuration.get(Configuration.CATEGORY_GENERAL, "sendMotdOnLogin", true, "Send the MOTD to players upon logging into the server").getBoolean(true);
            ConfigSettings.COLOR_OP_CHAT_MESSAGE = configuration.get(Configuration.CATEGORY_GENERAL, "colorOPMessages", true, "Colors op's chat messages").getBoolean(true);
            ConfigSettings.GENERATE_FLAT_BEDROCK = configuration.get(Configuration.CATEGORY_GENERAL, "flatBedrock", true, "Causes bedrock to generate only one layer thick").getBoolean(true);
            ConfigSettings.DEFAULT_REMOVE_ALL_RANGE = configuration.get(Configuration.CATEGORY_GENERAL, "defaultRemoveAllRange", 20, "The default range for the /removeall command").getInt();

        } catch (Exception e) {
            e.printStackTrace(System.err);
            FMLLog.severe(CoreConstants.MOD_NAME + " has failed to load its core configuration, game will exit now");
            System.exit(1);
        } finally {
            if (configuration.hasChanged()) {
                configuration.save();
            }
        }
    }
}
