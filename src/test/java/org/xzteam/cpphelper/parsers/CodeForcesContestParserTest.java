package org.xzteam.cpphelper.parsers;// Author: alexfetisov

import org.xzteam.cpphelper.constants.Platform;
import org.xzteam.cpphelper.data.Problem;
import org.xzteam.cpphelper.data.ProblemSample;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class CodeForcesContestParserTest {

    @Test
    public void testParserFromHTMLSingleProblemContest() throws IOException, URISyntaxException {
        String data = getData("contest_problem");
        CodeForcesContestParser parser = new CodeForcesContestParser();
        final Problem problem = parser.getProblem(data);

        Assert.assertEquals(0, problem.getId());
        Assert.assertEquals(256, problem.getMemoryLimit());
        Assert.assertEquals(1, problem.getTimeLimit());
        Assert.assertEquals(Platform.CODEFORCES, problem.getPlatform());
        Assert.assertEquals(2, problem.getSamples().size());

        // Test input/output
        Assert.assertEquals("2 10\nhalf\nhalfplus\n", problem.getSamples().get(0).getInput());
        Assert.assertEquals("15\n", problem.getSamples().get(0).getOutput());

        Assert.assertEquals("3 10\nhalfplus\nhalfplus\nhalfplus\n", problem.getSamples().get(1).getInput());
        Assert.assertEquals("55\n", problem.getSamples().get(1).getOutput());
    }

    @Test
    public void testParserFromHTMLSingleProblemArchieve() throws IOException, URISyntaxException {
        String data = getData("archieve_problem");
        CodeForcesContestParser parser = new CodeForcesContestParser();
        final Problem problem = parser.getProblem(data);

        Assert.assertEquals(3, problem.getId());
        Assert.assertEquals(256, problem.getMemoryLimit());
        Assert.assertEquals(2, problem.getTimeLimit());
        Assert.assertEquals(Platform.CODEFORCES, problem.getPlatform());
        Assert.assertEquals(2, problem.getSamples().size());

        // Test input/output
        Assert.assertEquals("7 8\n6 2 9 2 7 2 3\n", problem.getSamples().get(0).getInput());
        Assert.assertEquals("6 5\n1 2 4 6 7\n", problem.getSamples().get(0).getOutput());

        Assert.assertEquals("6 4\n2 2 2 3 3 3\n", problem.getSamples().get(1).getInput());
        Assert.assertEquals("2 3\n1 2 3\n", problem.getSamples().get(1).getOutput());
    }

    @Test
    public void testProblemIdParsing() {
        CodeForcesContestParser parser = new CodeForcesContestParser();
        Assert.assertEquals(-1, parser.getProblemId("Some title"));
        Assert.assertEquals(25, parser.getProblemId("Z. Some title"));
        Assert.assertEquals(0, parser.getProblemId("A. Some title"));
        Assert.assertEquals(1, parser.getProblemId("B. Some title"));
        Assert.assertEquals(10, parser.getProblemId("K. Some title"));
        Assert.assertEquals(-1, parser.getProblemId("a. Some title"));
        Assert.assertEquals(-1, parser.getProblemId("B Some title"));
        Assert.assertEquals(-1, parser.getProblemId("BC. Some title"));
    }

    @Test
    public void integrationTest() throws IOException {
        CodeForcesContestParser parser = new CodeForcesContestParser();
        final Problem problem = parser.parseSingleProblem(new URL("http://codeforces.com/contest/632/problem/F"));
        Assert.assertEquals(problem.getSamples(),
                            Arrays.asList(
                                new ProblemSample("3\n0 1 2\n1 0 2\n2 2 0\n", "MAGIC\n"),
                                new ProblemSample("2\n0 1\n2 3\n", "NOT MAGIC\n"),
                                new ProblemSample("4\n0 1 2 3\n1 0 3 4\n2 3 0 5\n3 4 5 0\n", "NOT MAGIC\n")));
        Assert.assertEquals(problem.getTitle(), "Magic Matrix");
        Assert.assertEquals(problem.getTimeLimit(), 5);
        Assert.assertEquals(problem.getMemoryLimit(), 512);
    }

    String getData(final String fileName) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("codeforces/" + fileName + ".html").toURI())));
    }
}
