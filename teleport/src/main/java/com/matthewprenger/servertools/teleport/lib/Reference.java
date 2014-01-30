package com.matthewprenger.servertools.teleport.lib;

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

import com.google.common.base.Throwables;

import java.io.InputStream;
import java.util.Properties;

public class Reference {

    static {
        Properties prop = new Properties();

        try {
            InputStream stream = Reference.class.getClassLoader().getResourceAsStream("teleport.properties");
            prop.load(stream);
            stream.close();
        } catch (Exception e) {
            Throwables.propagate(e);
        }

        VERSION = prop.getProperty("version");
    }

    public static final String MOD_ID = "ServerTools|Teleport";
    public static final String MOD_NAME = MOD_ID;
    public static final String VERSION;
    public static final String DEPENDENCIES = "required-after:ServerTools";
}
