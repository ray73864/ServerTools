package com.matthewprenger.servertools.core.util;

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
import com.matthewprenger.servertools.core.ServerTools;

public class Util {

    /**
     * Check to see if a module is the same version as ServerTools Core
     * The game will not run if they aren't equal
     *
     * @param moduleName    the name of the module (Used for error reporting)
     * @param moduleVersion the version of the module
     */
    public static void checkModuleVersion(String moduleName, String moduleVersion) {

        if (!(ServerTools.VERSION.equals(moduleVersion))) {

            String versionMismatch = "ServerTools is %s, %s is %s";

            ServerTools.log.severe("####################################");
            ServerTools.log.severe("#         Version Mismatch         #");
            ServerTools.log.severe(String.format(versionMismatch, ServerTools.VERSION, moduleName, moduleVersion));
            ServerTools.log.severe("Please download matching versions of ServerTools Modules!");
            ServerTools.log.severe("#     The Game Will Not Load       #");
            ServerTools.log.severe("####################################");

            Runtime.getRuntime().exit(1);
        }
    }

    /**
     * Get the Specification Version from the mod jar
     *
     * @param clazz A mod class
     * @return The mod version
     */
    public static String getVersionFromJar(Class<?> clazz) {

        String version = clazz.getPackage().getSpecificationVersion();
        return Strings.isNullOrEmpty(version) ? "0.0.0.0" : version;
    }
}
