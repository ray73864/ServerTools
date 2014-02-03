package com.matthewprenger.servertools.core.task;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import java.util.EnumSet;
import java.util.concurrent.ConcurrentLinkedQueue;

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

public class TickTaskManager implements ITickHandler {

    protected final ConcurrentLinkedQueue<ITickTask> tickTasks = new ConcurrentLinkedQueue<>();

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
