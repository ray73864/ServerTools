package com.matthewprenger.servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

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

public class CommandKillAll extends ServerToolsCommand {

    public CommandKillAll(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<?> addTabCompletionOptions(ICommandSender var1, String[] var2) {

        Collection var = EntityList.classToStringMapping.values();
        return var2.length >= 1 ? getListOfStringsMatchingLastWord(var2, (String[]) var.toArray(new String[var.size()])) : null;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " [entity]";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        if (astring.length < 1)
            throw new WrongUsageException(getCommandUsage(icommandsender));

        String name = null;
        String pname = func_82360_a(icommandsender, astring, 0);
        for (Object obj : EntityList.stringToClassMapping.keySet()) {
            if (obj instanceof String) {
                String ename = obj.toString();
                if (ename.equalsIgnoreCase(pname)) name = ename;
            }

        }

        if (name == null) throw new PlayerNotFoundException("That entity type is unknown");

        int removed = 0;
        for (World world : MinecraftServer.getServer().worldServers) {
            for (Object obj : world.loadedEntityList) {
                if (obj instanceof Entity) {
                    Entity ent = (Entity) obj;
                    String string = EntityList.getEntityString(ent);
                    if (string != null && string.equalsIgnoreCase(name) && !(ent instanceof EntityPlayer)) {
                        world.removeEntity(ent);
                        removed++;
                    }
                }
            }
        }

        notifyAdmins(icommandsender, "Removed " + removed + " entities of type " + name);
    }
}
