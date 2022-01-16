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
package com.github.kaklakariada.fritzbox.report.convert;

import org.jsoup.Jsoup;

import com.github.kaklakariada.html.HtmlElement;

public class EmailBody {
    public String body;

    public enum Type {
        PNG, CSV, HTML
    }

    public EmailBody(String body) {
        if (body.length() == 0) {
            throw new IllegalStateException("Empty body");
        }
        this.body = body;
    }

    public Type getType() {
        if (getRawContent().contains("<!DOCTYPE html")) {
            return Type.HTML;
        } else if (getRawContent().startsWith("\uFFFD" + "PNG")) {
            return Type.PNG;
        } else if (getRawContent().startsWith("sep=;")) {
            return Type.CSV;
        } else {
            throw new IllegalStateException("Found unknown part: " + getRawContent());
        }
    }

    public String getRawContent() {
        return body;
    }

    public HtmlElement getElement() {
        return new HtmlElement(Jsoup.parse(body, "mail"));
    }

    @Override
    public String toString() {
        return "EmailBody [body=" + body + "]";
    }
}
