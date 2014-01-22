package servertools.permission.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.command.ServerToolsCommand;
import servertools.permission.GroupManager;
import servertools.permission.elements.GroupException;

import java.util.List;
import java.util.Set;

public class CommandRemoveGroup extends ServerToolsCommand {

    public CommandRemoveGroup(String defaultName) {
        super(defaultName);
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/" + name + " [groupname]";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] strings) {

        Set<String> groupKeys = GroupManager.getGroups().keySet();
        return getListOfStringsMatchingLastWord(strings, groupKeys.toArray(new String[groupKeys.size()]));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length < 1)
            throw new WrongUsageException(getCommandUsage(sender));

        try {
            GroupManager.removeGroup(args[0]);
        } catch (GroupException e) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText(e.toString()).setColor(EnumChatFormatting.RED));
            return;
        }

        notifyAdmins(sender, String.format("Deleted group: %s", args[0]));
    }
}
