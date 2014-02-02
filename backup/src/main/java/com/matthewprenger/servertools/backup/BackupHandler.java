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

import com.google.common.base.Strings;
import com.matthewprenger.servertools.core.util.FileUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class BackupHandler {

    private static final String FILE_EXTENSION = ".zip";
    private static final BackupFileNameFilter backupFileNameFilter = new BackupFileNameFilter();

    private File backupDir;
    private File worldDir;

    private Timer backupTimer;

    public static BackupHandler instance;

    public BackupHandler() {

        instance = this;

        ServerToolsBackup.log.info("Initializing ServerTools Backup Handler");

        if (Strings.isNullOrEmpty(BackupConfig.backupDirPath))
            throw new IllegalArgumentException("The configured backup path is not set");

        backupDir = new File(BackupConfig.backupDirPath);
        worldDir = DimensionManager.getWorld(0).getChunkSaveLocation();

        if (backupDir.exists() && backupDir.isFile())
            throw new IllegalArgumentException("File exists with name of configured backup path, can't create backup directory");

        ServerToolsBackup.log.config(String.format("Backup Directory: %s", backupDir.getAbsolutePath()));

        backupDir.mkdirs();

        backupTimer = new Timer(true);

        if (BackupConfig.enableAutoBackup) {

            ServerToolsBackup.log.info(String.format("Initializing ServerTools AutoBackup to run every %s minutes", BackupConfig.autoBackupInterval));

            int interval = BackupConfig.autoBackupInterval * 60 * 1000;

            if (interval <= 0)
                throw new IllegalArgumentException("Autobackup interval must be greater than 0");

            try {
                backupTimer.scheduleAtFixedRate(
                        new Backup(worldDir, backupDir), interval, interval
                );
            } catch (IOException e) {
                e.printStackTrace();
                ServerToolsBackup.log.severe("Failed to initialize autobackup");
            }
        }
    }

    /**
     * Run the server backup
     */
    public void doBackup() throws IOException {

        backupTimer.schedule(
                new Backup(worldDir, backupDir), 0
        );
    }

    /**
     * Get the backup filename with wildcards replaced for the current date and time
     *
     * @return the name of the backup
     */
    public static String getBackupName() {

        Calendar cal = Calendar.getInstance();

        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH) + 1; // WHY DOES JAVA THINK JANUARY IS 0 ?!?!?
        Integer day = cal.get(Calendar.DATE);
        Integer hour = cal.get(Calendar.HOUR_OF_DAY);
        Integer minute = cal.get(Calendar.MINUTE);
        Integer second = cal.get(Calendar.SECOND);

        return BackupConfig.backupFileNameTemplate.replaceAll("%YEAR", year.toString())
                .replaceAll("%MONTH", month.toString())
                .replaceAll("%DAY", day.toString())
                .replaceAll("%HOUR", hour.toString())
                .replaceAll("%MINUTE", minute.toString())
                .replaceAll("%SECOND", second.toString()) + FILE_EXTENSION;
    }

    /**
     * Check the backup directories for files older than the maximum backup age
     * set in the config and deletes them.
     * <p/>
     * Does nothing if config setting is set to -1
     */
    public void checkForOldBackups() {

        if (BackupConfig.backupLifespanDays == -1)
            return;

        ServerToolsBackup.log.info("Checking backup directory for old backups");

        for (File file : backupDir.listFiles(backupFileNameFilter)) {
            if (file.isFile()) {
                int age = (int) ((new Date().getTime() - file.lastModified()) / 24 / 60 / 60 / 1000);

                ServerToolsBackup.log.fine(String.format("Found backup file %s; %s days old", file.getName(), age));

                if (age > BackupConfig.backupLifespanDays) {
                    ServerToolsBackup.log.info(String.format("Deleting old backup file: %s", file.getName()));
                    if (file.delete())
                        ServerToolsBackup.log.fine(String.format("Successfully deleted file %s", file.getName()));
                    else
                        ServerToolsBackup.log.warning(String.format("Failed to delete file %s", file.getName()));
                }
            }
        }
    }

    /**
     * Check the size of the backup directory and delete the
     * oldest file if the directory is larger than the config option
     */
    public void checkBackupDirSize() {

        if (BackupConfig.backupDirMaxSize == -1)
            return;

        ServerToolsBackup.log.fine("Checking size of the backup directory");

        ServerToolsBackup.log.fine(String.format("Backup directory size: %s MB", FileUtils.getFolderSize(backupDir) / org.apache.commons.io.FileUtils.ONE_MB));

        while (FileUtils.getFolderSize(backupDir) / org.apache.commons.io.FileUtils.ONE_MB > BackupConfig.backupDirMaxSize) {

            File oldestFile = FileUtils.getOldestFile(backupDir);

            if (oldestFile != null) {
                ServerToolsBackup.log.fine(String.format("Deleting oldest file: %s", oldestFile.getName()));
                oldestFile.delete();
            }
        }
    }

    /**
     * Check the number of backups in the backup directory and delete old ones if necessary
     */
    public void checkNumberBackups() {

        if (BackupConfig.backupMaxNumber == -1)
            return;

        ServerToolsBackup.log.fine("Checking number of backups in backup directory");

        ServerToolsBackup.log.fine(String.format("%s backups exist", getNumberBackups()));

        while (getNumberBackups() > BackupConfig.backupMaxNumber) {
            File oldestFile = FileUtils.getOldestFile(backupDir);
            if (oldestFile != null) {
                ServerToolsBackup.log.info(String.format("Deleting oldest backup file: %s", oldestFile.getName()));
                oldestFile.delete();
            }
        }
    }

    /**
     * Get the number of backups in the backup directory
     *
     * @return the number of backups
     */
    private int getNumberBackups() {

        int number = 0;

        if (backupDir.exists() && backupDir.isDirectory()) {

            File[] files = backupDir.listFiles(backupFileNameFilter);

            for (File ignored : files) {
                number++;
            }
        }

        return number;
    }

    /**
     * Send a backup related message to all users that should get backup messages
     *
     * @param component a {@link net.minecraft.util.ChatMessageComponent} to send
     */
    public static void sendBackupMessage(ChatMessageComponent component) {

        for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            EntityPlayerMP playerMP = (EntityPlayerMP) obj;

            if (playerMP != null) {
                if (BackupConfig.sendBackupMessageToOps && MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(playerMP.getCommandSenderName()))
                    playerMP.sendChatToPlayer(component);
                else if (BackupConfig.sendBackupMessageToUsers)
                    playerMP.sendChatToPlayer(component);
                else if (BackupConfig.backupMessageWhitelist.contains(playerMP.getCommandSenderName()))
                    playerMP.sendChatToPlayer(component);
            }
        }

        MinecraftServer.getServer().sendChatToPlayer(component);
    }

    /**
     * Toggle if worldservers can save or not
     *
     * @param canSave if the worldservers can save
     */
    public static void setWorldCanSave(boolean canSave) {

        MinecraftServer server = MinecraftServer.getServer();

        for (WorldServer worldServer : server.worldServers) {

            if (worldServer != null) {
                worldServer.canNotSave = !canSave;
            }
        }

        if (canSave)
            sendBackupMessage(ChatMessageComponent.createFromTranslationWithSubstitutions("chat.type.admin", ServerToolsBackup.instance.getCommandSenderName(),
                    ChatMessageComponent.createFromTranslationKey("commands.save.enabled")).setColor(EnumChatFormatting.GRAY));
        else
            sendBackupMessage(ChatMessageComponent.createFromTranslationWithSubstitutions("chat.type.admin", ServerToolsBackup.instance.getCommandSenderName(),
                    ChatMessageComponent.createFromTranslationKey("commands.save.disabled")).setColor(EnumChatFormatting.GRAY));
    }

    /**
     * Force save all worlds
     */
    public static void forceSaveWorld() {

        MinecraftServer server = MinecraftServer.getServer();

        if (server.getConfigurationManager() != null) {
            server.getConfigurationManager().saveAllPlayerData();
        }

        try {
            boolean flag;
            for (WorldServer worldServer : server.worldServers) {
                if (worldServer != null) {
                    flag = worldServer.canNotSave;
                    worldServer.canNotSave = false;
                    worldServer.saveAllChunks(true, null);
                    worldServer.canNotSave = flag;
                }
            }
        } catch (MinecraftException e) {
            sendBackupMessage(ChatMessageComponent.createFromTranslationWithSubstitutions("chat.type.admin", ServerToolsBackup.instance.getCommandSenderName(),
                    ChatMessageComponent.createFromTranslationWithSubstitutions("commands.save.failed", e.getMessage())).setColor(EnumChatFormatting.GRAY));
            return;
        }

        sendBackupMessage(ChatMessageComponent.createFromTranslationWithSubstitutions("chat.type.admin", ServerToolsBackup.instance.getCommandSenderName(),
                ChatMessageComponent.createFromTranslationKey("commands.save.success")).setColor(EnumChatFormatting.GRAY));
    }

    private static class BackupFileNameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(FILE_EXTENSION);
        }
    }
}
