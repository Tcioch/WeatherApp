package Pogoda;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class Controller implements Initializable {

        @FXML private TextField autoCompleteTextField;
        @FXML private Pane chartPane = new Pane();
        @FXML private ImageView mainImageWeather = new ImageView();
        @FXML private ImageView image1_day2 = new ImageView();
        @FXML private ImageView image1_day3 = new ImageView();
        @FXML private ImageView image1_day4 = new ImageView();
        @FXML private ImageView image1_day5 = new ImageView();
        @FXML private Label temperature1 = new Label();
        @FXML private Label temp1_day2 = new Label();
        @FXML private Label temp1_day3 = new Label();
        @FXML private Label temp1_day4 = new Label();
        @FXML private Label temp1_day5 = new Label();
        @FXML private Label description1 = new Label();
        @FXML private Label desc1_day2 = new Label();
        @FXML private Label desc1_day3 = new Label();
        @FXML private Label desc1_day4 = new Label();
        @FXML private Label desc1_day5 = new Label();
        @FXML private Label date1 = new Label();
        @FXML private Label date1_day2 = new Label();
        @FXML private Label date1_day3 = new Label();
        @FXML private Label date1_day4 = new Label();
        @FXML private Label date1_day5 = new Label();
        @FXML private HBox hboxDays1 = new HBox();

        @FXML private TextField autoCompleteTextField2;
        @FXML private Pane chartPane2 = new Pane();
        @FXML private ImageView mainImageWeather2 = new ImageView();
        @FXML private ImageView image2_day2 = new ImageView();
        @FXML private ImageView image2_day3 = new ImageView();
        @FXML private ImageView image2_day4 = new ImageView();
        @FXML private ImageView image2_day5 = new ImageView();
        @FXML private Label temperature2 = new Label();
        @FXML private Label temp2_day2 = new Label();
        @FXML private Label temp2_day3 = new Label();
        @FXML private Label temp2_day4 = new Label();
        @FXML private Label temp2_day5 = new Label();
        @FXML private Label description2 = new Label();
        @FXML private Label desc2_day2 = new Label();
        @FXML private Label desc2_day3 = new Label();
        @FXML private Label desc2_day4 = new Label();
        @FXML private Label desc2_day5 = new Label();
        @FXML private Label date2 = new Label();
        @FXML private Label date2_day2 = new Label();
        @FXML private Label date2_day3 = new Label();
        @FXML private Label date2_day4 = new Label();
        @FXML private Label date2_day5 = new Label();
        @FXML private HBox hboxDays2 = new HBox();

        @FXML private AreaChartWeather chart;
        @FXML private AreaChartWeather chart2;
        @FXML private ToggleGroup weatherToggle1 = new ToggleGroup();
        @FXML private ToggleGroup weatherToggle2 = new ToggleGroup();


        final ChangeListener<Toggle> listener1 =
            (ObservableValue<? extends Toggle> observable,
             Toggle old, Toggle now) -> {
                if (now == null) {
                    weatherToggle1.selectToggle(old);
                }
            };

        final ChangeListener<Toggle> listener2 =
            (ObservableValue<? extends Toggle> observable,
             Toggle old, Toggle now) -> {
                if (now == null) {
                    weatherToggle2.selectToggle(old);
                }
            };

        private Weather weather = new Weather();
        private GooglePlaces googlePlaces = new GooglePlaces();
        private ArrayList<String[]> autoPlaces;

        private AutoCompletionBinding<String[]> autoComplete1;
        private AutoCompletionBinding<String[]> autoComplete2;

        private ChartContentCreator chartContentCreator = new ChartContentCreator();
        private ChartContentCreator chartContentCreator2 = new ChartContentCreator();

        private StringConverter<String[]> converter = new StringConverter<String[]>() {
            @Override
            public String toString(String[] object) {
                return object[0].toString();
            }

            @Override
            public String[] fromString(String string) {
                return new String[0];
            }
        };

        private Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String[]>> callback =
        new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String[]>>() {
            @Override
            public ArrayList<String[]> call(AutoCompletionBinding.ISuggestionRequest param) {
                if (param.isCancelled() || param.getUserText().length() < 3)
                    return new ArrayList<String[]>();

                try{
                    ArrayList<String[]> places = googlePlaces.predictPlaces(param.getUserText());
                    autoPlaces = places;

                } catch (IOException e) {
                    System.out.println("Błąd: " + e.getMessage() );
                }
                return autoPlaces;
            }
        };

        @Override
        public void initialize(URL location, ResourceBundle resources){
            autoComplete1 = TextFields.bindAutoCompletion(autoCompleteTextField,callback, converter);
            autoComplete1.setMinWidth(400);
            autoCompleteTextField = TextFields.createClearableTextField();
            weatherToggle1.selectedToggleProperty().addListener(listener1);
            ToggleGroupSwitch.disableAll(weatherToggle1);
            autoComplete2 = TextFields.bindAutoCompletion(autoCompleteTextField2,callback, converter);
            autoComplete2.setMinWidth(400);
            autoCompleteTextField2 = TextFields.createClearableTextField();
            weatherToggle2.selectedToggleProperty().addListener(listener2);
            ToggleGroupSwitch.disableAll(weatherToggle2);
            addEvents();
        }

        private void addEvents() {
            autoComplete1.setOnAutoCompleted(event -> {
                try {
                    Double[] coordinates = googlePlaces.getCoordinates(event.getCompletion()[1].toString());
                    weather.downloadWeatherForPlace(coordinates[0], coordinates[1], 1, coordinates[2].intValue());
                    SingleWeatherData singleWeatherData = weather.getWeatherForDay(0, 1);

                    ImageView[] images1 = {mainImageWeather, image1_day2, image1_day3, image1_day4, image1_day5};
                    Label[] dates1 = {date1, date1_day2, date1_day3, date1_day4, date1_day5};
                    Label[] informations1 = {description1, desc1_day2, desc1_day3, desc1_day4, desc1_day5};
                    Label[] temperatures1 = {temperature1, temp1_day2, temp1_day3, temp1_day4, temp1_day5};
                    setInformations(weather.getAllWeatherData(1), images1, dates1, informations1, temperatures1);
                    hboxDays1.setVisible(true);

                    if (chart == null) {
                        chart = chartContentCreator.createNewChart(chart, weather.getAllWeatherData(1));
                        chartPane.getChildren().add(chart);
                        ToggleGroupSwitch.enableAll(weatherToggle1);
                    } else {
                        chartPane.getChildren().remove(chart);
                        chart = chartContentCreator.createNewChart(chart, weather.getAllWeatherData(1));
                        chartPane.getChildren().add(chart);
                    }
                    chartContentCreator.changeData(chart, weather.getAllWeatherData(1),
                            (ToggleButton) weatherToggle1.getSelectedToggle());

                } catch (IOException e) {
                    System.out.println("Błąd:" + e.getMessage());
                }
            });

            autoComplete2.setOnAutoCompleted(event -> {
                try {
                    Double[] coordinates = googlePlaces.getCoordinates(event.getCompletion()[1].toString());
                    weather.downloadWeatherForPlace(coordinates[0], coordinates[1], 2, coordinates[2].intValue());
                    SingleWeatherData singleWeatherData = weather.getWeatherForDay(0, 2);

                    ImageView[] images2 = {mainImageWeather2, image2_day2, image2_day3, image2_day4, image2_day5};
                    Label[] dates2 = {date2, date2_day2, date2_day3, date2_day4, date2_day5};
                    Label[] informations2 = {description2, desc2_day2, desc2_day3, desc2_day4, desc2_day5};
                    Label[] temperatures2 = {temperature2, temp2_day2, temp2_day3, temp2_day4, temp2_day5};
                    setInformations(weather.getAllWeatherData(2), images2, dates2, informations2, temperatures2);
                    hboxDays2.setVisible(true);

                    if (chart2 == null) {
                        chart2 = chartContentCreator2.createNewChart(chart2, weather.getAllWeatherData(2));
                        chartPane2.getChildren().add(chart2);
                        ToggleGroupSwitch.enableAll(weatherToggle2);
                    } else {
                        chartPane2.getChildren().remove(chart2);
                        chart2 = chartContentCreator2.createNewChart(chart2, weather.getAllWeatherData(2));
                        chartPane2.getChildren().add(chart2);
                    }
                    chartContentCreator2.changeData(chart2, weather.getAllWeatherData(2),
                            (ToggleButton) weatherToggle2.getSelectedToggle());

                } catch (IOException e) {
                    System.out.println("Błąd:" + e.getMessage());
                }
            });

        }

        @FXML
        private void changeChart(ActionEvent event) {
            if (((ToggleButton)event.getSource()).getToggleGroup() == weatherToggle1) {
                chartContentCreator.changeData(chart, weather.getAllWeatherData(1), (ToggleButton)weatherToggle1.getSelectedToggle());
            } else if(((ToggleButton)event.getSource()).getToggleGroup() == weatherToggle2) {
                chartContentCreator2.changeData(chart2, weather.getAllWeatherData(2),
                        (ToggleButton)weatherToggle2.getSelectedToggle());
            }
        }

        private void setInformations(SingleWeatherData[] weatherData, ImageView[] image, Label[] date,
                                     Label[] description, Label[] temp) {

            DecimalFormat decimalFormat = new DecimalFormat("#0.0");

            image[0].setImage(new Image(("img/" + weatherData[0].getIcon() + ".png")));
            temp[0].setText(decimalFormat.format(weatherData[0].getTemperature()) +
                    "\u00B0C");
            description[0].setText(weatherData[0].getDescription());
            date[0].setText(weatherData[0].getDayName() + " " + weatherData[0].getDay() + "." +
                    ( weatherData[0].getMonth() < 10 ? ("0" + weatherData[0].getMonth()) :
                            weatherData[0].getMonth()) + "." + weatherData[0].getYear());
            int indexOfSecondDay = WeatherDataParser.getIndexOfMiddleOfSecondDay(weatherData);

            int dayIndex = 1;
            for ( int i = indexOfSecondDay; i < 40 && dayIndex < 5; i = i + 8 ) {
                image[dayIndex].setImage(new Image(("img/" + weatherData[i].getIcon() + ".png")));
                temp[dayIndex].setText(decimalFormat.format(weatherData[i].getTemperature()) +
                        "\u00B0C");
                description[dayIndex].setText(weatherData[i].getDescription());
                date[dayIndex].setText(weatherData[i].getDayName() + " " + weatherData[i].getDay() + "." +
                        ( weatherData[i].getMonth() < 10 ? ("0" + weatherData[i].getMonth()) :
                                weatherData[i].getMonth()) + "." + weatherData[i].getYear());
                dayIndex++;
            }

        }
}