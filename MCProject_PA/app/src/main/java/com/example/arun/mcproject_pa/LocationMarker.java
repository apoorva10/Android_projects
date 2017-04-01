package com.example.arun.mcproject_pa;

/**
 * Created by Arun! on 28-07-2015.
 */
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.maps.GeoPoint;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.*;

public class LocationMarker extends FragmentActivity implements LocationListener, View.OnClickListener {

    GoogleMap map;
    //LatLng latLng;
    Button dMark;
    String lati="nope",longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        dMark = (Button)findViewById(R.id.dMark);
        dMark.setOnClickListener(this);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker")
                .draggable(true)
                .snippet("Drag Me")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        map.setOnMarkerDragListener(new OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO Auto-generated method stub
                // Here your code
                Toast.makeText(LocationMarker.this, "Dragging Start",
                        Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub
                LatLng position = marker.getPosition();
                Toast.makeText( LocationMarker.this,
                        "Lat " + position.latitude + " "
                                + "Long " + position.longitude,
                        Toast.LENGTH_LONG).show();
                lati = String.valueOf(position.latitude);
                longi = String.valueOf(position.longitude);
            }


            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub
                // Toast.makeText(MainActivity.this, "Dragging",
                // Toast.LENGTH_SHORT).show();
                System.out.println("Dragging");
            }
        });

    }





    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(LocationMarker.this, GetRadius.class);
        Bundle bundle = new Bundle();
        bundle.putString("lati",lati);
        bundle.putString("longi",longi);

//Add the bundle to the intent
        i.putExtras(bundle);

//Fire that second activity
        startActivity(i);

        finish();
    }
}