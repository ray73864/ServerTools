package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import servertools.core.ServerTools;

public class CommandMotd extends ServerToolsCommand {

    public CommandMotd(String defaultName) {
        super(defaultName);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (sender instanceof EntityPlayer) {

            ServerTools.instance.motd.serveMotd((EntityPlayer) sender);
        } else
            throw new WrongUsageException("This command must be used by a player");
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/" + name;
    }
}