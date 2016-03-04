package org.xzteam.cpphelper.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.xzteam.cpphelper.cli.FileUtil;
import org.xzteam.cpphelper.cli.Main;
import org.xzteam.cpphelper.gen.GenProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Parameters(commandNames = "init", commandDescription = "Generate project")
public class InitCommand implements Command {
    @Parameter(description = "CMake project name", names = {"-n", "--name"})
    String name = "CPPHelper";

    @Parameter(description = "System includes (used by inliner)", names = {"-I", "--include"})
    List<String> includes = getSystemIncludes();

    @Override
    public void execute(Main mainArgs) throws IOException {
        try {
            Map<String, String> treeDef = new GenProject()
                .setName(name)
                .setIncludes(includes)
                .gen();
            FileUtil.generateTree(mainArgs.dir, treeDef);
        } catch (FileAlreadyExistsException e) {
            System.err.printf("%s is not a directory, aborting.\n", e.getFile());
        }
    }

    private static List<String> getSystemIncludes() {
        try {
            Process p = new ProcessBuilder()
                .command("g++", "-E", "-x", "c++", "-", "-v")
                .redirectInput(ProcessBuilder.Redirect.from(new File("/dev/null")))
                .redirectError(ProcessBuilder.Redirect.PIPE).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            boolean inList = false;
            List<String> ret = new ArrayList<>();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if ("End of search list.".equals(line)) {
                    inList = false;
                }
                if (inList && !line.endsWith("(framework directory)")) {
                    ret.add(line.trim());
                }
                if ("#include <...> search starts here:".equals(line)) {
                    inList = true;
                }
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
