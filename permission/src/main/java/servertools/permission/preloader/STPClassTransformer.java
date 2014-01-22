package servertools.permission.preloader;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import servertools.core.STLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class STPClassTransformer implements IClassTransformer {

    private static final List<PatchNotes> patches = new ArrayList<PatchNotes>();

    static {
        PatchNotes commandHandlerPatch = new PatchNotes("net.minecraft.command.CommandHandler", "servertools.permission.preloader.STCommandHandler");
        commandHandlerPatch.addMethodToPatch(new MethodNotes("executeCommand","a","(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)I","(Lab;Ljava/lang/String;)I"));
        patches.add(commandHandlerPatch);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {

        byte[] transformedBytes = bytes;

        PatchNotes patchNotes = getPatchInfo(transformedName);

        if (patchNotes != null)
            transformedBytes = transform(patchNotes, bytes);

        return transformedBytes;
    }

    private byte[] transform(PatchNotes patchNotes, byte[] bytes) {

        boolean patched = false;
        byte[] transformedBytes = bytes;

        ClassNode targetClassNode = new ClassNode();
        ClassReader targetClassReader = new ClassReader(transformedBytes);
        targetClassReader.accept(targetClassNode, 0);

        STLog.fine( "Patching Class: " + patchNotes.targetClass);
        STLog.fine("Mapped Class Name: " + targetClassNode.name);

        Map<MethodNode, MethodNode> replaceMap = new HashMap<MethodNode, MethodNode>();

        for (MethodNode targetMethod : targetClassNode.methods) {
            for (MethodNotes targetMethodNotes : patchNotes.methodsToPatch) {

                if ((targetMethod.name.equals(targetMethodNotes.name) || targetMethod.name.equals(targetMethodNotes.mappedName))
                        && (targetMethod.desc.equals(targetMethodNotes.description) || targetMethod.desc.equals(targetMethodNotes.mappedDesc))) {
                    MethodNode replacementMethod = getReplacementMethod(patchNotes, targetMethodNotes);
                    replaceMap.put(targetMethod, replacementMethod);
                    patched = true;
                }
            }
        }

        if (patched) {

            for (MethodNode method : replaceMap.keySet()) {
                MethodNode replacement = replaceMap.get(method);
                targetClassNode.methods.remove(method);
                targetClassNode.methods.add(replacement);
            }

            ClassWriter targetCW = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            targetClassNode.accept(targetCW);
            transformedBytes = targetCW.toByteArray();
        }

        return transformedBytes;
    }

    private PatchNotes getPatchInfo(String className) {

        for (PatchNotes patchNotes : patches) {
            if (patchNotes.targetClass.equals(className))
                return patchNotes;
        }
        return null;
    }

    private MethodNode getReplacementMethod(PatchNotes patchNotes, MethodNotes methodNotes) {
        try {
            LaunchClassLoader loader = (LaunchClassLoader) this.getClass().getClassLoader();
            ClassNode cn = new ClassNode();
            ClassReader cr = new ClassReader(loader.getClassBytes(patchNotes.replacementClass));
            cr.accept(cn, 0);

            for (MethodNode method : cn.methods) {
                if ((method.name.equals(methodNotes.name) || method.name.equals(methodNotes.mappedName))
                        && (method.desc.equals(methodNotes.description) || method.desc.equals(methodNotes.mappedDesc))) {
                    return method;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
