package servertools.backup.compression;

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

import net.minecraft.server.MinecraftServer;
import servertools.backup.ServerToolsBackup;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchiveHandler extends ArchiveHandler {

    private ZipOutputStream zipOutputStream;

    public ZipArchiveHandler(MinecraftServer server) {
        super(server);
    }

    @Override
    public void openFile(File backupFolder, String backupFileName) throws IOException {

        if (zipOutputStream != null) {
            throw new IOException("Tried to open a new backup using the same ArchiveHandler");
        }

        zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(backupFolder, backupFileName)));
    }

    @Override
    public void addFileToArchive(File file) throws IOException {

        byte[] buffer = new byte[4096];
        int read;

        if (file.isDirectory()) return;

        zipOutputStream.putNextEntry(new ZipEntry(cleanPath(file.getCanonicalPath())));

        try {
            InputStream stream = new FileInputStream(file);
            while ((read = stream.read(buffer)) >= 0) {
                zipOutputStream.write(buffer, 0, read);
            }

            stream.close();
        } catch (IOException e) {
            ServerToolsBackup.log.warning(String.format("Couldn't backup file: %s", file.getAbsolutePath()));
        }

        zipOutputStream.closeEntry();
    }

    @Override
    public void closeFile() throws IOException {
        zipOutputStream.close();
        zipOutputStream = null;
    }

    @Override
    public String getFileExtension() {

        return ".zip";
    }
}
