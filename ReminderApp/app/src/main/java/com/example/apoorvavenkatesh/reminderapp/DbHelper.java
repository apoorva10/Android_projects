package com.example.apoorvavenkatesh.reminderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by apoorvavenkatesh on 10/31/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pill_model_database";

    /** Database version */
    private static final int DATABASE_VERSION = 3;

    /** Table names */
    private static final String Reminder_TABLE          = "reminders";
    private static final String ALARM_TABLE         = "alarms";
    private static final String Reminder_ALARM_LINKS    = "reminder_alarm";
    private static final String HISTORIES_TABLE     = "histories";

    /** Common column name and location */
    public static final String KEY_ROWID            = "id";

    /** Pill table columns, used by History Table */
    private static final String KEY_Remindername        = "reminderName";

    /** Alarm table columns, Hour & Minute used by History Table */
    private static final String KEY_INTENT           = "intent";
    private static final String KEY_HOUR             = "hour";
    private static final String KEY_MINUTE           = "minute";
    private static final String KEY_DAY_WEEK         = "day_of_week";
    private static final String KEY_ALARMS_Reminder_NAME = "reminderName";

    /** Pill-Alarm link table columns */
    private static final String KEY_ReminderTABLE_ID    = "reminder_id";
    private static final String KEY_ALARMTABLE_ID   = "alarm_id";

    /** History Table columns, some used above */
    private static final String KEY_DATE_STRING     = "date";

    /** Pill Table: create statement */
    private static final String CREATE_PILL_TABLE =
            "create table " + Reminder_TABLE + "("
                    + KEY_ROWID + " integer primary key not null,"
                    + KEY_Remindername + " text not null" + ")";

    /** Alarm Table: create statement */
    private static final String CREATE_ALARM_TABLE =
            "create table "         + ALARM_TABLE + "("
                    + KEY_ROWID     + " integer primary key,"
                    + KEY_INTENT    + " text,"
                    + KEY_HOUR      + " integer,"
                    + KEY_MINUTE    + " integer,"
                    + KEY_ALARMS_Reminder_NAME  + " text not null,"
                    + KEY_DAY_WEEK  + " integer" + ")";

    /** Pill-Alarm link table: create statement */
    private static final String CREATE_PILL_ALARM_LINKS_TABLE =
            "create table "             + Reminder_ALARM_LINKS + "("
                    + KEY_ROWID         + " integer primary key not null,"
                    + KEY_ReminderTABLE_ID  + " integer not null,"
                    + KEY_ALARMTABLE_ID + " integer not null" + ")";

    /** Histories Table: create statement */
    private static final String CREATE_HISTORIES_TABLE =
            "CREATE TABLE "             + HISTORIES_TABLE + "("
                    + KEY_ROWID         + " integer primary key, "
                    + KEY_Remindername      + " text not null, "
                    + KEY_DATE_STRING   + " text, "
                    + KEY_HOUR          + " integer, "
                    + KEY_MINUTE        + " integer " + ")";

    /** Constructor */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    /** Creating tables */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PILL_TABLE);
        db.execSQL(CREATE_ALARM_TABLE);
        db.execSQL(CREATE_PILL_ALARM_LINKS_TABLE);
        db.execSQL(CREATE_HISTORIES_TABLE);
    }

    @Override
    // TODO: change this so that updating doesn't delete old data
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Reminder_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Reminder_ALARM_LINKS);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORIES_TABLE);
        onCreate(db);
    }


    public long createPill(Remind remind) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_Remindername, remind.getReminderName());

        long pill_id = db.insert(Reminder_TABLE, null, values);

        return pill_id;
    }


    public long[] createAlarm(Alarm alarm, long pill_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long[] alarm_ids = new long[7];

        /** Create a separate row in the table for every day of the week for this alarm */
        int arrayPos = 0;
        for (boolean day : alarm.getDayOfWeek()) {
            if (day) {
                ContentValues values = new ContentValues();
                values.put(KEY_HOUR, alarm.getHour());
                values.put(KEY_MINUTE, alarm.getMinute());
                values.put(KEY_DAY_WEEK, arrayPos + 1);
                values.put(KEY_ALARMS_Reminder_NAME, alarm.getPillName());

                /** Insert row */
                long alarm_id = db.insert(ALARM_TABLE, null, values);
                alarm_ids[arrayPos] = alarm_id;

                /** Link alarm to a pill */
                createReminderAlarmLink(pill_id, alarm_id);
            }
            arrayPos++;
        }
        return alarm_ids;
    }


    private long createReminderAlarmLink(long pill_id, long alarm_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ReminderTABLE_ID, pill_id);
        values.put(KEY_ALARMTABLE_ID, alarm_id);

        /** Insert row */
        long pillAlarmLink_id = db.insert(Reminder_ALARM_LINKS, null, values);

        return pillAlarmLink_id;
    }


    public void createHistory(History history) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_Remindername, history.getPillName());
        values.put(KEY_DATE_STRING, history.getDateString());
        values.put(KEY_HOUR, history.getHourTaken());
        values.put(KEY_MINUTE, history.getMinuteTaken());

        /** Insert row */
        db.insert(HISTORIES_TABLE, null, values);
    }


    public Remind getReminderByName(String pillName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String dbPill = "select * from "
                + Reminder_TABLE        + " where "
                + KEY_Remindername      + " = "
                + "'"   + pillName  + "'";

        Cursor c = db.rawQuery(dbPill, null);

        Remind remind = new Remind();

        if (c.moveToFirst() && c.getCount() >= 1) {
            remind.setReminderName(c.getString(c.getColumnIndex(KEY_Remindername)));
            remind.setReminderId(c.getLong(c.getColumnIndex(KEY_ROWID)));
            c.close();
        }
        return remind;
    }


    public List<Remind> getAllReminders() {
        List<Remind> pills = new ArrayList<>();
        String dbPills = "SELECT * FROM " + Reminder_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbPills, null);

        /** Loops through all rows, adds to list */
        if (c.moveToFirst()) {
            do {
                Remind p = new Remind();
                p.setReminderName(c.getString(c.getColumnIndex(KEY_Remindername)));
                p.setReminderId(c.getLong(c.getColumnIndex(KEY_ROWID)));

                pills.add(p);
            } while (c.moveToNext());
        }
        c.close();
        return pills;
    }


    public List<Alarm> getAllAlarmsByReminder(String pillName) throws URISyntaxException {
        List<Alarm> alarmsByPill = new ArrayList<Alarm>();

        /** HINT: When reading string: '.' are not periods ex) pill.rowIdNumber */
        String selectQuery = "SELECT * FROM "       +
                ALARM_TABLE         + " alarm, "    +
                Reminder_TABLE          + " pill, "     +
                Reminder_ALARM_LINKS    + " pillAlarm WHERE "           +
                "pill."             + KEY_Remindername     + " = '"    + pillName + "'" +
                " AND pill."        + KEY_ROWID         + " = "     +
                "pillAlarm."        + KEY_ReminderTABLE_ID  +
                " AND alarm."       + KEY_ROWID         + " = "     +
                "pillAlarm."        + KEY_ALARMTABLE_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Alarm al = new Alarm();
                al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
                al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
                al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
                al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_Reminder_NAME)));

                alarmsByPill.add(al);
            } while (c.moveToNext());
        }

        c.close();


        return combineAlarms(alarmsByPill);
    }

    public List<Alarm> getAlarmsByDay(int day) {
        List<Alarm> daysAlarms = new ArrayList<Alarm>();

        String selectQuery = "SELECT * FROM "       +
                ALARM_TABLE     + " alarm WHERE "   +
                "alarm."        + KEY_DAY_WEEK      +
                " = '"          + day               + "'";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Alarm al = new Alarm();
                al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
                al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
                al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
                al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_Reminder_NAME)));

                daysAlarms.add(al);
            } while (c.moveToNext());
        }
        c.close();

        return daysAlarms;
    }



    public Alarm getAlarmById(long alarm_id) throws URISyntaxException {

        String dbAlarm = "SELECT * FROM "   +
                ALARM_TABLE + " WHERE "     +
                KEY_ROWID   + " = "         + alarm_id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        Alarm al = new Alarm();
        al.setId(c.getInt(c.getColumnIndex(KEY_ROWID)));
        al.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
        al.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
        al.setPillName(c.getString(c.getColumnIndex(KEY_ALARMS_Reminder_NAME)));

        c.close();

        return al;
    }

    private List<Alarm> combineAlarms(List<Alarm> dbAlarms) throws URISyntaxException {
        List<String> timesOfDay = new ArrayList<>();
        List<Alarm> combinedAlarms = new ArrayList<>();

        for (Alarm al : dbAlarms) {
            if (timesOfDay.contains(al.getStringTime())) {
                /** Add this db row to alarm object */
                for (Alarm ala : combinedAlarms) {
                    if (ala.getStringTime().equals(al.getStringTime())) {
                        int day = getDayOfWeek(al.getId());
                        boolean[] days = ala.getDayOfWeek();
                        days[day-1] = true;
                        ala.setDayOfWeek(days);
                        ala.addId(al.getId());
                    }
                }
            } else {
                /** Create new Alarm object with day of week array */
                Alarm newAlarm = new Alarm();
                boolean[] days = new boolean[7];

                newAlarm.setPillName(al.getPillName());
                newAlarm.setMinute(al.getMinute());
                newAlarm.setHour(al.getHour());
                newAlarm.addId(al.getId());

                int day = getDayOfWeek(al.getId());
                days[day-1] = true;
                newAlarm.setDayOfWeek(days);

                timesOfDay.add(al.getStringTime());
                combinedAlarms.add(newAlarm);
            }
        }

        Collections.sort(combinedAlarms);
        return combinedAlarms;
    }


    public int getDayOfWeek(long alarm_id) throws URISyntaxException {
        SQLiteDatabase db = this.getReadableDatabase();

        String dbAlarm = "SELECT * FROM "   +
                ALARM_TABLE + " WHERE "     +
                KEY_ROWID   + " = "         + alarm_id;

        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        int dayOfWeek = c.getInt(c.getColumnIndex(KEY_DAY_WEEK));
        c.close();

        return dayOfWeek;
    }


    public List<History> getHistory() {
        List<History> allHistory = new ArrayList<>();
        String dbHist = "SELECT * FROM " + HISTORIES_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbHist, null);

        if (c.moveToFirst()) {
            do {
                History h = new History();
                h.setPillName(c.getString(c.getColumnIndex(KEY_Remindername)));
                h.setDateString(c.getString(c.getColumnIndex(KEY_DATE_STRING)));
                h.setHourTaken(c.getInt(c.getColumnIndex(KEY_HOUR)));
                h.setMinuteTaken(c.getInt(c.getColumnIndex(KEY_MINUTE)));

                allHistory.add(h);
            } while (c.moveToNext());
        }
        c.close();
        return allHistory;
    }




    private void deleteReminderAlarmLinks(long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Reminder_ALARM_LINKS, KEY_ALARMTABLE_ID
                + " = ?", new String[]{String.valueOf(alarmId)});
    }

    public void deleteAlarm(long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();


        deleteReminderAlarmLinks(alarmId);

        /* Then delete alarm */
        db.delete(ALARM_TABLE, KEY_ROWID
                + " = ?", new String[]{String.valueOf(alarmId)});
    }

    public void deletePill(String pillName) throws URISyntaxException {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Alarm> pillsAlarms;


        pillsAlarms = getAllAlarmsByReminder(pillName);
        for (Alarm alarm : pillsAlarms) {
            long id = alarm.getId();
            deleteAlarm(id);
        }


        db.delete(Reminder_TABLE, KEY_Remindername
                + " = ?", new String[]{pillName});
    }

}
