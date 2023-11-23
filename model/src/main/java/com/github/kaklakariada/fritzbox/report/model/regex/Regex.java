package com.github.kaklakariada.fritzbox.report.model.regex;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    private final Pattern pattern;
    private final int expectedGroupCount;
    private final RegexMapper mapper;

    public static Regex create(final String regexp, final int expectedGroupCount) {
        return create(regexp, expectedGroupCount, null);
    }

    public static Regex create(final String regexp, final int expectedGroupCount, final RegexGroupMapper mapper) {
        return createWithMapper(regexp, expectedGroupCount,
                mapper == null ? null : (final MatchedRegex regex) -> mapper.map(regex.getGroups()));
    }

    private static Regex createWithMapper(final String regexp, final int expectedGroupCount, final RegexMapper mapper) {
        return new Regex(Pattern.compile(regexp), expectedGroupCount, mapper);
    }

    public Regex(final Pattern pattern, final int expectedGroupCount, final RegexMapper mapper) {
        this.expectedGroupCount = expectedGroupCount;
        this.pattern = pattern;
        this.mapper = mapper;
    }

    public Optional<MatchedRegex> matches(final String input) {
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

    private Matcher createMatcher(final String input) {
        return pattern.matcher(input);
    }

    public Optional<RegexMapper> getMapper() {
        return Optional.ofNullable(mapper);
    }

    @Override
    public String toString() {
        return "Regex [pattern='" + pattern + "', expectedGroupCount=" + expectedGroupCount + "]";
    }
}
