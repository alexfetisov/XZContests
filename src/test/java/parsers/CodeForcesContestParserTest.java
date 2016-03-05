package parsers;// Author: alexfetisov

import com.google.common.collect.ImmutableList;
import com.sun.deploy.util.StringUtils;
import com.sun.org.glassfish.external.arc.Taxonomy;
import constants.Platform;
import data.Problem;
import data.ProblemSample;
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.requests.FilterRequest;
import parsers.CodeForcesContestParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeForcesContestParserTest {

    @Test
    public void testParserFromHTMLSingleProblemContest() {
        String data = null;
        try {
            data = getData("contest_problem");
        } catch (Exception e) {
            Assert.fail();
        }
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
    public void testParserFromHTMLSingleProblemArchieve() {
        String data = null;
        try {
            data = getData("archieve_problem");
        } catch (Exception e) {
            Assert.fail();
        }
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

        Assert.assertEquals("6 4\n2 2 2 3 3 3\n", problem.getSamples().get(0).getInput());
        Assert.assertEquals("2 3\n1 2 3\n", problem.getSamples().get(0).getOutput());
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

    String getData(final String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(
            new FileReader("./test_resources/codeforces/" + fileName + ".html"));
        List<String> lines = new ArrayList<>();
        String line;
        while((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return StringUtils.join(lines, "\n");
    }
}
