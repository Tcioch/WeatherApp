package Pogoda;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class ApiCaller {

    private JsonReader jsonReader = new JsonReader();
    private final String API_KEY_WEATHER = "xxx";
    private final String API_KEY_GOOGLE = "xxx";

    public JSONObject getWeatherByCityName(String city)  throws IOException {
        // miasto
        JSONObject weatherData = this.jsonReader.readJsonFromUrl("https://api.openweathermap.org/data/2" +
                ".5/forecast?id=" + city + "&units=metric&lang=pl&appid=" + this.API_KEY_WEATHER);
        return weatherData;
    }

    public JSONObject getWeatherByCoordinates(double latitude, double longitude)  throws IOException {

        JSONObject weatherData = this.jsonReader.readJsonFromUrl("https://api.openweathermap.org/data/2" +
                ".5/forecast?lat=" + latitude + "&lon=" + longitude + "&units=metric&lang=pl&appid=" + this.API_KEY_WEATHER);
        return weatherData;
    }

    public JSONObject getPlacesPredictions(String input) throws  IOException {

        input = URLEncoder.encode(input, "UTF8");
        JSONObject places = this.jsonReader.readJsonFromUrl("https://maps.googleapis" +
                ".com/maps/api/place/autocomplete/json?input=" + input + "&key=" + this.API_KEY_GOOGLE + "&language" +
                "=pl&types" +
                "=geocode");
        return places;
    }

    public JSONObject getPlaceCoordinates(String placeId) throws IOException {

        JSONObject coordinates = this.jsonReader.readJsonFromUrl("https://maps.googleapis" +
                ".com/maps/api/place/details/json?placeid=" + placeId + "&fields=geometry,utc_offset&key=" + this.API_KEY_GOOGLE);
        return coordinates;
    }
}

