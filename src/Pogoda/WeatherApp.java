package Pogoda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WeatherApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource("Template.fxml"));
        Parent layout = fxmlLoader.load();
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(getClass().getResource("buttons.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("weatherDayPane.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("WeatherApp");

    }
}
