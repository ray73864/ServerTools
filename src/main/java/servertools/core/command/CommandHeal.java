package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandHeal extends ServerToolsCommand {

    public CommandHeal(String defaultName) {
        super(defaultName);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {

        return par2 == 0;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " {username}";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] astring) {

        if (astring.length < 1) {
            getCommandSenderAsPlayer(sender).heal(Integer.MAX_VALUE);
        } else {
            getPlayer(sender, astring[0]).heal(Integer.MAX_VALUE);
        }
    }
}
