package servertools.core;

public class CoreConstants {

    public static final String MOD_ID = "ServerTools";
    public static final String MOD_NAME = MOD_ID;
    public static final String VERSION = "%VERSION%";
    public static final String DEPENDENCIES = "required-after:Forge@[9.11.1.953,)";

    public static final String[] DEFAULT_MOTD = new String[] {
            "Hello, $PLAYER$!",
            "This is the default MOTD. In order to change it,",
            "edit the motd.txt in the servertools directory"};

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
}
