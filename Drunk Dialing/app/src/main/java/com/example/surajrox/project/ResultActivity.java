package com.example.surajrox.project;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = new Intent(this,ScreenOffActivity.class);
        final Intent intent1 = new Intent(this,QuizActivity.class);
        Handler handler = new Handler();
        TextView t=(TextView)findViewById(R.id.textResult);
        Bundle b = getIntent().getExtras();
        int score= b.getInt("score");
        switch (score)
        {
            case 1: t.setText("Sorry. You are drunk.");
                Toast.makeText(this, "Lock", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent1);
                    }
                }, 1000);
                break;
            case 2: t.setText("Sorry. You are drunk.");
                Toast.makeText(this, "Lock", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent1);
                    }
                }, 1000);
                break;
            case 3: t.setText("Sorry. You are drunk.");
                Toast.makeText(this, "Lock", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent1);
                    }
                }, 1000);
                break;
            case 4: t.setText("Sorry. You are drunk.");
                Toast.makeText(this, "Lock", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent1);
                    }
                }, 1000);
                break;
            case 5: t.setText("Good. You are fine.");
                moveTaskToBack(true);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_result, menu);
        return true;
    }
}
