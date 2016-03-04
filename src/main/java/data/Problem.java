package data;// Author: alexfetisov

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import constants.Sourse;
import parsers.CodeForcesContestParser;
import parsers.IContestParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Problem {

    public Problem(final Sourse source, final String urlString) {
        try {
            new Problem(source, new URL(urlString));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Problem(final Sourse source, final URL url) throws NotImplementedException {
        IContestParser parser;
        if (source == Sourse.CODEFORCES) {
            parser = new CodeForcesContestParser();
        } else {
            throw new NotImplementedException();
        }

        Problem problem = parser.parseSingleProblem(url);
        new Problem(problem);
    }

    public Problem(final Problem other) {
        new Problem(other.getTitle(), other.getId(), other.getMemoryLimit(), other.getTimeLimit());
        this.addSamples(other.samples);
    }

    private void addSamples(List<ProblemSample> samples) {
        this.samples = samples;
    }

    public Problem(final String title, int id, int memoryLimit, int timeLimit) {
        this.title = title;
        this.id = id;
        this.memoryLimit = memoryLimit;
        this.timeLimit = timeLimit;
        this.samples = new ArrayList<ProblemSample>();
    }

    public void addSample(final ProblemSample sample) {
        samples.add(sample);
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

    private String title;
    private int id;
    private int memoryLimit;
    private int timeLimit;
    private List<ProblemSample> samples;
    private Sourse source;
}
