package com.company.timesheets.app;

import org.springframework.context.ApplicationEvent;

public class TimeEntryStatusChangedEvent extends ApplicationEvent {

    public TimeEntryStatusChangedEvent(Object source) {
        super(source);
    }
}
