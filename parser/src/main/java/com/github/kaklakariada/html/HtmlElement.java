/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2018 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kaklakariada.html;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlElement {
    private static final Logger LOG = LoggerFactory.getLogger(HtmlElement.class);
    private final Element element;

    public HtmlElement(final Element element) {
        this.element = element;
    }

    public HtmlElement(final String htmlContent) {
        this(Jsoup.parse(htmlContent));
    }

    public HtmlElement getNthAncestor(final int n) {
        if (n == 0) {
            return this;
        }
        return new HtmlElement(element.parent()).getNthAncestor(n - 1);
    }

    public String getText() {
        return element.text();
    }

    public <T> List<T> map(final String cssSelector, final Function<HtmlElement, T> mapper) {
        final Elements elements = element.select(cssSelector);
        LOG.trace("Found {} elements matching '{}' in {}", elements.size(), cssSelector, element);
        final List<T> result = new ArrayList<>(elements.size());
        for (final Element row : elements) {
            final T mappedRow;
            try {
                mappedRow = mapper.apply(new HtmlElement(row));
            } catch (final Exception e) {
                throw new IllegalStateException("Error mapping element " + row, e);
            }
            if (mappedRow != null) {
                LOG.trace("Got object {} for row {}", mappedRow, row);
                result.add(mappedRow);
            } else {
                LOG.trace("Got null for row {}: ignore", row);
            }
        }
        return result;
    }

    public HtmlElement selectSingleElement(final String selector) {
        final HtmlElement singleElement = selectOptionalSingleElement(selector);
        if (singleElement == null) {
            throw new IllegalStateException(
                    "Selector '" + selector + "' found no elements, expected 1 at " + this.element);
        }
        return singleElement;
    }

    public HtmlElement selectOptionalSingleElement(final String selector) {
        final List<HtmlElement> elements = select(selector);
        if (elements.isEmpty()) {
            return null;
        }
        if (elements.size() != 1) {
            throw new IllegalStateException("Selector '" + selector + "' found " + elements.size()
                    + " elements, expected 1 at " + this.element + " but found " + elements);
        }
        return elements.get(0);
    }

    public String getRegexpResult(final String selector, final String regexp) {
        final HtmlElement selected = selectSingleElement(selector);
        return extractRegexp(regexp, selected);
    }

    public String getOptionalRegexpResult(final String selector, final String regexp) {
        final HtmlElement selected = selectOptionalSingleElement(selector);
        if (selected == null) {
            return null;
        }
        return extractRegexp(regexp, selected);
    }

    private String extractRegexp(final String regexp, final HtmlElement element) {
        final String text = element.text();
        final Matcher matcher = Pattern.compile(regexp).matcher(text);
        if (!matcher.matches()) {
            throw new IllegalStateException("Regexp '" + regexp + "' does not match string '" + text + "'");
        }
        return matcher.group(1);
    }

    public String text() {
        return element.text();
    }

    public int number() {
        return Integer.parseInt(text());
    }

    public HtmlElement selectElementWithContent(final String element, final String content) {
        final String selector = element + ":containsOwn(" + content + ")";
        final List<HtmlElement> candidates = select(selector) //
                .stream().filter(e -> e.text().equals(content)) //
                .toList();
        if (candidates.size() != 1) {
            throw new IllegalStateException("Expected 1 element '" + element + "' with content '" + content
                    + "' but found " + candidates.size() + " for selector '" + selector + "': " + candidates + " in "
                    + this);
        }
        return candidates.get(0);
    }

    public List<HtmlElement> select(final String select) {
        return map(select, Function.identity());
    }

    @Override
    public String toString() {
        return element.toString();
    }

    public String getCssClass() {
        return element.attr("class");
    }

    public String getName() {
        return element.tagName();
    }
}
