package com.matthewprenger.servertools.permission.preloader;

import com.matthewprenger.servertools.permission.GroupManager;
import net.minecraft.command.*;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;

import java.util.ArrayList;
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

public class STCommandHandler extends CommandHandler {

    @Override
    public int executeCommand(ICommandSender sender, String input) {

        input = input.trim();

        if (input.startsWith("/")) {
            input = input.substring(1);
        }

        String[] args = input.split(" ");
        String commandName = args[0];

        String[] var1 = new String[args.length - 1];
        System.arraycopy(args, 1, var1, 0, args.length - 1);

        args = var1;

        ICommand icommand = (ICommand) super.getCommands().get(commandName);

        int j = 0;

        try {
            if (icommand == null) {
                throw new CommandNotFoundException();
            }

            if (GroupManager.canUseCommand(sender, icommand)) {

                CommandEvent event = new CommandEvent(icommand, sender, args);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    if (event.exception != null) {
                        throw event.exception;
                    }
                    return 1;
                }

                icommand.processCommand(sender, args);
                ++j;

            } else {
                sender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("commands.generic.permission").setColor(EnumChatFormatting.RED));
            }
        } catch (WrongUsageException wrongusageexception) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.generic.usage", new Object[]{ChatMessageComponent.createFromTranslationWithSubstitutions(wrongusageexception.getMessage(), wrongusageexception.getErrorOjbects())}).setColor(EnumChatFormatting.RED));
        } catch (CommandException commandexception1) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions(commandexception1.getMessage(), commandexception1.getErrorOjbects()).setColor(EnumChatFormatting.RED));
        } catch (Throwable throwable) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("commands.generic.exception").setColor(EnumChatFormatting.RED));
            throwable.printStackTrace();
        }

        return j;
    }

    @Override
    public List getPossibleCommands(ICommandSender sender, String par2Str) {

        String[] args = par2Str.split(" ", -1);
        String commandName = args[0];

        if (args.length == 1) {
            ArrayList<String> arraylist = new ArrayList<>();

            for (Object o : super.getCommands().keySet()) {
                ICommand icommand = (ICommand) super.getCommands().get(o);

                if (icommand != null) {
                    if (CommandBase.doesStringStartWith(commandName, icommand.getCommandName())) {
                        if (GroupManager.canUseCommand(sender, icommand)) {
                            arraylist.add(icommand.getCommandName());
                        }
                    }
                }
            }

            return arraylist;
        } else {
            if (args.length > 1) {
                ICommand icommand = (ICommand) super.getCommands().get(commandName);

                if (icommand != null) {

                    String[] astring1 = new String[args.length - 1];

                    System.arraycopy(args, 1, astring1, 0, args.length - 1);

                    return icommand.addTabCompletionOptions(sender, astring1);
                }
            }

            return null;
        }
    }

    @Override
    public List getPossibleCommands(ICommandSender sender) {

        ArrayList<ICommand> arraylist = new ArrayList<>();

        for (Object o : super.getCommands().keySet()) {
            ICommand icommand = (ICommand) super.getCommands().get(o);

            if (icommand != null) {

                if (GroupManager.canUseCommand(sender, icommand)) {
                    arraylist.add(icommand);
                }
            }
        }

        return arraylist;
    }
}
