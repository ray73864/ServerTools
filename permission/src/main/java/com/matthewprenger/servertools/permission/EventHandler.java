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

package com.matthewprenger.servertools.permission;

import com.matthewprenger.servertools.permission.elements.Group;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;

import java.util.Collection;

public class EventHandler {

    public EventHandler() {

        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {

        if (PermissionConfig.prefixChatGroupName) {

            Collection<Group> groups = GroupManager.getPlayerGroups(event.username);

            if (!groups.isEmpty()) {
                ChatComponentStyle chatComponent = event.component;

                event.component = new ChatComponentTranslation("");
                for (Group group : groups) {
                    if (group.groupName.equals(PermissionConfig.defaultGroup))
                        continue;

                    EnumChatFormatting color = EnumChatFormatting.getValueByName(group.getChatColor());

                    if (color == null)
                        color = EnumChatFormatting.WHITE;

                    ChatComponentText componentText = new ChatComponentText(color + String.format("[%s] ", group.groupName) + EnumChatFormatting.RESET);

                    event.component.appendSibling(componentText);
                }

                event.component.appendSibling(chatComponent);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (GroupManager.getPlayerGroups(event.player.getCommandSenderName()).isEmpty()) {

            GroupManager.assignDefaultGroup(event.player.getCommandSenderName());
            ChatComponentText componentText = new ChatComponentText(String.format("You have been added to the default group %s", PermissionConfig.defaultGroup));
            componentText.getChatStyle().setItalic(true).setColor(EnumChatFormatting.GOLD);
            event.player.addChatMessage(componentText);
        }
    }
}
