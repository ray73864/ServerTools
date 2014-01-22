package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;

import java.util.Arrays;
import java.util.List;

public class CommandKillPlayer extends ServerToolsCommand {

    public CommandKillPlayer(String defaultName) {
        super(defaultName);
    }

    @Override
    public List getCommandAliases() {

        return Arrays.asList("kp");
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

        return "/" + name + " [username]";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        if (astring.length > 0) {
            getPlayer(icommandsender, astring[0]).attackEntityFrom(DamageSource.outOfWorld, Integer.MAX_VALUE);
            notifyAdmins(icommandsender, "Killing: " + astring[0]);
        } else
            throw new WrongUsageException(getCommandUsage(icommandsender));
    }
}
