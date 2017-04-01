package com.example.arun.mcproject_pa;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SmartAlertsBrowserService extends Service {
    AlarmManagerBroadcastReceiver alarmManagerBroadcastReceiver = new AlarmManagerBroadcastReceiver();
    List<String> title = new ArrayList<String>();
    List<String> url = new ArrayList<String>();
    List<String> date = new ArrayList<String>();

    public SmartAlertsBrowserService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmManagerBroadcastReceiver.setAlarm(this);
        Toast.makeText(this, "Service Started Browser Service", Toast.LENGTH_LONG).show();
        getBrowserHistory();
        return START_STICKY;
    }

    private void getBrowserHistory() {
        final Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");
        final String[] HISTORY_PROJECTION = new String[]
                {
                        "_id", // 0
                        "url", // 1
                        "visits", // 2
                        "date", // 3
                        "bookmark", // 4
                        "title", // 5
                        "favicon", // 6
                        "thumbnail", // 7
                        "touch_icon", // 8
                        "user_entered", // 9
                };


        Cursor mCur = getContentResolver().query(BOOKMARKS_URI, HISTORY_PROJECTION, null,   null,null);
        mCur .moveToFirst();

        String title = "";
        String url = "";
        String date="";

        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            while (mCur.isAfterLast() == false ) {

                title= mCur.getString(mCur .getColumnIndex("title"));
                url = mCur.getString(mCur .getColumnIndex("url"));
                date= mCur.getString(mCur .getColumnIndex("date"));
                mCur .moveToNext();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
