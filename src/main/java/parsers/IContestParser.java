// Author: alexfetisov

package parsers;

import data.InputOutput;

import java.net.URL;
import java.util.List;

public interface IContestParser {
    List<InputOutput> parse(final String data);
}
