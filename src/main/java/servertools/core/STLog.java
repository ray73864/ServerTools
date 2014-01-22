package servertools.core;

import cpw.mods.fml.common.FMLLog;
import servertools.core.config.ConfigSettings;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.*;

public class STLog {

    private static final Logger stLogger = Logger.getLogger(CoreConstants.MOD_ID);

    static {
        stLogger.setParent(FMLLog.getLogger());

        try {
            FileHandler fileHandler = new FileHandler(ServerTools.serverToolsDir.getAbsolutePath() + File.separator + "servertools.log");
            fileHandler.setFormatter(new STLogFormatter());
            stLogger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            stLogger.warning("Error adding file output to the servertools log");
        }

    }

    public static void log(Level level, Object object) {

        if (object != null) {
            stLogger.log(level, object.toString());
        } else {
            stLogger.log(level, "null");
        }
    }

    public static void debug(Object object) {
        if (ConfigSettings.DEBUG_MODE) log(Level.INFO, object);
    }

    public static void severe(Object object) {
        log(Level.SEVERE, object);
    }

    public static void warning(Object object) {
        log(Level.WARNING, object);
    }

    public static void info(Object object) {
        log(Level.INFO, object);
    }

    public static void config(Object object) {
        log(Level.CONFIG, object);
    }

    public static void fine(Object object) {
        log(Level.FINE, object);
    }

    public static void finer(Object object) {
        log(Level.FINER, object);
    }

    public static void finest(Object object) {
        log(Level.FINEST, object);
    }

    public static Logger getLogger() {

        return stLogger;
    }

    public static Logger getModuleLogger(String moduleName) {

        Logger childLogger = Logger.getLogger(moduleName);
        childLogger.setParent(getLogger());
        return childLogger;
    }

    /**
     * Log Formatter for the ServerTools log
     */
    private static class STLogFormatter extends Formatter {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
        public String format(LogRecord record) {
            StringBuilder msg = new StringBuilder();
            msg.append(this.dateFormat.format(record.getMillis()));
            Level lvl = record.getLevel();

            String name = lvl.getLocalizedName();
            if (name == null) {
                name = lvl.getName();
            }

            if ((name != null) && (name.length() > 0)) {
                msg.append(" [").append(name).append("] ");
            } else {
                msg.append(" ");
            }

            msg.append(formatMessage(record));
            msg.append(CoreConstants.LINE_SEPARATOR);
            Throwable thr = record.getThrown();

            if (thr != null) {
                StringWriter thrDump = new StringWriter();
                thr.printStackTrace(new PrintWriter(thrDump));
                msg.append(thrDump.toString());
            }

            return msg.toString();
        }

    }
}