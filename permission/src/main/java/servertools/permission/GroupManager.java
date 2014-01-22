package servertools.permission;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import servertools.core.util.FileUtils;
import servertools.permission.elements.Group;
import servertools.permission.elements.GroupException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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

public class GroupManager {

    private static final Map<String, Group> groups = new TreeMap<String, Group>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<String, File> groupFiles = new TreeMap<String, File>();
    private static File groupDir;

    private static Gson gson;

    public static void init(File groupDirectory) {

        gson = new GsonBuilder().setPrettyPrinting().create();

        groupDir = groupDirectory;
        groupDir.mkdirs();

        loadGroupsFromFile();
    }

    /**
     * Create a new permission group
     *
     * @param groupName the name of the new group
     * @throws GroupException if the group already exists
     */
    public static void createGroup(String groupName) throws GroupException {

        if (!groups.containsKey(groupName)) {
            groups.put(groupName, new Group(groupName));
            groupFiles.put(groupName, new File(groupDir, groupName + ".json"));
            saveGroupToFile(groupName);
        } else
            throw new GroupException("That group already exists");
    }

    /**
     * Removes a group and all members from the group
     *
     * @param groupName the name of the group
     * @throws GroupException if the group doesn't exist
     */
    public static void removeGroup(String groupName) throws GroupException {

        if (groups.containsKey(groupName)) {
            groups.remove(groupName);
            if (groupFiles.containsKey(groupName)) {
                groupFiles.get(groupName).delete();
                groupFiles.remove(groupName);
            }
        } else
            throw new GroupException("That group doesn't exist");
    }

    /**
     * Add a player to a permission group
     *
     * @param username  The username of the player to add
     * @param groupName The name of the group
     * @throws GroupException if the group doesn't exist
     */
    public static void addUserToGroup(String username, String groupName) throws GroupException {

        if (groups.containsKey(groupName)) {
            groups.get(groupName).addMember(username);
            saveGroupToFile(groupName);
        } else
            throw new GroupException("That group doesn't exist");
    }

    /**
     * Remove a player from a permission group
     *
     * @param username  the username of the player to remove
     * @param groupName the name of the group
     * @throws GroupException if the group doesn't exist or the player isn't in the group
     */
    public static void removeUserFromGroup(String username, String groupName) throws GroupException {

        if (groups.containsKey(groupName)) {

            Group group = groups.get(groupName);
            if (!group.removeUser(username)) {
                throw new GroupException("That player isn't a member of the group");
            }

            saveGroupToFile(groupName);
        } else
            throw new GroupException("That group doesn't exist");
    }

    /**
     * Add an allowed command to a group
     *
     * @param commandName the name of the command
     * @param groupName   the name of the group
     * @throws GroupException if the group doesn't exist
     */
    public static void addAllowedCommand(String commandName, String groupName) throws GroupException {

        if (groups.containsKey(groupName)) {
            groups.get(groupName).addAllowedCommand(commandName);
            saveGroupToFile(groupName);
        } else
            throw new GroupException("That group doesn't exist");
    }

    /**
     * Remove an allowed command from a group
     *
     * @param commandName the name of the command
     * @param groupName   the name of the group
     * @throws GroupException if the group doesn't exist or the command wasn't in the group
     */
    public static void removeAllowedCommand(String commandName, String groupName) throws GroupException {

        if (groups.containsKey(groupName)) {
            Group group = groups.get(groupName);
            if (!group.removeAllowedCommand(commandName))
                throw new GroupException("That command wasn't allowed for that group");

            saveGroupToFile(groupName);
        } else
            throw new GroupException("That group doesn't exist");
    }

    /**
     * Get a map of the current groups
     *
     * @return a {@link java.util.HashMap} of groups
     */
    public static Map<String, Group> getGroups() {

        return ImmutableMap.copyOf(groups);
    }

    /**
     * Get a list of allowed commands for a group
     *
     * @param groupName the name of the group
     * @return a {@link java.util.Set} of allowed commands or {@code null} if the group doesn't exist
     */
    public static Set<String> getAllowedCommands(String groupName) {

        if (groups.containsKey(groupName)) {
            return groups.get(groupName).getAllowedCommands();
        }

        return null;
    }

    /**
     * See if a user can use a command
     *
     * @param username    the username of the player
     * @param commandName the name of the command
     * @return {@code true} if the command is allowed, {@code false} if the command is not allowed
     */
    public static boolean canUseCommand(String username, String commandName) {

        for (Map.Entry<String, Group> entry : groups.entrySet()) {
            Group group = entry.getValue();
            if (group.isMember(username)) {
                for (String str : group.getAllowedCommands()) {
                    if (str.equals(commandName))
                        return true;
                }
            }
        }

        return false;
    }

    private static void saveGroupToFile(String groupName) {

        if (groups.containsKey(groupName)) {
            Group group = groups.get(groupName);
            File groupFile = groupFiles.get(groupName);

            try {
                FileUtils.writeStringToFile(gson.toJson(group), groupFile);
            } catch (IOException e) {
                e.printStackTrace();
                ServerToolsPermission.log(Level.WARNING, String.format("Failed to save group file: %s", groupFile.getAbsolutePath()));
            }
        }
    }

    /**
     * Clears the map of groups and reloads it from file
     */
    public static void loadGroupsFromFile() {

        groups.clear();

        File[] fileList = groupDir.listFiles();

        if (fileList == null || fileList.length == 0)
            return;

        for (File file : fileList) {

            if (file.getName().endsWith(".json")) {

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String groupName = file.getName().substring(0, file.getName().length() - 5);

                    groups.put(groupName, gson.fromJson(reader, Group.class));
                    groupFiles.put(groupName, file);

                } catch (Exception e) {
                    e.printStackTrace();
                    ServerToolsPermission.log(Level.WARNING, String.format("Failed to load group %s from file", file.getName()));
                }
            }
        }
    }
}
