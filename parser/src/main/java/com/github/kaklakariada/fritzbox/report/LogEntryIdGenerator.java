package com.github.kaklakariada.fritzbox.report;

public class LogEntryIdGenerator {

    private int nextId = 0;

    public int getNextId() {
        return nextId++;
    }
}
