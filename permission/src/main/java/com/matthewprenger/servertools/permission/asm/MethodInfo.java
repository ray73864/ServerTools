package com.matthewprenger.servertools.permission.asm;

import org.objectweb.asm.tree.MethodNode;

public class MethodInfo {

    public final String name;
    public final String obfName;
    public final String desc;
    public final String obfDesc;

    public MethodInfo(String name, String obfName, String desc, String obfDesc) {

        this.name = name;
        this.obfName = obfName;
        this.desc = desc;
        this.obfDesc = obfDesc;
    }

    public boolean matches(MethodNode mn) {

        return mn.name.equals(name) && mn.desc.equals(desc) ||
                mn.name.equals(obfName) && mn.desc.equals(obfDesc);
    }
}
