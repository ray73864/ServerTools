package com.matthewprenger.servertools.permission.preloader;

import com.google.common.eventbus.EventBus;
import com.matthewprenger.servertools.permission.Reference;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class STCoreModContainer extends DummyModContainer {

    public STCoreModContainer() {

        super(new ModMetadata());
        ModMetadata meta = super.getMetadata();
        meta.modId = "ST|Permission|Preloader";
        meta.name = "ServerTools|Permission Preloader";
        meta.description = "Sets up stuff for ServerTools Permissions";
        meta.parent = Reference.MOD_ID;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {

        bus.register(this);
        return true;
    }
}
