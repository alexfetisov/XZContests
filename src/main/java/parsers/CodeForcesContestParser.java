package parsers;

import com.google.common.annotations.VisibleForTesting;
import constants.Platform;
import data.Problem;
import data.ProblemBuilder;
import data.ProblemSample;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CodeForcesContestParser implements IContestParser {
    private static final int TIMEOUT = 10000;

    @Override
    public Problem parseSingleProblem(URL url) throws IOException {
        String response = getPageFromUrl(url);
        return getProblem(response);
    }

    @VisibleForTesting
    Problem getProblem(String data) {
        final Document document = Jsoup.parse(data);
        final String titleWithLetter = document.getElementsByClass("title").first().ownText();
        final int id = getProblemId(titleWithLetter);
        final String title;
        if (id != -1) {
            title = titleWithLetter.substring(2).trim();
        } else {
            title = titleWithLetter.trim();
        }
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
            .setMemoryLimit(memoryLimit)
            .setTimeLimit(timeLimit)
            .setPlatform(Platform.CODEFORCES)
            .setTitle(title)
            .setSamples(problemSamples)
            .createProblem();
    }

    @VisibleForTesting
    int getProblemId(final String title) {
        for (char letter = 'A'; letter <= 'Z'; ++letter) {
            if (title.startsWith("" + letter + ". ")) {
                return letter - 'A';
            }
        }
        return -1;
    }

    @VisibleForTesting
    String getPageFromUrl(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }


}
