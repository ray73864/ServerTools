package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.ServerTools;
import servertools.core.lib.Strings;

import java.util.List;

public class CommandVoice extends ServerToolsCommand {

    public CommandVoice(String defaultName) {
        super(defaultName);
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
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int index) {

        return index == 1;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length >= 1) {

            if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 2) {
                    ServerTools.instance.voiceHandler.giveVoice(args[1]);
                    notifyAdmins(sender, String.format(Strings.COMMAND_VOICE_ADD, args[1]));
                } else
                    throw new WrongUsageException(getCommandUsage(sender));

            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 2) {
                    if (ServerTools.instance.voiceHandler.removeVoice(args[1])) {
                        notifyAdmins(sender, String.format(Strings.COMMAND_VOICE_REMOVE, args[1]));
                    } else {
                        sender.sendChatToPlayer(ChatMessageComponent.createFromText(Strings.COMMAND_VOICE_REMOVE_NOUSER).setColor(EnumChatFormatting.RED));
                    }
                } else
                    throw new WrongUsageException(getCommandUsage(sender));

            } else if (args[0].equalsIgnoreCase("reload")) {

                ServerTools.instance.voiceHandler.loadVoiceList();
                notifyAdmins(sender, Strings.COMMAND_VOICE_RELOAD);
            } else
                throw new WrongUsageException(getCommandUsage(sender));
        } else
            throw new WrongUsageException(getCommandUsage(sender));
    }
}