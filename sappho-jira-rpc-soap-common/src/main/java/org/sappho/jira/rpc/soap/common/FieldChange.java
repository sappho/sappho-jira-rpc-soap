package org.sappho.jira.rpc.soap.common;

import java.util.Date;

public class FieldChange {

    private final Date date;
    private final String from;
    private final String to;

    public FieldChange(Date date, String from, String to) {

        this.date = date;
        this.from = from;
        this.to = to;
    }

    public Date getDate() {

        return date;
    }

    public String getFrom() {

        return from;
    }

    public String getTo() {

        return to;
    }
}
