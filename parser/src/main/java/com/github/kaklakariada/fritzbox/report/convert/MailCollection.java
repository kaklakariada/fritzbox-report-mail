package com.github.kaklakariada.fritzbox.report.convert;

import java.util.List;

public class MailCollection {
    private final List<EmailContent> mails;

    public MailCollection(List<EmailContent> mails) {
        this.mails = mails;
    }

    public List<EmailContent> getMails() {
        return mails;
    }
}
