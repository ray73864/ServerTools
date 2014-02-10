package com.matthewprenger.servertools.core.command;

import com.matthewprenger.servertools.core.CoreConfig;
import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.lib.Strings;
import com.matthewprenger.servertools.core.task.RemoveAllTickTask;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;

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

        return "/" + name + " [blockName] {radius}";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) {

        if (!(sender instanceof EntityPlayerMP))
            throw new WrongUsageException(Strings.COMMAND_ERROR_ONLYPLAYER);

        if (strings.length < 1)
            throw new WrongUsageException(getCommandUsage(sender));

        EntityPlayerMP player = (EntityPlayerMP) sender;
        int range = CoreConfig.DEFAULT_REMOVE_ALL_RANGE;

        if (strings.length >= 2)
            range = Integer.parseInt(strings[1]);

        Set<Block> blocksToClear = new HashSet<>();

        for (Object obj : GameData.blockRegistry) {
            if (obj instanceof Block) {

                String blockName = ((Block) obj).getUnlocalizedName();
                if (blockName.startsWith("tile."))
                    blockName = blockName.substring(5, blockName.length());

                if (blockName.equalsIgnoreCase(strings[0]) && !(obj == Blocks.air)) {
                    blocksToClear.add((Block) obj);
                }
            }
        }

        ServerTools.instance.tickHandler.registerTask(new RemoveAllTickTask(player, range, blocksToClear));
    }
}
