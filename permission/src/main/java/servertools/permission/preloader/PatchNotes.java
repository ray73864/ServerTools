package servertools.permission.preloader;

import java.util.ArrayList;
import java.util.List;

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