package data;// Author: alexfetisov

import com.google.common.collect.ImmutableList;
import constants.Platform;

import java.util.List;

public class Problem {

    Problem(final Platform platform, final String title, int id, int memoryLimit, int timeLimit, List<ProblemSample> samples) {
        this.platform = platform;
        this.title = title;
        this.id = id;
        this.memoryLimit = memoryLimit;
        this.timeLimit = timeLimit;
        this.samples = ImmutableList.copyOf(samples);
    }

    public List<ProblemSample> getSamples() {
        return samples;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
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

    //TODO add optional contest field
    private final String title;
    private final int id;
    private final int memoryLimit;
    private final int timeLimit;
    private final List<ProblemSample> samples;
    private final Platform platform;
}
