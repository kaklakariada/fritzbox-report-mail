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

    /**
     * Get the n-th group, index starts at 0 for the first matched group.
     * 
     * @param group the group index, starting at 0
     * @return the n-th group
     */
    public String group(int group) {
        return matcher.group(group + 1);
    }

    public String getInput() {
        return input;
    }

    public Regex getRegex() {
        return regex;
    }

    @Override
    public String toString() {
        return "MatchedRegex [input=" + input + ", regex=" + regex + "]";
    }
}
