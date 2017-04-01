package com.example.arun.mcproject_pa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Arun! on 30-07-2015.
 */
public class ViewGeofence extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geoview);
        TextView tv = (TextView)findViewById(R.id.tvSQLInfo);
        GeoData info = new GeoData(this);
        info.open();
        String data = info.getData();
        info.close();
        tv.setText(data);
    }
}
