// Author: alexfetisov

package parsers;

import com.google.common.collect.ImmutableMap;
import constants.Platform;
import data.Problem;
import data.ProblemSample;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface IContestParser {
    Problem parseSingleProblem(final URL url);

    List<ProblemSample> parse(final String data);

    static final Map<Platform, IContestParser> PARSERS =
        ImmutableMap.<Platform, IContestParser>builder()
        .put(Platform.CODEFORCES, new CodeForcesContestParser())
        .build();

    static Problem parse(final Platform platform, final String urlString) throws MalformedURLException {
        return parse(platform, new URL(urlString));
    }

    static Problem parse(final Platform platform, final URL url) throws UnsupportedOperationException {
        return IContestParser.PARSERS.get(platform).parseSingleProblem(url);
    }
}
