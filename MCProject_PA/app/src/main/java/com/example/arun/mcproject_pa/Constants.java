package com.example.arun.mcproject_pa;
import android.net.Uri;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;

/**
 * Created by Arun! on 06-07-2015.
 */
    /**
     * Constants used in this sample.
     */
    public final class Constants {

        private Constants() {
        }

        public static final String PACKAGE_NAME = "com.example.arun.maps";
        //public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

        public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

        public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

        /**
         * Used to set an expiration time for a geofence. After this amount of time Location Services
         * stops tracking the geofence.
         */
        public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

        /**
         * For this sample, geofences expire after twelve hours.
         */
        public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
                GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
        public static final float GEOFENCE_RADIUS_IN_METERS = 100; // 100meters


        // Geofence parameters for the Android building on Google's main campus in Mountain View.
        public static final String ASH_ID = "1";
        public static final double ASH_LATITUDE = 13.0568202;
        public static final double ASH_LONGITUDE = 80.2248365;
        public static final float ASH_RADIUS_METERS = 2;


        // The constants below are less interesting than those above.

        // Path for the DataItem containing the last geofence id entered.
        public static final String GEOFENCE_DATA_ITEM_PATH = "/geofenceid";
        public static final Uri GEOFENCE_DATA_ITEM_URI =
                new Uri.Builder().scheme("wear").path(GEOFENCE_DATA_ITEM_PATH).build();
        public static final String KEY_GEOFENCE_ID = "geofence_id";

        // Keys for flattened geofences stored in SharedPreferences.
        public static final String KEY_LATITUDE = "com.example.arun.maps.KEY_LATITUDE";
        public static final String KEY_LONGITUDE = "com.example.arun.maps.KEY_LONGITUDE";
        public static final String KEY_RADIUS = "com.example.arun.maps.KEY_RADIUS";
        public static final String KEY_EXPIRATION_DURATION =
                "com.example.arun.maps.KEY_EXPIRATION_DURATION";
        public static final String KEY_TRANSITION_TYPE =
                "com.example.arun.maps.KEY_TRANSITION_TYPE";
        // The prefix for flattened geofence keys.
        public static final String KEY_PREFIX = "com.example.arun.maps.KEY";

        // Invalid values, used to test geofence storage when retrieving geofences.
        public static final long INVALID_LONG_VALUE = -999l;
        public static final float INVALID_FLOAT_VALUE = -999.0f;
        public static final int INVALID_INT_VALUE = -999;

        public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();

        static {
            AREA_LANDMARKS.put("Aruns Place", new LatLng(13.0568202, 80.2248365));
        }
    }