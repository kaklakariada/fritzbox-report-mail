package com.github.kaklakariada.fritzbox.report.convert;

public class EmailBody {
    public String contentType;
    public String body;

    public EmailBody(String contentType, String body) {
        this.contentType = contentType;
        this.body = body;
    }
}
