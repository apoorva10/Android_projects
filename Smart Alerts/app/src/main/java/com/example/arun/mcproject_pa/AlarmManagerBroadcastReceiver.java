package com.example.arun.mcproject_pa;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import java.util.Calendar;

/**
 * Created by rahulrao on 10/27/16.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        wl.acquire();
        Intent intent1 = new Intent(context.getApplicationContext(),SmartAlertsService.class);
        context.startService(intent1);
        wl.release();
    }


    public void setAlarm(Context context)
    {
        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar Calendar_Object = Calendar.getInstance();
        long oneDay =  24 * 60 * 60 * 1000;
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + oneDay, oneDay, pi); // Millisec * Second * Minute
    }
}
