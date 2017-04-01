package com.example.apoorvavenkatesh.reminderapp;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by apoorvavenkatesh on 10/31/16.
 */
public class Remind {
    private String reminderName;
    private long reminderId;
    private List<Alarm> alarms = new LinkedList<Alarm>();

    public String getReminderName() { return reminderName; }

    public void setReminderName(String reminderName) { this.reminderName = reminderName; }


    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
        Collections.sort(alarms);
    }

    public long getReminderId() {
        return reminderId;
    }

    public void setReminderId(long reminderID) {
        this.reminderId = reminderID;
    }
}
