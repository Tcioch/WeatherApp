package Pogoda;

import org.json.JSONObject;

public class JSONDataSplitter {

    public static JSONObject getOneDayFromWeatherData(JSONObject weatherData, int dayIndex){
        return weatherData.getJSONArray("list").getJSONObject(dayIndex);
    }

    public static JSONObject getCityData(JSONObject weatherData){
        return weatherData.getJSONObject("city");
    }
}
