package servertools.permission.command;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.command.ServerToolsCommand;
import servertools.permission.GroupManager;
import servertools.permission.elements.GroupException;

import java.util.List;
import java.util.Set;

public class CommandAddCommand extends ServerToolsCommand {

    public CommandAddCommand(String defaultName) {
        super(defaultName);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] strings) {

        if (strings.length == 1) {
            CommandHandler ch = (CommandHandler) MinecraftServer.getServer().getCommandManager();
            return ch.getPossibleCommands(sender, strings[0]);
        } else if (strings.length == 2) {
            Set<String> groupKeys = GroupManager.getGroups().keySet();
        return getListOfStringsMatchingLastWord(strings, groupKeys.toArray(new String[groupKeys.size()]));
        } else
            return null;

    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "/" + name + " [commandname] [groupname]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) {

        if (strings.length != 2)
            throw new WrongUsageException(getCommandUsage(sender));

        try {
            GroupManager.addAllowedCommand(strings[0], strings[1]);
        } catch (GroupException e) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText(e.toString()).setColor(EnumChatFormatting.RED));
            return;
        }

        notifyAdmins(sender, String.format("Added command %s to %s", strings[0], strings[1]));
    }
}
