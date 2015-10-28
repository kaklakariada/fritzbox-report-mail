package org.chris.fritzbox.reports.mail.model;

import java.io.Serializable;

public abstract class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract String getDescription();
}
