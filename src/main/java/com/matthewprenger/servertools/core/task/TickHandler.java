package com.matthewprenger.servertools.core.task;

/*
 * Copyright 2014 matthewprenger
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import java.util.EnumSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TickHandler implements ITickHandler {

    private final ConcurrentLinkedQueue<ITickTask> tasks = new ConcurrentLinkedQueue<>();

    public TickHandler() {

        TickRegistry.registerTickHandler(this, Side.SERVER);
    }

    public void registerTask(ITickTask task) {

        tasks.offer(task);
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {

        for (ITickTask task : tasks) {

            if (task.isComplete()) {
                task.onComplete();
                tasks.remove(task);
            }

            task.tick();
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

    @Override
    public EnumSet<TickType> ticks() {

        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel() {

        return "ServerTools-TickHandler";
    }
}
