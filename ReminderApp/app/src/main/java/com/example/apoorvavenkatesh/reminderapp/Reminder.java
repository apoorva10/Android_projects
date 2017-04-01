package com.example.apoorvavenkatesh.reminderapp;

import android.content.Context;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

/**
 * Created by apoorvavenkatesh on 10/31/16.
 */
public class Reminder {
    private DbHelper db;
    private static List<Long> tempIds; // Ids of the alarms to be deleted or edited
    private static String tempName; // Ids of the alarms to be deleted or edited

    public List<Long> getTempIds() { return Collections.unmodifiableList(tempIds); }

    public void setTempIds(List<Long> tempIds) { this.tempIds = tempIds; }

    public String getTempName() { return tempName; }

    public void setTempName(String tempName) { this.tempName = tempName; }

    public List<Remind> getReminders(Context c) {
        db = new DbHelper(c);
        List<Remind> allPills = db.getAllReminders();
        db.close();
        return allPills;
    }

    public long addReminder(Context c, Remind remind) {
        db = new DbHelper(c);
        long pillId = db.createPill(remind);
        remind.setReminderId(pillId);
        db.close();
        return pillId;
    }

    public Remind getReminderByName(Context c, String reminderName){
        db = new DbHelper(c);
        Remind neededReminder = db.getReminderByName(reminderName);
        db.close();
        return neededReminder;
    }

    public void addAlarm(Context c, Alarm alarm, Remind remind){
        db = new DbHelper(c);
        db.createAlarm(alarm, remind.getReminderId());
        db.close();
    }

    public List<Alarm> getAlarms(Context c, int dayOfWeek) throws URISyntaxException {
        db = new DbHelper(c);
        List<Alarm> daysAlarms= db.getAlarmsByDay(dayOfWeek);
        db.close();
        Collections.sort(daysAlarms);
        return daysAlarms;
    }

    public  List<Alarm> getAlarmByPill(Context c, String reminderName) throws URISyntaxException {
        db = new DbHelper(c);
        List<Alarm> remindersAlarms = db.getAllAlarmsByReminder(reminderName);
        db.close();
        return remindersAlarms;
    }

    public boolean reminderExist(Context c, String reminderName) {
        db = new DbHelper(c);
        for(Remind pill: this.getReminders(c)) {
            if(pill.getReminderName().equals(reminderName))
                return true;
        }
        return false;
    }

    public void deletePill(Context c, String reminderName) throws URISyntaxException {
        db = new DbHelper(c);
        db.deletePill(reminderName);
        db.close();
    }

    public void deleteAlarm(Context c, long alarmId) {
        db = new DbHelper(c);
        db.deleteAlarm(alarmId);
        db.close();
    }

    public void addToHistory(Context c, History h){
        db = new DbHelper(c);
        db.createHistory(h);
        db.close();
    }

    public List<History> getHistory (Context c){
        db = new DbHelper(c);
        List<History> history = db.getHistory();
        db.close();
        return history;
    }

    public Alarm getAlarmById(Context c, long alarm_id) throws URISyntaxException{
        db = new DbHelper(c);
        Alarm alarm = db.getAlarmById(alarm_id);
        db.close();
        return alarm;
    }

    public int getDayOfWeek(Context c, long alarm_id) throws URISyntaxException{
        db = new DbHelper(c);
        int getDayOfWeek = db.getDayOfWeek(alarm_id);
        db.close();
        return getDayOfWeek;
    }
}

