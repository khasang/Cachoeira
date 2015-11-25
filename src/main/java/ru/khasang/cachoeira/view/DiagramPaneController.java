package ru.khasang.cachoeira.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by truesik on 25.11.2015.
 */
//Здесь
public class DiagramPaneController {
    @FXML
    private Tab taskTab;
    @FXML
    private Tab resourceTab;

    public DiagramPaneController() {
        try {
            FXMLLoader task = new FXMLLoader(getClass().getResource("/fxml/TaskPane.fxml"));
            VBox taskPane = task.load();
            taskTab.setContent(taskPane);

            FXMLLoader resource = new FXMLLoader(getClass().getResource("/fxml/ResourcePane.fxml"));
            VBox resourcePane = resource.load();
            resourceTab.setContent(resourcePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
