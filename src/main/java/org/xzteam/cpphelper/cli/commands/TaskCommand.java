package org.xzteam.cpphelper.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.xzteam.cpphelper.cli.FileUtil;
import org.xzteam.cpphelper.cli.Main;
import org.xzteam.cpphelper.constants.Platform;
import org.xzteam.cpphelper.data.Problem;
import org.xzteam.cpphelper.data.ProblemBuilder;
import org.xzteam.cpphelper.gen.GenTask;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Parameters(commandNames = "task", commandDescription = "Generate task")
public class TaskCommand implements Command {
    @Parameter(description = "Platform", names = {"-p", "--platform"}, required = true)
    Platform platform;

    @Parameter(description = "Contest id", names = {"-c", "--contest"})
    String contestId = null;

    @Parameter(description = "Task id", names = {"-t", "--task_id"}, required = true)
    String taskId;

    @Parameter(description = "Time limit", names = {"--tl"})
    int timeLimit = 1;

    @Parameter(description = "Memory limit", names = {"--ml"})
    int memoryLimit = 64;

    @Override
    public void execute(Main mainArgs) throws IOException {
        final Problem problem = new ProblemBuilder()
            .setPlatform(platform)
            .setId(taskId)
            .setContestId(contestId)
            .setSamples(Collections.emptyList())
            .setTimeLimit(timeLimit)
            .setMemoryLimit(memoryLimit)
            .createProblem();
        Map<String, String> treeDef = new GenTask()
            .setProblem(problem)
            .gen();
        FileUtil.generateTree(mainArgs.dir.resolve(problem.getDir()), treeDef);
    }
}
