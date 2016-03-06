package org.xzteam.cpphelper.data;

import com.google.common.collect.ImmutableList;
import org.xzteam.cpphelper.constants.Platform;

import java.net.URL;
import java.util.List;

public class Contest {

    public Contest(String title, Platform platform, List<Problem> problems) {
        this.title = title;
        this.platform = platform;
        this.problems = ImmutableList.copyOf(problems);
    }

    private String title;
    private Platform platform;
    ImmutableList<Problem> problems;

    public ImmutableList<Problem> getProblems() {
        return problems;
    }

    public String getTitle() {
        return title;
    }

    public Platform getPlatform() {
        return platform;
    }
}
