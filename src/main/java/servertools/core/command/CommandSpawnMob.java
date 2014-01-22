package servertools.core.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandSpawnMob extends ServerToolsCommand {

    public CommandSpawnMob(String defaultName) {
        super(defaultName);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
        List<?> var = getValidEntities();
        return var2.length >= 1 ? getListOfStringsMatchingLastWord(var2, (String[]) var.toArray()) : null;
    }

    @SuppressWarnings("unchecked")
    private List<?> getValidEntities() {
        List<String> ret = new ArrayList<String>();
        for (String name : ((Map<String, Class<?>>) EntityList.stringToClassMapping).keySet()) {
            Class<?> clazz = (Class<?>) EntityList.stringToClassMapping.get(name);
            if (EntityLiving.class.isAssignableFrom(clazz)) ret.add(name);
        }
        return ret;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " [mobname] {ammount}";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (!(sender instanceof EntityPlayer))
            throw new WrongUsageException("This command must be used by a player");

        EntityPlayer player = (EntityPlayer) sender;

        if (args.length < 1) throw new WrongUsageException(getCommandUsage(sender));

        int amount = 1;
        if (args.length > 1) amount = parseIntBounded(sender, args[1], 1, 100);

        Class<?> clazz = null;
        String type = "Unknown";
        for (String name : ((Map<String, Class<?>>) EntityList.stringToClassMapping).keySet()) {
            if (name.equalsIgnoreCase(args[0])) {
                clazz = (Class<?>) EntityList.stringToClassMapping.get(name);
                type = name;
                break;
            }
        }
        if (clazz == null || !EntityLiving.class.isAssignableFrom(clazz))
            throw new PlayerNotFoundException("That entity type is unknown");

        try {
            Constructor<?> ctor = clazz.getConstructor(World.class);
            for (int i = 0; i < amount; i++) {
                Entity ent = (Entity) ctor.newInstance(player.worldObj);
                ent.setPosition(player.posX, player.posY, player.posZ);
                player.worldObj.spawnEntityInWorld(ent);
            }
        } catch (Throwable e) {
            throw new PlayerNotFoundException("That entity type is unknown");
        }

        notifyAdmins(sender, "Spawned " + amount + " " + type);
    }
}
