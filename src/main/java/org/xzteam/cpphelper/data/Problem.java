package org.xzteam.cpphelper.data;// Author: alexfetisov

import com.google.common.collect.ImmutableList;
import org.xzteam.cpphelper.constants.Platform;

import java.util.List;

public class Problem {

    Problem(
        final Platform platform,
        final String title,
        String id,
        String contestId,
        int memoryLimit,
        int timeLimit,
        List<ProblemSample> samples) {
        this.platform = platform;
        this.title = title;
        this.id = id;
        this.contestId = contestId;
        this.memoryLimit = memoryLimit;
        this.timeLimit = timeLimit;
        this.samples = ImmutableList.copyOf(samples);
    }

    public ImmutableList<ProblemSample> getSamples() {
        return samples;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public Platform getPlatform() {
        return platform;
    }

    private final String title;
    private final String id;
    private final String contestId;
    private final int memoryLimit;
    private final int timeLimit;
    private final ImmutableList<ProblemSample> samples;
    private final Platform platform;

    public String getDir() {
        if (contestId == null) {
            return platform.name().toLowerCase() + "/" + id;
        }
        return platform.name().toLowerCase() + "/" + contestId + "/" + id;
    }

    public String getPrefix() {
        if (contestId == null) {
            return platform.name().toLowerCase() + id;
        }
        return platform.name().toLowerCase() + "_" + contestId + id;
    }
}
