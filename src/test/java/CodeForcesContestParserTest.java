// Author: alexfetisov

import data.ProblemSample;
import org.junit.Assert;
import org.junit.Test;
import parsers.CodeForcesContestParser;

import java.util.List;

public class CodeForcesContestParserTest {

    @Test
    public void testBasicParser() {
        final String input = "<div class=\"sample-test\"><div class=\"input\"><div class=\"title\">Входные данные</div><pre>3<br />2 1<br />1 0<br />0 1<br /></pre></div><div class=\"output\"><div class=\"title\">Выходные данные</div><pre>19<br /></pre></div><div class=\"input\"><div class=\"title\">Входные данные</div><pre>5<br />0 0<br />0 1<br />0 2<br />0 3<br />0 4<br /></pre></div><div class=\"output\"><div class=\"title\">Выходные данные</div><pre>2930<br /></pre></div></div>";
        CodeForcesContestParser parser = new CodeForcesContestParser();
        List<ProblemSample> io = parser.parse(input);
        Assert.assertEquals(2, io.size());
        Assert.assertEquals("3\n2 1\n1 0\n0 1\n", io.get(0).getInput());
        Assert.assertEquals("19\n", io.get(0).getOutput());
        Assert.assertEquals("5\n0 0\n0 1\n0 2\n0 3\n0 4\n", io.get(1).getInput());
        Assert.assertEquals("2930\n", io.get(1).getOutput());
    }
}
