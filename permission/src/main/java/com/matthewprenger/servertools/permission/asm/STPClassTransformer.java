package com.matthewprenger.servertools.permission.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class STPClassTransformer implements IClassTransformer {

    public static final Set<ClassPatch> classesToPatch = new HashSet<>();

    static {
        ClassPatch chPatch = new ClassPatch("net.minecraft.command.CommandHandler", "com.matthewprenger.servertools.permission.asm.STCommandHandler");
        chPatch.addMethodToPatch(new MethodInfo("executeCommand", "a", "(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)I", "(Lac;Ljava/lang/String;)I"));
        classesToPatch.add(chPatch);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {

        if (bytes == null)
            return null;

        byte[] newBytes = bytes;

        for (ClassPatch patch : classesToPatch) {
            if (patch.name.equals(transformedName))
                newBytes = transform(patch, bytes);
        }

        return newBytes;
    }

    private byte[] transform(ClassPatch classPatch, byte[] bytes) {

        boolean patched = false;
        byte[] newBytes = bytes;

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(newBytes);
        classReader.accept(classNode, 0);

        System.out.println("ServerTools is patching class: " + classPatch.name);
        System.out.println("  Mapped Class Name: " + classNode.name);

        HashMap<MethodNode, MethodNode> patchMap = new HashMap<>();

        for (MethodNode methodNode : classNode.methods) {
            for (MethodInfo methodInfo : classPatch.methodsToPatch) {
                if ((methodNode.name.equals(methodInfo.name) && methodNode.desc.equals(methodInfo.desc)) ||
                        methodNode.name.equals(methodInfo.obfName) && methodNode.desc.equals(methodInfo.obfDesc)) {
                    MethodNode replacementMethod = getReplacementMethod(classPatch, methodInfo);
                    patchMap.put(methodNode, replacementMethod);
                    System.out.println("## Patched: " + methodNode.name + "@" + methodNode.desc + "  ##"); //TODO Logging Debug
                    patched = true;
                }
            }
        }

        try {
        if (patched) {
            for (Map.Entry<MethodNode, MethodNode> entry : patchMap.entrySet()) {
                classNode.methods.remove(entry.getKey());
                classNode.methods.add(entry.getValue());
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            newBytes = classWriter.toByteArray();
        }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }

        return newBytes;
    }

    private MethodNode getReplacementMethod(ClassPatch ci, MethodInfo mi) {

        try {
            LaunchClassLoader loader = (LaunchClassLoader) this.getClass().getClassLoader();
            ClassNode cn = new ClassNode();
            ClassReader cr = new ClassReader(loader.getClassBytes(ci.replacement));
            cr.accept(cn, 0);

            for (MethodNode method : cn.methods) {
                if ((method.name.equals(mi.name) && method.desc.equals(mi.desc))
                        || (method.name.equals(mi.obfName) && method.desc.equals(mi.obfDesc))) {
                    return method;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
