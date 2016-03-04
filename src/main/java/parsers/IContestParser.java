// Author: alexfetisov

package parsers;

import data.Problem;
import data.ProblemSample;

import java.net.URL;
import java.util.List;

public interface IContestParser {
    Problem parseSingleProblem(final URL url);

    List<ProblemSample> parse(final String data);
}
