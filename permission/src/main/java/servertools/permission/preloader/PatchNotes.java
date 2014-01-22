package servertools.permission.preloader;

import java.util.ArrayList;
import java.util.List;

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

public class PatchNotes {

    public final String targetClass;
    public final String replacementClass;
    public final List<MethodNotes> methodsToPatch = new ArrayList<MethodNotes>();

    public PatchNotes(String targetClass, String replacementClass) {

        this.targetClass = targetClass;
        this.replacementClass = replacementClass;
    }

    public void addMethodToPatch(MethodNotes methodNotes) {

        methodsToPatch.add(methodNotes);
    }
}