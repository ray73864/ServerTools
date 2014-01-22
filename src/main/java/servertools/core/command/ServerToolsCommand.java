package servertools.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;

public abstract class ServerToolsCommand extends CommandBase {

    public String name;
    public final String defaultName;

    public ServerToolsCommand(String defaultName) {

        this.defaultName = defaultName;
    }

    public String getCommandName() {

        return name;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Object o) {
        if (o instanceof ICommand) {
            return this.compareTo((ICommand) o);
        } else {
            return 0;
        }
    }
}
