package com.github.kaklakariada.fritzbox.report.convert;

import java.io.IOException;
import java.util.function.Function;

import org.apache.james.mime4j.dom.TextBody;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.html.HtmlElement;

public class HtmlMailParser implements Function<TextBody, HtmlElement> {

    final static Logger logger = LoggerFactory.getLogger(HtmlMailParser.class);

    @Override
    public HtmlElement apply(final TextBody body) {
        try {
            return new HtmlElement(Jsoup.parse(body.getInputStream(), body.getMimeCharset(), "mail"));
        } catch (final IOException e) {
            throw new RuntimeException("Error parsing message " + body, e);
        }
    }
}
