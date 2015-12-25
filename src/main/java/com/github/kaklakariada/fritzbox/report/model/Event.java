package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;

public abstract class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract String getDescription();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}
