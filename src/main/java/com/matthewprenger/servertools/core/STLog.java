package com.matthewprenger.servertools.core;

import cpw.mods.fml.common.FMLLog;

import java.util.logging.Level;
import java.util.logging.Logger;

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

public class STLog {

    private final Logger stLogger;

    public STLog(String logName) {
        stLogger = Logger.getLogger(logName);
        stLogger.setParent(FMLLog.getLogger());
    }

    public void log(Level level, Object object) {

        if (object != null) {
            stLogger.log(level, object.toString());
        } else {
            stLogger.log(level, "null");
        }
    }

    public void debug(Object object) {
        if (CoreConfig.DEBUG_MODE) log(Level.INFO, object);
    }

    public void severe(Object object) {
        log(Level.SEVERE, object);
    }

    public void warning(Object object) {
        log(Level.WARNING, object);
    }

    public void info(Object object) {
        log(Level.INFO, object);
    }

    public void config(Object object) {
        log(Level.CONFIG, object);
    }

    public void fine(Object object) {
        log(Level.FINE, object);
    }

    public void finer(Object object) {
        log(Level.FINER, object);
    }

    public void finest(Object object) {
        log(Level.FINEST, object);
    }

    public Logger getLogger() {

        return stLogger;
    }
}