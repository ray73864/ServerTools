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

import com.matthewprenger.servertools.core.command.CommandManager;
import com.matthewprenger.servertools.core.STLog;
import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.util.Util;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.io.File;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = Reference.DEPENDENCIES)
public class ServerToolsBackup implements ICommandSender {

    public static final STLog log = new STLog(Reference.MOD_ID);

    @Mod.Instance
    public static ServerToolsBackup instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        String VERSION = Util.getVersionFromJar(getClass());

        event.getModMetadata().version = VERSION;
        event.getModMetadata().parent = "ServerTools";

        Util.checkModuleVersion("Backup", VERSION);

        File backupDir = new File(ServerTools.serverToolsDir, "backup");
        backupDir.mkdirs();

        BackupConfig.init(new File(backupDir, "backup.cfg"));
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

        CommandManager.registerSTCommand(new CommandBackup("backup"));
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {

        new BackupHandler();
    }

    @Override
    public String getCommandSenderName() {

        return "STBackup";
    }

    @Override
    public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {

        log.info(chatmessagecomponent.toString());
    }

    @Override
    public boolean canCommandSenderUseCommand(int i, String s) {

        return true;
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {

        return new ChunkCoordinates(0, 0, 0);
    }

    @Override
    public World getEntityWorld() {

        return MinecraftServer.getServer().worldServers[0];
    }
}
