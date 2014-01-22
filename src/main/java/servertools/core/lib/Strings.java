package servertools.core.lib;

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

public class Strings {

    /* Command Strings */
    public static final String COMMAND_VOICE_ADD = "Gave voice to %s";
    public static final String COMMAND_VOICE_REMOVE = "Removed voice from %s";
    public static final String COMMAND_VOICE_REMOVE_NOUSER = "That user is not voiced";
    public static final String COMMAND_VOICE_RELOAD = "Reloaded the voice list from file";

    public static final String COMMAND_SILENCE_ADD = "Silenced %s";
    public static final String COMMAND_SILENCE_REMOVE = "Removed silence from %s";
    public static final String COMMAND_SILENCE_REMOVE_NOUSER = "That user is not silenced";
    public static final String COMMAND_SILENCE_RELOAD = "Reloaded the silence list from file";

    public static final String ERROR_SILENCED = "You are silenced on this server";
}
