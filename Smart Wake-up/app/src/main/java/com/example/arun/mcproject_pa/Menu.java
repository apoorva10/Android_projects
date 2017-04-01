package com.example.arun.mcproject_pa;

/**
 * Created by Arun on 10/19/2016.
 */

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

    String classes[]={"Smart Alerts","A's Under Influence","Smart Profile Mode", "Wake Me Up When September Ends", "Close Enough?", "Medicine Reminder", "Weather Alert"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setListAdapter(new ArrayAdapter<String>(Menu.this, android.R.layout.simple_list_item_1, classes));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String choice=classes[position];
        choice = choice.replaceAll("\\s+","");
        try {
            Class ourClass = Class.forName("com.example.arun.mcproject_pa."+choice);
            Intent ourIntent = new Intent(getApplicationContext(), ourClass);
            startActivity(ourIntent);

        }
        catch(ClassNotFoundException e){e.printStackTrace();}


    }


}

