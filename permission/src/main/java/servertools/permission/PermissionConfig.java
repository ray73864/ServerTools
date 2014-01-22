package servertools.permission;

import net.minecraftforge.common.Configuration;

import java.io.File;
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

public class PermissionConfig {

    public static void init(File file) {

        Configuration configuration = new Configuration(file);

        try {

            configuration.load();


            if (configuration.hasChanged()) {
                configuration.save();
            }

        } catch (Exception e) {
            e.printStackTrace();
            ServerToolsPermission.log(Level.SEVERE, "Failed to load permission configuration");
        }
    }
}
