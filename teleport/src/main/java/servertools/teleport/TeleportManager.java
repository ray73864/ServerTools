package servertools.teleport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import servertools.core.util.FileUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class TeleportManager {

    public static Map<String, Location> teleportMap = new HashMap<String, Location>();
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

    private static void saveTeleportFile() {

        try {
        FileUtils.writeStringToFile(gson.toJson(teleportMap), teleportSaveFile);
        } catch (IOException e) {
            e.printStackTrace();
            ServerToolsTeleport.teleportLog.log(Level.WARNING, "Failed to save teleport file");
        }
    }

    private static void loadTeleportFile() {

        if (!teleportSaveFile.exists()) {
            ServerToolsTeleport.teleportLog.log(Level.FINE, "Teleport save file doesn't exist, skipping it");
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
            ServerToolsTeleport.teleportLog.log(Level.WARNING, String.format("The teleport file %s could not be parsed as valid JSON, it will not be loaded", teleportSaveFile.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ServerToolsTeleport.teleportLog.log(Level.WARNING, String.format("Tried to load non-existant file: %s", teleportSaveFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            ServerToolsTeleport.teleportLog.log(Level.WARNING, String.format("Failed to close buffered reader stream for: %s", teleportSaveFile.getAbsolutePath()));
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
