package com.github.kaklakariada.fritzbox.report.model.regex;

import java.util.List;

@FunctionalInterface
public interface RegexGroupMapper {
    Object map(List<String> groups);
}
