package com.example.apoorvavenkatesh.project;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.*;
/**
 * Created by apoorvavenkatesh on 10/21/16.
 */
public class WeatherForecast {
    private List<DayForecast> daysForecast = new ArrayList<DayForecast>();

    public void addForecast(DayForecast forecast) {
        daysForecast.add(forecast);
        System.out.println("Add forecast [" + forecast + "]");
    }

    public DayForecast getForecast(int dayNum) {
        return daysForecast.get(dayNum);
    }

    public static WeatherForecast getForecastWeather(String data) throws JSONException {

        WeatherForecast forecast = new WeatherForecast();

// We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        JSONArray jArr = jObj.getJSONArray("list"); // Here we have the forecast for every day

// We traverse all the array and parse the data
        for (int i = 0; i < jArr.length(); i++) {
            JSONObject jDayForecast = jArr.getJSONObject(i);

// Now we have the json object so we can extract the data
            DayForecast df = new DayForecast();

// We retrieve the timestamp (dt)
            df.timestamp = jDayForecast.getLong("dt");

// Temp is an object
            JSONObject jTempObj = jDayForecast.getJSONObject("temp");

            df.forecastTemp.day = (float) jTempObj.getDouble("day");
            df.forecastTemp.min = (float) jTempObj.getDouble("min");
            df.forecastTemp.max = (float) jTempObj.getDouble("max");
            df.forecastTemp.night = (float) jTempObj.getDouble("night");
            df.forecastTemp.eve = (float) jTempObj.getDouble("eve");
            df.forecastTemp.morning = (float) jTempObj.getDouble("morn");

// Pressure &amp; Humidity
            df.weather.currentCondition.setPressure((float) jDayForecast.getDouble("pressure"));
            df.weather.currentCondition.setHumidity((float) jDayForecast.getDouble("humidity"));

// ...and now the weather
            JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
            JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
            df.weather.currentCondition.setWeatherId(getInt("id", jWeatherObj));
            df.weather.currentCondition.setDescr(getString("description", jWeatherObj));
            df.weather.currentCondition.setCondition(getString("main", jWeatherObj));
            df.weather.currentCondition.setIcon(getString("icon", jWeatherObj));

            forecast.addForecast(df);
        }

        return forecast;
    }
    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
