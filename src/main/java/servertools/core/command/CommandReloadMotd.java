package servertools.core.command;

import net.minecraft.command.ICommandSender;
import servertools.core.ServerTools;

public class CommandReloadMotd extends ServerToolsCommand {

    public CommandReloadMotd(String defaultName) {
        super(defaultName);
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        ServerTools.instance.motd.loadMotd();
        notifyAdmins(icommandsender, "Reloaded the MOTD from file");
    }
}
