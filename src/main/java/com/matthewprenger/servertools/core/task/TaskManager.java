package com.matthewprenger.servertools.core.task;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/*
 * Copyright 2014 matthewprenger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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