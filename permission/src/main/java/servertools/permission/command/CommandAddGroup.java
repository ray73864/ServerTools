package servertools.permission.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.command.ServerToolsCommand;
import servertools.permission.GroupManager;
import servertools.permission.elements.GroupException;

public class CommandAddGroup extends ServerToolsCommand {

    public CommandAddGroup(String defaultName) {
        super(defaultName);
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/" + name + " [groupname]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length < 1)
            throw new WrongUsageException(getCommandUsage(sender));

        try {
            GroupManager.createGroup(args[0]);
        } catch (GroupException e) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText(e.toString()).setColor(EnumChatFormatting.RED));
            return;
        }

        notifyAdmins(sender, String.format("Created group: %s", args[0]));
    }
}
