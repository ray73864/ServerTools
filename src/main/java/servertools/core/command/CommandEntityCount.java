package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class CommandEntityCount extends ServerToolsCommand {

    public CommandEntityCount(String defaultName) {
        super(defaultName);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] par2ArrayOfStr) {

        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, (String[]) EntityList.classToStringMapping.values().toArray()) : null;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " {dimension} {entityname}";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (!(sender instanceof EntityPlayer))
            throw new WrongUsageException("Only players can use this command");

        World world = ((EntityPlayer) sender).worldObj;

        Map<String, Integer> entityCount = new HashMap<String, Integer>();

        for (Object obj : world.loadedEntityList) {
            if (obj instanceof Entity) {
                String name = EntityList.getEntityString((Entity) obj);
                if (name != null) {
                    if (entityCount.containsKey(name))
                        entityCount.put(name, entityCount.get(name) + 1);
                    else
                        entityCount.put(name, 1);
                }
            }
        }

        if (args.length == 0) {
            int count = 0;
            for (Map.Entry<String, Integer> entry : entityCount.entrySet()) {
                count += entry.getValue();
            }

            sender.sendChatToPlayer(ChatMessageComponent.createFromText("Entity Count: " + count));
        } else if (args[0].equalsIgnoreCase("all")) {

            sender.sendChatToPlayer(ChatMessageComponent.createFromText("Entity Count:"));

            for (Map.Entry<String, Integer> entry : entityCount.entrySet()) {
                sender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("  %s: %s", entry.getKey(), entry.getValue())));
            }
        } else {

            int count = 0;
            String name = null;
            String pname = func_82360_a(sender, args, 0);
            for (Object obj : EntityList.stringToClassMapping.keySet()) {
                if (obj.toString().equalsIgnoreCase(pname)) name = obj.toString();
            }

            if (name == null) throw new PlayerNotFoundException("That entity doesn't exist");

            for (Object obj : world.loadedEntityList) {
                if (obj instanceof Entity) {
                    Entity ent = (Entity) obj;

                    String string = EntityList.getEntityString(ent);
                    if (string != null && string.equalsIgnoreCase(name)) {
                        count++;
                    }
                }
            }

            sender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Entity: %s, Count: %s", name, count)));
        }

    }
}
