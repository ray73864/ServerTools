package com.matthewprenger.servertools.core.command;

import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.config.ConfigSettings;
import com.matthewprenger.servertools.core.task.RemoveAllTickTask;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashSet;
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

public class CommandRemoveAll extends ServerToolsCommand {

    public CommandRemoveAll(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "/" + name + " [blockID | \"liquid\"] {radius}";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) {

        if (!(sender instanceof EntityPlayerMP))
            throw new WrongUsageException("This command must be used by a player");

        if (strings.length < 1)
            throw new WrongUsageException(getCommandUsage(sender));

        EntityPlayerMP player = (EntityPlayerMP) sender;
        int range = ConfigSettings.DEFAULT_REMOVE_ALL_RANGE;

        if (strings.length >= 2)
            range = Integer.parseInt(strings[1]);

        Set<Integer> blockIdsToClear = new HashSet<>();

        if ("liquid".equalsIgnoreCase(strings[0])) {
            for (Block block : Block.blocksList)
                if (block instanceof BlockFluid)
                    blockIdsToClear.add(block.blockID);
        } else
            blockIdsToClear.add(parseIntBounded(sender, strings[0], 1, 4096));

        ServerTools.instance.taskManager.registerTickTask(new RemoveAllTickTask(player, range, blockIdsToClear));
    }
}
