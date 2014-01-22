package servertools.core.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void writeStringToFile(String string, File file) throws IOException {

        FileWriter writer = new FileWriter(file);
        writer.write(string);
        writer.flush();
        writer.close();
    }
}
