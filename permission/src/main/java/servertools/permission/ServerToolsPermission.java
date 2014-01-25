package servertools.permission;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import servertools.core.CommandManager;
import servertools.core.STLog;
import servertools.core.ServerTools;
import servertools.core.config.ConfigSettings;
import servertools.permission.command.*;

import java.io.File;
import java.util.logging.Level;

/*
 * Copyright 2014 matthewprenger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Mod(modid = PermissionConstants.MOD_ID, name = PermissionConstants.MOD_NAME, version = PermissionConstants.VERSION, dependencies = PermissionConstants.DEPENDENCIES)
public class ServerToolsPermission {

    @Mod.Instance
    public static ServerToolsPermission instance;

    public static PlayerTracker playerTracker;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        /* Initialize the Permission Configuration */
        PermissionConfig.init(new File(ServerTools.serverToolsDir, "permission.cfg"));
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

        CommandManager.addSTCommand(new CommandAddUser("adduser"));
        CommandManager.addSTCommand(new CommandRemoveUser("removeuser"));
        CommandManager.addSTCommand(new CommandAddGroup("addgroup"));
        CommandManager.addSTCommand(new CommandRemoveGroup("removegroup"));
        CommandManager.addSTCommand(new CommandAddCommand("addcommand"));
        CommandManager.addSTCommand(new CommandRemoveCommand("removecommand"));
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        if (playerTracker == null) playerTracker = new PlayerTracker();
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {

        GroupManager.init(new File(ServerTools.serverToolsDir, "groups"));

        if (GroupManager.shouldLoadDefaultGroups()) {
            GroupManager.loadDefaultGroups();
        }
    }

    public static void log(Level level, Object object) {

        STLog.log(level, " [PERMISSION] " + object);
    }

    public static void debug(Object object) {

        if (ConfigSettings.DEBUG_MODE) STLog.log(Level.INFO, " [DEBUG] " + object.toString());
    }

}
