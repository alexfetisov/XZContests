import data.InputOutput;
import org.junit.Assert;
import org.junit.Test;
import parsers.CodeChefContestParser;

import java.util.List;

public class CodeChefContestParserTest {
    @Test
    public void testBasicParserSingleTest() {
        final String input = "<h3>Example</h3>\n<pre><b>Input:</b>2\n3 3\nAbC\nAbc\naBC\n3 2\nabc\nAbC\n<b>Output:</b>\n5\n6\n</pre><p> </p>";
        CodeChefContestParser parser = new CodeChefContestParser();
        List<InputOutput> io = parser.parse(input);
        Assert.assertEquals(1, io.size());
        Assert.assertEquals("2\n3 3\nAbC\nAbc\naBC\n3 2\nabc\nAbC\n", io.get(0).getInput());
        Assert.assertEquals("5\n6\n", io.get(0).getOutput());
    }

    @Test
    public void testBasicParserManyTests() {
        final String input = "<h3>Example 1</h3>\n<pre><b>Input:</b>\n3 4 2\n6 1\n6 2\n<b>Output:</b>\n18\n</pre><h3>Example 2</h3>\n<pre><b>Input:</b>\n2 2 3\n1 1\n1 2\n1 3\n<b>Output:</b>\nImpossible\n</pre><h3>Explanations</h3>";
        CodeChefContestParser parser = new CodeChefContestParser();
        List<InputOutput> io = parser.parse(input);
        Assert.assertEquals(2, io.size());
        Assert.assertEquals("3 4 2\n6 1\n6 2\n", io.get(0).getInput());
        Assert.assertEquals("18\n", io.get(0).getOutput());
        Assert.assertEquals("2 2 3\n1 1\n1 2\n1 3\n", io.get(1).getInput());
        Assert.assertEquals("Impossible\n", io.get(1).getOutput());
    }
}
