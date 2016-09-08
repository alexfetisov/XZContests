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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        final String titleWithLetter = document
            .getElementsByClass("problem_title")
            .first()
            .ownText();
        String id = getProblemId(titleWithLetter);
        final String title = titleWithLetter.substring(5).trim();
        int[] limits = parseProblemLimits(
            document.getElementsByClass("problem_limits")
                    .first()
                    .html()
                    .replace("<br />", "<br/>")
                    .replace("<br/>", "\n"));
        final int timeLimit = limits[0];
        final int memoryLimit = limits[1];

        final Element samples = document.getElementsByClass("sample").first();
        final Elements tableRows = samples.getElementsByTag("tr");

        int numberOfElements = tableRows.size() - 1;
        List<ProblemSample> problemSamples = new ArrayList<>(numberOfElements);
        for (int i = 1; i < tableRows.size(); ++i) {
            Element row = tableRows.get(i);
            Elements ioData = row.getElementsByTag("pre");

            String inputData = ioData.get(0).html()
                .replace("<br />", "<br/>").replace("<br/>", "\n").replace("\r", "");
            String outputData = ioData.get(1).html()
                .replace("<br />", "<br/>").replace("<br/>", "\n").replace("\r", "");
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
            .setContestId(null)
            .setMemoryLimit(memoryLimit)
            .setTimeLimit(timeLimit)
            .setPlatform(Platform.TIMUS)
            .setTitle(title)
            .setSamples(problemSamples)
            .createProblem();
    }

    private int[] parseProblemLimits(String limits) {
        String[] tokens = limits.split("\n");
        int tl = -1, ml = -1;
        for (String token : tokens) {
            int id = token.indexOf(':');
            int number;
            if (id != -1) {
                String toParse = token.split(":")[1].trim().split(" ")[0];
                double cur = Double.parseDouble(toParse);
                number = (int) cur;
            } else {
                continue;
            }

            if (tl == -1) {
                tl = number;
            } else {
                ml = number;
            }
        }
        return new int[] {tl, ml};
    }

    @VisibleForTesting
    String getProblemId(final String title) {
        return title.substring(0, 4);
    }

}
