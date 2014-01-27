package servertools.permission.command;

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

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.command.ServerToolsCommand;
import servertools.permission.GroupManager;
import servertools.permission.elements.GroupException;

import java.util.List;
import java.util.Set;

public class CommandSetGroupColor extends ServerToolsCommand {

    public CommandSetGroupColor(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/" + name + " [groupName] [colorIndex]";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

        Set<String> strings = GroupManager.getGroups().keySet();
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, strings.toArray(new String[strings.size()])) : null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length != 2)
            throw new WrongUsageException(getCommandUsage(sender));

        if (!GroupManager.getGroups().containsKey(args[0]))
            throw new PlayerNotFoundException("That group doesn't exist");

        int colorIndex = CommandBase.parseIntBounded(sender, args[1], 0, EnumChatFormatting.values().length - 1);

        try {
            GroupManager.setGroupChatColor(args[0], colorIndex);
        } catch (GroupException e) {
            throw new PlayerNotFoundException("That group doesn't exist");
        }

        sender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Set %s color to %s", args[0], args[1])));
    }
}
