package Pogoda;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class WeatherDataParser {

    public static SingleWeatherData getSingleDayData(JSONObject weatherData, int dayIndex, int minutesOffset) throws JSONException {

        JSONObject cityDataToParse = JSONDataSplitter.getCityData(weatherData);
        String city = cityDataToParse.getString("name") ;
        double longitude = cityDataToParse.getJSONObject("coord").getDouble("lon");
        double latitude = cityDataToParse.getJSONObject("coord").getDouble("lat");

        JSONObject weatherDataToParse = JSONDataSplitter.getOneDayFromWeatherData(weatherData, dayIndex);

        String dateToParse = weatherDataToParse.getString("dt_txt");
        LocalDateTime dateAndTime = DateConverter.ConvertDate(dateToParse);
        if (minutesOffset >= 0) dateAndTime = dateAndTime.plusMinutes((long)minutesOffset);
        else {
            minutesOffset = minutesOffset * (-1);
            dateAndTime = dateAndTime.minusMinutes((long)minutesOffset);
        }
        int day = dateAndTime.getDayOfMonth();
        int month = dateAndTime.getMonthValue();
        int year = dateAndTime.getYear();
        String dayName = DateConverter.ConvertDay(dateAndTime.getDayOfWeek());
        int hour = dateAndTime.getHour();
        double pressure = weatherDataToParse.getJSONObject("main").getDouble("grnd_level");
        double temperature = weatherDataToParse.getJSONObject("main").getDouble("temp");
        int humidity = weatherDataToParse.getJSONObject("main").getInt("humidity");

        String description = weatherDataToParse.getJSONArray("weather").getJSONObject(0).getString("description");
        description = Character.toUpperCase(description.charAt(0)) + description.substring(1);
        String icon = weatherDataToParse.getJSONArray("weather").getJSONObject(0).getString("icon");

        double rain;
        double snow;
        try {
            rain = weatherDataToParse.getJSONObject("rain").getDouble("3h");
        } catch ( JSONException exp) {
            rain = 0;
        }
        try {
            snow = weatherDataToParse.getJSONObject("snow").getDouble("3h");
        } catch ( JSONException exp) {
            snow = 0;
        }
        int clouds = weatherDataToParse.getJSONObject("clouds").getInt("all");
        double windSpeed = weatherDataToParse.getJSONObject("wind").getDouble("speed");

        SingleWeatherData singleWeatherData = new SingleWeatherData(city, longitude, latitude, day, month, year,
                dayName, hour, description, pressure, temperature, humidity, rain, snow, windSpeed, clouds, icon);

        return singleWeatherData;
    }

    public static int getIndexOfMiddleOfSecondDay(SingleWeatherData[] weatherData){

        Long firstDay = DateConverter.getDateInLong(weatherData[0]);
        for (int i = 1; i < 40; i++) {
            if (firstDay < DateConverter.getDateInLong(weatherData[i]) && weatherData[i].getHour() >= 12 && weatherData[i].getHour() <= 14) {
                return i;
            }
        }
        return 0;
    }

}
