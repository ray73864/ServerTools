package servertools.permission.preloader;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

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

@IFMLLoadingPlugin.TransformerExclusions({"servertools.permission.preloader"})
@IFMLLoadingPlugin.MCVersion("1.6.4")
public class STPermissionPlugin implements IFMLLoadingPlugin, IFMLCallHook {

    @Override
    public Void call() throws Exception {
        System.out.println("Initializing ServerTools Permission CoreMod");
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {"servertools.permission.preloader.STPClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {

        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }
}
