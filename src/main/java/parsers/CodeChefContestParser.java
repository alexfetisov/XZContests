package parsers;

import data.InputOutput;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CodeChefContestParser implements IContestParser {

    @Override
    public List<InputOutput> parse(String data) {
        final Document document = Jsoup.parse(data);
        final Elements allElements = document.getElementsByTag("pre");
        List<InputOutput> ios = new ArrayList<InputOutput>();
        for (final Element element : allElements) {
            List<Node> nodes = element.childNodes();
            if (nodes.get(0).toString().contains("Input:")) {
                String inputData = nodes.get(1).toString();
                String outputData = nodes.get(3).toString();
                if (inputData.startsWith("\n")) inputData = inputData.substring(1);
                if (outputData.startsWith("\n")) outputData = outputData.substring(1);
                ios.add(new InputOutput(inputData, outputData));
            }
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
