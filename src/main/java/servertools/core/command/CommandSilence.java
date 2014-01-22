package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.ServerTools;
import servertools.core.lib.Strings;

import java.util.List;

public class CommandSilence extends ServerToolsCommand {

    public CommandSilence(String defaultName) {
        super(defaultName);
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {

        return par2 == 1;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return String.format("/%s [add|remove|reload} {username}", name);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] par2) {

        if (par2.length == 1) {
            return getListOfStringsMatchingLastWord(par2, "add", "remove", "reload");
        } else if (par2.length == 2) {
            if (!par2[0].equalsIgnoreCase("reload"))
                return getListOfStringsMatchingLastWord(par2, MinecraftServer.getServer().getAllUsernames());
        }

        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length < 1)
            throw new WrongUsageException(getCommandUsage(sender));

        if (args[0].equalsIgnoreCase("add")) {

            if (args.length == 2) {

                ServerTools.instance.voiceHandler.silence(args[1].toLowerCase());
                notifyAdmins(sender, String.format(Strings.COMMAND_SILENCE_ADD, args[1]));
            } else
                throw new WrongUsageException(getCommandUsage(sender));

        } else if (args[0].equalsIgnoreCase("remove")) {

            if (args.length == 2) {

                boolean result = ServerTools.instance.voiceHandler.removeSilence(args[1].toLowerCase());

                if (result)
                    notifyAdmins(sender, String.format(Strings.COMMAND_SILENCE_REMOVE, args[1]));
                else
                    sender.sendChatToPlayer(ChatMessageComponent.createFromText(Strings.COMMAND_SILENCE_REMOVE_NOUSER).setColor(EnumChatFormatting.RED));
            } else
                throw new WrongUsageException(getCommandUsage(sender));

        } else if (args[0].equalsIgnoreCase("reload")) {

            ServerTools.instance.voiceHandler.loadSilenceList();
            notifyAdmins(sender, Strings.COMMAND_VOICE_RELOAD);
        }
    }
}
