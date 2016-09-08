package org.xzteam.cpphelper.parsers;// Author: alexfetisov

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.xzteam.cpphelper.constants.Platform;
import org.xzteam.cpphelper.data.Contest;
import org.xzteam.cpphelper.data.Problem;
import org.xzteam.cpphelper.data.ProblemSample;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeForcesContestParserTest {

    @Test
    public void testParserFromHTMLParseContestProblemLinks() throws IOException, URISyntaxException {
        final String data = getData("contest");
        final CodeForcesContestParser parser = new CodeForcesContestParser();
        final List<URL> problemURLs = parser.getContestProblemUrls(data);
        final List<URL> expectedList = new ArrayList<URL>();
        for (char letter = 'A'; letter <= 'M'; ++letter) {
            expectedList.add(new URL("http://CODEFORCES.com/contest/589/problem/" + letter));
        }
        Assert.assertEquals(expectedList, problemURLs);
    }

    @Test
    public void testParserFromHTMLParseContestNoProblems() throws IOException, URISyntaxException {
        final String data = getData("contest_no_links");
        final CodeForcesContestParser parser = new CodeForcesContestParser();
        final Contest contest = parser.getContest(data);
        Assert.assertTrue(contest.getTitle().startsWith("2015-2016 ACM-ICPC, NEERC"));
        Assert.assertEquals(Platform.CODEFORCES, contest.getPlatform());
        Assert.assertEquals(0, contest.getProblems().size());
    }

    @Test
    public void testParserFromHTMLSingleProblemContest() throws IOException, URISyntaxException {
        String data = getData("contest_problem");
        CodeForcesContestParser parser = new CodeForcesContestParser();
        final Problem problem = parser.getProblem(data);

        Assert.assertEquals("A", problem.getId());
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

        Assert.assertEquals("D", problem.getId());
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
        Assert.assertEquals(null, parser.getProblemId("Some title"));
        Assert.assertEquals("Z", parser.getProblemId("Z. Some title"));
        Assert.assertEquals("A", parser.getProblemId("A. Some title"));
        Assert.assertEquals("B", parser.getProblemId("B. Some title"));
        Assert.assertEquals("K", parser.getProblemId("K. Some title"));
        Assert.assertEquals(null, parser.getProblemId("a. Some title"));
        Assert.assertEquals(null, parser.getProblemId("B Some title"));
        Assert.assertEquals(null, parser.getProblemId("BC. Some title"));
    }

    @Test
    public void integrationTest() throws IOException {
        CodeForcesContestParser parser = new CodeForcesContestParser();
        final Problem problem = parser.parseSingleProblem(new URL("http://CODEFORCES.com/contest/632/problem/F"));
        Assert.assertEquals(Arrays.asList(
                                new ProblemSample("3\n0 1 2\n1 0 2\n2 2 0\n", "MAGIC\n"),
                                new ProblemSample("2\n0 1\n2 3\n", "NOT MAGIC\n"),
                                new ProblemSample("4\n0 1 2 3\n1 0 3 4\n2 3 0 5\n3 4 5 0\n", "NOT MAGIC\n")),
                            problem.getSamples());
        Assert.assertEquals("Magic Matrix", problem.getTitle());
        Assert.assertEquals(5, problem.getTimeLimit());
        Assert.assertEquals(512, problem.getMemoryLimit());
        Assert.assertEquals("F", problem.getId());
    }

    String getData(final String fileName) throws IOException, URISyntaxException {
        return IOUtils.toString(getClass().getClassLoader().getResource("codeforces/" + fileName + ".html"));
    }
}
