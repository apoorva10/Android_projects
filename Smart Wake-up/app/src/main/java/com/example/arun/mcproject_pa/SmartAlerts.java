package com.example.arun.mcproject_pa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SmartAlerts extends Activity {
    final TextView[] myTextViews = new TextView[5];
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_alerts);

        Intent i = new Intent(this, SmartAlertsService.class);
        startService(i);

        Intent i2 = new Intent(this, SmartAlertsBrowserService.class);
        startService(i2);

    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> priority = new ArrayList<String>();
            LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout);

            Bundle bundle = intent.getExtras();

            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                ringtoneSetter(value.toString());
                priority.add(key + " " + value.toString());
            }
            //Toast.makeText(getApplicationContext(),priority.get(0),Toast.LENGTH_LONG).show();
            //modifying code to create a alarm dialogue
            final String value=priority.get(0);
            final CountDownTimer timer=new CountDownTimer(18000,1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    AlertDialog.Builder builder=new AlertDialog.Builder(SmartAlerts.this);
                    builder.setTitle("Reminder");
                    builder.setMessage("Do you want to call "+value);
                    builder.setCancelable(false);
                    builder.setPositiveButton("I will",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("I wont",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SmartAlerts.this.finish();
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();

                    // show it
                    alertDialog.show();



                }
            }.start();
            for (int i = 4; i >= 0; i--) {
                final TextView rowTextView = new TextView(getApplicationContext());
                rowTextView.setText(priority.get(i));
                rowTextView.setTextSize(20);
                rowTextView.setClickable(true);
                ll.addView(rowTextView);
                myTextViews[i] = rowTextView;
            }
        }

            public void ringtoneSetter(String number) {
            final Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number);
            final String[] projection = new String[]{
                    Contacts._ID, Contacts.LOOKUP_KEY
            };
            final Cursor data = getContentResolver().query(lookupUri, projection, null, null, null);
            data.moveToFirst();
            try {
                // Get the contact lookup Uri
                final long contactId = data.getLong(0);
                final String lookupKey = data.getString(1);
                final Uri contactUri = Contacts.getLookupUri(contactId, lookupKey);
                if (contactUri == null) {
                    // Invalid arguments
                    return;
                }

                // Get the path of ringtone you'd like to use
                final String storage = Environment.getExternalStorageDirectory().getPath();
                final File file = new File(storage + "/Music", "The Chainsmokers - Closer (Lyric) ft. Halsey.mp3");
                final String value = Uri.fromFile(file).toString();

                // Apply the custom ringtone
                final ContentValues values = new ContentValues(1);
                values.put(Contacts.CUSTOM_RINGTONE, value);
                getContentResolver().update(contactUri, values, null, null);
            } catch(Exception e){
                e.getMessage();
            }
            finally {
                // Don't forget to close your Cursor
                data.close();
            }

        }
    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        registerReceiver(br, new IntentFilter("1"));

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(br);
    }
}
