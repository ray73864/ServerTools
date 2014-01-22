package servertools.teleport.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.command.ServerToolsCommand;
import servertools.teleport.TeleportConfig;
import servertools.teleport.TeleportManager;

import java.util.List;
import java.util.Set;

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

public class CommandTeleport extends ServerToolsCommand {

    public CommandTeleport(String defaultName) {
        super(defaultName);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

        Set<String> entries = TeleportManager.teleportMap.keySet();
        return getListOfStringsMatchingLastWord(par2ArrayOfStr, entries.toArray(new String[entries.size()]));
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " [teleportname]";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        if (astring.length < 1)
            throw new WrongUsageException(getCommandUsage(icommandsender));

        if (!(icommandsender instanceof EntityPlayer))
            throw new WrongUsageException("This command must be used by a player");

        EntityPlayer player = (EntityPlayer) icommandsender;

        TeleportManager.Location teleport = TeleportManager.getTeleport(astring[0]);

        if (teleport != null) {

            if (teleport.dimension != player.worldObj.provider.dimensionId) {
                if (!TeleportConfig.ENABLE_TELEPORT_ACROSS_DIMENSION) {
                    icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("Teleporting across dimensions isn't allowed").setColor(EnumChatFormatting.RED));
                    return;
                }

                player.travelToDimension(teleport.dimension);
            }

            player.setPositionAndUpdate(teleport.x, teleport.y, teleport.z);
            icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Teleported to: %s", astring[0])).setColor(EnumChatFormatting.GREEN));

        } else {
            icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("That teleport doesn't exist").setColor(EnumChatFormatting.RED));
        }
    }
}
