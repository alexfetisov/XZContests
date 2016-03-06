package org.xzteam.cpphelper.data;

import org.xzteam.cpphelper.constants.Platform;

import java.util.List;

public class ContestBuilder {
    private Platform platform;
    private String title;
    private List<Problem> problems;

    public ContestBuilder setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public ContestBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ContestBuilder setProblems(List<Problem> problems) {
        this.problems = problems;
        return this;
    }

    public Contest createContest() {
        return new Contest(title, platform, problems);
    }
}