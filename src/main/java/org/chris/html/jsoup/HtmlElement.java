package org.chris.html.jsoup;

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
    final static Logger logger = LoggerFactory.getLogger(HtmlElement.class);
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

    public <T> List<T> map(final String cssSelector, final Function<HtmlElement, T> mapper) {
        final Elements elements = element.select(cssSelector);
        logger.trace("Found {} elements matching '{}' in {}", elements.size(), cssSelector, element);
        final List<T> result = new ArrayList<>(elements.size());
        for (final Element row : elements) {
            final T mappedRow;
            try {
                mappedRow = mapper.apply(new HtmlElement(row));
            } catch (final Exception e) {
                throw new RuntimeException("Error mapping element " + row, e);
            }
            if (mappedRow != null) {
                logger.trace("Got object {} for row {}", mappedRow, row);
                result.add(mappedRow);
            } else {
                logger.trace("Got null for row {}: ignore", row);
            }
        }
        return result;
    }

    public HtmlElement selectSingleElement(final String selector) {
        final List<HtmlElement> elements = select(selector);
        if (elements.size() != 1) {
            throw new IllegalStateException("Selector '" + selector + "' found " + elements.size()
                    + " elements, expected 1 at " + this.element + " but found " + elements);
        }
        return elements.get(0);
    }

    public String getRegexpResult(final String selector, final String regexp) {
        final HtmlElement element = selectSingleElement(selector);
        final String text = element.text();
        final Matcher matcher = Pattern.compile(regexp).matcher(text);
        if (!matcher.matches()) {
            throw new IllegalStateException("Regexp '" + regexp + "' does not match string '" + text + "'");
        }
        final String result = matcher.group(1);
        return result;
    }

    public String text() {
        return element.text();
    }

    public int number() {
        return Integer.parseInt(text());
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