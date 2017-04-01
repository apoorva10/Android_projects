package com.example.arun.mcproject_pa;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SmartAlertsService extends Service {

    AlarmManagerBroadcastReceiver alarmManagerBroadcastReceiver = new AlarmManagerBroadcastReceiver();
    double probabilityScore = 0.0;
    public SmartAlertsService() {
    }

    List<String> phoneNumbers = new ArrayList<String>();
    List<Date> callDatebuf = new ArrayList<Date>();
    List<String> callDuration = new ArrayList<String>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmManagerBroadcastReceiver.setAlarm(this);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        getCallDetails();
        return START_STICKY;
    }

    private void getCallDetails() {
        try {
            StringBuffer sb = new StringBuffer();
            Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            sb.append("Call Details :");
            while (managedCursor.moveToNext()) {
                String phoneNos = managedCursor.getString(number);
                if (phoneNos.length() == 11) {
                    phoneNos = phoneNos.substring(1);
                }
                if (phoneNos.length() == 12) {
                    phoneNos = phoneNos.substring(2);
                }
                phoneNumbers.add(phoneNos);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                callDatebuf.add(new Date(Long.valueOf(callDate)));
                callDuration.add(managedCursor.getString(duration));
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
            }
            managedCursor.close();
            dataManager(phoneNumbers,callDuration,callDatebuf);

        } catch (SecurityException securityException) {
            securityException.getMessage();
        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    public String contactExists(Context context, String number) {
        String contact = null;
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        int contactName = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);

        try {
            if (cur.moveToFirst()) {
               contact = cur.getString(contactName);
                return contact;
            }
        } finally {
            if (cur != null)
            cur.close();
        }
        return null;
    }

    public void dataManager(List<String>phoneNumbers, List<String> callDuration,List<Date>callDatebuf){
        Map<String, Integer> phNoCount = new HashMap<String, Integer>();
        HashMap<Double, String> probScores = new HashMap<Double, String>();
        HashMap<String, String> displayContact = new HashMap<String, String>();
        int countTop = 1;

        if(!probScores.isEmpty()){
            probScores.clear();
        }
        for (String temp : phoneNumbers) {
            Integer count = phNoCount.get(temp);
            phNoCount.put(temp, (count == null) ? 1 : count + 1);
        }
        double sum = 0.0;
        for (double f : phNoCount.values()) {
            sum += f;
        }

        Iterator it = phNoCount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            it.remove(); // avoids a ConcurrentModificationException
            probabilityScore = (Integer)pair.getValue() / sum;
            probScores.put(probabilityScore, pair.getKey().toString() );
        }

        Map<Double, String> newMap = new TreeMap(Collections.reverseOrder());
        newMap.putAll(probScores);

        Iterator iterator = newMap.entrySet().iterator();
        while (iterator.hasNext() && countTop <= 5) {
            Map.Entry pair = (Map.Entry) iterator.next();
            displayContact.put(contactExists(getApplicationContext(), pair.getValue().toString()), pair.getValue().toString());
            iterator.remove();
            countTop++;
        }
        publishResults(displayContact);
        phNoCount.clear();
        phoneNumbers.clear();
    }


    private void publishResults(HashMap<String, String> displayContact) {
        Intent intentLog = new Intent("callLogs");
        Iterator it = displayContact.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            it.remove();
            intentLog.putExtra(pair.getKey().toString(), pair.getValue().toString());
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentLog);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
