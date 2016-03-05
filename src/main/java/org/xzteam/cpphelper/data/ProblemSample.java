package org.xzteam.cpphelper.data;// Author: alexfetisov

public class ProblemSample {
    private final String input;
    private final String output;

    public ProblemSample(final String input, final String output) {
        this.input = input;
        this.output = output;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProblemSample that = (ProblemSample) o;

        if (input != null ? !input.equals(that.input) : that.input != null) {
            return false;
        }
        return output != null ? output.equals(that.output) : that.output == null;

    }

    @Override
    public int hashCode() {
        int result = input != null ? input.hashCode() : 0;
        result = 31 * result + (output != null ? output.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProblemSample{" +
               "input='" + input + '\'' +
               ", output='" + output + '\'' +
               '}';
    }
}
