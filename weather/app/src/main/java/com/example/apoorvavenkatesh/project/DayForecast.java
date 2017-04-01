package com.example.apoorvavenkatesh.project;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by apoorvavenkatesh on 10/21/16.
 */
public class DayForecast {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public ForecastTemp forecastTemp = new ForecastTemp();
    public Weather weather=new Weather();
    public long timestamp;

    public class ForecastTemp {
        public float day;
        public float min;
        public float max;
        public float night;
        public float eve;
        public float morning;
    }

    public String getStringDate() {
        return sdf.format(new Date(timestamp));
    }
}
