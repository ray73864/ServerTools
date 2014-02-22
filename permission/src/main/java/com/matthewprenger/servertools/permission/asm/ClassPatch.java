package com.matthewprenger.servertools.permission.asm;

import java.util.HashSet;
import java.util.Set;

public class ClassPatch {

    public final String name;
    public final String replacement;
    public final Set<MethodInfo> methodsToPatch = new HashSet<>();

    public ClassPatch(String name, String replacement) {

        this.name = name;
        this.replacement = replacement;
    }

    public void addMethodToPatch(MethodInfo methodInfo) {

        methodsToPatch.add(methodInfo);
    }
}
