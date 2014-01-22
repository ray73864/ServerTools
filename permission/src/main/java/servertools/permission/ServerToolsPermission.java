package servertools.permission;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import servertools.core.CommandManager;
import servertools.core.STLog;
import servertools.core.ServerTools;
import servertools.permission.command.*;

import java.io.File;
import java.util.logging.Level;

@Mod(modid = PermissionConstants.MOD_ID, name = PermissionConstants.MOD_NAME, version = PermissionConstants.VERSION, dependencies = PermissionConstants.DEPENDENCIES)
public class ServerToolsPermission {

    @Mod.Instance
    public static ServerToolsPermission instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        /* Initialize the Permission Configuration */
        PermissionConfig.init(new File(ServerTools.serverToolsDir, "permission.cfg"));

        GroupManager.init(new File(ServerTools.serverToolsDir, "groups"));
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        CommandManager.addSTCommand(new CommandAddUser("adduser"));
        CommandManager.addSTCommand(new CommandRemoveUser("removeuser"));
        CommandManager.addSTCommand(new CommandAddGroup("addgroup"));
        CommandManager.addSTCommand(new CommandRemoveGroup("removegroup"));
        CommandManager.addSTCommand(new CommandAddCommand("addcommand"));
        CommandManager.addSTCommand(new CommandRemoveCommand("removecommand"));
    }

    public static void log(Level level, Object object) {

        STLog.log(level, " [PERMISSION] " + object);
    }

}
