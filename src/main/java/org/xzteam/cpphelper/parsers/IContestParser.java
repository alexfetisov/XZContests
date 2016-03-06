// Author: alexfetisov

package org.xzteam.cpphelper.parsers;

import com.google.common.collect.ImmutableMap;
import org.xzteam.cpphelper.constants.Platform;
import org.xzteam.cpphelper.data.Contest;
import org.xzteam.cpphelper.data.Problem;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public interface IContestParser {
    Problem parseSingleProblem(final URL url) throws IOException;
    Contest parseContest(final URL url) throws IOException;

    static final Map<Platform, IContestParser> PARSERS =
        ImmutableMap.<Platform, IContestParser>builder()
        .put(Platform.CODEFORCES, new CodeForcesContestParser())
        .build();

    static Problem parseProblem(final URL url) throws UnsupportedOperationException, IOException {
        Platform platform = Platform.findPlatform(url);
        if (url == null) {
            throw new IllegalArgumentException("Platform not supported yet");
        }
        return IContestParser.PARSERS.get(platform).parseSingleProblem(url);
    }

    static Problem parseProblem(final Platform platform, final URL url) throws UnsupportedOperationException, IOException {
        return IContestParser.PARSERS.get(platform).parseSingleProblem(url);
    }

    static Contest parse(final URL url) throws UnsupportedOperationException, IOException {
        Platform platform = Platform.findPlatform(url);
        if (url == null) {
            throw new IllegalArgumentException("Platform not supported yet");
        }
        return IContestParser.PARSERS.get(platform).parseContest(url);
    }

    static Contest parse(final Platform platform, final URL url) throws UnsupportedOperationException, IOException {
        return IContestParser.PARSERS.get(platform).parseContest(url);
    }
}
