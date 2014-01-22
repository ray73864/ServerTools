package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;

public class CommandKillAll extends ServerToolsCommand {

    public CommandKillAll(String defaultName) {
        super(defaultName);
    }

    @Override
    public List<?> addTabCompletionOptions(ICommandSender var1, String[] var2) {

        Collection var = EntityList.classToStringMapping.values();
        return var2.length >= 1 ? getListOfStringsMatchingLastWord(var2, (String[]) var.toArray(new String[var.size()])) : null;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " [entity]";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        if (astring.length < 1)
            throw new WrongUsageException(getCommandUsage(icommandsender));

        String name = null;
        String pname = func_82360_a(icommandsender, astring, 0);
        for (Object obj : EntityList.stringToClassMapping.keySet()) {
            if (obj instanceof String) {
                String ename = obj.toString();
                if (ename.equalsIgnoreCase(pname)) name = ename;
            }

        }

        if (name == null) throw new PlayerNotFoundException("That entity type is unknown");

        int removed = 0;
        for (World world : MinecraftServer.getServer().worldServers) {
            for (Object obj : world.loadedEntityList) {
                if (obj instanceof Entity) {
                    Entity ent = (Entity) obj;
                    String string = EntityList.getEntityString(ent);
                    if (string != null && string.equalsIgnoreCase(name) && !(ent instanceof EntityPlayer)) {
                        world.removeEntity(ent);
                        removed++;
                    }
                }
            }
        }

        notifyAdmins(icommandsender, "Removed " + removed + " entities of type " + name);
    }
}
