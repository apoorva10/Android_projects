package com.example.arun.mcproject_pa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Arun! on 28-07-2015.
 */
public class GetRadius extends Activity implements View.OnClickListener {

    TextView tvLat,tvLong;
    EditText etRadius,etGName;
    Button btAdd;
    LatLng abc;
    String lat;
    String longi,radi,gname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radius);
        tvLat = (TextView)findViewById(R.id.tvLat);
        tvLong = (TextView)findViewById(R.id.tvLong);
        etRadius = (EditText)findViewById(R.id.etRadius);
        etGName = (EditText)findViewById(R.id.etGName);
        btAdd = (Button)findViewById(R.id.btAdd);
        btAdd.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        lat = bundle.getString("lati");
        longi = bundle.getString("longi");
        setLatLng();

    }


    private void setLatLng() {
        tvLat.setText(lat);
        tvLong.setText(longi);
    }

    @Override
    public void onClick(View v) {
        getData();
        //GeoMain g = new GeoMain();
        //g.addGeofenceList(gname, lat, longi, radi);
        //Constants.AREA_LANDMARKS.put(gname,new LatLng(Double.parseDouble(lat),Double.parseDouble(longi)));
        //Toast.makeText(this,"Added fence",Toast.LENGTH_SHORT);

            GeoData entry = new GeoData(this);
            entry.open();
            entry.createEntry(gname, lat, longi, radi);
            entry.close();


            Intent i = new Intent(GetRadius.this, GeoMain.class);
            startActivity(i);
            finish();

    }

    public void getData() {
        radi = String.valueOf(etRadius.getText());
        gname = String.valueOf(etGName.getText());
    }
}
