package servertools.core;

import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.Configuration;
import servertools.core.command.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

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

public class CommandManager {

    public static final String ENABLE_COMMAND_CONFIG_CATEGORY = "enableCommand";
    public static final String COMMAND_NAME_CONFIG_CATEGORY = "commandName";

    private static final Configuration commandConfig = new Configuration(new File(ServerTools.serverToolsDir, "command.cfg"));

    static {
        commandConfig.load();

        commandConfig.addCustomCategoryComment(ENABLE_COMMAND_CONFIG_CATEGORY, "Allows you to disable any command registered with ServerTools");
        commandConfig.addCustomCategoryComment(COMMAND_NAME_CONFIG_CATEGORY, "Allows you to rename any command registered with ServerTools");

        if (commandConfig.hasChanged()) commandConfig.save();
    }

    public static final Set<ServerToolsCommand> commandsToLoad = new HashSet<ServerToolsCommand>();

    private static boolean commandsLoaded = false;

    /**
     * Registers a command with ServerTools
     *
     * @param command A command that extends ServerToolsCommand
     */
    public static void addSTCommand(ServerToolsCommand command) {

        if (commandsLoaded) {
            throw new IllegalStateException("Only call this method before FMLServerStarting");
        }

        boolean enableCommand = commandConfig.get("enableCommand", command.getClass().getName(), true).getBoolean(true);
        command.name = commandConfig.get("commandName", command.getClass().getName(), command.defaultName).getString();

        if (enableCommand) {
            commandsToLoad.add(command);
        }

        if (commandConfig.hasChanged()) {
            commandConfig.save();
        }

    }

    protected static void registerCommands(CommandHandler commandHandler) {

        for (ServerToolsCommand command : commandsToLoad) {
            System.out.println("Registering Command: " + command.name);
            commandHandler.registerCommand(command);
        }

        commandsLoaded = true;
    }

    public static void onServerStopped() {

        commandsLoaded = false;
    }

    public static void initCoreCommands() {

        addSTCommand(new CommandMotd("motd"));
        addSTCommand(new CommandVoice("voice"));
        addSTCommand(new CommandSilence("silence"));
        addSTCommand(new CommandDisarm("disarm"));
        addSTCommand(new CommandEntityCount("entitycount"));
        addSTCommand(new CommandHeal("heal"));
        addSTCommand(new CommandInventory("inventory"));
        addSTCommand(new CommandKillPlayer("killplayer"));
        addSTCommand(new CommandKillAll("killall"));
        addSTCommand(new CommandReloadMotd("reloadmotd"));
        addSTCommand(new CommandSpawnMob("spawnmob"));
        addSTCommand(new CommandWhereIs("whereis"));
        addSTCommand(new CommandTPS("tps"));
        addSTCommand(new CommandRemoveAll("removeall"));
    }

    public static boolean areCommandsLoaded() {

        return commandsLoaded;
    }
}
