package org.chris.fritzbox.reports.mail.convert;

import java.util.List;
import java.util.function.Function;

import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHtmlTextBodyConverter implements Function<Message, TextBody> {
    final static Logger logger = LoggerFactory.getLogger(MessageHtmlTextBodyConverter.class);

    @Override
    public TextBody apply(final Message msg) {
        final Multipart body = (Multipart) msg.getBody();
        final List<Entity> bodyParts = body.getBodyParts();
        if (bodyParts.size() != 2) {
            throw new AssertionError("Expected 2 but got " + bodyParts.size());
        }
        final Entity part0 = bodyParts.get(0);
        final Entity part1 = bodyParts.get(1);
        logger.trace("part0: {}, part1: {}", part0.getMimeType(), part1.getMimeType());

        switch (part0.getMimeType()) {
        case "multipart/related":
            final Multipart part0body = (Multipart) part0.getBody();
            if (part0body.getBodyParts().size() != 2) {
                throw new AssertionError("Expected 2 parts but got " + part0body.getBodyParts());
            }
            final String mimeType = part0body.getBodyParts().get(0).getMimeType();
            if (!mimeType.equals("text/html")) {
                throw new AssertionError("Expected mime type text/html but got " + mimeType);
            }
            return convert(part0body.getBodyParts().get(0));
        case "text/html":
            return convert(part0);
        case "text/plain":
            switch (part1.getMimeType()) {
            case "text/html":
                return convert(part1);
            case "multipart/related":
                final Multipart part1body = (Multipart) part1.getBody();
                if (part1body.getBodyParts().size() != 2) {
                    throw new AssertionError("Expected 2 parts but got " + part1body.getBodyParts());
                }

                final String mimeType2 = part1body.getBodyParts().get(0).getMimeType();
                if (!mimeType2.equals("text/html")) {
                    throw new AssertionError("Expected mime type text/html but got " + mimeType2);
                }
                return convert(part1body.getBodyParts().get(0));
            default:
                throw new AssertionError("Unexpected mime type of part 1: " + part1.getMimeType());
            }
        default:
            throw new AssertionError("Unexpected mime type of part 0: " + part0.getMimeType());
        }
    }

    private TextBody convert(final Entity entity) {
        if (!entity.getMimeType().equals("text/html")) {
            throw new AssertionError("Expected mime type text/html but got " + entity.getMimeType());
        }
        return (TextBody) entity.getBody();
    }
}
