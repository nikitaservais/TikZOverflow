package be.ac.ulb.infof307.g06.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Used to get File content to string Java 8 style
 * */
public final class FileUtils {

    /**
     * Read all lines from a file and put them in a string
     * @param filePath path to the file
     */
    public static String fileToString(String filePath) throws IOException{
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8);
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
        if (contentBuilder.length() != 0)
            contentBuilder.deleteCharAt(contentBuilder.length() - 1);
        return contentBuilder.toString();
    }
}
