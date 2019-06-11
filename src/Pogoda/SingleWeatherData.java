package Pogoda;

public class SingleWeatherData {
    private String city;
    private double longitude;
    private double latitude;
    private int day;
    private int month;
    private int year;
    private String dayName;
    private int hour;
    private String description;
    private Double pressure;
    private Double temperature;
    private int humidity;
    private Double rain;
    private Double snow;
    private Double windSpeed;
    private int clouds;
    private String icon;

    public SingleWeatherData(String city, double longitude, double latitude, int day, int month, int year,
                             String dayName, int hour, String description, double pressure, double temperature,
                             int humidity, double rain, double snow, double windSpeed, int clouds, String icon) {
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.day = day;
        this.month = month;
        this.year = year;
        this.dayName = dayName;
        this.hour = hour;
        this.description = description;
        this.pressure = pressure;
        this.temperature = temperature;
        this.humidity = humidity;
        this.rain = rain;
        this.snow = snow;
        this.windSpeed = windSpeed;
        this.clouds = clouds;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getCity() {
        return city;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    public String getDayName() {
        return dayName;
    }

    public Integer getHour() {
        return hour;
    }

    public String getDescription() {
        return description;
    }

    public Double getPressure() {
        return pressure;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Double getRain() {
        return rain;
    }

    public Double getSnow() {
        return snow;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public Integer getClouds() {
        return clouds;
    }
}
