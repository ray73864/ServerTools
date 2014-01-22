package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CommandWhereIs extends ServerToolsCommand {

    public CommandWhereIs(String defaultName) {
        super(defaultName);
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {

        return par2 == 0;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " [username]";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        if (astring.length < 1)
            throw new WrongUsageException(getCommandUsage(icommandsender));

        EntityPlayerMP player = getPlayer(icommandsender, astring[0]);
        NumberFormat f = new DecimalFormat("#");

        String str = String.format("Player: %s is at X:%s Y:%s Z:%s in Dim:%s", player.username,
                f.format(player.posX), f.format(player.posY), f.format(player.posZ),
                player.worldObj.provider.dimensionId + "-" + player.worldObj.provider.getDimensionName());

        icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(str));
    }
}
