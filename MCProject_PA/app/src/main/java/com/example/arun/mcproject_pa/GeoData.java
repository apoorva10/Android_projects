package com.example.arun.mcproject_pa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by Arun! on 30-07-2015.
 */
public class GeoData {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_FNAME = "fence_name";
    public static final String KEY_FLAT = "f_lat";
    public static final String KEY_FLONG = "f_long";
    public static final String KEY_FRAD = "f_rad";

    public static final HashMap<String, LatLng> AREA_REQUIRED = new HashMap<String, LatLng>();

    private static final String DATABASE_NAME = "Fencedb";
    private static final String DATABASE_TABLE = "fenceTable";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;
    private String data;

    public GeoData(Context context) {
        ourContext = context;
    }

    public long createEntry(String gname, String lat, String longi, String radi) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_FNAME,gname);
        cv.put(KEY_FLAT,lat);
        cv.put(KEY_FLONG,longi);
        cv.put(KEY_FRAD,radi);
        return ourDatabase.insert(DATABASE_TABLE,null,cv);
    }

    public String getData() {
        String[] columns = new String[]{KEY_ROWID, KEY_FNAME, KEY_FLAT, KEY_FLONG, KEY_FRAD};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        String data = "";

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iFname = c.getColumnIndex(KEY_FNAME);
        int iFlat = c.getColumnIndex(KEY_FLAT);
        int iFlong = c.getColumnIndex(KEY_FLONG);
        int iFrad = c.getColumnIndex(KEY_FRAD);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            data = data + c.getString(iRow) + " " + c.getString(iFname) + " " + c.getString(iFlat) + " " + c.getString(iFlong) + " " + c.getString(iFrad) + "\n";
            double loclat =Double.parseDouble(c.getString(iFlat));
            double loclong = Double.parseDouble(c.getString(iFlong));
            AREA_REQUIRED.put(c.getString(iFname), new LatLng(loclat,loclong));

        }
        return data;

    }

    private static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_FNAME + " TEXT NOT NULL, " + KEY_FLAT + " REAL, " + KEY_FLONG + " REAL, " + KEY_FRAD + " REAL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);

        }
    }
    public void updateEntry(long lRow, String mname) {
        ContentValues cvUpdate = new ContentValues();
        String[] columns = new String[]{KEY_ROWID, KEY_FNAME, KEY_FLAT, KEY_FLONG, KEY_FRAD};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);

        int iFlat = c.getColumnIndex(KEY_FLAT);
        int iFlong = c.getColumnIndex(KEY_FLONG);
        int iFrad = c.getColumnIndex(KEY_FRAD);
        c.move(Integer.parseInt(KEY_ROWID+"="+lRow));
        cvUpdate.put(KEY_FNAME,mname);
        cvUpdate.put(KEY_FLAT,c.getString(iFlat));
        cvUpdate.put(KEY_FLONG,c.getString(iFlong));
        cvUpdate.put(KEY_FRAD,c.getString(iFrad));

        ourDatabase.update(DATABASE_TABLE,cvUpdate,KEY_ROWID+"="+lRow,null);
    }

    public void delRow(long lDel) {
        ourDatabase.delete(DATABASE_TABLE,KEY_ROWID+"="+lDel,null);

    }
    public GeoData  open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        ourHelper.close();
    }
}
