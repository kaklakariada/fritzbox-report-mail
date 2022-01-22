package com.github.kaklakariada.fritzbox.report.model.regex;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    private final Pattern pattern;
    private final int expectedGroupCount;

    public static Regex create(String regexp, int expectedGroupCount) {
        return new Regex(Pattern.compile(regexp), expectedGroupCount);
    }

    public Regex(Pattern pattern, int expectedGroupCount) {
        this.expectedGroupCount = expectedGroupCount;
        this.pattern = pattern;
    }

    public Optional<MatchedRegex> matches(String input) {
        final Matcher matcher = createMatcher(input);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        if (matcher.groupCount() != expectedGroupCount) {
            throw new IllegalStateException("Expected " + expectedGroupCount + " but got " + matcher.groupCount()
                    + " groups for regexp '" + pattern + "' and input string '" + input + "'");
        }
        return Optional.of(new MatchedRegex(this, input, matcher));
    }

    private Matcher createMatcher(String input) {
        return pattern.matcher(input);
    }

    @Override
    public String toString() {
        return "Regex [pattern='" + pattern + "', expectedGroupCount=" + expectedGroupCount + "]";
    }
}
