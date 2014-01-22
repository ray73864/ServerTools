package servertools.permission.preloader;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import servertools.permission.GroupManager;

public class STCommandHandler extends CommandHandler {

    @Override
    public int executeCommand(ICommandSender sender, String input) {

        input = input.trim();

        if (input.startsWith("/")) {
            input = input.substring(1);
        }

        String[] args = input.split(" ");
        String commandName = args[0];

        String[] var1 = new String[args.length - 1];
        System.arraycopy(args, 1, var1, 0, args.length - 1);

        args = var1;

        ICommand icommand = (ICommand) super.getCommands().get(commandName);

        int j = 0;

        try {
            if (icommand == null) {
                throw new CommandNotFoundException();
            }

            boolean canUse;

            if (!(sender instanceof EntityPlayerMP)) {
                canUse = icommand.canCommandSenderUseCommand(sender);
            } else {
                canUse = GroupManager.canUseCommand(sender.getCommandSenderName(), icommand.getCommandName());
            }

            if (canUse) {
                CommandEvent event = new CommandEvent(icommand, sender, args);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    if (event.exception != null) {
                        throw event.exception;
                    }
                    return 1;
                }

                icommand.processCommand(sender, args);
                ++j;

            } else {
                sender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("commands.generic.permission").setColor(EnumChatFormatting.RED));
            }
        } catch (WrongUsageException wrongusageexception) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("commands.generic.usage", new Object[]{ChatMessageComponent.createFromTranslationWithSubstitutions(wrongusageexception.getMessage(), wrongusageexception.getErrorOjbects())}).setColor(EnumChatFormatting.RED));
        } catch (CommandException commandexception1) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions(commandexception1.getMessage(), commandexception1.getErrorOjbects()).setColor(EnumChatFormatting.RED));
        } catch (Throwable throwable) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("commands.generic.exception").setColor(EnumChatFormatting.RED));
            throwable.printStackTrace();
        }

        return j;
    }
}
