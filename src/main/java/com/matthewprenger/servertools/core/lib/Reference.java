/*
 * Copyright 2014 Matthew Prenger
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

package com.matthewprenger.servertools.core.lib;

import com.matthewprenger.servertools.core.ServerTools;

public class Reference {


    public static final String MOD_ID = "ServerTools";
    public static final String MOD_NAME = MOD_ID;
    public static final String DEPENDENCIES = "required-after:Forge@[10.12.0.1024,)";
    public static final String MC_VERSION = "1.7.2";
    public static final String VERSION;

    public static final String[] AUTHORS = new String[] {"matthewprenger"};

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    static {
        String ver = Reference.class.getPackage().getImplementationVersion();

        if (ver == null) {
            ServerTools.log.warn("ServerTools doesn't have an assigned version number. Unpredictable results may occur");
            VERSION = "UNKNOWN";
        } else
            VERSION = ver;
    }
}
