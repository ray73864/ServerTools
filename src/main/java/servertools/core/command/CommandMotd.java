package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import servertools.core.ServerTools;

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

public class CommandMotd extends ServerToolsCommand {

    public CommandMotd(String defaultName) {
        super(defaultName);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (sender instanceof EntityPlayer) {

            ServerTools.instance.motd.serveMotd((EntityPlayer) sender);
        } else
            throw new WrongUsageException("This command must be used by a player");
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/" + name;
    }
}