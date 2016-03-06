package org.xzteam.cpphelper.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.xzteam.cpphelper.cli.FileUtil;
import org.xzteam.cpphelper.cli.Main;
import org.xzteam.cpphelper.data.Contest;
import org.xzteam.cpphelper.data.Problem;
import org.xzteam.cpphelper.gen.GenTask;
import org.xzteam.cpphelper.parsers.IContestParser;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

@Parameters(commandNames = "parse-contest", commandDescription = "Parse contest and generate tasks")
public class ParseContestCommand implements Command {
    @Parameter(description = "Contest url", names = {"-u", "--url"}, required = true)
    URL url;

    @Override
    public void execute(Main mainArgs) throws IOException {
        try {
            Contest contest = IContestParser.parse(url);
            // TODO: create all for contest.
        } catch (FileAlreadyExistsException e) {
            System.err.printf("%s is not a directory, aborting.\n", e.getFile());
        }
    }
}
