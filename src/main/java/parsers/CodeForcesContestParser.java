package parsers;

import data.InputOutput;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CodeForcesContestParser implements IContestParser {
    private static final int TIMEOUT = 10000;

    @Override
    public List<InputOutput> parse(String data) {
        final Document document = Jsoup.parse(data);
        final Elements inputElements = document.getElementsByClass("input");
        final Elements outputElements = document.getElementsByClass("output");
        final int numberOfElements = inputElements.size();
        List<InputOutput> ios = new ArrayList<InputOutput>(numberOfElements);
        for (int i = 0; i < numberOfElements; ++i) {
            final Element input = inputElements.get(i);
            final Element output = outputElements.get(i);
            String inputData = input.getElementsByTag("pre").get(0).html().replace("<br />", "<br/>").replace("<br/>", "\n");
            String outputData = output.getElementsByTag("pre").get(0).html().replace("<br />", "<br/>").replace("<br/>", "\n");
            if (!inputData.endsWith("\n")) {
                inputData += "\n";
            }
            if (!outputData.endsWith("\n")) {
                outputData += "\n";
            }
            ios.add(new InputOutput(inputData, outputData));
        }
        return ios;
    }

    public List<InputOutput> parse(final URL url) {
        try {
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return parse(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<InputOutput>();
    }
}
