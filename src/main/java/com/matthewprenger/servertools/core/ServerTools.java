package com.matthewprenger.servertools.core;

import com.matthewprenger.servertools.core.config.ConfigSettings;
import com.matthewprenger.servertools.core.config.CoreConfig;
import com.matthewprenger.servertools.core.handler.FlatBedrockHandler;
import com.matthewprenger.servertools.core.handler.VoiceHandler;
import com.matthewprenger.servertools.core.lib.Reference;
import com.matthewprenger.servertools.core.task.TaskManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;

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
public class ServerTools {

    public static final File serverToolsDir = new File("com/matthewprenger/servertools");

    public static final STLog log = new STLog(Reference.MOD_ID);

    static {
        if (serverToolsDir.mkdirs())
            log.fine(String.format("Creating ServerTools root dir at: %s", ServerTools.serverToolsDir.getAbsolutePath()));
    }

    @Mod.Instance(Reference.MOD_ID)
    public static ServerTools instance;

    public Motd motd;

    public VoiceHandler voiceHandler;

    public TaskManager taskManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        event.getModMetadata().version = Reference.VERSION;

        CoreConfig.init(new File(serverToolsDir, "core.cfg"));

        taskManager = new TaskManager();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        /* Register the Flat Bedrock Generator */
        if (ConfigSettings.GENERATE_FLAT_BEDROCK) {
            GameRegistry.registerWorldGenerator(new FlatBedrockHandler());
        }
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

        CommandManager.initCoreCommands();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        /* Initialize the Message of the Day */
        if (motd == null) motd = new Motd(new File(serverToolsDir, "motd.txt"));

        /* Initialize the Voice Handler */
        if (voiceHandler == null) voiceHandler = new VoiceHandler();

        /* Register Commands with the Server */
        CommandHandler ch = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        CommandManager.registerCommands(ch);
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {

        CommandManager.onServerStopped();
    }
}