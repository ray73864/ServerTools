package servertools.core.task;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import java.util.EnumSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TickTaskManager implements ITickHandler {

    protected final ConcurrentLinkedQueue<ITickTask> tickTasks = new ConcurrentLinkedQueue<ITickTask>();

    @Override
    public void tickStart(EnumSet<TickType> tickTypes, Object... objects) {

        if (tickTasks.isEmpty())
            return;

        for (ITickTask tickTask : tickTasks) {

            if (tickTask.isComplete()) {
                tickTask.onComplete();
                tickTasks.remove(tickTask);
            } else {
                tickTask.tick();
            }
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> tickTypes, Object... objects) {}

    @Override
    public EnumSet<TickType> ticks() {

        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel() {

        return "ServerTools-TickTaskManager";
    }
}
