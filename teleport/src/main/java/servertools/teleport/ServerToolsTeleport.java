package servertools.teleport;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import servertools.core.CommandManager;
import servertools.core.CoreConstants;
import servertools.core.STLog;
import servertools.core.ServerTools;
import servertools.teleport.command.CommandEditTeleport;
import servertools.teleport.command.CommandHome;
import servertools.teleport.command.CommandTeleport;

import java.io.File;
import java.util.logging.Logger;

@Mod(modid = "ServerTools|Teleport", name = "ServerTools|Teleport", dependencies = "required-after:ServerTools")
public class ServerToolsTeleport {

    private static final File serverToolsTeleportDir = new File(ServerTools.serverToolsDir, "teleport");
    static {
        if (serverToolsTeleportDir.mkdirs()) {
            STLog.fine(String.format("Creating root ServerTools-Teleport dir: %s", serverToolsTeleportDir.getAbsolutePath()));
        }
    }

    public static Logger teleportLog;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModMetadata metadata = event.getModMetadata();
        metadata.version = CoreConstants.VERSION;

        teleportLog = STLog.getModuleLogger("ServerTools|Teleport");

        TeleportConfig.init(new File(serverToolsTeleportDir, "teleport.cfg"));
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

        CommandManager.addSTCommand(new CommandHome("home"));
        CommandManager.addSTCommand(new CommandTeleport("teleport"));
        CommandManager.addSTCommand(new CommandEditTeleport("editteleport"));
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        HomeManager.init(new File(serverToolsTeleportDir, "homes"));
        TeleportManager.init(new File(serverToolsTeleportDir, "teleports.json"));
    }
}
