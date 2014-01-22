package servertools.permission.preloader;

public class MethodNotes {

    public final String name;
    public final String mappedName;
    public final String description;
    public final String mappedDesc;

    public MethodNotes(String name, String mappedName, String description, String mappedDescription) {
        this.name = name;
        this.mappedName = mappedName;
        this.description = description;
        this.mappedDesc = mappedDescription;
    }
}