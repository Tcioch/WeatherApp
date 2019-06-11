package Pogoda;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GooglePlaces {

    private ApiCaller apiCaller = new ApiCaller();

    public ArrayList predictPlaces(String input) throws IOException {

        JSONObject places = this.apiCaller.getPlacesPredictions(input);

        String status = places.getString("status");
        int numberOfRows = places.getJSONArray("predictions").length();

        ArrayList<String[]> placesList = new ArrayList<>();

        if (status.equalsIgnoreCase("OK")) {
            for(int i = 0; i < numberOfRows; i++) {
                String desciption = places.getJSONArray("predictions").getJSONObject(i).getString("description");
                String placeId = places.getJSONArray("predictions").getJSONObject(i).getString("place_id");
                String[] placeArray = new String[2];
                placeArray[0] = desciption;
                placeArray[1] = placeId;
                placesList.add(placeArray);
            }
        } else System.out.println("Brak podpowiedzi");

        return placesList;
    }

    public Double[] getCoordinates(String placeId) throws IOException{

        JSONObject coordinates = this.apiCaller.getPlaceCoordinates(placeId);
        double latitude =
                coordinates.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        double longitude =
                coordinates.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble(
                        "lng");
        double utc_offset = coordinates.getJSONObject("result").getDouble("utc_offset");
        Double[] coords = new Double[3];
        coords[0] = latitude;
        coords[1] = longitude;
        coords[2] = utc_offset;

        return coords;
    }

}
