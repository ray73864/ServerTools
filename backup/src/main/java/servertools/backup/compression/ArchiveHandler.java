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

import java.io.IOException;

public abstract class ArchiveHandler implements IArchiveHandler {

    protected final String serverDataDirectory;

    public ArchiveHandler(MinecraftServer server) {
        String directory;
        try {
            directory = server.getFile(".").getCanonicalPath();
        } catch (IOException e) {
            directory = server.getFile(".").getAbsolutePath();
        }

        serverDataDirectory = directory;
    }

    protected String cleanPath(String path) {

        if (path.substring(0, serverDataDirectory.length()).equals(serverDataDirectory)) {
            return path.substring(serverDataDirectory.length() + 1);
        }

        return path;
    }
}
