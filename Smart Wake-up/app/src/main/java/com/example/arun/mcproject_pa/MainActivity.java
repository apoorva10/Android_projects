package com.example.arun.mcproject_pa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show();
                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
    }

