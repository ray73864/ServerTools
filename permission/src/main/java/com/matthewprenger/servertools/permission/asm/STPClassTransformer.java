package com.matthewprenger.servertools.permission.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;

public class STPClassTransformer implements IClassTransformer {

    private static final String commandHandler = "net.minecraft.command.CommandHandler";
    private static final String stCommandHandler = "com.matthewprenger.servertools.permission.asm.STCommandHandler";
    private static final String srgExecuteCommand = "func_71556_a";

    private static final FMLDeobfuscatingRemapper remapper = FMLDeobfuscatingRemapper.INSTANCE;

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {

        if (bytes == null)
            return null;

        if (commandHandler.equals(transformedName))
            return patchCommandHandler(name, bytes);

        return bytes;
    }

    private static byte[] patchCommandHandler(String obfName, byte[] bytes) {

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        MethodNode source = null;
        MethodNode replacement = null;

        for (MethodNode node : classNode.methods) {
            if (srgExecuteCommand.equals(remapper.mapMethodName(obfName, node.name, node.desc))) {
                System.out.println("Found It"); //TODO
                source = node;
            }
        }

        try {
            ClassNode replacementCH = loadClass(stCommandHandler);

            for (MethodNode methodNode : replacementCH.methods) {
                if (srgExecuteCommand.equals(remapper.mapMethodName(stCommandHandler, methodNode.name, methodNode.desc))) {
                    replacement = methodNode;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (source != null && replacement != null) {
            System.out.println("** Replacing **"); //TODO
            classNode.methods.remove(source);
            classNode.methods.add(replacement);

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }

        return bytes;
    }

    private static ClassNode loadClass(String className) throws IOException {

        LaunchClassLoader loader = (LaunchClassLoader) STPClassTransformer.class.getClassLoader();
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(loader.getClassBytes(className));
        classReader.accept(classNode, 0);
        return classNode;
    }
}
