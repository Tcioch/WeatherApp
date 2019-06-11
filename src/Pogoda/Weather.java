package Pogoda;

import org.json.JSONObject;

import java.io.IOException;

public class Weather {

    private SingleWeatherData weatherDataFirstPlace[] = new SingleWeatherData[40];
    private SingleWeatherData weatherDataSecondPlace[] = new SingleWeatherData[40];
    private ApiCaller weatherApiCaller = new ApiCaller();


    public void downloadWeatherForPlace(String city, int placeNumber, int utcOffSet) throws IOException {

        JSONObject weatherData = this.weatherApiCaller.getWeatherByCityName(city);
        for (int i = 0; i < 40; i++) {
            SingleWeatherData singleDay = WeatherDataParser.getSingleDayData(weatherData, i, utcOffSet);
            if (placeNumber == 1) weatherDataFirstPlace[i] = singleDay;
            else if (placeNumber == 2) weatherDataSecondPlace[i] = singleDay;
        }
    }

    public void downloadWeatherForPlace(double latitude, double longitude, int placeNumber, int utcOffSet) throws IOException {

        JSONObject weatherData = this.weatherApiCaller.getWeatherByCoordinates(latitude, longitude);
        for (int i = 0; i < 40; i++) {
            SingleWeatherData singleDay = WeatherDataParser.getSingleDayData(weatherData, i, utcOffSet);
            if (placeNumber == 1) weatherDataFirstPlace[i] = singleDay;
            else if (placeNumber == 2) weatherDataSecondPlace[i] = singleDay;
        }
    }

    public SingleWeatherData getWeatherForDay(int dayIndex, int placeNumber) {

        if (placeNumber == 1) return this.weatherDataFirstPlace[dayIndex];
        if (placeNumber == 2) return this.weatherDataSecondPlace[dayIndex];
        else return null;
    }

    public SingleWeatherData[] getAllWeatherData(int place){

        if (place == 1) return weatherDataFirstPlace;
        else if (place == 2) return weatherDataSecondPlace;
        else return null;
    }

}
