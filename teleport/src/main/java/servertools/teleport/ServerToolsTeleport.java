package servertools.teleport;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import servertools.core.CommandManager;
import servertools.core.STLog;
import servertools.core.ServerTools;
import servertools.core.util.Util;
import servertools.teleport.command.CommandEditTeleport;
import servertools.teleport.command.CommandHome;
import servertools.teleport.command.CommandTeleport;
import servertools.teleport.command.CommandYes;
import servertools.teleport.lib.Reference;

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
public class ServerToolsTeleport {

    public CommandHome commandHome;
    public CommandTeleport commandTeleport;
    public CommandEditTeleport commandEditTeleport;
    public CommandYes commandYes;

    @Mod.Instance
    public static ServerToolsTeleport instance;

    private static final File serverToolsTeleportDir = new File(ServerTools.serverToolsDir, "teleport");
    static {
        if (serverToolsTeleportDir.mkdirs()) {
            ServerToolsTeleport.log.fine(String.format("Creating root ServerTools-Teleport dir: %s", serverToolsTeleportDir.getAbsolutePath()));
        }
    }

    public static final STLog log = new STLog(Reference.MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        event.getModMetadata().version = Reference.VERSION;
        event.getModMetadata().parent = "ServerTools";

        Util.checkModuleVersion("Teleport", Reference.VERSION);

        TeleportConfig.init(new File(serverToolsTeleportDir, "teleport.cfg"));
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

        commandHome = new CommandHome("home");
        commandTeleport = new CommandTeleport("teleport");
        commandEditTeleport = new CommandEditTeleport("editteleport");
        commandYes = new CommandYes("yes");

        CommandManager.registerSTCommand(commandHome);
        CommandManager.registerSTCommand(commandTeleport);
        CommandManager.registerSTCommand(commandEditTeleport);
        CommandManager.registerSTCommand(commandYes);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        HomeManager.init(new File(serverToolsTeleportDir, "homes"));
        TeleportManager.init(new File(serverToolsTeleportDir, "teleports.json"));
    }
}
