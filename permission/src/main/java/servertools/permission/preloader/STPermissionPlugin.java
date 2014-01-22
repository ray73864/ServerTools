package servertools.permission.preloader;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.6.4")
public class STPermissionPlugin implements IFMLLoadingPlugin, IFMLCallHook {

    @Override
    public Void call() throws Exception {
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
