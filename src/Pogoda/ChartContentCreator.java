package Pogoda;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ToggleButton;

public class ChartContentCreator {
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series<Number, Number> series;

    public AreaChartWeather createNewChart(AreaChartWeather chart, SingleWeatherData[] weatherData){
        series = new XYChart.Series<>();
        for(int i = 0; i<40; i++) {
            series.getData().add(new XYChart.Data<Number, Number>(0, 0));
        }
        xAxis = new NumberAxis(DateConverter.getDateAndTimeInLong(weatherData[0]), DateConverter.getDateAndTimeInLong(weatherData[39]), 10800000);
        yAxis = new NumberAxis(0, 0, 0);
        yAxis.setPrefWidth(50);
        xAxis.setLabel("Godzina");
        chart = new AreaChartWeather(xAxis, yAxis);
        long firstHour = DateConverter.getDateAndTimeInLong(weatherData[0]);
        long intervalFromFirstHour =  (24 - weatherData[0].getHour()) * 3600000l;
        long start = firstHour + intervalFromFirstHour;
        long end = start + 24 * 3600000l;
        XYChart.Data<Number, Number> verticalRangeMarker = new XYChart.Data<>(start,end);
        chart.addVerticalRangeMarker(verticalRangeMarker);
        while (end <= DateConverter.getDateAndTimeInLong(weatherData[39])) {
            start += 48 * 3600000l;
            end = start + 24* 3600000l;
            XYChart.Data<Number, Number> verticalRangeMarkers = new XYChart.Data<>(start,end);
            chart.addVerticalRangeMarker(verticalRangeMarkers);
        }
        chart.setPrefSize(750,250);
        chart.setLegendVisible(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelFormatter(DateConverter.dateIntToString);
        xAxis.setMinorTickVisible(false);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(true);
        chart.setAlternativeColumnFillVisible(false);
        chart.setAlternativeRowFillVisible(false);
        chart.setAnimated(false);
        chart.getData().add(series);
        return chart;
    }



    public void changeData(AreaChartWeather chart , SingleWeatherData[] weatherData, ToggleButton button) {
        int max = -9999;
        int min = 9999;
        String buttonId = button.getId();
        buttonId = buttonId.substring(0, buttonId.length() - 1);
        switch (buttonId) {
            case "temp" :
                for (int i = 0; i<40; i++) {
                    float temp = weatherData[i].getTemperature().floatValue();
                    max = ((int)temp > max) ? (int)temp : max;
                    min = ((int)temp < min) ? (int)temp : min;
                    series.getData().set(i,
                            new XYChart.Data<Number, Number>(DateConverter.getDateAndTimeInLong(weatherData[i]), temp));
                }
                yAxis.setLowerBound((int)min-2); yAxis.setUpperBound((int)max+2); yAxis.setTickUnit(2);
                chart.setTitle("Wykres temperatury");
                yAxis.setLabel("Temperatura [\u00B0C]");
                break;
            case "clouds" :
                for (int i = 0; i<40; i++) {
                    int clouds = weatherData[i].getClouds();
                    max = (clouds > max) ? clouds : max;
                    min = (clouds < min) ? clouds : min;
                    series.getData().set(i,new XYChart.Data<Number, Number>(DateConverter.getDateAndTimeInLong(weatherData[i]), clouds));
                }
                yAxis.setLowerBound(0); yAxis.setUpperBound( (max >= 95) ? 100 : max+5 ); yAxis.setTickUnit(10);
                chart.setTitle("Wykres zachmurzenia");
                yAxis.setLabel("Zachmurzenie [%]");
                break;
            case "humidity" :
                for (int i = 0; i<40; i++) {
                    int humidity = weatherData[i].getHumidity();
                    max = (humidity > max) ? humidity : max;
                    min = (humidity < min) ? humidity : min;
                    series.getData().set(i,new XYChart.Data<Number, Number>(DateConverter.getDateAndTimeInLong(weatherData[i]), humidity));
                }
                yAxis.setLowerBound(min - 5); yAxis.setUpperBound( (max >= 95) ? 100 : max+5 ); yAxis.setTickUnit(10);
                chart.setTitle("Wykres wilgotności");
                yAxis.setLabel("Wilgotność [%]");
                break;
            case "pressure" :
                for (int i = 0; i<40; i++) {
                    Double pressure = weatherData[i].getPressure();
                    max = (pressure.intValue() > max) ? pressure.intValue() : max;
                    min = (pressure.intValue() < min) ? pressure.intValue() : min;
                    series.getData().set(i,new XYChart.Data<Number, Number>(DateConverter.getDateAndTimeInLong(weatherData[i]), pressure));
                }
                yAxis.setLowerBound( min - 3 ); yAxis.setUpperBound( max + 3); yAxis.setTickUnit(3);
                chart.setTitle("Wykres ciśnienia");
                yAxis.setLabel("Ciśnienie [hPa]");
                break;
            case "wind" :
                for (int i = 0; i<40; i++) {
                    Double windSpeed = weatherData[i].getWindSpeed() * 3.6;
                    max = (windSpeed.intValue() > max) ? windSpeed.intValue() : max;
                    min = (windSpeed.intValue() < min) ? windSpeed.intValue() : min;
                    series.getData().set(i,new XYChart.Data<Number, Number>(DateConverter.getDateAndTimeInLong(weatherData[i]), windSpeed));
                }
                yAxis.setLowerBound( min > 2 ? min - 2 : 0 ); yAxis.setUpperBound( max + 2); yAxis.setTickUnit(2);
                chart.setTitle("Wykres prędkości wiatru");
                yAxis.setLabel("Prędkość wiatru [km/h]");
                break;
            case "rain" :
                for (int i = 0; i<40; i++) {
                    Double rain = weatherData[i].getRain();
                    max = (rain.intValue() > max) ? rain.intValue() : max;
                    min = (rain.intValue() < min) ? rain.intValue() : min;
                    series.getData().set(i,new XYChart.Data<Number, Number>(DateConverter.getDateAndTimeInLong(weatherData[i]), rain));
                }
                yAxis.setLowerBound( 0 ); yAxis.setUpperBound( max + 1); yAxis.setTickUnit(1);
                chart.setTitle("Wykres opadów deszczu");
                yAxis.setLabel("Opady [mm]");
                break;
            case "snow" :
                for (int i = 0; i<40; i++) {
                    Double snow = weatherData[i].getSnow();
                    max = (snow.intValue() > max) ? snow.intValue() : max;
                    min = (snow.intValue() < min) ? snow.intValue() : min;
                    series.getData().set(i,new XYChart.Data<Number, Number>(DateConverter.getDateAndTimeInLong(weatherData[i]), snow));
                }
                yAxis.setLowerBound( 0 ); yAxis.setUpperBound( max + 1); yAxis.setTickUnit(1);
                chart.setTitle("Wykres opadów śniegu");
                yAxis.setLabel("Opady [mm]");
                break;
        }
    }


}
