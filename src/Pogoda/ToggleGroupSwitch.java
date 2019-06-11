package Pogoda;

import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;

public class ToggleGroupSwitch {

    public static void disableAll(ToggleGroup toggleGroup) {
        toggleGroup.getToggles().forEach(toggle -> {
            Node node = (Node) toggle;
            node.setDisable(true);
        });
    }

    public static void enableAll(ToggleGroup toggleGroup) {
        toggleGroup.getToggles().forEach(toggle -> {
            Node node = (Node) toggle;
            node.setDisable(false);
        });
    }

}
