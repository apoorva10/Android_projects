package com.example.arun.mcproject_pa;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SmartAlertsBrowserService extends Service {
    AlarmManagerBroadcastReceiver alarmManagerBroadcastReceiver = new AlarmManagerBroadcastReceiver();
    List<String> title = new ArrayList<String>();
    List<String> url = new ArrayList<String>();
    List<String> date = new ArrayList<String>();
    double probabilityScore = 0.0;


    //String regex = "((https:[/][/]|http:[/][/]|www.|search.)([a-z]|[A-Z]|[0-9]|[/.]|[~])*)";
    //String regex = "((https:[/][/]|http:[/][/]|www.|search.)([a-z]|[A-Z]|[0-9])*([/.])([a-z]|[A-Z]|[0-9])*)";
    //String regex = "((https:[/][/]|http:[/][/]|www.|search.)([a-z]|[A-Z]|[0-9])*([.])*([a-z]|[A-Z]|[0-9])*(([/])([a-z]|[A-Z]|[0-9])*){2})";
    String regex = "(((https:[/][/]|http:[/][/]|www.|search.)([a-z]|[A-Z]|[0-9])*([.])*([a-z]|[A-Z]|[0-9])*)(((([/])([a-z]|[A-Z]|[0-9])*){2})|(([/])([a-z]|[A-Z]|[0-9])*)))";

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

        String strtitle = "";
        String strurl = "";
        String strdate = "";



        Cursor mCur = getContentResolver().query(BOOKMARKS_URI, HISTORY_PROJECTION, null, null,null);
        mCur .moveToFirst();



        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            while (mCur.isAfterLast() == false ) {


                strtitle = mCur.getString(mCur.getColumnIndex("title"));
                strurl = mCur.getString(mCur.getColumnIndex("url"));
                strdate = mCur.getString(mCur.getColumnIndex("date"));


                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(strurl);

                if (m.find()) {
                    url.add(m.group(1));
                }

                title.add(strtitle);
                date.add(strdate);
                mCur .moveToNext();
            }
        }
        mCur.close();
        dataManager(title,url,date);
    }

    public void dataManager(List<String> title,List<String> url,List<String> date){
        Map<String, Integer> urlCount = new HashMap<String, Integer>();
        Map<Double, String> probScores = new HashMap<Double, String>();
        //MultiMap<Double, String> probScores = new MultiValueMap<Double, String>();
        //Map<Double, String> probScores = new HashMap<Double, String>();
        ArrayList<String> displayURL = new ArrayList<String>();
        int countTop = 1;

        if(!probScores.isEmpty()){
            probScores.clear();
        }
        for (String temp : url) {
            Integer count = urlCount.get(temp);
            urlCount.put(temp, (count == null) ? 1 : count + 1);
        }
        double sum = 0.0;
        for (double f : urlCount.values()) {
            sum += f;
        }

        Iterator it = urlCount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            it.remove();
            probabilityScore = (Integer)pair.getValue() / sum;
            probScores.put(probabilityScore, pair.getKey().toString());
        }
        //System.out.println(probScores);
        Map<Double, String> newMap = new TreeMap(Collections.reverseOrder());
        newMap.putAll(probScores);


        Iterator iterator = newMap.entrySet().iterator();
        while (iterator.hasNext() && countTop <= 5) {
            Map.Entry pair = (Map.Entry) iterator.next();
            displayURL.add(pair.getValue().toString());
            iterator.remove();
            countTop++;
        }

        publishResults(displayURL);
        urlCount.clear();
        url.clear();
    }

    private void publishResults(ArrayList<String> displayURL) {
        Intent intent = new Intent("URLs");
        intent.putStringArrayListExtra("URL", displayURL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
