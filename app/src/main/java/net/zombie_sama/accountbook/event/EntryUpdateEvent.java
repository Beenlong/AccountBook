package net.zombie_sama.accountbook.event;

import java.util.Calendar;

public class EntryUpdateEvent extends DataUpdateEvent {
    private Calendar calendar;

    public EntryUpdateEvent(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
