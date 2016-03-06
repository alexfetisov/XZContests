package org.xzteam.cpphelper.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.xzteam.cpphelper.cli.Main;
import org.xzteam.cpphelper.data.Contest;
import org.xzteam.cpphelper.parsers.IContestParser;

import java.io.IOException;
import java.net.URL;

@Parameters(commandNames = "parse-contest", commandDescription = "Parse contest and generate tasks")
public class ParseContestCommand implements Command {
    @Parameter(description = "Contest url", names = {"-u", "--url"}, required = true)
    URL url;

    @Override
    public void execute(Main mainArgs) throws IOException {
        Contest contest = IContestParser.parse(url);
        // TODO: create all for contest.
    }
}
