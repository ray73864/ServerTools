package servertools.teleport.command;

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
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import servertools.core.command.ServerToolsCommand;
import servertools.core.handler.YesNoHandler;
import servertools.teleport.TeleportConfig;
import servertools.teleport.WarpToRequest;

import java.util.List;

public class CommandWarpTo extends ServerToolsCommand {

    public CommandWarpTo(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {

        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {

        return i == 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "/" + name + " [username]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length != 1)
            throw new WrongUsageException(getCommandUsage(sender));

        if (!(sender instanceof EntityPlayerMP))
            throw new WrongUsageException("This command must be used by a player");

        String username = null;
        for (String user : MinecraftServer.getServer().getAllUsernames()) {
            if (user.equalsIgnoreCase(args[0])) {
                username = user;
                break;
            }
        }

        if (Strings.isNullOrEmpty(username))
            throw new PlayerNotFoundException();

        WarpToRequest request = new WarpToRequest(sender.getCommandSenderName(), username);

        if (TeleportConfig.REQUIRE_WARPTO_ACCEPT) {
            YesNoHandler.addYesNoRequest(username, request);
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("Warp request created"));
        } else {
            request.onYes();
            sender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Warped to %s", username)));
        }
    }
}
