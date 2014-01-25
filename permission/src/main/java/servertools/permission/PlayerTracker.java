package servertools.permission;

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

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

public class PlayerTracker implements IPlayerTracker {

    public PlayerTracker() {

        GameRegistry.registerPlayerTracker(this);
    }

    @Override
    public void onPlayerLogin(EntityPlayer entityPlayer) {

        if (GroupManager.getPlayerGroups(entityPlayer.username).isEmpty()) {

            GroupManager.assignDefaultGroup(entityPlayer.username);
            entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("You have been added to the default group: %s", PermissionConfig.defaultGroup)).setItalic(true).setColor(EnumChatFormatting.GOLD));
        }
    }

    @Override
    public void onPlayerLogout(EntityPlayer entityPlayer) {

    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer entityPlayer) {

    }

    @Override
    public void onPlayerRespawn(EntityPlayer entityPlayer) {

    }
}
