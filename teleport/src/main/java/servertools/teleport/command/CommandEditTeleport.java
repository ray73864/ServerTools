package servertools.teleport.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.command.ServerToolsCommand;
import servertools.teleport.TeleportManager;

import java.util.List;
import java.util.Set;

public class CommandEditTeleport extends ServerToolsCommand {

    public CommandEditTeleport(String defaultName) {
        super(defaultName);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {


        if (par2ArrayOfStr.length == 1 && par2ArrayOfStr[0].equalsIgnoreCase("delete")) {
            Set<String> var = TeleportManager.teleportMap.keySet();
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, var.toArray(new String[var.size()]));
        }

        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " [set|delete] [teleportname]";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        if (astring.length < 2)
            throw new WrongUsageException(getCommandUsage(icommandsender));

        if (!(icommandsender instanceof EntityPlayer))
            throw new WrongUsageException("This command must be used by a player");

        EntityPlayer player = (EntityPlayer) icommandsender;

        if (astring[0].equalsIgnoreCase("set")) {

            TeleportManager.setTeleport(astring[1], new TeleportManager.Location(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ));
            icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Set teleport: %s", astring[1])).setColor(EnumChatFormatting.GREEN));

        } else if (astring[0].equalsIgnoreCase("delete")) {

            if (TeleportManager.removeTeleport(astring[1])) {
                icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Removed teleport: %s", astring[1])).setColor(EnumChatFormatting.GREEN));
            } else
                throw new PlayerNotFoundException("That teleport doesn't exist");

        } else
            throw new WrongUsageException(getCommandUsage(icommandsender));
    }
}
