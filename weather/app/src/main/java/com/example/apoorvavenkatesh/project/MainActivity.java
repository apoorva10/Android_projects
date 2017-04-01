package com.example.apoorvavenkatesh.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import android.location.Geocoder;
import android.os.AsyncTask;

import android.view.View;
import android.widget.Toast;
import android.support.v4.view.ViewPager;
import java.util.Locale;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    private TextView unitTemp;
    private EditText text;
    private Button search;
    private TextView hum;
    private ImageView imgView;

    private static String forecastDaysNum = "3";
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //String city = "Tempe, US";
        //String lang = "en";

        cityText = (TextView) findViewById(R.id.cityText);
        temp = (TextView) findViewById(R.id.temp);
        unitTemp = (TextView) findViewById(R.id.unittemp);
        //unitTemp.setText("°F");
        condDescr = (TextView) findViewById(R.id.skydesc);

        pager = (ViewPager) findViewById(R.id.pager);
        imgView = (ImageView) findViewById(R.id.condIcon);

        text=(EditText) findViewById(R.id.editText);

        String val=text.getText().toString();
        search=(Button) findViewById(R.id.button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text != null) {
                    JSONWeatherTask task = new JSONWeatherTask();
                    task.execute(new String[]{text.getText().toString(), "en"});

                    JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
                    task1.execute(new String[]{text.getText().toString(), "en", forecastDaysNum});
                }
            }
        });


        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{"Tempe","en"});

        JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
        task1.execute(new String[]{"Tempe","en",forecastDaysNum});
    }
    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();

            String data = ( (new WeatherHttpClient()).getWeatherData(params[0], params[1]));

            try {
                weather = JSONWeatherParser.getWeather(data);
                System.out.println("Weather ["+weather+"]");
                // Let's retrieve the icon
               // weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);


       cityText.setText(weather.location.getCity() + "," + weather.location.getCountry()+"          " +Math.round((weather.temperature.getTemp() - 215))+" °F");
           // temp.setText("Tempe,US" + Math.round((weather.temperature.getTemp() - 215)));
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
        }
    }


    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {

        @Override
        protected WeatherForecast doInBackground(String... params) {

            String data = ( (new WeatherHttpClient()).getForecastWeatherData(params[0], params[1], params[2]));
           System.out.println("DAta in main"+data);
            WeatherForecast forecast = new WeatherForecast();
            try {
                forecast = JSONWeatherParser.getForecastWeather(data);
                //System.out.println("Weather ["+forecast+"]°F");
                // Let's retrieve the icon
               // weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println("data in main"+data+"forecast in main"+forecast);
            return forecast;

        }



        @Override
        protected void onPostExecute(WeatherForecast forecastWeather) {
            super.onPostExecute(forecastWeather);
            DailyForecastPageAdapter adapter=null;

          /* if(text!=null) {

               int pos=pager.getCurrentItem();
               if(pos==0)
               {
                   pager.setCurrentItem(1);
               }

               DayForecast fo = new DayForecast();
               fo.forecastTemp.min -= 285;
               fo.forecastTemp.max -= 285;
               WeatherForecast forecast = new WeatherForecast();
               forecast.addForecast(fo);
               DailyForecastPageAdapter1 adapter1 = new DailyForecastPageAdapter1(Integer.parseInt(forecastDaysNum), getSupportFragmentManager(), forecastWeather);
               pager.setAdapter(adapter1);

           }*/

            {
                DayForecast fo = new DayForecast();
                fo.forecastTemp.min -= 0;
                fo.forecastTemp.max -= 0;
                WeatherForecast forecast = new WeatherForecast();
                forecast.addForecast(fo);
               adapter  = new DailyForecastPageAdapter(Integer.parseInt(forecastDaysNum), getSupportFragmentManager(), forecastWeather);
                pager.setAdapter(adapter);
            }
        }

    }
  /*  class MyLocationListener implements LocationListener {


        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onLocationChanged(android.location.Location loc) {
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            // Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            // Log.v(TAG, latitude);

        /*------- To get city name from coordinates --------
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
            Toast.makeText(getBaseContext(),cityName,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }*/
}
