package net.zombie_sama.accountbook.event;

import java.util.Calendar;

public class EntryChangedEvent {
    private Calendar calendar;

    public EntryChangedEvent(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
