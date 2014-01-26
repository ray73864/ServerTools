package servertools.core.util;

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

import servertools.core.STLog;
import servertools.core.lib.Reference;

import javax.swing.*;

public class Util {

    /**
     * Check to see if a module is the same version as ServerTools Core
     * The game will not run if they aren't equal
     *
     * @param moduleName    the name of the module (Used for error reporting)
     * @param moduleVersion the version of the module
     */
    public static void checkModuleVersion(String moduleName, String moduleVersion) {

        if (!(Reference.VERSION.equals(moduleVersion))) {

            String versionMismatch = "ServerTools is %s, %s is %s";

            STLog.severe("####################################");
            STLog.severe("######### Version Mismatch #########");
            STLog.severe(String.format(versionMismatch, Reference.VERSION, moduleName, moduleVersion));
            STLog.severe("Please download matching versions of ServerTools Modules!");
            STLog.severe("The game will not load");
            STLog.severe("####################################");
            STLog.severe("####################################");

            JEditorPane editorPane = new JEditorPane("text/html",
                    "<html>" + "Version Mismatch: " +
                            String.format(versionMismatch, Reference.VERSION, moduleName, moduleVersion) +
                            "<br>Please download matching ServerTools Module versions" +
                            "</html>");

            editorPane.setEditable(false);
            editorPane.setOpaque(false);

            JOptionPane.showMessageDialog(null, editorPane, "Version Mismatch", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
