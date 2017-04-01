package com.example.arun.mcproject_pa;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.content.LocalBroadcastManager;
import android.text.util.Linkify;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartAlerts extends Activity {
    final TextView[] URLTextViews = new TextView[5];
    final TextView[] myTextViews = new TextView[5];
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_alerts);


        Intent i2 = new Intent(this, SmartAlertsBrowserService.class);
        startService(i2);

        Intent i = new Intent(this, SmartAlertsService.class);
        startService(i);


        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                br, new IntentFilter("callLogs"));


        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("URLs"));

        /*LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                br, new IntentFilter("callLogs"));
*/

    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        Object value = new Object();
        String PhnoForDialer;
        //String textData;
        @Override
        public void onReceive(Context context, Intent intent) {

            final List<String> priority = new ArrayList<String>();
            LinearLayout ll = (LinearLayout) findViewById(R.id.loglinearLayout);

            Bundle bundle = intent.getExtras();

            for (String key : bundle.keySet()) {
                value = bundle.get(key);
                ringtoneSetter(value.toString());
                priority.add(key + " " + value.toString());
            }

            for (int i = 4; i >= 0; i--) {
                final TextView rowTextView = new TextView(getApplicationContext());
                final String textData = priority.get(i);
                rowTextView.setText(textData);
                rowTextView.setTextSize(20);
                rowTextView.setClickable(true);

                rowTextView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          String regex = "([0-9]+)";
                          Pattern p = Pattern.compile(regex);
                          Matcher m = p.matcher(textData);

                          if (m.find()) {
                              PhnoForDialer = m.group(1);
                          }
                          String phone = PhnoForDialer;
                          Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                                  "tel", phone, null));
                          startActivity(phoneIntent);
                      }
                  });
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
            } catch (Exception e) {
                e.getMessage();
            } finally {
                // Don't forget to close your Cursor
                data.close();

            }

        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.urllinearLayout);
            ArrayList<String> url = intent.getExtras().getStringArrayList("URL");
            System.out.println(url);

            for (int i = url.size() - 1; i >= 0; i--) {
                final TextView rowTextView = new TextView(getApplicationContext());
                if (url.get(i) != null) {
                    final String urls = url.get(i);
                    rowTextView.setTextSize(20);
                    rowTextView.setTextColor(Color.WHITE);
                    Linkify.addLinks(rowTextView, Linkify.WEB_URLS);

                    rowTextView.setPaintFlags(rowTextView.getPaintFlags()
                            | Paint.UNDERLINE_TEXT_FLAG);
                    rowTextView.setText(url.get(i));
                    rowTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse("http://" + urls));
                            Intent chooserIntent = Intent.createChooser(browserIntent, "Choose Browser ");
                            startActivity(chooserIntent);
                        }
                    });
                    ll.addView(rowTextView);
                }
                URLTextViews[i] = rowTextView;
            }
        }
    };
}
