package org.xzteam.cpphelper.data;

import org.xzteam.cpphelper.constants.Platform;

import java.util.List;

public class ProblemBuilder {
    private Platform platform;
    private String title;
    private String id;
    private int memoryLimit;
    private int timeLimit;
    private List<ProblemSample> samples;

    public ProblemBuilder setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public ProblemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ProblemBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ProblemBuilder setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
        return this;
    }

    public ProblemBuilder setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
        return this;
    }

    public ProblemBuilder setSamples(List<ProblemSample> samples) {
        this.samples = samples;
        return this;
    }

    public Problem createProblem() {
        return new Problem(platform, title, id, memoryLimit, timeLimit, samples);
    }
}