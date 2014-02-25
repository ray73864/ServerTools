/*
 * Copyright 2014 Matthew Prenger
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

package com.matthewprenger.servertools.core.util;

import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.lib.Reference;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Util {

    /**
     * Check if a module's {@link java.lang.Package} ImplementationVersion matches ServerTools' version
     *
     * The Game will not load if they aren't equal
     *
     * @param clazz any module class
     */
    public static void checkModuleVersion(Class clazz) {

        String moduleVersion = clazz.getPackage().getImplementationVersion();
        String moduleName = clazz.getPackage().getImplementationTitle();

        if (moduleVersion == null) {
            moduleVersion = "UNKNOWN";
            ServerTools.log.getLogger().warn("Module class: {} doesn't have a version number, unpredicatble results may occur", clazz.getName());
        }

        if (moduleName == null) {
            moduleName = "UNKNOWN";
            ServerTools.log.getLogger().warn("Module class: {} doesn't have an attached name, unpredictable results may occur", clazz.getName());
        }

        if (Reference.VERSION.equals(moduleVersion)) {
            ServerTools.log.getLogger().trace("Module: {} version: {} matches ServerTools version: {}", moduleName, moduleVersion, Reference.VERSION);
        } else {
            ServerTools.log.getLogger().fatal("Module {} version: {} does not match ServerTools version {}", moduleName, moduleVersion, Reference.VERSION);
            ServerTools.log.fatal("Download matching versions of ServerTools modules");

            throw new RuntimeException("Mismatched ServerTools module versions");
        }


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
