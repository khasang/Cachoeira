package ru.khasang.cachoeira.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import ru.khasang.cachoeira.controller.IController;

import java.io.IOException;

/**
 * Created by truesik on 25.11.2015.
 */
public class PropertiesPanelController {
    @FXML
    private Tab projectPropertiesTab;
    @FXML
    private Tab taskPropertiesTab;
    @FXML
    private Tab resourcePropertiesTab;

    private IController controller;

    public PropertiesPanelController() {
    }

    public void initTabs() {
        try {
            FXMLLoader project = new FXMLLoader(getClass().getResource("/fxml/ProjectPropertiesPane.fxml"));
            VBox projectProperties = project.load();
            ProjectPropertiesPaneController projectPropertiesController = project.getController();
            projectPropertiesController.setController(controller);
            projectPropertiesController.initFields();
            projectPropertiesTab.setContent(projectProperties);

            FXMLLoader task = new FXMLLoader(getClass().getResource("/fxml/TaskPropertiesPane.fxml"));
            VBox taskProperties = task.load();
            TaskPropertiesPaneController taskPropertiesPaneController = task.getController();
            taskPropertiesPaneController.initFields();
            taskPropertiesTab.setContent(taskProperties);

            FXMLLoader resource = new FXMLLoader(getClass().getResource("/fxml/ResourcePropertiesPane.fxml"));
            VBox resourceProperties = resource.load();
            ResourcePropertiesPaneController resourcePropertiesPaneController = resource.getController();
            resourcePropertiesPaneController.initFields();
            resourcePropertiesTab.setContent(resourceProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void init() {

    }

    public IController getController() {
        return controller;
    }

    public void setController(IController controller) {
        this.controller = controller;
    }
}
