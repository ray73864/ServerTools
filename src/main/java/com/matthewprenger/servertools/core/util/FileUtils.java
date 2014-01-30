package com.matthewprenger.servertools.core.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

public class FileUtils {
    public static void writeStringToFile(String string, File file) throws IOException {

        FileWriter writer = new FileWriter(file);
        writer.write(string);
        writer.flush();
        writer.close();
    }
}