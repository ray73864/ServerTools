package servertools.teleport.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import servertools.core.command.ServerToolsCommand;
import servertools.teleport.HomeManager;

public class CommandHome extends ServerToolsCommand {

    public CommandHome(String defaultName) {
        super(defaultName);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(sender.getCommandSenderName());

        if (args.length == 0) {

            HomeManager.Location home = HomeManager.getHome(player.username, player.worldObj.provider.dimensionId);

            if (home != null) {

                player.setPositionAndUpdate(home.x, home.y, home.z);
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Teleported Home").setColor(EnumChatFormatting.GREEN));
            } else {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Could not find your home").setColor(EnumChatFormatting.RED));
            }
        } else if (args[0].equalsIgnoreCase("set")) {

            HomeManager.setHome(player.username, player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ);
            player.sendChatToPlayer(ChatMessageComponent.createFromText("Set your home").setColor(EnumChatFormatting.GREEN));

        } else if (args[0].equalsIgnoreCase("clear")) {

            if (HomeManager.clearHome(player.username, player.worldObj.provider.dimensionId)) {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Cleared your home").setColor(EnumChatFormatting.GREEN));
            } else {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("You don't have a home to clear!").setColor(EnumChatFormatting.RED));
            }
        } else
            throw new WrongUsageException(getCommandUsage(sender));
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/" + name + "{set|clear}";
    }
}
