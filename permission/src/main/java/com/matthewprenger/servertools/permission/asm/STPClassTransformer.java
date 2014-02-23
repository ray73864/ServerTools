/*
 * Copyright 2014 Matthew Prenger
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

package com.matthewprenger.servertools.permission.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class STPClassTransformer implements IClassTransformer {

    private static final FMLDeobfuscatingRemapper remapper = FMLDeobfuscatingRemapper.INSTANCE;
    private static final Set<PatchNote> patches = new HashSet<>();

    static {

        PatchNote chPatch = new PatchNote("net.minecraft.command.CommandHandler", "com.matthewprenger.servertools.permission.asm.STCommandHandler");
        chPatch.addMethodToPatch(new MethodNote("executeCommand", "func_71556_a", "(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)I"));
        chPatch.addMethodToPatch(new MethodNote("getPossibleCommands", "func_71558_b", "(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)Ljava/util/List;"));
        chPatch.addMethodToPatch(new MethodNote("getPossibleCommands", "func_71557_a", "(Lnet/minecraft/command/ICommandSender;)Ljava/util/List;"));
        patches.add(chPatch);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {

        if (bytes == null)
            return null;


        for (PatchNote patchNote : patches) {
            if (patchNote.sourceClass.equals(transformedName)) {
                STPermissionPlugin.log.info("Found Class To Patch, Name:{}, TransformedName:{}", name, transformedName);
                return transform(name, patchNote, bytes);
            }
        }

        return bytes;
    }

    private static byte[] transform(String obfName, PatchNote patchNote, byte[] bytes) {

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        if (patchNote.methodsToPatch.isEmpty())
            return bytes;

        for (MethodNote methodNote : patchNote.methodsToPatch) {

            MethodNode sourceMethod = null;
            MethodNode replacementMethod = null;

            try {

                for (MethodNode method : classNode.methods) {
                    if (methodNote.srgMethodName.equals(remapper.mapMethodName(obfName, method.name, method.desc))) {
                    STPermissionPlugin.log.trace("Found Method to Patch: {}@{}", method.name, method.desc);
                        sourceMethod = method;
                        break;
                    } else if (methodNote.methodName.equals(method.name) && methodNote.deobfDesc.equals(method.desc)) {
                    STPermissionPlugin.log.trace("Found Deobfuscated Method to Patch: {}@{}", method.name, method.desc);
                        sourceMethod = method;
                    }
                }


                ClassNode replacementClass = loadClass(patchNote.replacementClass);
                for (MethodNode method : replacementClass.methods) {
                    if (methodNote.srgMethodName.equals(remapper.mapMethodName(patchNote.replacementClass, method.name, method.desc))) {
                    STPermissionPlugin.log.trace("Found Replacement Method: {}@{}", method.name, method.desc);
                        replacementMethod = method;
                        break;
                    } else if (methodNote.methodName.equals(method.name) && methodNote.deobfDesc.equals(method.desc)) {
                    STPermissionPlugin.log.trace("Found Deobfuscated Replacement Method: {}@{}", method.name, method.desc);
                        replacementMethod = method;
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace(System.err);
                STPermissionPlugin.log.warn("Failed to Map Replacement Method: {}", methodNote.methodName, t);
            }

            if (sourceMethod != null && replacementMethod != null) {
                STPermissionPlugin.log.info("Successfully Mapped Method to be Replaced");
                STPermissionPlugin.log.debug("  Source: {}@{} Replacement: {}@{}", sourceMethod.name, sourceMethod.desc, replacementMethod.name, replacementMethod.desc);
                classNode.methods.remove(sourceMethod);
                classNode.methods.add(replacementMethod);

            } else {
                STPermissionPlugin.log.info("Couldn't match methods to patch, skipping");
                return bytes;
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private static ClassNode loadClass(String className) throws IOException {

        LaunchClassLoader loader = (LaunchClassLoader) STPClassTransformer.class.getClassLoader();
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(loader.getClassBytes(className));
        classReader.accept(classNode, 0);
        return classNode;
    }

    private static class PatchNote {

        public final String sourceClass;
        public final String replacementClass;

        public final Set<MethodNote> methodsToPatch = new HashSet<>();

        public PatchNote(String sourceClass, String replacementClass) {
            this.sourceClass = sourceClass;
            this.replacementClass = replacementClass;
        }

        public void addMethodToPatch(MethodNote methodNote) {

            methodsToPatch.add(methodNote);
        }
    }

    private static class MethodNote {

        public final String methodName;
        public final String srgMethodName;
        public final String deobfDesc;

        public MethodNote(String methodName, String srgMethodName, String deobfDesc) {

            this.methodName = methodName;
            this.srgMethodName = srgMethodName;
            this.deobfDesc = deobfDesc;
        }
    }

}
