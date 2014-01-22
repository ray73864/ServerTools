package servertools.permission.command;

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

public class CommandRemoveUser extends ServerToolsCommand {

    public CommandRemoveUser(String defaultName) {
        super(defaultName);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

        if (par2ArrayOfStr.length == 1) {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
        } else if (par2ArrayOfStr.length == 2) {
            Set<String> groupKeys = GroupManager.getGroups().keySet();
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, groupKeys.toArray(new String[groupKeys.size()]));
        } else {
            return null;
        }
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {

        return par2 == 0;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " [username] [groupname]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length != 2)
            throw new WrongUsageException(getCommandUsage(sender));

        try {
            GroupManager.removeUserFromGroup(args[0], args[1]);
        } catch (GroupException e) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText(e.toString()).setColor(EnumChatFormatting.RED));
            return;
        }

        notifyAdmins(sender, String.format("Removed %s from the %s group", args[0], args[1]));
    }
}
