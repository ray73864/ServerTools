package servertools.core.command;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import servertools.core.ServerTools;
import servertools.core.config.ConfigSettings;
import servertools.core.task.RemoveAllTickTask;

import java.util.HashSet;
import java.util.Set;

public class CommandRemoveAll extends ServerToolsCommand {

    public CommandRemoveAll(String defaultName) {
        super(defaultName);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "/" + name + " [blockID | \"liquid\"] {radius}";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) {

        if (!(sender instanceof EntityPlayerMP))
            throw new WrongUsageException("This command must be used by a player");

        if (strings.length < 1)
            throw new WrongUsageException(getCommandUsage(sender));

        EntityPlayerMP player = (EntityPlayerMP) sender;
        int range = ConfigSettings.DEFAULT_REMOVE_ALL_RANGE;

        if (strings.length >= 2)
            range = Integer.parseInt(strings[1]);

        Set<Integer> blockIdsToClear = new HashSet<Integer>();

        if (strings[0].equalsIgnoreCase("liquid")) {
            for (Block block : Block.blocksList)
                if (block instanceof BlockFluid)
                    blockIdsToClear.add(block.blockID);
        } else
            blockIdsToClear.add(parseIntBounded(sender, strings[0], 1, 4096));

        ServerTools.instance.taskManager.registerTickTask(new RemoveAllTickTask(player, range, blockIdsToClear));
    }
}
