package org.xzteam.cpphelper.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.xzteam.cpphelper.cli.FileUtil;
import org.xzteam.cpphelper.cli.Main;
import org.xzteam.cpphelper.gen.GenTask;
import org.xzteam.cpphelper.model.TaskId;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

@Parameters(commandNames = "task", commandDescription = "Generate task")
public class TaskCommand implements Command {
    @Parameter(description = "Platform", names = {"-p", "--platform"}, required = true)
    String platform;

    @Parameter(description = "Contest", names = {"-c", "--contest"})
    String contest = null;

    @Parameter(description = "Task", names = {"-t", "--task"}, required = true)
    String task;

    @Override
    public void execute(Main mainArgs) throws IOException {
        try {
            TaskId id = new TaskId(platform, contest, task);
            Map<String, String> treeDef = new GenTask()
                .setId(id)
                .gen();
            FileUtil.generateTree(mainArgs.dir.resolve(id.getDir()), treeDef);
        } catch (FileAlreadyExistsException e) {
            System.err.printf("%s is not a directory, aborting.\n", e.getFile());
        }
    }
}
