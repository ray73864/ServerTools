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

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.Configuration;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class BackupConfig {

    public static String backupDirPath;
    public static String backupFileNameTemplate;
    public static int backupLifespanDays;
    public static int backupDirMaxSize;
    public static int backupMaxNumber;
    public static final Set<String> fileBlacklist = new HashSet<String>();
    public static boolean sendBackupMessageToOps;
    public static boolean sendBackupMessageToUsers;
    public static final Set<String> backupMessageWhitelist = new HashSet<String>();

    public static boolean enableAutoBackup;
    public static int autoBackupInterval;

    public static void init(File configFile) {

        Configuration backupConfg = new Configuration(configFile);

        try {

            backupConfg.load();
            String category;

            /* General Settings */
            category = "general";
            backupDirPath = backupConfg.get(category, "backupDir", "backup", "The backup directory").getString();
            backupFileNameTemplate = backupConfg.get(category, "filename", "%MONTH-%DAY-%YEAR_%HOUR-%MINUTE-%SECOND", "The Filename template for backup zips. %MONTH, %DAY, %YEAR, %HOUR, %MINUTE, %SECOND").getString();
            backupLifespanDays = backupConfg.get(category, "daysToKeepBackups", -1, "Set to -1 to disable").getInt(-1);
            backupDirMaxSize = backupConfg.get(category, "maxBackupDirSize", -1, "In megabytes, set to -1 to disable").getInt(-1);
            backupMaxNumber = backupConfg.get(category, "maxNumberBackups", -1, "Maximum number of backups to keep, Set to -1 to disable").getInt(-1);
            sendBackupMessageToOps = backupConfg.get(category, "sendBackupMessageToOps", true, "Send backup related message to ops").getBoolean(true);
            sendBackupMessageToUsers = backupConfg.get(category, "sendBackupMessageToUsers", false, "Send backup related messages to users").getBoolean(false);

            String[] fileBlacklistArray = backupConfg.get(category, "fileBlackList", "", "Comma separated list of files to not back up").getString().split(",");
            if (fileBlacklistArray.length > 0) {
                Collections.addAll(fileBlacklist, fileBlacklistArray);
            }

            fileBlacklist.add("level.dat_new"); /* Minecraft Temp File - Causes Backup Problems */

            String[] backupMessageWhitelistArray = backupConfg.get(category, "backupMessageWhitelist", "", "A list of users to send backup messages to").getString().split(",");
            if (backupMessageWhitelistArray.length > 0) {
                Collections.addAll(backupMessageWhitelist, backupMessageWhitelistArray);
            }

            /* Settings for AutoBackups */
            category = "autoBackup";
            enableAutoBackup = backupConfg.get(category, "enableAutoBackup", false, "Enable automatic backups at specified intervals").getBoolean(false);
            autoBackupInterval = backupConfg.get(category, "autoBackupInterval", 1440, "The interval in minutes for the auto backup to run").getInt(1440);

            if (backupConfg.hasChanged())
                backupConfg.save();

        } catch (Exception e) {
            e.printStackTrace();
            FMLLog.warning("ServerTools Backup failed to load its configuration", e);
        }
    }
}
