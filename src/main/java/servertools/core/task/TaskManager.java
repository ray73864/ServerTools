package servertools.core.task;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class TaskManager {

    private final TickTaskManager tickTaskManager;

    public TaskManager() {
        tickTaskManager = new TickTaskManager();
        TickRegistry.registerTickHandler(tickTaskManager, Side.SERVER);
    }

    public void registerTickTask(ITickTask task) {

        tickTaskManager.tickTasks.offer(task);
    }
}
//TODO Work on this