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
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.net.URI;
import java.util.Deque;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class Backup implements Runnable {

    private final File sourceDir;
    private final File backupDir;
    private final String backupFileName;

    public Backup(File sourceDir, File backupDir, String backupFileName) throws IOException {

        this.sourceDir = sourceDir;
        this.backupDir = backupDir;
        this.backupFileName = backupFileName;

        if (!sourceDir.exists())
            throw new FileNotFoundException("The given backup source path doesn't exist");

        if (!sourceDir.isDirectory())
            throw new IOException("The given backup source path is a file");

        if (backupDir.exists() && !backupDir.isDirectory())
            throw new IOException("The given backup directory exists and isn't a directory");

        if (Strings.isNullOrEmpty(backupFileName))
            throw new IllegalArgumentException("Backup filename can't be null or empty");

        backupDir.mkdirs();
    }

    @Override
    public void run() {

        ServerToolsBackup.log.info(String.format("Starting backup %s", backupFileName));
        BackupHandler.sendBackupMessage(ChatMessageComponent.createFromText("Starting Server Backup").setColor(EnumChatFormatting.AQUA));

        //SaveOff
        //SaveAll

        boolean completedSuccessfully = true;
        try {
            zipDirectory(sourceDir, new File(backupDir, backupFileName));
        } catch (IOException e) {
            completedSuccessfully = false;
            e.printStackTrace();
        }

        //SaveOn

        if (completedSuccessfully) {
            BackupHandler.sendBackupMessage(ChatMessageComponent.createFromText("Server Backup Finished").setColor(EnumChatFormatting.GREEN));
            ServerToolsBackup.log.severe("Backup completed successfully");
        } else {
            BackupHandler.sendBackupMessage(ChatMessageComponent.createFromText("Server Backup Error - Check Server Logs").setColor(EnumChatFormatting.RED));
            ServerToolsBackup.log.severe("Backup didn't complete successfully");
        }

        BackupHandler.checkBackupDirSize();
        BackupHandler.checkForOldBackups();
        BackupHandler.checkNumberBackups();
    }

    void zipDirectory(File directory, File zipfile) throws IOException {
        URI baseDir = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipfile);
        Closeable res = out;
        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.removeFirst();
                File[] dirFiles = directory.listFiles();
                if (dirFiles != null && dirFiles.length != 0) {
                    for (File child : dirFiles) {
                        if (child != null) {
                            String name = baseDir.relativize(child.toURI()).getPath();
                            if (child.isDirectory()) {
                                queue.push(child);
                                name = name.endsWith("/") ? name : name + "/";
                                zout.putNextEntry(new ZipEntry(name));
                            } else {
                                if (!BackupConfig.fileBlacklist.contains(child.getName())) {
                                    zout.putNextEntry(new ZipEntry(name));
                                    copy(child, zout);
                                    zout.closeEntry();
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            res.close();
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    private static void copy(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            copy(in, out);
        } finally {
            in.close();
        }
    }
}
