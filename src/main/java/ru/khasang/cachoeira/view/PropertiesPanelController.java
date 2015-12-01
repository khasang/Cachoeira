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
    private UIControl uiControl;

    public PropertiesPanelController() {
    }

    public void initTabs() {
        initProjectPropertiesPane();
        initTaskPropertiesPane();
        initResourcePropertiesPane();
    }

    public void initResourcePropertiesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ResourcePropertiesPane.fxml"));
            VBox resourceProperties = loader.load();
            resourcePropertiesTab.setContent(resourceProperties);

            ResourcePropertiesPaneController resourcePropertiesPaneController = loader.getController();
            resourcePropertiesPaneController.setController(controller);
            resourcePropertiesPaneController.initFields();
            resourcePropertiesPaneController.initTaskTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTaskPropertiesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskPropertiesPane.fxml"));
            VBox taskProperties = loader.load();
            taskPropertiesTab.setContent(taskProperties);

            TaskPropertiesPaneController taskPropertiesPaneController = loader.getController();
            taskPropertiesPaneController.setController(controller);
            taskPropertiesPaneController.initFields();
            taskPropertiesPaneController.initResourceTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initProjectPropertiesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProjectPropertiesPane.fxml"));
            VBox projectProperties = loader.load();
            projectPropertiesTab.setContent(projectProperties);

            ProjectPropertiesPaneController projectPropertiesController = loader.getController();
            projectPropertiesController.setController(controller);
            projectPropertiesController.initFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setController(IController controller) {
        this.controller = controller;
    }

    public void setUIControl(UIControl uiControl) {
        this.uiControl = uiControl;
    }
}
