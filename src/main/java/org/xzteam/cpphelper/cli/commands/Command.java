package org.xzteam.cpphelper.cli.commands;

import org.xzteam.cpphelper.cli.Main;

import java.io.IOException;

public interface Command {
    void execute(Main mainArgs) throws IOException;
}
