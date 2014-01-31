package com.matthewprenger.servertools.backup;

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

import com.matthewprenger.servertools.core.CommandManager;
import com.matthewprenger.servertools.core.STLog;
import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.util.Util;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;

import java.io.File;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = Reference.DEPENDENCIES)
public class ServerToolsBackup {

    public static final STLog log = new STLog(Reference.MOD_ID);

    private static File backupDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        event.getModMetadata().version = Reference.VERSION;
        event.getModMetadata().parent = "ServerTools";

        Util.checkModuleVersion("Backup", Reference.VERSION);

        backupDir = new File(ServerTools.serverToolsDir, "backup");
        backupDir.mkdirs();

        BackupConfig.init(new File(backupDir, "backup.cfg"));
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

        CommandManager.registerSTCommand(new CommandBackup("backup"));
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {

        BackupHandler.init();
    }
}
