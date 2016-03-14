package org.xzteam.cpphelper.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.xzteam.cpphelper.cli.FileUtil;
import org.xzteam.cpphelper.cli.Main;
import org.xzteam.cpphelper.data.Problem;
import org.xzteam.cpphelper.gen.GenTask;
import org.xzteam.cpphelper.parsers.IContestParser;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

@Parameters(commandNames = "parse", commandDescription = "Parse problem and generate task")
public class ParseCommand implements Command {
    @Parameter(description = "Problem url", names = {"-u", "--url"}, required = true)
    URL url;

    @Override
    public void execute(Main mainArgs) throws IOException {
        try {
            Problem problem = IContestParser.parseProblem(url);
            Map<String, String> treeDef = new GenTask()
                .setProblem(problem)
                .gen();
            FileUtil.generateTree(mainArgs.dir.resolve(problem.getDir()), treeDef);
        } catch (FileAlreadyExistsException e) {
            System.err.printf("%s already exists, aborting.\n", e.getFile());
        }
    }
}
