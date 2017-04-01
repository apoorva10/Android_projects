package com.example.apoorvavenkatesh.reminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlertActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private PendingIntent operation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        /** Creating an Alert Dialog Window */
        AlertAlarm alert = new AlertAlarm();

        /** Opening the Alert Dialog Window. This will be opened when the alarm goes off */
        alert.show(getSupportFragmentManager(), "AlertAlarm");
    }
    public void doNeutralClick(String pillName){
        final int _id = (int) System.currentTimeMillis();
        final long minute = 60000;
        long snoozeLength = 10;
        long currTime = System.currentTimeMillis();
        long min = currTime + minute * snoozeLength;

        Intent intent = new Intent(getBaseContext(), AlertActivity.class);
        intent.putExtra("pill_name", pillName);

        operation = PendingIntent.getActivity(getBaseContext(), _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /** Getting a reference to the System Service ALARM_SERVICE */
        alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, min, operation);
        Toast.makeText(getBaseContext(), "Alarm for " + pillName + " was snoozed for 10 minutes", Toast.LENGTH_SHORT).show();

        finish();

    }

    // I took it
    public void doPositiveClick(String reminderName){
        Reminder reminder = new Reminder();
        Remind remind = reminder.getReminderByName(this, reminderName);
        History history = new History();

        Calendar takeTime = Calendar.getInstance();
        Date date = takeTime.getTime();
        String dateString = new SimpleDateFormat("MMM d, yyyy").format(date);

        int hour = takeTime.get(Calendar.HOUR_OF_DAY);
        int minute = takeTime.get(Calendar.MINUTE);
        String am_pm = (hour < 12) ? "am" : "pm";

        history.setHourTaken(hour);
        history.setMinuteTaken(minute);
        history.setDateString(dateString);
        history.setPillName(reminderName);

        reminder.addToHistory(this, history);

        String stringMinute;
        if (minute < 10)
            stringMinute = "0" + minute;
        else
            stringMinute = "" + minute;

        int nonMilitaryHour = hour % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;

        //Toast.makeText(getBaseContext(),  pillName + " was taken at "+ nonMilitaryHour + ":" + stringMinute + " " + am_pm + ".", Toast.LENGTH_SHORT).show();

        Intent returnHistory = new Intent(getBaseContext(), MainActivity.class);
        startActivity(returnHistory);
        finish();
    }

    // I won't take it
    public void doNegativeClick(){
        finish();
    }
}
