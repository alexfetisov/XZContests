package inliner;

import java.io.*;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;

public class Linker {
    public static void main(String[] args) throws IOException {
        String rootPath = args[0];
        PrintWriter pw = new PrintWriter(System.out);
        inline(getCompiledResource(rootPath), pw);
        pw.close();
    }

    private static void inline(URL root, Writer w) throws IOException {
        HashSet<URL> processed = new HashSet<URL>();
        Queue<URL> queue = new ArrayDeque<URL>();
        queue.add(root);
        processed.add(root);
        while (!queue.isEmpty()) {
            URL resource = queue.poll();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()));
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                String[] parts = line.substring(2).split("->");
                if (!line.startsWith("//") || parts.length != 2) {
                    throw new AssertionError(line);
                }
                URL dependency = getCompiledResource(parts[0]);
                if (!processed.contains(dependency)) {
                    queue.add(dependency);
                    processed.add(dependency);
                }
            }
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                w.append(line).append("\n");
            }
        }
    }

    private static URL getCompiledResource(String path) {
        path = path.replace(".", "/") + ".inl";
        return ClassLoader.getSystemClassLoader().getResource(path);
    }
}
