package org.xzteam.cpphelper.cli;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class FileUtil {
    public static void generateTree(Path dir, Map<String, String> files) throws IOException {
        for (Map.Entry<String, String> e : files.entrySet()) {
            Path p = dir.resolve(e.getKey());
            if (Files.exists(p)) {
                throw new FileAlreadyExistsException(p.toString());
            }
        }
        for (Map.Entry<String, String> e : files.entrySet()) {
            Path p = dir.resolve(e.getKey());
            Files.createDirectories(p.getParent());
            Files.write(p, e.getValue().getBytes());
        }
    }

    private FileUtil() {};
}
