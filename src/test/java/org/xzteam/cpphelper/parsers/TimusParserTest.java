package org.xzteam.cpphelper.parsers;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.xzteam.cpphelper.constants.Platform;
import org.xzteam.cpphelper.data.Problem;
import org.xzteam.cpphelper.data.ProblemSample;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class TimusParserTest {

    @Test
    public void testParserFromHTMLSingleProblem() throws IOException, URISyntaxException {
        String data = getData("problem");
        TimusParser parser = new TimusParser();
        final Problem problem = parser.getProblem(data);

        Assert.assertEquals("1491", problem.getId());
        Assert.assertEquals(64, problem.getMemoryLimit());
        Assert.assertEquals(1, problem.getTimeLimit());
        Assert.assertEquals(Platform.TIMUS, problem.getPlatform());
        Assert.assertEquals(2, problem.getSamples().size());

        // Test input/output
        Assert.assertEquals("4\n2 3 2\n2 4 1\n3 4 1\n1 2 1\n1 1 1\n",
            problem.getSamples().get(0).getInput());
        Assert.assertEquals("2 4 4 2\n", problem.getSamples().get(0).getOutput());

        Assert.assertEquals("7\n1 7 1\n2 3 4\n3 5 3\n1 2 1\n5 7 4\n2 4 10\n3 4 2\n1 6 3\n",
            problem.getSamples().get(1).getInput());
        Assert.assertEquals("5 19 23 19 11 8 5\n", problem.getSamples().get(1).getOutput());
    }

    @Test
    public void integrationTest() throws IOException {
        TimusParser parser = new TimusParser();
        final Problem problem = parser.parseSingleProblem(
            new URL("http://acm.TIMUS.ru/problem.aspx?space=1&num=1202"));

        Assert.assertEquals(Arrays.asList(new ProblemSample("2\n0 0 3 5\n3 1 5 7\n", "8\n")),
                            problem.getSamples());
        Assert.assertEquals("Rectangles Travel", problem.getTitle());
        Assert.assertEquals(1, problem.getTimeLimit());
        Assert.assertEquals(64, problem.getMemoryLimit());
        Assert.assertEquals("1202", problem.getId());
    }

    String getData(final String fileName) throws IOException, URISyntaxException {
        return IOUtils.toString(getClass().getClassLoader().getResource("timus/" + fileName + ".html"));
    }
}
