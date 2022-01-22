package com.github.kaklakariada.fritzbox.report.model.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

public class MatchedRegex {

    private final Regex regex;
    private final Matcher matcher;
    private final String input;

    MatchedRegex(Regex regex, String input, Matcher matcher) {
        this.regex = regex;
        this.input = input;
        this.matcher = matcher;
    }

    MatchResult toMatchResult() {
        return matcher.toMatchResult();
    }

    public List<String> getGroups() {
        final List<String> groups = new ArrayList<>(matcher.groupCount());
        for (int i = 1; i <= matcher.groupCount(); i++) {
            groups.add(matcher.group(i));
        }
        return groups;
    }

    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "MatchedRegex [input=" + input + ", regex=" + regex + "]";
    }
}
