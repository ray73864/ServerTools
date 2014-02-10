package com.matthewprenger.servertools.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        stLogger = LogManager.getLogger(logName);
    }

    public void log(Level level, Object object) {

        if (object != null) {
            stLogger.log(level, object.toString());
        } else {
            stLogger.log(level, "null");
        }
    }

    public void debug(Object object) {
        if (CoreConfig.DEBUG_MODE) log(Level.DEBUG, object);
    }

    public void fatal(Object object) {
        log(Level.FATAL, object);
    }

    public void error(Object object) {
        log(Level.ERROR, object);
    }

    public void warn(Object object) {
        log(Level.WARN, object);
    }

    public void info(Object object) {
        log(Level.INFO, object);
    }

    public void trace(Object object) {
        log(Level.TRACE, object);
    }

    public Logger getLogger() {

        return stLogger;
    }
}