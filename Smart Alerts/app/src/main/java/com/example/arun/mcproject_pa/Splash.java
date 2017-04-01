package com.example.arun.mcproject_pa;

/**
 * Created by Arun on 10/19/2016.
 */


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;


public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        SharedPreferences getPrefs= PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        Thread timer=new Thread(){
            public void run(){
                try {
                    sleep(1000);
                }
                catch(InterruptedException e){e.printStackTrace();}
                finally {
                    Intent openActivity=new Intent("com.example.arun.mcproject_pa.MENU");
                    startActivity(openActivity);
                }
            }
        };
        timer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();

    }
}

