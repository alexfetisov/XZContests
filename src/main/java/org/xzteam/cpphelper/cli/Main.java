package org.xzteam.cpphelper.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.PathConverter;
import org.xzteam.cpphelper.cli.commands.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help;

    @Parameter(description = "Output directory", names = {"-d", "--dir"}, converter = PathConverter.class)
    public Path dir = Paths.get(".");

    public static void main(String[] args) {
        Main main = new Main();
        JCommander jc = new JCommander(main);
        jc.setProgramName("cpphelper-cli");
        jc.addCommand(new InitCommand());
        jc.addCommand(new TaskCommand());
        jc.addCommand(new ParseContestCommand());
        jc.addCommand(new ParseCommand());
        try {
            jc.parse(args);
            if (jc.getParsedCommand() == null || main.help) {
                jc.usage();
            } else {
                ((Command) jc.getCommands().get(jc.getParsedCommand()).getObjects().get(0)).execute(main);
            }
        } catch (ParameterException e) {
            jc.usage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
