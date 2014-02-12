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
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Util {

    /**
     * Check to see if a module is the same version as ServerTools Core
     * The game will not run if they aren't equal
     *
     * @param moduleName    the name of the module (Used for error reporting)
     * @param moduleVersion the version of the module
     */
    public static void checkModuleVersion(String moduleName, String moduleVersion) {
//
//        if (!(ServerTools.VERSION.equals(moduleVersion))) {
//
//            String versionMismatch = "ServerTools is %s, %s is %s";
//
//            ServerTools.log.fatal("####################################");
//            ServerTools.log.fatal("#         Version Mismatch         #");
////            ServerTools.log.fatal(String.format(versionMismatch, FMLMod, moduleName, moduleVersion));
//            ServerTools.log.fatal("Please download matching versions of ServerTools Modules!");
//            ServerTools.log.fatal("#     The Game Will Not Load       #");
//            ServerTools.log.fatal("####################################");
//
//            Runtime.getRuntime().exit(1);
//        }
    }

    /**
     * Get the Specification Version from the mod jar
     *
     * @param clazz A mod class
     * @return The mod version
     */
    public static String getVersionFromJar(Class<?> clazz) {

        String version = clazz.getPackage().getImplementationVersion();
        return Strings.isNullOrEmpty(version) ? "UNKNOWN" : version;
    }

    /**
     * Get a ChatComponent to send to an ICommandSender
     *
     * @param message    the message to add to the component
     * @param formatting any formatting to add
     * @return the component
     */
    public static ChatComponentStyle getChatComponent(String message, EnumChatFormatting formatting) {

        ChatComponentText componentText = new ChatComponentText(message);
        componentText.getChatStyle().setColor(formatting);
        return componentText;
    }
}
