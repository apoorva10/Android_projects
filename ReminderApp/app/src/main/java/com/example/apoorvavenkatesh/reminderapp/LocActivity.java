package com.example.apoorvavenkatesh.reminderapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.location.Location;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by apoorvavenkatesh on 11/19/16.
 */
public class LocActivity extends AppCompatActivity {
    private DbHelper db = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //  LocationActivity loc=new LocationActivity(LocActivity.this);
        // loc.getLocation();
        //    double lat=loc.getLatitude();
        //  double lon=loc.getLongitude();
        //Toast.makeText(this,lat+""+lon,Toast.LENGTH_LONG).show();
        List<Alarm> alarms = db.getAlarmsByDay(2);
        String str[]={"laundry","cleaning","cooking"};
        String val = "";
        String to_be = "";
        for (Alarm a : alarms) {
            val = a.getPillName();
            if (val.equals(str[0])||val.equals(str[1])||val.equals(str[2])) {
                to_be = val;
            }

        }
        final String assign = to_be;
        // Toast.makeText(this,assign,Toast.LENGTH_LONG).show();
        if (assign != "") {
            AlertDialog.Builder builder = new AlertDialog.Builder(LocActivity.this);
            builder.setTitle("Reminder");

            builder.setMessage("Do you want to do your  " + assign + " right now instead of later?");
            builder.setCancelable(false);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Reminder reminder = new Reminder();
                    Remind remind = reminder.getReminderByName(LocActivity.this, assign);
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
                    history.setPillName(assign);

                    reminder.addToHistory(LocActivity.this, history);

                    String stringMinute;
                    if (minute < 10)
                        stringMinute = "0" + minute;
                    else
                        stringMinute = "" + minute;

                    int nonMilitaryHour = hour % 12;
                    if (nonMilitaryHour == 0)
                        nonMilitaryHour = 12;

                    //Toast.makeText(getBaseContext(),  pillName + " was taken at "+ nonMilitaryHour + ":" + stringMinute + " " + am_pm + ".", Toast.LENGTH_SHORT).show();
                    try {
                        db.deletePill(assign);
                        //Toast.makeText(LocActivity.this,"Alarm removed",Toast.LENGTH_LONG).show();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    Intent returnHistory = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(returnHistory);
                    finish();
                    dialog.cancel();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.cancel();
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();

            // show it
            alertDialog.show();


        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(LocActivity.this);
            builder1.setTitle("Reminder");
            builder1.setMessage("No predictive alarms for right now");
            builder1.setCancelable(false);
            builder1.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.cancel();
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder1.create();

            // show it
            alertDialog.show();


        }
    }
}