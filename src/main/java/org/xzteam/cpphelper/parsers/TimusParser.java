package org.xzteam.cpphelper.parsers;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xzteam.cpphelper.constants.Platform;
import org.xzteam.cpphelper.data.Contest;
import org.xzteam.cpphelper.data.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimusParser implements IContestParser {
    private static final int TIMEOUT = 10000;

    @Override
    public Problem parseSingleProblem(URL url) throws IOException {
        return getProblem(IOUtils.toString(url));
    }

    @Override
    public Contest parseContest(URL url) throws IOException {
        throw new NotImplementedException();
    }

    @VisibleForTesting
    Problem getProblem(final String data) {
        final Document document = Jsoup.parse(data);
        final String titleWithLetter = document.getElementsByClass("title").first().ownText();
        String id = getProblemId(titleWithLetter);
        final String title;
        if (id != null) {
            title = titleWithLetter.substring(2).trim();
        } else {
            title = titleWithLetter.trim();
            id = title.toLowerCase().replaceAll("[^a-z0-9]+", "_");
        }
        final Element contestLink = document.select("ul.second-level-menu-list li a").first();
        final String contestId = contestLink == null ? null : getContestId(contestLink.attr("href"));
        final int timeLimit = Integer.parseInt(
            document.getElementsByClass("time-limit").first().ownText().split(" ")[0]);
        final int memoryLimit = Integer.parseInt(
            document.getElementsByClass("memory-limit").first().ownText().split(" ")[0]);
        final Elements inputElements = document.getElementsByClass("input");
        final Elements outputElements = document.getElementsByClass("output");
        final int numberOfElements = inputElements.size();
        List<ProblemSample> problemSamples = new ArrayList<>(numberOfElements);
        for (int i = 0; i < numberOfElements; ++i) {
            final Element input = inputElements.get(i);
            final Element output = outputElements.get(i);
            String inputData = input.getElementsByTag("pre").get(0).html()
                .replace("<br />", "<br/>").replace("<br/>", "\n");
            String outputData = output.getElementsByTag("pre").get(0).html()
                .replace("<br />", "<br/>").replace("<br/>", "\n");
            if (!inputData.endsWith("\n")) {
                inputData += "\n";
            }
            if (!outputData.endsWith("\n")) {
                outputData += "\n";
            }
            problemSamples.add(new ProblemSample(inputData, outputData));
        }

        return new ProblemBuilder()
            .setId(id)
            .setContestId(contestId)
            .setMemoryLimit(memoryLimit)
            .setTimeLimit(timeLimit)
            .setPlatform(Platform.CODEFORCES)
            .setTitle(title)
            .setSamples(problemSamples)
            .createProblem();
    }

    @VisibleForTesting
    String getContestId(final String link) {
        final Pattern pContest = Pattern.compile("/contest/(\\d+)");
        final Pattern pGym = Pattern.compile("/gym/(\\d+)");
        Matcher m = pContest.matcher(link);
        if (m.matches()) {
            return m.group(1);
        }
        m = pGym.matcher(link);
        if (m.matches()) {
            return "gym" + m.group(1);
        }
        return null;
    }

    @VisibleForTesting
    String getProblemId(final String title) {
        if (title.length() > 1 && title.charAt(1) == '.' && Character.isUpperCase(title.charAt(0))) {
            return title.substring(0, 1);
        }
        return null;
    }

}
