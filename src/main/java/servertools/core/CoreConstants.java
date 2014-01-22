package servertools.core;

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

public class CoreConstants {

    public static final String MOD_ID = "ServerTools";
    public static final String MOD_NAME = MOD_ID;
    public static final String VERSION = "%VERSION%";
    public static final String DEPENDENCIES = "required-after:Forge@[9.11.1.953,)";

    public static final String[] DEFAULT_MOTD = new String[] {
            "Hello, $PLAYER$!",
            "This is the default MOTD. In order to change it,",
            "edit the motd.txt in the servertools directory"};

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
}
