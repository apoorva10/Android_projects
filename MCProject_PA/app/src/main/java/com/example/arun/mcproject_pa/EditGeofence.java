package com.example.arun.mcproject_pa;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Arun! on 30-07-2015.
 */
public class EditGeofence extends Activity implements View.OnClickListener {

    Button sqlModify,sqlDelete;
    EditText sqlRow;
    String fencename = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geoedit);
        sqlRow=(EditText)findViewById(R.id.etRowID);
        sqlModify=(Button)findViewById(R.id.bEditEntry);

        sqlDelete=(Button)findViewById(R.id.bDeleteEntry);
        sqlModify.setOnClickListener(this);

        sqlDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
           case R.id.bEditEntry:
               String sRow = sqlRow.getText().toString();
                //Basic not working
               AlertDialog.Builder builder = new AlertDialog.Builder(EditGeofence.this);
               builder.setMessage("are you sure want to exit");
               builder.setNeutralButton("Ok",new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       // TODO Auto-generated method stub
                       Toast.makeText(getApplicationContext(),"text", Toast.LENGTH_LONG).show();
                   }
               });

               builder.show();
               /*builder.setTitle("Edit Name");
               builder.setMessage("Enter the new name:");

                // Set up the input
               final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
               //input.setInputType(InputType.TYPE_CLASS_TEXT);
               builder.setView(input);

                // Set up the buttons
               builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       fencename = input.getText().toString();
                   }
               });
               builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               });
               builder.create().show();*/
               //Rest of code
                long lRow =Long.parseLong(sRow);
                GeoData ex = new GeoData(this);
                try {
                    ex.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ex.updateEntry(lRow, fencename);
                ex.close();

                break;

            case R.id.bDeleteEntry:
                String sDel = sqlRow.getText().toString();
                long lDel =Long.parseLong(sDel);
                GeoData del=new GeoData(this);
                try {
                    del.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                del.delRow(lDel);
                del.close();


                break;
        }
        }
    }

