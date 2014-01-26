package servertools.permission;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import servertools.core.CommandManager;
import servertools.core.STLog;
import servertools.core.ServerTools;
import servertools.core.util.Util;
import servertools.permission.command.*;
import servertools.permission.lib.Reference;

import java.io.File;

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

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = Reference.DEPENDENCIES)
public class ServerToolsPermission {

    @Mod.Instance
    public static ServerToolsPermission instance;

    public static File permissionDir;

    public static STLog log = new STLog(Reference.MOD_ID);

    public static PlayerTracker playerTracker;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        event.getModMetadata().version = Reference.VERSION;
        event.getModMetadata().parent = "ServerTools";

        Util.checkModuleVersion("Permission", Reference.VERSION);

        permissionDir = new File(ServerTools.serverToolsDir, "permission");
        if (permissionDir.mkdirs()) ServerToolsPermission.log.fine("Creating Permission Directory at: " + permissionDir.getAbsolutePath());

        /* Initialize the Permission Configuration */
        PermissionConfig.init(new File(permissionDir, "permission.cfg"));
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

        GroupManager.init(new File(permissionDir, "groups"));

        if (GroupManager.shouldLoadDefaultGroups()) {
            GroupManager.loadDefaultGroups();
        }
    }
}
