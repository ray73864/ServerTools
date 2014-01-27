package servertools.teleport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.util.FileUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
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

public class TeleportManager {

    public static Map<String, Location> teleportMap = new HashMap<String, Location>();
    public static Map<String, String> warpToMap = new HashMap<String, String>();
    private static File teleportSaveFile;
    private static Gson gson;

    public static void init(File saveFile) {

        teleportSaveFile = saveFile;
        gson = new GsonBuilder().setPrettyPrinting().create();

        loadTeleportFile();
    }

    public static void setTeleport(String name, Location location) {

        teleportMap.put(name, location);

        saveTeleportFile();
    }

    public static Location getTeleport(String name) {

        if (teleportMap.containsKey(name)) {
            return teleportMap.get(name);
        }

        return null;
    }

    public static boolean removeTeleport(String name) {

        if (teleportMap.containsKey(name)) {
            teleportMap.remove(name);
            saveTeleportFile();
            return true;
        }

        return false;
    }

    public static boolean addWarpToRequest(String senderUsername, String toUsername) {

        EntityPlayerMP entityPlayerMP = null;
        for (String string : MinecraftServer.getServer().getAllUsernames()) {
            if (string.equalsIgnoreCase(toUsername)) {
                entityPlayerMP = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(string);
                break;
            }
        }

        if (entityPlayerMP == null)
            return false;

        warpToMap.put(toUsername, senderUsername);

        entityPlayerMP.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Player %s wants to teleport to you", senderUsername)).setColor(EnumChatFormatting.GOLD));
        entityPlayerMP.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("To accept type: /%s", ServerToolsTeleport.instance.commandYes.name)).setColor(EnumChatFormatting.GREEN));

        return true;
    }

    private static void saveTeleportFile() {

        try {
        FileUtils.writeStringToFile(gson.toJson(teleportMap), teleportSaveFile);
        } catch (IOException e) {
            e.printStackTrace();
            ServerToolsTeleport.log.log(Level.WARNING, "Failed to save teleport file");
        }
    }

    private static void loadTeleportFile() {

        if (!teleportSaveFile.exists()) {
            ServerToolsTeleport.log.log(Level.FINE, "Teleport save file doesn't exist, skipping it");
            return;
        }

        teleportMap.clear();

        try {

            FileReader fileReader = new FileReader(teleportSaveFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            Type type = new TypeToken<Map<String, Location>>(){}.getType();

            Map<String, Location> map = gson.fromJson(bufferedReader, type);

            if (map != null)
                teleportMap = map;

            bufferedReader.close();

        } catch (JsonParseException e) {
            e.printStackTrace();
            ServerToolsTeleport.log.log(Level.WARNING, String.format("The teleport file %s could not be parsed as valid JSON, it will not be loaded", teleportSaveFile.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ServerToolsTeleport.log.log(Level.WARNING, String.format("Tried to load non-existant file: %s", teleportSaveFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            ServerToolsTeleport.log.log(Level.WARNING, String.format("Failed to close buffered reader stream for: %s", teleportSaveFile.getAbsolutePath()));
        }
    }

    public static class Location {
        public final double x, y, z;
        public final int dimension;

        public Location(int dimension, double x, double y, double z) {
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
